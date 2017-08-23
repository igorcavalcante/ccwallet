create table credit_card (
  id                            bigint auto_increment not null,
  wallet_id                     bigint not null,
  name                          varchar(255) not null,
  number                        varchar(255) not null,
  expiration_date               date not null,
  security_number               integer not null,
  due_date                      date not null,
  user_limit                    decimal(38) not null,
  max_limit                     decimal(38) not null,
  usage                         decimal(38) not null,
  version                       bigint not null,
  constraint pk_credit_card primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  user_name                     varchar(255) not null,
  password                      varchar(255) not null,
  version                       bigint not null,
  constraint uq_user_user_name unique (user_name),
  constraint pk_user primary key (id)
);

create table wallet (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  real_limit                    decimal(38) not null,
  version                       bigint not null,
  constraint uq_wallet_user_id unique (user_id),
  constraint pk_wallet primary key (id)
);

alter table credit_card add constraint fk_credit_card_wallet_id foreign key (wallet_id) references wallet (id) on delete restrict on update restrict;
create index ix_credit_card_wallet_id on credit_card (wallet_id);

alter table wallet add constraint fk_wallet_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

