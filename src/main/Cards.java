package main;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class Cards {
    private final int mana;
    private int attackDamage;
    private int health;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;

    private boolean frozen = false;
    private boolean used = false;

    public Cards() {
        mana = 0;
        description = null;
        colors = null;
        name = null;
    }
    public Cards(final int mana,
                 final String description,
                 final ArrayList<String> colors,
                 final String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    public Cards(final int mana,
                 final int attackDamage,
                 final  int health,
                 final String description,
                 final ArrayList<String> colors,
                 final String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    //getters

    public int getMana() {
        return this.mana;
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }

    public int getHealth() {
        return this.health;
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

    public boolean getFrozen() {
        return this.frozen;
    }

    public boolean getUsed() {
        return this.used;
    }

    //setters

    void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    void setHealth(final int health) {
        this.health = health;
    }

    void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    void setUsed(final boolean used) {
        this.used = used;
    }

    //action methods

    /**
     * @param minion
     */
    public void ability(final Cards minion) {
        switch (this.name) {
            case "The Ripper":
                minion.setAttackDamage(minion.getAttackDamage() - 2);
                if (minion.getAttackDamage() < 0) {
                    minion.setAttackDamage(0);
                }
                break;

            case "Miraj":
                int copy = minion.getHealth();
                minion.setHealth(this.getHealth());
                this.setHealth(copy);
                break;

            case "The Cursed One":
                copy = minion.getAttackDamage();
                minion.setAttackDamage(minion.getHealth());
                minion.setHealth(copy);
                break;

            case "Disciple":
                minion.setHealth(minion.getHealth() + 2);
                break;

            default: break;
        }
    }

    /**
     * When a minion attacks, the attacked one loses life
     * @param minion for Card which attacked
     */
    public void attack(final @NotNull Cards minion) {
        this.health = this.health - minion.attackDamage;
        minion.setUsed(true);
    }

    /**
     * Environment card uses effect
     * @param row for the attacked row
     * @param myRow for the attacker's row
     */
    public void effect(final ArrayList<Cards> row, final ArrayList<Cards> myRow) {
        switch (this.name) {
            case "Firestorm":
                for (Cards i : row) {
                    i.setHealth(i.getHealth() - 1);
                }
                int i = 0;
                while (i != row.size()) {
                    if (row.get(i).getHealth() <= 0) {
                        row.remove(i);
                    } else {
                        i++;
                    }
                }
                break;

            case "Winterfell":
                for (Cards j : row) {
                    j.setFrozen(true);
                }
                break;

            case "Heart Hound":
                int maxHealth = 0;
                for (Cards k : row) {
                    if (k.getHealth() > maxHealth) {
                        maxHealth = k.getHealth();
                    }
                }
                for (Cards l : row) {
                    if (l.getHealth() == maxHealth) {
                        myRow.add(l);
                        row.remove(l);
                        break;
                    }
                }
                break;

            default: break;
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Cards{"
                + " mana: "
                + this.mana
                + " attackDamage: "
                + this.attackDamage
                + " health: "
                + this.health
                + " description: "
                + this.description
                + " colors: "
                + this.colors
                + " name: "
                + this.name
                + " frozen: "
                + this.frozen
                + " used: "
                + this.used
                + " } ";
    }
}
