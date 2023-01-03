package org.jhotdraw.draw.action.bringtofront;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.BringToFrontAction;
import org.jhotdraw.draw.figure.Figure;

import java.util.HashSet;
import java.util.Set;

public class WhenFigureIsBroughtToFront extends Stage<WhenFigureIsBroughtToFront> {

    @ScenarioState
    private BringToFrontAction bringToFrontAction;

    @ExpectedScenarioState
    @ProvidedScenarioState
    private DrawingEditor editor;

    @ProvidedScenarioState
    private Set<Figure> selectedFigures;

    @BeforeStage
    public void before() {
        bringToFrontAction = new BringToFrontAction(editor);

        selectedFigures = new HashSet<>(editor.getActiveView().getSelectedFigures());
    }

    public WhenFigureIsBroughtToFront bringingFiguresToTheFront(){
        bringToFrontAction.action(editor.getActiveView(), selectedFigures);

        return self();
    }
}
