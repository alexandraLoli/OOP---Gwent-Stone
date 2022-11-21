package main;

import fileio.CardInput;

import java.util.ArrayList;

public final class Deck {

    /**
     * Method for copying cards in our deck
     * @param otherDeck for the deck we want to copy
     * @return the copy of the received deck
     */
    public ArrayList<CardInput> chooseDeck(final ArrayList<CardInput> otherDeck) {
        ArrayList<CardInput> myDeck = new ArrayList<>();
        for (CardInput cardInput : otherDeck) {
            myDeck.add(cardInput);
        }
        return myDeck;
    }

    /**
     * Method used when we give the deck to a player
     * @param player for the player we give the deck to
     * @param deck for the deck we give to the player
     */
    public void setDecks(final Player player, final ArrayList<CardInput> deck) {
        for (CardInput cardInput : deck) {
            switch (cardInput.getName()) {
                case "Sentinel":
                case "Berserker":
                case "Goliath":
                case "Warden":
                case "The Ripper":
                case "Miraj":
                case "The Cursed One":
                case "Disciple":
                    int mana = cardInput.getMana();
                    String description = cardInput.getDescription();
                    ArrayList<String> colors = cardInput.getColors();
                    String name = cardInput.getName();
                    int attackDamage = cardInput.getAttackDamage();
                    int health = cardInput.getHealth();
                    Cards minion = new Cards(mana, attackDamage, health, description, colors, name);
                    player.getDeck().add(minion);
                    break;

                case "Firestorm":
                case "Winterfell":
                case "Heart Hound":
                    mana = cardInput.getMana();
                    description = cardInput.getDescription();
                    colors = cardInput.getColors();
                    name = cardInput.getName();
                    Cards environment = new Cards(mana, description, colors, name);
                    player.getDeck().add(environment);
                    break;

                default: break;
            }
        }
    }
}
