import java.util.HashSet;

public class User {
    public String id;
    public Bot bot;
    public State currentState;
    public HashSet<String> projects;

    public User(String userId) {
        id = userId;
        bot = new Bot();
        currentState = bot.getStart();
        projects = new HashSet<>();
    }
}