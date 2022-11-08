package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThenGrouped extends Stage<ThenGrouped> {

    @ExpectedScenarioState
    DrawingEditor drawingEditor;

    @ExpectedScenarioState
    Set<Figure> selectedFigures;

    @ExpectedScenarioState
    List<Figure> figures;

    public ThenGrouped figuresGrouped() {
        // assert only one child in the drawing view (the two children has been grouped)
        int figureCount = drawingEditor.getActiveView().getDrawing().getChildren().size();
        assertEquals(1, figureCount);
        // verify size mathes figures from ExpectedScenarioState
        assertEquals(figureCount, figures.size());

        // assert the selected figure is a GroupFigure
        Figure selectedFigure = drawingEditor.getActiveView().getSelectedFigures().iterator().next();
        assertTrue(selectedFigure instanceof GroupFigure);
        // verify matching the selectedFigures from ExpectedScenarioState
        assertEquals(selectedFigure, selectedFigures.iterator().next());
        return this;
    }
}
