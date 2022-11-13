package cards;

import java.util.ArrayList;

public class Sentinel extends Card {
    private int row;
    private boolean frozen;

    public Sentinel(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, health, attackDamange, description, colors, name);
        this.row = 2;
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
}
