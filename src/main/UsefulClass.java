package main;

import java.util.ArrayList;

public final class UsefulClass {

    /**
     * Method to get the message for an error
     * @param error for the error we got
     * @return the message
     */
    public String generateString(final String error) {
        //doesn't work??
        switch (error) {
            case "error_environment":
                return "Cannot place environment card on table.";

            case "error_mana":
                return "Not enough mana to place card on table.";

            case "error_not_enough_space":
                return "Cannot place card on table since row is full.";

            case "error_row" :
                return "Attacked card does not belong to the enemy.";

            case "error_attacker_attacked" :
                return "Attacker card has already attacked this turn.";

            case "error_attacker_frozen" :
                return "Attacker card is frozen.";

            case "error_tank":
                return "Attacked card is not of type 'Tank'.";

            case "error_not_environment":
                return "Chosen card is not of type environment.";

            case "error_not_mana_environment":
                return "Not enough mana to use environment card.";

            case "error_not_enemy_row":
                return "Chosen row does not belong to the enemy.";

            case "error_size":
                return "Cannot steal enemy card since the player's row is full.";

            case "error_not_current_player":
                return "Attacked card does not belong to the current player.";

            case "error_mana_hero":
                return "Not enough mana to use hero's ability.";

            case "error_hero_used":
                return "Hero has already attacked this turn.";

            case "error_row_not_ally":
                return "Selected row does not belong to the current player.";

            case "error_row_not_enemy":
                return "Selected row does not belong to the enemy.";

        }
        return null;
    }

    /**
     * Method that verify if a card is in a row
     * @param row
     * @param card
     * @return
     */
    public boolean existsCardInRow(final ArrayList<Cards> row, final Cards card) {
        for (Cards copy : row) {
            if (copy == card) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that verify if a row has a Tank card
     * @param row1
     * @param row2
     * @return
     */
    public boolean rowHasTank(final ArrayList<Cards> row1,
                              final ArrayList<Cards> row2) {
        for (Cards copy : row1) {
            if (copy.getName().equals("Goliath") || copy.getName().equals("Warden")) {
                return true;
            }
        }
        for (Cards copy : row2) {
            if (copy.getName().equals("Goliath") || copy.getName().equals("Warden")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that verify if a card is Tank
     * @param card
     * @return
     */
    public boolean cardIsTank(final Cards card) {
        if (card.getName().equals("Goliath") || card.getName().equals("Warden")) {
            return true;
        }
        return false;
    }
}
