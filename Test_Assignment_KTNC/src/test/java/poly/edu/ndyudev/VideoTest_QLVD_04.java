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

public class VideoTest_QLVD_04 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testQLVD_04_ThemVideoSaiLinkPoster() throws InterruptedException {
        System.out.println("=== CHẠY TEST CASE: QLVD_04 (Thêm Video: Sai định dạng link Poster) ===");

        // =======================================================
        // PHẦN 1: ĐĂNG NHẬP ADMIN
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys("toanpham@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass005");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();
        Thread.sleep(2000);

        // =======================================================
        // PHẦN 2: ĐIỀN FORM (DATA SAI LINK POSTER)
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/admin/videos");
        Thread.sleep(1500);

        // 1. Nhập Tiêu đề
        driver.findElement(By.name("title")).sendKeys("(Lair's Bar #2) CrisDevilGamer siêu phản @PhânTíchGame @dungsenpai @ChangDoran2k");

        // 2. Nhập Link Video (Lần này link video chuẩn embed)
        driver.findElement(By.name("videoUrl")).sendKeys("https://youtube.com/embed/FxsA00u4THQ");

        // 3. Nhập Link Poster (CỐ TÌNH NHẬP SAI ĐỊNH DẠNG HƯỚNG DẪN)
        String invalidPoster = "https://i.ytimg.com/vi/FxsA00u4THQ/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLDHtvF-NRxLMJ-j97Yc_ZpGRM470A";
        driver.findElement(By.name("posterUrl")).sendKeys(invalidPoster);
        System.out.println("-> 1. Đã nhập Link Poster sai quy tắc: " + invalidPoster);

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
        // MONG ĐỢI: Báo lỗi "Link Poster không hợp lệ"
        try {
            WebElement alertBox = driver.findElement(By.cssSelector(".alert"));
            String msgText = alertBox.getText().toLowerCase();

            System.out.println("-> 3. Thông báo từ hệ thống: " + alertBox.getText().trim());

            // NẾU HỆ THỐNG BÁO "THÀNH CÔNG" -> BOT ĐÁNH FAIL (BẮT BUG)
            if (msgText.contains("thành công")) {
                Assert.fail("❌ BUG (MINOR/MAJOR): Hệ thống cho phép thêm link Poster rác (i.ytimg.com). Đáng lẽ phải bắt buộc user nhập đúng form img.youtube.com/vi/.../maxresdefault.jpg!");
            } else {
                // NẾU HỆ THỐNG CHẶN LẠI VÀ BÁO LỖI -> TEST PASS
                Assert.assertTrue(msgText.contains("hợp lệ") || msgText.contains("poster") || msgText.contains("định dạng"),
                        "Hệ thống có báo lỗi nhưng câu thông báo không đúng!");
                System.out.println("✅ KẾT QUẢ: TEST CASE PASS (Web đã bắt lỗi Link Poster thành công)");
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