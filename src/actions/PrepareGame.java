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

public final class PrepareGame {
    private PrepareGame() {
    }

    /**
     * functions to parse the data and save it in a ArrayList of decks
     * @param decksInput decks read from file
     * @return decks for each player
     */
    public static ArrayList<ArrayList<Card>> prepareDecks(final DecksInput decksInput) {
        ArrayList<ArrayList<Card>> decksPlayer = new ArrayList<>();
        for (int i = 0; i < decksInput.getNrDecks(); i++) {
            // get each deck
            ArrayList<Card> newDeck = new ArrayList<>();
            ArrayList<CardInput> readDeck = decksInput.getDecks().get(i);
            for (int j = 0; j < decksInput.getNrCardsInDeck(); j++) {
                newDeck.add(prepareCard(readDeck.get(j)));
            }
            decksPlayer.add(newDeck);
        }
        return decksPlayer;
    }

    /**
     * function that creates a card from CardInput
     */
    public static Card prepareCard(final CardInput readCard) {
        return switch (readCard.getName()) {
            case "Sentinel", "Berserker", "The Cursed One", "Disciple" ->
                    new MinionCard(readCard.getMana(), readCard.getAttackDamage(),
                            readCard.getHealth(), readCard.getDescription(),
                            readCard.getColors(), readCard.getName(), 2);
            case "The Ripper", "Miraj", "Goliath", "Warden" ->
                    new MinionCard(readCard.getMana(), readCard.getAttackDamage(),
                            readCard.getHealth(), readCard.getDescription(),
                            readCard.getColors(), readCard.getName(), 1);
            case "Firestorm", "Winterfell", "Heart Hound" ->
                    new EnvironmentCard(readCard.getMana(), readCard.getAttackDamage(),
                            readCard.getHealth(), readCard.getDescription(),
                            readCard.getColors(), readCard.getName());
            default -> new HeroCard(readCard.getMana(), readCard.getAttackDamage(),
                    readCard.getHealth(), readCard.getDescription(),
                    readCard.getColors(), readCard.getName());
        };
    }

    /**
     * function that creates a card (for deep-copy)
     */
    public static Card prepareCardNewGame(final Card readCard) {
        return switch (readCard.getName()) {
            case "Sentinel", "Berserker", "The Cursed One", "Disciple" ->
                    new MinionCard(readCard.getMana(), readCard.getAttackDamage(),
                            readCard.getHealth(), readCard.getDescription(),
                            readCard.getColors(), readCard.getName(), 2);
            case "The Ripper", "Miraj", "Goliath", "Warden" ->
                    new MinionCard(readCard.getMana(), readCard.getAttackDamage(),
                            readCard.getHealth(), readCard.getDescription(),
                            readCard.getColors(), readCard.getName(), 1);
            case "Firestorm", "Winterfell", "Heart Hound" ->
                    new EnvironmentCard(readCard.getMana(), readCard.getAttackDamage(),
                            readCard.getHealth(), readCard.getDescription(),
                            readCard.getColors(), readCard.getName());
            default -> new HeroCard(readCard.getMana(), readCard.getAttackDamage(),
                    readCard.getHealth(), readCard.getDescription(),
                    readCard.getColors(), readCard.getName());
        };
    }

    /**
     * function to parse the commands given for the current game
     */
    public static ArrayList<GameActions> prepareActions(final ArrayList<GameInput> readGame) {
        ArrayList<GameActions> games = new ArrayList<>();
        for (GameInput gameInput : readGame) {
            StartGameInput readStartGame = gameInput.getStartGame();
            /* create startGame array */
            StartGame startGame = new StartGame(readStartGame.getPlayerOneDeckIdx(),
                    readStartGame.getPlayerTwoDeckIdx(),
                    readStartGame.getShuffleSeed(),
                    prepareCard(readStartGame.getPlayerOneHero()),
                    prepareCard(readStartGame.getPlayerTwoHero()),
                    readStartGame.getStartingPlayer());
            GameActions ggame = new GameActions();
            ggame.setStartGame(startGame);
            ArrayList<Action> actions = new ArrayList<>();
            for (int j = 0; j < gameInput.getActions().size(); j++) {
                ActionsInput readActions = gameInput.getActions().get(j);
                /* create action array */
                Action act = new Action(readActions.getCommand(),
                        readActions.getHandIdx(), readActions.getCardAttacker(),
                        readActions.getCardAttacked(), readActions.getAffectedRow(),
                        readActions.getPlayerIdx(), readActions.getX(),
                        readActions.getY());
                actions.add(act);
            }
            ggame.setActions(actions);
            games.add(ggame);
        }
        return games;
    }
}
