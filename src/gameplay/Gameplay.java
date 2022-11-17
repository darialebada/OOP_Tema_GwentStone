package gameplay;

import actions.Action;
import actions.GameActions;
import actions.StartGame;
import cards.Card;

import cards.MinionCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Gameplay {
    private int endTurn;
    private int turn;
    private int currentPlayer;
    private ArrayList<GameActions> gameActions;
    private ArrayList<Player> player;
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
            player = new ArrayList<Player>(2);

            player.add(new Player(decksPlayerOne.get(startGame.getPlayerOneDeckIdx()),
                       startGame.getPlayerOneHero()));

            player.add(new Player(decksPlayerTwo.get(startGame.getPlayerTwoDeckIdx()),
                       startGame.getPlayerTwoHero()));

            startGameAction(startGame);
            endTurn = 0;
            turn = 1;

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
                    endPlayerTurn(currentPlayer);
                } else if (actions.get(j).getCommand().equals("placeCard")) {
                    placeCard(actions.get(j).getHandIdx());
                } else if (actions.get(j).getCommand().equals("getCardsInHand")) {
                    getCardsInHand(actions.get(j).getPlayerIdx());
                } else if (actions.get(j).getCommand().equals("getPlayerMana")) {
                     getPlayerMana(actions.get(j).getPlayerIdx());
                } else if (actions.get(j).getCommand().equals("getCardsOnTable"))
                    getCardOnTable();
            }
        }
    }

    /**
     *
     * @param startGame
     */
    public void startGameAction(final StartGame startGame) {
        Collections.shuffle(player.get(0).getDeck(), new Random(startGame.getShuffleSeed()));
        Collections.shuffle(player.get(1).getDeck(), new Random(startGame.getShuffleSeed()));

        // add in row
        player.get(0).getCardsInHand().add(player.get(0).getDeck().get(0));
        player.get(0).getDeck().remove(0);


        player.get(1).getCardsInHand().add(player.get(1).getDeck().get(0));
        player.get(1).getDeck().remove(0);
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
        node.set("output", objectMapper.convertValue(player.get(playerIdx-1).getDeck(), JsonNode.class));
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
        node.set("output", objectMapper.convertValue(player.get(playerIdx-1).getHero(), JsonNode.class));
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
    public void endPlayerTurn(int index) {
        endTurn++;
        if (endTurn % 2 == 1) {
            if (turn < 10) {
                turn++;
            }
            for (int k = 0; k <= 1; k++) {
                if (player.get(k).getDeck().size() > 0) {
                    player.get(k).getCardsInHand().add(player.get(k).getDeck().get(0));
                    player.get(k).getDeck().remove(0);
                }

            }
        }
        if (currentPlayer == 1) {
            currentPlayer = 2;
                player.get(0).setMana(player.get(0).getMana() + turn);
        } else {
            currentPlayer = 1;
                player.get(1).setMana(player.get(1).getMana() + turn);
        }
    }

    public void placeCard(int index) {
        if (player.get(currentPlayer - 1).getCardsInHand().get(index).getType().equals("Environment")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "placeCard");
            node.put("handIdx", index);
            node.put("error", "Cannot place environment card on table.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getMana() < player.get(currentPlayer - 1).getCardsInHand().get(index).getMana()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "placeCard");
            node.put("handIdx", index);
            node.put("error", "Not enough mana to place card on table.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getCardsInHand().get(index) instanceof MinionCard) {
            int rowToAdd = ((MinionCard)player.get(currentPlayer - 1).getCardsInHand().get(index)).getRow() - 1;
            if (player.get(currentPlayer - 1).getCardsInRow().get(rowToAdd).size() >= 5) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "placeCard");
                node.put("handIdx", index);
                node.put("error", "Cannot place card on table since row is full.");
                output.add(node);
            } else {
                player.get(currentPlayer - 1).getCardsInRow().get(rowToAdd).add(player.get(currentPlayer - 1).getCardsInHand().get(index));
                player.get(currentPlayer - 1).setMana(player.get(currentPlayer - 1).getMana() -
                player.get(currentPlayer - 1).getCardsInHand().get(index).getMana());
                player.get(currentPlayer - 1).getCardsInHand().remove(index);
            }
        }
    }

    public void getPlayerMana(int index) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", index);
        node.put("output", player.get(index - 1).getMana());
        output.add(node);
    }

    public void getCardsInHand(int index) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getCardsInHand");
        node.put("playerIdx", index);
        node.set("output", objectMapper.convertValue(player.get(index-1).getCardsInHand(), JsonNode.class));
        output.add(node);
    }

    public void getCardOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getCardsOnTable");
        ArrayList<ArrayList<Card>> table = new ArrayList<>(4);
        table.add(player.get(1).getCardsInRow().get(1));
        table.add(player.get(1).getCardsInRow().get(0));
        table.add(player.get(0).getCardsInRow().get(0));
        table.add(player.get(0).getCardsInRow().get(1));

        node.set("output", objectMapper.convertValue(table, JsonNode.class));
        output.add(node);
    }

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

    public ArrayList<Player> getPlayer() {
        return player;
    }

    public void setPlayer(final ArrayList<Player> player) {
        this.player = player;
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

    public int isEndTurn() {
        return endTurn;
    }

    public void setEndTurn(final int endTurn) {
        this.endTurn = endTurn;
    }
}
