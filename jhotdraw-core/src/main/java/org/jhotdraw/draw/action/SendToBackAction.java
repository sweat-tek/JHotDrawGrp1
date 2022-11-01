/*
 * @(#)SendToBackAction.java
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
 * SendToBackAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SendToBackAction extends AbstractArrangeAction{

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.sendToBack";
    private BringToFrontAction bringToFrontAction;

    @Override
    String getID() {
        return ID;
    }

    /**
     * Creates a new instance.
     */
    public SendToBackAction(DrawingEditor editor) {
        super(editor);
    }

    public void setBringToFrontAction(BringToFrontAction bringToFrontAction) {
        this.bringToFrontAction = bringToFrontAction;
    }

    @FeatureEntryPoint(value = "SendToBack")
    @Override
    void action(DrawingView view, Collection<Figure> figures) {
        assert view != null;
        assert figures != null;

        sendToBack(view, figures);
    }

    private void sendToBack(DrawingView view, Collection<Figure> figures) {
        assert view != null;
        assert figures != null;

        if (figures.isEmpty()){
            return;
        }

        Drawing drawing = view.getDrawing();
        for (Figure figure : figures) {
            drawing.sendToBack(figure);
        }
    }

    @Override
    public void redoAction(DrawingView view, LinkedList<Figure> figures) {
        assert view != null;
        assert figures != null;

        sendToBack(view, figures);
    }

    @Override
    public void undoAction(DrawingView view, LinkedList<Figure> figures) {
        assert view != null;
        assert figures != null;

        if (bringToFrontAction == null) {
            bringToFrontAction = new BringToFrontAction(getEditor());
        }
        bringToFrontAction.action(view, figures);
    }
}
