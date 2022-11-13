package cards;

import java.util.ArrayList;

public class KingMudFace extends Card {
    private String ability;
    /**
     * ability = +1 life for all cards from row
     */
    public KingMudFace(int mana, int health, int attackDamange, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "Earth Born";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
