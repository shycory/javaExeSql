package org.sunyh;

import org.sunyh.service.ChangeTableService;
import org.sunyh.utils.TableColumnUtil;

public class App {

    public static final String TABLE_NAME="S_SYH_TEST";
    public static final String TABLE_NAME_BAK=TABLE_NAME+"_BAK";
    public static final String CONSTRAINT_NAME="PRIMARY_SST_"+getRoundNumber();

    public static void main( String[] args ){
        //构造函数中对 TABLE_NAME 表进行查询 表结构以及数据
        ChangeTableService changeTableService = ChangeTableService.getInstance();
        TableColumnUtil tableColumnUtil=TableColumnUtil.getInstance(changeTableService);

        //插入,位置取值范围:[0,list.size()-1]; 0为插到第一列
        try {
            tableColumnUtil.addVarchar2(1,"s_test",24,true,null,"ceshi");
            tableColumnUtil.addNumber(2,"s_test_a",24,6,true,"666",null);
            tableColumnUtil.addTimeStamp(3,"s_test_b",false,"sysdate","ceshi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //~~~~~~~~删除(不能删除主键)
        //tableColumnUtil.delColumn(1);

        //执行程序-->备份,建表,拷贝等操作;
        changeTableService.start();



        //以下为测试时用到的功能

        //在字段添加错位置,想重新插.执行一次重置;
        // 实现方式为,删掉现有的 原名表,将备份的名改回来
        //changeTableService.reset();

        //仅拷贝数据,已被注释,想用在放开
        //changeTableService.copyDataToBFromA(TABLE_NAME_BAK,TABLE_NAME);
    }
    private static String getRoundNumber(){
        double random = Math.random()*1000;
        return (int)random+"";
    }
}
