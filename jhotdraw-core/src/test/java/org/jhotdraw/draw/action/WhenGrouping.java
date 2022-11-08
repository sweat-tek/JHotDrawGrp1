package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DrawingEditor;

public class WhenGrouping extends Stage<WhenGrouping> {

    @ExpectedScenarioState
    @ProvidedScenarioState
    DrawingEditor drawingEditor;

    private GroupAction groupAction;

    @BeforeStage
    public void before() {
        groupAction = new GroupAction(drawingEditor);
    }

    WhenGrouping groupingFigures() {
        groupAction.actionPerformed(null);
        return this;
    }
}
