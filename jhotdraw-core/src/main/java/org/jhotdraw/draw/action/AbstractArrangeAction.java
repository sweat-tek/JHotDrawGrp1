package org.jhotdraw.draw.action;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.Collection;
import java.util.LinkedList;

public abstract class AbstractArrangeAction extends AbstractSelectedAction {
    abstract String getID();

    /**
     * Creates a new instance.
     */
    public AbstractArrangeAction(DrawingEditor editor) {
        super(editor);
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, getID());
        updateEnabledState();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        action(view, figures);
        fireUndoableEditHappened(createUndoableEdit(view, figures));
    }

    abstract void action(DrawingView view, Collection<Figure> figures);

    abstract void redoAction(DrawingView view, LinkedList<Figure> figures);

    abstract void undoAction(DrawingView view, LinkedList<Figure> figures);

    private AbstractUndoableEdit createUndoableEdit(DrawingView view, LinkedList<Figure> figures){
        return new AbstractUndoableEdit(){
            @Override
            public String getPresentationName() {
                return super.getPresentationName();
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                undoAction(view, figures);
            }

            @Override
            public void redo() throws CannotRedoException {
                redoAction(view, figures);
            }
        };
    }
}
