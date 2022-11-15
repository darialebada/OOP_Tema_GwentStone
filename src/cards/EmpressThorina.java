package cards;

import java.util.ArrayList;

public class EmpressThorina extends Card {
    private String ability;
    /**
     * ability = distroys card with maximum health from row
     */
    public EmpressThorina(int mana, int attackDamange,  int health, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "Low Blow";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
