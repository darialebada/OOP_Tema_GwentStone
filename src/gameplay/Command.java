package gameplay;

public class Command {
    private String command;
    private int playerIdx;

    public Command(final String command, final int playerIdx) {
        this.command = command;
        this.playerIdx = playerIdx;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(final int playerIdx) {
        this.playerIdx = playerIdx;
    }
}
