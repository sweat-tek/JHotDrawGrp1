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
public class GroupAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.groupSelection";
    private CompositeFigure prototype;

    public final DrawingView view = getView();

    /**
     * If this variable is true, this action groups figures.
     * If this variable is false, this action ungroups figures.
     */
    private boolean isGroupingAction;

    public GroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure(), true);
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype) {
        this(editor, prototype, true);
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype, boolean isGroupingAction) {
        super(editor);
        this.prototype = prototype;
        this.isGroupingAction = isGroupingAction;
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        if (view != null) {
            setEnabled(isGroupingAction ? canGroup() : canUngroup());
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
                ResourceBundleUtil labels
                        = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
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

    public Collection<Figure> ungroupFigures(CompositeFigure group) {
// XXX - This code is redundant with UngroupAction
        LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }

    public void groupFigures(CompositeFigure group, Collection<Figure> figures) {
        Collection<Figure> sorted = view.getDrawing().sort(figures);
        int index = view.getDrawing().indexOf(sorted.iterator().next());
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(index, group);
        group.willChange();
        for (Figure f : sorted) {
            f.willChange();
            group.basicAdd(f);
        }
        group.changed();
        view.addToSelection(group);
    }
}
