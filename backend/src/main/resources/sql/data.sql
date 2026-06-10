USE student_management_system;

INSERT INTO students (id, student_no, name, gender, age, college, major, class_name, phone, email) VALUES
(1, '20250001', '张明', '男', 20, '计算机学院', '软件工程', '软工一班', '13800000001', 'zhangming@example.com'),
(2, '20250002', '李娜', '女', 19, '计算机学院', '计算机科学与技术', '计科二班', '13800000002', 'lina@example.com'),
(3, '20250003', '王强', '男', 21, '信息工程学院', '物联网工程', '物联一班', '13800000003', 'wangqiang@example.com'),
(4, '20250004', '赵敏', '女', 20, '管理学院', '信息管理', '信管一班', '13800000004', 'zhaomin@example.com');

INSERT INTO users (id, username, password, role, real_name, student_id, created_at) VALUES
(1, 'admin', '123456', 'admin', '系统管理员', NULL, NOW()),
(2, 'teacher', '123456', 'teacher', '陈老师', NULL, NOW()),
(3, 'student', '123456', 'student', '张明', 1, NOW());

INSERT INTO courses (id, course_no, course_name, teacher_name, credit, classroom, course_time) VALUES
(1, 'CS101', 'Java 程序设计', '陈老师', 3.0, '教学楼 A301', '周一 1-2 节'),
(2, 'CS202', '数据库原理', '刘老师', 3.5, '教学楼 B204', '周三 3-4 节'),
(3, 'CS303', 'Web 前端开发', '王老师', 2.5, '实验楼 C105', '周五 5-6 节'),
(4, 'MG101', '管理信息系统', '赵老师', 2.0, '教学楼 D201', '周二 7-8 节');

INSERT INTO grades (student_id, course_id, score, semester) VALUES
(1, 1, 92, '2025-2026 第一学期'),
(1, 2, 86, '2025-2026 第一学期'),
(2, 1, 78, '2025-2026 第一学期'),
(2, 3, 95, '2025-2026 第一学期'),
(3, 2, 61, '2025-2026 第一学期'),
(4, 4, 88, '2025-2026 第一学期');

INSERT INTO announcements (title, content, publisher, created_at) VALUES
('关于本学期选课安排的通知', '请各位同学在规定时间内完成线上选课，并及时查看课表。', '教务处', NOW()),
('校园招聘宣讲会安排', '本周五下午将在大学生活动中心举办软件企业专场宣讲会。', '就业中心', NOW()),
('期末考试诚信教育提醒', '请同学们严格遵守考试纪律，诚信考试。', '学生处', NOW());

INSERT INTO activities (id, title, content, location, activity_time, max_count) VALUES
(1, '程序设计竞赛报名', '面向全校学生开放，鼓励跨专业组队参加。', '实验楼 C201', '2026-06-10 14:00:00', 60),
(2, '校园志愿服务活动', '参与校园环境维护和迎新志愿服务。', '大学生活动中心', '2026-06-18 09:00:00', 80),
(3, '职业规划讲座', '邀请企业导师分享简历、面试与职业规划经验。', '报告厅', '2026-06-22 19:00:00', 120);

INSERT INTO activity_registrations (activity_id, student_id, created_at) VALUES
(1, 1, NOW()),
(2, 2, NOW());

INSERT INTO payments (student_id, item_name, amount, status, paid_at) VALUES
(1, '2025-2026 学年学费', 5600.00, '未缴费', NULL),
(1, '住宿费', 1200.00, '已缴费', NOW()),
(2, '2025-2026 学年学费', 5600.00, '未缴费', NULL),
(3, '教材费', 350.00, '未缴费', NULL),
(4, '住宿费', 1200.00, '已缴费', NOW());
