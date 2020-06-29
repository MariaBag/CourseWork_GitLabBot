import org.junit.Assert;
import org.junit.Test;

public class BotTest {
    @Test
    public void testIsMakingAction() {
        User usr = new User("0");
        String help = "This bot sends you notifications about new Merge Requests in your GitLab project.\r\n" +
                "Available commands:\r\n" +
                "/project — Adds new project which you want to get notifications from.\r\n" +
                "/help — Shows this message.\r\n" +
                "/restart — Restarts the bot.";
        Assert.assertEquals(help, usr.bot.makeAction("/start", usr));
        Assert.assertEquals(help, usr.bot.makeAction("/help", usr));
        Assert.assertEquals(help, usr.bot.makeAction("/restart", usr));
        Assert.assertEquals("Sorry, the input is incorrect.", usr.bot.makeAction("abracadabra", usr));
        Assert.assertEquals("Wrong command", usr.bot.makeAction("/abracadabra", usr));
    }
}
