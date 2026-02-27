package poly.edu.ndyudev;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest_LG_05 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Chờ ngầm định 10s
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testLG_05_DangNhapTaiKhoanKhongTonTai() throws InterruptedException {
        String url = "http://localhost:8080/cinevo-web/cinevo/user?tab=login";
        driver.get(url);
        System.out.println("1. Đã truy cập URL: " + url);
        Thread.sleep(1000);

        WebElement txtEmail = driver.findElement(By.name("email"));
        txtEmail.sendKeys("map@gmail.com");
        System.out.println("2. Đã nhập Email: map@gmail.com");
        Thread.sleep(1000);

        WebElement txtPass = driver.findElement(By.name("password"));
        txtPass.sendKeys("123456");
        System.out.println("3. Đã nhập Mật khẩu: 123456");
        Thread.sleep(1000);

        WebElement btnLogin = driver.findElement(By.xpath("//button[contains(text(), 'Đăng Nhập')]"));
        btnLogin.click();
        System.out.println("4. Đã nhấn nút Đăng Nhập");

        Thread.sleep(2000);

        System.out.println(">>> Đang kiểm tra thông báo lỗi...");

        try {
            WebElement errorMessage = driver.findElement(By.cssSelector(".alert.alert-danger"));
            String actualErrorText = errorMessage.getText().trim();

            System.out.println("=> Thông báo thực tế trên màn hình: " + actualErrorText);

            Assert.assertTrue(actualErrorText.toLowerCase().contains("không tồn tại") || actualErrorText.toLowerCase().contains("sai tài khoản"),
                    "Lỗi: Câu thông báo không khớp với Test Case!");

            System.out.println("✅ TEST CASE PASS: Đúng với kết quả mong muốn!");

        } catch (Exception e) {
            Assert.fail("❌ TEST CASE FAIL: Không thấy hiển thị thông báo lỗi nào cả!");
        }
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {

        Thread.sleep(2000);
        if (driver != null) {
            driver.quit();
        }
        System.out.println("=== KẾT THÚC TEST CASE ===");
    }
}