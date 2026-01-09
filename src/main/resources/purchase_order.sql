-- auto-generated definition
create table purchase_order
(
    id             bigserial
        primary key,
    order_no       varchar(50)  not null,
    ext_order_no   varchar(100),
    order_date     date,
    part_no        varchar(100) not null,
    backorder      integer,
    shipment_no    varchar(50),
    target_date    date,
    confirmed_date date,
    priority       varchar(20),
    responsible    varchar(50),
    created_by     varchar(50)
);

alter table purchase_order
    owner to postgres;

