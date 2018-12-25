package com.snow.imc.module.netty;


import com.snow.imc.module.netty.message.ImProtobuf;
import io.netty.channel.Channel;

import java.util.UUID;

/**
 * IM 客户端启动入口
 * @author yinjihuan
 */
public class ImClientApp {
//	public static final String HOST = "192.168.242.1";
	public static final String HOST = "192.168.2.229";
	public static int PORT = 2222;
	public static void main(String[] args) {
		Channel channel = new ImConnection().connect(HOST, PORT);
		String id = UUID.randomUUID().toString().replaceAll("-", "");

//		ImProtobuf.Message message=ImProtobuf.Message.newBuilder().setType(Im.Message.DataType.USER_TO_USER_2).setUser2(Im.User2User2.newBuilder().setToUserName("ss").build()).build();
		ImProtobuf.Message message=ImProtobuf.Message
				.newBuilder()
				.setType(ImProtobuf.Message.DataType.USER_TO_USER_1)
				.setUser1(ImProtobuf.User2User1.newBuilder()
						.setUserId("2")
						.setUserName("萧毅")
						.setUserImg("xxxxxxxxx")
						.setToUserId("1")
						.setToUserName("冉伟")
						.setToUserImg("ssssssssssssss")
						.setContent("2222")
						.build()).build();

		channel.writeAndFlush(message);

//		ImProtobuf.Message message=ImProtobuf
//				.Message
//				.newBuilder()
//				.setType(ImProtobuf.Message.DataType.LOGIN)
//				.setLogin(ImProtobuf.Login.newBuilder().setUserId("2").build())
//				.build();
//		channel.writeAndFlush(message);










//		MessageProto.Message message = MessageProto.Message.newBuilder().setId(id).setContent("hello yinjihuan").build();
//		PersonData.Message message=PersonData.Message.newBuilder().setType().build();
//		channel.writeAndFlush(message);

		//对象传输数据
		/*Message message = new Message();
		message.setId(id);
		message.setContent("hello yinjihuan");
		channel.writeAndFlush(message);*/
		//字符串传输数据
		//channel.writeAndFlush("yinjihuan");
	}
}
