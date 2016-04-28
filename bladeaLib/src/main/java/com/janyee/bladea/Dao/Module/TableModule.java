package com.janyee.bladea.Dao.Module;

import android.database.Cursor;

import com.janyee.bladea.Cast.DaoCastor;
import com.janyee.bladea.Dao.Condition.Condition;
import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Factory.DataOpenHelperFactory;
import com.janyee.bladea.Dao.annotation.Column;
import com.janyee.bladea.Dao.annotation.Forget;
import com.janyee.bladea.Dao.annotation.ID;
import com.janyee.bladea.Dao.annotation.Link;
import com.janyee.bladea.Dao.annotation.NotNull;
import com.janyee.bladea.Dao.annotation.Table;
import com.janyee.bladea.Tools.Md5;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lixinghua on 2015/8/27.
 */
public  class TableModule<T> {
    String tableName;
    Map<String,CellModule> cellMap;
    Map<String,LinkModule<T>> linkMap;
    List<Field> fieldList;
    CellModule primaryCell;
    T boundValue = null;
    Class<T> boundClass;
    String md5;
    DataOpenHelperFactory factory;
    public TableModule(Class<T> tClass) throws Exception {
        boundClass=tClass;
        init();
    }
    public TableModule(T t) throws Exception {
        boundValue=t;
        boundClass= (Class<T>) t.getClass();
        init();
    }
    private void init() throws Exception {
        cellMap = new HashMap<>();
        linkMap =new HashMap<>();
        fieldList = getField(boundClass);
        Annotation annotation = boundClass.getAnnotation(Table.class);
        if (annotation != null) {
            tableName = ((Table) annotation).value();
            factory=((Table) annotation).factory().newInstance();
        }else{
            throw new DaoException("This Pojo '"+boundClass.getCanonicalName()+"' does not have a @Table Annotation!");
        }
        for (Field temp : fieldList) {
            Annotation[] annotations = temp.getAnnotations();
            for (Annotation ann : annotations) {
                if ((ann instanceof ID)) {
                    if (primaryCell == null) {
                        primaryCell = new CellModule(temp);
                    } else {
                        throw new Exception("Dumplicate Primary Key!");
                    }
                }else if (ann instanceof Column) {
                    CellModule cellModule = new CellModule(temp);
                    cellMap.put(cellModule.getCellName(),cellModule);
                }
                if(ann instanceof Link){
                    String field=((Link)ann).field();
                    LinkModule module=new LinkModule(this,temp,field);
                    linkMap.put(field,module);
                }
            }
        }
    }

    public DataOpenHelperFactory getFactory() {
        return factory;
    }

    public String getInsertList() {
        StringBuilder nameList = new StringBuilder();
        StringBuilder valueList = new StringBuilder();
        if(primaryCell.getFieldValue(boundValue)==null){
            throw new DaoException("Primary Key '"+primaryCell.getCellName()+"' could not be NULL!");
        }
        if (cellMap.size() > 0) {
            nameList.append("(");
            nameList.append("'"+primaryCell.getCellName()+"',");

            valueList.append(DaoCastor.ObjectToString(primaryCell.getFieldValue(boundValue))+",");
            Iterator<Map.Entry<String,CellModule>> iterator=cellMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,CellModule> entry = iterator.next();
                CellModule module=entry.getValue();
                if(module.getBoundField().getAnnotation(NotNull.class)!=null&&entry.getValue().getFieldValue(boundValue)==null){
                    throw new DaoException("The field named '"+module.getCellName()+"' has a @NotNull Flag,but the field is null!");
                }
                if(entry.getValue().getFieldValue(boundValue)!=null){
                    nameList.append("'"+entry.getValue().getCellName() + "',");
                    valueList.append(""+DaoCastor.ObjectToString(entry.getValue().getFieldValue(boundValue))+",");
                }
            }
            nameList.delete(nameList.length() - 1, nameList.length());
            valueList.delete(valueList.length() - 1, valueList.length());
            nameList.append(") VALUES (" + valueList.toString() + ");");
        }
        return nameList.toString();
    }
    public static List<Field> getField(Class classz){
        List<Field> fieldList=new ArrayList<>();
        if(!classz.getSuperclass().equals(Object.class)){
            List<Field> fatherFields=getField(classz.getSuperclass());
            fieldList.addAll(fatherFields);
        }
        Field[] fields=classz.getDeclaredFields();
        fieldList.addAll(Arrays.asList(fields));
        Annotation forget=classz.getAnnotation(Forget.class);
        if(forget!=null){
            String forgets=((Forget)forget).value();
            for(Field field:fieldList){
                if(forgets.indexOf(field.getName())>=0){
                    fieldList.remove(field);
                }
            }
        }
        return fieldList;
    }
    public Object boundFieldValue(String field,Cursor cursor) throws Exception {
        getCellMap().get(field).bindField(boundValue,cursor);
        return boundValue;
    }
    public List<Object> getInsertValueList() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Object> valueList = new ArrayList<>();
        if (cellMap.size() > 0 && boundValue != null) {
            Iterator<Map.Entry<String,CellModule>> iterator=cellMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,CellModule> entry = iterator.next();
                valueList.add(entry.getValue().getFieldValue(boundValue));
            }

        }
        return valueList;
    }

    public String getCreateList() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StringBuilder stringBuilder = new StringBuilder();
        if (primaryCell != null) {
            stringBuilder.append(primaryCell.getCreateCellString() + ",");
        }
        if (cellMap.size() > 0) {
            Iterator<Map.Entry<String,CellModule>> iterator=cellMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,CellModule> entry = iterator.next();
                stringBuilder.append(entry.getValue().getCreateCellString()+ ",");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        return stringBuilder.toString();
    }


    public Condition getCondition() {
        Condition condition = Condition.Where();
        if (boundValue != null && cellMap != null && cellMap.size() > 0) {
            Iterator<Map.Entry<String,CellModule>> iterator=cellMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,CellModule> entry = iterator.next();
                CellModule module=entry.getValue();
                Object obj = module.getFieldValue(boundValue);
                if (obj != null) {
                    condition.And(module.getCellName(), "=", DaoCastor.ObjectToString(obj));
                }
            }
        }
        return condition;
    }
    public T bindValue(Cursor cursor) throws Exception {
        T obj=  boundClass.newInstance();
        primaryCell.bindField(obj,cursor);
        if (getCellMap().size() > 0) {
            for (String key : getCellMap().keySet()) {
                CellModule tt=getCellMap().get(key);
                tt.bindField(obj, cursor);
            }
            return obj;
        }else{
            return null;
        }
    }
    public String getMd5(){
        if(md5==null){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(primaryCell.toString()+",");
            for(CellModule m:cellMap.values()){
                stringBuilder.append(m.toString()+",");
            }
            md5= Md5.getMd5(stringBuilder.toString());
        }
        return md5;
    }
    public String getTableName() {
        return tableName;
    }

    public CellModule getPrimaryCell() {
        return primaryCell;
    }

    public Map<String,CellModule> getCellMap() {
        return cellMap;
    }
    public Object getFieldValue(T t,String field){
        return getCellMap().get(field).getFieldValue(t);
    }

    public Class getBoundClass() {
        return boundClass;
    }

    public Map<String,LinkModule<T>> getLinkMap() {
        return linkMap;
    }
    public void setBoundValue(T obj){
        boundValue=obj;
    }

}
