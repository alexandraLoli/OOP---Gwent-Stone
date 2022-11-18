package main;

import java.util.ArrayList;

public final class Hero {

    private final int mana;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;
    private int health;
    private boolean used;

    public Hero() {

        description = null;
        mana = 0;
        colors = null;
        name = null;
    }

    public Hero(final int mana,
                final String description,
                final ArrayList<String> colors,
                final String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.health = 30;
    }

    //getters

    public int getMana() {
        return this.mana;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<String> getColors() {
        return this.colors;
    }

    public String getName() {
        return this.name;
    }

    public int getHealth() {
        return this.health;
    }

    public boolean getUsed() {
        return this.used;
    }

    //setters

    void setHealth(final int attackDamage) {
        this.health = this.health - attackDamage;
    }

    void setUsed(final boolean used) {
        this.used = used;
    }

    //action methods

    /**
     * @param row
     */
    public void heroEffect(final ArrayList<Cards> row) {
        switch (this.name) {
            case "Lord Royce":
                int attackMax = -1;
                for (Cards i : row) {
                    if (attackMax < i.getAttackDamage()) {
                        attackMax = i.getAttackDamage();
                    }
                }
                for (Cards i : row) {
                    if (i.getAttackDamage() == attackMax) {
                        i.setFrozen(true);
                        this.used = true;
                        break;
                    }
                }
                break;

            case "Empress Thorina":
                int healthMax = -1;
                for (Cards i : row) {
                    if (healthMax < i.getHealth()) {
                        healthMax = i.getHealth();
                    }
                }
                for (Cards i : row) {
                    if (i.getHealth() == healthMax) {
                        row.remove(i);
                        this.used = true;
                        break;
                    }
                }
                break;

            case "King Mudface":
                for (Cards i : row) {
                    i.setHealth(i.getHealth() + 1);
                }
                this.used = true;
                break;

            case "General Kocioraw":
                for (Cards i : row) {
                    i.setAttackDamage(i.getAttackDamage() + 1);
                }
                this.used = true;
                break;

            default: break;
        }
    }
}
