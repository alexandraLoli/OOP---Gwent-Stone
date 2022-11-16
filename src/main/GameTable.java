package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Objects;

public class GameTable {

    private ArrayList<ArrayList<Cards>> rows = new ArrayList<>(4);

    public GameTable() {
        for (int i = 0; i < 4; i++)
            this.rows.add(new ArrayList<>());
    }

    private int currentPlayer;
    public UsefulClass usefulClass = new UsefulClass();

    //getters

    public ArrayList<ArrayList<Cards>> getRows() {
        return this.rows;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    //setters
    public void changeCurrentPlayer(int index) {
        if (index == 1)
            this.currentPlayer = 2;
        else this.currentPlayer = 1;
    }

    public void setCurrentPlayer(int index) {
        this.currentPlayer = index;
    }

    public String placeCardOnTable(Cards card, Player player) {
        if (card.getName().equals("Firestorm") || card.getName().equals("Winterfell") || card.getName().equals("Heart Hound"))
            return "error_environment";
        else if (card.getMana() > player.getMana())
            return "error_mana";
        else
            switch (card.getName()) {
                case "The Ripper":
                case "Miraj":
                case "Goliath":
                case "Warden": {
                    if (this.rows.get(player.getFrontRow()).size() >= 5)
                        return "error_not_enough_space";
                    else {
                        this.rows.get(player.getFrontRow()).add(card);
                        player.changeMana(card);
                        player.getCardsInHand().remove(card);
                    }
                    break;
                }
                case "Sentinel":
                case "Berserker":
                case "The Cursed One":
                case "Disciple": {
                    if (this.rows.get(player.getBackRow()).size() >= 5)
                        return "error_not_enough_space";
                    else {
                        this.rows.get(player.getBackRow()).add(card);
                        player.changeMana(card);
                        player.getCardsInHand().remove(card);
                    }
                    break;
                }
            }
        return "succes";
    }

    public void unfrozeCards(Player player) {
        for (Cards i : this.rows.get(player.getBackRow()))
            if (i.getFrozen())
                i.setFrozen(false);

        for (Cards i : this.rows.get(player.getFrontRow()))
            if (i.getFrozen())
                i.setFrozen(false);
    }

    public void unuseCards(Player player) {
        for (Cards i : this.rows.get(player.getBackRow()))
            if (i.getUsed())
                i.setUsed(false);

        for (Cards i : this.rows.get(player.getFrontRow()))
            if (i.getUsed())
                i.setUsed(false);
    }

    public ObjectNode getCardsOnTable(ObjectMapper mapper) {
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
                    case "Disciple": {
                        nodes.get(count).put("mana", card.getMana());
                        nodes.get(count).put("attackDamage", card.getAttackDamage());
                        nodes.get(count).put("health", card.getHealth());
                        nodes.get(count).put("description", card.getDescription());
                        ArrayNode colors = mapper.createArrayNode();
                        for (String color : card.getColors())
                            colors.add(color);
                        nodes.get(count).set("colors", colors);
                        nodes.get(count).put("name", card.getName());
                        arrayNode.add(nodes.get(count));
                        count++;
                        break;
                    }
                    case "Firestorm":
                    case "Winterfell":
                    case "Heart Hound": {
                        nodes.get(count).put("mana", card.getMana());
                        nodes.get(count).put("description", card.getDescription());
                        ArrayNode colors = mapper.createArrayNode();
                        for (String color : card.getColors())
                            colors.add(color);
                        nodes.get(count).set("colors", colors);
                        nodes.get(count).put("name", card.getName());
                        arrayNode.add(nodes.get(count));
                        count++;
                        break;
                    }
                }
            }
            output.add(arrayNode);
        }
        objectNode.set("output", output);
        return objectNode;
    }

    public String CardUsesAttack(Player player1, Player player2, int x_attacker, int y_attacker, int x_attacked, int y_attacked) {
        if (x_attacked == player1.getFrontRow() || x_attacked == player1.getBackRow())
            return "error_row";
        else if (y_attacked < this.rows.get(x_attacked).size() && y_attacker < this.rows.get(x_attacker).size()) {
            Cards cardAttacker = this.rows.get(x_attacker).get(y_attacker);
            Cards cardAttacked = this.rows.get(x_attacked).get(y_attacked);
            if (cardAttacker.getUsed())
                return "error_attacker_attacked";
            else if (cardAttacker.getFrozen())
                return "error_attacker_frozen";
            else {
                boolean existsTank = usefulClass.rowHasTank(this.rows.get(player2.getFrontRow()), this.rows.get(player2.getBackRow()));
                if (existsTank) {
                    if (usefulClass.cardIsTank(cardAttacked)) {
                        cardAttacked.attack(cardAttacker);
                        cardAttacker.setUsed(true);
                        if (cardAttacked.getHealth() <= 0)
                            this.rows.get(x_attacked).remove(cardAttacked);
                        return "succes";
                    } else return "error_tank";
                } else {
                    cardAttacked.attack(cardAttacker);
                    cardAttacker.setUsed(true);
                    if (cardAttacked.getHealth() <= 0)
                        this.rows.get(x_attacked).remove(cardAttacked);
                    return "succes";
                }
            }
        } else return "succes";
    }

    public String getCardAtPosition(int x, int y) {
        if (this.rows.get(x).size() < y + 1)
            return "error_not_card";
        else
            return "succes";
    }

    public String cardUsesEffect(Player player1, Player player2, Cards card, int row) {
        if (!card.getName().equals("Firestorm") && !card.getName().equals("Winterfell") && !card.getName().equals("Heart Hound"))
            return "error_not_environment";
        else if (player1.getMana() < card.getMana())
            return "error_not_mana_environment";
        else if (player2.getBackRow() != row && player2.getFrontRow() != row)
            return "error_not_enemy_row";
        else if (card.getName().equals("Heart Hound")) {
            if (row == player2.getBackRow()) {
                if (this.rows.get(player1.getBackRow()).size() < 4) {
                    card.effect(this.rows.get(player2.getBackRow()), this.rows.get(player1.getBackRow()));
                    player1.changeMana(card);
                    player1.getCardsInHand().remove(card);
                    return "succes";
                } else return "error_size";
            } else if (row == player2.getFrontRow()) {
                if (this.rows.get(player1.getFrontRow()).size() < 4) {
                    card.effect(this.rows.get(player2.getFrontRow()), this.rows.get(player1.getFrontRow()));
                    player1.changeMana(card);
                    player1.getCardsInHand().remove(card);
                    return "succes";
                } else return "error_size";
            }
        } else {
            card.effect(this.rows.get(row), null);
            player1.changeMana(card);
            player1.getCardsInHand().remove(card);
            return "succes";
        }
        return "succes";
    }

    public ObjectNode getFrozenCardsOnTable(ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "getFrozenCardsOnTable");
        ArrayNode output = mapper.createArrayNode();
        ArrayList<ObjectNode> nodes = new ArrayList<>();
        int count = 0;
        int nr = 0;
        for (ArrayList<Cards> i : this.rows) {
            for (Cards card : i) {
                if (card.getFrozen() == true) {
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
                        case "Disciple": {
                            nodes.get(count).put("mana", card.getMana());
                            nodes.get(count).put("attackDamage", card.getAttackDamage());
                            nodes.get(count).put("health", card.getHealth());
                            nodes.get(count).put("description", card.getDescription());
                            ArrayNode colors = mapper.createArrayNode();
                            for (String color : card.getColors())
                                colors.add(color);
                            nodes.get(count).set("colors", colors);
                            nodes.get(count).put("name", card.getName());
                            output.add(nodes.get(count));
                            count++;
                            break;
                        }
                        case "Firestorm":
                        case "Winterfell":
                        case "Heart Hound": {
                            nodes.get(count).put("mana", card.getMana());
                            nodes.get(count).put("description", card.getDescription());
                            ArrayNode colors = mapper.createArrayNode();
                            for (String color : card.getColors())
                                colors.add(color);
                            nodes.get(count).set("colors", colors);
                            nodes.get(count).put("name", card.getName());
                            output.add(nodes.get(count));
                            count++;
                            break;
                        }
                    }
                }
            }
        }
        objectNode.set("output", output);
        return objectNode;
    }

    public String cardUsesAbility(Player player1, Player player2, Cards cardAttacker, Cards cardAttacked) {
        if (cardAttacker.getFrozen())
            return "error_attacker_frozen";
        else if (cardAttacker.getUsed())
            return "error_attacker_attacked";
        else {
            switch (cardAttacker.getName()) {
                case "The Ripper":
                case "Miraj":
                case "The Cursed One": {
                    if (usefulClass.existsCardInRow(this.rows.get(player1.getBackRow()), cardAttacked) || usefulClass.existsCardInRow(this.rows.get(player1.getFrontRow()), cardAttacked))
                        return "error_row";
                    else {
                        if (usefulClass.rowHasTank(this.rows.get(player2.getBackRow()), this.rows.get(player2.getFrontRow()))) {
                            if (usefulClass.cardIsTank(cardAttacked)) {
                                cardAttacker.ability(cardAttacked);
                                if (cardAttacked.getHealth() <= 0) {
                                    if (usefulClass.existsCardInRow(this.rows.get(player2.getBackRow()), cardAttacked))
                                        this.rows.get(player2.getBackRow()).remove(cardAttacked);
                                    else this.rows.get(player2.getFrontRow()).remove(cardAttacked);
                                }
                                cardAttacker.setUsed(true);
                                return "succes";
                            } else return "error_tank";
                        } else {
                            cardAttacker.ability(cardAttacked);
                            if (cardAttacked.getHealth() <= 0) {
                                if (usefulClass.existsCardInRow(this.rows.get(player2.getBackRow()), cardAttacked))
                                    this.rows.get(player2.getBackRow()).remove(cardAttacked);
                                else this.rows.get(player2.getFrontRow()).remove(cardAttacked);
                            }
                            cardAttacker.setUsed(true);
                            return "succes";
                        }
                    }
                }
                case "Disciple": {
                    if (usefulClass.existsCardInRow(this.rows.get(player2.getFrontRow()), cardAttacked) || usefulClass.existsCardInRow(this.rows.get(player2.getBackRow()), cardAttacked)) {
                        return "error_not_current_player";
                    } else {
                        cardAttacker.ability(cardAttacked);
                        cardAttacker.setUsed(true);
                        return "succes";
                    }
                }
                default: return "succes";
            }
        }
    }

    public String cardAttackHero(Player player1, Player player2,  Cards card, Hero hero) {
        if(card.getFrozen())
            return "error_attacker_frozen";
        else if (card.getUsed())
            return "error_attacker_attacked";
        else if(usefulClass.rowHasTank(this.rows.get(player2.getFrontRow()), this.rows.get(player2.getBackRow())))
            return "error_tank";
        else {
            hero.setHealth(card.getAttackDamage());
            card.setUsed(true);
            if(hero.getHealth() <= 0)
                return "victory";
            return "succes";
        }
    }
    public String heroUseAbility(int row, Hero hero, Player player1) {
        if(player1.getMana() < hero.getMana())
            return "error_mana_hero";
        else if(hero.getUsed())
            return "error_hero_used";
        else switch (hero.getName()) {
                case "Lord Royce":
                case "Empress Thorina": {
                    if(row == player1.getBackRow() || row == player1.getFrontRow())
                        return "error_row_not_enemy";
                    else {
                        hero.hero_effect(this.rows.get(row));
                        player1.changeMana(hero);
                        return "succes";
                    }
                }
                case "General Kocioraw":
                case "King Mudface": {
                    if(row != player1.getFrontRow() && row != player1.getBackRow())
                        return "error_row_not_ally";
                    else {
                        hero.hero_effect(this.rows.get(row));
                        player1.changeMana(hero);
                        return "succes";
                    }
                }
        }
        return "succes";
    }
}
