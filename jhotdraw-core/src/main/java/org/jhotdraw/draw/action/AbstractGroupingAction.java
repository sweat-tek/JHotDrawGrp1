package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import java.util.*;
import org.jhotdraw.draw.*;

public abstract class AbstractGroupingAction extends AbstractSelectedAction {

    protected CompositeFigure compositeFigure;
    public final DrawingView view = getView();

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

    protected int selectionCount() {
        if (view != null) {
            return view.getSelectionCount();
        }
        return 0;
    }

    abstract void generateUndo(CompositeFigure group, LinkedList<Figure> ungroupedFigures);
}
