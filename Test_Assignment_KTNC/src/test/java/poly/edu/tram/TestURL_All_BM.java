package poly.edu.tram;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestURL_All_BM {

    WebDriver driver;

    // 1. MỞ TRÌNH DUYỆT
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // ================= TEST: TRUY CẬP TRÁI PHÉP ADMIN (CÓ DELAY) =================
    @Test(groups = {"admin", "security"}, priority = 0)
    public void testUnauthorizedAccessToAdmin() throws InterruptedException {

        // Xóa toàn bộ session để đảm bảo chưa đăng nhập
        driver.manage().deleteAllCookies();

        // Danh sách URL admin cần kiểm tra
        String[] adminUrls = {
                "http://localhost:8080/cinevo-web/cinevo/admin",
                "http://localhost:8080/cinevo-web/cinevo/admin?tab=videos",
                "http://localhost:8080/cinevo-web/cinevo/admin?tab=category",
                "http://localhost:8080/cinevo-web/cinevo/admin?tab=users",
                "http://localhost:8080/cinevo-web/cinevo/admin?tab=reports",
                "http://localhost:8080/cinevo-web/cinevo/admin?tab=dashboard"
        };

        for (String url : adminUrls) {

            System.out.println("Đang kiểm tra truy cập trái phép: " + url);

            driver.get(url);

            // Chờ server xử lý và redirect
            Thread.sleep(3000);

            String currentUrl = driver.getCurrentUrl();

            // Kiểm tra có bị chuyển về login không
            Assert.assertTrue(
                    currentUrl.contains("/cinevo/user?tab=login"),
                    "LỖI: Không bị chuyển về login! URL hiện tại: " + currentUrl
            );

            // Chờ trang login load xong
            Thread.sleep(2000);

            WebElement loginForm = driver.findElement(By.xpath("//form"));
            Assert.assertTrue(loginForm.isDisplayed(), "LỖI: Không hiển thị form đăng nhập!");

            System.out.println("✓ Đã chặn truy cập trái phép thành công\n");

            // Nghỉ giữa các lần test để không spam server
            Thread.sleep(2000);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("====== TEST XONG ======");
    }
}
