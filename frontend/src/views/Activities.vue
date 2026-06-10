<template>
  <div class="page">
    <h1 class="page-title">活动报名</h1>
    <div class="table-panel">
      <div class="toolbar">
        <span class="muted">共 {{ rows.length }} 个活动</span>
        <el-button v-if="isAdmin" type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          发布活动
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="title" label="活动名称" min-width="170" />
        <el-table-column prop="content" label="内容" min-width="220" show-overflow-tooltip />
        <el-table-column prop="location" label="地点" width="130" />
        <el-table-column prop="activityTime" label="时间" width="180" />
        <el-table-column label="人数" width="110">
          <template #default="{ row }">{{ row.registeredCount }} / {{ row.maxCount }}</template>
        </el-table-column>
        <el-table-column label="报名状态" width="110">
          <template #default="{ row }">
            <el-tag v-if="row.registered" type="success">已报名</el-tag>
            <el-tag v-else type="info">未报名</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="auth.role === 'student'" link type="primary" :disabled="row.registered" @click="register(row)">报名</el-button>
            <el-button v-if="isAdmin" link type="primary" @click="openRegs(row)">报名名单</el-button>
            <el-button v-if="isAdmin" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="isAdmin" link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑活动' : '发布活动'" width="620px">
      <el-form class="dialog-form" :model="form" label-width="90px">
        <el-form-item label="活动名称"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="地点"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="时间"><el-date-picker v-model="form.activityTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" /></el-form-item>
        <el-form-item label="人数限制"><el-input-number v-model="form.maxCount" :min="1" style="width: 100%" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="regsVisible" title="报名名单" width="560px">
      <el-table :data="registrations" border>
        <el-table-column prop="studentNo" label="学号" />
        <el-table-column prop="studentName" label="姓名" />
        <el-table-column prop="createdAt" label="报名时间" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createActivity, deleteActivity, getActivities, getActivityRegistrations, registerActivity, updateActivity } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const isAdmin = computed(() => auth.role === 'admin')
const rows = ref([])
const registrations = ref([])
const dialogVisible = ref(false)
const regsVisible = ref(false)
const form = reactive({})

const load = async () => {
  const params = auth.role === 'student' ? { studentId: auth.studentId } : {}
  rows.value = await getActivities(params)
}

const openCreate = () => {
  Object.assign(form, { id: null, title: '', content: '', location: '', activityTime: '', maxCount: 50 })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateActivity(form.id, form)
  } else {
    await createActivity(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const register = async (row) => {
  await registerActivity(row.id, auth.studentId)
  ElMessage.success('报名成功')
  load()
}

const openRegs = async (row) => {
  registrations.value = await getActivityRegistrations(row.id)
  regsVisible.value = true
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除活动 ${row.title}？`, '删除确认', { type: 'warning' })
  await deleteActivity(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
