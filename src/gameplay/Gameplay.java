package gameplay;

import actions.Action;
import actions.GameActions;
import actions.StartGame;
import cards.Card;

import cards.EnvironmentCard;
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
    private int frozenPlayer;
    private int frozenRow;

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
                } else if (actions.get(j).getCommand().equals("getCardsOnTable")) {
                    getCardOnTable();
                } else if (actions.get(j).getCommand().equals("getEnvironmentCardsInHand")) {
                    getEnvironmentCardsInHand(actions.get(j).getPlayerIdx());
                } else if (actions.get(j).getCommand().equals("useEnvironmentCard")) {
                    useEnvironmentCard(actions.get(j).getHandIdx(), actions.get(j).getAffectedRow());
                } else if (actions.get(j).getCommand().equals("getCardAtPosition")) {
                    getCardAtPosition(actions.get(j).getX(), actions.get(j).getY());
                } else if (actions.get(j).getCommand().equals("getFrozenCardsOnTable")) {
                    getFrozenCardsOnTable();
                }
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
        if (endTurn % 2 == 0) {
            if (turn < 10) {
                turn++;
            }
            if (frozenPlayer != 0) {
                unfrozeCards();
            }
            for (int k = 0; k <= 1; k++) {
                if (player.get(k).getDeck().size() > 0) {
                    player.get(k).getCardsInHand().add(player.get(k).getDeck().get(0));
                    player.get(k).getDeck().remove(0);
                }
            }
            player.get(0).setMana(player.get(0).getMana() + turn);
            player.get(1).setMana(player.get(1).getMana() + turn);
        }
        if (currentPlayer == 1) {
            currentPlayer = 2;
            //player.get(0).setMana(player.get(0).getMana() + turn);
        } else {
            currentPlayer = 1;
            //player.get(1).setMana(player.get(1).getMana() + turn);
        }
    }

    public void unfrozeCards() {
        for (int i = 0; i < player.get(frozenPlayer - 1).getCardsInRow().get(frozenRow).size(); i++) {
            int health = player.get(frozenPlayer - 1).getCardsInRow().get(frozenRow).get(i).getHealth() - 1;
            player.get(frozenPlayer - 1).getCardsInRow().get(frozenRow).get(i).setHealth(health);
        }
        frozenPlayer = 0;
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

    public void getEnvironmentCardsInHand(int playerIdx) {
        ArrayList<EnvironmentCard> environment = new ArrayList<>();
        for (int i = 0; i < player.get(playerIdx - 1).getCardsInHand().size(); i++) {
            //if (player.get(playerIdx - 1).getCardsInHand().get(i).getType().equals("Environment")) {
            if (player.get(playerIdx - 1).getCardsInHand().get(i) instanceof EnvironmentCard) {
                environment.add(new EnvironmentCard((EnvironmentCard)(player.get(playerIdx-1).getCardsInHand().get(i))));
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getEnvironmentCardsInHand");
        node.put("playerIdx", playerIdx);
        node.set("output", objectMapper.convertValue(environment, JsonNode.class));
        output.add(node);
    }

    public void useEnvironmentCard(final int handIndx, final int affectedRow) {
        if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx) instanceof MinionCard) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useEnvironmentCard");
            node.put("handIdx", handIndx);
            node.put("affectedRow", affectedRow);
            node.put("error", "Chosen card is not of type environment.");
            output.add(node);
        } else if(player.get(currentPlayer - 1).getMana() < player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useEnvironmentCard");
            node.put("handIdx", handIndx);
            node.put("affectedRow", affectedRow);
            node.put("error", "Not enough mana to use environment card.");
            output.add(node);
        } else if ((currentPlayer == 2 && (affectedRow == 0 || affectedRow == 1))
                    || (currentPlayer == 1 && (affectedRow == 2 || affectedRow == 3)) ) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useEnvironmentCard");
            node.put("handIdx", handIndx);
            node.put("affectedRow", affectedRow);
            node.put("error", "Chosen row does not belong to the enemy.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx) instanceof EnvironmentCard) {
            int affectedPlayer = 0;
            int row = 0;
            if (currentPlayer == 1) {
                affectedPlayer = 2;
                if (affectedRow == 0) {
                    row = 1;
                } else if (affectedRow == 1) {
                    row = 0;
                }
            } else {
                affectedPlayer = 1;
                if (affectedRow == 2) {
                    row = 0;
                } else if (affectedRow == 3) {
                    row = 1;
                }
            }
            int manaCardUsed = 0;
            if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getName().equals("Heart Hound")) {
                int cardToStealIdx;
                if (player.get(currentPlayer - 1).getCardsInRow().get(row).size() == 5) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "useEnvironmentCard");
                    node.put("handIdx", handIndx);
                    node.put("affectedRow", affectedRow);
                    node.put("error", "Cannot steal enemy card since the player's row is full.");
                    output.add(node);
                } else {
                    cardToStealIdx = useHeartHoundCard(handIndx, affectedRow, 2, row);
                    player.get(currentPlayer - 1).getCardsInRow().get(row).add(new
                    Card(player.get(affectedPlayer - 1).getCardsInRow().get(row).get(cardToStealIdx)));

                    manaCardUsed = player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana();

                    player.get(affectedPlayer - 1).getCardsInRow().get(row).remove(cardToStealIdx);
                    player.get(currentPlayer - 1).getCardsInHand().remove(handIndx);
                }
            } else if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getName().equals("Firestorm")) {
                useFirestormCard(affectedPlayer, row);

                manaCardUsed = player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana();

                player.get(currentPlayer - 1).getCardsInHand().remove(handIndx);
            } else if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getName().equals("Winterfell")) {
                useWinterfellCard(affectedPlayer, row);

                manaCardUsed = player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana();

                player.get(currentPlayer - 1).getCardsInHand().remove(handIndx);
            }
            player.get(currentPlayer - 1).setMana(player.get(currentPlayer - 1).getMana() - manaCardUsed);
        }
    }

    public int useHeartHoundCard(final int handIndx, final int affectedRow, final int attackedPlayerIdx, final int row) {
        int maxHealth = -1;
        int index = 0;
        for (int i = 0; i < player.get(attackedPlayerIdx - 1).getCardsInRow().get(row).size(); i++) {
            if (player.get(attackedPlayerIdx - 1).getCardsInRow().get(row).get(i).getHealth() > maxHealth) {
                maxHealth = player.get(attackedPlayerIdx - 1).getCardsInRow().get(row).get(i).getHealth();
                index = i;
            }
        }
        return index;
    }

    public void useFirestormCard(final int affectedPlayer, final int row) {
        for (int i = 0; i < player.get(affectedPlayer - 1).getCardsInRow().get(row).size(); i++) {

            int health = player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i).getHealth() - 1;
            player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i).setHealth(health);

            if ( player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i).getHealth() <= 0) {

                player.get(affectedPlayer - 1).getCardsInRow().get(row).remove(i);
                i--;
            }
        }
        //frozenPlayer = affectedPlayer;
        //frozenRow = row;
    }

    public void useWinterfellCard(final int affectedPlayer, final int row) {
        frozenPlayer = affectedPlayer;
        for (int i = 0; i < player.get(affectedPlayer - 1).getCardsInRow().get(row).size(); i++) {
            if (player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i) instanceof MinionCard) {
                ((MinionCard) player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i)).setFrozen(true);
            }
        }
    }

    public void getCardAtPosition(int x, int y) {
        // x = rand
        // y = pozitie pe rand
        int row = 0;
        int playerIdx = 0;
        if (x == 0) {
            playerIdx = 2;
            row = 1;
        } else if (x == 1) {
            playerIdx = 2;
            row = 0;
        } else if (x == 2) {
            playerIdx = 1;
            row = 0;
        } else if (x == 3) {
            playerIdx = 1;
            row = 1;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getCardAtPosition");
        node.put("x", x);
        node.put("y", y);
        if (player.get(playerIdx - 1).getCardsInRow().get(row).size() <= y) {
            node.put("output", "No card available at that position.");
        } else {
            node.put("output",
            objectMapper.convertValue(player.get(playerIdx - 1).getCardsInRow().get(row).get(y), JsonNode.class));
        }
        output.add(node);
    }

    public void getFrozenCardsOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getFrozenCardsOnTable");
        ArrayList<Card> frozenTable = new ArrayList<>(4);

        for (int i = 0; i < player.get(1).getCardsInRow().get(1).size(); i++) {
            if (player.get(1).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(1).getCardsInRow().get(1).get(i)).isFrozen()) {
                    frozenTable.add(player.get(1).getCardsInRow().get(1).get(i));
                }
            }
        }

        for (int i = 0; i < player.get(1).getCardsInRow().get(0).size(); i++) {
            ArrayList<Card> frozenCards = new ArrayList<>();
            if (player.get(1).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(1).getCardsInRow().get(0).get(i)).isFrozen()) {
                    frozenTable.add(player.get(1).getCardsInRow().get(0).get(i));
                }
            }
        }

        for (int i = 0; i < player.get(0).getCardsInRow().get(0).size(); i++) {
            ArrayList<Card> frozenCards = new ArrayList<>();
            if (player.get(0).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(0).getCardsInRow().get(0).get(i)).isFrozen()) {
                    frozenTable.add(player.get(0).getCardsInRow().get(0).get(i));
                }
            }
        }

        for (int i = 0; i < player.get(0).getCardsInRow().get(1).size(); i++) {
            ArrayList<Card> frozenCards = new ArrayList<>();
            if (player.get(0).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(0).getCardsInRow().get(1).get(i)).isFrozen()) {
                    frozenTable.add(player.get(0).getCardsInRow().get(1).get(i));
                }
            }
        }

        node.set("output", objectMapper.convertValue(frozenTable, JsonNode.class));
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

    public int getEndTurn() {
        return endTurn;
    }

    public void setEndTurn(final int endTurn) {
        this.endTurn = endTurn;
    }

    public int getFrozenPlayer() {
        return frozenPlayer;
    }

    public void setFrozenPlayer(final int frozenPlayer) {
        this.frozenPlayer = frozenPlayer;
    }

    public int getFrozenRow() {
        return frozenRow;
    }

    public void setFrozenRow(int frozenRow) {
        this.frozenRow = frozenRow;
    }
}
