package io.pp.arcade.v1.admin.aspect;


import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@RequiredArgsConstructor
@Component
public class AdminRoleAspect {
    private final TokenService tokenService;

    @Pointcut("execution(public * io.pp.arcade.v1.admin..*(..)) &&" +
            " @target(org.springframework.web.bind.annotation.RestController)")
    private void adminController(){}

    @Around("adminController()")
    public Object checkRoleUser(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        UserDto user = tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        if (user.getRoleType() != RoleType.ADMIN)
            return forbidden(response);
        return joinPoint.proceed();
    }

    private Object forbidden(HttpServletResponse response) {
        response.setStatus(HttpStatus.SC_FORBIDDEN);
        return null;
    }
}
