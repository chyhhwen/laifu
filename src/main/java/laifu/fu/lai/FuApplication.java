package laifu.fu.lai;

import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class FuApplication {
	private static final Logger LOGGER = Logger.getLogger(FuApplication.class.getName());
	private static Integer port;
	private static ConfigurableApplicationContext context;

	public static ConfigurableApplicationContext getApplicationContext() {
		return context;
	}

	public static <T> T getBean(Class<T> type) {
		ConfigurableApplicationContext ctx = context;
		if (ctx == null) throw new IllegalStateException("Spring context not initialized");
		return ctx.getBean(type);
	}

	public static void setApplicationContext(ConfigurableApplicationContext ctx) {
		context = ctx;
	}

	public static boolean isContextReady() {
		return context != null;
	}

	public static Integer getPort() {
		return port;
	}

	public static void setPort(Integer p) {
		port = p;
	}


	public static void main(String[] args)
	{
		try {
			// 確保資料庫目錄存在，避免 SQLite 報路徑不存在錯誤
			String userHome = System.getProperty("user.home");
			java.io.File dbDir = new java.io.File(userHome, ".laifu");
			if (!dbDir.exists()) {
				dbDir.mkdirs();
			}

			start(args);
		} catch (UnsupportedPlatformException | CefInitializationException e) {
			LOGGER.log(Level.SEVERE, "Failed to initialize platform", e);
			System.err.println("This platform is not supported or CEF failed to initialize.");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "I/O error", e);
			System.err.println("An I/O error occurred: " + e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Application interrupted", e);
			Thread.currentThread().interrupt();
			System.err.println("Application startup was interrupted.");
		}
	}

	private static void start(String[] args)
			throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {

		ConfigurableApplicationContext run = new SpringApplicationBuilder(FuApplication.class)
				.headless(false)
				.listeners((ApplicationListener<WebServerInitializedEvent>) event -> {
					// 這個 listener 只負責拿到 port（GUI 啟動由 UI 的 @EventListener 處理）
					setPort(event.getWebServer().getPort());
				})
				.run(args);
		setApplicationContext(run);
		LOGGER.info("Application started.");
	}

}
