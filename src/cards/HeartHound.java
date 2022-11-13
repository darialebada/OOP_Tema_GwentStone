package cards;

import java.util.ArrayList;

public class HeartHound extends Card {
    private String ability;
    /**
     * ability = steal the minion with most health and place it opposite
     */
    public HeartHound(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "steal";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
