import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FSM {
    private final State start = new State("start", "");
    private final State project = new State("project",
            "Enter your project id (your project must be public)");
    private final State help = new State("help",
            "This bot sends you notifications about new Merge Requests in your GitLab project.\r\n" +
                    "Available commands:\r\n" +
                    "/project — Adds new project which you want to get notifications from.\r\n" +
                    "/help — Shows this message.\r\n" +
                    "/restart — Restarts the bot.");
    private final Map<State, Map<String, State>> movingToState = new HashMap<>();

    public FSM() {
        movingToState.put(start, new HashMap<>(){{
            put("/start", help);}});
        movingToState.put(project, new HashMap<>(){{
            put("/restart", help);
            put("/project", project);
            put("/help", help);}});
        movingToState.put(help, new HashMap<>(){{
            put("/restart", help);
            put("/project", project);
            put("/help", help);}});
    }

    public State getStart() {
        return start;
    }

    public ArrayList<String> getNextCommands(State state) {
        return new ArrayList<>(movingToState.get(state).keySet());
    }

    public State changeState(State state, String cmd) {
        return movingToState.get(state).get(cmd);
    }
}
