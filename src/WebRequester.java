import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WebRequester {

    private static final String TARGET_URL = "Your WebSite";
    private static final int INTERVAL_SECONDS = 4;

    public static void main(String[] args) {
        System.out.println("开始访问 " + TARGET_URL + "，每 " + INTERVAL_SECONDS + " 秒一次。按 Ctrl+C 停止。");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger counter = new AtomicInteger(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                URL url = new URL(TARGET_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
                connection.setRequestProperty("Connection", "keep-alive");
                connection.connect();

                int statusCode = connection.getResponseCode();
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                if (statusCode == HttpURLConnection.HTTP_OK) {
                    int currentCount = counter.getAndIncrement();
                    System.out.println("[" + timestamp + "] 第 " + currentCount + " 次成功访问 " + TARGET_URL + "，状态码：" + statusCode);
                } else {
                    System.out.println("[" + timestamp + "] 访问 " + TARGET_URL + " 失败，状态码：" + statusCode);
                }

                connection.disconnect();

            } catch (IOException e) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                System.out.println("[" + timestamp + "] 访问 " + TARGET_URL + " 发生错误：" + e.getMessage());
            }
        }, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
    }
}