package com.tom.config;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SqlSessionInvokeHandler<T> implements InvocationHandler {

    private SqlSession sqlSession;

    private T target;

    public SqlSessionInvokeHandler(SqlSession sqlSession, T target) {
        this.sqlSession = sqlSession;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 通过反射调用目标方法
        Object result = method.invoke(target, args);
        sqlSession.close();
        return result;
    }
}
