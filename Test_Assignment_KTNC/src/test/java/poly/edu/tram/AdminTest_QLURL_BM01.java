package poly.edu.tram;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class AdminTest_QLURL_BM01 {
    WebDriver driver;

    // 1. MỞ TRÌNH DUYỆT TRƯỚC KHI TEST
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Thời gian chờ tối đa 10s để tìm phần tử
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // 2. KỊCH BẢN TEST: BẢO MẬT URL ADMIN
    @Test(priority = 1, groups = {"security"})
    public void testChanTruyCapTrangAdmin() throws InterruptedException {
        System.out.println("Bắt đầu test: Cố tình truy cập trang Admin khi CHƯA ĐĂNG NHẬP...");

        // Bước 1: Trình duyệt đang trống, ta gõ thẳng URL của trang Admin/Quản lý
        String linkAdmin = "http://localhost:8080/cinevo-web/cinevo/admin";
        driver.get(linkAdmin);

        // Chờ 2 giây để hệ thống web xử lý việc chặn và đá văng ra
        Thread.sleep(2000);

        // Bước 2: Lấy URL hiện tại sau khi bị hệ thống chuyển hướng
        String urlHienTai = driver.getCurrentUrl();
        System.out.println("Đã bị chuyển hướng đến URL: " + urlHienTai);

        // Bước 3: Kiểm tra xem URL hiện tại có phải là trang Đăng nhập hay không
        boolean biChuyenVeLogin = urlHienTai.contains("login") || urlHienTai.contains("user?tab=login");

        // Nếu biChuyenVeLogin = true -> PASS, nếu false -> Báo lỗi đỏ
        Assert.assertTrue(biChuyenVeLogin, "LỖI BẢO MẬT: Chưa đăng nhập mà vẫn vào được trang Admin!");
    }

    // 3. CHỤP MÀN HÌNH VÀ ĐÓNG TRÌNH DUYỆT SAU KHI TEST XONG
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // --- Tự động chụp màn hình ---
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            // Lấy tên test case và trạng thái Pass/Fail
            String trangThai = result.isSuccess() ? "PASS" : "FAIL";
            String tenAnh = result.getName() + "_" + trangThai + "_" + System.currentTimeMillis() + ".png";

            // Lưu vào thư mục screenshots (nó tự tạo nếu chưa có)
            File thuMuc = new File("./screenshots/");
            if (!thuMuc.exists()) thuMuc.mkdirs();

            File destination = new File("./screenshots/" + tenAnh);
            FileHandler.copy(source, destination);
            System.out.println("Đã chụp ảnh kết quả lưu tại: " + destination.getPath());
        } catch (IOException e) {
            System.out.println("Lỗi chụp màn hình: " + e.getMessage());
        }

    }
}