import org.junit.Assert;
import org.junit.Test;

public class SquareTest {

    @Test
    public void storeAbility(){
        Square testSquare = new Square(3, 7);
        Assert.assertEquals(testSquare.string, 3);
        Assert.assertEquals(testSquare.column, 7);
        Assert.assertFalse(testSquare.isFull);
        testSquare.isFull = true;
        Assert.assertTrue(testSquare.isFull);
    }

    @Test
    public void storeItemAbility(){
        CheckItem toTestSquare = new CheckItem(Color.black,-6, 2);
        CheckItem toTestSquare2 = new CheckItem(Color.white, 0, 2, true);
        Square testSquare = new Square(5, 8);
        Assert.assertNull(testSquare.currentCheck);
        testSquare.currentCheck = toTestSquare;
        Assert.assertEquals(testSquare.currentCheck, toTestSquare);
        testSquare.currentCheck = toTestSquare2;
        Assert.assertEquals(testSquare.currentCheck, toTestSquare2);
    }
}
