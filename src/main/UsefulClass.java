package main;

import java.util.ArrayList;

public class UsefulClass {
    public String generateString(String error) {
        //doesn't work??
        switch (error) {
            case "error_environment": {
                return "Cannot place environment card on table.";
            }
            case "error_mana": {
                return "Not enough mana to place card on table.";
            }
            case "error_not_enough_space": {
                return "Cannot place card on table since row is full.";
            }
            case "error_row" : {
                return "Attacked card does not belong to the enemy.";
            }
            case "error_attacker_attacked" : {
                return "Attacker card has already attacked this turn.";
            }
            case "error_attacker_frozen" : {
                return "Attacker card is frozen.";
            }
            case "error_tank": {
                return "Attacked card is not of type 'Tank'.";
            }
            case "error_not_environment": {
                return "Chosen card is not of type environment.";
            }
            case "error_not_mana_environment": {
                return "Not enough mana to use environment card.";
            }
            case "error_not_enemy_row": {
                return "Chosen row does not belong to the enemy.";
            }
            case "error_size": {
                return "Cannot steal enemy card since the player's row is full.";
            }
            case "error_not_current_player": {
                return "Attacked card does not belong to the current player.";
            }
            case "error_mana_hero": {
                return "Not enough mana to use hero's ability.";
            }
            case "error_hero_used": {
                return "Hero has already attacked this turn.";
            }
            case "error_row_not_ally": {
                return "Selected row does not belong to the current player.";
            }
            case "error_row_not_enemy": {
                return "Selected row does not belong to the enemy.";
            }
        }
        return null;
    }

    public boolean existsCardInRow(ArrayList<Cards> row, Cards card) {
        for (Cards copy : row)
            if (copy == card)
                return true;
        return false;
    }

    public boolean rowHasTank(ArrayList<Cards> row1, ArrayList<Cards> row2) {
        for (Cards copy : row1){
            if (copy.getName().equals("Goliath") || copy.getName().equals("Warden"))
                return true;
        }
        for (Cards copy : row2){
            if (copy.getName().equals("Goliath") || copy.getName().equals("Warden"))
                return true;
        }
        return false;
    }

    public boolean cardIsTank(Cards card) {
        if (card.getName().equals("Goliath") || card.getName().equals("Warden"))
            return true;
        return false;
    }
}
