package gameplay;

import cards.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cardsFirstRow;
    private ArrayList<Card> cardsSecondRow;
    private ArrayList<Card> deck;
    private ArrayList<Card> cardsInHand;
    private Card hero;
    private int handIdx;

    public Player(ArrayList<Card> deck, Card hero) {
        this.cardsFirstRow = new ArrayList<>();
        this.cardsSecondRow = new ArrayList<>();
        this.deck = new ArrayList<Card>(deck);
        this.cardsInHand = new ArrayList<Card>();
        this.hero = hero;
        this.handIdx = 0;
    }

    public ArrayList<Card> getCardsFirstRow() {
        return cardsFirstRow;
    }

    public void setCardsFirstRow(ArrayList<Card> cardsFirstRow) {
        this.cardsFirstRow = cardsFirstRow;
    }

    public ArrayList<Card> getCardsSecondRow() {
        return cardsSecondRow;
    }

    public void setCardsSecondRow(ArrayList<Card> cardsSecondRow) {
        this.cardsSecondRow = cardsSecondRow;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(ArrayList<Card> cardsHand) {
        this.cardsInHand = cardsHand;
    }

    public Card getHero() {
        return hero;
    }

    public void setHero(Card hero) {
        this.hero = hero;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public void setHandIdx(int handIdx) {
        this.handIdx = handIdx;
    }
}
