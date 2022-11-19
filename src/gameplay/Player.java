package gameplay;

import actions.PrepareGame;
import cards.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<ArrayList<Card>> cardsInRow;
    private ArrayList<Card> deck;
    private ArrayList<Card> cardsInHand;
    private Card hero;
    private int mana;

    public Player(final ArrayList<Card> deck1, final Card hero) {
        this.cardsInRow = new ArrayList<ArrayList<Card>>(2);
        this.cardsInRow.add(new ArrayList<Card>());
        this.cardsInRow.add(new ArrayList<Card>());
        this.deck = new ArrayList<Card>();
        for (int i = 0; i < deck1.size(); i++) {
            this.deck.add(PrepareGame.prepareCard_again(deck1.get(i)));
        }
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
