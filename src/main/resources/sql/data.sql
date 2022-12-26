insert into user (id, user_name, password, role)
values (1, 'string', '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e', 'USER'),
(2, 'string2', '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e', 'USER');

insert into post (id, body, is_deleted, title, user_id)
values (1, "body", false, "title", 1),
(2, "body2", false, "title2", 1);