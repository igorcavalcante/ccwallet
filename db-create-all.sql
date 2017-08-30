create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  user_name                     varchar(255) not null,
  password                      varchar(255) not null,
  version                       bigint not null,
  whencreated                   timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_user primary key (id)
);

