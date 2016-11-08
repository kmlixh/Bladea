package com.janyee.bladea.Dao.Condition;


import com.janyee.bladea.Cast.DaoCastor;
import com.janyee.bladea.Dao.Dao;
import com.janyee.bladea.Dao.Exception.DaoException;
import com.janyee.bladea.Dao.Module.CellModule;
import com.janyee.bladea.Dao.Module.TableModule;
import com.janyee.bladea.Dao.SqlFactory;
import com.janyee.bladea.Dao.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/26.
 */
public class Condition {
    Map<String, Object> CndsMap;
    OrderBy[] order;
    Pager pager;
    String[] groupBy;
    StringBuilder staticCondition;

    private Condition(String Key, String Oper, Object Value) {

        CndsMap = new HashMap<String, Object>();
        putInMap("[" + Key + "] " + Oper + " ?", Value);
    }

    private Condition() {
        CndsMap = new HashMap<String, Object>();
    }

    public static Condition Static(String info) {
        Condition condition = new Condition();
        condition.staticCondition = new StringBuilder(info);
        return condition;
    }

    public static Condition Where(String Key, String Oper, Object Value) {
        Condition condition = new Condition(Key, Oper, Value);
        return condition;
    }

    public static Condition Where() {
        return new Condition();
    }

    public Condition And(String Key, String Oper, Object Value) {
        putInMap(Key + " " + Oper + " ? ", Value);
        return this;
    }

    public Condition AndIn(String key, Object[] objs) {
        StringBuilder stringBuilder = new StringBuilder();
        if (objs != null && objs.length > 0) {
            for (Object temp : objs) {
                stringBuilder.append(DaoCastor.ObjectToString(temp) + ",");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        return AndIn(key, stringBuilder);
    }

    public Condition AndIn(String key, StringBuilder staticSql) {
        putInMap(key + " IN (?)", staticSql.toString());
        return this;
    }

    private void putInMap(String key, Object value) {
        if (key == null || value == null) {
            throw new DaoException("Parameter could not be null!（传入参数不能为空！）");
        } else {
            CndsMap.put(key, value);
        }
    }

    public Condition OrderBy(OrderBy... orderBies) {
        order = orderBies;
        return this;
    }

    public Condition GroupBy(String... groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public Condition Pager(int pagerIndex, int length) {
        this.pager = new Pager(pagerIndex, length);
        return this;
    }

    public Map<String, Object> getCndsMap() {
        return CndsMap;
    }

    public List<Object> getValues() {
        Object[] values = new Object[CndsMap.size()];
        return new ArrayList<Object>(CndsMap.values());
    }

    public static Condition getPrimaryCondition(TableModule module,Object value){
        if (module.getPrimaryCell() != null) {
            CellModule primary = module.getPrimaryCell();
            return Condition.Where(primary.getCellName(), "=",value);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (staticCondition != null && staticCondition.length() > 0) {
            return staticCondition.toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" WHERE 1=1");
            Iterator iterator = CndsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
                String key = entry.getKey();
                String val = DaoCastor.ObjectToString(entry.getValue());
                stringBuilder.append(" AND " + key.replace("?", val));
            }
            if (groupBy != null && groupBy.length > 0) {
                stringBuilder.append(" GROUP BY ");
                for (String group : groupBy) {
                    stringBuilder.append("[" + group + "],");
                }
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
            if (order != null && order.length > 0) {
                stringBuilder.append(" ORDER BY ");
                for (OrderBy orderBy : order) {
                    stringBuilder.append(orderBy.orderInfo() + ",");
                }
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
            if (pager != null) {
                stringBuilder.append(pager.toString());
            }
            return stringBuilder.toString();
        }
    }
}