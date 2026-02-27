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

public class LoginLikeVideoTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:8080/cinevo-web";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testLikeVideoWithoutLogin_ShouldRedirectToLogin() throws InterruptedException {
        // Mở trang user (home)
        driver.get(baseUrl + "/cinevo/user");

        // Click vào 1 video bất kỳ (nút play dẫn tới video-detail)
        driver.findElement(By.cssSelector("a[href*='cinevo/user?tab=video-detail']")).click();
        Thread.sleep(2000);

        // Ở trang chi tiết, nút "Yêu thích" cho user chưa login là link sang trang login
        driver.findElement(By.cssSelector("a[href*='cinevo/user?tab=login']")).click();
        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("cinevo/user?tab=login"),
                "LỖI: Chưa đăng nhập mà click Yêu thích không bị chuyển sang trang đăng nhập!"
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