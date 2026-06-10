<template>
  <div class="page">
    <h1 class="page-title">公告通知</h1>
    <div class="table-panel">
      <div class="toolbar">
        <span class="muted">共 {{ rows.length }} 条公告</span>
        <el-button v-if="canPublish" type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          发布公告
        </el-button>
      </div>
      <el-table :data="rows" border stripe>
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="content" label="内容" min-width="260" show-overflow-tooltip />
        <el-table-column prop="publisher" label="发布人" width="120" />
        <el-table-column prop="createdAt" label="发布时间" width="180" />
        <el-table-column v-if="canPublish" label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公告' : '发布公告'" width="620px">
      <el-form class="dialog-form" :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="发布人"><el-input v-model="form.publisher" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="5" /></el-form-item>
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
import { createAnnouncement, deleteAnnouncement, getAnnouncements, updateAnnouncement } from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const canPublish = computed(() => auth.role === 'admin' || auth.role === 'teacher')
const rows = ref([])
const dialogVisible = ref(false)
const form = reactive({})

const load = async () => {
  rows.value = await getAnnouncements()
}

const openCreate = () => {
  Object.assign(form, { id: null, title: '', publisher: auth.user?.realName || auth.user?.username, content: '' })
  dialogVisible.value = true
}

const openEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateAnnouncement(form.id, form)
  } else {
    await createAnnouncement(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除公告 ${row.title}？`, '删除确认', { type: 'warning' })
  await deleteAnnouncement(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
