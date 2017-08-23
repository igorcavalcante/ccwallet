alter table credit_card drop constraint if exists fk_credit_card_wallet_id;
drop index if exists ix_credit_card_wallet_id;

alter table wallet drop constraint if exists fk_wallet_user_id;

drop table if exists credit_card;

drop table if exists user;

drop table if exists wallet;

