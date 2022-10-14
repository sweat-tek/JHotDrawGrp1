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

    @FeatureEntryPoint(value = "SendToBack")
    @Override
    void action(DrawingView view, Collection<Figure> figures) {
        sendToBack(view, figures);
    }

    public static void sendToBack(DrawingView view, Collection<Figure> figures) {
        Drawing drawing = view.getDrawing();
        for (Figure figure : figures) {
            drawing.sendToBack(figure);
        }
    }

    @Override
    public void redoAction(DrawingView view, LinkedList<Figure> figures) {
        SendToBackAction.sendToBack(view, figures);
    }

    @Override
    public void undoAction(DrawingView view, LinkedList<Figure> figures) {
        BringToFrontAction.bringToFront(view, figures);
    }
}
