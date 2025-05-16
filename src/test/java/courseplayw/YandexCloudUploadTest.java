//package courseplayw;
//
//import com.microsoft.playwright.Browser;
//import com.microsoft.playwright.FileChooser;
//import com.microsoft.playwright.Page;
//import com.microsoft.playwright.Playwright;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//
//import java.net.URI;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class YandexCloudUploadTest {
//    public static void main(String[] args) throws Exception {
//        // Генерация CSV
//        String csvData = "ID,Name\n1,Иван Петров\n2,Анна Сидорова";
//        Path csvPath = Paths.get("users.csv");
//        Files.write(csvPath, csvData.getBytes());
//
//        // Загрузка файла через Playwright
//        try (Playwright playwright = Playwright.create()) {
//            Browser browser = playwright.chromium().launch();
//            Page page = browser.newPage();
//            page.navigate("https://the-internet.herokuapp.com/upload");
//
//            FileChooser fileChooser = page.waitForFileChooser(() -> page.click("#file-upload"));
//            fileChooser.setFiles(csvPath);
//            page.click("#file-submit");
//            assert page.locator("h3").textContent().contains("File Uploaded!");
//        }
//        // Проверка в Яндекс.Облако
//        S3Client s3Client = S3Client.builder()
//                .region(Region.of("ru-central1"))
//                .endpointOverride(URI.create("https://storage.yandexcloud.net"))
//                .credentialsProvider(() -> AwsBasicCredentials.create("YCAJEpXKLdtjvhb7wfLFBrZ0j", "YCPtqa1AgfZp1ceGQmTDH6A5pfWZo-bso30udeNZ"))
//                .build();
//
//        String content = s3Client.getObjectAsBytes(GetObjectRequest.builder()
//                .bucket("ksiuhere")
//                .key("users.csv")
//                .build()).asUtf8String();
//
//        assert content.contains("Иван Петров");
//    }
//}