package ru.pechatny.cloud.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.pechatny.cloud.common.Command;
import ru.pechatny.cloud.common.CommandMessage;
import ru.pechatny.cloud.common.FileList;
import ru.pechatny.cloud.common.FileMessage;
import ru.pechatny.cloud.common.LoginRequest;
import ru.pechatny.cloud.common.RegistrationRequest;
import ru.pechatny.cloud.common.SuccessResponse;

import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private String host;
    private int port;
    private Socket socket;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        ObjectEncoderOutputStream oeos = null;
        ObjectDecoderInputStream odis = null;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to server!");
    }

    public FileList getFileList(String value) {
        return (FileList) sendCommand(Command.DIRECTORY_LIST, value);
    }

    public void mkdir(String value) {
        sendCommand(Command.MKDIR, value);
    }

    public boolean deleteFile(String value) {
        return (boolean) sendCommand(Command.DELETE_FILE, value);
    }

    public boolean deleteDirectory(String value) {
        return (boolean) sendCommand(Command.DELETE_DIR, value);
    }

    private Object sendCommand(Command command, String argument) {
        ObjectEncoderOutputStream oeos;
        Object responseObject = null;

        try {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            CommandMessage getFiles = new CommandMessage(command, argument);
            oeos.writeObject(getFiles);
            oeos.flush();

            ObjectDecoderInputStream odis1 = new ObjectDecoderInputStream(socket.getInputStream());
            responseObject = odis1.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public Object sendFile(FileMessage file) {
        ObjectEncoderOutputStream oeos;
        Object responseObject = null;
        try {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            oeos.writeObject(file);
            oeos.flush();

            ObjectDecoderInputStream odis1 = new ObjectDecoderInputStream(socket.getInputStream());
            responseObject = odis1.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return responseObject;
    }

    public SuccessResponse login(LoginRequest loginRequest) {
        ObjectEncoderOutputStream oeos;
        try {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            oeos.writeObject(loginRequest);
            oeos.flush();

            ObjectDecoderInputStream odis1 = new ObjectDecoderInputStream(socket.getInputStream());

            return (SuccessResponse) odis1.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new SuccessResponse(false);
    }

    public SuccessResponse registration(RegistrationRequest registrationRequest) {
        ObjectEncoderOutputStream oeos;
        try {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            oeos.writeObject(registrationRequest);
            oeos.flush();

            ObjectDecoderInputStream odis1 = new ObjectDecoderInputStream(socket.getInputStream());

            return (SuccessResponse) odis1.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new SuccessResponse(false);
    }
}
