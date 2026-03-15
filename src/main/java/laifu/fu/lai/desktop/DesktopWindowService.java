package laifu.fu.lai.desktop;

import org.cef.browser.CefBrowser;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;

@Service
public class DesktopWindowService {
    private volatile JFrame frame;
    private volatile CefBrowser browser;

    public void attachFrame(JFrame frame) {
        this.frame = frame;
    }

    public void attachBrowser(CefBrowser browser) {
        this.browser = browser;
    }

    public void minimize() {
        JFrame f = frame;
        if (f == null) return;
        EventQueue.invokeLater(() -> f.setState(Frame.ICONIFIED));
    }

    public void toggleMaximize() {
        JFrame f = frame;
        if (f == null) return;
        EventQueue.invokeLater(() -> {
            int state = f.getExtendedState();
            boolean maximized = (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
            if (maximized) {
                f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
            } else {
                f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
            }
        });
    }

    public void close() {
        JFrame f = frame;
        if (f == null) return;
        EventQueue.invokeLater(() -> {
            f.dispatchEvent(new java.awt.event.WindowEvent(f, java.awt.event.WindowEvent.WINDOW_CLOSING));
        });
    }

    public void openDevTools() {
        CefBrowser b = browser;
        if (b == null) return;
        b.openDevTools();
    }
}
