package org.sunyh;

import org.sunyh.bean.NumberField;
import org.sunyh.bean.TimeStampField;
import org.sunyh.bean.Varchar2Field;
import org.sunyh.service.ChangeTableService;
import org.sunyh.utils.TableColumnUtil;

import java.util.List;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App {

    public static final String TABLE_NAME="S_SYH_TEST";
    public static final String TABLE_NAME_BAK=TABLE_NAME+"_BAK";
    public static final String CONSTRAINT_NAME="PRIMARY_SST_"+getRoundNumber();

    public static void main( String[] args ){
        //构造函数中对 TABLE_NAME 表进行查询表结构
        ChangeTableService changeTableService = new ChangeTableService();
        //获取现有表结构
        List<Map> tableStruct = changeTableService.getTableStruct();

        // 向现有表结构添加字段,现只支持 varchar2,number,timestamp,   NULLABLE={"Y"(可为空),"N"(不可为空)}

        //创建字段
        //Varchar2Field field=new Varchar2Field("s_test","32","Y","def","beizhu");
        //NumberField field=new NumberField("t_number_test","24","6","Y",null,null);
        TimeStampField field=new TimeStampField("t_time_test",null,"Y",null);

        //插入,位置取值范围:[0,list.size()-1]; 0为插到第一列
        tableStruct.add(2, TableColumnUtil.getMapFromBean(field));

        //执行程序-->备份,建表,拷贝等操作;
        changeTableService.start();

        //在字段添加错位置,想重新插.执行一次重置;
        //changeTableService.reset();

        //仅拷贝数据
        //changeTableService.copyDataToBFromA(TABLE_NAME_BAK,TABLE_NAME);
    }
    private static String getRoundNumber(){
        double random = Math.random()*1000;
        return (int)random+"";
    }
}
