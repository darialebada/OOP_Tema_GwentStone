package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "description", "colors", "name", "health"})
@JsonIgnoreProperties({ "attackDamage", "ability" })
public class LordRoyce extends Card {
    private String ability;
    /**
     * ability = frozes the card with maximum health from row
     */
    public LordRoyce(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 30, description, colors, name);
        this.ability = "Sub-Zero";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
