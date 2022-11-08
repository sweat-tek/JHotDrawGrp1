package org.jhotdraw.draw.action.bringtofront;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.action.BringToFrontAction;
import org.jhotdraw.draw.figure.Figure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhenFigureIsBringToFront extends Stage<WhenFigureIsBringToFront> {

    @Autowired
    @ScenarioState
    private BringToFrontAction bringToFrontAction;

    @ExpectedScenarioState
    @ProvidedScenarioState
    private DrawingEditor editor;

    @ProvidedScenarioState
    private Set<Figure> selectedFigures;

    @ProvidedScenarioState
    private Set<Figure> nonselectedFigures;

    @ProvidedScenarioState
    private Set<Figure> childrenFigures;


    @BeforeStage
    public void before() {
        bringToFrontAction = new BringToFrontAction(editor);

        selectedFigures = new HashSet<>(editor.getActiveView().getSelectedFigures());

        nonselectedFigures = new HashSet<>(editor.getActiveView().getDrawing().getChildren());
        nonselectedFigures.removeAll(selectedFigures);
    }

    public WhenFigureIsBringToFront figureIsBringToFront(){
        bringToFrontAction.action(editor.getActiveView(), selectedFigures);

        return self();
    }
}
