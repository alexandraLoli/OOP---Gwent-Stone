package main;

import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;

public class Deck {

    public ArrayList<CardInput> chooseDeck(ArrayList<CardInput> otherDeck) {
        ArrayList<CardInput> myDeck = new ArrayList<>();
        for (CardInput cardInput : otherDeck) {
            myDeck.add(cardInput);
        }
        return myDeck;
    }
    public void setDecks (Player player, ArrayList<CardInput> deck) {
        for (CardInput cardInput : deck) {
            switch (cardInput.getName()) {
                case "Sentinel", "Berserker", "Goliath", "Warden", "The Ripper", "Miraj", "The Cursed One", "Disciple" -> {
                    int mana = cardInput.getMana();
                    String description = cardInput.getDescription();
                    ArrayList<String> colors = cardInput.getColors();
                    String name = cardInput.getName();
                    int attackDamage = cardInput.getAttackDamage();
                    int health = cardInput.getHealth();
                    Cards minion = new Cards(mana, attackDamage, health, description, colors, name);
                    player.getDeck().add(minion);
                }
                case "Firestorm", "Winterfell", "Heart Hound" -> {
                    int mana = cardInput.getMana();
                    String description = cardInput.getDescription();
                    ArrayList<String> colors = cardInput.getColors();
                    String name = cardInput.getName();
                    Cards environment = new Cards(mana, description, colors, name);
                    player.getDeck().add(environment);
                }
            }
        }
    }
}
