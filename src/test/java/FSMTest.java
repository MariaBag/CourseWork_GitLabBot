import org.junit.Assert;
import org.junit.Test;

public class FSMTest {
    @Test
    public void testGetStart() {
        FSM fSM = new FSM();
        Assert.assertEquals("start", fSM.getStart().name);
        Assert.assertEquals("", fSM.getStart().message);
    }

    @Test
    public void testChangeStateStart() {
        FSM fSM = new FSM();
        State start = fSM.getStart();
        Assert.assertNull(fSM.changeState(start, "/project"));
        Assert.assertNull(fSM.changeState(start, "/restart"));
        Assert.assertNull(fSM.changeState(start, "/help"));
        Assert.assertNotNull(fSM.changeState(start, "/start"));
    }

    @Test
    public void testChangeStateHelp() {
        FSM fSM = new FSM();
        State start = fSM.getStart();
        State help = fSM.changeState(start, "/start");
        Assert.assertNull(fSM.changeState(help, "/start"));
        Assert.assertNotNull(fSM.changeState(help, "/project"));
        Assert.assertNotNull(fSM.changeState(help, "/restart"));
        Assert.assertNotNull(fSM.changeState(help, "/help"));
    }

    @Test
    public void testChangeStateProject() {
        FSM fSM = new FSM();
        State start = fSM.getStart();
        State help = fSM.changeState(start, "/start");
        State genres = fSM.changeState(help, "/project");
        Assert.assertNotNull(fSM.changeState(genres, "/project"));
        Assert.assertNull(fSM.changeState(genres, "/start"));
        Assert.assertNotNull(fSM.changeState(genres, "/restart"));
        Assert.assertNotNull(fSM.changeState(genres, "/help"));
    }
}
