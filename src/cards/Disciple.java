package cards;

import java.util.ArrayList;

public class Disciple extends Card {
    private int row;
    private boolean frozen;
    private String ability;
    /**
     * ability = "God's Plan", +2 for a minion teammate
     */
    public Disciple(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamange, description, colors, name);
        this.setAttackDamage(0);
        this.row = 1;
        this.ability = "God's Plan";
        this.frozen = false;
    }
}