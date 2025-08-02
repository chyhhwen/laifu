package laifu.fu.lai.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResizeWindowHandler extends MouseAdapter {
    private static final int NONE = 0, MOVE = 1, RESIZE_W = 2, RESIZE_E = 3, RESIZE_N = 4, RESIZE_S = 5,
            RESIZE_NW = 6, RESIZE_NE = 7, RESIZE_SW = 8, RESIZE_SE = 9;
    private final JFrame frame;
    private final int border;
    private Point startDrag;
    private int resizeDir = NONE;

    public ResizeWindowHandler(JFrame frame, int border) {
        this.frame = frame;
        this.border = border;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        resizeDir = getResizeDir(e);
        frame.setCursor(getCursor(resizeDir));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startDrag = e.getPoint();
        resizeDir = getResizeDir(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(resizeDir == NONE) return;
        Rectangle bounds = frame.getBounds();
        int mx = e.getXOnScreen();
        int my = e.getYOnScreen();

        switch(resizeDir) {
            case RESIZE_W:
                int newW = bounds.width + bounds.x - mx;
                if(newW > frame.getMinimumSize().width) {
                    bounds.width = newW;
                    bounds.x = mx;
                    frame.setBounds(bounds);
                }
                break;
            case RESIZE_E:
                bounds.width = Math.max(frame.getMinimumSize().width, mx - bounds.x);
                frame.setBounds(bounds);
                break;
            case RESIZE_N:
                int newH = bounds.height + bounds.y - my;
                if(newH > frame.getMinimumSize().height) {
                    bounds.height = newH;
                    bounds.y = my;
                    frame.setBounds(bounds);
                }
                break;
            case RESIZE_S:
                bounds.height = Math.max(frame.getMinimumSize().height, my - bounds.y);
                frame.setBounds(bounds);
                break;
            case RESIZE_NW:
                mouseDragged(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        0, 0, e.getClickCount(), false, MouseEvent.BUTTON1));
                mouseDragged(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        0, 0, e.getClickCount(), false, MouseEvent.BUTTON1));
                break;
            case RESIZE_NE:
                mouseDragged(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        frame.getWidth(), 0, e.getClickCount(), false, MouseEvent.BUTTON1));
                mouseDragged(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        frame.getWidth(), 0, e.getClickCount(), false, MouseEvent.BUTTON1));
                break;
            case RESIZE_SW:
                mouseDragged(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        0, frame.getHeight(), e.getClickCount(), false, MouseEvent.BUTTON1));
                mouseDragged(new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                        0, frame.getHeight(), e.getClickCount(), false, MouseEvent.BUTTON1));
                break;
            case RESIZE_SE:
                frame.setSize(Math.max(frame.getMinimumSize().width, mx - bounds.x),
                        Math.max(frame.getMinimumSize().height, my - bounds.y));
                break;
        }
    }

    private int getResizeDir(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int w = frame.getWidth();
        int h = frame.getHeight();
        boolean left = x < border;
        boolean right = x > w - border;
        boolean top = y < border;
        boolean bottom = y > h - border;

        if(left && top) return RESIZE_NW;
        if(right && top) return RESIZE_NE;
        if(left && bottom) return RESIZE_SW;
        if(right && bottom) return RESIZE_SE;
        if(left) return RESIZE_W;
        if(right) return RESIZE_E;
        if(top) return RESIZE_N;
        if(bottom) return RESIZE_S;
        return NONE;
    }

    private Cursor getCursor(int dir) {
        switch(dir) {
            case RESIZE_W: return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
            case RESIZE_E: return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
            case RESIZE_N: return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            case RESIZE_S: return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
            case RESIZE_NW: return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
            case RESIZE_NE: return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
            case RESIZE_SW: return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
            case RESIZE_SE: return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
            default: return Cursor.getDefaultCursor();
        }
    }
}