package com.snow.imc.module.netty;


import com.snow.im.module.netty.message.MessageProto;
import com.snow.imc.module.netty.message.ImProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.TimeUnit;

public class ImClientHandler extends SimpleChannelInboundHandler<ImProtobuf.Message> {

    private ImConnection imConnection = new ImConnection();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImProtobuf.Message msg) throws Exception {
        if (null != msg ) {
          System.out.println(msg);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("掉线了...");
        //使用过程中断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                imConnection.connect(ImClientApp.HOST, ImClientApp.PORT);
            }
        }, 2L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("长期没收到服务器推送数据");
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("长期未向服务器发送数据");
                //发送心跳包
                ctx.writeAndFlush(MessageProto.Message.newBuilder().setType(1));
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("ALL");
            }
        }
    }
}
