package poly.edu;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KTSoDuong {
    SoDuong sd = new SoDuong();

//    Kiểm tra điều kện trong 1 mảng
    @ParameterizedTest
    @ValueSource(ints ={1,5,10,-100,7,5,-45})
    public void testSD(int a){
        assertTrue(sd.soduong(a), a + " là số nguyên lớn hơn 0");
    }
}
