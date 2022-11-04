package io.pp.arcade.v1.global.trace.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class TracePointcut {
    @Pointcut("execution(* io.pp.arcade.v1.domain..*(..))")
    public void allDomain(){}

    //타입 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){}

    //allOrder && allService
    @Pointcut("allDomain() && allService()")
    public void orderAndService(){}
}
