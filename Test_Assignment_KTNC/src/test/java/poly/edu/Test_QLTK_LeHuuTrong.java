package poly.edu;

// Selenium & WebDriver
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.io.FileHandler;

// TestNG
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

// Excel
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Java Utils
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Test_QLTK_LeHuuTrong {
    WebDriver driver;

    // =========================================================================
    // PHẦN 1: CÁC HÀM TIỆN ÍCH (HELPER METHODS)
    // =========================================================================

    public void chupAnhManHinh(String tenFileAnh) {
        try {
            // Tạo thư mục Screenshots nếu chưa có
            File thuMuc = new File("./Screenshots");
            if (!thuMuc.exists()) {
                thuMuc.mkdirs();
            }

            // Nhờ robot chụp lại toàn bộ màn hình
            File anhChup = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Lưu ảnh vào thư mục 'Screenshots' trong project
            File noiLuu = new File("./Screenshots/" + tenFileAnh + ".png");
            FileHandler.copy(anhChup, noiLuu);

            System.out.println("📸 Đã chụp ảnh bằng chứng: " + tenFileAnh + ".png");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi chụp ảnh: " + e.getMessage());
        }
    }

    public void ghiKetQuaVaoExcel(int viTriDong, int viTriCot, String noiDung) {
        try {
            File file = new File("Bảng test Ass_KTNC.xlsx");
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet("TaiKhoan_QLTK");

            // Tìm đúng dòng robot vừa chạy, tạo một ô mới ở cột chỉ định và điền chữ vào
            sheet.getRow(viTriDong).createCell(viTriCot).setCellValue(noiDung);

            // Đóng luồng đọc, mở luồng ghi để lưu file
            fis.close();
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
            wb.close();
            System.out.println("📝 Đã cập nhật file Excel thành công!");
        } catch (Exception e) {
            System.out.println("❌ Lỗi ghi Excel: " + e.getMessage());
        }
    }


    // =========================================================================
    // PHẦN 2: CUNG CẤP DỮ LIỆU TỪ EXCEL (DATA PROVIDERS)
    // =========================================================================

    @DataProvider(name = "DuLieuExcel_DangNhap")
    public Object[][] docDataLogin() throws Exception {
        // Chỉ vào file Excel
        FileInputStream fis = new FileInputStream("Bảng test Ass_KTNC.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("DangNhap");  // Vào Sheet DangNhap
        DataFormatter formatter = new DataFormatter();

        String emailCanTim = "duy@example.com";
        int soCot = sheet.getRow(0).getLastCellNum();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i) == null) continue;

            String emailTrongExcel = formatter.formatCellValue(sheet.getRow(i).getCell(0)).trim();

            if (emailTrongExcel.equalsIgnoreCase(emailCanTim)) {
                Object[] duLieuMotDong = new Object[soCot];
                for (int j = 0; j < soCot; j++) {
                    duLieuMotDong[j] = formatter.formatCellValue(sheet.getRow(i).getCell(j));
                }
                wb.close();
                return new Object[][] { duLieuMotDong };
            }
        }
        wb.close();
        throw new Exception("🛑 LỖI: Không tìm thấy tài khoản " + emailCanTim + " trong Sheet DangNhap!");
    }

    @DataProvider(name = "DuLieuExcel_TaiKhoan_Test")
    public Object[][] docDataTuExcel() throws Exception {
        FileInputStream fis = new FileInputStream("Bảng test Ass_KTNC.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("TaiKhoan_QLTK");

        int soDong = sheet.getLastRowNum();
        int soCot = sheet.getRow(0).getLastCellNum();
        DataFormatter formatter = new DataFormatter();
        List<Object[]> danhSachDataHopLe = new ArrayList<>();

        for (int i = 1; i <= soDong; i++) {
            if (sheet.getRow(i) == null) continue;

            String kichBan = formatter.formatCellValue(sheet.getRow(i).getCell(0)).trim();
            if (kichBan.isEmpty()) continue;

            Object[] duLieuMotDong = new Object[soCot + 1];
            duLieuMotDong[0] = i; // Cất vị trí dòng vào ô đầu tiên
            for (int j = 0; j < soCot; j++) {
                duLieuMotDong[j + 1] = formatter.formatCellValue(sheet.getRow(i).getCell(j));
            }
            danhSachDataHopLe.add(duLieuMotDong);
        }
        wb.close();
        return danhSachDataHopLe.toArray(new Object[0][0]);
    }


    // =========================================================================
    // PHẦN 3: SETUP & CÁC KỊCH BẢN TEST (TEST CASES)
    // =========================================================================

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(groups = {"admin", "login"}, priority = 1, dataProvider = "DuLieuExcel_DangNhap")
    public void vaoadmin(String email, String matKhau) throws InterruptedException {
        System.out.println("Đăng nhập quyền Admin...");
        System.out.println("Tài khoản Admin: " + email);
        System.out.println("Mật khẩu Admin: " + matKhau);
        Thread.sleep(2000);

        driver.get("http://localhost:8080/cinevo-web/cinevo/user?tab=login");
        Thread.sleep(3000);

        driver.findElement(By.xpath("/html/body/div/div/div/form/div[1]/div/input")).sendKeys(email);
        driver.findElement(By.xpath("/html/body/div/div/div/form/div[2]/div/input")).sendKeys(matKhau);
        driver.findElement(By.xpath("/html/body/div/div/div/form/button")).click();
        Thread.sleep(2000);

        String urlHienTai = driver.getCurrentUrl();
        Assert.assertTrue(urlHienTai.contains("/cinevo/admin"), "LỖI: Không vào trang admin");

        WebElement thongBao = driver.findElement(By.xpath("//*[contains(., 'Đăng nhập thành công')]"));
        Assert.assertTrue(thongBao.isDisplayed(), "LỖI: Không thấy thông báo đăng nhập thành công!");

        chupAnhManHinh("TC1_DangNhapAdminThanhCong");
        System.out.println("Hoàn thành Test 1");
    }

    @Test(groups = {"admin", "account"}, dataProvider = "DuLieuExcel_TaiKhoan_Test", dependsOnMethods = "vaoadmin")
    public void testThemTaiKhoan(int viTriDong, String kichBan, String hoTen, String email, String matKhau, String vaiTro, String cotAnh) throws InterruptedException {

        System.out.println("==============================================");
        System.out.println("Đang chạy kịch bản: " + kichBan);

        // Mở thẳng trang Quản lý tài khoản
        driver.get("http://localhost:8080/cinevo-web/cinevo/admin?tab=users");
        Thread.sleep(2000);

        // Đưa DỮ LIỆU TỪ EXCEL VÀO FORM
        System.out.println("Đang nhập Data: " + hoTen + " | " + email + " | " + matKhau);
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div[1]/input")).sendKeys(hoTen);
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div[2]/input")).sendKeys(email);
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/div[3]/input")).sendKeys(matKhau);

        // CHỌN VAI TRÒ (Admin hoặc User)
        if (vaiTro.equalsIgnoreCase("Admin")) {
            driver.findElement(By.xpath("//*[@id=\"roleAdmin\"]")).click();
        } else {
            driver.findElement(By.xpath("//*[@id=\"roleUser\"]")).click();
        }

        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/form/button")).click();
        Thread.sleep(2000); // Chờ web xử lý dữ liệu

        // ================= KIỂM CHỨNG =================
        String tenAnh = "TC_ThemTK_" + kichBan.replaceAll("[^a-zA-Z0-9]", "_");

        try {
            // Dùng if-else để phân biệt được kịch bản:
            if (kichBan.toLowerCase().contains("chuẩn") || kichBan.toLowerCase().contains("thành công")) {
                WebElement thongBaoTot = driver.findElement(By.xpath("//*[contains(., 'Thêm thành công')]"));
                Assert.assertTrue(thongBaoTot.isDisplayed(), "LỖI: Nhập data chuẩn mà không thấy báo thành công!");
            } else {
                WebElement thongBaoLoi = driver.findElement(By.xpath("//*[contains(., 'không hợp lệ') or contains(., 'tối thiểu')]"));
                Assert.assertTrue(thongBaoLoi.isDisplayed(), "LỖI: Nhập sai data mà form không thèm báo lỗi!");
            }

            // Nếu code chạy đến đây ko lỗi nào -> TỨC LÀ PASS ---
            tenAnh = "PASS_" + tenAnh;
            chupAnhManHinh(tenAnh);
            ghiKetQuaVaoExcel(viTriDong, 5, "PASS - Đã lưu ảnh: " + tenAnh + ".png");
            System.out.println("Đã test xong (Đúng như mong đợi): " + kichBan);

        } catch (Throwable e) {
            // Nếu tìm không thấy thì -> FAIL ---
            tenAnh = "FAIL_" + tenAnh;
            chupAnhManHinh(tenAnh);
            ghiKetQuaVaoExcel(viTriDong, 5, "FAIL - Xem ảnh: " + tenAnh + ".png");
            System.out.println("Test thất bại: " + kichBan);

            // Chụp ảnh và ghi sổ xong xuôi rồi thì ném lỗi lại cho TestNG đánh dấu đỏ!
            throw e;
        }
    }

    // =========================================================================
    // PHẦN 4: KẾT THÚC DỌN DẸP
    // =========================================================================

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        System.out.println("====== TEST XONG ======");
    }
}