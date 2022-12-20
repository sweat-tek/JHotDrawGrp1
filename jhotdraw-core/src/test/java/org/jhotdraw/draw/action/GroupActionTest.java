package org.jhotdraw.draw.action;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;

// test framework imports
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

// import java classes
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class GroupActionTest {

    GroupAction groupAction;
    CompositeFigure groupFigure;
    DrawingEditor drawingEditorMock = mock(DrawingEditor.class);
    Drawing drawingMock = mock((Drawing.class));
    DrawingView drawingViewMock = mock(DrawingView.class);

    @Before
    public void setUp() throws Exception {
        groupAction = new GroupAction(drawingEditorMock);
        groupAction.setView(drawingViewMock);
    }

    @After
    public void tearDown() throws Exception {
        groupAction = null;
    }

    @Test
    public void groupingFiguresTest() {

        groupFigure = new GroupFigure();
        List<Figure> figures = getTwoMockedFigures();
        addBehaviourToMocks(figures);

        assertEquals(0, groupFigure.getChildCount());
        GroupAction.groupFigures(groupFigure, figures, drawingViewMock);
        assertEquals("CompositeFigure not having two children", 2, groupFigure.getChildCount());
        verify(drawingViewMock, times(1)).addToSelection((CompositeFigure) anyObject());
    }

    private void addBehaviourToMocks(List<Figure> figures) {
        // drawingView Mock
        when(drawingViewMock.getDrawing()).thenReturn(drawingMock);
        doNothing().when(drawingViewMock).clearSelection();
        doNothing().when(drawingViewMock).addToSelection((Figure) any());

        // drawing Mock
        when(drawingMock.sort(anyCollection())).thenReturn(figures);
        when(drawingMock.indexOf(figures.iterator().next())).thenReturn(0);
        doNothing().when(drawingMock).basicRemoveAll(anyCollection());
        doNothing().when(drawingMock).add(anyInt(), any());
    }

    private List<Figure> getTwoMockedFigures() {
        // Generate mock of two figures + behaviour
        Figure figure1 = mock(Figure.class);
        Figure figure2 = mock(Figure.class);
        when(figure1.getDrawingArea(anyDouble())).thenReturn(mock(Rectangle2D.Double.class));
        when(figure2.getDrawingArea(anyDouble())).thenReturn(mock(Rectangle2D.Double.class));
        doNothing().when(figure1).willChange();
        doNothing().when(figure2).willChange();

        // adding them to list
        List<Figure> figures = new LinkedList<>();
        figures.add(figure1);
        figures.add(figure2);

        return figures;
    }

    // boundary case
    @Test
    public void testGroupingWithSelectionOfOneFigure() {
        when(drawingViewMock.getSelectionCount()).thenReturn(1);
        assertFalse("grouping requires two figures selected", groupAction.canGroup());
    }
}