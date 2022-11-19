package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "row", "frozen", "type", "attack" })
public class MinionCard extends Card {
    private int row;
    private boolean frozen;
    private boolean attack;

    public MinionCard(final int mana, final int attackDamange, final int health,
                      final String description, final ArrayList<String> colors,
                      final String name, final int row) {
        super(mana, attackDamange, health, description, colors, name);
        this.row = row;
        this.frozen = false;
        this.setType("Minion");
        this.attack = false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

}
