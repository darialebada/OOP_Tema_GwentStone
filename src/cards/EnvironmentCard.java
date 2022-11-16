package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "health", "attackDamage", "type" })
public class EnvironmentCard extends Card {
    public EnvironmentCard(final int mana, final int attackDamange, final int health,
                           final String description, final ArrayList<String> colors,
                           final String name) {
        super(mana, 0, 0, description, colors, name);
        this.setType("Environment");
    }
}
