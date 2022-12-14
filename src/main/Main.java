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

        Error error = new Error();

        int gamesPlayed = 0;
        int player1Wins = 0;
        int player2Wins = 0;

        /* The game is on! */
        for (GameInput gameInput : inputData.getGames()) {

            /* Players come and take their sits */
            Player player1 = new Player();
            Player player2 = new Player();
            player1.setRows(2, 2 + 1);
            player2.setRows(1, 0);

            /* We set the table */
            GameTable gameTable = new GameTable();

            /* The  game starts */
            StartGameInput startGameInput = gameInput.getStartGame();
            Deck deck = new Deck();

            /* Each player chooses a deck */
            ArrayList<CardInput> copy = inputData
                    .getPlayerOneDecks()
                    .getDecks()
                    .get(startGameInput
                            .getPlayerOneDeckIdx());
            ArrayList<CardInput> my1Deck = deck.chooseDeck(copy);
            copy = inputData
                    .getPlayerTwoDecks()
                    .getDecks()
                    .get(startGameInput
                            .getPlayerTwoDeckIdx());
            ArrayList<CardInput> my2Deck = deck.chooseDeck(copy);

            /* We shuffle the cards in deck */
            int shuffleSeed = startGameInput.getShuffleSeed();
            Random random1 = new Random(shuffleSeed);
            Random random2 = new Random(shuffleSeed);
            Collections.shuffle(my1Deck, random1);
            Collections.shuffle(my2Deck, random2);

            /* We give the decks to players */
            deck.setDecks(player1, my1Deck);
            deck.setDecks(player2, my2Deck);

            /* We give the hero cards to players */
            player1.setHero(startGameInput.getPlayerOneHero());
            player2.setHero(startGameInput.getPlayerTwoHero());

            /* We choose a player to start the game */
            int startingPlayer = startGameInput.getStartingPlayer();
            gameTable.setCurrentPlayer(startingPlayer);

            /* Now the game starts for real! */
            int newRound = 0;
            int counterMana = 1;

            ArrayList<ObjectNode> objectNode = new ArrayList<>();
            int i = 0;
            final int maxMana = 10;
            final int three = 3;

            for (ActionsInput action : gameInput.getActions()) {

                /* Do we start a new round? */
                if (newRound == 0) {
                    player2.addCardInHand();
                    player1.addCardInHand();

                    if (counterMana <= maxMana) {
                        player1.changeMana(counterMana);
                        player2.changeMana(counterMana);
                        counterMana++;
                    }
                    newRound++;
                }

                /* We check the commands */
                switch (action.getCommand()) {

                    /* Commands for debuging */
                    case "getCardsInHand" :
                    case "getPlayerDeck" :
                    case "getEnvironmentCardsInHand" :
                    case "getPlayerHero" :
                        ObjectNode object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        if (action.getPlayerIdx() == 1) {
                            objectNode.set(i,
                                    player1.command(objectMapper, action.getCommand(), 1));
                        } else {
                            objectNode.set(i,
                                    player2.command(objectMapper, action.getCommand(), 2));
                        }
                        output.add(objectNode.get(i));
                        i++;
                        break;

                    case "getCardsOnTable" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.set(i, gameTable.getCardsOnTable(objectMapper));
                        output.add(objectNode.get(i));
                        i++;
                        break;

                    case "getPlayerTurn" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", gameTable.getCurrentPlayer());
                        output.add(objectNode.get(i));
                        i++;
                        break;

                    case "getCardAtPosition" :
                        String string = gameTable.getCardAtPosition(action.getX(), action.getY());
                        if (string.equals("succes")) {
                            object = objectMapper.createObjectNode();
                            objectNode.add(object);
                            objectNode.get(i).put("command", action.getCommand());
                            objectNode.get(i).put("x", action.getX());
                            objectNode.get(i).put("y", action.getY());
                            ObjectNode myCard = objectMapper.createObjectNode();
                            Cards copyCard = gameTable.getRows()
                                    .get(action.getX())
                                    .get(action.getY());
                            myCard.put("mana", copyCard.getMana());
                            myCard.put("attackDamage", copyCard.getAttackDamage());
                            myCard.put("health", copyCard.getHealth());
                            myCard.put("description", copyCard.getDescription());
                            ArrayNode colors = objectMapper.createArrayNode();
                            for (String color : copyCard.getColors()) {
                                colors.add(color);
                            }
                            myCard.set("colors", colors);
                            myCard.put("name", copyCard.getName());

                            objectNode.get(i).set("output", myCard);
                            output.add(objectNode.get(i));
                            i++;
                        } else {
                            object = objectMapper.createObjectNode();
                            objectNode.add(object);
                            objectNode.get(i).put("command", action.getCommand());
                            objectNode.get(i).put("x", action.getX());
                            objectNode.get(i).put("y", action.getY());
                            objectNode.get(i).put("output", "No card available at that position.");
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;

                    case "getPlayerMana" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", "getPlayerMana");
                        objectNode.get(i).put("playerIdx", action.getPlayerIdx());
                        if (action.getPlayerIdx() == 1) {
                            objectNode.get(i).put("output", player1.getMana());
                        } else {
                            objectNode.get(i).put("output", player2.getMana());
                        }
                        output.add(objectNode.get(i));
                        i++;
                        break;

                    case "getFrozenCardsOnTable" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.set(i, gameTable.getFrozenCardsOnTable(objectMapper));
                        output.add(objectNode.get(i));
                        i++;
                        break;


                    /* Commands for statistics */
                    case "getTotalGamesPlayed" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", gamesPlayed);
                        output.add(objectNode.get(i));
                        i++;
                        break;

                    case "getPlayerOneWins" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", player1Wins);
                        output.add(objectNode.get(i));
                        i++;
                        break;

                    case "getPlayerTwoWins" :
                        object = objectMapper.createObjectNode();
                        objectNode.add(object);
                        objectNode.get(i).put("command", action.getCommand());
                        objectNode.get(i).put("output", player2Wins);
                        output.add(objectNode.get(i));
                        i++;
                        break;


                    /* Commands for actions */
                    case "endPlayerTurn" :
                        if (gameTable.getCurrentPlayer() == 1) {
                            gameTable.unfrozeCards(player1);
                        } else {
                            gameTable.unfrozeCards(player2);
                        }

                        if (gameTable.getCurrentPlayer() == 1) {
                            gameTable.unuseCards(player1);
                        } else {
                            gameTable.unuseCards(player2);
                        }

                        player1.getHero().setUsed(false);
                        player2.getHero().setUsed(false);

                        newRound++;
                        if (newRound == three) {
                            newRound = 0;
                        }
                        gameTable.changeCurrentPlayer(gameTable.getCurrentPlayer());
                        break;

                    case "placeCard" :
                        if (gameTable.getCurrentPlayer() == 1) {
                            if (player1.getCardsInHand().size() > 0
                                    && action.getHandIdx() < player1.getCardsInHand().size()) {
                                String string1;
                                string1 = gameTable.placeCardOnTable(
                                        player1
                                                .getCardsInHand()
                                                .get(action.getHandIdx()),
                                        player1);
                                if (string1.equals("error_environment")
                                        || string1.equals("error_mana")
                                        || string1.equals("error_not_enough_space")) {
                                    ObjectNode o = error.errorPlaceCard(action.getCommand(),
                                            action.getHandIdx(),
                                            string1,
                                            objectMapper);
                                    objectNode.add(o);
                                    output.add(objectNode.get(i));
                                    i++;
                                }
                            }
                        } else {
                            if (player2.getCardsInHand().size() > 0
                                    && action.getHandIdx() < player2.getCardsInHand().size()) {
                                String string1;
                                string1 = gameTable.placeCardOnTable(
                                        player2
                                                .getCardsInHand()
                                                .get(action.getHandIdx()),
                                        player2);
                                if (string1.equals("error_environment")
                                        || string1.equals("error_mana")
                                        || string1.equals("error_not_enough_space")) {
                                    ObjectNode o = error.errorPlaceCard(
                                            action.getCommand(),
                                            action.getHandIdx(),
                                            string1,
                                            objectMapper);
                                    objectNode.add(o);
                                    output.add(objectNode.get(i));
                                    i++;
                                }
                            }
                        }
                        break;

                    case "cardUsesAttack" :
                        String string1;
                        int xAttacker = action.getCardAttacker().getX();
                        int yAttacker = action.getCardAttacker().getY();
                        int xAttacked = action.getCardAttacked().getX();
                        int yAttacked = action.getCardAttacked().getY();

                        if (gameTable.getCurrentPlayer() == 1) {
                            string1 = gameTable.cardUsesAttack(
                                    player1,
                                    player2,
                                    xAttacker,
                                    yAttacker,
                                    xAttacked,
                                    yAttacked);
                        } else {
                            string1 = gameTable.cardUsesAttack(
                                    player2,
                                    player1,
                                    xAttacker,
                                    yAttacker,
                                    xAttacked,
                                    yAttacked);
                        }
                        if (!string1.equals("succes")) {
                            String command = action.getCommand();
                            objectNode.add(error.errorCardUsesAttack(
                                    command,
                                    xAttacker,
                                    yAttacker,
                                    xAttacked,
                                    yAttacked,
                                    string1,
                                    objectMapper));
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;

                    case "cardUsesAbility" :

                        xAttacker = action.getCardAttacker().getX();
                        yAttacker = action.getCardAttacker().getY();
                        xAttacked = action.getCardAttacked().getX();
                        yAttacked = action.getCardAttacked().getY();

                        if (yAttacker < gameTable.getRows().get(xAttacker).size()
                                && yAttacked < gameTable.getRows().get(xAttacked).size()) {
                            String string2;
                            Cards cardAttacker = gameTable.getRows().get(xAttacker).get(yAttacker);
                            Cards cardAttacked = gameTable.getRows().get(xAttacked).get(yAttacked);

                            if (gameTable.getCurrentPlayer() == 1) {
                                string2 = gameTable.cardUsesAbility(player1,
                                        player2,
                                        cardAttacker,
                                        cardAttacked);
                            } else {
                                string2 = gameTable.cardUsesAbility(player2,
                                        player1,
                                        cardAttacker,
                                        cardAttacked);
                            }
                            if (!string2.equals("succes")) {
                                String command = action.getCommand();
                                objectNode.add(error.errorCardUsesAbility(command,
                                        xAttacker,
                                        yAttacker,
                                        xAttacked,
                                        yAttacked,
                                        string2,
                                        objectMapper));
                                output.add(objectNode.get(i));
                                i++;
                            }
                        }
                        break;

                    case "useAttackHero" :
                        xAttacker = action.getCardAttacker().getX();
                        yAttacker = action.getCardAttacker().getY();
                        if (yAttacker < gameTable.getRows().get(xAttacker).size()) {
                            String string2;
                            Cards card = gameTable.getRows().get(xAttacker).get(yAttacker);
                            if (gameTable.getCurrentPlayer() == 1) {
                                string2 = gameTable.cardAttackHero(
                                        player1,
                                        player2,
                                        card,
                                        player2.getHero());
                                if (string2.equals("victory")) {
                                    ObjectNode node = objectMapper.createObjectNode();
                                    node.put("gameEnded", "Player one killed the enemy hero.");
                                    gamesPlayed++;
                                    player1Wins++;
                                    objectNode.add(node);
                                    output.add(objectNode.get(i));
                                    i++;
                                    break;
                                }
                            } else {
                                string2 = gameTable.cardAttackHero(
                                        player2,
                                        player1,
                                        card,
                                        player1.getHero());
                                if (string2.equals("victory")) {
                                    ObjectNode node = objectMapper.createObjectNode();
                                    node.put("gameEnded", "Player two killed the enemy hero.");
                                    gamesPlayed++;
                                    objectNode.add(node);
                                    player2Wins++;
                                    output.add(objectNode.get(i));
                                    i++;
                                    break;
                                }
                            }
                            if (!string2.equals("succes")) {
                                String command = action.getCommand();
                                objectNode.add(error.errorAttackHero(
                                        action.getCommand(),
                                                xAttacker,
                                                yAttacker,
                                                string2,
                                                objectMapper));
                                output.add(objectNode.get(i));
                                i++;
                            }
                        }
                        break;

                    case "useHeroAbility" :
                        int affectedRow = action.getAffectedRow();
                        String string2;
                        if (gameTable.getCurrentPlayer() == 1) {
                            string2 = gameTable.heroUseAbility(
                                    affectedRow,
                                    player1.getHero(),
                                    player1);
                        } else {
                            string2 = gameTable.heroUseAbility(
                                    affectedRow,
                                    player2.getHero(),
                                    player2);
                        }
                        if (!string2.equals("succes")) {
                            String command = action.getCommand();
                            objectNode.add(error.errorHeroAttack(
                                    action.getCommand(),
                                    action.getAffectedRow(),
                                    string2,
                                    objectMapper));
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;

                    case "useEnvironmentCard" :
                        if (gameTable.getCurrentPlayer() == 1) {
                            if (player1.getCardsInHand().size() <= action.getHandIdx()) {
                                break;
                            }
                        } else if (gameTable.getCurrentPlayer() == 2) {
                            if (player2.getCardsInHand().size() <= action.getHandIdx()) {
                                break;
                            }
                        }

                        Cards card;
                        if (gameTable.getCurrentPlayer() == 1) {
                            card = player1.getCardsInHand().get(action.getHandIdx());
                        } else  {
                            card = player2.getCardsInHand().get(action.getHandIdx());
                        }

                        String string3;
                        if (gameTable.getCurrentPlayer() == 1) {
                            string3 = gameTable.cardUsesEffect(
                                    player1,
                                    player2,
                                    card,
                                    action.getAffectedRow());
                        } else {
                            string3 = gameTable.cardUsesEffect(
                                    player2,
                                    player1,
                                    card,
                                    action.getAffectedRow());
                        }
                        if (!string3.equals("succes")) {
                            String command = action.getCommand();
                            objectNode.add(error.errorUsesEnvironmentCard(
                                    command,
                                    action.getHandIdx(),
                                    action.getAffectedRow(),
                                    string3,
                                    objectMapper));
                            output.add(objectNode.get(i));
                            i++;
                        }
                        break;

                    default: break;
                }
            }

        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}