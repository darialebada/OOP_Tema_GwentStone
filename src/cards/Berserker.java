package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "row", "frozen" })
public class Berserker extends Card {
    private int row;
    private boolean frozen ;

    public Berserker(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, attackDamange, health, description, colors, name);
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
