package cards;

import java.util.ArrayList;

public class Firestorm extends Card {
    private String ability;
    /**
     * ability = -1 life for all minions on the targeted row
     */
    public Firestorm(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "-1 life";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
