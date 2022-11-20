package actions;

import cards.Card;
public final class StartGame {
    private final int playerOneDeckIdx;
    private final int playerTwoDeckIdx;
    private final int shuffleSeed;
    private final Card playerOneHero;
    private final Card playerTwoHero;
    private final int startingPlayer;

    public StartGame(final int playerOneDeckIdx, final int playerTwoDeckIdx, final int shuffleSeed,
                     final Card playerOneHero, final Card playerTwoHero, final int startingPlayer) {
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.shuffleSeed = shuffleSeed;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.startingPlayer = startingPlayer;
    }

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public Card getPlayerOneHero() {
        return playerOneHero;
    }

    public Card getPlayerTwoHero() {
        return playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

}
