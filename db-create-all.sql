create table credit_card (
  id                            bigint auto_increment not null,
  wallet_id                     bigint not null,
  crypt_name                    varchar(100),
  crypt_number                  varchar(100),
  crypt_security_number         varchar(100),
  expiration_date               date not null,
  due_date                      date not null,
  card_limit                    decimal(38) not null,
  usage                         decimal(38) not null,
  version                       bigint not null,
  whencreated                   timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_credit_card primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  wallet_id                     bigint,
  name                          varchar(255) not null,
  user_name                     varchar(255) not null,
  password                      varchar(255) not null,
  version                       bigint not null,
  whencreated                   timestamp not null,
  when_modified                 timestamp not null,
  constraint uq_user_wallet_id unique (wallet_id),
  constraint uq_user_user_name unique (user_name),
  constraint pk_user primary key (id)
);

create table wallet (
  id                            bigint auto_increment not null,
  user_limit                    decimal(38) not null,
  version                       bigint not null,
  whencreated                   timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_wallet primary key (id)
);

alter table credit_card add constraint fk_credit_card_wallet_id foreign key (wallet_id) references wallet (id) on delete restrict on update restrict;
create index ix_credit_card_wallet_id on credit_card (wallet_id);

alter table user add constraint fk_user_wallet_id foreign key (wallet_id) references wallet (id) on delete restrict on update restrict;

