### Bladea是一个Android敏捷开发框架：

数据库操作的灵感来自于[nutz](http://www.nutzam.com/)目前最主要的功能是用来处理Sqlite数据操作的POJO化，举个简单的栗子：

```java
Dao dao=Dao.getInstance(context);
List<UserInfo> userList= dao.query(UserInfo.class);
```

大概的，UserInfo.java会长这个样子：
```java
/**
 * Created by kmlixh on 2016/4/19.
 */
@Table("user_info")
public class UserInfo {
    @ID("user_id")
    String userId;
    @Column("user_name")
    String userName;
    @Column
    String pass_word;
    @Column
    int age;
    @Column
    float money;
    @Column
    Calendar birthday;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }
}

```
这样你就可以通过各种方法来操作你的Pojo！大概可以做的操作有以下几种：

* dao.query:数据查询类；
* dao.fetch;数据查询，单个。其实是对query查询做了limit 0,1
* dao.save;数据存储；
* dao.insert:数据插入；真心不推荐这么玩。
* dao.delete;输出删除；
* dao.create;手动创建表；话说，创建表，维护表结构这些事情，亲都不用操心。Dao会自行处理。

Dao实现的基本原理是，使用注解+反射的方式来检查Pojo的结构，完成数据库到Pojo的映射。其中用到了一些简单的注解来实现表结构的优化，如下：
* @Table，通过此注解指定表名，附加的，你可以指定一个自定义的DataOpenHelperFactory，此工厂类的作用是生成一个SQLiteOpenHelper，你可以自定义SQLiteOpenHelper来实现打开SD卡中的数据文件，甚至您可以导入一个远程的数据库文件。
* @ID，此注解用来标记表中的主索引，主键。目前只允许一张表有一个主键（虽然官方允许多个主键，但是作者近期不打算实现多主键，所以，如果你用了多主键，那就会抛出异常）
* @Column，顾名思义。@ID，@Column默认会取当前字段名作为数据库字段名称，当然，也同样允许自定义映射，可以写成@ID("user_id"),@Column("user_name")
最主要用到的字段就是以上三个，其他的比较有用的注解还有：
* @DataParseBy，指定一个继承自IDataParser.java的类，可以自定义实现数据的读取和写入过程中的转换操作。
* @Forget，这个很关键，当两个Pojo指间存在继承关系的时候，如果子类需要覆盖或者需要去除父类中的某些字段，则可以使用此标记注解到类名上。（没详细测试过，慎用，有bug就请告诉我！）
* @NotNull，此注解用来标记某个字段不能为空，这会影响到两个事情，第一是做save，update之类的操作的时候，会检查此字段是否为空；第二就是，在数据库建表的时候，会同样带上“NOT NULL”的标记。
* @Unique，主要影响建表时的sql语句，会增加“UNIQUE”标记。
* @OnConflict，顾名思义，影响同上。
* @Link，这个重点说明一下。

@Link是用来解决关联查询的问题的，诸如有一个视频，视频属于某个分类，分类Sort.java可以这么写：

```java
@Table("video_sort")
public class Sort {
    @ID(type = SqlDataType.NVARCHAR,length = 32)
    String sort_id;
    @Column
    String sort_name;
    @Link(target = Video.class,localField = "sort_id",targetField = "sort_id")
    Video[] videoList;

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public Video[] getVideoList() {
        return videoList;
    }

    public void setVideoList(Video[] videoList) {
        this.videoList = videoList;
    }
}
```
其中有一句： @Link(target = Video.class,localField = "sort_id",targetField = "sort_id")
这里可以详细说明一下，target指定目标类，localField指定当前类对应的字段名称，注意，名称为数据库中列的名称，targetField指定对应类中相同取值的字段。
所以查询的时候的条件语句会指定为targetField等于某个值。之所以这么设定则是为了保证灵活性，被@Link注解的属性，可以是一个实体对象（必须保证和target指定的类一致），
也可以是一个List或者一个数组，Dao会自动判断并处理的。

#### *关于Dao就讲这么多，其他功能尚且弱小，现在不足为表。*

