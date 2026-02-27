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

public class VideoTest_TTND_05 {
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
    public void testTTND_05_DangXuatThanhCong() {

        System.out.println("=== CHẠY TEST CASE: TTND_05 (Đăng xuất) ===");

        // 1️⃣ Login
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.name("email")).sendKeys("minhanh@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass002");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();

        // 2️⃣ Đợi login hoàn tất (fullname hoặc icon user xuất hiện)
        By userDropdownLocator =
                By.xpath("//a[contains(@class,'dropdown-toggle') and .//i[contains(@class,'fa-user')]]");

        wait.until(ExpectedConditions.visibilityOfElementLocated(userDropdownLocator));

        // 3️⃣ Click mở dropdown
        driver.findElement(userDropdownLocator).click();

        // 4️⃣ Click Đăng xuất
        By logoutLocator =
                By.xpath("//a[contains(@href,'/auth/logout')]");

        wait.until(ExpectedConditions.elementToBeClickable(logoutLocator)).click();

        // 5️⃣ Đợi hệ thống xử lý logout (URL đổi hoặc reload)
        wait.until(ExpectedConditions.urlContains("cinevo"));

        // 6️⃣ Verify menu đã trở về trạng thái chưa đăng nhập
        driver.findElement(userDropdownLocator).click();

        By loginLocator =
                By.xpath("//a[contains(@href,'tab=login')]");

        WebElement loginMenu =
                wait.until(ExpectedConditions.visibilityOfElementLocated(loginLocator));

        Assert.assertTrue(loginMenu.isDisplayed(),
                "Không hiển thị menu Đăng nhập sau khi logout!");

        // 7️⃣ Verify không còn link logout
        boolean isLogoutStillVisible =
                driver.findElements(By.xpath("//a[contains(@href,'/auth/logout')]")).size() > 0;

        Assert.assertFalse(isLogoutStillVisible,
                "Logout vẫn còn hiển thị sau khi đăng xuất!");

        System.out.println("-> PASS: Đăng xuất thành công");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
