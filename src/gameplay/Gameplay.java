package gameplay;

import actions.Action;
import actions.GameActions;
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
    private ArrayList<Card> deckPlayerOneGame;
    private ArrayList<Card> deckPlayerTwoGame;
    private ArrayList<Card> deckPlayerOneHand;
    private ArrayList<Card> deckPlayerTwoHand;
    private ArrayList<GameActions> gameActions;
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

            // get each playing deck
            deckPlayerOneGame = new ArrayList<Card>(decksPlayerOne.get(startGame.getPlayerOneDeckIdx()));
            deckPlayerTwoGame = new ArrayList<Card>(decksPlayerTwo.get(startGame.getPlayerTwoDeckIdx()));

            startGameAction(startGame);
            ArrayList<Action> actions = gameActions.get(i).getActions();
            for (int j = 0; j < actions.size(); j++) {
                if (actions.get(j).getCommand().equals("getPlayerDeck")) {
                    getPlayerDeck(actions.get(j).getPlayerIdx());
                }
            }

        }
    }

    public void startGameAction(StartGame startGame) {
        Collections.shuffle(deckPlayerOneGame, new Random(startGame.getShuffleSeed()));
        Collections.shuffle(deckPlayerTwoGame, new Random(startGame.getShuffleSeed()));

        deckPlayerOneHand = new ArrayList<>();
        deckPlayerOneHand.add(deckPlayerOneGame.get(0));
        deckPlayerOneGame.remove(0);

        deckPlayerTwoHand = new ArrayList<>();
        deckPlayerTwoHand.add(deckPlayerTwoGame.get(0));
        deckPlayerTwoGame.remove(0);
    }

    public void getPlayerDeck(int playerIdx) {
        ArrayList<Card> deck;
        if(playerIdx == 1) {
            deck = new ArrayList<Card>(deckPlayerOneGame);
        } else {
            deck = new ArrayList<Card>(deckPlayerTwoGame);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "getPlayerDeck");
        node.put("playerIdx", playerIdx);
        node.set("output", objectMapper.convertValue(deck, JsonNode.class));
        output.add(node);
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

    public ArrayList<Card> getDeckPlayerOneHand() {
        return deckPlayerOneHand;
    }

    public void setDeckPlayerOneHand(ArrayList<Card> deckPlayerOneHand) {
        this.deckPlayerOneHand = deckPlayerOneHand;
    }

    public ArrayList<Card> getDeckPlayerTwoHand() {
        return deckPlayerTwoHand;
    }

    public void setDeckPlayerTwoHand(ArrayList<Card> deckPlayerTwoHand) {
        this.deckPlayerTwoHand = deckPlayerTwoHand;
    }

    public ArrayList<Card> getDeckPlayerOneGame() {
        return deckPlayerOneGame;
    }

    public void setDeckPlayerOneGame(ArrayList<Card> deckPlayerOneGame) {
        this.deckPlayerOneGame = deckPlayerOneGame;
    }

    public ArrayList<Card> getDeckPlayerTwoGame() {
        return deckPlayerTwoGame;
    }

    public void setDeckPlayerTwoGame(ArrayList<Card> deckPlayerTwoGame) {
        this.deckPlayerTwoGame = deckPlayerTwoGame;
    }
}
