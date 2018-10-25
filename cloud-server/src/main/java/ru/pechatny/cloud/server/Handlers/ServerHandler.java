package ru.pechatny.cloud.server.Handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.pechatny.cloud.common.RequestMessage;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    String basePath = "/Users/d.pechatnikov/study/remoteStorage/";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Second handler connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMessage) {
            System.out.println("Second handler connected:" + ((RequestMessage) msg).getData());
            ctx.writeAndFlush(msg);
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
