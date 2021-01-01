package org.sunyh.utils;

import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrintUtil {
    private static Logger log=Logger.getLogger(PrintUtil.class);


    public static void printMapList(List<Map> list){
        log.info("打印表结构-start");
        list.forEach(item->{
            for(Object object:item.keySet()){
                log.info(object+":"+item.get(object)+"  ");
            }
            log.info("-----------------------------------");
        });
        log.info("打印表结构-end");

    }

    public static void printMap(Map<?,?> map){
        log.info("打印 map-->start");
        map.forEach((key,value)->{
            log.info("key="+key+"\tvalue="+value);
        });
        log.info("打印 map-->end");
    }
    public static void printList(List<?> list){
        log.info("打印 list-->start");
        list.forEach(item->{
            log.info(item.toString());
        });
        log.info("打印 list-->end");
    }
}
