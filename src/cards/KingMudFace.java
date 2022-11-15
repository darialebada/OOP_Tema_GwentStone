package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "description", "colors", "name", "health"})
@JsonIgnoreProperties({ "attackDamage", "ability" })
public class KingMudFace extends Card {
    private String ability;
    /**
     * ability = +1 life for all cards from row
     */
    public KingMudFace(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 30, description, colors, name);
        this.ability = "Earth Born";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }
}
