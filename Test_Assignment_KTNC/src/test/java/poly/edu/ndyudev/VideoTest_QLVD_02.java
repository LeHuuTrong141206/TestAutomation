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

public class VideoTest_QLVD_02 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testQLVD_02_ThemVideoTieuDeQuaDai() throws InterruptedException {
        System.out.println("=== CHẠY TEST CASE: QLVD_02 (Thêm Video Tiêu đề > 100 ký tự) ===");

        // =======================================================
        // PHẦN 1: ĐĂNG NHẬP ADMIN
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        driver.findElement(By.name("email")).sendKeys("toanpham@gmail.com");
        driver.findElement(By.name("password")).sendKeys("pass005");
        driver.findElement(By.xpath("//button[text()='Đăng Nhập']")).click();
        Thread.sleep(2000);

        // =======================================================
        // PHẦN 2: ĐIỀN FORM THÊM MỚI VIDEO
        // =======================================================
        driver.get("http://localhost:8080/cinevo-web/admin/videos");
        Thread.sleep(1500);

        // 1. Nhập Tiêu đề (> 100 ký tự theo đúng Excel của bro)
        String longTitle = "Tom and Jerry đã lừa CrisDevilGamer 1 cú chí mạng AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        driver.findElement(By.name("title")).sendKeys(longTitle);
        System.out.println("-> 1. Đã nhập Tiêu đề siêu dài (> 100 ký tự)");

        // 2. Nhập các thông tin hợp lệ khác
        driver.findElement(By.name("videoUrl")).sendKeys("https://youtube.com/embed/L6AJQKem_oI");
        driver.findElement(By.name("posterUrl")).sendKeys("https://img.youtube.com/vi/L6AJQKem_oI/maxresdefault.jpg");
        driver.findElement(By.name("director")).sendKeys("Cris Devil Gamer");
        driver.findElement(By.name("releaseYear")).sendKeys("2025");

        // 3. Chọn Danh mục
        WebElement categoryDropdown = driver.findElement(By.name("categoryId"));
        Select selectCategory = new Select(categoryDropdown);
        try {
            selectCategory.selectByVisibleText("Hài hước");
        } catch (Exception e) {
            selectCategory.selectByIndex(1);
        }
        Thread.sleep(1000);

        // =======================================================
        // PHẦN 3: LƯU VÀ KIỂM TRA BẮT BUG
        // =======================================================
        WebElement btnLuu = driver.findElement(By.xpath("//button[@type='submit' and contains(., 'Thêm mới')]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnLuu);
        Thread.sleep(500);
        btnLuu.click();
        System.out.println("-> 2. Đã bấm nút Thêm mới");
        Thread.sleep(2000);

        // KIỂM TRA KẾT QUẢ
        // Theo Test case mong muốn: Phải báo lỗi giới hạn ký tự.
        try {
            // Thử tìm thẻ hiển thị thông báo
            WebElement alertBox = driver.findElement(By.cssSelector(".alert"));
            String msgText = alertBox.getText().toLowerCase();

            System.out.println("-> 3. Thông báo từ hệ thống: " + alertBox.getText().trim());

            // NẾU HỆ THỐNG BÁO THÀNH CÔNG -> TỨC LÀ LỖI BUG BỊ LỌT
            if (msgText.contains("thành công")) {
                Assert.fail("❌ BUG (MAJOR): Hệ thống cho phép thêm video có tiêu đề > 100 ký tự. Thực tế báo 'Thành công' thay vì báo lỗi chặn lại!");
            } else {
                // NẾU HỆ THỐNG CÓ CHẶN LẠI BÁO LỖI KÝ TỰ -> TEST PASS
                Assert.assertTrue(msgText.contains("lỗi") || msgText.contains("ký tự") || msgText.contains("dài"),
                        "Hệ thống có báo lỗi nhưng câu thông báo không đúng!");
                System.out.println("✅ KẾT QUẢ: TEST CASE PASS (Web đã chặn thành công dữ liệu sai)");
            }

        } catch (Exception e) {
            // Nếu web bị sập ra trang trắng (Error 500) do DB không chứa nổi chuỗi dài
            if (driver.getPageSource().contains("Exception") || driver.getPageSource().contains("Error 500")) {
                Assert.fail("❌ BUG (CRITICAL): Crash Server (Error 500) khi nhập tiêu đề quá dài do không bắt lỗi (validate) từ Backend!");
            } else {
                Assert.fail("❌ KẾT QUẢ: Không thấy thông báo lỗi nào hiển thị trên màn hình!");
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