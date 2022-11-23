package org.jhotdraw.draw.action;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SendToBackActionTest {

    @Mock
    private DrawingEditor drawingEditor;

    @Mock
    private Drawing drawing;

    @Mock
    private DrawingView drawingView;

    @Mock
    private BringToFrontAction bringToFrontAction;

    private SendToBackAction sendToBackAction;

    @Before
    public void setUp() {
        sendToBackAction = new SendToBackAction(drawingEditor);
        sendToBackAction.setBringToFrontAction(bringToFrontAction);
    }

    @Test
    public void sendToBackOneFigure() {
        Figure figure = Mockito.mock(Figure.class);
        List<Figure> figureCollection = new ArrayList<Figure>(){{
            add(figure);
        }};

        when(drawingView.getDrawing()).thenReturn(drawing);
        doNothing().when(drawing).sendToBack(any(Figure.class));

        sendToBackAction.action(drawingView, figureCollection);

        verify(drawingView, times(1)).getDrawing();
        verify(drawing, times(1)).sendToBack(any(Figure.class));

        verifyNoMoreInteractions(drawingView);
        verifyNoMoreInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void sendToBackNullFigureInput() {
        sendToBackAction.action(drawingView, null);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void sendToBackNullDrawingViewInput() {
        Figure figure = Mockito.mock(Figure.class);
        List<Figure> figureCollection = new ArrayList<Figure>(){{
            add(figure);
        }};

        sendToBackAction.action(null, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test
    public void sendToBackMultipleFigures() {
        Figure figure = Mockito.mock(Figure.class);
        Figure figure2 = Mockito.mock(Figure.class);
        List<Figure> figureCollection = new ArrayList<Figure>(){{
            add(figure);
            add(figure2);
        }};

        when(drawingView.getDrawing()).thenReturn(drawing);
        doNothing().when(drawing).sendToBack(any(Figure.class));

        sendToBackAction.action(drawingView, figureCollection);

        verify(drawingView, times(1)).getDrawing();
        verify(drawing, times(2)).sendToBack(any(Figure.class));

        verifyNoMoreInteractions(drawingView);
        verifyNoMoreInteractions(drawing);
    }

    @Test
    public void sendToBackEmpty() {
        List<Figure> figureCollection = new ArrayList<>();

        sendToBackAction.action(drawingView, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test
    public void redoAction() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};
        when(drawingView.getDrawing()).thenReturn(drawing);
        doNothing().when(drawing).sendToBack(any(Figure.class));

        sendToBackAction.redoAction(drawingView, figureCollection);

        verify(drawingView, times(1)).getDrawing();
        verify(drawing, times(1)).sendToBack(any(Figure.class));

        verifyNoMoreInteractions(drawingView);
        verifyNoMoreInteractions(drawing);
    }

    @Test
    public void undoAction() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};

        doNothing().when(bringToFrontAction).action(any(DrawingView.class), anyList());

        sendToBackAction.undoAction(drawingView, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }


    @Test(expected = AssertionError.class)
    public void undoActionNullDrawingView() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};

        sendToBackAction.undoAction(null, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void undoActionNullFigureCollection() {
        sendToBackAction.undoAction(drawingView, null);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void redoActionNullDrawingView() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};

        sendToBackAction.redoAction(null, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void redoActionNullFigureCollection() {
        sendToBackAction.redoAction(drawingView, null);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }
}