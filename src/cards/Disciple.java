package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "ability", "row", "frozen" })
public class Disciple extends Card {
    private int row;
    private boolean frozen;
    private String ability;
    /**
     * ability = "God's Plan", +2 for a minion teammate
     */
    public Disciple(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamange, health, description, colors, name);
        this.setAttackDamage(0);
        this.row = 1;
        this.ability = "God's Plan";
        this.frozen = false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
