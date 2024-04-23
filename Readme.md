# MyDriver
我写MyDriver的目标就是想实现一个私人的文件同步客户端，类似oneDrive这样，并且预期会将处理逻辑集中在客户端上，省去服务端，远端只租一个数据库和
云文件服务。javafx算是我最近的新玩具，所以优化空间还比较大，有很长一段时间好肝了

后面记录一些遇到的问题和更新日志...

## jlink打包问题
主要是项目中依赖非模块化的包的场合，可能遇到的问题，可以通过下面的指令手动处理（jdk9之前开发或是采用非模块化打包的第三方库）

处理步骤可以分为3步

1 创建非模块化包的module-info.java文件，通过以下命令(本项目中需要使用)
```shell
jdeps --ignore-missing-deps --generate-module-info . hutool-core-5.8.27.jar
jdeps --ignore-missing-deps --generate-module-info . slf4j-api-1.7.36.jar
jdeps --ignore-missing-deps --generate-module-info . mybatis-3.5.16.jar
jdeps --ignore-missing-deps --generate-module-info . mysql-connector-j-8.3.0.jar
```
实际情况可能会更复杂，比如你直接依赖的非模块包依赖了其他第三方的非模块包，这种情况以上操作就需要将相关的这些包都执行到

2 对创建出的module-info.java进行编译(本项目中需要使用)
```shell
javac --patch-module cn.hutool.core=hutool-core-5.8.27.jar cn.hutool.core/module-info.java
javac --patch-module org.slf4j=slf4j-api-1.7.36.jar org.slf4j/module-info.java
javac --patch-module org.mybatis=mybatis-3.5.16.jar org.mybatis/module-info.java
javac --patch-module mysql.connector.j=mysql-connector-j-8.3.0.jar mysql.connector.j/module-info.java
```
如果这个第三方库本身还引用了其他第三方库，需要通过-p指定，举例如下(这个例子来源参考资料，myDriver暂时没有遇到)
```
javac -p .\slf4j-api-1.7.36.jar --patch-module com.rabbitmq.client=amqp-client-5.16.0.jar com.rabbitmq.client/module-info.java
```
上面的操作中对amqp-client-5.16.0.jar包的module-info.java文件编译，还指明了包中引用的其他第三方包 slf4j-api-1.7.36.jar

3 将上面编译后的module-info.class文件混进原jar包之中(本项目中需要使用)
```shell
jar uf hutool-core-5.8.27.jar -C cn.hutool.core module-info.class
jar uf slf4j-api-1.7.36.jar -C org.slf4j module-info.class
jar uf mybatis-3.5.16.jar -C org.mybatis module-info.class
jar uf mysql-connector-j-8.3.0.jar -C mysql.connector.j module-info.class
```
这个操作过后就可以查看原本的jar包中多了module-info文件了

以上理解参考
https://www.cnblogs.com/zeromi/p/javaFX_package.html

## exe打包方式
1.先执行javafx插件，通过j-link打包，完成后可以在./target/build-link/bin目录下找到MyDriver.bat
双击测试是否正常运行，能够运行说明j-link打包成功
2.接着执行以下jpackage命令（需要注意的是不能直接用cmd执行，建议使用powershell，23版的idea可以在直接在md里一键运行 好方便~）
```shell
jpackage --type app-image -n MyDriver -m "my_driver/com.tom.JavafxMain"  --icon ".\src\main\resources\favicon.ico" --java-options --enable-preview --runtime-image ".\target\build-link\" --dest ".\target\build-package" 
```

上面这个命令也可以通过maven命令执行，因为项目里配了exec-maven-plugin插件，idea的话可以在maven，Run Configurations里找到这个快捷命令直接执行
```shell
mvn exec:exec@image -f pom.xml
```

综合一下，总体的打包命令可以分成下面几种方式
方式一
```shell
mvn clean
mvn javafx:jlink
mvn exec:exec@image -f pom.xml
```
打包输出./target/build-package/MyDriver

方式二
```shell
mvn clean
mvn package
mvn exec:exec@imageFromPackage -f pom.xml
```
打包输出./target/mvnPackage/MyDriver

目录下看到MyDriver.exe了，输出路径也可以在pom文件中进行调整

jpackage打包部分参考了 
* https://github.com/JavaFX-Starter/JavaFX-Package-Sample
* https://docs.oracle.com/en/java/javase/17/docs/specs/man/jpackage.html
* https://www.bilibili.com/video/BV1BK4y1W72q/?spm_id_from=333.337.search-card.all.click&vd_source=72d894bf4fb5c4389e3a57d06cb8161b


截至20240405截图如下

![20240405](http://8.142.121.115:8080/crm_pack/v20240405.png)

### 20240405日志
#### 地址栏部分-已实现
1.后退按钮
2.路径拆分展示/跳转
3.搜索框

##### 待实现
搜索按钮图标优化，图标点击事件待添加

##### 文件排列部分-已实现
1.文件图标滚动排列展示
2.鼠标悬浮变色
3.鼠标点击变色
4.鼠标双击操作

##### 待实现
文件整理-多维度分类排序
文件辅助信息展示

#### 外边框windows部分
ui展示完成
左上4图标点击事件待完成
resize事件待优化
切卡及标题有待添加

#### 同步服务部分有待添加


### 20240406
调整了一些样式
窗口的基本功能初步实现了，样式也添加了，简单模仿win11的窗口
打包配置目前都调通了，第一次打成了exe，激动~

### 20240407
添加了标签页，鼠标效果基本实现

![20240407](http://8.142.121.115:8080/crm_pack/v20240407.png)

调整了RecWindows类，完善了tab和传入视图的关联功能，剩下再补充右键菜单基本框架就完善了~

### 20240413
整体窗体的框架基本完善
<ul>
    <li>标签切卡切换事件完成</li>
    <li>标签切卡相关的菜单事件完善</li>
    <li>标签切卡的关闭事件完成</li>
    <li>优化了右键菜单的位置</li>
    <li>右键菜单打开、新标签页打开功能实现</li>
    <li>右键菜单部分交互问题和操作问题修复</li>
</ul>

![20240413](http://8.142.121.115:8080/crm_pack/v20240413.png)

### 20240414~15
将除了窗体部分外的布局用fxml重写了一边，让更多的布局放到fxml中，和逻辑部分分离


### 20240417
引入log4j2，增加了日志文件输出和输出位置动态调整

其中遇到的几个问题，包括
* 1.slf4j-api的门面在打包时可能遇到的非模块化包在jlink打包时提示异常（因为jlink、jpackage就不支持对自动模块打包）
* 2.给org.slf4j混打module-info.class后，又会遇到使用log4j-core中的类时
出现下面这个异常
> Caused by: java.lang.IllegalAccessError: class org.slf4j.LoggerFactory (in module org.slf4j) cannot access class org.slf4j.impl.StaticLoggerBinder (in module org.apache.logging.log4j.slf4j.impl) because module org.slf4j does not read module org.apache.logging.log4j.slf4j.impl

解决方案是添加vm参数，或者在module-info中添加require，但这里由于slf4j并没有直接引用log4j，所以也不好直接在slf4j-api的module-info中申明，
所以这里选择了添加vm参数的方式
```vm option
--add-reads org.slf4j=org.apache.logging.log4j.slf4j.impl
```

我推测应该还有其他方式，比如可以通过引用sfl4j官方的一些包可以解决上面的问题，按理说上只要官方某个包里把module-info准备好就可以解决上面的问题

至于怎么给org.slf4j混打module-info，指令也都在前面打包的部分补充进去了

### 20240418
打包问题新的一个解决方式是使用log4j-slf4j2-impl依赖替换log4j-slf4j-impl，两者的区别就是对应slf4j-api 1.7之前和2.0之后的api
而slf4j-api 2.0开始支持JPMS的项目，1.7的api还是采用静态绑定的方式，这对非JPMS的应用来说没有感知，但对模块化应用就很明显。将log4j-slf4j-impl替换成log4j-slf4j2-impl问题就解决了，不需要那些启动参数了
国内对于slf4j+log4j2的文章就没有看到有提log4j-slf4j2-impl这个包的，去到log4j官网仔细看，确实发现官网有提到这点
> Due to a break in compatibility in the SLF4J binding, as of release 2.19.0 two SLF4J to Log4j Adapters are provided.
> * log4j-slf4j-impl should be used with SLF4J 1.7.x releases or older.
> * log4j-slf4j2-impl should be used with SLF4J 2.0.x releases or newer.

> Applications that take advantage of the Java Module System should use SLF4J 2.0.x and log4j-slf4j2-impl.

参考链接：
* https://logging.apache.org/log4j/2.x/log4j-slf4j-impl.html
* https://github.com/qos-ch/slf4j/issues/415


### 20240420
引入mybatis遇到的相关问题

1.在JPMS项目中，需要对相关的mapper包和实体包声明open和export，但这些操作在异常中没有很好的提示
我也猜测可能是mybatis需要用到我mapper包和映射的实体类，所以对相关模块进行open和export声明，第一个问题就解决了
```log
Exception in thread "main" org.apache.ibatis.binding.BindingException: Type interface com.tom.mapper.FileRecordMapper is not known to the MapperRegistry.
	at org.mybatis@3.5.16/org.apache.ibatis.binding.MapperRegistry.getMapper(MapperRegistry.java:47)
	at org.mybatis@3.5.16/org.apache.ibatis.session.Configuration.getMapper(Configuration.java:940)
	at org.mybatis@3.5.16/org.apache.ibatis.session.defaults.DefaultSqlSession.getMapper(DefaultSqlSession.java:291)
	at my_driver/com.tom.MT.main(MT.java:23)
```
我后来又测试了一下，mapper包必须声明open，export不行，说明mybatis一定有对mapper包反射操作。

就这个提示而言，又是一个搜破论坛都找不到原因的问题，还会看到很多其他配置错误导致这个问题的情况，但很遗憾不能解决我这种情况，官方的提示很值得吐槽。

2.第二个问题是 accessExternalDTD 属性设置的限制导致不允许 'http' 访问
```log
Caused by: org.xml.sax.SAXParseException; lineNumber: 3; columnNumber: 55; 外部 DTD: 无法读取外部 DTD 'mybatis-3-config.dtd', 因为 accessExternalDTD 属性设置的限制导致不允许 'http' 访问。
	at java.xml/com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:204)
	at java.xml/com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.fatalError(ErrorHandlerWrapper.java:178)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:400)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:327)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLScanner.reportFatalError(XMLScanner.java:1471)
```
这个问题实际在我昨天解决上面问题之后并没有遇到，但到了第二天再跑时，抛出了这个异常。

解决方案有几种，我自己确定的有两种
* 1.通过添加vm启动参数 `-Djavax.xml.accessExternalDTD=all` ，可以解决上述问题
* 2.通过修改jdk目录中jaxp.properties文件，添加以下配置，值得一提的是jdk8之后jre就可以根据项目需要动态生成了，所以网上看到的修改路径`%JAVA_HOME%\jre\lib\`这种都是对j8和之前的，在j8后jaxp.properties处在`%JAVA_HOME%\conf`路径下
```config
javax.xml.accessExternalSchema=all
javax.xml.accessExternalDTD=all
```
* 3.还有一种方案是说把相关的dtd文件保存到本地，然后xml里修改dtd的路径为file://xxxx这种，我没再试了，仅作记录

3.第三个问题是mybatis混module-info遇到，module-info编译class时抛出异常，如下
```shell
javac --patch-module org.mybatis=mybatis-3.5.16.jar org.mybatis/module-info.java

org.mybatis\module-info.java:13: 错误: 程序包为空或不存在: org.apache.ibatis
    exports org.apache.ibatis;
                      ^
1 个错误
```
我看了org.apache.ibatis包下确实只有一个package-info.class，外部并不需要使用。

解决方法就是到module-info.java中把 `export org.apache.ibatis;`  删掉，再次执行就不报错了。

最新情况截图如下

![20240420](http://8.142.121.115:8080/crm_pack/v20240420.png)
新增
* 设置窗口基本布局完成
* 大部分交互逻辑已经完成

待处理
* apply按钮事件待写
* 配置更新后的刷新原界面待处理
* 数据库交互部分逻辑有待处理（比如说测试链接之类的）

好在数据库处理类的结构已经构思好了，基本用例也都调试完了，后面处理起来应该会快很多，剩下就是专注我们的业务场景了。

最近一段时间，这个项目写下来最大的感受是在spring外写东西，就容易出现方法传参过多的问题，参数存在哪里，怎么取需变得至关重要，
当需要传4以上参数的时候，把这些参数封装到一个对象里去是一种做法，但这样又会让这个对象的通用性降低，而这个对象的创建又需
要有额外的代码，用spring时很少会关注到这件事，因为对象都是直接被spring注入的，好处是封装对象的含义会比较清晰；另一种做法就考虑做一个容器，
来存取需要的参数，这种思路其实和spring差不多，这样当某个对象需要在多处被获取时，可以直接从容器中取，如果是个静态容器提供些静态方法，存取也变得简单；
但即使这样不得不考虑另一件事，是这些对象中到底哪些参数是被需要的。当我传递了一个拥有很多成员的对象时，我开始思考这个问题，这个对象的每一处的成员用意变得模糊，
原因是这个对象被用在了多个地方，而不同的方法在使用这个对象中的成员也不尽相同，开始可能问题不大，但越到后面这种对原本对象中的成员限制就对越，需要考虑对之前
逻辑的兼容，然后为了解决这个问题就变成不用原来的成员，转而增加这个对象的成员，一个恶性循环就此开始了...

实际上，上面问题的一系列思考，在工作中也会遇到，但通常如果某个实体类的第一作者不是自己的情况下，稳妥起见我是会选择自己声明一个成员用来传递参数，避免后续的麻烦...随便写点小结吧

![20240421](http://8.142.121.115:8080/crm_pack/v20240423.png)

mybatis问题展示得到解决了，其实排查下来存在两个问题
* 第一个问题非常隐蔽是由于mysql的驱动jar包没有混module-info，而这个问题之所以很隐蔽一方面是由于jlink打包时完全没有提示，不报错。
我尝试不用mybatis，直接jdbc操作后发现，会有ClassNotFoundException，报驱动不存在，在尝试混打module-info后，并且require这个驱动模块后就正常了

在此也补充下module-info中的关键词uses,说是用SPI接口是就写uses，对应的SPI实现模块中用provides关键词

* 第二个问题是mybatis的Resource类中getResourceAsStream在我测试中就经常有返回的inputStream是null的情况，具体有待后续验证。总之connection test在打包后正常了。


mybatis配置中mapper指定package或者resource，在jlink打包后存都在问题，已经定位了问题出在VFS
```xml
<mappers>
    <package name="com.tom.mapper"/>
</mappers>
```
```java
protected static List<URL> getResources(String path) throws IOException {
    return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
}
```
这段代码中Thread.currentThread().getContextClassLoader().getResources(path)的部分在jlink打包后返回的是null。

目前jlink打包后如何遍历包路径变成了一个不知道怎么处理的问题，这些问题对于jar包或者位未打包时都很容易解决，而那些方法都不适用jlink打包后，当然有一种是通过spring进行，
这种方式我还未测试，不过当前项目里我是不准备在引入spring的，所以迂回一下还是有一些其他方式可以处理，我发现指定resource其实还是好处理的，只要改改XMLConfigBuilder中找资源的方式
,从classLoader的getResource方法调整导this.getClass().getResource("..")这样，获取资源还是ok的，只是免不了自己手动加一下具体的mapper...
