<template>
  <div class="page">
    <h1 class="page-title">{{ auth.role === 'student' ? '在线缴费' : '缴费记录' }}</h1>
    <div class="table-panel">
      <div class="toolbar">
        <span class="muted">共 {{ rows.length }} 条记录</span>
        <el-button v-if="auth.role === 'admin'" type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          新增缴费项目
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column prop="studentName" label="学生" width="110" />
        <el-table-column prop="itemName" label="缴费项目" min-width="180" />
        <el-table-column prop="amount" label="金额" width="110" />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.status === '已缴费' ? 'success' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paidAt" label="缴费时间" width="180" />
        <el-table-column v-if="auth.role === 'student'" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :disabled="row.status === '已缴费'" @click="pay(row)">模拟缴费</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="新增缴费项目" width="560px">
      <el-form class="dialog-form" :model="form" label-width="90px">
        <el-form-item label="学生">
          <el-select v-model="form.studentId" filterable style="width: 100%">
            <el-option v-for="item in students" :key="item.id" :label="`${item.studentNo} - ${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目名称"><el-input v-model="form.itemName" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" /></el-form-item>
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
import { createPayment, getPayments, getStudents, payPayment } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const rows = ref([])
const students = ref([])
const dialogVisible = ref(false)
const form = reactive({})

const load = async () => {
  const params = auth.role === 'student' ? { studentId: auth.studentId } : {}
  rows.value = await getPayments(params)
}

const loadStudents = async () => {
  if (auth.role === 'admin') {
    students.value = await getStudents({})
  }
}

const openCreate = () => {
  Object.assign(form, { studentId: students.value[0]?.id, itemName: '', amount: 0 })
  dialogVisible.value = true
}

const save = async () => {
  await createPayment(form.studentId, { itemName: form.itemName, amount: form.amount, status: '未缴费' })
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const pay = async (row) => {
  await ElMessageBox.confirm(`确认模拟缴纳 ${row.itemName}？`, '缴费确认', { type: 'info' })
  await payPayment(row.id)
  ElMessage.success('缴费成功')
  load()
}

onMounted(async () => {
  await Promise.all([load(), loadStudents()])
})
</script>
