package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Error {

    UsefulClass usefulClass = new UsefulClass();
    public ObjectNode errorPlaceCard(String command, int handIdx, String error, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String error_text = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        objectNode.put("handIdx", handIdx);

        objectNode.put("error", error_text);
        return objectNode;
    }

    public ObjectNode errorCardUsesAttack(String command, int x_er, int y_er, int x_ed, int y_ed, String  error, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String error_text = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        ObjectNode attacker = mapper.createObjectNode();
        attacker.put("x", x_er);
        attacker.put("y", y_er);
        objectNode.set("cardAttacker", attacker);

        ObjectNode attacked = mapper.createObjectNode();
        attacked.put("x", x_ed);
        attacked.put("y", y_ed);
        objectNode.set("cardAttacked", attacked);

        objectNode.put("error", error_text);

        return objectNode;
    }

    public ObjectNode errorUsesEnvironmentCard(String command, int idx, int row, String error, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String error_text = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        objectNode.put("handIdx", idx);

        objectNode.put("affectedRow", row);

        objectNode.put("error", error_text);

        return objectNode;
    }

    public ObjectNode errorCardUsesAbility(String command, int x_er, int y_er, int x_ed, int y_ed, String  error, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String error_text = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        ObjectNode attacker = mapper.createObjectNode();
        attacker.put("x", x_er);
        attacker.put("y", y_er);
        objectNode.set("cardAttacker", attacker);

        ObjectNode attacked = mapper.createObjectNode();
        attacked.put("x", x_ed);
        attacked.put("y", y_ed);
        objectNode.set("cardAttacked", attacked);

        objectNode.put("error", error_text);

        return objectNode;
    }

    public ObjectNode errorAttackHero(String command, int x_er, int y_er, String error, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String error_text = this.usefulClass.generateString(error);

        objectNode.put("command", command);

        ObjectNode attacker = mapper.createObjectNode();
        attacker.put("x", x_er);
        attacker.put("y", y_er);
        objectNode.set("cardAttacker", attacker);

        objectNode.put("error", error_text);

        return objectNode;
    }
    public ObjectNode errorHeroAttack(String command, int row, String error, ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        String error_text = this.usefulClass.generateString(error);

        objectNode.put("command", command);
        objectNode.put("affectedRow", row);
        objectNode.put("error", error_text);

        return objectNode;
    }

}
