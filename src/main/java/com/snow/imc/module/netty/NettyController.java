package com.snow.imc.module.netty;

import com.snow.imc.common.pojo.User;
import com.snow.imc.module.netty.message.ImProtobuf;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Netty")
@RestController
@RequestMapping("netty")
public class NettyController {


    @Autowired
    private Channel channel;
    @Autowired
    private Protobuf protobuf;


    @PostMapping("addFriend")
    @ApiOperation(value = "添加好友")
    public void addFriend(@ApiParam(value = "用户ID",required = true)@RequestParam Long userId,
                          @ApiParam(value = "其他用户ID",required = true)@RequestParam Long toUserId){
        User user= User.builder().id(userId).username("萧毅").img("sssssss").build();
        User toUser= User.builder().id(toUserId).username("冉伟").img("sssssss").build();
        channel.writeAndFlush(protobuf.user2user2(user,toUser));
    }

    @PostMapping("agree")
    @ApiOperation(value = "回复好友申请")
    public void agree(@ApiParam(value = "用户ID",required = true)@RequestParam Long userId,
                      @ApiParam(value = "其他用户ID",required = true)@RequestParam Long toUserId,
                      @ApiParam(value = "类型 0 拒绝 1 同意",required = true)@RequestParam Integer type){
        User user= User.builder().id(userId).username("萧毅").img("sssssss").build();
        User toUser= User.builder().id(toUserId).username("冉伟").img("sssssss").build();
       channel.writeAndFlush(protobuf.user2user3(user,toUser,type));
    }

    @PostMapping("msg")
    @ApiOperation(value = "发送消息")
    public void msg(@RequestParam String userId,
                    @RequestParam String toUserId,
                    @RequestParam String content){

        ImProtobuf.Message message=ImProtobuf
                .Message
                .newBuilder()
                .setType(ImProtobuf.Message.DataType.USER_TO_USER_1)
                .setUser1(ImProtobuf.User2User1.newBuilder()
                        .setUserId(userId)
                        .setUserName("萧毅")
                        .setUserImg("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2310514390,3580363630&fm=27&gp=0.jpg")
                        .setToUserId(toUserId)
                        .setToUserImg("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=764856423,3994964277&fm=27&gp=0.jpg")
                        .setToUserName("冉伟")
                        .setContent(content)
                        .build())
                .build();
        channel.writeAndFlush(message);
    }

    @GetMapping("logout")
    @ApiOperation(value = "退出")
    public void logout(@RequestParam String userId){
        ImProtobuf.Message message=ImProtobuf
                .Message
                .newBuilder()
                .setType(ImProtobuf.Message.DataType.LOGOUT)
                .setLogout(ImProtobuf.Logout.newBuilder().setUserId(userId).build())
                .build();
        channel.writeAndFlush(message);
    }

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public void login(@RequestParam String userId){
        ImProtobuf.Message message=ImProtobuf
                .Message
                .newBuilder()
                .setType(ImProtobuf.Message.DataType.LOGIN)
                .setLogin(ImProtobuf.Login.newBuilder().setUserId(userId).build())
                .build();
        channel.writeAndFlush(message);
    }









}
