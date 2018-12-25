package com.snow.imc.module.netty;

import io.netty.channel.Channel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass
public class NettyConfig {

//    public static final String HOST = "192.168.242.1";
public static final String HOST = "192.168.2.229";

    public static int PORT = 2222;

   @Bean
   @ConditionalOnMissingBean
   public Channel channel(){
     return new ImConnection().connect(HOST, PORT);
   }







}
