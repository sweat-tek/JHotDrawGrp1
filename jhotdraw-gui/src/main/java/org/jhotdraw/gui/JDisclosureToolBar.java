/**
 * @(#)JDisclosureToolBar.java
 *
 * Copyright (c) 2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.gui;

import java.awt.*;
import javax.swing.*;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.gui.plaf.palette.PaletteToolBarUI;

/**
 * A ToolBar with disclosure functionality.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class JDisclosureToolBar extends JToolBar {

    private static final long serialVersionUID = 1L;
    private JButton disclosureButton;
    public static final String DISCLOSURE_STATE_PROPERTY = "disclosureState";
    public static final String DISCLOSURE_STATE_COUNT_PROPERTY = "disclosureStateCount";

    /**
     * Creates new form.
     */
    public JDisclosureToolBar() {
        setUI(PaletteToolBarUI.createUI(this));
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        disclosureButton = disclosureButton != null ? disclosureButton : createPaletteButton();

        add(disclosureButton, createDisclosureButtonGridConstraints());

        putClientProperty(PaletteToolBarUI.TOOLBAR_INSETS_OVERRIDE_PROPERTY, new Insets(0, 0, 0, 0));

        putClientProperty(PaletteToolBarUI.TOOLBAR_ICON_PROPERTY, new EmptyIcon(10, 8));
    }

    private JButton createPaletteButton(){
        JButton btn = new JButton();
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        btn.setBorderPainted(false);
        btn.setIcon(new DisclosureIcon());
        btn.setOpaque(false);
        btn.putClientProperty(DisclosureIcon.CURRENT_STATE_PROPERTY, 1);
        btn.putClientProperty(DisclosureIcon.STATE_COUNT_PROPERTY, 2);
        btn.addActionListener(e -> onPaletteButtonPressed());
        return btn;
    }

    private void onPaletteButtonPressed(){
        int currentState = (int) disclosureButton.getClientProperty(DisclosureIcon.CURRENT_STATE_PROPERTY);
        int stateCount = (int) disclosureButton.getClientProperty(DisclosureIcon.STATE_COUNT_PROPERTY);
        int newState = calculateNewState(currentState, stateCount);
        setDisclosureState(newState);
    }

    private int calculateNewState(int currentState, int stateCount){
        return (currentState + 1) % stateCount;
    }

    private GridBagConstraints createDisclosureButtonGridConstraints(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 1, 0, 1);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 1d;
        gbc.weightx = 1d;
        return gbc;
    }


    public void setDisclosureStateCount(int newValue) {
        setDisclosureStateProperty(newValue, DisclosureIcon.STATE_COUNT_PROPERTY, DISCLOSURE_STATE_COUNT_PROPERTY);
    }

    private void setDisclosureStateProperty(int newValue, String icon, String property) {
        int oldValue = getDisclosureStateCount();
        disclosureButton.putClientProperty(icon, newValue);
        firePropertyChange(property, oldValue, newValue);
    }

    public void setDisclosureState(int newValue) {
        setDisclosureStateCount(newValue);

        reorganizeDisclosureButton(newValue);

        validateParent();

        repaint();

        setDisclosureStateProperty(newValue, DisclosureIcon.CURRENT_STATE_PROPERTY,DISCLOSURE_STATE_PROPERTY);
    }

    private void reorganizeDisclosureButton(int newValue){
        removeAll();

        JComponent c = getDisclosedComponent(newValue);

        GridBagConstraints gbc = createDisclosureButtonGridConstraints();
        if (c != null) {
            add(c, createDisclosedComponentGridConstraints());
            gbc.weightx = 0d;
        } else {
            gbc.gridx = 1;
        }
        add(disclosureButton, gbc);
    }

    private void validateParent(){
        invalidate();

        Container parent = getParent();

        while (parent.getParent() != null && !parent.getParent().isValid()) {
            parent = parent.getParent();
        }

        parent.validate();
    }

    private GridBagConstraints createDisclosedComponentGridConstraints(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.weightx = 1d;
        gbc.weighty = 1d;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }



    public int getDisclosureStateCount() {
        Integer value = (Integer) disclosureButton.getClientProperty(DisclosureIcon.STATE_COUNT_PROPERTY);
        return (value == null) ? 2 : value;
    }

    public int getDisclosureState() {
        Integer value = (Integer) disclosureButton.getClientProperty(DisclosureIcon.CURRENT_STATE_PROPERTY);
        return (value == null) ? 1 : value;
    }

    protected JComponent getDisclosedComponent(int state) {
        return new JLabel(Integer.toString(state));
    }
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     * /
       // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
