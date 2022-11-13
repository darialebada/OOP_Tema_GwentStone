package cards;

import java.util.ArrayList;

public class Warden extends Card{
    private int row;
    private boolean frozen;
    private String ability;
    /**
     * ability = "Tank", pasive ability, this card must be attacked first
     */

    public Warden(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamange, description, colors, name);
        this.row = 1;
        this.ability = "Tank";
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
