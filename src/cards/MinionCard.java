package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "row", "frozen", "type", "attack" })
public final class MinionCard extends Card {
    private final int row;
    private boolean frozen;
    private boolean attack;

    public MinionCard(final int mana, final int attackDamage, final int health,
                      final String description, final ArrayList<String> colors,
                      final String name, final int row) {
        super(mana, attackDamage, health, description, colors, name);
        this.row = row;
        this.frozen = false;
        this.setType("Minion");
        this.attack = false;
    }

    public int getRow() {
        return row;
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

    public void setAttack(final boolean attack) {
        this.attack = attack;
    }

}
