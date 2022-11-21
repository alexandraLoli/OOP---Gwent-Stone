package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public final class Player {

    private ArrayList<Cards> deck = new ArrayList<>();
    private ArrayList<Cards> cardsInHand = new ArrayList<>();
    private Hero hero;
    private int mana;
    private int backRow;
    private int frontRow;

    public Player() {
        this.mana = 0;
    }

    /*
    Getters
     */
    public ArrayList<Cards> getDeck() {
        return this.deck;
    }

    public ArrayList<Cards> getCardsInHand() {
        return this.cardsInHand;
    }

    public Hero getHero() {
        return this.hero;
    }

    public int getMana() {
        return this.mana;
    }

    public int getBackRow() {
        return this.backRow;
    }

    public int getFrontRow() {
        return this.frontRow;
    }

    /*
    Setters
     */

    /**
     * Method for when players come and take their places
     * @param frontRow
     * @param backRow
     */
    public void setRows(final int frontRow, final int backRow) {
        this.backRow = backRow;
        this.frontRow = frontRow;
    }

    /**
     * Method for when we give the hero to a player
     * @param hero
     */
    //setters
    public void setHero(final CardInput hero) {
        int mana = hero.getMana();
        String description = hero.getDescription();
        ArrayList<String> colors = hero.getColors();
        String name = hero.getName();
        this.hero = new Hero(mana, description, colors, name);
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Method for when a player uses a card
     * @param card for the card the player uses
     */
    public void changeMana(final Cards card) {
        this.mana = this.mana - card.getMana();
    }

    /**
     * Method for when player uses hero
     * @param hero
     */
    public void changeMana(final Hero hero) {
        this.mana = this.mana - hero.getMana();
    }

    /**
     * Method for when a new round starts
     * @param mana
     */
    public void changeMana(final int mana) {
        this.mana = this.mana + mana;
    }


    // action methods

    /**
     * Method for when a player takes a card from deck
     */
    public void addCardInHand() {
        if (this.deck.size() != 0) {
            this.cardsInHand.add(this.deck.get(0));
            this.deck.remove(0);
        }
    }

    /**
     * Method for when we need to do debugging for player
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @param command for the action we tried to do
     * @param playerIdk for the current player
     * @return the ObjectNode we have to print
     */
    public ObjectNode command(final ObjectMapper mapper,
                              final String command,
                              final int playerIdk) {
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("command", command);
        outputNode.put("playerIdx", playerIdk);
        ArrayNode output = mapper.createArrayNode();

        switch (command) {
            case "getCardsInHand" -> {
                ArrayList<ObjectNode> nodes = new ArrayList<>();
                int i = 0;
                for (Cards hand : this.cardsInHand) {
                    ObjectNode cards = mapper.createObjectNode();
                    nodes.add(cards);
                    switch (hand.getName()) {
                        case "Sentinel":
                        case "Berserker":
                        case "Goliath":
                        case "Warden":
                        case "The Ripper":
                        case "Miraj":
                        case "The Cursed One":
                        case "Disciple":
                            Cards minion = this.cardsInHand.get(i);
                            nodes.get(i).put("mana", minion.getMana());
                            nodes.get(i).put("attackDamage", minion.getAttackDamage());
                            nodes.get(i).put("health", minion.getHealth());
                            nodes.get(i).put("description", minion.getDescription());
                            ArrayNode colors = mapper.createArrayNode();
                            for (String color : minion.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(i).set("colors", colors);
                            nodes.get(i).put("name", minion.getName());
                            output.add(nodes.get(i));
                            i++;
                            break;

                        case "Firestorm":
                        case "Winterfell":
                        case "Heart Hound":
                            Cards environment = this.cardsInHand.get(i);
                            nodes.get(i).put("mana", environment.getMana());
                            nodes.get(i).put("description", environment.getDescription());
                            colors = mapper.createArrayNode();
                            for (String color : environment.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(i).set("colors", colors);
                            nodes.get(i).put("name", environment.getName());
                            output.add(nodes.get(i));
                            i++;
                            break;

                        default: break;
                    }
                    // cards.removeAll();
                }
                outputNode.set("output", output);
                break;
            }
            case "getPlayerDeck" -> {
                ArrayList<ObjectNode> nodes = new ArrayList<>();
                for (int i = 0; i < this.deck.size(); i++) {
                    ObjectNode cards = mapper.createObjectNode();
                    nodes.add(cards);
                    switch (this.deck.get(i).getName()) {
                        case "Sentinel":
                        case "Berserker":
                        case "Goliath":
                        case "Warden":
                        case "The Ripper":
                        case "Miraj":
                        case "The Cursed One":
                        case "Disciple":
                            Cards minion = this.deck.get(i);
                            nodes.get(i).put("mana", minion.getMana());
                            nodes.get(i).put("attackDamage", minion.getAttackDamage());
                            nodes.get(i).put("health", minion.getHealth());
                            nodes.get(i).put("description", minion.getDescription());
                            ArrayNode colors = mapper.createArrayNode();
                            for (String color : minion.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(i).set("colors", colors);
                            nodes.get(i).put("name", minion.getName());
                            output.add(nodes.get(i));
                            break;

                        case "Firestorm":
                        case "Winterfell":
                        case "Heart Hound":
                            Cards environment = this.deck.get(i);
                            nodes.get(i).put("mana", environment.getMana());
                            nodes.get(i).put("description", environment.getDescription());
                            colors = mapper.createArrayNode();
                            for (String color : environment.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(i).set("colors", colors);
                            nodes.get(i).put("name", environment.getName());
                            output.add(nodes.get(i));
                            break;

                        default: break;
                    }
                    //cards.removeAll();
                }
                outputNode.set("output", output);
                break;
            }
            case "getPlayerHero" -> {
                ObjectNode myHero = mapper.createObjectNode();
                myHero.put("mana", this.hero.getMana());
                myHero.put("description", this.hero.getDescription());
                ArrayNode colors = mapper.createArrayNode();
                for (String color : hero.getColors()) {
                    colors.add(color);
                }
                myHero.set("colors", colors);
                myHero.put("name", this.hero.getName());
                myHero.put("health", this.hero.getHealth());

                outputNode.set("output", myHero);
                break;
            }
            case "getPlayerMana" -> {
                outputNode.put("output", this.mana);
                break;
            }
            case "getEnvironmentCardsInHand" -> {
                ArrayList<ObjectNode> nodes = new ArrayList<>();
                int counter = 0;
                for (int i = 0; i < this.cardsInHand.size(); i++) {
                    ObjectNode cards = mapper.createObjectNode();
                    nodes.add(cards);
                    switch (this.cardsInHand.get(i).getName()) {
                        case "Firestorm", "Winterfell", "Heart Hound" -> {
                            Cards environment = this.cardsInHand.get(i);
                            nodes.get(counter).put("mana", environment.getMana());
                            nodes.get(counter).put("description", environment.getDescription());
                            ArrayNode colors = mapper.createArrayNode();
                            for (String color : environment.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(counter).set("colors", colors);
                            nodes.get(counter).put("name", environment.getName());
                            output.add(nodes.get(counter));
                            counter++;
                            break;
                        }
                        default -> {
                        }
                    }
                }
                outputNode.set("output", output);
                break;
            }
            default -> {
            }
        }
        return outputNode;
    }

}
