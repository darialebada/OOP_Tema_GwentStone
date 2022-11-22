package cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import helpers.MagicNumber;

import java.util.ArrayList;

@JsonPropertyOrder({"mana", "description", "colors", "name", "health"})
@JsonIgnoreProperties({ "attackDamage", "type", "ability" })
public final class HeroCard extends Card {
    private boolean ability;
    public HeroCard(final int mana, final int attackDamage, final int health,
                    final String description, final ArrayList<String> colors,
                    final String name) {
        super(mana, attackDamage, health, description, colors, name);
        this.setAttackDamage(0);
        this.setHealth(MagicNumber.THIRTY);
        this.setType("Hero");
        this.ability = false;
    }

    /**
     *
     * @return whether the card used its ability in the current turn
     */
    public boolean isAbility() {
        return ability;
    }

    public void setAbility(final boolean ability) {
        this.ability = ability;
    }
}
