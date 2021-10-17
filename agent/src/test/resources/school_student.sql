create table student
(
    id          int         not null
        primary key,
    name        varchar(45) null,
    update_time datetime    null
);

INSERT INTO school.student (id, name, update_time) VALUES (1, '张三', '2017-12-22 15:27:18');
INSERT INTO school.student (id, name, update_time) VALUES (2, '李四', '2017-12-22 15:27:18');
INSERT INTO school.student (id, name, update_time) VALUES (3, '王五', '2017-12-22 15:27:18');