package gameplay;

import actions.Action;
import actions.GameActions;
import actions.PrepareGame;
import actions.StartGame;
import cards.Card;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class Gameplay {
    static int currentPlayer;
    private ArrayList<GameActions> gameActions;
    private Player playerOne;
    private Player playerTwo;
    private ArrayNode output;

    public Gameplay(ArrayList<GameActions> gameActions, ArrayNode output) {
        this.gameActions = gameActions;
        this.output = output;
    }
    public void chooseAction(ArrayList<ArrayList<Card>> decksPlayerOne, ArrayList<ArrayList<Card>> decksPlayerTwo) {
        // number of games
        for (int i = 0; i < gameActions.size(); i++) {
            // number of actions
            StartGame startGame = gameActions.get(i).getStartGame();

            //get each player
            playerOne = new Player(decksPlayerOne.get(startGame.getPlayerOneDeckIdx()),
                                          startGame.getPlayerOneHero());
            playerTwo = new Player(decksPlayerTwo.get(startGame.getPlayerTwoDeckIdx()),
                                          startGame.getPlayerTwoHero());

            startGameAction(startGame);
            currentPlayer = startGame.getStartingPlayer();
            playerOne.setHandIdx(1);
            playerTwo.setHandIdx(1);

            ArrayList<Action> actions = gameActions.get(i).getActions();
            for (int j = 0; j < actions.size(); j++) {
                if (actions.get(j).getCommand().equals("getPlayerDeck")) {
                    getPlayerDeck(actions.get(j).getPlayerIdx());
                } else if (actions.get(j).getCommand().equals("getPlayerHero")) {
                    getPlayerHero(actions.get(j).getPlayerIdx());
                    //getPlayerHero(actions.get(j).getPlayerIdx(), startGame);
                } else if (actions.get(j).getCommand().equals("getPlayerTurn")) {
                    getPlayerTurn(currentPlayer);
                } else if (actions.get(j).getCommand().equals("endPlayerTurn")) {
                    endPlayerTurn();
                } else if (actions.get(j).getCommand().equals("placeCard")) {
                    //placeCard(actions.get(j).getHandIdx());
                }
            }

        }
    }

    public void startGameAction(StartGame startGame) {
        Collections.shuffle(playerOne.getDeck(), new Random(startGame.getShuffleSeed()));
        Collections.shuffle(playerTwo.getDeck(), new Random(startGame.getShuffleSeed()));

        // add in row
        playerOne.getCardsInHand().add(playerOne.getDeck().get(0));
        playerOne.getDeck().remove(0);

        playerTwo.getCardsInHand().add(playerTwo.getDeck().get(0));
        playerTwo.getDeck().remove(0);
    }

    public void getPlayerDeck(int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);
        if(playerIdx == 1) {
            node.putPOJO("output", playerOne.getDeck());
        } else {
            node.putPOJO("output", playerTwo.getDeck());
        }
        output.add(node);
    }

    //public void getPlayerHero(int playerIdx, StartGame startGame) {
    public void getPlayerHero(int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", playerIdx);
        if(playerIdx == 1) {
            node.putPOJO("output", playerOne.getHero());
        } else {
            node.putPOJO("output", playerTwo.getHero());
        }
        output.add(node);
    }

    public void getPlayerTurn(int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", playerIdx);
        output.add(node);
    }

    public void endPlayerTurn() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    public ArrayList<GameActions> getGameActions() {
        return gameActions;
    }

    public void setGameActions(ArrayList<GameActions> gameActions) {
        this.gameActions = gameActions;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }
}
