package poly.edu.ngữ;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class VideoManagementTest {

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

    private void openVideoForm() {
        driver.get(baseUrl + "/cinevo/admin?tab=videos");
    }

    private void fillVideoForm(String title, String director, String releaseYear) {
        driver.findElement(By.name("title")).clear();
        driver.findElement(By.name("title")).sendKeys(title);

        driver.findElement(By.name("videoUrl")).clear();
        driver.findElement(By.name("videoUrl")).sendKeys("Otp3fV5yd8c");

        driver.findElement(By.name("posterUrl")).clear();
        driver.findElement(By.name("posterUrl"))
                .sendKeys("https://img.youtube.com/vi/Otp3fV5yd8c/maxresdefault.jpg");

        WebElement description = driver.findElement(By.name("description"));
        description.clear();
        description.sendKeys("Mô tả test Selenium");

        WebElement directorInput = driver.findElement(By.name("director"));
        directorInput.clear();
        directorInput.sendKeys(director);

        WebElement yearInput = driver.findElement(By.name("releaseYear"));
        yearInput.clear();
        yearInput.sendKeys(releaseYear);

        Select categorySelect = new Select(driver.findElement(By.name("categoryId")));
        // Bỏ option rỗng, chọn item đầu tiên thực sự
        categorySelect.selectByIndex(1);
    }

    // 40. Thêm video có đạo diễn hơn 100 ký tự
    @Test
    public void testAddVideo_DirectorOver100Chars_ShouldShowError() throws InterruptedException {
        loginAsAdmin();
        openVideoForm();

        String longDirector = "Đạo diễn " + "A".repeat(110);
        fillVideoForm("Video đạo diễn >100 ký tự", longDirector, "2024");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        String source = driver.getPageSource();
        Assert.assertTrue(
                source.contains("Đạo diễn không được vượt quá 100 ký tự"),
                "LỖI: Đạo diễn > 100 ký tự nhưng không hiển thị lỗi!"
        );
    }

    // 41. Thêm video có năm phát hành là số âm
    @Test
    public void testAddVideo_NegativeYear_ShouldShowError() throws InterruptedException {
        loginAsAdmin();
        openVideoForm();

        fillVideoForm("Video năm âm", "Đạo diễn Test", "-2024");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        String source = driver.getPageSource();
        Assert.assertTrue(
                source.contains("Năm phát hành không hợp lệ"),
                "LỖI: Năm âm nhưng không hiển thị lỗi 'Năm phát hành không hợp lệ'!"
        );
    }

    // 42. Thêm video có năm phát hành là số thực
    @Test
    public void testAddVideo_FloatYear_ShouldShowError() throws InterruptedException {
        loginAsAdmin();
        openVideoForm();

        fillVideoForm("Video năm số thực", "Đạo diễn Test", "2024.5");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        String source = driver.getPageSource();
        Assert.assertTrue(
                source.contains("Năm phát hành không hợp lệ"),
                "LỖI: Năm là số thực nhưng không hiển thị lỗi 'Năm phát hành không hợp lệ'!"
        );
    }

    // 43. Thêm video có năm phát hành lớn hơn 2100
    @Test
    public void testAddVideo_YearOver2100_ShouldShowError() throws InterruptedException {
        loginAsAdmin();
        openVideoForm();

        fillVideoForm("Video năm > 2100", "Đạo diễn Test", "2101");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        String source = driver.getPageSource();
        Assert.assertTrue(
                source.contains("Năm phát hành không hợp lệ"),
                "LỖI: Năm > 2100 nhưng không hiển thị lỗi 'Năm phát hành không hợp lệ'!"
        );
    }

    // 44. Chỉnh sửa thông tin của một video
    @Test
    public void testEditVideoInformation_ShouldUpdateSuccessfully() throws InterruptedException {
        loginAsAdmin();
        openVideoForm();

        // Tạo trước 1 video để sửa
        String originalTitle = "Video để sửa " + System.currentTimeMillis();
        fillVideoForm(originalTitle, "Đạo diễn Gốc", "2024");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        // Click nút Edit của video vừa tạo
        WebElement editLink = driver.findElement(
                By.xpath("//tr[td[contains(.,'" + originalTitle + "')]]//a[contains(@href,'/admin/videos/edit')]")
        );
        editLink.click();
        Thread.sleep(2000);

        // Đổi tiêu đề
        String newTitle = originalTitle + " - Đã sửa";
        WebElement titleInput = driver.findElement(By.name("title"));
        titleInput.clear();
        titleInput.sendKeys(newTitle);

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        String source = driver.getPageSource();
        Assert.assertTrue(
                source.contains(newTitle),
                "LỖI: Không thấy tiêu đề mới sau khi cập nhật video!"
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