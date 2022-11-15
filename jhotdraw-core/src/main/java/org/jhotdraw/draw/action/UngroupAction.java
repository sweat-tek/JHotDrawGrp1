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

import java.util.Collection;
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
    private GroupAction groupAction;

    public UngroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public UngroupAction(DrawingEditor editor, CompositeFigure compositeFigure) {
        super(editor);
        this.compositeFigure = compositeFigure;
        labels.configureAction(this, ID);
        updateEnabledState();
        groupAction = new GroupAction(editor, compositeFigure);
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
    void undoAction(Collection<Figure> figures, CompositeFigure group) {
        groupAction.groupFigures(group, figures);
    }

    @Override
    void redoAction(Collection<Figure> figures, CompositeFigure group) {
        ungroupFigures(group);
    }

    public Collection<Figure> ungroupFigures(CompositeFigure group) {
        LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }
}
