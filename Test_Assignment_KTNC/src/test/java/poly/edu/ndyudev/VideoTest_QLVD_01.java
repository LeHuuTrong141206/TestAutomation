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

public class VideoTest_QLVD_01 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testQLVD_01_ThemMoiVideoThanhCong() throws InterruptedException {
        System.out.println("=== CHẠY TEST CASE: QLVD_01 (Thêm mới Video) ===");

        // =======================================================
        // PHẦN 1: ĐĂNG NHẬP ADMIN
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys("toanpham@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass005");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();
        Thread.sleep(2000);
        System.out.println("-> 1. Đã đăng nhập Admin");

        // =======================================================
        // PHẦN 2: ĐIỀN FORM THÊM MỚI VIDEO
        // =======================================================
        // Dựa vào Servlet của bro, đường dẫn quản lý video là /admin/videos
        driver.get("http://localhost:8080/cinevo-web/admin/videos");
        Thread.sleep(1500);

        // 1. Nhập Tiêu đề
        String videoTitle = "Tom and Jerry đã lừa CrisDevilGamer 1 cú chí mạng";
        driver.findElement(By.name("title")).sendKeys(videoTitle);

        // 2. Nhập Link Video
        driver.findElement(By.name("videoUrl")).sendKeys("https://youtube.com/embed/L6AJQKem_oI");

        // 3. Nhập Link Poster
        driver.findElement(By.name("posterUrl")).sendKeys("https://img.youtube.com/vi/L6AJQKem_oI/maxresdefault.jpg");

        // 4. Bỏ qua mô tả (vì Excel không ghi), Nhập Đạo diễn
        driver.findElement(By.name("director")).sendKeys("Cris Devil Gamer");

        // 5. Nhập Năm phát hành
        driver.findElement(By.name("releaseYear")).sendKeys("2025");

        System.out.println("-> 2. Đã điền các thông tin text");

        // 6. Chọn Danh mục (Hài hước) bằng class Select của Selenium
        WebElement categoryDropdown = driver.findElement(By.name("categoryId"));
        Select selectCategory = new Select(categoryDropdown);
        // Chọn theo đúng chữ hiển thị trên giao diện
        try {
            selectCategory.selectByVisibleText("Hài hước");
            System.out.println("-> 3. Đã chọn danh mục: Hài hước");
        } catch (Exception e) {
            System.out.println("⚠️ Lỗi: Không tìm thấy danh mục 'Hài hước', sẽ chọn danh mục đầu tiên!");
            selectCategory.selectByIndex(1); // Chọn đại thằng đầu tiên nếu Data chưa có "Hài hước"
        }
        Thread.sleep(1000);

        // =======================================================
        // PHẦN 3: LƯU VÀ KIỂM TRA KẾT QUẢ
        // =======================================================
        // Tìm nút Thêm mới (nút submit)
        WebElement btnLuu = driver.findElement(By.xpath("//button[@type='submit' and contains(., 'Thêm mới')]"));

        // Cuộn chuột xuống để thấy nút Lưu (Tránh lỗi ElementClickInterceptedException)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnLuu);
        Thread.sleep(500);
        btnLuu.click();

        System.out.println("-> 4. Đã bấm nút Thêm mới");
        Thread.sleep(2000);

        // Kiểm tra 1: Xem có hiện thông báo "Thêm video thành công!" không
        try {
            WebElement successMsg = driver.findElement(By.cssSelector(".alert.alert-info"));
            String msgText = successMsg.getText().toLowerCase();
            Assert.assertTrue(msgText.contains("thành công"), "Lỗi: Lời nhắn không chứa chữ thành công!");
            System.out.println("-> 5. Hệ thống báo: " + successMsg.getText().trim());
        } catch (Exception e) {
            Assert.fail("❌ FAIL: Không hiển thị thông báo thêm video thành công!");
        }

        // Kiểm tra 2: Video có xuất hiện trong bảng Danh sách không?
        try {
            // Tìm trong cái bảng xem có chứa cái Tiêu đề video vừa thêm không
            WebElement tableBody = driver.findElement(By.tagName("tbody"));
            Assert.assertTrue(tableBody.getText().contains(videoTitle) ||
                            tableBody.getText().contains("Tom and Jerry"),
                    "Lỗi: Không tìm thấy video trong bảng!");

            System.out.println("-> 6. ✅ Video đã xuất hiện trên bảng danh sách.");
            System.out.println("🎉 KẾT QUẢ: TEST CASE PASS!");

        } catch (Exception e) {
            Assert.fail("❌ FAIL: Video báo thêm thành công nhưng không thấy trong danh sách!");
        }
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(3000); // Ngâm xíu cho bro nhìn kết quả
        if (driver != null) {
            driver.quit();
        }
    }
}