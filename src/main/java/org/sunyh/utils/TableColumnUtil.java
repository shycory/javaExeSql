package org.sunyh.utils;

import org.sunyh.bean.Varchar2Field;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableColumnUtil {

    /**
     * 实体转map
     * @param obj
     * @return
     */
    public static Map<String, Object> getMapFromBean(Object obj){
        Map<String,Object> propertiesMap=new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 对于每个属性，获取属性名
            String varName = field.getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = field.get(obj);
                    propertiesMap.put(varName, o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                field.setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return propertiesMap;
    }



}
