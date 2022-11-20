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
import helpers.MagicNumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public final class Gameplay {
    private int endTurn;
    private int turn;
    private int currentPlayer;
    private final ArrayList<GameActions> gameActions;
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
     * @param decksPlayerOne player one's decks
     * @param decksPlayerTwo player two's decks
     */
    public void chooseAction(final ArrayList<ArrayList<Card>> decksPlayerOne,
                             final ArrayList<ArrayList<Card>> decksPlayerTwo) {
        /* For each played game */
        for (GameActions gameAction : gameActions) {
            /* number of actions */
            StartGame startGame = gameAction.getStartGame();

            /* get each player */
            player = new ArrayList<>(2);

            player.add(new Player(decksPlayerOne.get(startGame.getPlayerOneDeckIdx()),
                    startGame.getPlayerOneHero()));

            player.add(new Player(decksPlayerTwo.get(startGame.getPlayerTwoDeckIdx()),
                    startGame.getPlayerTwoHero()));

            /* helpers for ending players' turns */
            endTurn = 0;
            turn = 1;

            /* start game */
            startGameAction(startGame);
            currentPlayer = startGame.getStartingPlayer();

            /* game commands */
            ArrayList<Action> actions = gameAction.getActions();
            for (Action action : actions) {
                switch (action.getCommand()) {
                    case "getPlayerDeck" -> getPlayerDeck(action.getPlayerIdx());
                    case "getPlayerHero" -> getPlayerHero(action.getPlayerIdx());
                    case "getPlayerTurn" -> getPlayerTurn();
                    case "endPlayerTurn" -> endPlayerTurn();
                    case "placeCard" -> placeCard(action.getHandIdx());
                    case "getCardsInHand" -> getCardsInHand(action.getPlayerIdx());
                    case "getPlayerMana" -> getPlayerMana(action.getPlayerIdx());
                    case "getCardsOnTable" -> getCardOnTable();
                    case "getEnvironmentCardsInHand" ->
                            getEnvironmentCardsInHand(action.getPlayerIdx());
                    case "useEnvironmentCard" ->
                            useEnvironmentCard(action.getHandIdx(), action.getAffectedRow());
                    case "getCardAtPosition" -> getCardAtPosition(action.getX(), action.getY());
                    case "getFrozenCardsOnTable" -> getFrozenCardsOnTable();
                    case "cardUsesAttack" ->
                            cardUsesAttack(action.getCardAttacker().getX(),
                                           action.getCardAttacker().getY(),
                                           action.getCardAttacked().getX(),
                                           action.getCardAttacked().getY());
                    case "cardUsesAbility" ->
                            cardUsesAbility(action.getCardAttacker().getX(),
                                            action.getCardAttacker().getY(),
                                            action.getCardAttacked().getX(),
                                            action.getCardAttacked().getY());
                    case "useAttackHero" ->
                            useAttackHero(action.getCardAttacker().getX(),
                                          action.getCardAttacker().getY());
                    case "useHeroAbility" -> useHeroAbility(action.getAffectedRow());
                    case "getTotalGamesPlayed" -> getTotalGamesPlayed();
                    case "getPlayerOneWins" -> getPlayerOneWins();
                    case "getPlayerTwoWins" -> getPlayerTwoWins();
                    default -> System.out.println("No valid command");
                }
            }
        }
    }

    /**
     * actions performed at the beginning of each game
     */
    public void startGameAction(final StartGame startGame) {
        /* shuffle each player's deck */
        Collections.shuffle(player.get(0).getDeck(), new Random(startGame.getShuffleSeed()));
        Collections.shuffle(player.get(1).getDeck(), new Random(startGame.getShuffleSeed()));

        /* add one card in each player's hand */
        player.get(0).getCardsInHand().add(player.get(0).getDeck().get(0));
        player.get(0).getDeck().remove(0);

        player.get(1).getCardsInHand().add(player.get(1).getDeck().get(0));
        player.get(1).getDeck().remove(0);
    }

    /**
     * Print a player's deck
     */
    public void getPlayerDeck(final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);
        node.set("output",
                 objectMapper.convertValue(player.get(playerIdx - 1).getDeck(), JsonNode.class));
        output.add(node);
    }

    /**
     * Print a player's hero
     */
    public void getPlayerHero(final int playerIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerHero");
        node.put("playerIdx", playerIdx);
        node.set("output",
                 objectMapper.convertValue(player.get(playerIdx - 1).getHero(), JsonNode.class));
        output.add(node);
    }

    /**
     * Print whose turn it is
     */
    public void getPlayerTurn() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerTurn");
        node.put("output", currentPlayer);
        output.add(node);
    }

    /**
     * Ending a player's turn
     */
    public void endPlayerTurn() {
        endTurn++;
        /* new turn */
        if (endTurn % 2 == 0) {
            if (turn < MagicNumber.TEN) {
                turn++;
            }
            /* take first card from deck in hand */
            for (int k = 0; k <= 1; k++) {
                if (player.get(k).getDeck().size() > 0) {
                    player.get(k).getCardsInHand().add(player.get(k).getDeck().get(0));
                    player.get(k).getDeck().remove(0);
                }
            }
            /* increment each player's mana */
            player.get(0).setMana(player.get(0).getMana() + turn);
            player.get(1).setMana(player.get(1).getMana() + turn);
        }
        /* change current player and unfroze cards */
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

    /**
     * at the end of each player's turn, we set each attack field as false
     * so the card will be able to attack in the next turn
     */
    public void nonAttackCards(final int attackerIdx) {
        /* front row */
        for (int i = 0; i < player.get(attackerIdx - 1).getCardsInRow().get(0).size(); i++) {
            if (player.get(attackerIdx - 1).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard)
                        player.get(attackerIdx - 1).getCardsInRow().get(0).get(i)).isAttack()) {
                    ((MinionCard)
                    player.get(attackerIdx - 1).getCardsInRow().get(0).get(i)).setAttack(false);
                }
            }
        }
        /* back row */
        for (int i = 0; i < player.get(attackerIdx - 1).getCardsInRow().get(1).size(); i++) {
            if (player.get(attackerIdx - 1).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard)
                        player.get(attackerIdx - 1).getCardsInRow().get(1).get(i)).isAttack()) {
                    ((MinionCard)
                    player.get(attackerIdx - 1).getCardsInRow().get(1).get(i)).setAttack(false);
                }
            }
        }
    }

    /**
     * check as unfrozen all player's cards at the end of his turn
     */
    public void unfrozeCards(final int playerIdx) {
        /* front row */
        for (int i = 0; i < player.get(playerIdx - 1).getCardsInRow().get(0).size(); i++) {
            if (player.get(playerIdx - 1).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard)
                        player.get(playerIdx - 1).getCardsInRow().get(0).get(i)).isFrozen()) {
                    ((MinionCard)
                    player.get(playerIdx - 1).getCardsInRow().get(0).get(i)).setFrozen(false);
                }
            }
        }
        /* back row */
        for (int i = 0; i < player.get(playerIdx - 1).getCardsInRow().get(1).size(); i++) {
            if (player.get(playerIdx - 1).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard)
                        player.get(playerIdx - 1).getCardsInRow().get(1).get(i)).isFrozen()) {
                    ((MinionCard)
                    player.get(playerIdx - 1).getCardsInRow().get(1).get(i)).setFrozen(false);
                }
            }
        }
    }

    /**
     * current player places card on table
     */
    public void placeCard(final int index) {
        /* errors */
        if (Objects.equals(player.get(currentPlayer - 1).getCardsInHand().get(index).getType(),
            "Environment")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "placeCard");
            node.put("handIdx", index);
            node.put("error", "Cannot place environment card on table.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getMana()
                < player.get(currentPlayer - 1).getCardsInHand().get(index).getMana()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "placeCard");
            node.put("handIdx", index);
            node.put("error", "Not enough mana to place card on table.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getCardsInHand().get(index)
                instanceof MinionCard) {
            int rowToAdd
            = ((MinionCard) player.get(currentPlayer - 1).getCardsInHand().get(index)).getRow() - 1;
            if (player.get(currentPlayer - 1).getCardsInRow().get(rowToAdd).size()
                    >= MagicNumber.FIVE) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "placeCard");
                node.put("handIdx", index);
                node.put("error", "Cannot place card on table since row is full.");
                output.add(node);
            } else {
                /* valid card can be placed on table */
                player.get(currentPlayer - 1).getCardsInRow().
                  get(rowToAdd).add(player.get(currentPlayer - 1).getCardsInHand().get(index));
                /* decrement mana */
                player.get(currentPlayer - 1).setMana(player.get(currentPlayer - 1).getMana()
                        - player.get(currentPlayer - 1).getCardsInHand().get(index).getMana());
                /* remove card from hand */
                player.get(currentPlayer - 1).getCardsInHand().remove(index);
            }
        }
    }

    /**
     * return a player's current mana
     */
    public void getPlayerMana(final int index) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerMana");
        node.put("playerIdx", index);
        node.put("output", player.get(index - 1).getMana());
        output.add(node);
    }

    /**
    * print the cards from a player's hand
     */
    public void getCardsInHand(final int index) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getCardsInHand");
        node.put("playerIdx", index);
        node.set("output",
                 objectMapper.convertValue(player.get(index - 1).getCardsInHand(), JsonNode.class));
        output.add(node);
    }

    /**
     * print the cards existing on the table
     */
    public void getCardOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getCardsOnTable");
        ArrayList<ArrayList<Card>> table = new ArrayList<>(MagicNumber.FOUR);
        table.add(player.get(1).getCardsInRow().get(1));
        table.add(player.get(1).getCardsInRow().get(0));
        table.add(player.get(0).getCardsInRow().get(0));
        table.add(player.get(0).getCardsInRow().get(1));
        node.set("output", objectMapper.convertValue(table, JsonNode.class));
        output.add(node);
    }

    /**
     * print all environmental cards from a player's hand
     */
    public void getEnvironmentCardsInHand(final int playerIdx) {
        ArrayList<EnvironmentCard> environment = new ArrayList<>();
        for (int i = 0; i < player.get(playerIdx - 1).getCardsInHand().size(); i++) {
            if (player.get(playerIdx - 1).getCardsInHand().get(i) instanceof EnvironmentCard) {
                environment.add(new EnvironmentCard(
                        (EnvironmentCard) (player.get(playerIdx - 1).getCardsInHand().get(i))));
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getEnvironmentCardsInHand");
        node.put("playerIdx", playerIdx);
        node.set("output", objectMapper.convertValue(environment, JsonNode.class));
        output.add(node);
    }

    /**
     * use the ability of an environment card
     */
    public void useEnvironmentCard(final int handIndx, final int affectedRow) {
        /* errors */
        if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx) instanceof MinionCard) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useEnvironmentCard");
            node.put("handIdx", handIndx);
            node.put("affectedRow", affectedRow);
            node.put("error", "Chosen card is not of type environment.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getMana()
                < player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useEnvironmentCard");
            node.put("handIdx", handIndx);
            node.put("affectedRow", affectedRow);
            node.put("error", "Not enough mana to use environment card.");
            output.add(node);
        } else if ((currentPlayer == 2 && (affectedRow == 0 || affectedRow == 1))
                || (currentPlayer == 1
                && (affectedRow == 2 || affectedRow == MagicNumber.THREE))) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "useEnvironmentCard");
            node.put("handIdx", handIndx);
            node.put("affectedRow", affectedRow);
            node.put("error", "Chosen row does not belong to the enemy.");
            output.add(node);
        } else if (player.get(currentPlayer - 1).getCardsInHand().get(handIndx)
                instanceof EnvironmentCard) {
            /* valid card can be used */
            int affectedPlayer = getPlayerAttackedIdx();
            int row = getAttackedRow(affectedRow, affectedPlayer);
            int manaCardUsed = 0;

            switch (player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getName()) {
                case "Heart Hound" -> {
                    int cardToSteal;
                    if (player.get(currentPlayer - 1).getCardsInRow().get(row).size()
                        == MagicNumber.FIVE) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ObjectNode node = objectMapper.createObjectNode();
                        node.put("command", "useEnvironmentCard");
                        node.put("handIdx", handIndx);
                        node.put("affectedRow", affectedRow);
                        node.put("error",
                                 "Cannot steal enemy card since the player's row is full.");
                        output.add(node);
                    } else {
                        cardToSteal = useHeartHoundCard(2, row);
                        player.get(currentPlayer - 1).getCardsInRow().get(row).add(new Card(
                        player.get(affectedPlayer - 1).getCardsInRow().get(row).get(cardToSteal)));

                        manaCardUsed
                           = player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana();

                        player.get(affectedPlayer - 1).getCardsInRow().get(row).
                            remove(cardToSteal);
                        player.get(currentPlayer - 1).getCardsInHand().remove(handIndx);
                    }
                }
                case "Firestorm" -> {
                    useFirestormCard(affectedPlayer, row);
                    manaCardUsed
                        = player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana();
                    player.get(currentPlayer - 1).getCardsInHand().remove(handIndx);
                }
                case "Winterfell" -> {
                    useWinterfellCard(affectedPlayer, row);
                    manaCardUsed
                        = player.get(currentPlayer - 1).getCardsInHand().get(handIndx).getMana();
                    player.get(currentPlayer - 1).getCardsInHand().remove(handIndx);
                }
                default -> {
                }
            }
            player.get(currentPlayer - 1).setMana(player.get(currentPlayer - 1).getMana()
                    - manaCardUsed);
        }
    }

    /**
     * @return index of card with max health from enemy row
     */
    public int useHeartHoundCard(final int attackedPlayerIdx, final int row) {
        int maxHealth = -1;
        int index = 0;
        int i = 0;
        while (i < player.get(attackedPlayerIdx - 1).getCardsInRow().get(row).size()) {
            if (player.get(attackedPlayerIdx - 1).getCardsInRow().get(row).get(i).getHealth()
                    > maxHealth) {
                maxHealth
                   = player.get(attackedPlayerIdx - 1).getCardsInRow().get(row).get(i).getHealth();
                index = i;
            }
            i++;
        }
        return index;
    }

    /**
     * use the ability for Firestorm card
     */
    public void useFirestormCard(final int affectedPlayer, final int row) {
        int i = 0;
        while (i < player.get(affectedPlayer - 1).getCardsInRow().get(row).size()) {
            int health
                  = player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i).getHealth() - 1;
            player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i).setHealth(health);

            if (player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i).getHealth() <= 0) {
                player.get(affectedPlayer - 1).getCardsInRow().get(row).remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * use the ability for Winterfell card
     */
    public void useWinterfellCard(final int affectedPlayer, final int row) {
        int i = 0;
        while (i < player.get(affectedPlayer - 1).getCardsInRow().get(row).size()) {
            if (player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i)
                    instanceof MinionCard) {
                ((MinionCard)
                   player.get(affectedPlayer - 1).getCardsInRow().get(row).get(i)).setFrozen(true);
            }
            i++;
        }
    }

    /**
     * return the card existing at a given position on the table
     */
    public void getCardAtPosition(final int x, final int y) {
        int row = 0;
        int playerIdx = 0;
        if (x == 0) {
            playerIdx = 2;
            row = 1;
        } else if (x == 1) {
            playerIdx = 2;
        } else if (x == 2) {
            playerIdx = 1;
        } else if (x == MagicNumber.THREE) {
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
            objectMapper.convertValue(player.get(playerIdx - 1).getCardsInRow().get(row).get(y),
            JsonNode.class));
        }
        output.add(node);
    }

    /**
     * print all frozen cards on table
     */
    public void getFrozenCardsOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getFrozenCardsOnTable");
        ArrayList<Card> frozenTable = new ArrayList<>(MagicNumber.FOUR);

        /* check each row for frozen cards */
        for (int i = 0; i < player.get(1).getCardsInRow().get(1).size(); i++) {
            if (player.get(1).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(1).getCardsInRow().get(1).get(i)).isFrozen()) {
                    frozenTable.add(player.get(1).getCardsInRow().get(1).get(i));
                }
            }
        }

        for (int i = 0; i < player.get(1).getCardsInRow().get(0).size(); i++) {
            if (player.get(1).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(1).getCardsInRow().get(0).get(i)).isFrozen()) {
                    frozenTable.add(player.get(1).getCardsInRow().get(0).get(i));
                }
            }
        }

        for (int i = 0; i < player.get(0).getCardsInRow().get(0).size(); i++) {
            if (player.get(0).getCardsInRow().get(0).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(0).getCardsInRow().get(0).get(i)).isFrozen()) {
                    frozenTable.add(player.get(0).getCardsInRow().get(0).get(i));
                }
            }
        }

        for (int i = 0; i < player.get(0).getCardsInRow().get(1).size(); i++) {
            if (player.get(0).getCardsInRow().get(1).get(i) instanceof MinionCard) {
                if (((MinionCard) player.get(0).getCardsInRow().get(1).get(i)).isFrozen()) {
                    frozenTable.add(player.get(0).getCardsInRow().get(1).get(i));
                }
            }
        }
        node.set("output", objectMapper.convertValue(frozenTable, JsonNode.class));
        output.add(node);
    }

    /**
     * a card from table uses its attack on enemy
     */
    public void cardUsesAttack(final int xAttacker, final int yAttacker,
                               final int xAttacked, final int yAttacked) {
        Coordinates coord1 = new Coordinates();
        coord1.setX(xAttacked);
        coord1.setY(yAttacked);
        Coordinates coord2 = new Coordinates();
        coord2.setX(xAttacker);
        coord2.setY(yAttacker);
        if ((currentPlayer == 2 && (xAttacked == 0 || xAttacked == 1))
                || (currentPlayer == 1
                && (xAttacked == 2 || xAttacked == MagicNumber.THREE))) {
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
            int rowAttacked = getAttackedRow(xAttacked, playerAttacked);
            int rowAttacker = getAttackerRow(xAttacker);

            if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)
                    instanceof MinionCard) {
                if (((MinionCard) player.get(currentPlayer - 1).
                        getCardsInRow().get(rowAttacker).get(yAttacker)).isAttack()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAttack");
                    node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                    node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                    node.put("error", "Attacker card has already attacked this turn.");
                    output.add(node);
                } else if (((MinionCard) player.get(currentPlayer - 1).
                        getCardsInRow().get(rowAttacker).get(yAttacker)).isFrozen()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAttack");
                    node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                    node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                    node.put("error", "Attacker card is frozen.");
                    output.add(node);
                } else {
                    if (player.get(playerAttacked - 1).getCardsInRow().
                            get(rowAttacked).get(yAttacked).getName().equals("Goliath")
                            || player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                            get(yAttacked).getName().equals("Warden")) {
                        /* valid attack on a tank card */
                        attackCard(playerAttacker, playerAttacked, rowAttacker,
                                   rowAttacked, yAttacker, yAttacked);
                    } else {
                        if (checkIfOtherTank(playerAttacked)) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            ObjectNode node = objectMapper.createObjectNode();
                            node.put("command", "cardUsesAttack");
                            node.set("cardAttacker", objectMapper.convertValue(coord2,
                                    JsonNode.class));
                            node.set("cardAttacked", objectMapper.convertValue(coord1,
                                    JsonNode.class));
                            node.put("error", "Attacked card is not of type 'Tank'.");
                            output.add(node);
                        } else {
                            /* valid attack on a card */
                            attackCard(playerAttacker, playerAttacked, rowAttacker,
                                       rowAttacked, yAttacker, yAttacked);
                        }
                    }
                }
            }
        }
    }

    /**
     * change health for attacked card and set attack true for the attacker card
     */
    public void attackCard(final int playerAttacker, final int playerAttacked,
                           final int rowAttacker, final int rowAttacked,
                           final int yAttacker, final int yAttacked) {
        int health
                = player.get(playerAttacked - 1).getCardsInRow().
                get(rowAttacked).get(yAttacked).getHealth()
                - player.get(playerAttacker - 1).getCardsInRow().
                get(rowAttacker).get(yAttacker).getAttackDamage();

        if (health <= 0) {
            player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).remove(yAttacked);
        } else {
            player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                    setHealth(health);
        }
        if (player.get(playerAttacker - 1).getCardsInRow().get(rowAttacker).get(yAttacker)
                instanceof MinionCard) {
            ((MinionCard)
                    player.get(playerAttacker - 1).getCardsInRow().get(rowAttacker).get(yAttacker)).
                    setAttack(true);
        }
    }

    /**
     * check if there are any tank cards on enemy's row
     */
    public boolean checkIfOtherTank(final int playerAttacked) {
        for (int i = 0; i < player.get(playerAttacked - 1).getCardsInRow().get(0).size(); i++) {
            if (player.get(playerAttacked - 1).getCardsInRow().get(0).get(i).
                    getName().equals("Goliath")
                    || (player.get(playerAttacked - 1).getCardsInRow().get(0).get(i).
                    getName().equals("Warden"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * card on table uses its ability
     */
    public void cardUsesAbility(final int xAttacker, final int yAttacker,
                                final int xAttacked, final int yAttacked) {
        Coordinates coord1 = new Coordinates();
        coord1.setX(xAttacked);
        coord1.setY(yAttacked);
        Coordinates coord2 = new Coordinates();
        coord2.setX(xAttacker);
        coord2.setY(yAttacker);

        int rowAttacker = getAttackerRow(xAttacker);
        int rowAttacked;
        int playerAttacked;

        /* errors */
        if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)
                instanceof MinionCard) {
            if (((MinionCard)
                    player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)).
                    isFrozen()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacker card is frozen.");
                output.add(node);
                return;
            }
            if (((MinionCard)
                    player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)).
                    isAttack()) {
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
        if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker).
                getName().equals("Disciple")) {
            if ((currentPlayer == 1 && (xAttacked == 0 || xAttacked == 1))
                    || (currentPlayer == 2
                    && (xAttacked == 2 || xAttacked == MagicNumber.THREE))) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacked card does not belong to the current player.");
                output.add(node);
            } else {
                playerAttacked = currentPlayer;
                rowAttacked = getAttackedRow(xAttacked, playerAttacked);
                healDisciple(rowAttacked, yAttacked);
                if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)
                        instanceof MinionCard) {
                    ((MinionCard)
                            player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).
                            get(yAttacker)).setAttack(true);
                }
            }
        } else {
            /* Miraj/ The Ripper/ The Cursed One */
            playerAttacked = getPlayerAttackedIdx();
            rowAttacked = getAttackedRow(xAttacked, playerAttacked);

            if ((currentPlayer == 1
                    && (xAttacked == 2 || xAttacked == MagicNumber.THREE))
                    || (currentPlayer == 2 && (xAttacked == 0 || xAttacked == 1))) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                node.put("error", "Attacked card does not belong to the enemy.");
                output.add(node);
            } else {
                /* check if the attacked card is tank */
                if (player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                        getName().equals("Goliath")
                        || player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                        get(yAttacked).getName().equals("Warden")) {
                    abilityCard(playerAttacked, rowAttacker, rowAttacked, yAttacker, yAttacked);
                } else {
                    if (checkIfOtherTank(playerAttacked)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ObjectNode node = objectMapper.createObjectNode();
                        node.put("command", "cardUsesAbility");
                        node.set("cardAttacker", objectMapper.convertValue(coord2, JsonNode.class));
                        node.set("cardAttacked", objectMapper.convertValue(coord1, JsonNode.class));
                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(node);
                    } else {
                        abilityCard(playerAttacked, rowAttacker, rowAttacked, yAttacker, yAttacked);
                    }
                }
            }
        }
    }

    /**
     * apply ability for Miraj/ The Ripper/ The Cursed One
     */
    public void abilityCard(final int playerAttacked, final int rowAttacker,
                            final int rowAttacked, final int yAttacker, final int yAttacked) {
        switch (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).
                get(yAttacker).getName()) {
            case "The Ripper" -> {
                /* -2 attackDamage for each minion card on enemy row */
                int att
                        = player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                        get(yAttacked).getAttackDamage() - 2;
                if (att < 0) {
                    att = 0;
                }
                player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                        setAttackDamage(att);
            }
            case "Miraj" -> {
                /* swap its life with minion from enemy row */
                int healthMiraj
                        = player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).
                        get(yAttacker).getHealth();
                int healthEnemy
                        = player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                        get(yAttacked).getHealth();

                player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker).
                        setHealth(healthEnemy);
                player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                        setHealth(healthMiraj);
            }
            case "The Cursed One" -> {
                /* swap health - attackDamage for minion on enemy row */
                int healthEnemy
                        = player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                        get(yAttacked).getHealth();
                int attackEnemy
                        = player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                        get(yAttacked).getAttackDamage();

                player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                        setHealth(attackEnemy);
                player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                        setAttackDamage(healthEnemy);

                if (player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                        getHealth() <= 0) {
                    player.get(playerAttacked - 1).getCardsInRow().get(rowAttacked).
                            remove(yAttacked);
                }
            }
            default -> {
            }
        }
        if (player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)
                instanceof MinionCard) {
            ((MinionCard)
                    player.get(currentPlayer - 1).getCardsInRow().get(rowAttacker).get(yAttacker)).
                    setAttack(true);
        }
    }

    /**
     * apply ability for Disciple card
     */
    public void healDisciple(final int rowAttacked, final int yAttacked) {
        /* +2 health for minion on its team */
        int health
                = player.get(currentPlayer - 1).getCardsInRow().get(rowAttacked).
                get(yAttacked).getHealth()
                + 2;
        player.get(currentPlayer - 1).getCardsInRow().get(rowAttacked).get(yAttacked).
                setHealth(health);
    }

    /**
     * card uses attack against enemy hero
     */
    public void useAttackHero(final int x, final int y) {
        Coordinates coord = new Coordinates();
        coord.setX(x);
        coord.setY(y);

        int row = getAttackerRow(x);
        int attackedPlayer = getPlayerAttackedIdx();

        /* errors */
        if (player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)
                instanceof MinionCard) {
            if (((MinionCard)
                    player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)).
                    isFrozen()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "useAttackHero");
                node.set("cardAttacker", objectMapper.convertValue(coord, JsonNode.class));
                node.put("error", "Attacker card is frozen.");
                output.add(node);
                return;
            }
            if (((MinionCard)
                    player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)).
                    isAttack()) {
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

        /* valid attack on hero card */
        int damage
                = player.get(currentPlayer - 1).getCardsInRow().get(row).get(y).getAttackDamage();
        int heroLife = player.get(attackedPlayer - 1).getHero().getHealth();

        player.get(attackedPlayer - 1).getHero().setHealth(heroLife - damage);
        if (player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)
                instanceof MinionCard) {
            ((MinionCard)
                    player.get(currentPlayer - 1).getCardsInRow().get(row).get(y)).setAttack(true);
        }

        /* check if hero is killed -> game over */
        if (player.get(attackedPlayer - 1).getHero().getHealth() <= 0) {
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

    /**
     * hero card uses its ability against enemy minion
     */
    public void useHeroAbility(final int affectedRow) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        int row;

        /* errors */
        if (player.get(currentPlayer - 1).getMana()
                < player.get(currentPlayer - 1).getHero().getMana()) {
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
            if ((currentPlayer == 1
                    && (affectedRow == 2 || affectedRow == MagicNumber.THREE))
                    || (currentPlayer == 2 && (affectedRow == 0 || affectedRow == 1))) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Selected row does not belong to the enemy.");
                output.add(node);
                return;
            } else {
                /* valid use of ability for Lord Royce or Empress Thorina hero cards */
                int attackedPlayer = getPlayerAttackedIdx();
                row = getAttackedRow(affectedRow, attackedPlayer);
                useHeroAbilityOnEnemy(row, attackedPlayer);
            }
        } else  if (player.get(currentPlayer - 1).getHero().getName().equals("General Kocioraw")
                || player.get(currentPlayer - 1).getHero().getName().equals("King Mudface")) {
            if ((currentPlayer == 1 && (affectedRow == 0 || affectedRow == 1))
                    || (currentPlayer == 2
                    && (affectedRow == 2 || affectedRow == MagicNumber.THREE))) {
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Selected row does not belong to the current player.");
                output.add(node);
                return;
            } else {
                /* valid use of ability for General Kocioraw or King Mudface hero cards */
                row = getAttackedRow(affectedRow, currentPlayer);
                useHeroAbilityOnSelf(row);
            }
        }
        /* decrement current player's mana */
        int newMana
                = player.get(currentPlayer - 1).getMana() - player.get(currentPlayer - 1).
                getHero().getMana();
        player.get(currentPlayer - 1).setMana(newMana);
    }

    /**
     * specific ability for Empress Thorina and Lord Royce hero cards
     */
    public void useHeroAbilityOnEnemy(final int row, final int attackedPlayer) {
        if (player.get(currentPlayer - 1).getHero().getName().equals("Empress Thorina")) {
            int index = 0;
            int maxHealth = 0;
            int i = 0;
            while (i < player.get(attackedPlayer - 1).getCardsInRow().get(row).size()) {
                if (player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).getHealth()
                        > maxHealth) {
                    maxHealth
                            = player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).
                            getHealth();
                    index = i;
                }
                i++;
            }
            player.get(attackedPlayer - 1).getCardsInRow().get(row).remove(index);
        } else {
            int index = 0;
            int maxDamage = -1;
            int i = 0;
            while (i < player.get(attackedPlayer - 1).getCardsInRow().get(row).size()) {
                if (player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).getAttackDamage()
                        > maxDamage) {
                    maxDamage
                            = player.get(attackedPlayer - 1).getCardsInRow().get(row).get(i).
                            getAttackDamage();
                    index = i;
                }
                i++;
            }
            if (player.get(attackedPlayer - 1).getCardsInRow().get(row).get(index)
                    instanceof MinionCard) {
                ((MinionCard)
                        player.get(attackedPlayer - 1).getCardsInRow().get(row).get(index)).
                        setFrozen(true);
            }
        }
        if (player.get(currentPlayer - 1).getHero() instanceof HeroCard) {
            ((HeroCard) player.get(currentPlayer - 1).getHero()).setAbility(true);
        }
    }

    /**
     * specific ability for General Kocioraw and King Mudface hero cards
     */
    public void useHeroAbilityOnSelf(final int row) {
        int value;
        int i = 0;
        if (player.get(currentPlayer - 1).getHero().getName().equals("King Mudface")) {
            while (i < player.get(currentPlayer - 1).getCardsInRow().get(row).size()) {
                value
                  = player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).getHealth()
                  + 1;
                player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).setHealth(value);
                i++;
            }
        } else {
            while (i < player.get(currentPlayer - 1).getCardsInRow().get(row).size()) {
                value
                   = player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).getAttackDamage()
                   + 1;
                player.get(currentPlayer - 1).getCardsInRow().get(row).get(i).
                        setAttackDamage(value);
                i++;
            }
        }
        if (player.get(currentPlayer - 1).getHero() instanceof HeroCard) {
            ((HeroCard) player.get(currentPlayer - 1).getHero()).setAbility(true);
        }
    }

    /**
     * print number of played games
     */
    public void getTotalGamesPlayed() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getTotalGamesPlayed");
        node.put("output", gamesNumber);
        output.add(node);
    }

    /**
     * print number of player one's winned games
     */
    public void getPlayerOneWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerOneWins");
        node.put("output", winsPlayerOne);
        output.add(node);
    }

    /**
     * print number of player two's winned games
     */
    public void getPlayerTwoWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerTwoWins");
        node.put("output", winsPlayerTwo);
        output.add(node);
    }

    /* helper functions */

    /**
     * @return index of attacked player
     */
    public int getPlayerAttackedIdx() {
        if (currentPlayer == 1) {
            return 2;
        }
        return 1;
    }

    /**
     * @return attacker's row
     */
    public int getAttackerRow(final int x) {
        if (currentPlayer == 2) {
            if (x == 0) {
                return 1;
            }
            return 0;
        } else {
            if (x == 2) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * @return attacked row
     */
    public int getAttackedRow(final int x, final int playerIdx) {
        if (playerIdx == 1) {
            if (x == 2) {
                return 0;
            }
            return 1;
        } else {
            if (x == 0) {
                return 1;
            }
            return 0;
        }
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

    public int getTurn() {
        return turn;
    }

    public void setTurn(final int turn) {
        this.turn = turn;
    }
}
