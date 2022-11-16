package actions;

import cards.Card;
public class StartGame {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private Card playerOneHero;
    private Card playerTwoHero;
    private int startingPlayer;

    public StartGame(final int playerOneDeckIdx, final int playerTwoDeckIdx, final int shuffleSeed,
                     final Card playerOneHero, final Card playerTwoHero, final int startingPlayer) {
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.shuffleSeed = shuffleSeed;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.startingPlayer = startingPlayer;
    }

    /**
     *
     * @return index for player one's deck
     */
    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    /**
     * set index for player one's deck
     */
    public void setPlayerOneDeckIdx(final int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    /**
     *
     * @return index for player two's deck
     */
    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    /**
     * set index for player two's deck
     */
    public void setPlayerTwoDeckIdx(final int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    /**
     *
     * @return seed for shuffle (to randomize cards)
     */
    public int getShuffleSeed() {
        return shuffleSeed;
    }

    /**
     *
     * @param shuffleSeed
     */
    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    /**
     *
     * @return hero card for player one
     */
    public Card getPlayerOneHero() {
        return playerOneHero;
    }

    /**
     *
     * @param playerOneHero
     */
    public void setPlayerOneHero(final Card playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    /**
     *
     * @return hero card for player two
     */
    public Card getPlayerTwoHero() {
        return playerTwoHero;
    }

    /**
     *
     * @param playerTwoHero
     */
    public void setPlayerTwoHero(final Card playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    /**
     *
     * @return index of the player who starts the game
     */
    public int getStartingPlayer() {
        return startingPlayer;
    }

    /**
     *
     * @param startingPlayer
     */
    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
}
