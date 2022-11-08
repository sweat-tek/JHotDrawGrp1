package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThenGrouped extends Stage<ThenGrouped> {

    @ExpectedScenarioState
    DrawingEditor drawingEditor;

    public ThenGrouped figuresAreGrouped() {
        // assert only one Figure in the drawing view
        int figureCount = drawingEditor.getActiveView().getDrawing().getChildren().size();
        assertEquals(1, figureCount);

        // assert the selected figure is a GroupFigure
        Figure selectedFigure = drawingEditor.getActiveView().getSelectedFigures().iterator().next();
        assertTrue(selectedFigure instanceof GroupFigure);

        return this;
    }
}
