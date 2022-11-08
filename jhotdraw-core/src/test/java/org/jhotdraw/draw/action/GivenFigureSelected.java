package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.DiamondFigure;
import org.jhotdraw.draw.figure.Figure;

public class GivenFigureSelected extends Stage<GivenFigureSelected> {
    @ProvidedScenarioState
    DrawingEditor drawingEditor;

    @BeforeStage
    private void before() {
        drawingEditor = new DefaultDrawingEditor();
        DrawingView view = new DefaultDrawingView();
        view.setDrawing(new QuadTreeDrawing());
        drawingEditor.setActiveView(view);
    }


    public GivenFigureSelected aFigureIsSelectedAndSomeAreUnselected(){
        Figure figure1 = new DiamondFigure();
        Figure figure2 = new DiamondFigure();
        Figure figure3 = new DiamondFigure();

        drawingEditor.getActiveView().getDrawing().add(figure1);
        drawingEditor.getActiveView().getDrawing().add(figure2);
        drawingEditor.getActiveView().getDrawing().add(figure3);

        drawingEditor.getActiveView().addToSelection(figure1);

        return self();
    }
}
