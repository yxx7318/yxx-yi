<template>
  <div style="height: 100%; margin: 5px" @dragenter="isFilesUpload = true" @dragleave="handleDragLeave($event, () => isFilesUpload = false)">
    <div style="height: 100%; display: flex; align-items: center; justify-content: center" v-show="isFilesUpload">
      <drag-upload
        ref="dragUpload"
        :fileType="['pdf']"
        style="height: 92%; width: 80%"
        v-model:modelValue="filesValue"
        @uploadedSuccess="uploadedSuccess"
      >
      </drag-upload>
    </div>
    <!-- 侧边栏抽屉 -->
    <el-drawer
      v-model="drawer"
      title="会话历史"
      direction="ltr"
    >
      <!-- 侧边栏内容 -->
      <el-container
        style="height: 100%; flex: 2; min-width: 320px; max-width: 320px; background-color: #f3f4f6">
        <el-header style="width: 100%; display: flex; align-items: center;">
          <el-button style="width: 100%; height: 70%;" @click="newConversation">
            <div style="font-size: 16px">新建会话</div>
          </el-button>
        </el-header>
        <el-main style="width: 100%">
          <Conversations
            v-model:active="activeMenuKey"
            :items="conversationList"
            :label-max-width="200"
            :show-tooltip="true"
            row-key="id"
            show-to-top-btn
            show-built-in-menu
            show-built-in-menu-type="always"
            @menu-command="handleConversationListCommand"
            @change="handleConversationChange"
          />
        </el-main>
        <el-footer style="width: 100%"></el-footer>
      </el-container>
    </el-drawer>
    <div style="height: 100%; display: flex; justify-content: space-between;" v-show="!isFilesUpload">
      <div style="height: 100%; flex: 7; max-width: 100%;">
        <el-button style="height: 5%; margin-left: 2%;" @click="drawer = true">
          会话列表
          <template #icon>
            <svg-icon icon-class="conversation-list" />
          </template>
        </el-button>
        <div
          style="height: 90%; margin-top: 5%; display: flex; align-items: center;"
          :class="hasBubble ? 'justifyContentSpaceBetween' : 'justifyContentCenter'">
          <BubbleList v-show="hasBubble" :list="chatList" max-height="70%" style="width: 90%; margin-bottom: 50px">
            <template #content="{ item }">
              <!-- 文件列表 -->
              <div style="display: flex; flex-wrap: wrap; gap: 12px" v-if="item.role === 'user'">
                <div v-for="(i, index) in item.files" :key="index" style="flex: 1">
                  <el-tooltip
                    placement="top"
                    :content="i.name"
                  >
                    <a :href="i.url" target="_blank">
                      <files-card
                        :uid="i.uid"
                        :name="i.name"
                      >
                      </files-card>
                    </a>
                  </el-tooltip>
                </div>
              </div>
              <!-- md格式解析 -->
              <XMarkdown
                :allowHtml="true"
                :enableLatex="true"
                :enableBreaks="true"
                :markdown="item.content"
                default-theme-mode="light"
              />
            </template>
            <template #header="{ item }">
              <el-text>{{ item.role === 'ai' ? 'YXX智能体' : userStore.nickName }}</el-text>
            </template>
          </BubbleList>
          <div style="width: 90%; margin-bottom: 4%">
            <Typewriter
              v-show="!hasBubble"
              :content="`你好，我是 YXX 智能体`"
              :typing="{
                step: 1,
                interval: 80,
              }"
              style="text-align: center; font-size: 32px; font-weight: 550; letter-spacing: 1px; margin-bottom: 20px"
            >
            </Typewriter>
            <Sender
              style="height: 30%; flex: 1; width: 100%"
              ref="senderRef"
              v-model="senderValue"
              :auto-size="{
                maxRows: 10,
                minRows: 5,
              }"
              variant="updown"
              clearable
              allow-speech
              @submit="handleSend"
            >
              <template #header>
                <div style="display: flex; overflow-x: auto; gap: 20px; flex-wrap: nowrap; padding: 20px;">
                  <div v-for="(item, index) in filesValue" :key="index">
                    <el-tooltip
                      placement="top"
                      :content="item.name"
                    >
                      <a :href="item.url" target="_blank">
                        <files-card
                          :uid="item.uid"
                          :name="item.name"
                          show-del-icon
                          @delete="deleteCard(item.uid)"
                        >
                        </files-card>
                      </a>
                    </el-tooltip>
                  </div>

                </div>
              </template>

              <template #prefix>
                <div
                  style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap; height: 100%"
                >
                  <!-- 文件选择按钮 -->
                  <el-button round plain color="#626aef" @click="clickSelectFile">
                    <el-icon><Paperclip /></el-icon>
                  </el-button>

                  <!-- 深度思考按钮 -->
                  <div
                    :class="{ isSelect }"
                    class="noSelect"
                    @click="isSelect = !isSelect"
                  >
                    <el-icon><ElementPlus /></el-icon>
                    <span>深度思考</span>
                  </div>
                </div>
              </template>

              <template #action-list>
                <div style="display: flex; align-items: center; gap: 8px">
                  <el-button round color="#626aef" @click="handleSend">
                    <el-icon><Promotion /></el-icon>
                  </el-button>
                </div>
              </template>
            </Sender>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Welcome, Conversations, Typewriter, Sender, BubbleList, XMarkdown, FilesCard } from 'vue-element-plus-x'
import { ElementPlus, Paperclip, Promotion } from '@element-plus/icons-vue'
import DragUpload from '@/components/DragUpload'
import chatGpt from '@/assets/icons/svg/chat-gpt.svg'
import useUserStore from '@/store/modules/user'
import { content } from '@/utils/sse'
import { startChatSSE, getSessionList, getSession, addSessionHistory, addSession, updateSession, delSession } from '@/api/ai/session'
import { handleDragLeave } from "@/utils/handleDrag"

const { proxy } = getCurrentInstance()

const userStore = useUserStore()

// 会话id
const conversationId = ref("")

// 是否当前会话是第一次发起
const firstConversation = ref(true)

// 是否上转文件状态
const isFilesUpload = ref(false)

// 文件内容值
const filesValue = ref([])

// 点击的方式选择文件
const clickSelectFile = () => {
  proxy.$refs.dragUpload.triggerFileSelect()
}

// 删除卡片
const deleteCard = (uid) => {
  filesValue.value = filesValue.value.filter(i => uid !== i.uid)
  refreshSenderHeader()
}

// 刷新输入框头打开状态
const refreshSenderHeader = () => {
  if (filesValue.value.length === 0) {
    proxy.$refs.senderRef.closeHeader()
  } else {
    proxy.$refs.senderRef.openHeader()
  }
}

// 上传成功回调
const uploadedSuccess = () => {
  isFilesUpload.value = false
  refreshSenderHeader()
}

onMounted(() => {
  // 完成挂载刷新
  refreshSenderHeader()
  // 获取对话列表
  getConversationList()
})

// 聊天记录侧面栏
const drawer = ref(false)

// 会话历史信息
const conversationList = ref([])

// 获取所有的历史会话
function getConversationList() {
  getSessionList().then(res => {
    // id, label, group
    res.data.forEach(i => conversationList.value.push({
      id: String(i.chatConversationId),
      group: i.chatGroup,
      label: i.chatTitle,
    }))
  })
}

// 当前选中的会话
const activeMenuKey = ref()

// 获取指定的会话
function getConversation(conversationId) {
  firstConversation.value = false
  getSession(conversationId).then(res => {
    chatList.value = []
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
}

// 新建会话
function newConversation() {
  // 清理相关数据
  chatList.value = []
  hasBubble.value = false
  drawer.value = false
  filesValue.value = []
  firstConversation.value = true
}

// 选中会话
function handleConversationChange(item) {
  proxy.$modal.msgSuccess(`切换到对应会话${JSON.stringify(item.label)}`)
  getConversation(item.id)
}

/**
 * 内置菜单点击事件
 * @param command delete || rename
 * @param item 元素对象 key, label
 */
const handleConversationListCommand = (command, item) => {
  console.log('内置菜单点击事件：', command, item)
  // 直接修改 item 是否生效
  if (command === 'delete') {
    const index = conversationList.value.findIndex(
      itemSelf => itemSelf.key === item.key
    )

    if (index !== -1) {
      conversationList.value.splice(index, 1)
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

// 是否有会话
const hasBubble = ref()

// 输入框绑定的值
const senderValue = ref()

// 是否选中深度思考
const isSelect = ref(false)

// 上一个对话内容，防止SSE的content值为刷新导致显示问题
let preContent = ""

/**
 * 开启会话
 * @param userContent
 */
const handleSend = async (userContent) => {
  // 是否第一次
  if (firstConversation.value) {
    // 保存新的历史会话
    let res = await addSessionHistory({
      chatGroup: "",
      chatTitle: userContent.substring(0, 20),
    })
    // 获取到会话ID
    conversationId.value = String(res.data.chatConversationId)
    // 当前历史会话增加
    conversationList.value.unshift({
      id: conversationId.value,
      label: userContent.substring(0, 20),
    })
    firstConversation.value = false
  }

  // 当前AI会话内容
  let currentAiText = ref("")

  // 当前AI会话附件
  let files = filesValue.value

  // 有会话
  hasBubble.value = true
  // 添加会话
  chatList.value.push(getFakeItem(new Date().getTime(), "user", userContent))
  chatList.value.push(getFakeItem(new Date().getTime() + 1, "ai", currentAiText))

  // 当SSE的content变化时自动更新
  const stopWatchEffect = watchEffect(() => {
    if (preContent !== content.value) {
      currentAiText.value = content.value
    }
  })

  // 清除操作
  proxy.$refs.senderRef.clear()
  filesValue.value = []
  refreshSenderHeader()

  // SSE请求
  await startChatSSE({
    conversationId: conversationId.value,
    prompt: userContent,
    files: files
  })

  // 设置为上一个会话内容
  preContent = currentAiText.value
  // 停止监听
  stopWatchEffect()
}

// 生成一个会话结果对象
function getFakeItem(key, role, content) {
  const placement = role === 'ai' ? 'start' : 'end'
  const loading = false
  const shape = 'corner'
  const variant = role === 'ai' ? 'outlined' : 'filled'
  const isMarkdown = true
  const typing = true
  const avatar = role === 'ai' ? chatGpt : userStore.avatar

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
    avatarGap: '12px', // 头像与气泡之间的距离
    // 传递福建信息
    files: filesValue.value.map(i => {
      return {
        uid: i.uid,
        name: i.name,
        url: i.url
      }
    })
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

/* 深度思考未选中状态样式 */
.noSelect {
  display: flex;
  height: 100%;
  align-items: center;
  gap: 4px;
  padding: 2px 12px;
  border: 1px solid silver;
  border-radius: 15px;
  cursor: pointer;
  font-size: 12px;
}

/* 深度思考选中后增加的样式 */
.isSelect {
  color: #626aef;
  border: 1px solid #626aef !important;
  border-radius: 15px;
  padding: 3px 12px;
  font-weight: 700;
}
</style>
