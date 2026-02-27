package poly.edu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    Calculator car = new Calculator();

//    @Test
//    public void testCalculatorAdd(){
//        assertEquals(10, car.add(7,3)); //Kiểm coi 7 + 3 = 10 không
//        assertNotEquals(20, car.add(10,8)); //Kiểm coi 10 + 8 không phải bằng 20
//        assertTrue(car.add(5,10) == 15); //Kiểm tra điều kiện 5+10=15 là đúng
//        assertFalse(car.add(2,3) == 2); //Kiểm tra điều kiện 2+3=2 là sai
//
//        //assertNull: Kiểm tra đối tượng phải là null
//        //assertNotNull: Kiểm tra đối tượng không được null
//        //assertArrayEquals: Kiểm tra hai mảng phải giống hệt nhau (thứ tự và giá trị)
//        //assertSame & assertNotSame: Kiểm tra cùng một địa chỉ ô nhớ (tham chiếu)
//        //assertThrows: Kiểm tra xem code có văng ra lỗi đúng như mong đợi không
//    }
//
//    @Test
//    public void testCalculatorSubtract(){
//        assertEquals(4, car.subtract(7,3)); //Kiểm coi 7 - 3 = 4 không
//        assertNotEquals(3, car.subtract(10,8)); //Kiểm coi 10 - 8 không phải bằng 3
//        assertTrue(car.subtract(10,5) == 5); //Kiểm tra điều kiện 10-5=5 là đúng
//        assertFalse(car.subtract(3,2) == 2); //Kiểm tra điều kiện 3-2=2 là sai
//    }
//
//    @Test
//    public void testCalculatorMultiply(){
//        assertEquals(21, car.multiply(7,3)); //Kiểm coi 7 * 3 = 21 không
//        assertNotEquals(20, car.multiply(10,8)); //Kiểm coi 10 * 8 không phải bằng 20
//        assertTrue(car.multiply(5,10) == 50); //Kiểm tra điều kiện 5*10=50 là đúng
//        assertFalse(car.multiply(2,3) == 2); //Kiểm tra điều kiện 2*3=2 là sai
//    }
//
//    @Test
//    public void testCalculatorDivide(){
//        assertEquals(5, car.divide(10,2)); //Kiểm coi 10/2 = 5 đúng không
//        assertNotEquals(4, car.divide(10,2)); //Kiểm coi 10 / 2 không phải bằng 4
//        assertTrue(car.divide(100,50) == 2); //Kiểm tra điều kiện 100/50=2 là đúng
//        assertFalse(car.divide(200,25) == 2); //Kiểm tra điều kiện 200/25=2 là sai
//    }
//
//
    @Test
    //Phép toán ko hợp lệ thì dùng ArithmeticException
    public void testCalculatorDivide_ArithmeticException(){
        assertThrows(ArithmeticException.class, () -> {
            car.divide(10, 0);
        }, "Phải lỗi ArithmeticException khi b là 0");
    }

    @Test
    //Cố đổi vắn bản sang số thì dùng NumberFormatException
    public void testNumberFormatException(){
        assertThrows(NumberFormatException.class, () -> {
            car.parseStringToInt("abc");
        }, "Phải lỗi NumberFormatException khi s là chữ");
    }

    @Test
    //Cố gắng gọi hàm mà mang giá trị null thì dùng NullPointerException
    void testNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            car.getStringLength(null);
        }, "Phải lỗi NullPointerException khi chuỗi rỗng");
    }


}
