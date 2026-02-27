package poly.edu.ngữ;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class RegisterEmailValidationTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:8080/cinevo-web";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testRegister_WithDoubleAtEmail_ShouldShowError() throws InterruptedException {
        driver.get(baseUrl + "/cinevo/user?tab=register");

        driver.findElement(By.name("fullname")).sendKeys("Test User");
        driver.findElement(By.name("email")).sendKeys("test@@gmail.com");
        driver.findElement(By.name("password")).sendKeys("123456");
        driver.findElement(By.name("confirmPassword")).sendKeys("123456");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        String pageSource = driver.getPageSource();
        Assert.assertTrue(
                pageSource.contains("Email không hợp lệ"),
                "LỖI: Email có 2 dấu @ nhưng không hiển thị thông báo 'Email không hợp lệ'!"
        );
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        capture(result);
        if (driver != null) {
            driver.quit();
        }
    }

    private void capture(ITestResult result) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            String status = result.isSuccess() ? "PASS" : "FAIL";
            File dest = new File("./screenshots/" + result.getName() + "_" + status + ".png");
            dest.getParentFile().mkdirs();
            FileHandler.copy(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}