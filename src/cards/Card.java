package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties({ "type" })
public class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private String type;

    public Card() { }

    public Card(final Card card) {
        this.mana = card.mana;
        this.health = card.health;
        this.attackDamage = card.attackDamage;
        this.description = card.description;
        this.colors = new ArrayList<>(card.colors);
        this.name = card.name;
        this.type = card.type;
    }
    public Card(final int mana, final int attackDamage, final int health,
                final String description, final ArrayList<String> colors, final String name) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    /**
     *
     * @return player's current mana
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @param mana = game currency
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return player's current health
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @param health = card's life
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     *
     * @return player's attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     *
     * @param attackDamage = how much damage it can do to other cards
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     *
     * @return card's description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description = short description of the card
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @return card's colors
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     *
     * @param colors = card's graphic design
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     *
     * @return card's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name = card's name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return card's type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type = Minion/ Environment/ Hero
     */
    public void setType(final String type) {
        this.type = type;
    }
}
