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

public class DashboardTest_QLURL_BM06 {
    WebDriver driver;

    // 1. MỞ TRÌNH DUYỆT
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // 2. KỊCH BẢN: CHƯA ĐĂNG NHẬP CỐ TÌNH DÁN LINK DASHBOARD
    @Test(priority = 1, groups = {"security"})
    public void testChuaDangNhapXemDashboard() throws InterruptedException {
        System.out.println("Bắt đầu test: Copy link Dashboard dán thẳng vào URL khi chưa đăng nhập...");

        // Bước 1: Gõ thẳng đường link trang Dashboard vào trình duyệt
        String linkDashboard = "http://localhost:8080/cinevo-web/cinevo/admin?tab=dashboard";

        System.out.println("Đang cố truy cập link Dashboard: " + linkDashboard);
        driver.get(linkDashboard);

        // Chờ 2 giây để hệ thống check quyền và đá văng ra
        Thread.sleep(2000);

        // Bước 2: Xem URL hiện tại bị đẩy đi đâu
        String urlHienTai = driver.getCurrentUrl();
        System.out.println("URL hiện tại sau khi dán link là: " + urlHienTai);

        // Bước 3: Đánh giá kết quả
        boolean biChuyenVeLogin = urlHienTai.contains("login") || urlHienTai.contains("user?tab=login");

        Assert.assertTrue(biChuyenVeLogin, "LỖI BẢO MẬT: Chưa đăng nhập mà vẫn vào xem được Dashboard (Thống kê)!");
    }

    // 3. CHỤP MÀN HÌNH SAU KHI TEST VÀ ĐÓNG TRÌNH DUYỆT
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            String trangThai = result.isSuccess() ? "PASS" : "FAIL";
            String tenAnh = result.getName() + "_" + trangThai + "_" + System.currentTimeMillis() + ".png";

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