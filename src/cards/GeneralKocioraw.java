package cards;

import java.util.ArrayList;

public class GeneralKocioraw extends Card {
    private String ability;
    /**
     * ability = +1 attack for all card from row
     */
    public GeneralKocioraw(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "Blood Thirst";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
