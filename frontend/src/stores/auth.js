import { defineStore } from 'pinia'
import { login as loginApi } from '../api'

const storageKey = 'sms_user'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem(storageKey) || 'null')
  }),
  getters: {
    isLogin: (state) => Boolean(state.user),
    role: (state) => state.user?.role || '',
    studentId: (state) => state.user?.studentId || null
  },
  actions: {
    async login(payload) {
      const user = await loginApi(payload)
      this.user = user
      localStorage.setItem(storageKey, JSON.stringify(user))
    },
    logout() {
      this.user = null
      localStorage.removeItem(storageKey)
    }
  }
})
