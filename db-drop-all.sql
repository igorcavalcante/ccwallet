alter table credit_card drop constraint if exists fk_credit_card_wallet_id;
drop index if exists ix_credit_card_wallet_id;

alter table user drop constraint if exists fk_user_wallet_id;

drop table if exists credit_card;

drop table if exists user;

drop table if exists wallet;

