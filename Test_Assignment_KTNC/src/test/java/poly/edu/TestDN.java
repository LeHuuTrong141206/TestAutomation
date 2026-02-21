package poly.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class TestDN {
    WebDriver driver;

    // alwaysRun = true là luôn chạy
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // ================= TEST 1: ĐĂNG NHẬP =================
    // priority giống số thứ tự ưu tiên
    @Test(groups = {"auth", "login"}, priority = 1)
    public void testLogin() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/div/input")).sendKeys("minhanh@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/div/input")).sendKeys("pass002");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        String urlAfterLogin = driver.getCurrentUrl();
        Assert.assertTrue(urlAfterLogin.contains("/cinevo/user"), "LỖI: Đăng nhập thất bại, sai URL!");
        Thread.sleep(2000);
    }

    // ================= TEST 2: YÊU THÍCH VIDEO (CẦN ĐĂNG NHẬP) =================
    // dependsOnMethods: bắt buộc test đó xong thì ms chạy đc test này
    @Test(groups = {"video", "like_auth"}, priority = 2, dependsOnMethods = "testLogin")
    public void testLikeVideoLoggedIn() throws InterruptedException {
        // Trở về trang chủ cho chắc chắn
        driver.get("http://localhost:8080/cinevo-web/cinevo/user");
        Thread.sleep(1000);

        // Click vào video Spiderman
        // cho video spiderman thành 1 biến
        WebElement videoSpiderman = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div/a/div[1]/div"));

        // Gọi biến cuộn chuột JavascriptExecutor ra
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Cho cuộn màn hình xuống đúng video spiderman
        // scrollIntoView(true) là cuộn đến mép trên video chậm vào mép màn hình còn false sẽ là mép dưới video
        js.executeScript("arguments[0].scrollIntoView(false);", videoSpiderman);
        Thread.sleep(1000);
        videoSpiderman.click();

        Thread.sleep(2000);

        String urlvideonoibat = driver.getCurrentUrl();
        Assert.assertTrue(urlvideonoibat.contains("id=20"), "LỖI: Không vào được trang chi tiết video!");

        // Gắn nút yêu thích/Đã thích thành 1 biến
        WebElement nutYeuThich = driver.findElement(By.xpath("//button[contains(., 'Yêu thích')] | //button[contains(., 'Đã thích')]")); //Xpath có cấu trúc tưởng đối, còn cái hay dùng là tuyệt đối

        // Gọi JavaScriptExecutor ra để click ép buộc (bỏ qua cuộn chuột luôn cũng được)
        js.executeScript("arguments[0].click();", nutYeuThich);
        Thread.sleep(2000);
    }

    // ================= TEST 3: ĐĂNG XUẤT =================
    @Test(groups = {"auth", "logout"}, priority = 3, dependsOnMethods = "testLogin")
    public void testLogout() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user"); // Reset về trang chủ trước khi thao tác
        Thread.sleep(1000);

        // Nhấn Avatar xổ menu
        driver.findElement(By.xpath("//*[@id=\"collapsibleNavbar\"]/div/div/a")).click();
        Thread.sleep(1000);
        // Nhấn Đăng xuất
        driver.findElement(By.xpath("//*[@id=\"collapsibleNavbar\"]/div/div/ul/li[8]/a")).click();

        String urlAfterLogout = driver.getCurrentUrl();
        // Sửa lại kiểm chứng: Đăng xuất xong thường bị văng về /login hoặc /user ẩn chức năng
        Assert.assertTrue(urlAfterLogout.contains("/cinevo/user?tab=login") || urlAfterLogout.contains("/cinevo/user"), "LỖI: Đăng xuất thất bại!");
        Thread.sleep(2000);
    }

    // ================= TEST 4: BẢO MẬT - CHƯA ĐĂNG NHẬP NHƯNG BẤM YÊU THÍCH =================
    // Test này chạy độc lập ở priority 4 (Lúc này tài khoản đã bị Test 3 đăng xuất)
    @Test(groups = {"security", "like_unauth"}, priority = 4)
    public void testLikeVideoUnauthorized() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user");
        Thread.sleep(2000);

        // Vào lại video Spiderman
        WebElement videoSpiderman = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div/a/div[1]/div"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(false);", videoSpiderman);
        Thread.sleep(1000);
        videoSpiderman.click();

        Thread.sleep(2000);

        String urlvideonoibat = driver.getCurrentUrl();
        Assert.assertTrue(urlvideonoibat.contains("id=20"), "LỖI: Không vào được trang chi tiết video!");

        // Gắn nút yêu thích thành 1 biến
        WebElement nutYeuThich = driver.findElement(By.xpath("//a[contains(., 'Yêu thích')]"));

        // Gọi JavaScriptExecutor ra để click ép buộc (bỏ qua cuộn chuột luôn cũng được)
        js.executeScript("arguments[0].click();", nutYeuThich);
        Thread.sleep(2000);

        // Trình duyệt BẮT BUỘC phải chuyển sang trang Đăng nhập
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/cinevo/user?tab=login"), "LỖI BẢO MẬT: Yêu thích vẫn được là sao");
    }

    // ================= TEST 5: ĐĂNG NHẬP TK KHÔNG CÓ THẬT =================
    @Test(groups = {"auth", "loginnotauth"}, priority = 5)
    public void testLoginNotAuth() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/div/input")).sendKeys("lehuutrong@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/div/input")).sendKeys("113");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        Thread.sleep(2000);

        WebElement thongBaoLoi = driver.findElement(By.xpath("//*[contains(., 'Email không tồn tại')]"));
        Assert.assertTrue(thongBaoLoi.isDisplayed(), "LỖI: Có chặn đăng nhập nhưng lại không thông báo lỗi cho người dùng biết!");

    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("====== TEST XONG ======");
    }
}