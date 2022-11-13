package cards;

import java.util.ArrayList;

public class LordRoyce extends Card {
    private String ability;
    /**
     * ability = frozes the card with maximum health from row
     */
    public LordRoyce(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "Sub-Zero";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
