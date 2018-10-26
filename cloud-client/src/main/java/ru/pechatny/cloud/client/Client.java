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
import java.util.prefs.Preferences;

public class Client implements Runnable {
    private static Client instance;
    private String host;
    private int port;
    private Socket socket;

    private Client() {
    }

    private Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Client getInstance() {
        if (instance == null) {
            Preferences preferences = Preferences.userRoot();
            String host = preferences.get("remoteHost", "localhost");
            Integer port = Integer.parseInt(preferences.get("remotePort", "8189"));
            instance = new Client(host, port);
            instance.run();
        }

        return instance;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            CommandMessage getFiles = new CommandMessage(command, argument);
            return sendRequest(getFiles);
        } catch (IOException | ClassNotFoundException e) {
            return new Object();
        }
    }

    public Object sendFile(FileMessage file) {
        try {
            return sendRequest(file);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Object();
        }
    }

    public SuccessResponse login(LoginRequest loginRequest) {
        try {
            return (SuccessResponse) sendRequest(loginRequest);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new SuccessResponse(false);
        }
    }

    public SuccessResponse registration(RegistrationRequest registrationRequest) {
        try {
            return (SuccessResponse) sendRequest(registrationRequest);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new SuccessResponse(false);
        }
    }

    private <T> Object sendRequest(T request) throws IOException, ClassNotFoundException {
        ObjectEncoderOutputStream oeos;
        ObjectDecoderInputStream odis1;
        oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
        oeos.writeObject(request);
        oeos.flush();

        odis1 = new ObjectDecoderInputStream(socket.getInputStream());

        return odis1.readObject();
    }
}
