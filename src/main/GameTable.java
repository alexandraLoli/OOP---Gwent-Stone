package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class GameTable {

    private final int length = 4;
    private ArrayList<ArrayList<Cards>> rows = new ArrayList<>(length);

    public GameTable() {
        for (int i = 0; i < length; i++) {
            this.rows.add(new ArrayList<>());
        }
    }

    private int currentPlayer;
    private UsefulClass usefulClass = new UsefulClass();

    /*
    Getters
     */

    public UsefulClass getUsefulClass() {
        return this.usefulClass;
    }
    public ArrayList<ArrayList<Cards>> getRows() {
        return this.rows;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Method used when it's other's player's turn
     * @param index for current player
     */
    //setters
    public void changeCurrentPlayer(final int index) {
        if (index == 1) {
            this.currentPlayer = 2;
        } else {
            this.currentPlayer = 1;
        }
    }

    public void setCurrentPlayer(final int index) {
        this.currentPlayer = index;
    }

    /**
     * Method used when the player places a card on table
     * @param card for the card the player want to place
     * @param player for the player who places the card
     * @return error or succes message
     */
    public String placeCardOnTable(final Cards card, final Player player) {
        if (card.getName().equals("Firestorm")
                || card.getName().equals("Winterfell")
                || card.getName().equals("Heart Hound")) {
            return "error_environment";
        } else if (card.getMana() > player.getMana()) {
            return "error_mana";
        } else {
            switch (card.getName()) {
                case "The Ripper":
                case "Miraj":
                case "Goliath":
                case "Warden":
                    final int five = 5;
                    if (this.rows.get(player.getFrontRow()).size() >= five) {
                        return "error_not_enough_space";
                    } else {
                        this.rows.get(player.getFrontRow()).add(card);
                        player.changeMana(card);
                        player.getCardsInHand().remove(card);
                    }
                    break;

                case "Sentinel":
                case "Berserker":
                case "The Cursed One":
                case "Disciple":
                    final int a = 5;
                    if (this.rows.get(player.getBackRow()).size() >= a) {
                        return "error_not_enough_space";
                    } else {
                        this.rows.get(player.getBackRow()).add(card);
                        player.changeMana(card);
                        player.getCardsInHand().remove(card);
                    }
                    break;

                default: break;
            }
        }
        return "succes";
    }

    /**
     * @param player for the player whose turn ended
     */
    public void unfrozeCards(final Player player) {
        for (Cards i : this.rows.get(player.getBackRow())) {
            if (i.getFrozen()) {
                i.setFrozen(false);
            }
        }

        for (Cards i : this.rows.get(player.getFrontRow())) {
            if (i.getFrozen()) {
                i.setFrozen(false);
            }
        }
    }

    /**
     * @param player for the player whose turn ended
     */
    public void unuseCards(final Player player) {
        for (Cards i : this.rows.get(player.getBackRow())) {
            if (i.getUsed()) {
                i.setUsed(false);
            }
        }

        for (Cards i : this.rows.get(player.getFrontRow())) {
            if (i.getUsed()) {
                i.setUsed(false);
            }
        }
    }

    /**
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return error or succes message
     */
    public ObjectNode getCardsOnTable(final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getCardsOnTable");
        ArrayNode output = mapper.createArrayNode();
        ArrayList<ObjectNode> nodes = new ArrayList<>();
        int count = 0;
        int nr = 0;
        for (ArrayList<Cards> i : this.rows) {
            ArrayNode arrayNode = mapper.createArrayNode();
            for (Cards card : i) {
                ObjectNode cards = mapper.createObjectNode();
                nodes.add(cards);
                switch (card.getName()) {
                    case "Sentinel":
                    case "Berserker":
                    case "Goliath":
                    case "Warden":
                    case "The Ripper":
                    case "Miraj":
                    case "The Cursed One":
                    case "Disciple":
                        nodes.get(count).put("mana", card.getMana());
                        nodes.get(count).put("attackDamage", card.getAttackDamage());
                        nodes.get(count).put("health", card.getHealth());
                        nodes.get(count).put("description", card.getDescription());
                        ArrayNode colors = mapper.createArrayNode();
                        for (String color : card.getColors()) {
                            colors.add(color);
                        }
                        nodes.get(count).set("colors", colors);
                        nodes.get(count).put("name", card.getName());
                        arrayNode.add(nodes.get(count));
                        count++;
                        break;

                    case "Firestorm":
                    case "Winterfell":
                    case "Heart Hound":
                        nodes.get(count).put("mana", card.getMana());
                        nodes.get(count).put("description", card.getDescription());
                        colors = mapper.createArrayNode();
                        for (String color : card.getColors()) {
                            colors.add(color);
                        }
                        nodes.get(count).set("colors", colors);
                        nodes.get(count).put("name", card.getName());
                        arrayNode.add(nodes.get(count));
                        count++;
                        break;

                    default: break;
                }
            }
            output.add(arrayNode);
        }
        objectNode.set("output", output);
        return objectNode;
    }

    /**
     * @param player1 for the attacker
     * @param player2 for the attacked
     * @param xAttacker for x index of attacker's card
     * @param yAttacker for y index of attacker's card
     * @param xAttacked for x index of attacked player's card
     * @param yAttacked for y index of attacked player's card
     * @return error or succes message
     */
    public String cardUsesAttack(final Player player1,
                                 final Player player2,
                                 final int xAttacker,
                                 final int yAttacker,
                                 final int xAttacked,
                                 final int yAttacked) {
        if (xAttacked == player1.getFrontRow()
                || xAttacked == player1.getBackRow()) {
            return "error_row";
        } else if (yAttacked < this.rows.get(xAttacked).size()
                && yAttacker < this.rows.get(xAttacker).size()) {
            Cards cardAttacker = this.rows.get(xAttacker).get(yAttacker);
            Cards cardAttacked = this.rows.get(xAttacked).get(yAttacked);
            if (cardAttacker.getUsed()) {
                return "error_attacker_attacked";
            } else if (cardAttacker.getFrozen()) {
                return "error_attacker_frozen";
            } else {
                boolean existsTank = usefulClass.rowHasTank(
                        this.rows.get(player2.getFrontRow()),
                        this.rows.get(player2.getBackRow()));
                if (existsTank) {
                    if (usefulClass.cardIsTank(cardAttacked)) {
                        cardAttacked.attack(cardAttacker);
                        cardAttacker.setUsed(true);
                        if (cardAttacked.getHealth() <= 0) {
                            this.rows.get(xAttacked).remove(cardAttacked);
                        }
                        return "succes";
                    } else {
                        return "error_tank";
                    }
                } else {
                    cardAttacked.attack(cardAttacker);
                    cardAttacker.setUsed(true);
                    if (cardAttacked.getHealth() <= 0) {
                        this.rows.get(xAttacked).remove(cardAttacked);
                    }
                    return "succes";
                }
            }
        } else {
            return "succes";
        }
    }

    /**
     * @param x for x index of card
     * @param y for y index of card
     * @return error or succes message
     */
    public String getCardAtPosition(final int x, final int y) {
        if (this.rows.get(x).size() < y + 1) {
            return "error_not_card";
        } else {
            return "succes";
        }
    }

    /**
     * @param player1 for attacker player
     * @param player2 for attacked player
     * @param card for card which attacks
     * @param row for the attacked row
     * @return error or succes message
     */
    public String cardUsesEffect(final Player player1,
                                 final Player player2,
                                 final Cards card,
                                 final int row) {
        if (!card.getName().equals("Firestorm")
                && !card.getName().equals("Winterfell")
                && !card.getName().equals("Heart Hound")) {
            return "error_not_environment";
        } else if (player1.getMana() < card.getMana()) {
            return "error_not_mana_environment";
        } else if (player2.getBackRow() != row && player2.getFrontRow() != row) {
            return "error_not_enemy_row";
        } else if (card.getName().equals("Heart Hound")) {
            final int four = 4;
            if (row == player2.getBackRow()) {
                if (this.rows.get(player1.getBackRow()).size() < four) {
                    card.effect(this.rows.get(player2.getBackRow()),
                            this.rows.get(player1.getBackRow()));
                    player1.changeMana(card);
                    player1.getCardsInHand().remove(card);
                    return "succes";
                } else {
                    return "error_size";
                }
            } else if (row == player2.getFrontRow()) {
                if (this.rows.get(player1.getFrontRow()).size() < four) {
                    card.effect(this.rows.get(player2.getFrontRow()),
                            this.rows.get(player1.getFrontRow()));
                    player1.changeMana(card);
                    player1.getCardsInHand().remove(card);
                    return "succes";
                } else {
                    return "error_size";
                }
            }
        } else {
            card.effect(this.rows.get(row), null);
            player1.changeMana(card);
            player1.getCardsInHand().remove(card);
            return "succes";
        }
        return "succes";
    }

    /**
     * Method which gets the frozen Cards on Table
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return error or succes message
     */
    public ObjectNode getFrozenCardsOnTable(final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getFrozenCardsOnTable");
        ArrayNode output = mapper.createArrayNode();
        ArrayList<ObjectNode> nodes = new ArrayList<>();
        int count = 0;
        int nr = 0;
        for (ArrayList<Cards> i : this.rows) {
            for (Cards card : i) {
                if (card.getFrozen()) {
                    ObjectNode cards = mapper.createObjectNode();
                    nodes.add(cards);
                    switch (card.getName()) {
                        case "Sentinel":
                        case "Berserker":
                        case "Goliath":
                        case "Warden":
                        case "The Ripper":
                        case "Miraj":
                        case "The Cursed One":
                        case "Disciple":
                            nodes.get(count).put("mana", card.getMana());
                            nodes.get(count).put("attackDamage", card.getAttackDamage());
                            nodes.get(count).put("health", card.getHealth());
                            nodes.get(count).put("description", card.getDescription());
                            ArrayNode colors = mapper.createArrayNode();
                            for (String color : card.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(count).set("colors", colors);
                            nodes.get(count).put("name", card.getName());
                            output.add(nodes.get(count));
                            count++;
                            break;

                        case "Firestorm":
                        case "Winterfell":
                        case "Heart Hound":
                            nodes.get(count).put("mana", card.getMana());
                            nodes.get(count).put("description", card.getDescription());
                            colors = mapper.createArrayNode();
                            for (String color : card.getColors()) {
                                colors.add(color);
                            }
                            nodes.get(count).set("colors", colors);
                            nodes.get(count).put("name", card.getName());
                            output.add(nodes.get(count));
                            count++;
                            break;

                        default: break;
                    }
                }
            }
        }
        objectNode.set("output", output);
        return objectNode;
    }

    /**
     * @param player1 for attacker
     * @param player2 for attacker player
     * @param cardAttacker
     * @param cardAttacked
     * @return error or succes message
     */
    public String cardUsesAbility(final Player player1,
                                  final Player player2,
                                  final Cards cardAttacker, final
                                  Cards cardAttacked) {
        if (cardAttacker.getFrozen()) {
            return "error_attacker_frozen";
        } else if (cardAttacker.getUsed()) {
            return "error_attacker_attacked";
        } else {
            switch (cardAttacker.getName()) {
                case "The Ripper":
                case "Miraj":
                case "The Cursed One":
                    if (usefulClass.existsCardInRow(
                            this.rows.get(player1.getBackRow()), cardAttacked)
                            || usefulClass.existsCardInRow(this.rows.get(player1.getFrontRow()),
                                                        cardAttacked)) {
                        return "error_row";
                    } else {
                        if (usefulClass.rowHasTank(
                                this.rows.get(player2.getBackRow()),
                                this.rows.get(player2.getFrontRow()))) {
                            if (usefulClass.cardIsTank(cardAttacked)) {
                                cardAttacker.ability(cardAttacked);
                                if (cardAttacked.getHealth() <= 0) {
                                    if (usefulClass.existsCardInRow(
                                            this.rows.get(player2.getBackRow()), cardAttacked)) {
                                        this.rows.get(player2.getBackRow()).remove(cardAttacked);
                                    } else {
                                        this.rows.get(player2.getFrontRow()).remove(cardAttacked);
                                    }
                                }
                                cardAttacker.setUsed(true);
                                return "succes";
                            } else {
                                return "error_tank";
                            }
                        } else {
                            cardAttacker.ability(cardAttacked);
                            if (cardAttacked.getHealth() <= 0) {
                                if (usefulClass.existsCardInRow(this.rows.get(player2.getBackRow()),
                                                                    cardAttacked)) {
                                    this.rows.get(player2.getBackRow()).remove(cardAttacked);
                                } else {
                                    this.rows.get(player2.getFrontRow()).remove(cardAttacked);
                                }
                            }
                            cardAttacker.setUsed(true);
                            return "succes";
                        }
                    }

                case "Disciple":
                    if (usefulClass.existsCardInRow(this.rows.get(player2.getFrontRow()),
                                                    cardAttacked)
                            || usefulClass.existsCardInRow(this.rows.get(player2.getBackRow()),
                                                                cardAttacked)) {
                        return "error_not_current_player";
                    } else {
                        cardAttacker.ability(cardAttacked);
                        cardAttacker.setUsed(true);
                        return "succes";
                    }

                default: return "succes";
            }
        }
    }

    /**
     * @param player1 for attacker
     * @param player2 for attacked player
     * @param card for attacker card
     * @param hero for attacked hero
     * @return error or succes message
     */
    public String cardAttackHero(final Player player1,
                                 final Player player2,
                                 final Cards card,
                                 final Hero hero) {
        if (card.getFrozen()) {
            return "error_attacker_frozen";
        } else if (card.getUsed()) {
            return "error_attacker_attacked";
        } else if (usefulClass.rowHasTank(this.rows.get(player2.getFrontRow()),
                this.rows.get(player2.getBackRow()))) {
            return "error_tank";
        } else {
            hero.setHealth(card.getAttackDamage());
            card.setUsed(true);
            if (hero.getHealth() <= 0) {
                return "victory";
            }
            return "succes";
        }
    }

    /**
     * @param row for attacked row
     * @param hero for attacker hero
     * @param player1 for the player who has the hero
     * @return error or succes message
     */
    public String heroUseAbility(final int row,
                                 final Hero hero,
                                 final Player player1) {
        if (player1.getMana() < hero.getMana()) {
            return "error_mana_hero";
        } else if (hero.getUsed()) {
            return "error_hero_used";
        } else {
            switch (hero.getName()) {
                case "Lord Royce":
                case "Empress Thorina":
                    if (row == player1.getBackRow() || row == player1.getFrontRow()) {
                        return "error_row_not_enemy";
                    } else {
                        hero.heroEffect(this.rows.get(row));
                        player1.changeMana(hero);
                        return "succes";
                    }

                case "General Kocioraw":
                case "King Mudface":
                    if (row != player1.getFrontRow() && row != player1.getBackRow()) {
                        return "error_row_not_ally";
                    } else {
                        hero.heroEffect(this.rows.get(row));
                        player1.changeMana(hero);
                        return "succes";
                    }
                default: break;
            }
        }
        return "succes";
    }
}
