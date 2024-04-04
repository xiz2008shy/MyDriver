
## jlink打包问题，主要是对非模块化的包（jdk9之前开发或是采用非模块化打包的第三方库）在项目中依赖，需要通过下面的指令手动处理

处理步骤可以分为3步

1 创建非模块化包的module-info.java文件，通过以下命令
```
jdeps --ignore-missing-deps --generate-module-info . hutool-core-5.8.27.jar
```
实际情况可能会更复杂，比如你直接依赖的非模块包依赖了其他第三方的非模块包，这种情况以上操作就需要将相关的这些包都执行到

2 对创建出的module-info.java进行编译
```
javac --patch-module cn.hutool.core=hutool-core-5.8.27.jar cn.hutool.core/module-info.java
```
如果引用了第三方库还需要通过-p指定，比如
```
javac -p .\slf4j-api-1.7.36.jar --patch-module com.rabbitmq.client=amqp-client-5.16.0.jar com.rabbitmq.client/module-info.java
```
上面的操作中对amqp-client-5.16.0.jar包的module-info.java文件编译，还指明了包中引用的其他第三方包 slf4j-api-1.7.36.jar

3 将上面编译后的module-info.class文件混进原jar包之中
```
jar uf hutool-core-5.8.27.jar -C cn.hutool.core module-info.class
```
这个操作过后就可以查看原本的jar包中多了module-info文件了

以上理解参考
https://www.cnblogs.com/zeromi/p/javaFX_package.html

截至20240405截图如下
![20240405](http://8.142.121.115:8080/crm_pack/v20240405.png)

20240405日志
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