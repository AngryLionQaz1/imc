package com.snow.imc.module.netty;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负责监听启动时连接失败，重新连接功能
 * @author yinjihuan
 *
 */
public class ConnectionListener implements ChannelFutureListener {
	
	private ImConnection imConnection = new ImConnection();
	private static AtomicInteger count=new AtomicInteger();
	
	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		if (!channelFuture.isSuccess()) {
			System.out.println(count);
			if (count.get()>4)return;
			count.addAndGet(1);
			final EventLoop loop = channelFuture.channel().eventLoop();
			loop.schedule(new Runnable() {
				@Override
				public void run() {
					System.err.println("服务端链接不上，开始重连操作...");
					imConnection.connect(ImClientApp.HOST, ImClientApp.PORT);
				}
			}, 2L, TimeUnit.SECONDS);
		} else {
			count.set(0);
			System.err.println("服务端链接成功...");
		}
	}
}
