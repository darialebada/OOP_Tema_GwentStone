package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "health", "attackDamage", "type" })
public class EnvironmentCard extends Card {
    public EnvironmentCard(final EnvironmentCard environmentCard) {
        this.setMana(environmentCard.getMana());
        this.setAttackDamage(environmentCard.getAttackDamage());
        this.setHealth(environmentCard.getHealth());
        this.setDescription(environmentCard.getDescription());
        this.setColors(environmentCard.getColors());
        this.setName(environmentCard.getName());
    }
    public EnvironmentCard(final int mana, final int attackDamage, final int health,
                           final String description, final ArrayList<String> colors,
                           final String name) {
        super(mana, attackDamage, health, description, colors, name);
        this.setAttackDamage(0);
        this.setHealth(0);
        this.setType("Environment");
    }
}
