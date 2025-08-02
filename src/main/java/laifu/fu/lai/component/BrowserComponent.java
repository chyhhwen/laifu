package laifu.fu.lai.component;

import laifu.fu.lai.event.CGEventBus;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;

/**
 * A {@link CGComponent} implementation that shows a {@link CefBrowser}
 * inside the application window.
 */
public class BrowserComponent implements CGComponent {
    private final CefBrowser browser;
    private final CGEventBus eventBus = new CGEventBus();
    private Status status = Status.DISABLED;

    public BrowserComponent(CefBrowser browser) {
        this.browser = browser;
    }

    @Override
    public void onEnable(JFrame frame) {
        frame.getContentPane().add(getComponent(), BorderLayout.CENTER);
        status = Status.ENABLED;
    }

    @Override
    public void onDisable() {
        status = Status.DISABLED;
    }

    @Override
    public void onReload() {
        browser.reload();
    }

    @Override
    public void onError(Throwable throwable) {
        status = Status.ERROR;
    }

    @Override
    public Component getComponent() {
        return browser.getUIComponent();
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public CGEventBus getEventBus() {
        return eventBus;
    }
}
