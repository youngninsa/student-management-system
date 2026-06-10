<template>
  <div class="login-page">
    <div class="bubble bubble-a"></div>
    <div class="bubble bubble-b"></div>
    <div class="bubble bubble-c"></div>

    <section class="login-shell">
      <div class="login-showcase">
        <div class="system-badge">
          <el-icon><School /></el-icon>
          高校智慧校园
        </div>
        <div class="showcase-copy">
          <h1>高校学生综合管理系统</h1>
          <p>让学生档案、课程成绩、活动缴费在一个清爽的平台里完成联动。</p>
        </div>
        <div class="showcase-board">
          <div class="board-row">
            <span>学生信息</span>
            <strong>统一维护</strong>
          </div>
          <div class="board-row">
            <span>课程成绩</span>
            <strong>实时查询</strong>
          </div>
          <div class="board-row">
            <span>活动缴费</span>
            <strong>在线办理</strong>
          </div>
        </div>
      </div>

      <div class="login-card">
        <div class="login-head">
          <div class="logo">SMS</div>
          <div>
            <h2>欢迎登录</h2>
            <p>选择测试账号，快速进入演示系统</p>
          </div>
        </div>

        <el-form :model="form" size="large" @keyup.enter="submit">
          <el-form-item>
            <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" placeholder="密码" prefix-icon="Lock" show-password />
          </el-form-item>
          <el-button type="primary" class="login-button" :loading="loading" @click="submit">
            登录系统
          </el-button>
        </el-form>

        <div class="accounts">
          <button type="button" @click="fillAccount('admin')">
            <span>管理员</span>
            <strong>admin / 123456</strong>
          </button>
          <button type="button" @click="fillAccount('teacher')">
            <span>教师</span>
            <strong>teacher / 123456</strong>
          </button>
          <button type="button" @click="fillAccount('student')">
            <span>学生</span>
            <strong>student / 123456</strong>
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({
  username: 'admin',
  password: '123456'
})

const fillAccount = (username) => {
  form.username = username
  form.password = '123456'
}

const submit = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await auth.login(form)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  position: relative;
  display: grid;
  place-items: center;
  min-height: 100vh;
  padding: 42px;
  overflow: hidden;
  background:
    linear-gradient(rgba(56, 189, 248, 0.11) 1px, transparent 1px),
    linear-gradient(90deg, rgba(56, 189, 248, 0.11) 1px, transparent 1px),
    linear-gradient(135deg, #f0f9ff 0%, #f7fee7 48%, #fff7ed 100%);
  background-size: 38px 38px, 38px 38px, auto;
}

.bubble {
  position: absolute;
  border-radius: 999px;
  filter: blur(2px);
  opacity: 0.7;
  animation: drift 8s ease-in-out infinite;
}

.bubble-a {
  width: 180px;
  height: 180px;
  left: 7%;
  top: 12%;
  background: #bae6fd;
}

.bubble-b {
  width: 150px;
  height: 150px;
  right: 10%;
  top: 18%;
  background: #bbf7d0;
  animation-delay: -2s;
}

.bubble-c {
  width: 120px;
  height: 120px;
  left: 18%;
  bottom: 10%;
  background: #fed7aa;
  animation-delay: -4s;
}

.login-shell {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 1.05fr 430px;
  width: min(1040px, 100%);
  min-height: 590px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.88);
  border: 1px solid rgba(186, 230, 253, 0.95);
  border-radius: 8px;
  box-shadow: 0 28px 70px rgba(14, 165, 233, 0.18);
  backdrop-filter: blur(18px);
  animation: riseIn 0.56s ease both;
}

.login-showcase {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 44px;
  color: #0f172a;
  background:
    radial-gradient(circle at 22% 18%, rgba(34, 197, 94, 0.26), transparent 28%),
    radial-gradient(circle at 80% 30%, rgba(56, 189, 248, 0.28), transparent 26%),
    linear-gradient(135deg, #ecfeff, #f0fdf4 55%, #fff7ed);
}

.system-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  width: fit-content;
  padding: 8px 12px;
  color: #0369a1;
  background: rgba(255, 255, 255, 0.78);
  border: 1px solid #bae6fd;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 700;
}

.showcase-copy h1 {
  margin: 0;
  max-width: 520px;
  color: #0f172a;
  font-size: 42px;
  line-height: 1.18;
  font-weight: 800;
}

.showcase-copy p {
  margin: 18px 0 0;
  max-width: 440px;
  color: #475569;
  font-size: 17px;
  line-height: 1.7;
}

.showcase-board {
  display: grid;
  gap: 12px;
  max-width: 430px;
}

.board-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 16px;
  background: rgba(255, 255, 255, 0.74);
  border: 1px solid rgba(125, 211, 252, 0.75);
  border-radius: 8px;
  box-shadow: 0 12px 28px rgba(14, 165, 233, 0.1);
  animation: slideSoft 0.55s ease both;
}

.board-row:nth-child(2) {
  animation-delay: 0.08s;
}

.board-row:nth-child(3) {
  animation-delay: 0.16s;
}

.board-row span {
  color: #64748b;
}

.board-row strong {
  color: #0284c7;
}

.login-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 42px;
  background: rgba(255, 255, 255, 0.94);
}

.login-head {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 30px;
}

.logo {
  display: grid;
  place-items: center;
  width: 58px;
  height: 58px;
  color: #fff;
  background: linear-gradient(135deg, #22c55e, #38bdf8);
  border-radius: 8px;
  font-size: 18px;
  font-weight: 800;
  box-shadow: 0 14px 30px rgba(14, 165, 233, 0.24);
  animation: floatLogo 3.8s ease-in-out infinite;
}

h2 {
  margin: 0;
  color: #0f172a;
  font-size: 26px;
}

p {
  margin: 7px 0 0;
  color: #64748b;
}

.login-button {
  width: 100%;
  height: 44px;
  margin-top: 4px;
  font-weight: 800;
}

.accounts {
  display: grid;
  gap: 10px;
  margin-top: 24px;
}

.accounts button {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  min-height: 46px;
  padding: 0 14px;
  color: #334155;
  background: #f8fafc;
  border: 1px solid #dbeafe;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.18s ease, box-shadow 0.18s ease, background 0.18s ease;
}

.accounts button:hover {
  color: #0284c7;
  background: #ecfeff;
  box-shadow: 0 10px 22px rgba(14, 165, 233, 0.12);
  transform: translateY(-2px);
}

.accounts span {
  font-size: 13px;
}

.accounts strong {
  font-size: 13px;
}

@keyframes riseIn {
  from {
    opacity: 0;
    transform: translateY(18px) scale(0.98);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes slideSoft {
  from {
    opacity: 0;
    transform: translateX(-14px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes drift {
  0%,
  100% {
    transform: translate3d(0, 0, 0);
  }
  50% {
    transform: translate3d(16px, -18px, 0);
  }
}

@keyframes floatLogo {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-4px);
  }
}
</style>
