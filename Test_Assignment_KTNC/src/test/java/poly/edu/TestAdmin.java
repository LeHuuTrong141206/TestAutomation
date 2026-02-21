package poly.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestAdmin {
    WebDriver driver;

    // alwaysRun = true là luôn chạy
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        // Tạo bảng điều khiển cấu hình riêng cho Chrome
        ChromeOptions options = new ChromeOptions();

        // Bật chế độ ẩn danh
        options.addArguments("--incognito");

        // Tắt thông báo
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // ================= TEST 1: ĐĂNG NHẬP TÀI KHOẢN ADMIN =================
    @Test(groups = {"admin", "login"}, priority = 1)
    public void testLogin() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/div/input")).sendKeys("duy@example.com");
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/div/input")).sendKeys("123456");
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();

        String urlAfterLogin = driver.getCurrentUrl();
        Assert.assertTrue(urlAfterLogin.contains("/cinevo/admin"), "LỖI: Đăng nhập thất bại, sai URL!");

        Thread.sleep(2000);

        WebElement thongBao = driver.findElement(By.xpath("//*[contains(., 'Đăng nhập thành công')]"));
        Assert.assertTrue(thongBao.isDisplayed(),"LỖI");

        Thread.sleep(2000);
    }

    // ================= TEST 2: THÊM DANH MỤC =================
    @Test(groups = {"admin", "category_add"}, priority = 2, dependsOnMethods = "testLogin")
    public void testCategory_1() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=dashboard");

        Thread.sleep(2000);

        driver.findElement(By.xpath("/html/body/div[1]/div/ul/li[3]/a")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div/input")).sendKeys("18Plus");

        Thread.sleep(2000);

        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/button")).click();

        Thread.sleep(2000);

        WebElement thongBao = driver.findElement(By.xpath("//*[contains(., 'Thêm thành công')]"));
        Assert.assertTrue(thongBao.isDisplayed(),"LỖI");

        Thread.sleep(2000);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement KTDS = driver.findElement(By.xpath("//*[contains(., '18Plus')]"));
        js.executeScript("arguments[0].scrollIntoView(false);", KTDS);

        Thread.sleep(5000);

        Assert.assertTrue(KTDS.isDisplayed(),"LỖI");

        Thread.sleep(2000);
    }

    // ================= TEST 3: ĐỔI 18PLUS THÀNH HAREM QUA NÚT EDIT =================
    @Test(groups = {"admin", "category_Editicon"}, priority = 3, dependsOnMethods = "testLogin")
    public void testCategory_2() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=category");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Thread.sleep(2000);

        WebElement LXDS = driver.findElement(By.xpath("//*[contains(., '18Plus')]"));
        js.executeScript("arguments[0].scrollIntoView(false);", LXDS);

        Thread.sleep(5000);

        //Tìm cột có 18plus và thẻ a thứ 2 cà click
        driver.findElement(By.xpath("//tr[contains(., '18Plus')]//a[1]")).click();

        Thread.sleep(2000);

        WebElement thongBao1 = driver.findElement(By.xpath("//*[contains(., 'Đang sửa')]"));
        Assert.assertTrue(thongBao1.isDisplayed(),"LỖI");

        WebElement oNhapTen = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div/input"));

        //Xóa chữ trong ô nhập
        oNhapTen.clear(); // oNhapTen.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE); (Xóa ô đó bằng tổ hợp phím ctrl + A + delete)

        Thread.sleep(2000);

        String tenmoi = "Harem";
        oNhapTen.sendKeys(tenmoi);

        Thread.sleep(2000);

        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/button")).click();

        WebElement thongBao2 = driver.findElement(By.xpath("//*[contains(., 'Cập nhật')]"));
        Assert.assertTrue(thongBao2.isDisplayed(),"LỖI");

        Thread.sleep(2000);
    }

    // ================= TEST 4: XÓA HAREM BẰNG ICON =================
    @Test(groups = {"admin", "category_Deleteicon"}, priority = 4, dependsOnMethods = "testLogin")
    public void testCategory_3() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=category");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Thread.sleep(2000);

        WebElement LXDS = driver.findElement(By.xpath("//*[contains(., 'Harem')]"));
        js.executeScript("arguments[0].scrollIntoView(false);", LXDS);

        Thread.sleep(5000);

        //Tìm cột có 18plus và thẻ a thứ 2 cà click
        driver.findElement(By.xpath("//tr[contains(., 'Harem')]//a[2]")).click();

        Thread.sleep(2000);

        WebElement thongBao = driver.findElement(By.xpath("//*[contains(., 'Xóa thành công')]"));
        Assert.assertTrue(thongBao.isDisplayed(),"LỖI");
    }

    // ================= TEST 5: TEST VỚI DANH MỤC CÓ KÝ TỰ ĐẶC BIỆT TỰ THÊM ZỒI TỰ XÓA =================
    @Test(groups = {"admin", "category_KyTuDacBiet"}, priority = 4, dependsOnMethods = "testLogin")
    public void testCategory_4_() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=category");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Thread.sleep(2000);

        WebElement oNhapTen = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div/input"));
        oNhapTen.sendKeys("18+");

        Thread.sleep(2000);

        WebElement nutLuu = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/button"));
        nutLuu.click();

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'Thêm thành công')]"));
        Assert.assertTrue(thongbao.isDisplayed(),"Lỗi");

        Thread.sleep(2000);

        WebElement cuon = driver.findElement(By.xpath("//*[contains(., '18+')]"));
        js.executeScript("arguments[0].scrollIntoView(false);", cuon);

        Thread.sleep(2000);

        WebElement iconDelete = driver.findElement(By.xpath("//tr[contains(., '18+')]//a[2]"));
        iconDelete.click();

        WebElement thongbao1 = driver.findElement(By.xpath("//*[contains(., 'Xóa thành công')]"));
        Assert.assertTrue(thongbao1.isDisplayed(),"Lỗi");


        Thread.sleep(2000);

    }

    // ================= TEST 6: TEST VỚI DANH MỤC CÓ KÝ TỰ NHIỀU CHỮ =================
    @Test(groups = {"admin", "category_QuaNhieuKyTu"}, priority = 4, dependsOnMethods = "testLogin")
    public void testCategory_5_() throws InterruptedException {
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=category");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Thread.sleep(2000);

        WebElement oNhapTen = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div/input"));
        oNhapTen.sendKeys("Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch Kịch ");

        Thread.sleep(2000);

        WebElement nutLuu = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/button"));
        nutLuu.click();

        Thread.sleep(2000);

        WebElement thongbao = driver.findElement(By.xpath("//*[contains(., 'Lỗi')]"));
        Assert.assertTrue(thongbao.isDisplayed(),"Lỗi");

        Thread.sleep(2000);

    }


    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("====== TEST XONG ======");
    }
}
