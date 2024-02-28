package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j

/*
*   AOP编程
*   插入和更新时 操作人 操作时间 更新人 更新时间的代码重复性很大
*   所以面向切面编程 减少代码冗余
*
* */
public class AutoFillAspect {

    /*
    * 切入点
    * */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /*
    * 前置通知
    * */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//从方法上获取注解对象
        OperationType operationType = autoFill.value();//获取数据库操作类型

        //获取到方法参数
        Object[] args = joinPoint.getArgs();

        if (args.length==0 || args==null){
            return;
        }

        //为方便这里提取方法的第一个参数为实体类,后面编程时调用mapper层传参第一个都为实体类
        Object entity = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        //根据不同的操作类型,为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){

            //AutoFillConstant为实现定义好的常量,在sky-common.constant里面,防止方法名写错
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            //通过反射为对象属性赋值
            setCreateTime.invoke(entity,now);
            setCreateUser.invoke(entity,id);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,id);

        }else if (operationType == OperationType.UPDATE){

            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,id);


        }

    }
}
