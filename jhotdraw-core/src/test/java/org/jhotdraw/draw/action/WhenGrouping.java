package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;
import java.util.Set;

public class WhenGrouping extends Stage<WhenGrouping> {

    @ExpectedScenarioState
    @ProvidedScenarioState
    DrawingEditor drawingEditor;

    @ProvidedScenarioState
    List<Figure> figures;

    @ProvidedScenarioState
    Set<Figure> selectedFigures;

    private GroupAction groupAction;

    @BeforeStage
    public void before() {
        groupAction = new GroupAction(drawingEditor);
        selectedFigures = drawingEditor.getActiveView().getSelectedFigures();
        figures = drawingEditor.getActiveView().getDrawing().getChildren();

    }

    WhenGrouping groupingFigures() {
        groupAction.actionPerformed(null);
        return this;
    }
}
