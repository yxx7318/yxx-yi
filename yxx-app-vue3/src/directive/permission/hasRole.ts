/**
 * v-hasRole 角色权限处理
 */

import useUserStore from '@/store/modules/user'
import type {Directive} from "vue"

const vHasRole: Directive = {
  mounted(el, binding, vnode) {
    const {value} = binding
    const super_admin = "admin";
    const roles = useUserStore().roles

    if (value && value instanceof Array && value.length > 0) {
      const roleFlag = value

      const hasRole = roles.some(role => {
        return super_admin === role || roleFlag.includes(role)
      })

      if (!hasRole) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`请设置角色权限标签值`)
    }
  }
}

export default vHasRole;
