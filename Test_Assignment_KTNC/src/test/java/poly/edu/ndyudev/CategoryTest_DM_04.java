package poly.edu.ndyudev;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class CategoryTest_DM_04 {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testDM_04_ThemKyTuDacBietRoiXoa() throws InterruptedException {
        System.out.println("=== CHẠY TEST CASE: DM_04 (Thêm '18+' rồi Xóa) ===");

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
        // PHẦN 2: THÊM DANH MỤC MỚI ("18+")
        // =======================================================
        // Truy cập thẳng vào trang Quản lý Danh Mục theo URL bro cung cấp
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=category");
        Thread.sleep(1500);

        // Nhập "18+" vào ô Tên Danh Mục
        driver.findElement(By.name("name")).sendKeys("18+");
        System.out.println("-> 2. Đã nhập tên danh mục: 18+");
        Thread.sleep(1000);

        // Bấm nút Lưu (Bắt chính xác nút có chữ Lưu và type=submit)
        // Tìm nút Lưu
        WebElement btnLuu = driver.findElement(By.xpath("//button[@type='submit' and contains(., 'Lưu')]"));

        // Dùng Javascript để tự động cuộn màn hình xuống đúng chỗ cái nút đó
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btnLuu);

        // Nghỉ nửa giây cho màn hình cuộn mượt mà
        Thread.sleep(500);

        // Giờ thì nó hiện ra rồi, bấm thoải mái!
        btnLuu.click();
        Thread.sleep(2000);

        // Kiểm tra Alert "Thêm thành công" (Dựa vào class alert-info trong JSP)
        try {
            WebElement msg = driver.findElement(By.cssSelector(".alert.alert-info"));
            System.out.println("-> 3. Hệ thống báo: " + msg.getText().trim());
        } catch (Exception e) {
            System.out.println("-> (Bỏ qua nếu web không văng alert thêm mới)");
        }

        // =======================================================
        // PHẦN 3: TÌM TRONG BẢNG VÀ XÓA DANH MỤC "18+"
        // =======================================================
        try {
            // 1. Tìm cái nút Xóa của dòng 18+
            WebElement btnDelete = driver.findElement(By.xpath("//tr[td[contains(text(), '18+')]]//a[contains(@class, 'btn-danger')]"));

            // 2. Ép con bot LĂN CHUỘT tuốt xuống cuối bảng cho đến khi thấy cái nút Xóa
            JavascriptExecutor js2 = (JavascriptExecutor) driver;
            js2.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnDelete);

            // Nghỉ 1 giây để hiệu ứng cuộn chuột trôi xuống tới nơi
            Thread.sleep(1000);

            // 3. Thực hiện click (Nếu click bình thường vẫn bị lỗi chặn, thì dùng lệnh click của JS luôn)
            try {
                btnDelete.click();
            } catch (Exception clickEx) {
                // Tuyệt chiêu cuối: Ép click thẳng bằng Javascript bất chấp giao diện che khuất
                js.executeScript("arguments[0].click();", btnDelete);
            }

            System.out.println("-> 4. Đã cuộn xuống cuối bảng và nhấn nút XÓA dòng '18+'");
            Thread.sleep(2000);

            // Xóa xong, kiểm tra câu thông báo
            WebElement deleteMsg = driver.findElement(By.cssSelector(".alert.alert-info"));
            String msgText = deleteMsg.getText().toLowerCase();

            Assert.assertTrue(msgText.contains("thành công") || msgText.contains("xóa"),
                    "Lỗi: Lời nhắn không đúng chuẩn!");

            System.out.println("-> 5. Hệ thống báo sau khi xóa: " + msgText);
            System.out.println("✅ KẾT QUẢ: TEST CASE PASS (Đúng với kết quả mong muốn!)");

        } catch (Exception e) {
            Assert.fail("❌ KẾT QUẢ: FAIL (Không tìm thấy danh mục '18+' ở cuối bảng hoặc lỗi khi Xóa)");
        }
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(3000); // Ngâm 3s cho bro nhìn kết quả
        if (driver != null) {
            driver.quit();
        }
    }
}