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

public class RegisterTest_RG_02 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testRG_02_HoTenChuaKyTuDacBiet() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=register");
        Thread.sleep(1000);

        driver.findElement(By.name("fullname")).sendKeys("Trường @n");
        Thread.sleep(500);

        driver.findElement(By.name("email")).sendKeys("an@gmail.com");
        Thread.sleep(500);

        driver.findElement(By.name("password")).sendKeys("123456789");
        Thread.sleep(500);

        driver.findElement(By.name("confirmPassword")).sendKeys("123456789");
        Thread.sleep(500);

        // Bỏ cái dòng translate dài ngoằng đi, thay bằng dòng này:
        driver.findElement(By.xpath("//button[text()='Đăng Ký']")).click();

        try {
            WebElement errorMessage = driver.findElement(By.cssSelector(".alert.alert-danger"));
            String actualErrorText = errorMessage.getText().toLowerCase();
            Assert.assertTrue(actualErrorText.contains("hợp lệ"), "Lỗi: Thông báo không đúng");
        } catch (Exception e) {
            Assert.fail("FAIL: Tạo tài khoản thành công thay vì báo lỗi (BUG: Major)");
        }
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        if (driver != null) {
            driver.quit();
        }
    }
}