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

public class CategoryManagementTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:8080/cinevo-web";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void loginAsAdmin() {
        driver.get(baseUrl + "/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys("admin@gmail.com");
        driver.findElement(By.name("password")).sendKeys("123456");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test
    public void testDeleteCategory_ShouldRemoveFromList() throws InterruptedException {
        loginAsAdmin();

        // Vào màn hình quản lý danh mục
        driver.get(baseUrl + "/cinevo/admin?tab=category");

        // Tạo mới một danh mục để xóa (tránh đụng dữ liệu có thật)
        String categoryName = "DM Selenium Xoa " + System.currentTimeMillis();
        WebElement nameInput = driver.findElement(By.name("name"));
        nameInput.clear();
        nameInput.sendKeys(categoryName);

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        // Click nút xóa ở dòng chứa tên danh mục vừa tạo
        WebElement deleteLink = driver.findElement(
                By.xpath("//tr[td[text()='" + categoryName + "']]//a[contains(@href,'/admin/categories/delete')]")
        );
        deleteLink.click();
        Thread.sleep(2000);

        String pageSource = driver.getPageSource();
        Assert.assertFalse(
                pageSource.contains(categoryName),
                "LỖI: Tên danh mục vẫn còn xuất hiện sau khi xóa!"
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