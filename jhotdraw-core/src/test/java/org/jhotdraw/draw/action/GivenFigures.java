package org.jhotdraw.draw.action;

import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.BezierFigure;
import org.jhotdraw.draw.figure.DiamondFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;

public class GivenFigures {

    @ProvidedScenarioState
    DrawingEditor drawingEditor;

    @BeforeStage
    void before() {
        drawingEditor = new DefaultDrawingEditor();
        DrawingView drawingView = new DefaultDrawingView();
        Drawing drawing = new DefaultDrawing();

        drawingView.setDrawing(drawing);
        drawingEditor.setActiveView(drawingView);
    }

    public GivenFigures twoRandomSelectedFigures() {
        Figure figure1 = new DiamondFigure();
        Figure figure2 = new RectangleFigure();
        drawingEditor.getActiveView().getDrawing().add(figure1);
        drawingEditor.getActiveView().getDrawing().add(figure2);
        drawingEditor.getActiveView().addToSelection(figure1);
        drawingEditor.getActiveView().addToSelection(figure2);
        return this;
    }
}
