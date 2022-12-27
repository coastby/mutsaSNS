insert into user (id, user_name, password, role)
values (1, 'user', '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e', 'ROLE_USER'),
(2, 'admin', '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e', 'ROLE_ADMIN');

insert into post (id, body, is_deleted, title, user_id)
values (1, "body", false, "title", 1),
(2, "body2", false, "title2", 1);