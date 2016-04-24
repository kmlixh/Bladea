### Bladea是一个Android敏捷开发框架：

目前最主要的功能是用来处理Sqlite数据操作的POJO化：
		
    	即使用POJO来作为数据库操作的基本元素，不再过度关注数据库结构。让用户从繁杂的数据操作中解放出来！放个例子出来：
        

```java
Dao dao=Dao.getInstance();
dao.query(UserInfo.class);
```
