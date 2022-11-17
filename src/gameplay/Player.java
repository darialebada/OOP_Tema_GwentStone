package gameplay;

import cards.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<ArrayList<Card>> cardsInRow;
    private ArrayList<Card> deck;
    private ArrayList<Card> cardsInHand;
    private Card hero;
    private int mana;

    public Player(final ArrayList<Card> deck, final Card hero) {
        this.cardsInRow = new ArrayList<ArrayList<Card>>(2);
        this.cardsInRow.add(new ArrayList<Card>());
        this.cardsInRow.add(new ArrayList<Card>());
        this.deck = new ArrayList<Card>(deck);
        this.cardsInHand = new ArrayList<Card>();
        this.hero = hero;
        this.mana = 1;
    }

    public ArrayList<ArrayList<Card>> getCardsInRow() {
        return cardsInRow;
    }

    public void setCardsInRow(final ArrayList<ArrayList<Card>> cardsInRow) {
        this.cardsInRow = cardsInRow;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(final ArrayList<Card> cardsHand) {
        this.cardsInHand = cardsHand;
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
