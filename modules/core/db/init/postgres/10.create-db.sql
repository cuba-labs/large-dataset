-- begin SAMPLE_CUSTOMER
create table SAMPLE_CUSTOMER (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    ADDRESS varchar(255),
    --
    primary key (ID)
)^
-- end SAMPLE_CUSTOMER
-- begin SAMPLE_ORDER
create table SAMPLE_ORDER (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_ date,
    NUM varchar(100),
    AMOUNT decimal(19, 2),
    CUSTOMER_ID uuid,
    --
    primary key (ID)
)^
-- end SAMPLE_ORDER
