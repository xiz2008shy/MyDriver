
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