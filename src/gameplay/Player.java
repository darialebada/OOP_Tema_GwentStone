package gameplay;

import cards.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<ArrayList<Card>> cardsInRow;
    private ArrayList<Card> deck;
    private ArrayList<Card> cardsInHand;
    private Card hero;
    private int handIdx;

    public Player(final ArrayList<Card> deck, final Card hero) {
        this.cardsInRow = new ArrayList<ArrayList<Card>>(2);
        this.deck = new ArrayList<Card>(deck);
        this.cardsInHand = new ArrayList<Card>();
        this.hero = hero;
        this.handIdx = 0;
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

    public int getHandIdx() {
        return handIdx;
    }

    public void setHandIdx(final int handIdx) {
        this.handIdx = handIdx;
    }
}
