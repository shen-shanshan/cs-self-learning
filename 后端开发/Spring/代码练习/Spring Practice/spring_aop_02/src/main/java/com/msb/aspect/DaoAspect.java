package com.msb.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * 切面（Aspect）：
 * 要执行切面中的方法（通知），需要先创建该类的对象。
 * 由于切面不属于任何一层，所以用 @Component 实例化。
 */
@Aspect
@Component
public class DaoAspect {

    /**
     * 公共切点：
     * 切点表达式应直接指向接口，而不应写实现类，这样可以降低耦合度。
     */
    @Pointcut("execution(* com.msb.dao.*.add*(..))")
    public void addPointCut() {
    }

    /**
     * 前置通知：
     * 在目标切点方法之前执行。
     *
     * @param joinPoint
     */
    @Before("addPointCut()")
    public void methodBefore(JoinPoint joinPoint) {
        System.out.println("前置通知：method before invoked ...");
        // joinPoint = addUser()
        // 获取调用切点（方法）时传入的实参
        Object[] args = joinPoint.getArgs();
        System.out.println("切点方法参数列表:" + Arrays.toString(args));
    }

    /**
     * 返回通知：
     * 在切点方法执行完成并返回后执行。
     * 先于后置通知执行。
     *
     * @param joinPoint
     * @param res
     */
    @AfterReturning(value = "addPointCut()"
            , returning = "res")
    public void methodAfterReturning(JoinPoint joinPoint, Object res) {
        // res 用于接收切点方法运行完成后的返回值
        System.out.println("返回通知：method after returning invoked ...");
        System.out.println("返回值:" + res);
    }

    /**
     * 后置通知：
     * 在目标切点方法之后执行。
     * 后置通知无论是否出现异常，最终都会被执行。
     *
     * @param joinPoint
     */
    @After("addPointCut()")
    public void methodAfter(JoinPoint joinPoint) {
        System.out.println("后置通知：method after invoked ...");
    }

    /**
     * 异常通知：
     * 出现异常后才执行，不出异常就不执行。
     * 可以接收切点方法抛出的异常对象。
     *
     * @param ex
     */
    @AfterThrowing(value = "addPointCut()"
            , throwing = "ex")
    public void methodAfterThrowing(Exception ex) {
        System.out.println("异常通知：method after throwing ...");
        System.out.println(ex.getMessage());
    }

    /**
     * 环绕通知：
     * 在切点方法前、后执行增强方法。
     * 返回值必须是 Object，并且需要将切点方法继续向上返回。
     *
     * @param proceedingJoinPoint
     * @throws Throwable
     */
    @Around("addPointCut()")
    public Object methodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕通知：method around before invoked ...");
        // proceedingJoinPoint 代表切点，手动控制切点方法执行的位置
        Object res = proceedingJoinPoint.proceed();
        System.out.println("环绕通知：method around after invoked ...");
        return res;
    }

}
