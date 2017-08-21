create table user (
  id                            bigint auto_increment not null,
  version                       bigint not null,
  name                          varchar(255) not null,
  constraint pk_user primary key (id)
);

