package org.jhotdraw.draw.action;

// import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
// import org.jhotdraw.draw.figure.GroupFigure;

import java.util.*;
// import javax.swing.undo.*;

import org.jhotdraw.draw.*;
// import org.jhotdraw.util.ResourceBundleUtil;

public abstract class AbstractGrouping extends AbstractSelectedAction {

    protected CompositeFigure prototype;
    public final DrawingView view = getView();

    /**
     * Creates an action which acts on the selected figures on the current view
     * of the specified editor.
     *
     * @param editor
     */
    public AbstractGrouping(DrawingEditor editor) {
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
}
