insert into user (id, user_name, password, role)
values (1, 'user', '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e', 'ROLE_USER'),
(2, 'admin', '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e', 'ROLE_ADMIN');

insert into post (id, body, title, user_id)
values (1, "body", "title", 1),
(2, "body2", "title2", 1);

insert into likelion_test.comment (id, created_at, updated_at, comment, post_id, user_id)
values (1, "2022/11/11 11:11:11", "2022/11/11 11:11:11", "안녕", 1, 1),
(2, "2022/11/11 11:11:11", "2022/11/11 11:11:11", "안녕안녕", 1, 1);