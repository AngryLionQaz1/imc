package com.snow.imc.common.mapper;


import com.snow.imc.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from s_user")
    List<User> findAll();



}
