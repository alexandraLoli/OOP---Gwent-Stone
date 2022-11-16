package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);


        ArrayNode output = objectMapper.createArrayNode();


        //TODO add here the entry point to your implementation

        System.out.println("NEW GAAAMEEEE");

        Error error = new Error();

        int gamesPlayed = 0;
        int player1Wins = 0;
        int player2Wins = 0;

        for (GameInput gameInput : inputData.getGames()) {

            Player player1 = new Player();
            Player player2 = new Player();
            player1.setRows(2,3);
            player2.setRows(1, 0);

            GameTable gameTable = new GameTable();

            // the games start
            StartGameInput startGameInput = gameInput.getStartGame();
            Deck deck = new Deck();

            // the players choose a deck
            ArrayList<CardInput> copy = inputData.getPlayerOneDecks().getDecks().get(startGameInput.getPlayerOneDeckIdx());
            ArrayList<CardInput> my1Deck = deck.chooseDeck(copy);
            copy = inputData.getPlayerTwoDecks().getDecks().get(startGameInput.getPlayerTwoDeckIdx());
            ArrayList<CardInput> my2Deck = deck.chooseDeck(copy);


            // we shuffle the cards in deck
            int shuffleSeed = startGameInput.getShuffleSeed();
            Random random1 = new Random(shuffleSeed);
            Random random2 = new Random(shuffleSeed);
            Collections.shuffle(my1Deck, random1);
            Collections.shuffle(my2Deck, random2);

            System.out.println(my1Deck);
            System.out.println(my2Deck);

            // we give the decks to players
            deck.setDecks(player1, my1Deck);
            deck.setDecks(player2, my2Deck);

            // we give the hero to the players
            player1.setHero(startGameInput.getPlayerOneHero());
            player2.setHero(startGameInput.getPlayerTwoHero());

            // we choose a player to start the game
            int startingPlayer = startGameInput.getStartingPlayer();
            gameTable.setCurrentPlayer(startingPlayer);

            // now the game starts for real!
            int newRound = 0;
            int counterMana = 1;

            ArrayList<ObjectNode> objectNode = new ArrayList<>();
            int i = 0;

            for (ActionsInput action : gameInput.getActions()) {

                //do we start a new round?
                if (newRound == 0) {
                    player2.addCardInHand();
                    player1.addCardInHand();

                    if (counterMana <= 10) {
                        player1.changeMana(counterMana);
                        player2.changeMana(counterMana);
                        counterMana++;
                    }
                    newRound ++;
                }
                switch (action.getCommand()) {

                    //commands for debug
                    case "getCardsInHand" :
                    case "getPlayerDeck" :
                    case "getEnvironmentCardsInHand" :
                    case "getPlayerHero" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        if (action.getPlayerIdx() == 1)
                            objectNode.set(i, player1.command(objectMapper, action.getCommand(), 1));
                        else
                            objectNode.set(i, player2.command(objectMapper, action.getCommand(), 2));
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }
                    case "getCardsOnTable" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.set(i, gameTable.getCardsOnTable(objectMapper));
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }
                    case "getPlayerTurn" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", gameTable.getCurrentPlayer());
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }
                    case "getCardAtPosition" : {
                        String string = gameTable.getCardAtPosition(action.getX(), action.getY());
                        if(string.equals("succes")) {
                            ObjectNode object = objectMapper.createObjectNode();
                            objectNode.add(object);
                            objectNode.get(i).put("command", action.getCommand());
                            objectNode.get(i).put("x", action.getX());
                            objectNode.get(i).put("y", action.getY());
                            ObjectNode myCard = objectMapper.createObjectNode();
                            Cards copy_card = gameTable.getRows().get(action.getX()).get(action.getY());
                            myCard.put("mana", copy_card.getMana());
                            myCard.put("attackDamage", copy_card.getAttackDamage());
                            myCard.put("health", copy_card.getHealth());
                            myCard.put("description", copy_card.getDescription());
                            ArrayNode colors = objectMapper.createArrayNode();
                            for(String color : copy_card.getColors())
                                colors.add(color);
                            myCard.set("colors", colors);
                            myCard.put("name", copy_card.getName());

                            objectNode.get(i).set("output", myCard);
                            output.add(objectNode.get(i));
                            i++;
                        }
                        else {
                            ObjectNode object = objectMapper.createObjectNode();
                            objectNode.add(object);
                            objectNode.get(i).put("command", action.getCommand());
                            objectNode.get(i).put("x", action.getX());
                            objectNode.get(i).put("y", action.getY());
                            objectNode.get(i).put("output", "No card available at that position.");
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;
                    }
                    case "getPlayerMana" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", "getPlayerMana");
                        objectNode.get(i).put("playerIdx", action.getPlayerIdx());
                        if(action.getPlayerIdx() == 1)
                            objectNode.get(i).put("output", player1.getMana());
                        else objectNode.get(i).put("output", player2.getMana());
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }
                    case "getFrozenCardsOnTable" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.set(i, gameTable.getFrozenCardsOnTable(objectMapper));
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }

                    //commands for statistics
                    case "getTotalGamesPlayed" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", gamesPlayed);
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }
                    case "getPlayerOneWins" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", player1Wins);
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }
                    case "getPlayerTwoWins" : {
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", player2Wins);
                        output.add(objectNode.get(i));
                        i++;
                        break;
                    }

                    //commands for actions
                    case "endPlayerTurn" : {
                        if (gameTable.getCurrentPlayer() == 1)
                            gameTable.unfrozeCards(player1);
                        else gameTable.unfrozeCards(player2);

                        if(gameTable.getCurrentPlayer() == 1)
                            gameTable.unuseCards(player1);
                        else gameTable.unuseCards(player2);

                        player1.getHero().setUsed(false);
                        player2.getHero().setUsed(false);

                        newRound ++;
                        if(newRound == 3)
                            newRound = 0;
                        gameTable.changeCurrentPlayer(gameTable.getCurrentPlayer());
                        break;
                    }
                    case "placeCard" : {
                        if (gameTable.getCurrentPlayer() == 1) {
                            if (player1.getCardsInHand().size() > 0 && action.getHandIdx() < player1.getCardsInHand().size()) {
                                String string;
                                string = gameTable.placeCardOnTable(player1.getCardsInHand().get(action.getHandIdx()), player1);
                                if (string.equals("error_environment") || string.equals("error_mana") || string.equals("error_not_enough_space")) {
                                    ObjectNode o = error.errorPlaceCard(action.getCommand(), action.getHandIdx(), string, objectMapper);
                                    objectNode.add(o);
                                    output.add(objectNode.get(i));
                                    i++;
                                }
                            }
                        }
                        else {
                            if (player2.getCardsInHand().size() > 0 && action.getHandIdx() < player2.getCardsInHand().size()) {
                                String string;
                                string = gameTable.placeCardOnTable(player2.getCardsInHand().get(action.getHandIdx()), player2);
                                if (string.equals("error_environment") || string.equals("error_mana") || string.equals("error_not_enough_space")) {
                                    ObjectNode o = error.errorPlaceCard(action.getCommand(), action.getHandIdx(), string, objectMapper);
                                    objectNode.add(o);
                                    output.add(objectNode.get(i));
                                    i++;
                                }
                            }
                        }
                        break;
                    }
                    case "cardUsesAttack" : {
                        String string;
                        int x_attacker = action.getCardAttacker().getX();
                        int y_attacker = action.getCardAttacker().getY();
                        int x_attacked = action.getCardAttacked().getX();
                        int y_attacked = action.getCardAttacked().getY();

                        if(gameTable.getCurrentPlayer() == 1)
                            string = gameTable.CardUsesAttack(player1, player2, x_attacker, y_attacker, x_attacked, y_attacked);
                        else
                            string = gameTable.CardUsesAttack(player2, player1, x_attacker, y_attacker, x_attacked, y_attacked);
                        if(!string.equals("succes")) {
                            String command = action.getCommand();
                            objectNode.add(error.errorCardUsesAttack(command, x_attacker, y_attacker, x_attacked, y_attacked, string, objectMapper));
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;
                    }
                    case "cardUsesAbility" : {

                        int x_attacker = action.getCardAttacker().getX();
                        int y_attacker = action.getCardAttacker().getY();
                        int x_attacked = action.getCardAttacked().getX();
                        int y_attacked = action.getCardAttacked().getY();

                        if(y_attacker < gameTable.getRows().get(x_attacker).size() && y_attacked < gameTable.getRows().get(x_attacked).size()) {
                            String string;
                            Cards cardAttacker = gameTable.getRows().get(x_attacker).get(y_attacker);
                            Cards cardAttacked = gameTable.getRows().get(x_attacked).get(y_attacked);

                            if (gameTable.getCurrentPlayer() == 1)
                                string = gameTable.cardUsesAbility(player1, player2, cardAttacker, cardAttacked);
                            else
                                string = gameTable.cardUsesAbility(player2, player1, cardAttacker, cardAttacked);
                            if (!string.equals("succes")) {
                                String command = action.getCommand();
                                objectNode.add(error.errorCardUsesAbility(command, x_attacker, y_attacker, x_attacked, y_attacked, string, objectMapper));
                                output.add(objectNode.get(i));
                                i++;
                            }
                        }
                        break;
                    }
                    case "useAttackHero" : {
                        int x_attacker = action.getCardAttacker().getX();
                        int y_attacker = action.getCardAttacker().getY();
                        if(y_attacker < gameTable.getRows().get(x_attacker).size()) {
                            String string;
                            Cards card = gameTable.getRows().get(x_attacker).get(y_attacker);
                            if(gameTable.getCurrentPlayer() == 1) {
                                string = gameTable.cardAttackHero(player1, player2, card, player2.getHero());
                                if (string.equals("victory")) {
                                    ObjectNode node = objectMapper.createObjectNode();
                                    node.put("gameEnded", "Player one killed the enemy hero.");
                                    gamesPlayed ++;
                                    player1Wins++;
                                    objectNode.add(node);
                                    output.add(objectNode.get(i));
                                    i++;
                                    break;
                                }
                            }
                            else {
                                string = gameTable.cardAttackHero(player2, player1, card, player1.getHero());
                                if (string.equals("victory")) {
                                    ObjectNode node = objectMapper.createObjectNode();
                                    node.put("gameEnded", "Player two killed the enemy hero.");
                                    gamesPlayed ++;
                                    objectNode.add(node);
                                    player2Wins++;
                                    output.add(objectNode.get(i));
                                    i++;
                                    break;
                                }
                            }
                            if(!string.equals("succes")) {
                                String command = action.getCommand();
                                objectNode.add(error.errorAttackHero(action.getCommand(), x_attacker, y_attacker, string, objectMapper));
                                output.add(objectNode.get(i));
                                i++;
                            }
                        }
                        break;
                    }
                    case "useHeroAbility" : {
                        int affectedRow = action.getAffectedRow();
                        String string;
                        if (gameTable.getCurrentPlayer() == 1)
                            string = gameTable.heroUseAbility(affectedRow, player1.getHero(), player1);
                        else string = gameTable.heroUseAbility(affectedRow, player2.getHero(), player2);
                        if(!string.equals("succes")) {
                            String command = action.getCommand();
                            objectNode.add(error.errorHeroAttack(action.getCommand(), action.getAffectedRow(), string, objectMapper));
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;
                    }
                    case "useEnvironmentCard" : {
                        if(gameTable.getCurrentPlayer() == 1) {
                            if (player1.getCardsInHand().size() <= action.getHandIdx())
                                break;
                        }
                        else if(gameTable.getCurrentPlayer() == 2) {
                            if (player2.getCardsInHand().size() <= action.getHandIdx())
                                break;
                        }

                        Cards card;
                        if(gameTable.getCurrentPlayer() == 1)
                            card = player1.getCardsInHand().get(action.getHandIdx());
                        else card = player2.getCardsInHand().get(action.getHandIdx());

                        String string;
                        if(gameTable.getCurrentPlayer() == 1)
                            string = gameTable.cardUsesEffect(player1, player2, card, action.getAffectedRow());
                        else string = gameTable.cardUsesEffect(player2, player1, card, action.getAffectedRow());
                        if(!string.equals("succes")) {
                            String command = action.getCommand();
                            objectNode.add(error.errorUsesEnvironmentCard(command, action.getHandIdx(), action.getAffectedRow(), string, objectMapper));
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;
                    }
                }
            }

        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}