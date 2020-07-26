create table user(
    username varchar_ignorecase(20) not null primary key,
    password varchar_ignorecase(100) not null,
    enabled boolean not null
);

create table user_role (
	id NUMERIC(38) not null primary key,
    username varchar_ignorecase(20) not null,
    role varchar_ignorecase(50) not null,
    constraint fk_users_roles foreign key(username) references user(username)
);
create unique index ix_auth_username on user_role (username,role);