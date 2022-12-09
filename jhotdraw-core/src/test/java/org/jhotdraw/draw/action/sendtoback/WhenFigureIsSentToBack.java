package org.jhotdraw.draw.action.sendtoback;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.SendToBackAction;
import org.jhotdraw.draw.figure.Figure;

import java.util.HashSet;
import java.util.Set;

public class WhenFigureIsSentToBack extends Stage<WhenFigureIsSentToBack> {

    @ScenarioState
    private SendToBackAction sendToBackAction;

    @ExpectedScenarioState
    @ProvidedScenarioState
    private DrawingEditor editor;

    @ProvidedScenarioState
    private Set<Figure> selectedFigures;


    @BeforeStage
    public void before() {
        sendToBackAction = new SendToBackAction(editor);

        selectedFigures = new HashSet<>(editor.getActiveView().getSelectedFigures());
    }

    public WhenFigureIsSentToBack sendingFiguresToTheBack(){
        sendToBackAction.action(editor.getActiveView(), selectedFigures);

        return self();
    }
}
