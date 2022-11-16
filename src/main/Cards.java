package main;

import java.util.ArrayList;

public class Cards {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    private boolean frozen = false;
    private boolean used = false;

    public Cards() {

    }
    public Cards(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    public Cards(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.attackDamage = attackDamage;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    //getters

    public int getMana () {
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

    void setMana(int mana) {
        this.mana = mana;
    }

    void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    void setHealth(int health) {
        this.health = health;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setColors(ArrayList<String> colors) {
        this.colors.addAll(colors);
    }

    void setName(String name) {
        this.name = name;
    }

    void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    void setUsed(boolean used) {
        this.used = used;
    }

    //action methods

    public void ability(Cards minion) {
        switch (this.name) {
            case "The Ripper": {
                minion.setAttackDamage(minion.getAttackDamage() - 2);
                if(minion.getAttackDamage() < 0)
                    minion.setAttackDamage(0);
                break;
            }
            case "Miraj": {
                int copy = minion.getHealth();
                minion.setHealth(this.getHealth());
                this.setHealth(copy);
                break;
            }
            case "The Cursed One": {
                int copy = minion.getAttackDamage();
                minion.setAttackDamage(minion.getHealth());
                minion.setHealth(copy);
                break;
            }
            case "Disciple": {
                minion.setHealth(minion.getHealth() + 2);
                break;
            }
        }
    }
    public void attack(Cards minion) {
        this.health = this.health - minion.attackDamage;
        minion.setUsed(true);
    }

    public void effect(ArrayList<Cards> row, ArrayList<Cards> my_row) {
        switch (this.name) {
            case "Firestorm": {
                for (Cards i : row) {
                    i.setHealth(i.getHealth() - 1);
                }
                int i = 0;
                while(i != row.size()) {
                    if(row.get(i).getHealth() <= 0)
                        row.remove(i);
                    else i++;
                }
                break;
            }
            case "Winterfell": {
                for (Cards i : row) {
                    i.setFrozen(true);
                }
                break;
            }
            case "Heart Hound": {
                int maxHealth = 0;
                for (Cards i : row)
                    if(i.getHealth() > maxHealth) {
                        maxHealth = i.getHealth();
                    }
                for(Cards i : row)
                    if(i.getHealth() == maxHealth) {
                        my_row.add(i);
                        row.remove(i);
                        break;
                    }
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Cards{" +
                " mana: " + this.mana +
                " attackDamage: " + this.attackDamage +
                " health: " + this.health +
                " description: " + this.description +
                " colors: " + this.colors +
                " name: " + this.name +
                " frozen: " + this.frozen +
                " used: " + this.used +
                " } ";
    }
}
