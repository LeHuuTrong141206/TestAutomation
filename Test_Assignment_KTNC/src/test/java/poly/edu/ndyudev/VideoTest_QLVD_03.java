package poly.edu.ndyudev;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class VideoTest_QLVD_03 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testQLVD_03_ThemVideoSaiLinkYoutube() throws InterruptedException {
        System.out.println("=== CHẠY TEST CASE: QLVD_03 (Thêm Video: Sai định dạng link Youtube) ===");

        // =======================================================
        // PHẦN 1: ĐĂNG NHẬP ADMIN
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys("toanpham@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass005");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();
        Thread.sleep(2000);

        // =======================================================
        // PHẦN 2: ĐIỀN FORM THÊM MỚI VIDEO (DATA SAI ĐỊNH DẠNG LINK)
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/admin/videos");
        Thread.sleep(1500);

        // 1. Nhập Tiêu đề
        driver.findElement(By.name("title")).sendKeys("(Lair's Bar #2) CrisDevilGamer siêu phản @PhânTíchGame @dungsenpai @ChangDoran2k");

        // 2. Nhập Link Video (CỐ TÌNH NHẬP SAI ĐỊNH DẠNG: dùng watch?v= thay vì embed/)
        String invalidLink = "https://www.youtube.com/watch?v=FxsA00u4THQ&t=731s";
        driver.findElement(By.name("videoUrl")).sendKeys(invalidLink);
        System.out.println("-> 1. Đã nhập Link video sai định dạng: " + invalidLink);

        // 3. Nhập Link Poster
        driver.findElement(By.name("posterUrl")).sendKeys("https://img.youtube.com/vi/FxsA00u4THQ/maxresdefault.jpg");

        // 4. Nhập Đạo diễn & Năm phát hành
        driver.findElement(By.name("director")).sendKeys("Cris Devil Gamer");
        driver.findElement(By.name("releaseYear")).sendKeys("2025");

        // 5. Chọn Danh mục
        WebElement categoryDropdown = driver.findElement(By.name("categoryId"));
        Select selectCategory = new Select(categoryDropdown);
        try {
            selectCategory.selectByVisibleText("Hài hước");
        } catch (Exception e) {
            selectCategory.selectByIndex(1);
        }
        Thread.sleep(1000);

        // =======================================================
        // PHẦN 3: LƯU VÀ BẮT BUG
        // =======================================================
        WebElement btnLuu = driver.findElement(By.xpath("//button[@type='submit' and contains(., 'Thêm mới')]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnLuu);
        Thread.sleep(500);
        btnLuu.click();
        System.out.println("-> 2. Đã bấm nút Thêm mới");
        Thread.sleep(2000);

        // =======================================================
        // PHẦN 4: KIỂM TRA KẾT QUẢ MONG ĐỢI
        // =======================================================
        // MONG ĐỢI: Báo lỗi "Link Youtube không hợp lệ"
        try {
            WebElement alertBox = driver.findElement(By.cssSelector(".alert"));
            String msgText = alertBox.getText().toLowerCase();

            System.out.println("-> 3. Thông báo từ hệ thống: " + alertBox.getText().trim());

            // NẾU HỆ THỐNG BÁO "THÀNH CÔNG" -> BOT SẼ ĐÁNH FAIL (BẮT BUG)
            if (msgText.contains("thành công")) {
                Assert.fail("❌ BUG (MAJOR): Hệ thống cho phép thêm link Youtube sai định dạng (chứa watch?v=). Đáng lẽ phải báo lỗi chặn lại!");
            } else {
                // NẾU HỆ THỐNG CHẶN LẠI VÀ BÁO LỖI -> TEST PASS
                Assert.assertTrue(msgText.contains("không hợp lệ") || msgText.contains("định dạng") || msgText.contains("lỗi"),
                        "Hệ thống có báo lỗi nhưng câu thông báo không đúng!");
                System.out.println("✅ KẾT QUẢ: TEST CASE PASS (Web đã bắt lỗi thành công)");
            }
        } catch (Exception e) {
            Assert.fail("❌ KẾT QUẢ: Không tìm thấy bất kỳ thông báo lỗi nào trên màn hình!");
        }
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(3000);
        if (driver != null) {
            driver.quit();
        }
    }
}
