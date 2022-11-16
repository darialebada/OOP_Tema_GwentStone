package gameplay;

import actions.Action;
import actions.GameActions;
import actions.StartGame;
import cards.Card;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Gameplay {
    private int turn;
    private int currentPlayer;
    private ArrayList<GameActions> gameActions;
    private Player playerOne;
    private Player playerTwo;
    private ArrayNode output;

    public Gameplay(final ArrayList<GameActions> gameActions,
                    final ArrayNode output) {
        this.gameActions = gameActions;
        this.output = output;
    }

    /**
     * parsing the data for actions
     * @param decksPlayerOne
     * @param decksPlayerTwo
     */
    public void chooseAction(final ArrayList<ArrayList<Card>> decksPlayerOne,
                             final ArrayList<ArrayList<Card>> decksPlayerTwo) {
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
            ArrayList<Action> actions = gameActions.get(i).getActions();
            for (int j = 0; j < actions.size(); j++) {
                if (actions.get(j).getCommand().equals("getPlayerDeck")) {
                    getPlayerDeck(actions.get(j).getPlayerIdx());
                } else if (actions.get(j).getCommand().equals("getPlayerHero")) {
                    getPlayerHero(actions.get(j).getPlayerIdx());
                } else if (actions.get(j).getCommand().equals("getPlayerTurn")) {
                    getPlayerTurn(currentPlayer);
                } else if (actions.get(j).getCommand().equals("endPlayerTurn")) {
                    endPlayerTurn();
                } else if (actions.get(j).getCommand().equals("placeCard")) {
                   // placeCard(actions.get(j).getHandIdx());
                }
            }

        }
    }

    /**
     *
     * @param startGame
     */
    public void startGameAction(final StartGame startGame) {
        Collections.shuffle(playerOne.getDeck(), new Random(startGame.getShuffleSeed()));
        Collections.shuffle(playerTwo.getDeck(), new Random(startGame.getShuffleSeed()));

        // add in row
        playerOne.getCardsInHand().add(playerOne.getDeck().get(0));
        playerOne.getDeck().remove(0);

        playerTwo.getCardsInHand().add(playerTwo.getDeck().get(0));
        playerTwo.getDeck().remove(0);
    }

    /**
     *
     * @param playerIdx
     */
    public void getPlayerDeck(final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            node.putPOJO("output", playerOne.getDeck());
        } else {
            node.putPOJO("output", playerTwo.getDeck());
        }
        output.add(node);
    }

    /**
     *
     * @param playerIdx
     */
    public void getPlayerHero(final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", playerIdx);
        if (playerIdx == 1) {
            node.putPOJO("output", playerOne.getHero());
        } else {
            node.putPOJO("output", playerTwo.getHero());
        }
        output.add(node);
    }

    /**
     *
     * @param playerIdx
     */
    public void getPlayerTurn(final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", playerIdx);
        output.add(node);
    }

    /**
     *
     */
    public void endPlayerTurn() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
        turn++;
    }
/*
    public void placeCard(int handIndx) {
        if (currentPlayer == 1) {
            //if (playerOne.getCardsInHand().get(handIndx)) {
                //addCardInRow()
            //}
        }
    }

    public void addCardInRow(Player player, int index) {
        if (player.getCardsInHand().get(index).getType().equals("Environment")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "placeCard");
            node.put("handIdx", index);
            node.put("error", "Cannot place environment card on table.");
            output.add(node);
            return;
        } else if (player.getHandIdx() < player.getCardsInHand().get(0).getMana()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "placeCard");
            node.put("handIdx", index);
            node.put("error", "Not enough mana to place card on table.");
            output.add(node);
            return;
        //} else if ()
        }

        //(player.getCardsInHand().get(index) instanceof TheRipper) {
        //((TheRipper) player.getCardsInHand().get(index)).getRow()

                //player.getCardsInHand().get(index).getName().equals("The Ripper") ||
               // player.getCardsInHand().get(index).getName().)
    }*/

    public ArrayList<GameActions> getGameActions() {
        return gameActions;
    }

    public void setGameActions(final ArrayList<GameActions> gameActions) {
        this.gameActions = gameActions;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(final Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(final Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(final int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(final int turn) {
        this.turn = turn;
    }
}
