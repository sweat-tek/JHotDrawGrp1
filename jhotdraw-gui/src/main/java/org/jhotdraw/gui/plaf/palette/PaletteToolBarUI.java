/**
 * @(#)PaletteToolBarUI.java
 *
 * Copyright (c) 2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.gui.plaf.palette;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;

import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler.PaletteToolbarHandlerCallback;

/**
 * ToolBarUI for palette components.
 * <p>
 * This UI differs from BasicToolBarUI, in that the component holding the
 * toolbar
 * is supposed to use BoxLayout instead of BorderLayout. This allows to have
 * multiple toolbars in the same component. The toolbars can be reorderd in the
 * component, but they are not allowed to float in their own floating window.
 * <p>
 * The JToolBar starts dragging only, if the drag starts over the insets of
 * its border.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class PaletteToolBarUI extends ToolBarUI implements SwingConstants {

    private static final boolean IS_FLOATING_ALLOWED = false;
    protected JToolBar toolBar;
    private boolean floating;
    private int floatingX;
    private int floatingY;
    private RootPaneContainer floatingToolBar;
    protected DragWindow dragWindow;
    private Container dockingSource;
    protected int focusedCompIndex = -1;
    protected Color dockingColor = null;
    protected Color floatingColor = null;
    protected Color dockingBorderColor = null;
    protected Color floatingBorderColor = null;
    protected MouseInputListener dockingListener;
    protected PropertyChangeListener propertyListener;
    protected ContainerListener toolBarContListener;
    protected FocusListener toolBarFocusListener;
    private HandlerFactory handlerFactory;
    protected Integer constraintBeforeFloating = 0;
    // Rollover button implementation.
    private static String IS_ROLLOVER = "JToolBar.isRollover";
    /* private */ static String IS_DIVIDER_DRAWN = "Palette.ToolBar.isDividerDrawn";
    // client properties
    /* The value of this client property must be an Icon or null. */
    public static final String TOOLBAR_ICON_PROPERTY = "Palette.ToolBar.icon";
    /*
     * The value of this client property must be an Integer or null, if it is null,
     * the value 2 is used.
     */
    public static final String TOOLBAR_TEXT_ICON_GAP_PROPERTY = "Palette.ToolBar.textIconGap";
    /*
     * The value of this client property must be an Insets object or null, if it is
     * null, the insets of the toolbar border are used
     */
    public static final String TOOLBAR_INSETS_OVERRIDE_PROPERTY = "Palette.ToolBar.insetsOverride";
    private static Border rolloverBorder;
    private static Border nonRolloverBorder;
    private static Border nonRolloverToggleBorder;
    private boolean rolloverBorders = false;
    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no
     * longer used.
     * Key bindings are now defined by the LookAndFeel, please refer to
     * the key bindings specification for further details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @Deprecated
    protected KeyStroke upKey;
    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no
     * longer used.
     * Key bindings are now defined by the LookAndFeel, please refer to
     * the key bindings specification for further details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @Deprecated
    protected KeyStroke downKey;
    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no
     * longer used.
     * Key bindings are now defined by the LookAndFeel, please refer to
     * the key bindings specification for further details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @Deprecated
    protected KeyStroke leftKey;
    /**
     * As of Java 2 platform v1.3 this previously undocumented field is no
     * longer used.
     * Key bindings are now defined by the LookAndFeel, please refer to
     * the key bindings specification for further details.
     *
     * @deprecated As of Java 2 platform v1.3.
     */
    @Deprecated
    protected KeyStroke rightKey;
    private static String FOCUSED_COMP_INDEX = "JToolBar.focusedCompIndex";

    public static ComponentUI createUI(JComponent c) {
        return new PaletteToolBarUI();
    }

    @Override
    public void installUI(JComponent c) {
        toolBar = (JToolBar) c;
        // Set defaults
        installDefaults();
        installComponents();
        installListeners();
        installKeyboardActions();
        floating = false;
        floatingX = floatingY = 0;
        floatingToolBar = null;
        setOrientation(toolBar.getOrientation());
        LookAndFeel.installProperty(c, "opaque", Boolean.TRUE);
        if (c.getClientProperty(FOCUSED_COMP_INDEX) != null) {
            focusedCompIndex = ((Integer) (c.getClientProperty(FOCUSED_COMP_INDEX)));
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        // Clear defaults
        uninstallDefaults();
        uninstallComponents();
        uninstallListeners();
        uninstallKeyboardActions();
        // Clear instance vars
        if (isFloating() == true) {
            setFloating(false, null);
        }
        floatingToolBar = null;
        dragWindow = null;
        dockingSource = null;
        c.putClientProperty(FOCUSED_COMP_INDEX, focusedCompIndex);
    }

    protected void installDefaults() {
        PaletteLookAndFeel.installBorder(toolBar, "ToolBar.border");
        PaletteLookAndFeel.installColorsAndFont(toolBar,
                "ToolBar.background",
                "ToolBar.foreground",
                "ToolBar.font");
        // Toolbar specific defaults
        if (dockingColor == null || dockingColor instanceof UIResource) {
            dockingColor = UIManager.getColor("ToolBar.dockingBackground");
        }
        if (floatingColor == null || floatingColor instanceof UIResource) {
            floatingColor = UIManager.getColor("ToolBar.floatingBackground");
        }
        if (dockingBorderColor == null
                || dockingBorderColor instanceof UIResource) {
            dockingBorderColor = UIManager.getColor("ToolBar.dockingForeground");
        }
        if (floatingBorderColor == null
                || floatingBorderColor instanceof UIResource) {
            floatingBorderColor = UIManager.getColor("ToolBar.floatingForeground");
            // ToolBar rollover button borders
        }
        Object rolloverProp = toolBar.getClientProperty(IS_ROLLOVER);
        if (rolloverProp == null) {
            rolloverProp = UIManager.get("ToolBar.isRollover");
        }
        if (rolloverProp != null) {
            rolloverBorders = ((Boolean) rolloverProp);
        }
        if (rolloverBorder == null) {
            rolloverBorder = createRolloverBorder();
        }
        if (nonRolloverBorder == null) {
            nonRolloverBorder = createNonRolloverBorder();
        }
        if (nonRolloverToggleBorder == null) {
            nonRolloverToggleBorder = createNonRolloverToggleBorder();
        }
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(toolBar);
        dockingColor = null;
        floatingColor = null;
        dockingBorderColor = null;
        floatingBorderColor = null;
        rolloverBorder = null;
        nonRolloverBorder = null;
        nonRolloverToggleBorder = null;
    }

    protected void installComponents() {
    }

    protected void uninstallComponents() {
    }

    protected void installListeners() {
        handlerFactory = new HandlerFactory();
        dockingListener = handlerFactory.getDockingListener();
        if (dockingListener != null) {
            toolBar.addMouseMotionListener(dockingListener);
            toolBar.addMouseListener(dockingListener);
        }
        propertyListener = handlerFactory.getPropertyListener();
        if (propertyListener != null) {
            toolBar.addPropertyChangeListener(propertyListener);
        }
        toolBarContListener = handlerFactory.getContainerListener();
        if (toolBarContListener != null) {
            toolBar.addContainerListener(toolBarContListener);
        }
        toolBarFocusListener = handlerFactory.getFocusListener();
        if (toolBarFocusListener != null) {
            // Put focus listener on all components in toolbar
            Component[] components = toolBar.getComponents();
            for (int i = 0; i < components.length; ++i) {
                components[i].addFocusListener(toolBarFocusListener);
            }
        }
    }

    protected void uninstallListeners() {
        if (dockingListener != null) {
            toolBar.removeMouseMotionListener(dockingListener);
            toolBar.removeMouseListener(dockingListener);
            dockingListener = null;
        }
        if (propertyListener != null) {
            toolBar.removePropertyChangeListener(propertyListener);
            propertyListener = null; // removed in setFloating
        }
        if (toolBarContListener != null) {
            toolBar.removeContainerListener(toolBarContListener);
            toolBarContListener = null;
        }
        if (toolBarFocusListener != null) {
            // Remove focus listener from all components in toolbar
            Component[] components = toolBar.getComponents();
            for (int i = 0; i < components.length; ++i) {
                components[i].removeFocusListener(toolBarFocusListener);
            }
            toolBarFocusListener = null;
        }
        handlerFactory = null;
    }

    protected void installKeyboardActions() {
        InputMap km = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        SwingUtilities.replaceUIInputMap(toolBar, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                km);
        PaletteLazyActionMap.installLazyActionMap(toolBar, PaletteToolBarUI.class,
                "ToolBar.actionMap");
    }

    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
            return (InputMap) PaletteLookAndFeel.getInstance().get(
                    "ToolBar.ancestorInputMap");
        }
        return null;
    }

    static void loadActionMap(PaletteLazyActionMap map) {
        map.put(new Actions(Actions.NAVIGATE_RIGHT));
        map.put(new Actions(Actions.NAVIGATE_LEFT));
        map.put(new Actions(Actions.NAVIGATE_UP));
        map.put(new Actions(Actions.NAVIGATE_DOWN));
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(toolBar, null);
        SwingUtilities.replaceUIInputMap(toolBar, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                null);
    }

    protected void navigateFocusedComp(int direction) {
        int nComp = toolBar.getComponentCount();
        int j;
        switch (direction) {
            case EAST:
            case SOUTH:
                if (focusedCompIndex < 0 || focusedCompIndex >= nComp) {
                    break;
                }
                j = focusedCompIndex + 1;
                while (j != focusedCompIndex) {
                    if (j >= nComp) {
                        j = 0;
                    }
                    Component comp = toolBar.getComponentAtIndex(j++);
                    if (comp != null && comp.isFocusable() && comp.isEnabled()) {
                        comp.requestFocus();
                        break;
                    }
                }
                break;
            case WEST:
            case NORTH:
                if (focusedCompIndex < 0 || focusedCompIndex >= nComp) {
                    break;
                }
                j = focusedCompIndex - 1;
                while (j != focusedCompIndex) {
                    if (j < 0) {
                        j = nComp - 1;
                    }
                    Component comp = toolBar.getComponentAtIndex(j--);
                    if (comp != null && comp.isFocusable() && comp.isEnabled()) {
                        comp.requestFocus();
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Creates a rollover border for toolbar components. The
     * rollover border will be installed if rollover borders are
     * enabled.
     * <p>
     * Override this method to provide an alternate rollover border.
     *
     * @since 1.4
     */
    protected Border createRolloverBorder() {
        Object border = UIManager.get("ToolBar.rolloverBorder");
        if (border != null) {
            return (Border) border;
        }
        return new EmptyBorder(0, 0, 0, 0);
    }

    /**
     * Creates the non rollover border for toolbar components. This
     * border will be installed as the border for components added
     * to the toolbar if rollover borders are not enabled.
     * <p>
     * Override this method to provide an alternate rollover border.
     *
     * @since 1.4
     */
    protected Border createNonRolloverBorder() {
        Object border = UIManager.get("ToolBar.nonrolloverBorder");
        if (border != null) {
            return (Border) border;
        }
        return new EmptyBorder(0, 0, 0, 0);
    }

    /**
     * Creates a non rollover border for Toggle buttons in the toolbar.
     */
    private Border createNonRolloverToggleBorder() {
        return new EmptyBorder(0, 0, 0, 0);
    }

    /**
     * No longer used, use PaletteToolBarUI.createFloatingWindow(JToolBar)
     *
     * @see #createFloatingWindow
     */
    protected JFrame createFloatingFrame(JToolBar toolbar) {
        Window window = SwingUtilities.getWindowAncestor(toolbar);
        JFrame frame = new JFrame(toolbar.getName(),
                (window != null) ? window.getGraphicsConfiguration() : null) {
            private static final long serialVersionUID = 1L;

            // Override createRootPane() to automatically resize
            // the frame when contents change
            @Override
            protected JRootPane createRootPane() {
                JRootPane rootPane = new JRootPane() {
                    private static final long serialVersionUID = 1L;
                    private boolean packing = false;

                    @Override
                    public void validate() {
                        super.validate();
                        if (!packing) {
                            packing = true;
                            pack();
                            packing = false;
                        }
                    }
                };
                rootPane.setOpaque(true);
                return rootPane;
            }
        };
        frame.getRootPane().setName("ToolBar.FloatingFrame");
        frame.setResizable(false);
        WindowListener wl = createFrameListener();
        frame.addWindowListener(wl);
        return frame;
    }

    /**
     * Creates a window which contains the toolbar after it has been
     * dragged out from its container
     *
     * @return a <code>RootPaneContainer</code> object, containing the toolbar.
     */
    protected RootPaneContainer createFloatingWindow(JToolBar toolbar) {
        class ToolBarDialog extends JDialog {

            private static final long serialVersionUID = 1L;

            public ToolBarDialog(Frame owner, String title, boolean modal) {
                super(owner, title, modal);
            }

            public ToolBarDialog(Dialog owner, String title, boolean modal) {
                super(owner, title, modal);
            }

            // Override createRootPane() to automatically resize
            // the frame when contents change
            @Override
            protected JRootPane createRootPane() {
                JRootPane rootPane = new JRootPane() {
                    private static final long serialVersionUID = 1L;
                    private boolean packing = false;

                    @Override
                    public void validate() {
                        super.validate();
                        if (!packing) {
                            packing = true;
                            pack();
                            packing = false;
                        }
                    }
                };
                rootPane.setOpaque(true);
                return rootPane;
            }
        }
        JDialog dialog;
        Window window = SwingUtilities.getWindowAncestor(toolbar);
        if (window instanceof Frame) {
            dialog = new ToolBarDialog((Frame) window, toolbar.getName(), false);
        } else if (window instanceof Dialog) {
            dialog = new ToolBarDialog((Dialog) window, toolbar.getName(), false);
        } else {
            dialog = new ToolBarDialog((Frame) null, toolbar.getName(), false);
        }
        dialog.getRootPane().setName("ToolBar.FloatingWindow");
        dialog.setTitle(toolbar.getName());
        dialog.setResizable(false);
        WindowListener wl = createFrameListener();
        dialog.addWindowListener(wl);
        return dialog;
    }

    protected DragWindow createDragWindow(JToolBar toolbar) {
        Window frame = null;
        if (toolBar != null) {
            Container p;
            for (p = toolBar.getParent(); p != null && !(p instanceof Window); p = p.getParent()) {
            }
            frame = (Window) p;
        }
        if (floatingToolBar == null) {
            floatingToolBar = createFloatingWindow(toolBar);
        }
        if (floatingToolBar instanceof Window) {
            frame = (Window) floatingToolBar;
        }
        DragWindow w = new DragWindow(frame);
        JRootPane rp = ((RootPaneContainer) w).getRootPane();
        rp.putClientProperty("Window.alpha", 0.6f);
        return w;
    }

    /**
     * Returns a flag to determine whether rollover button borders
     * are enabled.
     *
     * @return true if rollover borders are enabled; false otherwise
     * @see #setRolloverBorders
     * @since 1.4
     */
    public boolean isRolloverBorders() {
        return rolloverBorders;
    }

    public void setFloatingLocation(int x, int y) {
        floatingX = x;
        floatingY = y;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean b, Point p) {
        if (toolBar.isFloatable() == true) {
            if (dragWindow != null) {
                dragWindow.setVisible(false);
            }
            this.floating = b;
            if (b && IS_FLOATING_ALLOWED) {
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                    dockingSource.remove(toolBar);
                }
                constraintBeforeFloating = calculateConstraint();
                if (propertyListener != null) {
                    UIManager.addPropertyChangeListener(propertyListener);
                }
                if (floatingToolBar == null) {
                    floatingToolBar = createFloatingWindow(toolBar);
                }
                floatingToolBar.getContentPane().add(toolBar, BorderLayout.CENTER);
                if (floatingToolBar instanceof Window) {
                    ((Window) floatingToolBar).pack();
                }
                if (floatingToolBar instanceof Window) {
                    ((Window) floatingToolBar).setLocation(floatingX, floatingY);
                }
                if (floatingToolBar instanceof Window) {
                    ((Window) floatingToolBar).setVisible(true);
                }
            } else {
                if (floatingToolBar == null) {
                    floatingToolBar = createFloatingWindow(toolBar);
                }
                if (floatingToolBar instanceof Window) {
                    ((Window) floatingToolBar).setVisible(false);
                }
                floatingToolBar.getContentPane().remove(toolBar);
                Integer constraint = getDockingConstraint(dockingSource, p);
                if (constraint == null) {
                    constraint = 0;
                }
                int orientation = mapConstraintToOrientation(constraint);
                setOrientation(orientation);
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                }
                if (propertyListener != null) {
                    UIManager.removePropertyChangeListener(propertyListener);
                }
                dockingSource.add(toolBar, constraint.intValue());
            }
            dockingSource.invalidate();
            Container dockingSourceParent = dockingSource.getParent();
            if (dockingSourceParent != null) {
                dockingSourceParent.validate();
            }
            dockingSource.repaint();
        }
    }

    private int mapConstraintToOrientation(Object constraint) {
        int orientation = toolBar.getOrientation();
        if (constraint != null) {
            if (constraint.equals(BorderLayout.EAST) || constraint.equals(BorderLayout.WEST)) {
                orientation = JToolBar.VERTICAL;
            } else if (constraint.equals(BorderLayout.NORTH) || constraint.equals(BorderLayout.SOUTH)) {
                orientation = JToolBar.HORIZONTAL;
            }
        }
        return orientation;
    }

    public void setOrientation(int orientation) {
        toolBar.setOrientation(orientation);
        if (dragWindow != null) {
            dragWindow.setOrientation(orientation);
        }
    }

    /**
     * Gets the color displayed when over a docking area
     */
    public Color getDockingColor() {
        return dockingColor;
    }

    /**
     * Sets the color displayed when over a docking area
     */
    public void setDockingColor(Color c) {
        this.dockingColor = c;
    }

    /**
     * Gets the color displayed when over a floating area
     */
    public Color getFloatingColor() {
        return floatingColor;
    }

    /**
     * Sets the color displayed when over a floating area
     */
    public void setFloatingColor(Color c) {
        this.floatingColor = c;
    }

    public boolean canDock(Component c, Point p) {
        return (p != null && getDockingConstraint(c, p) != null);
    }

    private Integer calculateConstraint() {
        Integer constraint = null;
        LayoutManager lm = dockingSource.getLayout();
        if (lm instanceof BoxLayout) {
            for (int i = 0, n = dockingSource.getComponentCount(); i < n; i++) {
                if (dockingSource.getComponent(i) == toolBar) {
                    constraint = i;
                    break;
                }
            }
        }
        return (constraint != null) ? constraint : constraintBeforeFloating;
    }

    private Integer getDockingConstraint(Component c, Point p) {
        if (p == null) {
            return constraintBeforeFloating;
        }
        if (c.contains(p)) {
            for (int i = 0, n = dockingSource.getComponentCount(); i < n; i++) {
                Component child = dockingSource.getComponent(i);
                Point childP = new Point(p.x - child.getX(), p.y - child.getY());
                if (child.contains(childP)) {
                    return Math.min(n - 1, (childP.x <= child.getWidth()) ? i : i + 1);
                }
            }
            if (dockingSource.getComponentCount() == 0
                    || p.x < dockingSource.getComponent(0).getX()) {
                return 0;
            }
            return dockingSource.getComponentCount() - 1;
        }
        return null;
    }

    protected void dragTo(Point position, Point origin) {
        if (toolBar.isFloatable() == true) {
            try {
                if (dragWindow == null) {
                    dragWindow = createDragWindow(toolBar);
                }
                Point offset = dragWindow.getOffset();
                if (offset == null) {
                    // Dimension size = toolBar.getPreferredSize();
                    Dimension size = toolBar.getSize();
                    offset = new Point(size.width / 2, size.height / 2);
                    dragWindow.setOffset(offset);
                }
                Point global = new Point(origin.x + position.x,
                        origin.y + position.y);
                Point dragPoint = new Point(global.x - offset.x,
                        global.y - offset.y);
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                }
                constraintBeforeFloating = calculateConstraint();
                Point dockingPosition = dockingSource.getLocationOnScreen();
                Point comparisonPoint = new Point(global.x - dockingPosition.x,
                        global.y - dockingPosition.y);
                if (canDock(dockingSource, comparisonPoint)) {
                    dragWindow.setBackground(getDockingColor());
                    Object constraint = getDockingConstraint(dockingSource,
                            comparisonPoint);
                    int orientation = mapConstraintToOrientation(constraint);
                    dragWindow.setOrientation(orientation);
                    dragWindow.setBorderColor(dockingBorderColor);
                } else {
                    dragWindow.setBackground(getFloatingColor());
                    dragWindow.setBorderColor(floatingBorderColor);
                }
                dragWindow.setLocation(dragPoint.x, dragPoint.y);
                if (dragWindow.isVisible() == false) {
                    // Dimension size = toolBar.getPreferredSize();
                    Dimension size = toolBar.getSize();
                    dragWindow.setSize(size.width, size.height);
                    dragWindow.setVisible(true);
                }
            } catch (IllegalComponentStateException e) {
                // allowed empty
            }
        }
    }

    protected void floatAt(Point position, Point origin) {
        if (toolBar.isFloatable() == true) {
            try {
                Point offset = dragWindow.getOffset();
                if (offset == null) {
                    offset = position;
                    dragWindow.setOffset(offset);
                }
                Point global = new Point(origin.x + position.x,
                        origin.y + position.y);
                setFloatingLocation(global.x - offset.x,
                        global.y - offset.y);
                if (dockingSource != null) {
                    Point dockingPosition = dockingSource.getLocationOnScreen();
                    Point comparisonPoint = new Point(global.x - dockingPosition.x,
                            global.y - dockingPosition.y);
                    if (canDock(dockingSource, comparisonPoint)) {
                        setFloating(false, comparisonPoint);
                    } else {
                        setFloating(true, null);
                    }
                } else {
                    setFloating(true, null);
                }
                dragWindow.setOffset(null);
            } catch (IllegalComponentStateException e) {
                // allowed empty
            }
        }
    }

    private class HandlerFactory {
        PaletteToolbarHandler handler = new PaletteToolbarHandler(toolBar, new PaletteToolbarHandlerCallback() {

            @Override
            public void floatAt(Point position, Point origin) {
                floatAt(position, origin);

            }

            @Override
            public void dragTo(Point position, Point origin) {
                dragTo(position, origin);

            }

            @Override
            public void focusGained(int index) {
                focusedCompIndex = index;
            }

        });

        public PaletteToolbarHandler getHandler() {
            return handler;
        }

        public ContainerListener getContainerListener() {
            return handler;
        }

        public FocusListener getFocusListener() {
            return handler;
        }

        public PropertyChangeListener getPropertyListener() {
            return handler;
        }

        public MouseInputListener getDockingListener() {
            return handler;
        }
    }

    protected WindowListener createFrameListener() {
        return new FrameListener();
    }

    /**
     * Paints the contents of the window used for dragging.
     *
     * @param g Graphics to paint to.
     * @throws NullPointerException is <code>g</code> is null
     * @since 1.5
     */
    protected void paintDragWindow(Graphics g) {
        int w = dragWindow.getWidth();
        int h = dragWindow.getHeight();
        g.setColor(dragWindow.getBackground());
        g.fillRect(0, 0, w, h);
        boolean wasDoubleBuffered = false;
        if (toolBar.isDoubleBuffered()) {
            wasDoubleBuffered = true;
            toolBar.setDoubleBuffered(false);
        }
        Graphics g2 = g.create();
        toolBar.paintAll(g2);
        g2.dispose();
        g.setColor(dragWindow.getBorderColor());
        g.drawRect(0, 0, w - 1, h - 1);
        if (wasDoubleBuffered) {
            toolBar.setDoubleBuffered(true);
        }
    }

    private static class Actions extends /* UI */ AbstractAction {

        private static final long serialVersionUID = 1L;
        private static final String NAVIGATE_RIGHT = "navigateRight";
        private static final String NAVIGATE_LEFT = "navigateLeft";
        private static final String NAVIGATE_UP = "navigateUp";
        private static final String NAVIGATE_DOWN = "navigateDown";

        public Actions(String name) {
            super(name);
        }

        public String getName() {
            return (String) getValue(Action.NAME);
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String key = getName();
            JToolBar toolBar = (JToolBar) evt.getSource();
            PaletteToolBarUI ui = (PaletteToolBarUI) PaletteLookAndFeel.getUIOfType(
                    toolBar.getUI(), PaletteToolBarUI.class);
            if ((NAVIGATE_RIGHT == null && key == null) || (NAVIGATE_RIGHT != null && NAVIGATE_RIGHT.equals(key))) {
                ui.navigateFocusedComp(EAST);
            } else if ((NAVIGATE_LEFT == null && key == null) || (NAVIGATE_LEFT != null && NAVIGATE_LEFT.equals(key))) {
                ui.navigateFocusedComp(WEST);
            } else if ((NAVIGATE_UP == null && key == null) || (NAVIGATE_UP != null && NAVIGATE_UP.equals(key))) {
                ui.navigateFocusedComp(NORTH);
            } else if ((NAVIGATE_DOWN == null && key == null) || (NAVIGATE_DOWN != null && NAVIGATE_DOWN.equals(key))) {
                ui.navigateFocusedComp(SOUTH);
            }
        }
    }

    protected class FrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent w) {
            if (toolBar.isFloatable() == true) {
                if (dragWindow != null) {
                    dragWindow.setVisible(false);
                }
                floating = false;
                if (floatingToolBar == null) {
                    floatingToolBar = createFloatingWindow(toolBar);
                }
                if (floatingToolBar instanceof Window) {
                    ((Window) floatingToolBar).setVisible(false);
                }
                floatingToolBar.getContentPane().remove(toolBar);
                Integer constraint = constraintBeforeFloating;
                if (dockingSource == null) {
                    dockingSource = toolBar.getParent();
                }
                if (propertyListener != null) {
                    UIManager.removePropertyChangeListener(propertyListener);
                }
                dockingSource.add(toolBar, constraint.intValue());
                dockingSource.invalidate();
                Container dockingSourceParent = dockingSource.getParent();
                if (dockingSourceParent != null) {
                    dockingSourceParent.validate();
                }
                dockingSource.repaint();
            }
        }
    }

    protected class ToolBarContListener implements ContainerListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        @Override
        public void componentAdded(ContainerEvent e) {
            handlerFactory.getContainerListener().componentAdded(e);
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
            handlerFactory.getContainerListener().componentRemoved(e);
        }
    }

    protected class ToolBarFocusListener implements FocusListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        @Override
        public void focusGained(FocusEvent e) {
            handlerFactory.getFocusListener().focusGained(e);
        }

        @Override
        public void focusLost(FocusEvent e) {
            handlerFactory.getFocusListener().focusLost(e);
        }
    }

    protected class PropertyListener implements PropertyChangeListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            handlerFactory.getPropertyListener().propertyChange(e);
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of PaletteToolBarUI.
     */
    public class DockingListener implements MouseInputListener {

        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this
        // class calls into the Handler.
        protected JToolBar toolBar;
        protected boolean isDragging = false;
        protected Point origin = null;

        public DockingListener(JToolBar t) {
            this.toolBar = t;
            handlerFactory.getHandler().setToolbar(toolBar);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            handlerFactory.getHandler().setToolbar(toolBar);
            handlerFactory.getDockingListener().mouseClicked(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            handlerFactory.getHandler().setToolbar(toolBar);
            handlerFactory.getDockingListener().mousePressed(e);
            isDragging = handlerFactory.getHandler().isDragging();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            handlerFactory.getHandler().setToolbar(toolBar);
            handlerFactory.getHandler().setIsDragging(isDragging);
            handlerFactory.getHandler().setOrigin(origin);
            handlerFactory.getHandler().mouseReleased(e);
            isDragging = handlerFactory.getHandler().isDragging();
            origin = handlerFactory.getHandler().getOrigin();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            handlerFactory.getHandler().mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            handlerFactory.getHandler().mouseExited(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            handlerFactory.getHandler().setToolbar(toolBar);
            handlerFactory.getHandler().setOrigin(origin);
            handlerFactory.getHandler().mouseDragged(e);
            isDragging = handlerFactory.getHandler().isDragging();
            origin = handlerFactory.getHandler().getOrigin();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            handlerFactory.getHandler().mouseMoved(e);
        }
    }

    protected class DragWindow extends JWindow {

        private static final long serialVersionUID = 1L;
        Color borderColor = Color.gray;
        int orientation = toolBar.getOrientation();
        Point offset; // offset of the mouse cursor inside the DragWindow

        DragWindow(Window w) {
            super(w);
            getContentPane().add(new JPanel() {
                private static final long serialVersionUID = 1L;

                @Override
                public void paintComponent(Graphics g) {
                    paintDragWindow(g);
                }
            });
        }

        public void setOrientation(int o) {
            if (isShowing()) {
                if (o == this.orientation) {
                    return;
                }
                this.orientation = o;
                Dimension size = getSize();
                setSize(new Dimension(size.height, size.width));
                if (offset != null) {
                    if (toolBar.getComponentOrientation().isLeftToRight()) {
                        setOffset(new Point(offset.y, offset.x));
                    } else if (o == JToolBar.HORIZONTAL) {
                        setOffset(new Point(size.height - offset.y, offset.x));
                    } else {
                        setOffset(new Point(offset.y, size.width - offset.x));
                    }
                }
                repaint();
            }
        }

        public Point getOffset() {
            return offset;
        }

        public void setOffset(Point p) {
            this.offset = p;
        }

        public void setBorderColor(Color c) {
            if (this.borderColor == c) {
                return;
            }
            this.borderColor = c;
            repaint();
        }

        public Color getBorderColor() {
            return this.borderColor;
        }

        @Override
        public Insets getInsets() {
            return new Insets(1, 1, 1, 1);
        }
    }
}
