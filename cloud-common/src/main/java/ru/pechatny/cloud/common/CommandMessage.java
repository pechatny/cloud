package ru.pechatny.cloud.common;

import java.io.Serializable;

public class CommandMessage implements Serializable {
    private Command command;
    private String argument;

    public CommandMessage(Command command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
