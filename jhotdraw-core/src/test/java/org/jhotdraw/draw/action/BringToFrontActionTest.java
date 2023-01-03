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
public class BringToFrontActionTest {

    @Mock
    private DrawingEditor drawingEditor;

    @Mock
    private Drawing drawing;

    @Mock
    private DrawingView drawingView;

    @Mock
    private SendToBackAction sendToBackAction;

    private BringToFrontAction bringToFrontAction;

    @Before
    public void setUp() {
        bringToFrontAction = new BringToFrontAction(drawingEditor);
        bringToFrontAction.setSendToBackAction(sendToBackAction);
    }

    @Test
    public void bringToFrontOneFigure() {
        Figure figure = Mockito.mock(Figure.class);
        List<Figure> figureCollection = new ArrayList<Figure>(){{
            add(figure);
        }};

        when(drawingView.getDrawing()).thenReturn(drawing);
        when(drawing.sort(anyCollection())).thenReturn(figureCollection);
        doNothing().when(drawing).bringToFront(any(Figure.class));

        bringToFrontAction.action(drawingView, figureCollection);

        verify(drawingView, times(1)).getDrawing();
        verify(drawing, times(1)).bringToFront(any(Figure.class));
        verify(drawing, times(1)).sort(anyCollection());

        verifyNoMoreInteractions(drawingView);
        verifyNoMoreInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void sendToBackNullFigureInput() {
        bringToFrontAction.action(drawingView, null);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void sendToBackNullDrawingViewInput() {
        Figure figure = Mockito.mock(Figure.class);
        List<Figure> figureCollection = new ArrayList<Figure>(){{
            add(figure);
        }};

        bringToFrontAction.action(null, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test
    public void bringToFrontMultipleFigures() {
        Figure figure = Mockito.mock(Figure.class);
        Figure figure2 = Mockito.mock(Figure.class);
        List<Figure> figureCollection = new ArrayList<Figure>(){{
            add(figure);
            add(figure2);
        }};

        when(drawingView.getDrawing()).thenReturn(drawing);
        when(drawing.sort(anyCollection())).thenReturn(figureCollection);
        doNothing().when(drawing).bringToFront(any(Figure.class));

        bringToFrontAction.action(drawingView, figureCollection);

        verify(drawingView, times(1)).getDrawing();
        verify(drawing, times(2)).bringToFront(any(Figure.class));
        verify(drawing, times(1)).sort(anyCollection());

        verifyNoMoreInteractions(drawingView);
        verifyNoMoreInteractions(drawing);
    }

    @Test
    public void bringToFrontEmpty() {
        List<Figure> figureCollection = new ArrayList<>();

        bringToFrontAction.action(drawingView, figureCollection);

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
        when(drawing.sort(anyCollection())).thenReturn(figureCollection);
        doNothing().when(drawing).bringToFront(any(Figure.class));

        bringToFrontAction.redoAction(drawingView, figureCollection);

        verify(drawingView, times(1)).getDrawing();
        verify(drawing, times(1)).bringToFront(any(Figure.class));
        verify(drawing, times(1)).sort(anyCollection());

        verifyNoMoreInteractions(drawingView);
        verifyNoMoreInteractions(drawing);
    }

    @Test
    public void undoAction() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};

        doNothing().when(sendToBackAction).action(any(DrawingView.class), anyList());

        bringToFrontAction.undoAction(drawingView, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void undoActionNullDrawingView() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};

        bringToFrontAction.undoAction(null, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void undoActionNullFigureCollection() {
        bringToFrontAction.undoAction(drawingView, null);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void redoActionNullDrawingView() {
        Figure figure = Mockito.mock(Figure.class);
        LinkedList<Figure> figureCollection = new LinkedList<Figure>(){{
            add(figure);
        }};

        bringToFrontAction.redoAction(null, figureCollection);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }

    @Test(expected = AssertionError.class)
    public void redoActionNullFigureCollection() {
        bringToFrontAction.redoAction(drawingView, null);

        verifyNoInteractions(drawingView);
        verifyNoInteractions(drawing);
    }
}