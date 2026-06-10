import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Login from '../views/Login.vue'
import MainLayout from '../components/MainLayout.vue'
import Dashboard from '../views/Dashboard.vue'
import Students from '../views/Students.vue'
import Courses from '../views/Courses.vue'
import Grades from '../views/Grades.vue'
import Announcements from '../views/Announcements.vue'
import Activities from '../views/Activities.vue'
import Payments from '../views/Payments.vue'
import Users from '../views/Users.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: Login },
    {
      path: '/',
      component: MainLayout,
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', component: Dashboard, meta: { title: '首页' } },
        { path: 'students', component: Students, meta: { title: '学生管理' } },
        { path: 'courses', component: Courses, meta: { title: '课程管理' } },
        { path: 'grades', component: Grades, meta: { title: '成绩管理' } },
        { path: 'announcements', component: Announcements, meta: { title: '公告通知' } },
        { path: 'activities', component: Activities, meta: { title: '活动报名' } },
        { path: 'payments', component: Payments, meta: { title: '在线缴费' } },
        { path: 'users', component: Users, meta: { title: '用户管理', roles: ['admin'] } }
      ]
    }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.path !== '/login' && !auth.isLogin) {
    return '/login'
  }
  if (to.path === '/login' && auth.isLogin) {
    return '/dashboard'
  }
  if (to.meta.roles && !to.meta.roles.includes(auth.role)) {
    return '/dashboard'
  }
})

export default router
