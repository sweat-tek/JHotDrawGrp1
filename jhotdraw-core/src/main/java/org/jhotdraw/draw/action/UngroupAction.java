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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
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
    protected void generateUndo(CompositeFigure group, LinkedList<Figure> ungroupedFigures) {
        UndoableEdit unGroupUndoableEdit = getUnGroupUndoableEdit(group, ungroupedFigures);
        fireUndoableEditHappened(unGroupUndoableEdit);
    }

    private AbstractUndoableEdit getUnGroupUndoableEdit(CompositeFigure group, LinkedList<Figure> ungroupedFigures) {
        return new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels
                        = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getString("edit.ungroupSelection.text");
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                ungroupFigures(group);
            }

            @Override
            public void undo() throws CannotUndoException {
                groupFigures(group, ungroupedFigures);
                super.undo();
            }
        };
    }
}
