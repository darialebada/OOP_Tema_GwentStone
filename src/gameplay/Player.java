package gameplay;

import actions.PrepareGame;
import cards.Card;

import java.util.ArrayList;

public final class Player {
    private final ArrayList<ArrayList<Card>> cardsInRow;
    private final ArrayList<Card> deck;
    private final ArrayList<Card> cardsInHand;
    private Card hero;
    private int mana;

    public Player(final ArrayList<Card> deck, final Card hero) {
        this.cardsInRow = new ArrayList<>(2);
        this.cardsInRow.add(new ArrayList<>());
        this.cardsInRow.add(new ArrayList<>());
        this.deck = new ArrayList<>();
        /* create deep-copy of each card */
        for (Card card : deck) {
            this.deck.add(PrepareGame.prepareCardNewGame(card));
        }
        this.cardsInHand = new ArrayList<>();
        this.hero = hero;
        this.mana = 1;
    }

    public ArrayList<ArrayList<Card>> getCardsInRow() {
        return cardsInRow;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Card getHero() {
        return hero;
    }

    public void setHero(final Card hero) {
        this.hero = hero;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }
}
