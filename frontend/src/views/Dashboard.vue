<template>
  <div class="page dashboard-page">
    <section class="welcome-band">
      <div class="welcome-copy">
        <div class="eyebrow">综合管理看板</div>
        <h1>{{ greeting }}，{{ auth.user?.realName || auth.user?.username }}</h1>
        <p>当前身份：{{ roleLabel }}。从这里快速进入学生、课程、成绩、公告、活动和缴费业务。</p>
      </div>
      <div class="welcome-actions">
        <el-button type="primary" @click="$router.push('/students')">
          <el-icon><User /></el-icon>
          学生信息
        </el-button>
        <el-button plain @click="$router.push('/announcements')">
          <el-icon><Bell /></el-icon>
          公告通知
        </el-button>
      </div>
    </section>

    <div class="stats-grid">
      <div v-for="item in statsCards" :key="item.label" class="stat-card">
        <div class="stat-top">
          <div class="stat-icon" :style="{ background: item.color }">
            <el-icon><component :is="item.icon" /></el-icon>
          </div>
          <el-tag effect="plain" :type="item.tagType">{{ item.tag }}</el-tag>
        </div>
        <div class="stat-value">{{ item.value }}</div>
        <div class="stat-label">{{ item.label }}</div>
      </div>
    </div>

    <div class="dashboard-grid">
      <div class="panel notice-panel">
        <div class="panel-head">
          <div>
            <h2>最近公告</h2>
            <p>按发布时间倒序展示最新通知</p>
          </div>
          <el-button link type="primary" @click="$router.push('/announcements')">查看全部</el-button>
        </div>
        <el-empty v-if="latest.length === 0" description="暂无公告" />
        <div v-else class="notice-list">
          <div v-for="item in latest" :key="item.id" class="notice-item">
            <div class="notice-date">{{ formatDate(item.createdAt) }}</div>
            <div class="notice-content">
              <div class="notice-title">{{ item.title }}</div>
              <div class="notice-meta">{{ item.publisher || '系统' }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="panel quick-panel">
        <div class="panel-head">
          <div>
            <h2>快捷入口</h2>
            <p>根据当前角色显示可访问功能</p>
          </div>
        </div>
        <div class="quick-grid">
          <button v-for="item in quickMenus" :key="item.path" class="quick-button" type="button" @click="$router.push(item.path)">
            <span class="quick-icon">
              <el-icon><component :is="item.icon" /></el-icon>
            </span>
            <span>{{ item.title }}</span>
            <el-icon class="quick-arrow"><ArrowRight /></el-icon>
          </button>
        </div>
      </div>
    </div>

    <div class="overview-grid">
      <div class="panel">
        <div class="panel-head">
          <div>
            <h2>业务概览</h2>
            <p>系统数据</p>
          </div>
        </div>
        <div class="overview-list">
          <div v-for="item in overviewItems" :key="item.label" class="overview-item">
            <div class="overview-title">
              <span>{{ item.label }}</span>
              <strong>{{ item.value }}</strong>
            </div>
            <el-progress :percentage="item.percent" :stroke-width="9" :show-text="false" />
          </div>
        </div>
      </div>

      <div class="panel role-panel">
        <div class="role-card">
          <div class="role-icon">
            <el-icon><Avatar /></el-icon>
          </div>
          <div>
            <h2>{{ roleLabel }}</h2>
            <p>{{ roleDescription }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { getDashboardStats } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const stats = ref({})
const latest = ref([])

const roleLabel = computed(() => ({ admin: '管理员', teacher: '教师', student: '学生' }[auth.role] || auth.role))
const roleDescription = computed(() => ({
  admin: '拥有完整管理权限，可维护学生、课程、公告、活动、缴费和系统用户。',
  teacher: '可查看学生与课程信息，维护成绩并发布公告。',
  student: '可查看个人信息、成绩公告，完成活动报名和模拟缴费。'
}[auth.role] || '可使用系统分配的业务功能。'))

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 11) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const statsCards = computed(() => [
  { label: '学生数量', value: stats.value.studentCount || 0, icon: 'User', color: 'linear-gradient(135deg, #38bdf8, #2563eb)', tag: '基础档案', tagType: 'primary' },
  { label: '课程数量', value: stats.value.courseCount || 0, icon: 'Reading', color: 'linear-gradient(135deg, #34d399, #10b981)', tag: '教学资源', tagType: 'success' },
  { label: '公告数量', value: stats.value.announcementCount || 0, icon: 'Bell', color: 'linear-gradient(135deg, #fb923c, #f97316)', tag: '通知发布', tagType: 'warning' },
  { label: '活动数量', value: stats.value.activityCount || 0, icon: 'Calendar', color: 'linear-gradient(135deg, #a78bfa, #6366f1)', tag: '校园事务', tagType: 'info' }
])

const quickMenus = computed(() => {
  const items = [
    { title: '学生管理', path: '/students', icon: 'User' },
    { title: '课程管理', path: '/courses', icon: 'Reading' },
    { title: '成绩管理', path: '/grades', icon: 'TrendCharts' },
    { title: '公告通知', path: '/announcements', icon: 'Bell' },
    { title: '活动报名', path: '/activities', icon: 'Calendar' }
  ]
  if (auth.role !== 'teacher') {
    items.push({ title: '在线缴费', path: '/payments', icon: 'Wallet' })
  }
  if (auth.role === 'admin') {
    items.push({ title: '用户管理', path: '/users', icon: 'Setting' })
  }
  return items
})

const overviewItems = computed(() => {
  const studentCount = Number(stats.value.studentCount || 0)
  const courseCount = Number(stats.value.courseCount || 0)
  const announcementCount = Number(stats.value.announcementCount || 0)
  const activityCount = Number(stats.value.activityCount || 0)
  const max = Math.max(studentCount, courseCount, announcementCount, activityCount, 1)
  return [
    { label: '学生档案覆盖', value: `${studentCount} 人`, percent: Math.round((studentCount / max) * 100) },
    { label: '课程资源维护', value: `${courseCount} 门`, percent: Math.round((courseCount / max) * 100) },
    { label: '公告通知发布', value: `${announcementCount} 条`, percent: Math.round((announcementCount / max) * 100) },
    { label: '校园活动发布', value: `${activityCount} 个`, percent: Math.round((activityCount / max) * 100) }
  ]
})

const formatDate = (value) => {
  if (!value) return '-'
  return String(value).replace('T', ' ').slice(0, 16)
}

onMounted(async () => {
  stats.value = await getDashboardStats()
  latest.value = stats.value.latestAnnouncements || []
})
</script>

<style scoped>
.dashboard-page {
  display: grid;
  gap: 16px;
}

.welcome-band {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  min-height: 164px;
  overflow: hidden;
  padding: 28px;
  color: #0f172a;
  background:
    radial-gradient(circle at 8% 20%, rgba(34, 197, 94, 0.24), transparent 28%),
    radial-gradient(circle at 85% 20%, rgba(56, 189, 248, 0.28), transparent 25%),
    linear-gradient(135deg, #ecfeff, #f0fdf4 55%, #fff7ed);
  border: 1px solid #bae6fd;
  border-radius: 8px;
  box-shadow: 0 18px 42px rgba(14, 165, 233, 0.13);
  animation: riseIn 0.48s ease both;
}

.welcome-band::after {
  content: "";
  position: absolute;
  right: 34%;
  top: 24px;
  width: 78px;
  height: 78px;
  background: #fed7aa;
  border-radius: 999px;
  opacity: 0.7;
  animation: floatBubble 5s ease-in-out infinite;
}

.welcome-copy {
  position: relative;
  z-index: 1;
}

.eyebrow {
  margin-bottom: 10px;
  color: #0284c7;
  font-size: 13px;
  font-weight: 800;
}

.welcome-band h1 {
  margin: 0;
  font-size: 31px;
  line-height: 1.25;
}

.welcome-band p {
  margin: 12px 0 0;
  max-width: 650px;
  color: #475569;
  line-height: 1.7;
}

.welcome-actions {
  position: relative;
  z-index: 1;
  display: flex;
  gap: 10px;
  flex-shrink: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  min-height: 145px;
  padding: 18px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid #e0f2fe;
  border-radius: 8px;
  box-shadow: 0 12px 28px rgba(14, 165, 233, 0.08);
  transition: transform 0.18s ease, box-shadow 0.18s ease;
  animation: popIn 0.45s ease both;
}

.stat-card:nth-child(2) {
  animation-delay: 0.06s;
}

.stat-card:nth-child(3) {
  animation-delay: 0.12s;
}

.stat-card:nth-child(4) {
  animation-delay: 0.18s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 18px 36px rgba(14, 165, 233, 0.14);
}

.stat-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18px;
}

.stat-icon {
  display: grid;
  place-items: center;
  width: 46px;
  height: 46px;
  color: #fff;
  border-radius: 8px;
  font-size: 22px;
}

.stat-value {
  color: #0f172a;
  font-size: 34px;
  line-height: 1;
  font-weight: 800;
}

.stat-label {
  margin-top: 8px;
  color: #64748b;
}

.dashboard-grid,
.overview-grid {
  display: grid;
  grid-template-columns: 1.25fr 1fr;
  gap: 16px;
}

.panel {
  padding: 20px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid #e0f2fe;
  border-radius: 8px;
  box-shadow: 0 12px 28px rgba(14, 165, 233, 0.08);
  animation: riseIn 0.5s ease both;
}

.panel-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 16px;
}

.panel-head h2 {
  margin: 0;
  color: #0f172a;
  font-size: 18px;
}

.panel-head p {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 13px;
}

.notice-list {
  display: grid;
  gap: 12px;
}

.notice-item {
  display: grid;
  grid-template-columns: 118px 1fr;
  gap: 14px;
  padding: 14px;
  background: #f8fafc;
  border: 1px solid #dbeafe;
  border-radius: 8px;
  transition: transform 0.18s ease, background 0.18s ease;
}

.notice-item:hover {
  background: #ecfeff;
  transform: translateX(3px);
}

.notice-date {
  color: #0284c7;
  font-weight: 800;
  font-size: 13px;
}

.notice-title {
  margin-bottom: 6px;
  color: #0f172a;
  font-weight: 800;
}

.notice-meta {
  color: #64748b;
  font-size: 13px;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.quick-button {
  display: grid;
  grid-template-columns: 34px 1fr 18px;
  align-items: center;
  gap: 10px;
  min-height: 54px;
  padding: 0 14px;
  color: #334155;
  text-align: left;
  background: #f8fafc;
  border: 1px solid #dbeafe;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.18s ease, box-shadow 0.18s ease, background 0.18s ease;
}

.quick-button:hover {
  color: #0284c7;
  background: #ecfeff;
  box-shadow: 0 10px 24px rgba(14, 165, 233, 0.12);
  transform: translateY(-2px);
}

.quick-icon {
  display: grid;
  place-items: center;
  width: 34px;
  height: 34px;
  color: #0284c7;
  background: #e0f2fe;
  border-radius: 8px;
}

.quick-arrow {
  color: #94a3b8;
}

.overview-list {
  display: grid;
  gap: 16px;
}

.overview-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  color: #334155;
}

.overview-title strong {
  color: #0f172a;
}

.role-panel {
  display: flex;
  align-items: stretch;
}

.role-card {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
  padding: 18px;
  color: #0f172a;
  background: linear-gradient(135deg, #f0fdfa, #eff6ff);
  border: 1px solid #bae6fd;
  border-radius: 8px;
}

.role-icon {
  display: grid;
  place-items: center;
  width: 58px;
  height: 58px;
  color: #fff;
  background: linear-gradient(135deg, #34d399, #38bdf8);
  border-radius: 8px;
  font-size: 26px;
  animation: floatBubble 4s ease-in-out infinite;
}

.role-card h2 {
  margin: 0 0 8px;
  font-size: 22px;
}

.role-card p {
  margin: 0;
  color: #475569;
  line-height: 1.7;
}

@keyframes riseIn {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes popIn {
  from {
    opacity: 0;
    transform: translateY(14px) scale(0.98);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes floatBubble {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-8px);
  }
}
</style>
