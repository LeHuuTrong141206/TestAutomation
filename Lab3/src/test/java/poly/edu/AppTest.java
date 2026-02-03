package poly.edu;

import org.junit.jupiter.api.*;


public class AppTest
{
    @BeforeAll // Chạy một lần duy nhất trước tất cả các bài kiểm thử
     static void initAll() {
        System.out.println("@BeforeAll - Run before all methods once");
    }

    @BeforeEach // Chạy trước mỗi phương thức test
    void init() {
        System.out.println("@BeforeEach - Run before each test methods");
    }

    @Test
    @DisplayName("First test") // Đặt tên hiển thị cho test case
    void testMethod1() {
        System.out.println("Test method 1");
    }

    @Test
    @Disabled // Vô hiệu hóa test case này (không chạy)
    void testMethod2() {
        System.out.println("Test method 2");
    }

    @Test
    void testMethod3() {
        System.out.println("Test method 3");
    }

    @AfterEach // Chạy sau mỗi phương thức test
    void tearDown() {
        System.out.println("@AfterEach - Run after each test methods");
    }

    @AfterAll // Chạy một lần duy nhất sau khi tất cả các test đã hoàn tất
    static void tearDownAll() {
        System.out.println("@AfterAll - Run after all test methods once");
    }
}
