/**
 * v-hasPermi 操作权限处理
 */

import useUserStore from '@/store/modules/user'

import type {Directive} from "vue";

const vHasPermi: Directive = {
  mounted(el, binding, vnode) {
    const {value} = binding
    const all_permission = "*:*:*";
    const permissions = useUserStore().permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = permissions.some(permission => {
        return all_permission === permission || permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`请设置操作权限标签值`)
    }
  }
}
export default vHasPermi
