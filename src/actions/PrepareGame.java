package actions;

import cards.Card;
import cards.EnvironmentCard;
import cards.HeroCard;
import cards.MinionCard;
import fileio.DecksInput;
import fileio.CardInput;
import fileio.GameInput;
import fileio.ActionsInput;
import fileio.StartGameInput;

import java.util.ArrayList;

public class PrepareGame {
    private PrepareGame() {
    }

    /**
     * functions to parse the data and save it in a ArrayList of decks
     * @param decksInput decks read from file
     * @return decks for each player
     */
    public static ArrayList<ArrayList<Card>> prepareDecks(final DecksInput decksInput) {
        ArrayList<ArrayList<Card>> decksPlayer = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < decksInput.getNrDecks(); i++) {
            // get each deck
            ArrayList<Card> newDeck = new ArrayList<Card>();
            ArrayList<CardInput> readDeck = decksInput.getDecks().get(i);
            for (int j = 0; j < decksInput.getNrCardsInDeck(); j++) {
                newDeck.add(prepareCard(readDeck.get(j)));
            }
            decksPlayer.add(newDeck);
        }
        return decksPlayer;
    }

    /**
     * function that creates a card
     * @param readCard
     * @return
     */
    public static Card prepareCard(final CardInput readCard) {
        Card card;
        if (readCard.getName().equals("Sentinel")
            || readCard.getName().equals("Berserker")
            || readCard.getName().equals("The Cursed One")
            || readCard.getName().equals("Disciple")) {

            card = new MinionCard(readCard.getMana(), readCard.getAttackDamage(),
                                readCard.getHealth(), readCard.getDescription(),
                                readCard.getColors(), readCard.getName(), 2);

        } else if (readCard.getName().equals("The Ripper")
                   || readCard.getName().equals("Miraj")
                   || readCard.getName().equals("Goliath")
                   || readCard.getName().equals("Warden")) {

            card = new MinionCard(readCard.getMana(), readCard.getAttackDamage(),
                    readCard.getHealth(), readCard.getDescription(),
                    readCard.getColors(), readCard.getName(), 1);

        } else if (readCard.getName().equals("Firestorm")
                   || readCard.getName().equals("Winterfell")
                   || readCard.getName().equals("Heart Hound")) {

            card = new EnvironmentCard(readCard.getMana(), readCard.getAttackDamage(),
                    readCard.getHealth(), readCard.getDescription(),
                    readCard.getColors(), readCard.getName());
        } else {
            card = new HeroCard(readCard.getMana(), readCard.getAttackDamage(),
                    readCard.getHealth(), readCard.getDescription(),
                    readCard.getColors(), readCard.getName());
        }
        return card;
    }

    /**
     * function to parse the commands given for the current game
     */
    public static ArrayList<GameActions> prepareActions(final ArrayList<GameInput> readGame) {
        ArrayList<GameActions> games = new ArrayList<GameActions>();
        for (int i = 0; i < readGame.size(); i++) {
            StartGameInput readStartGame = readGame.get(i).getStartGame();
            // create startGame array
            StartGame startGame = new StartGame(readStartGame.getPlayerOneDeckIdx(),
                                                readStartGame.getPlayerTwoDeckIdx(),
                                                readStartGame.getShuffleSeed(),
                                                prepareCard(readStartGame.getPlayerOneHero()),
                                                prepareCard(readStartGame.getPlayerTwoHero()),
                                                readStartGame.getStartingPlayer());
            GameActions ggame = new GameActions();
            ggame.setStartGame(startGame);
            ArrayList<Action> actions = new ArrayList<Action>();
            for (int j = 0; j < readGame.get(i).getActions().size(); j++) {
                ActionsInput readActions = readGame.get(i).getActions().get(j);
                // create action array
                Action act = new Action(readActions.getCommand(),
                                        readActions.getHandIdx(),
                                        readActions.getCardAttacker(),
                                        readActions.getCardAttacked(),
                                        readActions.getAffectedRow(),
                                        readActions.getPlayerIdx(),
                                        readActions.getX(), readActions.getY());
                actions.add(act);
            }
            ggame.setActions(actions);
            games.add(ggame);
        }
        return games;
    }
}
