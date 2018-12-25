package com.snow.imc.config.security;

import com.snow.imc.common.pojo.User;
import org.springframework.stereotype.Component;

@Component
public  class SecurityContextHolder {

    private static ThreadLocal<User> securityContext=new ThreadLocal<>();

    public void setUser(User user){
        securityContext.set(user);
    }

    public void removeUser(){
        securityContext.remove();
    }


    public User getUser(){
     return securityContext.get();
    }






}

