import org.junit.Assert;
import org.junit.Test;

public class CheckItemTest {
    @Test
    public void checkAbilityToAddSimple(){
        CheckItem testItem = new CheckItem(Color.white, 1, 2);
        Assert.assertEquals(testItem.checkColor, Color.white);
        Assert.assertEquals(testItem.xPos, 1);
        Assert.assertEquals(testItem.yPos, 2);
    }

    @Test
    public void checkAbilityToAddKing(){
        CheckItem testItem = new CheckItem(Color.black, 0, 10, true);
        Assert.assertEquals(testItem.checkColor, Color.black);
        Assert.assertEquals(testItem.xPos, 0);
        Assert.assertEquals(testItem.yPos, 10);
        Assert.assertTrue(testItem.isKing);
    }

    @Test
    public void checkAbilityToAddNotKing(){
        CheckItem testItem = new CheckItem(Color.white, -5, -7, false);
        Assert.assertEquals(testItem.checkColor, Color.white);
        Assert.assertEquals(testItem.xPos, -5);
        Assert.assertEquals(testItem.yPos, -7);
        Assert.assertFalse(testItem.isKing);
    }
}
