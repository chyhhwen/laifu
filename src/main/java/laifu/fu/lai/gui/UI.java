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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Component
public class UI implements InitializingBean
{
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UI.class);
    private final CoreService myService;
    private CefApp cefApp_;
    private boolean browserFocus_;

    public UI(CoreService myService) {
        this.myService = myService;
    }


    @EventListener
    public void onWebServerReady(WebServerInitializedEvent event) {
        LOGGER.info("WebServer initialized on port: {}. Starting GUI...", event.getWebServer().getPort());
        init();
    }


    private void init() {
        LOGGER.info("Entering init() method. Headless state: {}", GraphicsEnvironment.isHeadless());

        // 1. 先建立一個簡單的載入進度視窗
        EventQueue.invokeLater(() -> {
            JDialog loadingDialog = new JDialog();
            loadingDialog.setTitle("福福 AI 啟動中");
            loadingDialog.setUndecorated(true); // 簡潔風格
            loadingDialog.setSize(400, 120);
            loadingDialog.setLocationRelativeTo(null);
            loadingDialog.setLayout(new BorderLayout());
            loadingDialog.setAlwaysOnTop(true);

            JPanel panel = new JPanel(new BorderLayout(15, 15));
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(63, 71, 80), 1),
                    BorderFactory.createEmptyBorder(20, 25, 20, 25)
            ));
            panel.setBackground(new Color(18, 21, 25));

            JLabel label = new JLabel("正在準備瀏覽器組件...");
            label.setForeground(new Color(157, 171, 184));
            label.setFont(new Font("SansSerif", Font.BOLD, 14));

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            progressBar.setForeground(new Color(43, 48, 54));
            progressBar.setBackground(new Color(18, 20, 23));
            progressBar.setBorder(BorderFactory.createLineBorder(new Color(63, 71, 80)));

            panel.add(label, BorderLayout.NORTH);
            panel.add(progressBar, BorderLayout.CENTER);
            loadingDialog.add(panel);
            loadingDialog.setVisible(true);

            // 2. 將 JCEF 的建立與下載放進背景執行緒，以免卡死 UI 導致進度條不動
            new Thread(() -> {
                try {
                    CefAppBuilder builder = new CefAppBuilder();
                    // 讓 JCEF 資源包存放在家目錄下的 .laifu/jcef-bundle 中，確保打包後路徑依然有效
                    String userHome = System.getProperty("user.home");
                    builder.setInstallDir(new File(userHome + "/.laifu/jcef-bundle"));
                    builder.addJcefArgs("--disable-gpu");
                    builder.getCefSettings().windowless_rendering_enabled = false;

                    // USE builder.setAppHandler INSTEAD OF CefApp.addAppHandler!
                    // Fixes compatibility issues with macOS
                    builder.setAppHandler(new MavenCefAppHandlerAdapter() {
                        @Override
                        public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                            // Shutdown the app if the native CEF part is terminated
                            if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
                        }
                    });

                    // 註冊進度回報，將百分比同步到 Swing 進度條
                    builder.setProgressHandler((state, percent) -> {
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setValue((int) percent);
                            String stateText = switch (state) {
                                case LOCATING -> "正在定位資源";
                                case DOWNLOADING -> "正在下載核心";
                                case EXTRACTING -> "正在解壓組件";
                                case INSTALL -> "正在安裝";
                                case INITIALIZING -> "正在啟動核心";
                                default -> state.toString();
                            };
                            label.setText(stateText + "... " + (int) percent + "%");
                        });
                    });

                    cefApp_ = builder.build();

                    // 3. 核心建立完成後，回到 EDT 關閉載入視窗並顯示主視窗
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        startMainGui();
                    });

                } catch (Exception e) {
                    LOGGER.error("Failed to initialize JCEF", e);
                    SwingUtilities.invokeLater(() -> {
                        loadingDialog.dispose();
                        JOptionPane.showMessageDialog(null,
                                "啟動失敗：無法載入瀏覽器核心。\n" + e.getMessage(),
                                "啟動錯誤", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    });
                }
            }).start();
        });
    }

    private void startMainGui() {
        CefClient client = cefApp_.createClient();

        //client.addContextMenuHandler(new MenuHandler());
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

        CefBrowser browser_ = client.createBrowser("http://localhost:"+ FuApplication.getPort() + "/chat", false, false);

        GuiManager guiManager = new GuiManager();
        guiManager.register(new BrowserComponent(browser_));
        guiManager.show();

        // 將 DesktopWindowService 與 JFrame / CefBrowser 綁定，讓 HTTP API 能控制視窗
        try {
            laifu.fu.lai.desktop.DesktopWindowService desktop = FuApplication.getBean(laifu.fu.lai.desktop.DesktopWindowService.class);
            desktop.attachFrame(guiManager.getFrame());
            desktop.attachBrowser(browser_);
        } catch (Exception ignored) {
        }

        // 也提供快捷鍵（避免前端按鈕失效時無法開 DevTools）
        guiManager.getFrame().getRootPane().registerKeyboardAction(
                e -> { try { browser_.openDevTools(); } catch (Exception ignored) {} },
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I,
                        java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | java.awt.event.InputEvent.ALT_DOWN_MASK),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    @Override
    public void afterPropertiesSet()
    {
    }
}
