import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

public class Bot {
    private final FSM fSM = new FSM();

    public Bot() {}

    public String makeAction(String cmd, User user) {
        if (cmd.startsWith("/")) {
            return makeCommand(cmd, user);
        } else {
            return setParameters(cmd, user);
        }
    }

    private String makeCommand(String cmd, User user) {
        State next = fSM.changeState(user.currentState, cmd);
        if (next == null) {
            return "Wrong command";
        } else {
            user.currentState = next;
            if (cmd.equals("/restart")) {
                user.projects = new HashSet<>();
            }
        }
        return next.message;
    }

    public String setParameters(String parameter, User user) {
        if (user.currentState.name.equals("project") && isProject(parameter)) {
            addNewProjectId(user, parameter);
            return "New project id added.";
        } else {
            return "Sorry, the input is incorrect.";
        }
    }

    private boolean isProject(String projectId) {
        try {
            String url = String.format("https://gitlab.com/api/v4/projects/%s", projectId);
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void addNewProjectId(User user, String projectId) {
        user.projects.add(projectId);
    }

    public List<String> getNextCommands(User user){
        return fSM.getNextCommands(user.currentState);
    }

    public State getStart() {
        return fSM.getStart();
    }
}