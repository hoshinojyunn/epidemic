package com.cdut.epidemicsyscontrolframework.filters;

import com.cdut.epidemicsyscontrolcommon.utils.JWTUtil;
import com.cdut.epidemicsyscontrolcommon.utils.RedisCache;
import com.cdut.pojo.LoginUser;
import com.cdut.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisCache redisCache;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("token");
        // 如果不携带token
        if(!StringUtils.hasText(token)){
            //交由后续filter处理
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String username = null;
        try {
            username = JWTUtil.getUsername(token);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        LoginUser loginUser  = redisCache.getCacheObject(username);
        // 用户注销后 即使携带token 在redis中也查不出来 应该先判断是否在redis中
        if(Objects.isNull(loginUser))
            throw new RuntimeException("用户未登录");
        // TODO: 2022/8/30 赋予权限
        // 封装用户信息存入context
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        // TODO: 2022/8/29 完成jwt过滤
    }
}
