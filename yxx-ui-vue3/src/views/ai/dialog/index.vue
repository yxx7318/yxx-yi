<template>
  <div style="height: 100%; margin: 5px">
    <div style="height: 100%; display: flex; justify-content: space-between;">
      <el-container
        style="height: 100%; flex: 2; min-width: 320px; max-width: 320px; background-color: #f3f4f6"
        v-show="!isMobile">
        <el-header style="width: 100%">
          <Welcome title="欢迎来到 YXX 智能体"/>
        </el-header>
        <el-main style="width: 100%">
          <Conversations
            v-model:active="activeMenuKey"
            :items="menuItems"
            :label-max-width="200"
            :show-tooltip="true"
            row-key="id"
            show-to-top-btn
            show-built-in-menu
            show-built-in-menu-type="always"
            @menu-command="handleMenuCommand"
            @change="handleChange"
          />
        </el-main>
        <el-footer style="width: 100%">Footer</el-footer>
      </el-container>
      <div style="height: 100%; flex: 7;">
        <div
          style="height: 95%; margin-top: 5%; display: flex; align-items: center;"
          :class="hasBubble ? 'justifyContentSpaceBetween' : 'justifyContentCenter'">
          <BubbleList v-show="hasBubble" :list="chatList" max-height="70%" style="width: 90%; margin-bottom: 50px">
            <template #content="{ item }">
              <XMarkdown
                :allowHtml="true"
                :enableLatex="true"
                :enableBreaks="true"
                :markdown="item.content"
                default-theme-mode="light"
              />
            </template>
          </BubbleList>
          <div style="width: 90%; margin-bottom: 100px">
            <Typewriter
              v-show="!hasBubble"
              :content="`你好，我是 yxx 智能体`"
              :typing="{
                step: 1,
                interval: 80,
              }"
              style="text-align: center; font-size: 32px; font-weight: 550; letter-spacing: 1px; margin-bottom: 20px"
            >
            </Typewriter>
            <Sender
              style="height: 30%; flex: 1;"
              ref="senderRef"
              v-model="senderValue"
              :auto-size="{
                maxRows: 10,
                minRows: 3,
              }"
              variant="updown"
              clearable
              allow-speech
              @submit="handleSend"
            >
            </Sender>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Welcome, Conversations, Typewriter, Sender, BubbleList, XMarkdown } from 'vue-element-plus-x'
import useUserStore from '@/store/modules/user'
import { startChat, getSessionList, getSession, addSession, updateSession, delSession } from '@/api/ai/session'
import { mobileFlag } from "@/utils/yxx"

const { proxy } = getCurrentInstance()

const userStore = useUserStore()

// ****************************
const isMobile = ref()

function checkScreenSize() {
  // isMobile.value = mobileFlag()
  isMobile.value = true
}

checkScreenSize()

window.addEventListener('resize', checkScreenSize)
onBeforeUnmount(() => {
  window.removeEventListener('resize', checkScreenSize)
})
// ****************************

// id, label, group
const menuItems = ref([
  {
    id: '1',
    label: '今天的会话111111111111111111111111111',
    group: 'today'
  },
  {
    id: '2',
    group: 'today',
    label: '今天的会话2',
    disabled: true
  },
  {
    id: '3',
    group: 'yesterday',
    label: '昨天的会话1'
  },
  {
    id: '4',
    label: '昨天的会话2'
  },
  {
    id: '5',
    label: '一周前的会话'
  },
  {
    id: '6',
    label: '一个月前的会话'
  },
  {
    id: '7',
    label: '很久以前的会话'
  }
])

// 当前选中的会话
const activeMenuKey = ref()

getSession(userStore.id).then(res => {
  for (let i = 0; i < res.data.length; i++) {
    let item = res.data[i]
    if (item.role === "user") {
      chatList.value.push(getFakeItem(1, "user", item.content))
    } else if (item.role === "assistant") {
      chatList.value.push(getFakeItem(2, "ai", item.content))
    }
  }
  if (res.data.length > 0) {
    hasBubble.value = true
  }
})

function handleChange(item) {
  proxy.$modal.msgSuccess(`选中了: ${item.label}`)
}

/**
 * 内置菜单点击事件
 * @param command delete || rename
 * @param item 元素对象 key, label
 */
const handleMenuCommand = (command, item) => {
  console.log('内置菜单点击事件：', command, item)
  // 直接修改 item 是否生效
  if (command === 'delete') {
    const index = menuItems.value.findIndex(
      itemSelf => itemSelf.key === item.key
    )

    if (index !== -1) {
      menuItems.value.splice(index, 1)
      proxy.$modal.msgSuccess('删除成功')
    }
  }

  if (command === 'rename') {
    item.label = '已修改'
    proxy.$modal.msgSuccess('重命名成功')
  }
}

// 当前渲染的会话列表
const chatList = ref([])

// 是否有对话
const hasBubble = ref()

// 输入框绑定的值
const senderValue = ref()

/**
 * 开启会话
 * @param value
 */
const handleSend = (value) => {
  let currentAiText = ref("")
  // 有对话
  hasBubble.value = true
  // 添加对话
  chatList.value.push(getFakeItem(1, "user", value))
  chatList.value.push(getFakeItem(2, "ai", currentAiText))
  // 流式响应
  startChat({
    chatId: userStore.id,
    prompt: value
  }, (chunk) => {
    currentAiText.value = currentAiText.value + chunk
    console.log(chunk)
  })
  // 清除输入框
  proxy.$refs.senderRef.clear()
}

// 生成一个会话结果对象
function getFakeItem(key, role, content) {
  const placement = role === 'ai' ? 'start' : 'end'
  const loading = false
  const shape = 'corner'
  const variant = role === 'ai' ? 'outlined' : 'filled'
  const isMarkdown = true
  const typing = true
  const avatar =
    role === 'ai'
      ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
      : userStore.avatar

  return {
    key, // 唯一标识
    role, // user | ai 根据模型定义
    placement, // start | end 气泡位置
    content, // 消息内容 流式接受的时候，字段需要被vue代理
    loading, // 当前气泡的加载状态
    shape, // 气泡的形状
    variant, // 气泡的样式
    isMarkdown, // 是否渲染为 markdown
    typing, // 是否开启打字器效果 该属性不会和流式接受冲突
    isFog: role === 'ai', // 是否开启打字雾化效果，该效果 v1.1.6 新增，且在 typing 为 true 时生效，该效果会覆盖 typing 的 suffix 属性
    avatar,
    // 控制Bubble组件样式，这里增加最大宽度
    maxWidth: "80%",
    avatarSize: '24px', // 头像占位大小
    avatarGap: '12px' // 头像与气泡之间的距离
  }
}

</script>

<style scoped>
.justifyContentSpaceBetween {
  flex-direction: column;
  justify-content: space-between;
}

.justifyContentCenter {
  justify-content: center;
}
</style>
