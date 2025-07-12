import { login, logout, getInfo } from "@/api/login"
import { getToken, setToken, removeToken } from "@/utils/auth"
import defAva from "@/static/images/profile.jpg"
import { defineStore } from "pinia"

export interface LoginForm {
  username: string
  password: string
  code: string
  uuid: string
}

const useUserStore = defineStore("user", {
  state: () => ({
    token: getToken(),
    name: "",
    nickName: "",
    avatar: "",
    roles: Array(),
    permissions: [],
  }),
  actions: {
    // 登录
    login(userInfo: LoginForm) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid)
          .then((res: any) => {
            setToken(res.token)
            this.token = res.token
            resolve(null)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    // 获取用户信息
    getInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res: any) => {
            const user = res.user
            const avatar =
              user.avatar == "" || user.avatar == null
                ? defAva
                : import.meta.env.VITE_APP_BASE_API + user.avatar

            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
            } else {
              this.roles = ["ROLE_DEFAULT"]
            }
            this.name = user.userName
            this.nickName = user.nickName
            this.avatar = avatar
            resolve(res)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    // 退出系统
    logOut() {
      return new Promise<null>((resolve, reject) => {
        logout()
          .then(() => {
            this.token = ""
            this.roles = []
            this.permissions = []
            this.name = ""
            this.nickName = ""
            this.avatar = ""
            removeToken()
            resolve(null)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
  },
})

export default useUserStore
