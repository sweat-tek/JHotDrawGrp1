package org.jhotdraw.gui.plaf.palette;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

public class PaletteToolbarHandler implements ContainerListener,
        FocusListener, MouseInputListener, PropertyChangeListener {

    JToolBar toolBar;
    PaletteToolbarHandlerCallback callBack;

    PaletteToolbarHandler(JToolBar toolBar, PaletteToolbarHandlerCallback callBack) {
        this.toolBar = toolBar;
        this.callBack = callBack;
    }

    // ContainerListener
    @Override
    public void componentAdded(ContainerEvent evt) {
        Component c = evt.getChild();
        c.addFocusListener(this);
    }

    @Override
    public void componentRemoved(ContainerEvent evt) {
        Component c = evt.getChild();
        c.removeFocusListener(this);
    }

    // FocusListener
    @Override
    public void focusGained(FocusEvent evt) {
        Component c = evt.getComponent();
        callBack.focusGained(toolBar.getComponentIndex(c));
    }

    @Override
    public void focusLost(FocusEvent evt) {
    }

    // MouseInputListener (DockingListener)
    private JToolBar tb;
    private boolean isDragging = false;
    private Point origin = null;
    private boolean isArmed = false;

    void setToolbar(JToolBar toolBar) {
        tb = toolBar;
    }

    void setIsDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    void setOrigin(Point origin) {
        this.origin = origin;
    }

    Point getOrigin() {
        return origin;
    }

    boolean getIsDragging() {
        return isDragging;
    }

    // @FeatureEntryPoint(value = "drag-and-drop-pressed")
    @Override
    public void mousePressed(MouseEvent evt) {
        if (!tb.isEnabled()) {
            return;
        }
        isDragging = false;
        if (evt.getSource() instanceof JToolBar) {
            JComponent c = (JComponent) evt.getSource();
            Insets insets;
            if (c.getBorder() instanceof PaletteToolBarBorder) {
                insets = ((PaletteToolBarBorder) c.getBorder()).getDragInsets(c);
            } else {
                insets = c.getInsets();
            }
            isArmed = calculateEventOutsideBounds(insets, evt.getX(), evt.getY(), c.getWidth(), c.getHeight());
        }
    }

    private boolean calculateEventOutsideBounds(Insets insets, int evtX, int evtY, int cWidth, int cHeight) {
        boolean xOutsideBounds = evtX < insets.left || evtX > cWidth - insets.right;
        boolean yOutsideBounds = evtY < insets.top || evtY > cHeight - insets.bottom;
        return xOutsideBounds || yOutsideBounds;
    }

    // @FeatureEntryPoint(value = "drag-and-drop-released")
    @Override
    public void mouseReleased(MouseEvent evt) {
        if (!tb.isEnabled()) {
            return;
        }
        if (isDragging == true) {
            Point position = evt.getPoint();
            if (origin == null) {
                origin = evt.getComponent().getLocationOnScreen();
            }
            callBack.floatAt(position, position);
        }
        origin = null;
        isDragging = false;
    }

    // @FeatureEntryPoint(value = "drag-and-drop-dragged")
    @Override
    public void mouseDragged(MouseEvent evt) {
        if (!tb.isEnabled()) {
            return;
        }
        if (!isArmed) {
            return;
        }
        isDragging = true;
        Point position = evt.getPoint();
        if (origin == null) {
            origin = evt.getComponent().getLocationOnScreen();
        }
        callBack.dragTo(position, position);
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
    }

    @Override
    public void mouseExited(MouseEvent evt) {
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
    }

    // PropertyChangeListener
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("lookAndFeel".equals(propertyName)) {
            toolBar.updateUI();
        } else if ("orientation".equals(propertyName)) {
            searchForSeparatorAndSetOrientation((Integer) evt.getNewValue());
        }
    }

    private void searchForSeparatorAndSetOrientation(Integer orientation) {
        Component[] components = toolBar.getComponents();
        JToolBar.Separator separator;
        for (int i = 0; i < components.length; ++i) {
            if (!(components[i] instanceof JToolBar.Separator))
                continue;
            separator = (JToolBar.Separator) components[i];
            setSeparatorOrientation(separator, orientation);
        }
    }

    private void setSeparatorOrientation(JToolBar.Separator separator, Integer oldOrientation) {
        Dimension size = separator.getSeparatorSize();
        boolean sizeNotSquare = size != null && size.width != size.height;

        int newOrientation;

        if (sizeNotSquare) {
            newOrientation = oldOrientation;
        } else if (oldOrientation == JToolBar.HORIZONTAL) {
            newOrientation = JToolBar.VERTICAL;
        } else {
            newOrientation = JToolBar.HORIZONTAL;
        }
        separator.setOrientation(newOrientation);
    }

    interface PaletteToolbarHandlerCallback {
        void floatAt(Point position, Point origin);
        void dragTo(Point position, Point origin);
        void focusGained(int index);
    }
}