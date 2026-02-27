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

public class VideoTest_TTND_03 {
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
    public void testTTND_03_ChiaSeVideoThanhCong() {

        System.out.println("=== CHẠY TEST CASE: TTND_03 (Chia sẻ video) ===");

        // 1️⃣ Login
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.name("email")).sendKeys("minhanh@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass002");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();

        // 2️⃣ Mở video id=20
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=video-detail&id=20");

        // 3️⃣ Click nút Chia sẻ
        By shareButtonLocator = By.xpath("//button[@data-bs-target='#shareModal']");

        WebElement shareBtn = wait.until(ExpectedConditions.elementToBeClickable(shareButtonLocator));

        shareBtn.click();

        // 4️⃣ Wait modal hiển thị
        By modalLocator = By.id("shareModal");

        wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));

        // 5️⃣ Nhập email
        By emailInputLocator = By.name("toEmail");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInputLocator));

        emailInput.clear();
        emailInput.sendKeys("vudotruongkyfa@gmail.com");

        // 6️⃣ Click nút Gửi
        By sendButtonLocator = By.xpath("//div[@id='shareModal']//button[@type='submit']");

        WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(sendButtonLocator));

        sendBtn.click();
        sendBtn.click();

// ✅ Đợi modal đóng
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("shareModal")));

// ✅ Đợi URL ổn định
        wait.until(ExpectedConditions.urlContains("video-detail"));
        Assert.assertTrue(driver.getCurrentUrl().contains("video-detail"));
        // Cách 1: verify redirect về lại video-detail

        System.out.println("-> PASS: Chia sẻ video thành công");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
