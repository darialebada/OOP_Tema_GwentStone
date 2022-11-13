package actions;

import cards.*;
import fileio.*;

import java.util.ArrayList;

public class PrepareGame {
    /**
     * functions to parse the data and save it in a ArrayList of decks
     * @param decksInput decks read from file
     * @return decks for each player
     */
    public static ArrayList<ArrayList<Card>> prepareDecks (DecksInput decksInput) {
        ArrayList<ArrayList<Card>> decksPlayer = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < decksInput.getNrDecks(); i++) {
            // get each deck
            ArrayList<Card> newDeck = new ArrayList<Card>();
            ArrayList<CardInput> readDeck = decksInput.getDecks().get(i);
            for (int j = 0; j < decksInput.getNrCardsInDeck(); j++) {
                Card card = new Card(prepareCard(readDeck.get(i)));
                newDeck.add(card);
            }
            decksPlayer.add(newDeck);
        }
        return decksPlayer;
    }

    public static Card prepareCard (CardInput readCard) {
        Card card;
        if (readCard.getName().equals("Sentinel")) {
            card = new Sentinel(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Berserker")) {
                card = new Berserker(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                     readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Goliath")) {
                card = new Goliath(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                   readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Warden")) {
                card = new Warden(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                  readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("The Ripper")) {
                card = new TheRipper(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                     readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Miraj")) {
                card = new Miraj(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                 readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("The Cursed One")) {
                card = new TheCursedOne(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                        readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Disciple")) {
                card = new Disciple(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                    readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Firestorm")) {
                card = new Firestorm(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                     readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Winterfell")) {
                card = new Winterfell(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                      readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Heart Hound")) {
                card = new HeartHound(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                      readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Lord Royce")) {
                card = new LordRoyce(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                     readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("Empress Thorina")) {
                card = new EmpressThorina(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                          readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("King Mudface")) {
                card = new KingMudFace(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                       readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else
            if (readCard.getName().equals("General Kocioraw")) {
                card = new GeneralKocioraw(readCard.getMana(), readCard.getHealth(), readCard.getAttackDamage(),
                                           readCard.getDescription(), readCard.getColors(), readCard.getName());
        } else return null;

        return card;
    }

    /**
     * function to parse the commands given for the current game
     */
    public static ArrayList<GameActions> prepareActions (ArrayList<GameInput> readGame) {
        ArrayList<GameActions> games = new ArrayList<GameActions>();
        for (int i = 0; i < readGame.size(); i++) {
            StartGameInput readStartGame = readGame.get(i).getStartGame();
            StartGame startGame = new StartGame(readStartGame.getPlayerOneDeckIdx(), readStartGame.getPlayerTwoDeckIdx(),
                                                readStartGame.getShuffleSeed(), new Card(prepareCard(readStartGame.getPlayerOneHero())),
                                                new Card(prepareCard(readStartGame.getPlayerTwoHero())), readStartGame.getStartingPlayer());
            GameActions ggame = new GameActions();
            ggame.setStartGame(startGame);
            ArrayList<Action> actions = new ArrayList<Action>();
            for (int j = 0; j < readGame.get(i).getActions().size(); j++) {
                ActionsInput readActions = readGame.get(i).getActions().get(j);
                Action act = new Action(readActions.getCommand(), readActions.getHandIdx(), readActions.getCardAttacker(),
                                        readActions.getCardAttacked(), readActions.getAffectedRow(), readActions.getPlayerIdx(),
                                        readActions.getX(), readActions.getY());
                actions.add(act);
            }
            ggame.setActions(actions);
            games.add(ggame);
        }
        return games;
    }
}
