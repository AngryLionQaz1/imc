package com.snow.imc.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.snow.imc.common.bean.Config;
import com.snow.imc.common.pojo.Role;
import com.snow.imc.common.pojo.User;
import com.snow.imc.common.repository.UserRepository;
import com.snow.imc.config.annotation.SecurityPermission;
import com.snow.imc.config.security.SecurityContextHolder;
import com.snow.imc.config.token.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.snow.imc.common.bean.Result.auth;


@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private Config config;
    @Autowired
    private JWTToken jwtToken;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityContextHolder securityContextHolder;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) return true;
        if (hasPermission(request,handler))return true;
        response(response);
        return false;
    }

    @Override
    //整个请求执行完成后调用
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        securityContextHolder.removeUser();
    }


    private boolean hasPermission(HttpServletRequest request,Object handler) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String token=request.getHeader(config.getAuthorization());
            //获取类上的注解
            SecurityPermission requiredPermission=handlerMethod.getMethod().getDeclaringClass().getAnnotation(SecurityPermission.class);
            // 获取方法上的注解
            if (requiredPermission==null) requiredPermission = handlerMethod.getMethod().getAnnotation(SecurityPermission.class);
            if (requiredPermission==null)return true;
            if (!"".equals(requiredPermission.value())&&permission(request.getRequestURI(),requiredPermission.value()))return true;
            if (Optional.ofNullable(token).isPresent()&& jwtToken.validateToken(token)&&jwtToken.getUserId(token)!=null)return permissionUser(request.getRequestURI(),jwtToken.getUserId(token));
            return false;
    }

    private boolean permissionUser(String uri,String id){
        boolean flag=false;
        Optional<User> o=userRepository.findById(Long.valueOf(id));
        if (!o.isPresent())return false;
        List<Role> roles=o.get().getRoles();
        if (checkAdmin(roles))return setUser(o.get());
        for (int i=0;i<roles.size();i++){
            for (int j=0;j<roles.get(i).getAuthorities().size();j++){
                if (uri.equals(roles.get(i).getAuthorities().get(j).getUri())){
                    flag=setUser(o.get());
                    break;
                }
            }
        }
        return flag;
    }

    private boolean checkAdmin(List<Role> roles){
        boolean flag=false;
        for (int i=0;i<roles.size();i++){
            if (config.getAuthorityAdmin().equals(roles.get(i).getName())){
                flag=true;
                break;
            }
        }
        return flag;
    }

    private boolean setUser(User user){
        user.setRoles(new ArrayList<>());
        securityContextHolder.setUser(user);
        return true;
    }

    private boolean permission(String uri,String value){
        boolean flag=false;
        String[]s = value.split(",");
        Set<String> strings=new LinkedHashSet<>();
        Arrays.stream(s).forEach((v)->strings.add(v));
        String[] ss=uri.split("/");
        for (int i=0;i<ss.length;i++){
            if (strings.contains(ss[i])){
                flag=true;
                break;
            }
        }
        return flag;
    }

    /**
     * 返回错误信息
     */
    public void response(HttpServletResponse response){
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter out= null;
        try {
            out = response.getWriter();
            out.write(JSON.toJSONString(auth()));
            out.flush();
        } catch (IOException e) {
        }finally {
            out.close();
        }

    }

}
