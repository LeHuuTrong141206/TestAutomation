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

public class VideoTest_QLURL_BM02 {
    WebDriver driver;

    // 1. MỞ TRÌNH DUYỆT
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    //  CHƯA ĐĂNG NHẬP NHƯNG COPY LINK DÁN VÀO TRÌNH DUYỆT
    @Test(priority = 1, groups = {"security"})
    public void testChuaDangNhapXemVideo() throws InterruptedException {
        System.out.println("Bắt đầu test: Copy link video dán thẳng vào URL khi chưa đăng nhập...");

        // Bước 1: Gõ thẳng đường link của 1 video cụ thể vào trình duyệt
        String linkChiTietVideo = "http://localhost:8080/cinevo-web/cinevo/admin?tab=videos";

        System.out.println("Đang cố truy cập link: " + linkChiTietVideo);
        driver.get(linkChiTietVideo);

        // Chờ 2 giây để hệ thống check quyền và đá văng ra
        Thread.sleep(2000);

        // Bước 2: Xem nó đang ở trang nào
        String urlHienTai = driver.getCurrentUrl();
        System.out.println("URL hiện tại sau khi dán link là: " + urlHienTai);

        // Bước 3: Đánh giá kết quả
        // KỲ VỌNG: Trang web phải tự động đẩy về trang Login có chứa chữ "login"
        boolean biChuyenVeLogin = urlHienTai.contains("login") || urlHienTai.contains("user?tab=login");

        Assert.assertTrue(biChuyenVeLogin, "LỖI BẢO MẬT: Chưa đăng nhập, chỉ cần có link là xem được video chùa!");
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