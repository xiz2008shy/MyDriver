package com.tom.mybatis;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MyBaseBuilder {
    protected final MyConfiguration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public MyBaseBuilder(MyConfiguration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public MyConfiguration getConfiguration() {
        return configuration;
    }

    protected Pattern parseExpression(String regex, String defaultValue) {
        return Pattern.compile(regex == null ? defaultValue : regex);
    }

    protected Boolean booleanValueOf(String value, Boolean defaultValue) {
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    protected Integer integerValueOf(String value, Integer defaultValue) {
        return value == null ? defaultValue : Integer.valueOf(value);
    }

    protected Set<String> stringSetValueOf(String value, String defaultValue) {
        value = value == null ? defaultValue : value;
        return new HashSet<>(Arrays.asList(value.split(",")));
    }

    protected JdbcType resolveJdbcType(String alias) {
        try {
            return alias == null ? null : JdbcType.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving JdbcType. Cause: " + e, e);
        }
    }

    protected ResultSetType resolveResultSetType(String alias) {
        try {
            return alias == null ? null : ResultSetType.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
        }
    }

    protected ParameterMode resolveParameterMode(String alias) {
        try {
            return alias == null ? null : ParameterMode.valueOf(alias);
        } catch (IllegalArgumentException e) {
            throw new BuilderException("Error resolving ParameterMode. Cause: " + e, e);
        }
    }

    protected Object createInstance(String alias) {
        Class<?> clazz = resolveClass(alias);
        try {
            return clazz == null ? null : clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BuilderException("Error creating instance. Cause: " + e, e);
        }
    }

    protected <T> Class<? extends T> resolveClass(String alias) {
        try {
            return alias == null ? null : resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }

    protected TypeHandler<?> resolveTypeHandler(Class<?> javaType, String typeHandlerAlias) {
        if (typeHandlerAlias == null) {
            return null;
        }
        Class<?> type = resolveClass(typeHandlerAlias);
        if (type != null && !TypeHandler.class.isAssignableFrom(type)) {
            throw new BuilderException(
                    "Type " + type.getName() + " is not a valid TypeHandler because it does not implement TypeHandler interface");
        }
        @SuppressWarnings("unchecked") // already verified it is a TypeHandler
        Class<? extends TypeHandler<?>> typeHandlerType = (Class<? extends TypeHandler<?>>) type;
        return resolveTypeHandler(javaType, typeHandlerType);
    }

    protected TypeHandler<?> resolveTypeHandler(Class<?> javaType, Class<? extends TypeHandler<?>> typeHandlerType) {
        if (typeHandlerType == null) {
            return null;
        }
        // javaType ignored for injected handlers see issue #746 for full detail
        TypeHandler<?> handler = typeHandlerRegistry.getMappingTypeHandler(typeHandlerType);
        // if handler not in registry, create a new one, otherwise return directly
        return handler == null ? typeHandlerRegistry.getInstance(javaType, typeHandlerType) : handler;
    }

    protected <T> Class<? extends T> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
