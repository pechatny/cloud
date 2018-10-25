package ru.pechatny.cloud.server.Handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.pechatny.cloud.common.FileMessage;
import ru.pechatny.cloud.common.RequestMessage;
import ru.pechatny.cloud.server.Server;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler extends ChannelInboundHandlerAdapter {
    private String storagePath;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        storagePath = Server.properties.getProperty("storage.path");
        System.out.println("File handler connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String basePath = storagePath + ctx.channel().attr(Server.USER_DIR_ATTR).get() + "/";

        if (msg instanceof FileMessage) {
            System.out.println("File handler connected");

            String fullPath = basePath + ((FileMessage) msg).getPath();
            Path filePath = Paths.get(fullPath);
            for (int i = 1; i < filePath.getNameCount(); i++) {
                String subpathString = "/" + filePath.subpath(0, i).toString() + "/";
                Path subpath = Paths.get(subpathString);
                if (Files.notExists(subpath)) {
                    Files.createDirectory(subpath);
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
            RequestMessage responseMessage = new RequestMessage("File saved:" + ((FileMessage) msg).getPath());
            fileOutputStream.write(((FileMessage) msg).getData());
            System.out.println("File saved:" + ((FileMessage) msg).getPath());
            ctx.writeAndFlush(responseMessage);
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
