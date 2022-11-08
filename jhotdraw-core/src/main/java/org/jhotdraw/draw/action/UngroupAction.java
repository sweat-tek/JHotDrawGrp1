/*
 * @(#)UngroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

import java.util.LinkedList;

/**
 * UngroupAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class UngroupAction extends AbstractGroupingAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.ungroupSelection";

    public UngroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public UngroupAction(DrawingEditor editor, CompositeFigure compositeFigure) {
        super(editor);
        this.compositeFigure = compositeFigure;
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        if (view != null) {
            setEnabled(canUngroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canUngroup() {
        return selectionCount() == 1 && isCompositeFigure();
    }

    private boolean isCompositeFigure() {
        if (compositeFigure == null) {
            return false;
        }
        return view.getSelectedFigures().iterator().next().getClass().equals(compositeFigure.getClass());
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (canUngroup()) {
            CompositeFigure group = (CompositeFigure) view.getSelectedFigures().iterator().next();
            LinkedList<Figure> ungroupedFigures = new LinkedList<>();
            ungroupedFigures.addAll(ungroupFigures(group));
            generateUndo(group, ungroupedFigures);
        }
    }

    @Override
    void undoAction(LinkedList<Figure> figures, CompositeFigure group) {
        groupFigures(group, figures);
    }

    @Override
    void redoAction(LinkedList<Figure> figures, CompositeFigure group) {
        ungroupFigures(group);
    }
}
