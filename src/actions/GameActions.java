package actions;

import java.util.ArrayList;

public class GameActions {
    private StartGame startGame;
    private ArrayList<Action> actions;

    public StartGame getStartGame() {
        return startGame;
    }

    public void setStartGame(StartGame startGame) {
        this.startGame = startGame;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
