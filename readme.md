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
 
#### 待完善:
    删除字段 在建表的过程应该没有什么问题,但在插入数据处应该会失败,除非那一列数据全部为null.
    应该写一个方法专门删除所选下表的值后,将删除的哪个值的列名也就是字段名取到
    对数据进行循环,将 equals 该字段的 值,赋值为null;

