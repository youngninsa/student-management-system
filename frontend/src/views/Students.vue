<template>
  <div class="page">
    <h1 class="page-title">{{ auth.role === 'student' ? '个人信息' : '学生信息管理' }}</h1>
    <el-form v-if="auth.role !== 'student'" :inline="true" :model="query" class="search-form">
      <el-form-item label="姓名">
        <el-input v-model="query.name" clearable placeholder="姓名" />
      </el-form-item>
      <el-form-item label="学号">
        <el-input v-model="query.studentNo" clearable placeholder="学号" />
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="query.major" clearable placeholder="专业" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="table-panel">
      <div class="toolbar">
        <span class="muted">共 {{ rows.length }} 条记录</span>
        <el-button v-if="canEdit" type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          新增学生
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="college" label="学院" min-width="150" />
        <el-table-column prop="major" label="专业" min-width="130" />
        <el-table-column prop="className" label="班级" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="130" />
        <el-table-column prop="email" label="邮箱" min-width="170" />
        <el-table-column v-if="canEdit" label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学生' : '新增学生'" width="640px">
      <el-form class="dialog-form" :model="form" label-width="90px">
        <el-row :gutter="12">
          <el-col :span="12"><el-form-item label="学号"><el-input v-model="form.studentNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="性别"><el-select v-model="form.gender"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="年龄"><el-input-number v-model="form.age" :min="15" :max="40" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="学院"><el-input v-model="form.college" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="专业"><el-input v-model="form.major" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="班级"><el-input v-model="form.className" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createStudent, deleteStudent, getStudent, getStudents, updateStudent } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const canEdit = computed(() => auth.role === 'admin')
const rows = ref([])
const dialogVisible = ref(false)
const query = reactive({ name: '', studentNo: '', major: '' })
const form = reactive({})

const load = async () => {
  if (auth.role === 'student' && auth.studentId) {
    rows.value = [await getStudent(auth.studentId)]
    return
  }
  rows.value = await getStudents(query)
}

const reset = () => {
  Object.assign(query, { name: '', studentNo: '', major: '' })
  load()
}

const openCreate = () => {
  Object.assign(form, { id: null, studentNo: '', name: '', gender: '男', age: 20, college: '', major: '', className: '', phone: '', email: '' })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateStudent(form.id, form)
  } else {
    await createStudent(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除学生 ${row.name}？`, '删除确认', { type: 'warning' })
  await deleteStudent(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
