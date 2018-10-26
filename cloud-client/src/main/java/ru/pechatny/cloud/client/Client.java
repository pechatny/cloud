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

public class Client {
    private static Client instance;
    private Socket socket;

    private Client() {
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }

        if (instance.socket == null) {
            instance.connect();
        }

        return instance;
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

    private void connect() {
        Preferences preferences = Preferences.userRoot();
        String host = preferences.get("remoteHost", "localhost");
        int port = Integer.parseInt(preferences.get("remotePort", "8189"));
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
