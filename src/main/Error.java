package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Error {

    private UsefulClass usefulClass = new UsefulClass();

    public UsefulClass getUsefulClass() {
        return this.usefulClass;
    }

    /**
     * @param command
     * @param handIdx
     * @param error
     * @param mapper
     * @return
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
     * @param command
     * @param xEr
     * @param yEr
     * @param xEd
     * @param yEd
     * @param error
     * @param mapper
     * @return
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
     * @param command
     * @param idx
     * @param row
     * @param error
     * @param mapper
     * @return
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
     * @param command
     * @param xEr
     * @param yEr
     * @param xEd
     * @param yEd
     * @param error
     * @param mapper
     * @return
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
     * @param command
     * @param xEr
     * @param yEr
     * @param error
     * @param mapper
     * @return
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
     * @param command
     * @param row
     * @param error
     * @param mapper
     * @return
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
