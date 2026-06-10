import request from './request'

export const login = (data) => request.post('/auth/login', data)
export const getDashboardStats = () => request.get('/dashboard/stats')

export const getStudents = (params) => request.get('/students', { params })
export const getStudent = (id) => request.get(`/students/${id}`)
export const createStudent = (data) => request.post('/students', data)
export const updateStudent = (id, data) => request.put(`/students/${id}`, data)
export const deleteStudent = (id) => request.delete(`/students/${id}`)

export const getCourses = (params) => request.get('/courses', { params })
export const createCourse = (data) => request.post('/courses', data)
export const updateCourse = (id, data) => request.put(`/courses/${id}`, data)
export const deleteCourse = (id) => request.delete(`/courses/${id}`)

export const getGrades = (params) => request.get('/grades', { params })
export const createGrade = (data) => request.post('/grades', data)
export const updateGrade = (id, data) => request.put(`/grades/${id}`, data)
export const deleteGrade = (id) => request.delete(`/grades/${id}`)

export const getAnnouncements = () => request.get('/announcements')
export const createAnnouncement = (data) => request.post('/announcements', data)
export const updateAnnouncement = (id, data) => request.put(`/announcements/${id}`, data)
export const deleteAnnouncement = (id) => request.delete(`/announcements/${id}`)

export const getActivities = (params) => request.get('/activities', { params })
export const createActivity = (data) => request.post('/activities', data)
export const updateActivity = (id, data) => request.put(`/activities/${id}`, data)
export const deleteActivity = (id) => request.delete(`/activities/${id}`)
export const registerActivity = (id, studentId) => request.post(`/activities/${id}/register`, null, { params: { studentId } })
export const getActivityRegistrations = (id) => request.get(`/activities/${id}/registrations`)

export const getPayments = (params) => request.get('/payments', { params })
export const createPayment = (studentId, data) => request.post('/payments', data, { params: { studentId } })
export const payPayment = (id) => request.put(`/payments/${id}/pay`)

export const getUsers = () => request.get('/users')
export const createUser = (data) => request.post('/users', data)
export const updateUser = (id, data) => request.put(`/users/${id}`, data)
export const deleteUser = (id) => request.delete(`/users/${id}`)
