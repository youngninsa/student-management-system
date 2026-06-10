<template>
  <div class="page">
    <h1 class="page-title">系统用户管理</h1>
    <div class="table-panel">
      <div class="toolbar">
        <span class="muted">共 {{ rows.length }} 个用户</span>
        <el-button type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="password" label="密码" width="120" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">{{ roleText(row.role) }}</template>
        </el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="140" />
        <el-table-column prop="studentId" label="关联学生ID" width="130" />
        <el-table-column prop="createdAt" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" :disabled="row.username === 'admin'" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="560px">
      <el-form class="dialog-form" :model="form" label-width="100px">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="管理员" value="admin" />
            <el-option label="教师" value="teacher" />
            <el-option label="学生" value="student" />
          </el-select>
        </el-form-item>
        <el-form-item label="真实姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="关联学生">
          <el-select v-model="form.studentId" clearable filterable style="width: 100%">
            <el-option v-for="item in students" :key="item.id" :label="`${item.studentNo} - ${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createUser, deleteUser, getStudents, getUsers, updateUser } from '../api'

const rows = ref([])
const students = ref([])
const dialogVisible = ref(false)
const form = reactive({})

const roleText = (role) => ({ admin: '管理员', teacher: '教师', student: '学生' }[role] || role)

const load = async () => {
  rows.value = await getUsers()
}

const loadStudents = async () => {
  students.value = await getStudents({})
}

const openCreate = () => {
  Object.assign(form, { id: null, username: '', password: '123456', role: 'student', realName: '', studentId: null })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateUser(form.id, form)
  } else {
    await createUser(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除用户 ${row.username}？`, '删除确认', { type: 'warning' })
  await deleteUser(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(async () => {
  await Promise.all([load(), loadStudents()])
})
</script>
