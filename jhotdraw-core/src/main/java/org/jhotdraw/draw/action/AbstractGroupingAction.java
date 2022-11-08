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
    protected DrawingView view = getView();

    /**
     * This Abstract class can be extended into a Group og Ungroup action
     */
    public AbstractGroupingAction(DrawingEditor editor) {
        super(editor);
    }

    public Collection<Figure> ungroupFigures(CompositeFigure group) {
        LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }

    public void groupFigures(CompositeFigure group, Collection<Figure> figures) {

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

    protected int selectionCount() {
        if (view != null) {
            return view.getSelectionCount();
        }
        return 0;
    }

    public void setView(DrawingView view) {
        this.view = view;
    }

    abstract void undoAction(Collection<Figure> figures, CompositeFigure group);
    abstract void redoAction(Collection<Figure> figures, CompositeFigure group);

    public void generateUndo(CompositeFigure group, LinkedList<Figure> figures) {
        fireUndoableEditHappened(new groupingUndoableEdit(figures, group));
    }

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
