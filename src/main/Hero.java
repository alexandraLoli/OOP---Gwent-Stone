package main;

import java.util.ArrayList;

public class Hero{

    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int health;
    private boolean used;

    public Hero() {

    }

    public Hero(int mana, String description, ArrayList<String> colors, String name) {
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
    public boolean getUsed() {return  this.used; }

    //setters

    void setHealth(int attackDamage) {
        this.health = this.health - attackDamage;
    }

    void setMana(int mana) {
        this.mana = mana;
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
    void setUsed(boolean used) {this.used = used; }

    //action methods

    public void hero_effect(ArrayList<Cards> row) {
        switch (this.name) {
            case "Lord Royce": {
                int attack_max = -1;
                for (Cards i : row) {
                    if (attack_max < i.getAttackDamage()) {
                        attack_max = i.getAttackDamage();
                    }
                }
                for(Cards i : row)
                    if(i.getAttackDamage() == attack_max) {
                        i.setFrozen(true);
                        this.used = true;
                        break;
                    }
                break;
            }
            case "Empress Thorina": {
                int health_max = -1;
                for (Cards i : row) {
                    if (health_max < i.getHealth()) {
                        health_max = i.getHealth();
                    }
                }
                for(Cards i : row) {
                    if (i.getHealth() == health_max) {
                        row.remove(i);
                        this.used = true;
                        break;
                    }
                }
                break;
            }
            case "King Mudface": {
                for (Cards i : row) {
                    i.setHealth(i.getHealth() + 1);
                }
                this.used = true;
                break;
            }
            case "General Kocioraw": {
                for (Cards i : row) {
                    i.setAttackDamage(i.getAttackDamage() + 1);
                }
                this.used = true;
                break;
            }
        }
    }
}
