create table S_SYH_TEST(
    s_id varchar2(20 CHAR) not null ,
    s_age number(24,6) default 0,
    s_reg_time timestamp(0),
    constraint "PRIMARY_SSTT_005" PRIMARY KEY (s_id)
);
insert all
    into S_SYH_TEST ("S_ID","S_AGE","S_REG_TIME") VALUES ('3','32','SYSDATE')
    into S_SYH_TEST ("S_ID","S_AGE","S_REG_TIME") VALUES ('4','23','SYSDATE')
select 1 from dual;

rename S_SYH_TEST_BAK to S_SYH_TEST;

select * from S_SYH_TEST

