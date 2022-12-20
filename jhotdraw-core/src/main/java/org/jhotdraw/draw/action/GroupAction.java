/*
 * @(#)GroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

// import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.GroupFigure;

import java.util.*;

/**
 * GroupAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */

public class GroupAction extends AbstractGroupingAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.groupSelection";

    public GroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public GroupAction(DrawingEditor editor, CompositeFigure compositeFigure) {
        super(editor);
        this.compositeFigure = compositeFigure;
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    protected boolean canGroup() {
        return selectionCount() > 1;
    }

    @Override
    protected void updateEnabledState() {
        if (view != null) {
            setEnabled(canGroup());
        } else {
            setEnabled(false);
        }
    }

    // @FeatureEntryPoint(value = "GroupAction")
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        assert view != null;
        if (canGroup()) {
            LinkedList<Figure> ungroupedFigures = new LinkedList<>(view.getSelectedFigures());
            CompositeFigure group = (CompositeFigure) compositeFigure.clone();
            groupFigures(group, ungroupedFigures, view);
            generateUndo(group, ungroupedFigures);
        }
    }

    public static void groupFigures(CompositeFigure group, Collection<Figure> figures, DrawingView view) {

        // assertions
        assert group != null;
        assert figures.size() > 1;

        // returns a copy of the figures sorted from back to front
        Collection<Figure> sorted = view.getDrawing().sort(figures);
        int index = view.getDrawing().indexOf(sorted.iterator().next());
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();

        // adding the composite figure to the drawing
        view.getDrawing().add(index, group);
        group.willChange();

        // Adding figures to group
        for (Figure f : sorted) {
            f.willChange();
            group.basicAdd(f);
        }
        group.changed();
        view.addToSelection(group);
    }

    @Override
    void undoAction(Collection<Figure> figures, CompositeFigure group) {
        UngroupAction.ungroupFigures(group, view);
    }

    @Override
    void redoAction(Collection<Figure> figures, CompositeFigure group) {
        groupFigures(group, figures, view);
    }
}
