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

	public static void main(String[] args)
	{
		try {
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
					// 此监听器内的代码会在 Spring 应用完全就绪后执行
					// 这是启动 GUI 的正确时机
					port = event.getWebServer().getPort();
				})
				.run(args);
		LOGGER.info("Application started.");
	}

    public static Integer getPort()
    {
        return port;
    }
}
