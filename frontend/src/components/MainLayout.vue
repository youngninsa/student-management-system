<template>
  <el-container class="layout">
    <el-aside width="238px" class="aside">
      <div class="brand">
        <div class="brand-mark">S</div>
        <div>
          <div class="brand-title">高校学生综合管理系统</div>
          <div class="brand-subtitle">Bright Campus Console</div>
        </div>
      </div>

      <el-menu :default-active="$route.path" router class="menu">
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>

      <div class="aside-tip">
        <div class="tip-icon">
          <el-icon><Sunny /></el-icon>
        </div>
        <div>
          <strong>课程作业演示</strong>
          <span>前后端联动 · MySQL 持久化</span>
        </div>
      </div>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div>
          <div class="header-title">{{ route.meta.title || '首页' }}</div>
          <div class="header-subtitle">轻量级学生综合管理平台</div>
        </div>
        <div class="user-box">
          <el-tag class="role-tag">{{ roleLabel }}</el-tag>
          <span class="user-name">{{ auth.user?.realName || auth.user?.username }}</span>
          <el-button type="primary" plain @click="logout">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const allMenus = [
  { title: '首页', path: '/dashboard', icon: 'DataBoard', roles: ['admin', 'teacher', 'student'] },
  { title: '学生管理', path: '/students', icon: 'User', roles: ['admin', 'teacher', 'student'] },
  { title: '课程管理', path: '/courses', icon: 'Reading', roles: ['admin', 'teacher', 'student'] },
  { title: '成绩管理', path: '/grades', icon: 'TrendCharts', roles: ['admin', 'teacher', 'student'] },
  { title: '公告通知', path: '/announcements', icon: 'Bell', roles: ['admin', 'teacher', 'student'] },
  { title: '活动报名', path: '/activities', icon: 'Calendar', roles: ['admin', 'teacher', 'student'] },
  { title: '在线缴费', path: '/payments', icon: 'Wallet', roles: ['admin', 'student'] },
  { title: '用户管理', path: '/users', icon: 'Setting', roles: ['admin'] }
]

const menus = computed(() => allMenus.filter((item) => item.roles.includes(auth.role)))
const roleLabel = computed(() => ({ admin: '管理员', teacher: '教师', student: '学生' }[auth.role] || auth.role))

const logout = () => {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: #f6f9ff;
}

.aside {
  display: flex;
  flex-direction: column;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.98), rgba(240, 249, 255, 0.95)),
    radial-gradient(circle at 30px 60px, rgba(56, 189, 248, 0.18), transparent 35%),
    radial-gradient(circle at 210px 260px, rgba(52, 211, 153, 0.18), transparent 30%);
  border-right: 1px solid #dbeafe;
  box-shadow: 10px 0 30px rgba(59, 130, 246, 0.08);
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 82px;
  padding: 0 18px;
}

.brand-mark {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  color: #fff;
  background: linear-gradient(135deg, #22c55e, #38bdf8);
  border-radius: 8px;
  font-size: 22px;
  font-weight: 800;
  box-shadow: 0 12px 24px rgba(14, 165, 233, 0.24);
  animation: floatLogo 3.6s ease-in-out infinite;
}

.brand-title {
  color: #0f172a;
  font-size: 15px;
  font-weight: 800;
}

.brand-subtitle {
  margin-top: 4px;
  color: #0ea5e9;
  font-size: 12px;
  font-weight: 700;
}

.menu {
  flex: 1;
  padding: 8px 12px;
  background: transparent;
  border-right: none;
}

:deep(.el-menu-item) {
  height: 46px;
  margin: 5px 0;
  color: #475569;
  border-radius: 8px;
  transition: transform 0.18s ease, background 0.18s ease, color 0.18s ease;
}

:deep(.el-menu-item:hover) {
  color: #0284c7;
  background: #e0f2fe;
  transform: translateX(3px);
}

:deep(.el-menu-item.is-active) {
  color: #0369a1;
  background: linear-gradient(90deg, #bae6fd, #dcfce7);
  font-weight: 800;
  box-shadow: 0 8px 20px rgba(14, 165, 233, 0.14);
}

.aside-tip {
  display: flex;
  gap: 10px;
  align-items: center;
  margin: 14px;
  padding: 12px;
  color: #0f172a;
  background: rgba(255, 255, 255, 0.78);
  border: 1px solid #bfdbfe;
  border-radius: 8px;
}

.tip-icon {
  display: grid;
  place-items: center;
  width: 34px;
  height: 34px;
  color: #f97316;
  background: #ffedd5;
  border-radius: 8px;
}

.aside-tip strong,
.aside-tip span {
  display: block;
}

.aside-tip strong {
  font-size: 13px;
}

.aside-tip span {
  margin-top: 3px;
  color: #64748b;
  font-size: 12px;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 76px;
  background: rgba(255, 255, 255, 0.86);
  border-bottom: 1px solid #e0f2fe;
  backdrop-filter: blur(12px);
}

.header-title {
  color: #0f172a;
  font-size: 19px;
  font-weight: 800;
}

.header-subtitle {
  margin-top: 5px;
  color: #64748b;
  font-size: 13px;
}

.user-box {
  display: flex;
  align-items: center;
  gap: 12px;
}

.role-tag {
  color: #0369a1;
  background: #e0f2fe;
  border-color: #bae6fd;
}

.user-name {
  color: #334155;
  font-weight: 700;
}

.main {
  padding: 0;
  background:
    linear-gradient(rgba(125, 211, 252, 0.16) 1px, transparent 1px),
    linear-gradient(90deg, rgba(125, 211, 252, 0.16) 1px, transparent 1px),
    #f6f9ff;
  background-size: 34px 34px, 34px 34px, auto;
}

@keyframes floatLogo {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-3px);
  }
}
</style>
