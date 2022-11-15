package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "health", "attackDamage", "ability" })
public class Winterfell extends Card {
    private String ability;
    /**
     * ability = all cards on targeted row frozen for a tour
     */
    public Winterfell(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "frozen";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
