package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import java.util.*;
import org.jhotdraw.draw.*;

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

    abstract void generateUndo(CompositeFigure group, LinkedList<Figure> figures);
}
