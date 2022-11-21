package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Error {

    private UsefulClass usefulClass = new UsefulClass();

    public UsefulClass getUsefulClass() {
        return this.usefulClass;
    }

    /**
     * Method for printing error output
     * @param command for the action we tried to do
     * @param handIdx for the card the player tried to place on table
     * @param error for the error we got
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return the ObjectNode we have to print
     */
    public ObjectNode errorPlaceCard(final String command,
                                     final int handIdx,
                                     final String error,
                                     final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String errorText = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        objectNode.put("handIdx", handIdx);

        objectNode.put("error", errorText);
        return objectNode;
    }

    /**
     * Method for printing error output
     * @param command for the action we tried to do
     * @param xEr for x index of attacker's card
     * @param yEr for y index of attacker's card
     * @param xEd for x index of attacked player's card
     * @param yEd for y index of attacked player's card
     * @param error for the error we got
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return the ObjectNode we have to print
     */
    public ObjectNode errorCardUsesAttack(final String command,
                                          final int xEr,
                                          final int yEr,
                                          final int xEd,
                                          final int yEd,
                                          final String  error,
                                          final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String errorText = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        ObjectNode attacker = mapper.createObjectNode();
        attacker.put("x", xEr);
        attacker.put("y", yEr);
        objectNode.set("cardAttacker", attacker);

        ObjectNode attacked = mapper.createObjectNode();
        attacked.put("x", xEd);
        attacked.put("y", yEd);
        objectNode.set("cardAttacked", attacked);

        objectNode.put("error", errorText);

        return objectNode;
    }

    /**
     * Method for printing error output
     * @param command for the action we tried to do
     * @param idx for the Environment card the playe used
     * @param row for attacked row
     * @param error for the error we got
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return the ObjectNode we have to print
     */
    public ObjectNode errorUsesEnvironmentCard(final String command,
                                               final int idx,
                                               final int row,
                                               final String error,
                                               final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String errorText = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        objectNode.put("handIdx", idx);

        objectNode.put("affectedRow", row);

        objectNode.put("error", errorText);

        return objectNode;
    }

    /**
     * Method for printing error output
     * @param command for the action we tried to do
     * @param xEr
     * @param yEr
     * @param xEd
     * @param yEd
     * @param error for the error we got
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return the ObjectNode we have to print
     */
    public ObjectNode errorCardUsesAbility(final String command,
                                           final int xEr,
                                           final int yEr,
                                           final int xEd,
                                           final int yEd,
                                           final String  error,
                                           final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String errorText = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        ObjectNode attacker = mapper.createObjectNode();
        attacker.put("x", xEr);
        attacker.put("y", yEr);
        objectNode.set("cardAttacker", attacker);

        ObjectNode attacked = mapper.createObjectNode();
        attacked.put("x", xEd);
        attacked.put("y", yEd);
        objectNode.set("cardAttacked", attacked);

        objectNode.put("error", errorText);

        return objectNode;
    }

    /**
     * Method for printing error output
     * @param command for the action we tried to do
     * @param xEr for x index of attacker's card
     * @param yEr for y index of attacker's card
     * @param error for the error we got
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return the ObjectNode we have to print
     */
    public ObjectNode errorAttackHero(final String command,
                                      final int xEr,
                                      final int yEr,
                                      final String error,
                                      final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String errorText = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        ObjectNode attacker = mapper.createObjectNode();
        attacker.put("x", xEr);
        attacker.put("y", yEr);
        objectNode.set("cardAttacker", attacker);

        objectNode.put("error", errorText);

        return objectNode;
    }

    /**
     * Method for printing error output
     * @param command for the action we tried to do
     * @param row for the row the player attacked
     * @param error for the error we got
     * @param mapper for the object mapper we use to create ObjectNodes and ArrayNodes
     * @return the ObjectNode we have to print
     */
    public ObjectNode errorHeroAttack(final String command,
                                      final int row,
                                      final String error,
                                      final ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String errorText = this.usefulClass.generateString(error);

        objectNode.put("command", command);
        objectNode.put("affectedRow", row);
        objectNode.put("error", errorText);

        return objectNode;
    }

}
