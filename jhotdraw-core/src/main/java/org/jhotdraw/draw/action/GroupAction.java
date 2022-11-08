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

import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

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
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
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

    @FeatureEntryPoint(value = "GroupAction")
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        assert view != null;
        if (canGroup()) {
            LinkedList<Figure> ungroupedFigures = new LinkedList<>(view.getSelectedFigures());
            CompositeFigure group = (CompositeFigure) compositeFigure.clone();
            groupFigures(group, ungroupedFigures);
            generateUndo(group, ungroupedFigures);
        }
    }

    @Override
    void undoAction(Collection<Figure> figures, CompositeFigure group) {
        ungroupFigures(group);
    }

    @Override
    void redoAction(Collection<Figure> figures, CompositeFigure group) {
        groupFigures(group, figures);
    }
}
