package gameplay;

import actions.Action;
import actions.GameActions;
import actions.StartGame;
import cards.Card;

import cards.EnvironmentCard;
import cards.HeroCard;
import cards.MinionCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.Coordinates;

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
    private int winsPlayerOne;
    private int winsPlayerTwo;
    private int gamesNumber;


    public Gameplay(final ArrayList<GameActions> gameActions,
                    final ArrayNode output) {
        this.gameActions = gameActions;
        this.output = output;
        this.winsPlayerOne = 0;
        this.winsPlayerTwo = 0;
        this.gamesNumber = 0;
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
                } else if (actions.get(j).getCommand().equals("cardUsesAttack")) {
                    cardUsesAttack(actions.get(j).getCardAttacker().getX(), actions.get(j).getCardAttacker().getY(),
                                   actions.get(j).getCardAttacked().getX(), actions.get(j).getCardAttacked().getY());
                } else if (actions.get(j).getCommand().equals("cardUsesAbility")) {
                    cardUsesAbility(actions.get(j).getCardAttacker().getX(), actions.get(j).getCardAttacker().getY(),
                                    actions.get(j).getCardAttacked().getX(), actions.get(j).getCardAttacked().getY());
                } else if (actions.get(j).getCommand().equals("useAttackHero")) {
                    useAttackHero(actions.get(j).getCardAttacker().getX(), actions.get(j).getCardAttacker().getY());
                } else if (actions.get(j).getCommand().equals("useHeroAbility")) {
                    useHeroAbility(actions.get(j).getAffectedRow());
                } else if (actions.get(j).getCommand().equals("getTotalGamesPlayed")) {
                    getTotalGamesPlayed();
                } else if (actions.get(j).getCommand().equals("getPlayerOneWins")) {
                    getPlayerOneWins();
                } else if (actions.get(j).getCommand().equals("getPlayerTwoWins")) {
                    getPlayerTwoWins();
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
            if (player.get(0).getHero() instanceof HeroCard) {
                ((HeroCard) player.get(0).getHero()).setAbility(false);
            }
            nonAttackCards(1);
            unfrozeCards(1);
        } else {
            currentPlayer = 1;
            if (player.get(1).getHero() instanceof HeroCard) {
                ((HeroCard) player.get(1).getHero()).setAbility(false);
            }
            nonAttackCards(2);
            unfrozeCards(2);
        }
    }

    public void nonAttackCards(final int attackerIdx) {
        for (int i = 0; i < player.get(attackerIdx - 1).getCardsInRow().get(0).size(); i++) {
            if(player.get(attackerIdx - 1).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(attackerIdx - 1).getCardsInRow().get(0).get(i)).isAttack()) {
                    ((MinionCard) player.get(attackerIdx - 1).getCardsInRow().get(0).get(i)).setAttack(false);
                }
            }
        }
        for (int i = 0; i < player.get(attackerIdx - 1).getCardsInRow().get(1).size(); i++) {
            if(player.get(attackerIdx - 1).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if ( ((MinionCard) player.get(attackerIdx - 1).getCardsInRow().get(1).get(i)).isAttack()) {
                    ((MinionCard) player.get(attackerIdx - 1).getCardsInRow().get(1).get(i)).setAttack(false);
                }
            }
        }
    }

    public void unfrozeCards(int playerIdx) {
        for (int i = 0; i < player.get(playerIdx - 1).getCardsInRow().get(0).size(); i++) {
            if (player.get(playerIdx - 1).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(playerIdx - 1).getCardsInRow().get(0).get(i)).isFrozen()) {
                    ((MinionCard) player.get(playerIdx - 1).getCardsInRow().get(0).get(i)).setFrozen(false);
                }
            }
        }
        for (int i = 0; i < player.get(playerIdx - 1).getCardsInRow().get(1).size(); i++) {
            if (player.get(playerIdx - 1).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(playerIdx - 1).getCardsInRow().get(1).get(i)).isFrozen()) {
                    ((MinionCard) player.get(playerIdx - 1).getCardsInRow().get(1).get(i)).setFrozen(false);
                }
            }
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
    }

    public void useWinterfellCard(final int affectedPlayer, final int row) {
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
            node.set("output",
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

    public void cardUsesAttack(final int x_attacker, final int y_attacker,
                               final int x_attacked, final int y_attacked) {
        Coordinates coord1 = new Coordinates();
        coord1.setX(x_attacked);
        coord1.setY(y_attacked);
        Coordinates coord2 = new Coordinates();
        coord2.setX(x_attacker);
        coord2.setY(y_attacker);
        if ((currentPlayer == 2 && (x_attacked == 0 || x_attacked == 1))
            || (currentPlayer == 1 && (x_attacked == 2 || x_attacked == 3)) ) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "cardUsesAttack");
            node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
            node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
            node.put("error", "Attacked card does not belong to the enemy.");
            output.add(node);
        } else {
            int playerAttacker = currentPlayer;
            int playerAttacked = getPlayerAttackedIdx();
            int rowAttacked = getAttackedRow(x_attacked, playerAttacked);
            int rowAttacker = getAttackerRow(x_attacker);

            if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker) instanceof MinionCard) {
                if (((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).isAttack()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAttack");
                    node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                    node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                    node.put("error", "Attacker card has already attacked this turn.");
                    output.add(node);
                } else if (((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).isFrozen()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAttack");
                    node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                    node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                    node.put("error", "Attacker card is frozen.");
                    output.add(node);
                } else {
                    if (player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(y_attacked).getName().equals("Goliath")
                        || player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(y_attacked).getName().equals("Warden")) {
                        // attack
                        attackCard(playerAttacker, playerAttacked, rowAttacker, rowAttacked, y_attacker, y_attacked);
                    } else {
                        if (checkIfOtherTank(playerAttacked)) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            ObjectNode node = objectMapper.createObjectNode();
                            node.put("command", "cardUsesAttack");
                            node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                            node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                            node.put("error", "Attacked card is not of type 'Tank'.");
                            output.add(node);
                        } else {
                            //attack
                            attackCard(playerAttacker, playerAttacked, rowAttacker, rowAttacked, y_attacker, y_attacked);
                        }
                    }
                }
            }
        }
    }
    public void attackCard(final int playerAttacker, final int playerAttacked,
                           final int rowAttacker, final int rowAttacked,
                           final int y_attacker, final int y_attacked) {
        int health = player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(y_attacked).getHealth()
                - player.get(playerAttacker - 1).getCardsInRow().get(rowAttacker).get(y_attacker).getAttackDamage();
        if (health <= 0) {
            player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).remove(y_attacked);
        } else {
            player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(y_attacked).setHealth(health);
        }
        if (player.get(playerAttacker - 1).getCardsInRow().get(rowAttacker).get(y_attacker) instanceof MinionCard) {
            ((MinionCard) player.get(playerAttacker - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).setAttack(true);
        }
    }

    public boolean checkIfOtherTank(final int playerAttacked) {
        for (int i = 0; i < player.get(playerAttacked - 1).getCardsInRow().get(0).size(); i++) {
            if (player.get(playerAttacked - 1).getCardsInRow().get(0).get(i).getName().equals("Goliath")
                || (player.get(playerAttacked - 1).getCardsInRow().get(0).get(i).getName().equals("Warden"))) {
                return true;
            }
        }
        return false;
    }

    public void cardUsesAbility(final int x_attacker, final int y_attacker,
                                final int x_attacked, final int y_attacked) {
        Coordinates coord1 = new Coordinates();
        coord1.setX(x_attacked);
        coord1.setY(y_attacked);
        Coordinates coord2 = new Coordinates();
        coord2.setX(x_attacker);
        coord2.setY(y_attacker);

        // errors
        int rowAttacker = getAttackerRow(x_attacker);
        int rowAttacked;
        int playerAttacked;

        if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker) instanceof MinionCard) {
            if (((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).isFrozen()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacker card is frozen.");
                output.add(node);
                return;
            }
            if (((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).isAttack()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacker card has already attacked this turn.");
                output.add(node);
                return;
            }
        }
        if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker).getName().equals("Disciple")) {
            if ((currentPlayer == 1 && (x_attacked == 0 || x_attacked == 1))
                || (currentPlayer == 2 && (x_attacked == 2 || x_attacked == 3))) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacked card does not belong to the current player.");
                output.add(node);
            } else {
                playerAttacked = currentPlayer;
                rowAttacked = getAttackedRow(x_attacked, playerAttacked);
                healDisciple(rowAttacked, y_attacked);
                if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker) instanceof MinionCard) {
                    ((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).setAttack(true);
                }
            }
        } else {
            // MIRAJ/ RIPPER/ CURSED ONE
            playerAttacked = getPlayerAttackedIdx();
            rowAttacked = getAttackedRow(x_attacked, playerAttacked);

            if ((currentPlayer == 1 && (x_attacked == 2 || x_attacked == 3))
                    || (currentPlayer == 2 && (x_attacked == 0 || x_attacked == 1))) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacked card does not belong to the enemy.");
                output.add(node);
                return;
            } else {
                // check TANK
                if (player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(y_attacked).getName().equals("Goliath")
                    || player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(y_attacked).getName().equals("Warden")) {
                    abilityCard(playerAttacked, rowAttacker, rowAttacked, y_attacker, y_attacked);
                } else {
                    if (checkIfOtherTank(playerAttacked)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ObjectNode node = objectMapper.createObjectNode();
                        node.put("command", "cardUsesAbility");
                        node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                        node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(node);
                        return;
                    } else {
                        abilityCard(playerAttacked, rowAttacker, rowAttacked, y_attacker, y_attacked);
                    }
                }
            }
        }
    }

    public void abilityCard(final int playerAttacked, final int rowAttacker,
                            final int rowAttacked, final int y_attacker, final int y_attacked) {
            if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker).getName().equals("The Ripper")) {
                int att = player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).getAttackDamage() - 2;
                if (att < 0)
                    att = 0;
                player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).setAttackDamage(att);

            } else if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker).getName().equals("Miraj")) {
                int healthMiraj = player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker).getHealth();
                int healthEnemy = player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).getHealth();

                player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker).setHealth(healthEnemy);
                player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).setHealth(healthMiraj);

            } else if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker).getName().equals("The Cursed One")) {
                int healthEnemy = player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).getHealth();
                int attackEnemy = player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).getAttackDamage();

                player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).setHealth(attackEnemy);
                player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).setAttackDamage(healthEnemy);

                if (player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).get(y_attacked).getHealth() <= 0) {
                    player.get(playerAttacked -1).getCardsInRow().get(rowAttacked).remove(y_attacked);
                }
            }
        if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker) instanceof MinionCard) {
            ((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(y_attacker)).setAttack(true);
        }
    }

    public void healDisciple(final int rowAttacked, final int y_attacked) {
        int health = player.get(currentPlayer - 1).getCardsInRow().get(rowAttacked).get(y_attacked).getHealth() + 2;
        player.get(currentPlayer - 1).getCardsInRow().get(rowAttacked).get(y_attacked).setHealth(health);
    }

    public void useAttackHero(final int x, final int y) {
        Coordinates coord = new Coordinates();
        coord.setX(x);
        coord.setY(y);

        int row = getAttackerRow(x);
        int attackedPlayer = getPlayerAttackedIdx();

        if (player.get(currentPlayer - 1).getCardsInRow().get(row).get(y) instanceof MinionCard) {
            if (((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)).isFrozen()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "useAttackHero");
                node.set("cardAttacker", objectMapper.convertValue(coord, JsonNode.class));
                node.put("error", "Attacker card is frozen.");
                output.add(node);
                return;
            }
            if (((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)).isAttack()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "useAttackHero");
                node.set("cardAttacker", objectMapper.convertValue(coord, JsonNode.class));
                node.put("error", "Attacker card has already attacked this turn.");
                output.add(node);
                return;
            }
        }

        if (checkIfOtherTank(attackedPlayer)) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useAttackHero");
            node.set("cardAttacker", objectMapper.convertValue(coord, JsonNode.class));
            node.put("error", "Attacked card is not of type 'Tank'.");
            output.add(node);
            return;
        }

        int damage = player.get(currentPlayer - 1).getCardsInRow().get(row).get(y).getAttackDamage();
        int heroLife = player.get(attackedPlayer - 1).getHero().getHealth();

        player.get(attackedPlayer - 1).getHero().setHealth(heroLife - damage);
        if (player.get(currentPlayer - 1).getCardsInRow().get(row).get(y) instanceof MinionCard) {
            ((MinionCard) player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)).setAttack(true);
        }

        if ( player.get(attackedPlayer - 1).getHero().getHealth() <= 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            if (currentPlayer == 1) {
                node.put("gameEnded", "Player one killed the enemy hero.");
                winsPlayerOne++;
            } else {
                node.put("gameEnded", "Player two killed the enemy hero.");
                winsPlayerTwo++;
            }
            gamesNumber++;
            output.add(node);
        }
    }

    public void useHeroAbility(final int affectedRow) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        int row;

        if (player.get(currentPlayer - 1).getMana() < player.get(currentPlayer - 1).getHero().getMana()) {
            node.put("command", "useHeroAbility");
            node.put("affectedRow", affectedRow);
            node.put("error", "Not enough mana to use hero's ability.");
            output.add(node);
            return;
        }
        if (player.get(currentPlayer - 1).getHero() instanceof HeroCard) {
            if (((HeroCard) player.get(currentPlayer - 1).getHero()).isAbility()) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Hero has already attacked this turn.");
                output.add(node);
                return;
            }
        }
        if (player.get(currentPlayer - 1).getHero().getName().equals("Lord Royce")
            || player.get(currentPlayer - 1).getHero().getName().equals("Empress Thorina")) {
            if ((currentPlayer == 1 && (affectedRow == 2 || affectedRow == 3))
                || (currentPlayer == 2 && (affectedRow == 0 || affectedRow == 1))) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Selected row does not belong to the enemy.");
                output.add(node);
                return;
            } else {
                int attackedPlayer = getPlayerAttackedIdx();
                row = getAttackedRow(affectedRow, attackedPlayer);
                useHeroAbilityOnEnemy(row, attackedPlayer);
            }
        } else  if (player.get(currentPlayer - 1).getHero().getName().equals("General Kocioraw")
                || player.get(currentPlayer - 1).getHero().getName().equals("King Mudface")) {
            if ((currentPlayer == 1 && (affectedRow == 0 || affectedRow == 1))
                    || (currentPlayer == 2 && (affectedRow == 2 || affectedRow == 3))) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Selected row does not belong to the current player.");
                output.add(node);
                return;
            } else {
                row = getAttackedRow(affectedRow, currentPlayer);
                useHeroAbilityOnSelf(row);
            }
        }
        int newMana = player.get(currentPlayer - 1).getMana() - player.get(currentPlayer - 1).getHero().getMana();
        player.get(currentPlayer - 1).setMana(newMana);
    }

    public void useHeroAbilityOnEnemy(final int row, final int attackedPlayer) {
        if (player.get(currentPlayer - 1).getHero().getName().equals("Empress Thorina")) {
            int index = 0;
            int maxHealth = 0;
            for (int i = 0; i < player.get(attackedPlayer - 1).getCardsInRow().get(row).size(); i++) {
                if (player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).getHealth() > maxHealth) {
                    maxHealth = player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).getHealth();
                    index = i;
                }
            }
            player.get(attackedPlayer - 1).getCardsInRow().get(row).remove(index);
        } else {
            // Lord Royce
            int index = 0;
            int maxDamage = -1;
            for (int i = 0; i < player.get(attackedPlayer - 1).getCardsInRow().get(row).size(); i++) {
                if (player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).getAttackDamage() > maxDamage) {
                    maxDamage = player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).getAttackDamage();
                    index = i;
                }
            }
            if (player.get(attackedPlayer - 1).getCardsInRow().get(row).get(index) instanceof MinionCard) {
                ((MinionCard) player.get(attackedPlayer - 1).getCardsInRow().get(row).get(index)).setFrozen(true);
            }
        }
        if (player.get(currentPlayer - 1).getHero() instanceof HeroCard) {
            ((HeroCard) player.get(currentPlayer - 1).getHero()).setAbility(true);
        }
    }

    public void useHeroAbilityOnSelf(final int row) {
        int value = 0;
        if (player.get(currentPlayer - 1).getHero().getName().equals("King Mudface")) {
            for (int i = 0; i < player.get(currentPlayer - 1).getCardsInRow().get(row).size(); i++) {
                value = player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).getHealth() + 1;
                player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).setHealth(value);
            }
        } else {
            // General Kocioraw
            for (int i = 0; i < player.get(currentPlayer - 1).getCardsInRow().get(row).size(); i++) {
                value = player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).getAttackDamage() + 1;
                player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).setAttackDamage(value);
            }
        }
        if (player.get(currentPlayer - 1).getHero() instanceof HeroCard) {
            ((HeroCard) player.get(currentPlayer - 1).getHero()).setAbility(true);
        }
    }

    public void getTotalGamesPlayed() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getTotalGamesPlayed");
        node.put("output", gamesNumber);
        output.add(node);
    }

    public void getPlayerOneWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerOneWins");
        node.put("output", winsPlayerOne);
        output.add(node);
    }

    public void getPlayerTwoWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerTwoWins");
        node.put("output", winsPlayerTwo);
        output.add(node);
    }

    public int getPlayerAttackedIdx() {
        if (currentPlayer == 1)
            return 2;
        return 1;
    }

    public int getAttackerRow(final int x) {
        if (currentPlayer == 2) {
            if (x == 0)
                return 1;
            return 0;
        } else {
            if (x == 2)
                return 0;
            return 1;
        }
    }

    public int getAttackedRow(final int x, final int playerIdx) {
        if (playerIdx == 1) {
            if (x == 2)
                return 0;
            return 1;
        } else {
            if (x == 0)
                return 1;
            return 0;
        }
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

    public int getWinsPlayerOne() {
        return winsPlayerOne;
    }

    public void setWinsPlayerOne(final int winsPlayerOne) {
        this.winsPlayerOne = winsPlayerOne;
    }

    public int getWinsPlayerTwo() {
        return winsPlayerTwo;
    }

    public void setWinsPlayerTwo(final int winsPlayerTwo) {
        this.winsPlayerTwo = winsPlayerTwo;
    }

    public int getGamesNumber() {
        return gamesNumber;
    }

    public void setGamesNumber(final int gamesNumber) {
        this.gamesNumber = gamesNumber;
    }
}
