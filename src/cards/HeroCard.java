package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "description", "colors", "name", "health"})
@JsonIgnoreProperties({ "attackDamage", "ability", "type" })
public class HeroCard extends Card {
    public HeroCard(final int mana, final int attackDamange, final int health,
                    final String description, final ArrayList<String> colors,
                    final String name) {
        super(mana, 0, 30, description, colors, name);
        this.setType("Hero");
    }
}
