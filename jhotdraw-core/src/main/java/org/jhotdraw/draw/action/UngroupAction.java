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
// import java.awt.event.ActionEvent;
import java.util.LinkedList;

/**
 * UngroupAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class UngroupAction extends AbstractGrouping {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.ungroupSelection";
    private boolean isGroupingAction = false;

    public UngroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
        //ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        //labels.configureAction(this, ID);
        updateEnabledState();
    }

    public UngroupAction(DrawingEditor editor, CompositeFigure prototype) {
        super(editor);
        this.prototype = prototype;
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

    protected boolean canGroup() {
        return selectionCount() > 1;
    }

    protected boolean canUngroup() {
        return selectionCount() == 1 && isCompositeFigure();
    }

    private int selectionCount() {
        if (view != null) {
            return view.getSelectionCount();
        }
        return 0;
    }

    private boolean isCompositeFigure() {
        if (prototype == null) {
            return false;
        }
        return view.getSelectedFigures().iterator().next().getClass().equals(prototype.getClass());
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {

        if (!isGroupingAction && canUngroup()) {
            CompositeFigure group = (CompositeFigure) view.getSelectedFigures().iterator().next();
            LinkedList<Figure> ungroupedFigures = new LinkedList<>();
            UndoableEdit unGroupUndoableEdit = getUnGroupUndoableEdit(group, ungroupedFigures);
            ungroupedFigures.addAll(ungroupFigures(group));
            fireUndoableEditHappened(unGroupUndoableEdit);
        }
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

    private AbstractUndoableEdit getGroupUnduableEdit(LinkedList<Figure> ungroupedFigures, CompositeFigure group) {
        return new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getString("edit.groupSelection.text");
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                groupFigures(group, ungroupedFigures);
            }

            @Override
            public void undo() throws CannotUndoException {
                ungroupFigures(group);
                super.undo();
            }

            @Override
            public boolean addEdit(UndoableEdit anEdit) {
                return super.addEdit(anEdit);
            }
        };
    }
}
