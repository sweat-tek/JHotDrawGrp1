package org.jhotdraw.draw.action.sendtoback;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.BringToFrontAction;
import org.jhotdraw.draw.action.SendToBackAction;
import org.jhotdraw.draw.figure.Figure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class WhenFigureIsSentToBack extends Stage<WhenFigureIsSentToBack> {

    @Autowired
    @ScenarioState
    private SendToBackAction sendToBackAction;

    @ExpectedScenarioState
    @ProvidedScenarioState
    private DrawingEditor editor;

    @ProvidedScenarioState
    private Set<Figure> selectedFigures;

    @ProvidedScenarioState
    private Set<Figure> nonselectedFigures;


    @BeforeStage
    public void before() {
        sendToBackAction = new SendToBackAction(editor);

        selectedFigures = new HashSet<>(editor.getActiveView().getSelectedFigures());

        nonselectedFigures = new HashSet<>(editor.getActiveView().getDrawing().getChildren());
        nonselectedFigures.removeAll(selectedFigures);
    }

    public WhenFigureIsSentToBack figureIsSentToBack(){
        sendToBackAction.action(editor.getActiveView(), selectedFigures);

        return self();
    }
}
