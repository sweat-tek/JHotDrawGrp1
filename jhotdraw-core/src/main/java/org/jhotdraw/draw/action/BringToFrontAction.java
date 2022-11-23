/*
 * @(#)BringToFrontAction.java
 *
 * Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;

import java.util.Collection;
import java.util.LinkedList;

/**
 * ToFrontAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class BringToFrontAction extends AbstractArrangeAction{

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.bringToFront";
    private SendToBackAction sendToBackAction;

    @Override
    String getID() {
        return ID;
    }

    /**
     * Creates a new instance.
     */
    public BringToFrontAction(DrawingEditor editor) {
        super(editor);
    }

    public void setSendToBackAction(SendToBackAction sendToBackAction) {
        this.sendToBackAction = sendToBackAction;
    }

    //@FeatureEntryPoint(value = "BringToFront")
    @Override
    public void action(DrawingView view, Collection<Figure> figures) {
        assert view != null;
        assert figures != null;

        bringToFront(view, figures);
    }

    private void bringToFront(DrawingView view, Collection<Figure> figures) {
        assert view != null;
        assert figures != null;

        if (figures.isEmpty()){
            return;
        }

        Drawing drawing = view.getDrawing();
        for (Figure figure : drawing.sort(figures)) {
            drawing.bringToFront(figure);
        }
    }

    @Override
    public void redoAction(DrawingView view, LinkedList<Figure> figures) {
        assert view != null;
        assert figures != null;

        bringToFront(view, figures);
    }

    @Override
    public void undoAction(DrawingView view, LinkedList<Figure> figures) {
        assert view != null;
        assert figures != null;

        if (sendToBackAction == null) {
            sendToBackAction = new SendToBackAction(getEditor());
        }
        sendToBackAction.action(view, figures);
    }
}
