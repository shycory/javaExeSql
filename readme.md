java maven project
---
#### 使用框架:mybatis

#### 操作数据库: oracle

#### jdbc: ojdbc6

#### jdk:1.8

#### 功能:
    将表A重命名为 A_BAK
    读取A_BAK中的数据以及表结构
    在主程序中新建字段,插入到表结构集合中/删除表结构集合中的某个项
    根据新的表结构集合,创建名称为 A 的新表
    最后将数据拷贝到 A 表中,完成复制.

#### 使用:
    程序入口:
        org.sunyh.App
    参数说明:
        TABLE_NAME:需要修改的表
        TABLE_NAME_BAK:原表的备份表(默认原名+_BAK)
        CONSTRAINT_NAME:约束的key,最后会获取一个三位的随机数
                        若数据库中存在了该约束,建表将失败
                        在catch中会将备份表名rename为原来的表名
---

 **注意!!!: 执行完查看日志,若在 将 表名改为备份表名后 失败**
 **且将备份表名rename为原来的表名的步骤也异常,不能再点击运行**
 **因为程序第一步就是删除备份表!!!**
 **若建表失败,此时的原表名为备份表名,再次执行会直接drop掉!!!**
 ---
#### 待完善:
    删除字段 在建表的过程应该没有什么问题,但在插入数据处应该会失败,除非那一列数据全部为null.
    应该写一个方法专门删除所选下表的值后,将删除的哪个值的列名也就是字段名取到
    对数据进行循环,将 equals 该字段的 值,赋值为null;
