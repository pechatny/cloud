package ru.pechatny.cloud.server.Handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.pechatny.cloud.common.LoginRequest;
import ru.pechatny.cloud.common.RegistrationRequest;
import ru.pechatny.cloud.common.SuccessResponse;
import ru.pechatny.cloud.server.Server;
import ru.pechatny.cloud.server.dao.UserDao;
import ru.pechatny.cloud.server.dao.UserDaoImpl;
import ru.pechatny.cloud.server.models.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class LoginHandler extends ChannelInboundHandlerAdapter {

    private String storagePath;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        storagePath = Server.properties.getProperty("storage.path");
        System.out.println("Login handler connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof LoginRequest) {

            LoginRequest request = (LoginRequest) msg;

            UserDao userDao = new UserDaoImpl();
            Optional<User> user = userDao.get(request.getLogin());
            if (!user.isPresent()) {
                SuccessResponse loginResponse = new SuccessResponse(false);
                ctx.writeAndFlush(loginResponse);
            }

            if (user.get().password.equals(request.getPassword())) {
                SuccessResponse loginResponse = new SuccessResponse(true);
                ctx.channel().attr(Server.USER_DIR_ATTR).set(request.getLogin());
                ctx.writeAndFlush(loginResponse);
                ctx.pipeline().remove(this);
            } else {
                SuccessResponse loginResponse = new SuccessResponse(false);
                ctx.writeAndFlush(loginResponse);
            }
        }

        if (msg instanceof RegistrationRequest) {
            RegistrationRequest request = (RegistrationRequest) msg;

            UserDao userDao = new UserDaoImpl();
            boolean result = userDao.save(request.getLogin(), request.getPassword());
            SuccessResponse loginResponse = new SuccessResponse(result);
            if (result) {
                Path userDirectoryPath = Paths.get(storagePath + request.getLogin() + "/");
                if (Files.notExists(userDirectoryPath)) {
                    Files.createDirectory(userDirectoryPath);
                }
            }

            ctx.writeAndFlush(loginResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
