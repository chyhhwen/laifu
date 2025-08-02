package laifu.fu.lai.gui;

import laifu.fu.lai.component.CGComponent;
import org.cef.CefApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple utility class that wraps the JFrame creation logic for the
 * embedded JCEF browser. It is responsible for displaying the browser
 * component and shutting down the CEF runtime when the window is closed.
 */
public class GuiManager {
    private JFrame frame;
    private final List<CGComponent> handlers = new ArrayList<>();

    /** Register a component to be managed. */
    public GuiManager register(CGComponent component) {
        handlers.add(component);
        return this;
    }

    /** Display the window and enable all registered components. */
    public void show() {
        frame = new JFrame("Fu Desktop App");
        frame.setUndecorated(true); // 隱藏所有外框與標題欄
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);    // 默认窗口全屏
        frame.setLocationRelativeTo(null);

        JPanel dragPanel = new JPanel();
        dragPanel.setPreferredSize(new Dimension(0, 10)); // 高 40px
        dragPanel.setOpaque(false); // 如果要透明或之後自訂著色都可

        // 掛上拖曳事件
        int border = 10;
        ResizeWindowHandler resizeHandler = new ResizeWindowHandler(frame, border);
        frame.addMouseListener(resizeHandler);
        frame.addMouseMotionListener(resizeHandler);
        DragWindowHandler dragWindowHandler = new DragWindowHandler(frame);
        dragPanel.addMouseListener(dragWindowHandler);
        dragPanel.addMouseMotionListener(dragWindowHandler);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(dragPanel, BorderLayout.NORTH);

        JPanel stackPanel = new JPanel();
        stackPanel.setLayout(new BoxLayout(stackPanel, BoxLayout.Y_AXIS));

        // 將所有 handler 的 ui 組件加入 stackPanel
        for (CGComponent handler : handlers) {
            try {
                handler.onEnable(frame);
                Component ui = handler.getComponent();
                if (ui != null) {
                    stackPanel.add(ui);
                }
            } catch (Exception ex) {
                handler.onError(ex);
            }
        }

        mainPanel.add(stackPanel, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (CGComponent handler : handlers) {
                    try {
                        handler.onDisable();
                    } catch (Exception ex) {
                        handler.onError(ex);
                    }
                }
                CefApp.getInstance().dispose();
                frame.dispose();
                System.exit(0);
            }
        });

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setLocation(100, 100);
        frame.setSize(1366, 738);
        frame.setVisible(true);
    }

    /** Reload all registered components. */
    public void reload() {
        for (CGComponent handler : handlers) {
            try {
                handler.onReload();
            } catch (Exception ex) {
                handler.onError(ex);
            }
        }
    }
}
