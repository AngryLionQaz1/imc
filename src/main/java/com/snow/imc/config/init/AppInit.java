package com.snow.imc.config.init;

import  com.snow.imc.common.bean.Config;
import  com.snow.imc.common.pojo.Authority;
import  com.snow.imc.common.repository.AuthorityRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AppInit implements ApplicationRunner {

    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private Config config;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (config.getAuthorityInit())initData(getUrl());
    }



    /**
     * 查看数据库中是否存在
     */
     private boolean checkData(String url){
         return authorityRepository.findByUri(url).isPresent();
     }

     private List<Authority> makeData(List<Map<String,String>> urls){
       return   urls.stream()
                 .filter(i->!outUrl(i)&&!checkData(i.get("url")))
                 .map(i->makeData(i))
                 .collect(Collectors.toList());
     }

    /**
     * 去掉url
     */
    private boolean outUrl(Map<String,String> map){
        List<String> uls=Arrays.asList("/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/error");
        return uls.contains(map.get("url"));
    }

    /**
     * 生成数据
     */
    private Authority makeData(Map<String,String> map){
        Authority a=Authority.builder().build();
        if (map.containsKey("name"))a.setName(map.get("name"));
        if (map.containsKey("url"))a.setUri(map.get("url"));
        if (map.containsKey("details"))a.setDetails(map.get("details"));
        return a;
    }

    /**
     * 数据库
     */
    private void initData(List<Map<String,String>> urls){
        authorityRepository.saveAll(makeData(urls));
    }

    /**
     * 获取url
     */
    private List<Map<String,String>> getUrl(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo,HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String,String>> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()){
            Map<String,String> limitMap = new HashMap<>();
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns){
                limitMap.put("url",url);
            }
            HandlerMethod hm = map.get(info);
            Method m = hm.getMethod();
            ApiOperation apiOperation = m.getAnnotation(ApiOperation.class);
            Method[] me = {};
            if(apiOperation!=null) me = apiOperation.annotationType().getDeclaredMethods();
            for(Method meth : me){
                try {
                    if("notes".equals(meth.getName())){
                        String color = (String) meth.invoke(apiOperation, new  Object[]{});
                        limitMap.put("details",color);
                    }
                    if("value".equals(meth.getName())){
                        String color = (String) meth.invoke(apiOperation, new  Object[]{});
                        limitMap.put("name",color);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            urlList.add(limitMap);
        }
        return urlList;
    }
}

