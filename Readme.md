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
```
实际情况可能会更复杂，比如你直接依赖的非模块包依赖了其他第三方的非模块包，这种情况以上操作就需要将相关的这些包都执行到

2 对创建出的module-info.java进行编译(本项目中需要使用)
```shell
javac --patch-module cn.hutool.core=hutool-core-5.8.27.jar cn.hutool.core/module-info.java
javac --patch-module org.slf4j=slf4j-api-1.7.36.jar org.slf4j/module-info.java
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
https://github.com/JavaFX-Starter/JavaFX-Package-Sample
https://docs.oracle.com/en/java/javase/17/docs/specs/man/jpackage.html
https://www.bilibili.com/video/BV1BK4y1W72q/?spm_id_from=333.337.search-card.all.click&vd_source=72d894bf4fb5c4389e3a57d06cb8161b


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

### 20240417
引入log4j2，增加了日志文件输出和输出位置动态调整

其中遇到的几个问题，包括1.slf4j的门面在打包时可能遇到的非模块化问题，2.给org.slf4j混打module-info.class后，又会遇到使用log4j-core中的类时
出现下面这个异常
> Caused by: java.lang.IllegalAccessError: class org.slf4j.LoggerFactory (in module org.slf4j) cannot access class org.slf4j.impl.StaticLoggerBinder (in module org.apache.logging.log4j.slf4j.impl) because module org.slf4j does not read module org.apache.logging.log4j.slf4j.impl

解决方案是添加vm参数，或者在module-info中添加require，但这里由于slf4j并没有直接引用log4j，所以也不好直接在slf4j-api的module-info中申明，
所以这里选择了添加vm参数的方式
```java
--add-reads org.slf4j=org.apache.logging.log4j.slf4j.impl
```

我猜应该还有其他方式，比如可以通过引用sfl4j官方的一些包可以解决上面的问题，理论上只要官方某个包里把module-info准备好就可以解决上面的问题

至于怎么给org.slf4j混打module-info，指令也都在前面打包的部分补充进去了



