package poly.edu;

import org.testng.annotations.Test;
import org.testng.Assert;

public class TestVatCalculator {
    @Test
    public void testGetVatOnAmount(){
        VatCalculator calc = new VatCalculator();
        double expected = 10.0;
        Assert.assertEquals(calc.getVatOnAmount(100), expected);
        Assert.assertNotEquals(calc.getVatOnAmount(120), expected);
    }
}
