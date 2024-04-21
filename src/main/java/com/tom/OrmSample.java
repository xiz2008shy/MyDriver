package com.tom;

/*import com.dream.drive.config.DreamProperties;
import com.dream.drive.factory.DefaultDriveFactory;
import com.dream.drive.factory.DriveFactory;
import com.dream.drive.listener.DebugListener;*/
//import com.dream.util.exception.DreamRunTimeException;
import com.dream.util.common.LowHashMap;
import com.dream.util.reflection.wrapper.BeanObjectFactoryWrapper;
import com.tom.config.MySetting;
import com.tom.entity.FileRecord2;
import com.zaxxer.hikari.HikariDataSource;

import java.lang.invoke.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import com.dream.util.reflect.*;


public class OrmSample {

    public static void main(String[] args) {
        /*MySetting.initSetting(MySetting.mockParam());
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://rm-bp12x6y0jvne23m63ko.mysql.rds.aliyuncs.com:3306/u_db");
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("xiuai530@");
        List<String> packageList = Arrays.asList("com.tom");
        String sql = "create table if not exists file_record_table2\n" +
                "(\n" +
                "    id                int auto_increment\n" +
                "        primary key,\n" +
                "    file_name         varchar(255) not null,\n" +
                "    relative_location varchar(500) not null,\n" +
                "    base_id           int          not null\n" +
                ");\n";
        DreamProperties dreamProperties = new DreamProperties();
        dreamProperties.setListeners(new String[]{DebugListener.class.getName()});
        DriveFactory driveFactory = new DefaultDriveFactory(hikariDataSource, packageList, packageList, dreamProperties);
        driveFactory.jdbcMapper().execute(sql);
        driveFactory.templateMapper().selectById(FileRecord2.class, 2);*/


        /*Supplier<Object> objectSupplier = constructorCreator(FileRecord2.class);
        System.out.println(objectSupplier);
        System.out.println(objectSupplier.get());*/

        BeanObjectFactoryWrapper beanObjectFactoryWrapper = new BeanObjectFactoryWrapper(FileRecord2.class);
        System.out.println(beanObjectFactoryWrapper);
    }

}

