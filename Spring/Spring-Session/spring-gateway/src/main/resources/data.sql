insert into user (username, password, enabled) values ('user1', '$2a$10$H3AC0dO1mZKVzHTpJ4pKouC94hsy11K3xK/GZl5ih51F6R/mWMn9e', true);
insert into user_role (id, username, role) values (1, 'user1', 'USER_ROLE');

insert into user (username, password, enabled) values ('user2', '$2a$10$UDRhp6tvx15/Tkg81H2uy.gvtImhdsWj7xtdLP2mZ98lbFm8ySoGG', true);
insert into user_role (id, username, role) values (2, 'user2', 'ADMIN_ROLE');