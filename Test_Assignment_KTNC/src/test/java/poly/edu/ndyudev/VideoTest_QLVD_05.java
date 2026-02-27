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

public class VideoTest_QLVD_05 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testQLVD_05_ThemVideoMoTaQuaDai() throws InterruptedException {
        System.out.println("=== CHẠY TEST CASE: QLVD_05 (Thêm Video: Mô tả > 500 ký tự) ===");

        // =======================================================
        // PHẦN 1: ĐĂNG NHẬP ADMIN
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys("toanpham@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass005");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();
        Thread.sleep(2000);

        // =======================================================
        // PHẦN 2: ĐIỀN FORM (TẠO MÔ TẢ > 500 KÝ TỰ)
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/admin/videos");
        Thread.sleep(1500);

        // 1. Nhập Tiêu đề, Link Video, Link Poster
        driver.findElement(By.name("title")).sendKeys("(Lair's Bar #2) CrisDevilGamer siêu phản @PhânTíchGame @dungsenpai");
        driver.findElement(By.name("videoUrl")).sendKeys("https://youtube.com/embed/FxsA00u4THQ");
        driver.findElement(By.name("posterUrl")).sendKeys("https://i.ytimg.com/vi/FxsA00u4THQ/hq720.jpg");

        // 2. Tạo đoạn mô tả dài 505 ký tự (Dùng StringBuilder cho ngầu)
        StringBuilder longDesc = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            longDesc.append("Mô tả này rất dài. "); // Mỗi câu 19 ký tự * 50 = 950 ký tự
        }
        String stringMoreThan500 = longDesc.toString().substring(0, 505); // Cắt lấy đúng 505 ký tự

        driver.findElement(By.name("description")).sendKeys(stringMoreThan500);
        System.out.println("-> 1. Đã nhập Mô tả siêu dài (" + stringMoreThan500.length() + " ký tự)");

        // 3. Nhập Đạo diễn, Năm, Chọn Danh mục
        driver.findElement(By.name("director")).sendKeys("Cris Devil Gamer");
        driver.findElement(By.name("releaseYear")).sendKeys("2025");

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
        // MONG ĐỢI: Báo lỗi vượt quá ký tự cho phép của mô tả.
        try {
            WebElement alertBox = driver.findElement(By.cssSelector(".alert"));
            String msgText = alertBox.getText().toLowerCase();

            System.out.println("-> 3. Thông báo từ hệ thống: " + alertBox.getText().trim());

            // NẾU BÁO "THÀNH CÔNG" -> BẮT BUG
            if (msgText.contains("thành công")) {
                Assert.fail("❌ BUG (MAJOR): Hệ thống cho phép thêm Mô tả > 500 ký tự mà không hề chặn lại!");
            } else {
                // NẾU CÓ CHẶN LẠI VÀ BÁO LỖI -> TEST PASS
                Assert.assertTrue(msgText.contains("vượt quá") || msgText.contains("ký tự") || msgText.contains("mô tả"),
                        "Hệ thống có báo lỗi nhưng câu thông báo không đúng!");
                System.out.println("✅ KẾT QUẢ: TEST CASE PASS (Web đã chặn thành công dữ liệu sai)");
            }
        } catch (Exception e) {
            // NẾU SERVER SẬP, VĂN LỖI 500
            String pageSource = driver.getPageSource().toLowerCase();
            if (pageSource.contains("exception") || pageSource.contains("error 500") || pageSource.contains("data truncation")) {
                Assert.fail("❌ BUG (CRITICAL - Sập Server): Lỗi Data Truncation! Do Dev không bắt lỗi Form, ráng nhồi > 500 ký tự vào DB nên Database từ chối và làm sập web!");
            } else {
                Assert.fail("❌ KẾT QUẢ: Không tìm thấy bất kỳ thông báo lỗi nào trên màn hình!");
            }
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
