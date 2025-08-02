package laifu.fu.lai.gui;

import laifu.fu.lai.FuApplication;
import laifu.fu.lai.component.BrowserComponent;
import laifu.fu.lai.handler.DownloadHandler;
import laifu.fu.lai.handler.MenuHandler;
import laifu.fu.lai.handler.MessageRouterHandler;
import laifu.fu.lai.services.CoreService;
import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.CefFocusHandlerAdapter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Component
public class UI implements InitializingBean
{
    private final CoreService myService;
    private CefApp cefApp_;
    private boolean browserFocus_;

    public UI(CoreService myService) {
        this.myService = myService;
    }


    @EventListener
    public void onWebServerReady(WebServerInitializedEvent event) {
        init();
    }


    private void init() {
        EventQueue.invokeLater(() -> {
            CefAppBuilder builder = new CefAppBuilder();
            // windowless_rendering_enabled must be set to false if not wanted.
            builder.setInstallDir(new File("jcef-bundle")); //Default
            builder.addJcefArgs("--disable-gpu"); //Just an example
            builder.getCefSettings().windowless_rendering_enabled = false; //Default - select OSR mod
            builder.setProgressHandler(new ConsoleProgressHandler()); //Default
            // USE builder.setAppHandler INSTEAD OF CefApp.addAppHandler!
            // Fixes compatibility issues with macOS
            builder.setAppHandler(new MavenCefAppHandlerAdapter() {
                @Override
                public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                    // Shutdown the app if the native CEF part is terminated
                    if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
                }
            });
            try {
                cefApp_ = builder.build();
            } catch (IOException | UnsupportedPlatformException | CefInitializationException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            CefClient client = cefApp_.createClient();

            client.addContextMenuHandler(new MenuHandler());
            // 绑定 MessageRouter 使前端可以执行 js 到 java 中
            CefMessageRouter cmr = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig());
            cmr.addHandler(new MessageRouterHandler(myService), true);
            client.addMessageRouter(cmr);
            // 绑定 DownloadHandler 实现下载功能
            client.addDownloadHandler(new DownloadHandler());
            // Clear focus from the address field when the browser gains focus.
            client.addFocusHandler(new CefFocusHandlerAdapter() {
                @Override
                public void onGotFocus(CefBrowser browser) {
                    if (browserFocus_) return;
                    browserFocus_ = true;
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                    browser.setFocus(true);
                }

                @Override
                public void onTakeFocus(CefBrowser browser, boolean next) {
                    browserFocus_ = false;
                }
            });

            CefBrowser browser_ = client.createBrowser("http://localhost:"+ FuApplication.getPort(), false, false);

            GuiManager guiManager = new GuiManager();
            guiManager.register(new BrowserComponent(browser_));
            guiManager.show();
        });
    }

    @Override
    public void afterPropertiesSet()
    {
    }
}
