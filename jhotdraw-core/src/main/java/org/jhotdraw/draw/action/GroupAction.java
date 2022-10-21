/*
 * @(#)GroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.GroupFigure;

import java.util.*;
import javax.swing.undo.*;

import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * GroupAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class GroupAction extends AbstractGrouping {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.groupSelection";


    /**
     * If this variable is true, this action groups figures.
     * If this variable is false, this action ungroups figures.
     */
    private boolean isGroupingAction = true;

    public GroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype) {
        super(editor);
        this.prototype = prototype;
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        if (view != null) {
            setEnabled(canGroup());
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

    @FeatureEntryPoint(value = "GroupAction")
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (isGroupingAction && canGroup()) {
            LinkedList<Figure> ungroupedFigures = new LinkedList<>(view.getSelectedFigures());
            CompositeFigure group = (CompositeFigure) prototype.clone();
            UndoableEdit groupUndoableEdit = getGroupUnduableEdit(ungroupedFigures, group);
            groupFigures(group, ungroupedFigures);
            fireUndoableEditHappened(groupUndoableEdit);
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
