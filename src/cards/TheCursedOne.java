package cards;

import java.util.ArrayList;

public class TheCursedOne extends Card {
    private int row;
    private boolean frozen;
    private String ability;
    /**
     * ability = "Shapeshift", can swap for an enemy minion life with attack
     */

    public TheCursedOne(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamange, health, description, colors, name);
        this.setAttackDamage(0);
        this.row = 1;
        this.ability = "Shapeshift";
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
