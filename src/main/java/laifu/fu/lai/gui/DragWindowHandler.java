package laifu.fu.lai.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragWindowHandler extends MouseAdapter {
    private final JFrame frame;
    private Point mousePressedLocation = null;

    public DragWindowHandler(JFrame frame) {
        this.frame = frame;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedLocation = e.getLocationOnScreen();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if (mousePressedLocation == null) return;
        Point currentLocation = e.getLocationOnScreen();
        Point frameLocation = frame.getLocation();
        int dx = currentLocation.x - mousePressedLocation.x;
        int dy = currentLocation.y - mousePressedLocation.y;
        frame.setLocation(frameLocation.x + dx, frameLocation.y + dy);
        mousePressedLocation = currentLocation;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressedLocation = null;
    }
}