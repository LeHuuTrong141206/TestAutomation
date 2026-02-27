package poly.edu.ky;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class VideoTest_TTND_04 {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testTTND_04_ChiaSeVideo_EmailSaiDinhDang() {

        System.out.println("=== TC: Share thất bại - Email sai định dạng ===");

        // 1. Login
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.name("email")).sendKeys("minhanh@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass002");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();

        // 2. Mở video
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=video-detail&id=20");

        // 3. Mở modal Share
        By shareBtnLocator = By.xpath("//button[@data-bs-target='#shareModal']");
        wait.until(ExpectedConditions.elementToBeClickable(shareBtnLocator)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shareModal")));

        // 4. Nhập email sai định dạng
        WebElement emailInput =
                driver.findElement(By.name("toEmail"));

        emailInput.clear();
        emailInput.sendKeys("abc123");   // Sai format

        // 5. Click gửi
        driver.findElement(
                By.xpath("//div[@id='shareModal']//button[@type='submit']")
        ).click();

// ✅ Đợi modal vẫn hiển thị (không bị đóng)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shareModal")));

        Assert.assertTrue(
                driver.findElement(By.id("shareModal")).isDisplayed()
        );

        // 6. Verify vẫn còn ở trang video (không redirect)
        wait.until(ExpectedConditions.urlContains("video-detail"));

        Assert.assertTrue(driver.getCurrentUrl().contains("video-detail"),
                "Trang đã redirect dù email sai định dạng!");

        System.out.println("-> PASS: Hệ thống không cho share với email sai định dạng");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}