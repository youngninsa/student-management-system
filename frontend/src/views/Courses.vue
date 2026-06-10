<template>
  <div class="page">
    <h1 class="page-title">课程管理</h1>
    <el-form :inline="true" :model="query" class="search-form">
      <el-form-item label="关键词">
        <el-input v-model="query.keyword" clearable placeholder="课程编号、名称、教师" />
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
          新增课程
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="courseNo" label="课程编号" width="130" />
        <el-table-column prop="courseName" label="课程名称" min-width="160" />
        <el-table-column prop="teacherName" label="任课教师" width="120" />
        <el-table-column prop="credit" label="学分" width="90" />
        <el-table-column prop="classroom" label="上课地点" min-width="130" />
        <el-table-column prop="courseTime" label="课程时间" min-width="180" />
        <el-table-column v-if="canEdit" label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑课程' : '新增课程'" width="560px">
      <el-form class="dialog-form" :model="form" label-width="90px">
        <el-form-item label="课程编号"><el-input v-model="form.courseNo" /></el-form-item>
        <el-form-item label="课程名称"><el-input v-model="form.courseName" /></el-form-item>
        <el-form-item label="任课教师"><el-input v-model="form.teacherName" /></el-form-item>
        <el-form-item label="学分"><el-input-number v-model="form.credit" :min="0" :step="0.5" style="width: 100%" /></el-form-item>
        <el-form-item label="上课地点"><el-input v-model="form.classroom" /></el-form-item>
        <el-form-item label="课程时间"><el-input v-model="form.courseTime" /></el-form-item>
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
import { createCourse, deleteCourse, getCourses, updateCourse } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const canEdit = computed(() => auth.role === 'admin')
const rows = ref([])
const dialogVisible = ref(false)
const query = reactive({ keyword: '' })
const form = reactive({})

const load = async () => {
  rows.value = await getCourses(query)
}

const reset = () => {
  query.keyword = ''
  load()
}

const openCreate = () => {
  Object.assign(form, { id: null, courseNo: '', courseName: '', teacherName: '', credit: 2, classroom: '', courseTime: '' })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateCourse(form.id, form)
  } else {
    await createCourse(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除课程 ${row.courseName}？`, '删除确认', { type: 'warning' })
  await deleteCourse(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
