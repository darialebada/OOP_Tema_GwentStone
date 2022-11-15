package gameplay;

import cards.Card;

import java.util.ArrayList;

public class Command {
    private String command;
    private int playerIdx;

    public Command (String command, int playerIdx) {
        this.command = command;
        this.playerIdx = playerIdx;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(int playerIdx) {
        this.playerIdx = playerIdx;
    }
}
