<template>
  <div class="page">
    <h1 class="page-title">{{ auth.role === 'student' ? '成绩查询' : '成绩管理' }}</h1>
    <div class="table-panel">
      <div class="toolbar">
        <span class="muted">共 {{ rows.length }} 条记录</span>
        <el-button v-if="canEdit" type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          录入成绩
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="studentName" label="学生" width="110" />
        <el-table-column prop="courseNo" label="课程编号" width="130" />
        <el-table-column prop="courseName" label="课程" min-width="160" />
        <el-table-column prop="semester" label="学期" min-width="150" />
        <el-table-column prop="score" label="成绩" width="100" />
        <el-table-column prop="level" label="等级" width="100">
          <template #default="{ row }">
            <el-tag :type="tagType(row.level)">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="canEdit" label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑成绩' : '录入成绩'" width="560px">
      <el-form class="dialog-form" :model="form" label-width="90px">
        <el-form-item label="学生">
          <el-select v-model="form.studentId" filterable style="width: 100%">
            <el-option v-for="item in students" :key="item.id" :label="`${item.studentNo} - ${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="form.courseId" filterable style="width: 100%">
            <el-option v-for="item in courses" :key="item.id" :label="`${item.courseNo} - ${item.courseName}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="成绩"><el-input-number v-model="form.score" :min="0" :max="100" style="width: 100%" /></el-form-item>
        <el-form-item label="学期"><el-input v-model="form.semester" placeholder="2025-2026 第一学期" /></el-form-item>
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
import { createGrade, deleteGrade, getCourses, getGrades, getStudents, updateGrade } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const canEdit = computed(() => auth.role === 'admin' || auth.role === 'teacher')
const rows = ref([])
const students = ref([])
const courses = ref([])
const dialogVisible = ref(false)
const form = reactive({})

const load = async () => {
  const params = auth.role === 'student' ? { studentId: auth.studentId } : {}
  rows.value = await getGrades(params)
}

const loadOptions = async () => {
  if (canEdit.value) {
    students.value = await getStudents({})
    courses.value = await getCourses({})
  }
}

const openCreate = () => {
  Object.assign(form, { id: null, studentId: students.value[0]?.id, courseId: courses.value[0]?.id, score: 80, semester: '2025-2026 第一学期' })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, { id: row.id, studentId: row.studentId, courseId: row.courseId, score: row.score, semester: row.semester })
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateGrade(form.id, form)
  } else {
    await createGrade(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除 ${row.studentName} 的 ${row.courseName} 成绩？`, '删除确认', { type: 'warning' })
  await deleteGrade(row.id)
  ElMessage.success('删除成功')
  load()
}

const tagType = (level) => {
  if (level === '优秀') return 'success'
  if (level === '良好') return 'primary'
  if (level === '及格') return 'warning'
  return 'danger'
}

onMounted(async () => {
  await Promise.all([load(), loadOptions()])
})
</script>
