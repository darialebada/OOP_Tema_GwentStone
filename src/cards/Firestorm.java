package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties({ "health", "attackDamage", "ability" })
public class Firestorm extends Card {
    private String ability;
    /**
     * ability = -1 life for all minions on the targeted row
     */
    public Firestorm(int mana, int attackDamange, int health, String description, ArrayList<String> colors, String name) {
        super(mana, 0, 0, description, colors, name);
        this.ability = "-1 life";
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    @Override
    public String toString() {
        return  "{"
                +  "mana="
                + super.getMana()
                +  ", description='"
                + super.getDescription()
                + '\''
                + ", colors="
                + super.getColors()
                + ", name='"
                +  ""
                + super.getName()
                + '\''
                + '}';
    }
}
