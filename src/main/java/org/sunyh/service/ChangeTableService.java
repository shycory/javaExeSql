package org.sunyh.service;

import oracle.sql.TIMESTAMP;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.sunyh.App;
import org.sunyh.mapper.TableMapper;
import org.sunyh.utils.MybatisUtil;
import org.sunyh.utils.PrintUtil;

import java.sql.SQLException;
import java.util.*;

/**
 * @author SunYuhong
 * @Date: 2020/12/31 13:13
 * @Description TODO
 */
public class ChangeTableService {
    private static Logger log=Logger.getLogger(ChangeTableService.class);
    private static ChangeTableService changeTableService;
    //数据
    private List<LinkedHashMap> linkedHashMaps;
    //表结构
    private List<Map> tableStruct;

    public static ChangeTableService getInstance(){
        if(changeTableService==null){
            synchronized (ChangeTableService.class){
                if(changeTableService==null){
                    changeTableService=new ChangeTableService();
                }
            }
        }
        return changeTableService;
    }

    private ChangeTableService() {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper mapper = sqlSession.getMapper(TableMapper.class);
        try{
            //查询表结构
            tableStruct = mapper.getTableStruct(App.TABLE_NAME);
            PrintUtil.printMapList(tableStruct);

            linkedHashMaps = mapper.queryAllData(App.TABLE_NAME);
            this.changeTimeStamp();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
    }

    public List<LinkedHashMap> getLinkedHashMaps() {
        return linkedHashMaps;
    }

    public List<Map> getTableStruct() {
        return tableStruct;
    }

    //转换时间类型
    private void changeTimeStamp(){
        linkedHashMaps.forEach(map->{
            map.forEach((key,value)->{
                if(value instanceof oracle.sql.TIMESTAMP){
                    try {
                        map.put(key,((TIMESTAMP) value).timestampValue());
                    } catch (SQLException e) {
                        log.info("oracle.sql.TIMESTAMP转成java.sql.TimeStamp 异常");
                        e.printStackTrace();
                    }
                }
            });
        });
    }
    //获取数据通过表名
   /* private void getData(String tableName) throws Exception {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper mapper = sqlSession.getMapper(TableMapper.class);
        try{
            linkedHashMaps = mapper.queryAllData(tableName);
            this.changeTimeStamp();
            //printList(linkedHashMaps);
        }catch (Exception e){
            log.info("查询数据失败");
            throw new Exception("查询数据失败");
        }finally {
            sqlSession.close();
        }
    }*/
    //向B表中插入数据
    private void insertBatchDataToB(String B,List<LinkedHashMap> list) throws Exception {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper mapper = sqlSession.getMapper(TableMapper.class);
        try {
            if(list.size()>0){
                mapper.insertBatch(B,list);
            }
            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
            log.info("插入数据失败");
            throw new Exception("插入数据失败异常!");
        }finally {
            sqlSession.close();
        }
    }
    //将A中数据 拷贝到B
    /*public void copyDataToBFromA(String A,String B){
        try {
            getData(A);
            insertBatchDataToB(B,linkedHashMaps);
        }catch (Exception e){
            log.info("拷贝数据失败");
            e.printStackTrace();
        }
    }*/
    //重置
    public void reset(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper mapper = sqlSession.getMapper(TableMapper.class);
        log.info("-------------------------------------------------------------------------");
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<    RESET   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("-------------------------------------------------------------------------");
        try{
            //删除新表
            mapper.dropTableBak(App.TABLE_NAME);
            //将名改回来
            mapper.renameTableToBak(App.TABLE_NAME_BAK,App.TABLE_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
    }
    //总程序
    public void start(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        TableMapper mapper = sqlSession.getMapper(TableMapper.class);
        log.info("-------------------------------------------------------------------------");
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<    START   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("-------------------------------------------------------------------------");
        try{
            //删除备份表
            try{
                mapper.dropTableBak(App.TABLE_NAME_BAK);
                log.info(">>>>>>>>  备份表已删除   >>>>>>>>>");
            }catch (Exception e){
                log.info("<<<<<<<<  备份表不存在,删除失败,继续向下执行");
            }

            //查询主键
            String tableKey = mapper.getTableKey(App.TABLE_NAME);
            log.info(">>>>>>>>  主键为:"+tableKey);
            //查询表注释
            String tableComments = mapper.getTableComments(App.TABLE_NAME);
            log.info("~~~~~~~~"+tableComments+"~~~~~~~~~");

            //重命名
            mapper.renameTableToBak(App.TABLE_NAME,App.TABLE_NAME_BAK);

            //创建表
            log.info(">>>>>>>>  开始创建表 ");
            try{
                Map map=getMapForTableStruct(App.TABLE_NAME,tableComments,tableKey,tableStruct);
                mapper.createNewTable(map);
            }catch (Exception e){
                log.info("建表失败-->>将表重命名回来");
                mapper.renameTableToBak(App.TABLE_NAME_BAK,App.TABLE_NAME);
                log.info("表名重置成功");
                e.printStackTrace();
            }

            //添加表描述
            if(tableComments!=null && !tableComments.trim().equals("")){
                mapper.updateTableComment(App.TABLE_NAME,tableComments);
            }
            //添加字段描述
            for(Map obj:tableStruct){
                if(obj.get("COMMENTS")!=null) {
                    mapper.updateColumnComments(App.TABLE_NAME,(String) obj.get("COLUMN_NAME"),(String) obj.get("COMMENTS"));
                }
            }
            //插入数据
            try{
                insertBatchDataToB(App.TABLE_NAME,linkedHashMaps);
            }catch (Exception e){
                log.info("执行总程序->插入失败");
                log.info("回到最开始的表名>>>>");
                mapper.dropTableBak(App.TABLE_NAME);
                mapper.renameTableToBak(App.TABLE_NAME_BAK,App.TABLE_NAME);
                log.info("重置成功");
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
    }



    private Map getMapForTableStruct(String tableName,String comments,String key,List<Map> tableStruct){
        Map<String,Object> map=new HashMap<>();
        map.put("tableName",tableName);
        map.put("tableComments",comments);
        map.put("CONSTRAINT_NAME",App.CONSTRAINT_NAME);
        map.put("COLUMN_NAME",key);
        map.put("columns",tableStruct);
        return  map;
    }

}
