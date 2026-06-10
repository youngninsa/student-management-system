# 高校学生综合管理系统

这是一个用于 B/S 架构课程作业的前后端分离项目，面向管理员、教师和学生三类角色，提供学生信息、课程、成绩、公告、活动报名、在线缴费和系统用户管理等功能。项目重点保证能运行、能演示、结构清晰，便于课程答辩讲解。

## 技术栈

- 后端：Java 17、Spring Boot、Spring Web、Spring Data JPA、MySQL、Maven
- 前端：Vue 3、Vite、Vue Router、Pinia、Axios、Element Plus
- 数据库：MySQL 8.x

## 功能模块

- 登录：简化版登录，按角色进入系统
- 首页：统计学生、课程、公告、活动数量，并展示最近公告
- 学生管理：列表、搜索、新增、编辑、删除
- 课程管理：列表、搜索、新增、编辑、删除
- 成绩管理：录入、修改、删除成绩，学生只查看本人记录
- 公告通知：管理员和教师发布公告，所有角色查看公告
- 活动报名：管理员发布活动，学生报名，防止重复报名
- 在线缴费：学生模拟缴费，管理员查看缴费记录
- 用户管理：管理员维护系统用户

## 项目目录

```text
student-management-system/
  backend/
    pom.xml
    src/main/java/com/example/sms/
      common/
      controller/
      dto/
      entity/
      repository/
      service/
    src/main/resources/application.yml
    src/main/resources/sql/schema.sql
    src/main/resources/sql/data.sql
  frontend/
    package.json
    src/
      api/
      router/
      stores/
      views/
      components/
      App.vue
      main.js
  docs/answer.md
  README.md
```

## 数据库导入

1. 确认本机已安装并启动 MySQL。
2. 打开 MySQL 客户端，依次执行：

```sql
source backend/src/main/resources/sql/schema.sql;
source backend/src/main/resources/sql/data.sql;
```

也可以在 Navicat、DataGrip、MySQL Workbench 中分别运行 `schema.sql` 和 `data.sql`。

默认后端配置在 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_management_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false
    username: root
    password: 123456
```

如果你的 MySQL 用户名或密码不同，请修改这里。

## 后端启动

```bash
cd backend
mvn spring-boot:run
```

启动成功后，后端接口地址为：

```text
http://localhost:8080/api
```

## 前端启动

```bash
cd frontend
npm install
npm run dev
```

启动成功后，浏览器访问：

```text
http://localhost:5173
```

Vite 已配置 `/api` 代理到 `http://localhost:8080`。

## 测试账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | admin | 123456 |
| 教师 | teacher | 123456 |
| 学生 | student | 123456 |

## 常用接口

- `POST /api/auth/login`
- `GET /api/dashboard/stats`
- `GET /api/students`
- `POST /api/students`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`
- `GET /api/courses`
- `GET /api/grades`
- `GET /api/announcements`
- `GET /api/activities`
- `POST /api/activities/{id}/register?studentId=1`
- `GET /api/payments`
- `PUT /api/payments/{id}/pay`
- `GET /api/users`

## 演示步骤

1. 导入数据库脚本，启动后端和前端。
2. 使用 `admin / 123456` 登录，展示首页统计卡片和最近公告。
3. 进入学生管理，演示搜索、新增、编辑、删除。
4. 进入课程管理，演示课程维护。
5. 进入成绩管理，演示录入成绩并查看自动等级。
6. 进入公告通知，演示发布公告。
7. 进入活动报名，演示活动发布和报名名单查看。
8. 进入在线缴费，查看所有缴费记录。
9. 切换 `student / 123456` 登录，演示学生查看本人信息、成绩、活动报名和模拟缴费。
10. 切换 `teacher / 123456` 登录，演示教师查看学生、维护成绩、发布公告。

## 说明

- 本项目未引入 Spring Security，登录逻辑保持简洁，方便课程演示和代码讲解。
- 密码以明文形式存储，仅用于课程作业演示，正式项目应加密存储。
- 在线缴费为模拟功能，不接入真实支付平台。
