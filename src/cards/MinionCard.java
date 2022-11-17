package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "row", "frozen", "type", "ability" })
public class MinionCard extends Card {
    private int row;
    private boolean frozen;
    private boolean ability;

    public MinionCard(final int mana, final int attackDamange, final int health,
                      final String description, final ArrayList<String> colors,
                      final String name, final int row) {
        super(mana, attackDamange, health, description, colors, name);
        this.row = row;
        this.frozen = false;
        this.setType("Minion");
        this.ability = false;
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

    public boolean isAbility() {
        return ability;
    }

    public void setAbility(final boolean ability) {
        this.ability = ability;
    }

    @Override
    public String toString() {
        return "MinionCard{" +
                "row=" + row +
                ", frozen=" + frozen +
                ", ability=" + ability +
                '}' + super.getName();
    }
}
