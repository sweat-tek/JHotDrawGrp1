package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public abstract class AbstractGroupingAction extends AbstractSelectedAction {

    protected CompositeFigure compositeFigure;
    protected DrawingView view;
    ResourceBundleUtil labels;

    /**
     * This Abstract class can be extended into a Group og Ungroup action
     */
    public AbstractGroupingAction(DrawingEditor editor) {
        super(editor);
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        view = getView();
    }



    protected int selectionCount() {
        assert view !=null;

        if (view != null) {
            return view.getSelectionCount();
        }
        return 0;
    }

    public void setView(DrawingView view) {
        this.view = view;
    }

    public void generateUndo(CompositeFigure group, LinkedList<Figure> figures) {
        fireUndoableEditHappened(new groupingUndoableEdit(figures, group));
    }

    abstract void undoAction(Collection<Figure> figures, CompositeFigure group);
    abstract void redoAction(Collection<Figure> figures, CompositeFigure group);

    private class groupingUndoableEdit extends AbstractUndoableEdit {

        private CompositeFigure group;
        private Collection<Figure> figures;
        private static final long serialVersionUID = 1L;

        public groupingUndoableEdit(Collection<Figure> figures, CompositeFigure group) {
            this.group = group;
            this.figures = figures;
        }

        @Override
        public String getPresentationName() {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            return labels.getString("edit.groupSelection.text");
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            redoAction(figures, group);
        }

        @Override
        public void undo() throws CannotUndoException {
            undoAction(figures, group);
            super.undo();
        }

        @Override
        public boolean addEdit(UndoableEdit anEdit) {
            return super.addEdit(anEdit);
        }
    }
}
