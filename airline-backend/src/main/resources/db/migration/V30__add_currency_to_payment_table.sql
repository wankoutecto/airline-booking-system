alter table payment
add column currency varchar(10);

update payment
set currency = 'USD';

alter table payment
alter column currency set not null;