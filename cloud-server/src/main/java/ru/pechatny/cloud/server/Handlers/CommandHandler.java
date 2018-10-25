package ru.pechatny.cloud.server.Handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.pechatny.cloud.common.Command;
import ru.pechatny.cloud.common.CommandMessage;
import ru.pechatny.cloud.common.FileList;
import ru.pechatny.cloud.server.Server;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CommandHandler extends ChannelInboundHandlerAdapter {
    private String storagePath;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        storagePath = Server.properties.getProperty("storage.path");
        System.out.println("Command handler connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String basePath = storagePath + ctx.channel().attr(Server.USER_DIR_ATTR).get() + "/";

        if (msg instanceof CommandMessage) {
            CommandMessage command = (CommandMessage) msg;
            if (command.getCommand() == (Command.DIRECTORY_LIST)) {
                Path baseDirPaths = Paths.get(basePath + command.getArgument());

                try {
                    final FileList fileList = new FileList();
                    Files.newDirectoryStream(baseDirPaths).forEach(item -> fileList.add(item, basePath));
                    ctx.writeAndFlush(fileList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (command.getCommand() == (Command.DELETE_FILE)) {
                Path deletePath = Paths.get(basePath + command.getArgument());

                try {
                    Files.delete(deletePath);
                    ctx.writeAndFlush(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (command.getCommand() == (Command.MKDIR)) {
                Path directory = Paths.get(basePath + command.getArgument());

                try {
                    if (!Files.exists(directory)) {
                        Files.createDirectory(directory);
                    }

                    ctx.writeAndFlush(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (command.getCommand() == (Command.DELETE_DIR)) {
                Path directory = Paths.get(basePath + command.getArgument());

                try {
                    if (Files.exists(directory) && Files.isDirectory(directory)) {
                        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                Files.delete(file);
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                                Files.delete(dir);
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    }
                    Files.deleteIfExists(directory);

                    ctx.writeAndFlush(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
