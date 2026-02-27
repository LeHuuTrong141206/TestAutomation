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

public class VideoTest_TTND_01 {

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
    public void testTTND_01_ThichVideoThanhCong() {

        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.name("email")).sendKeys("minhanh@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass002");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();

        // Wait login hoàn tất (đợi URL đổi hoặc element xuất hiện)
        wait.until(ExpectedConditions.urlContains("cinevo"));

        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=video-detail&id=20");

        By likeButtonLocator = By.xpath("//input[@name='videoId' and @value='20']/following-sibling::button");

        WebElement btnYeuThich = wait.until(ExpectedConditions.elementToBeClickable(likeButtonLocator));

        btnYeuThich.click();

        // Đợi trạng thái nút đổi text
        wait.until(ExpectedConditions.or(
                ExpectedConditions.textToBe(likeButtonLocator, "Đã thích"),
                ExpectedConditions.textToBe(likeButtonLocator, "Yêu thích")
        ));

        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=favorite");

        By videoLocator = By.xpath("//a[contains(@href,'tab=video-detail') and contains(@href,'id=20')]");

        WebElement likedVideo = wait.until(ExpectedConditions.visibilityOfElementLocated(videoLocator));

        Assert.assertTrue(likedVideo.isDisplayed());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}