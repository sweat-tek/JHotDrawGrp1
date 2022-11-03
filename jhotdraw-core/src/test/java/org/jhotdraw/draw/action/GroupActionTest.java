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


import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;


public class GroupActionTest {

    GroupAction groupAction;
    DrawingEditor drawingEditorMock = mock(DrawingEditor.class);
    CompositeFigure groupFigure;
    Drawing drawing = mock((Drawing.class));
    DrawingView drawingView = mock(DrawingView.class);

    @Before
    public void setUp() throws Exception {
        groupAction = new GroupAction(drawingEditorMock);
        groupAction.setView(drawingView);
    }

    @After
    public void tearDown() throws Exception {
        groupAction = null;
    }
/*
    @Test
    public void canGroup() {

        // get no of selected figures
        int numberOfSelectedFigures = groupAction.selectionCount();

        // get cangroup
        boolean canGroup = groupAction.canGroup();

        // assert that if number of selected figures i more than 1, then method returns true.
        assertTrue("Number of selected figures lower than 1", numberOfSelectedFigures > 1 && canGroup);
    }

 */


    // Assert that composite figure that you pass has more figures after call
    // groupFigures(CompositeFigure group, Collection<Figure> figures)
    @Test
    public void groupFiguresTest() {

        // create a composite figure
        groupFigure = new GroupFigure();

        // create list of two mocked figures
        List<Figure> figures = new LinkedList<>();
        Figure figure1 = mock(Figure.class);
        Figure figure2 = mock(Figure.class);
        figures.add(figure1);
        figures.add(figure2);


        assertEquals(0, groupFigure.getChildCount());

        // Adding behaviour to mocks
        when(drawingView.getDrawing()).thenReturn(drawing);
        when(drawing.sort(anyCollection())).thenReturn(figures);
        when(drawing.indexOf(figures.iterator().next())).thenReturn(0);
        doNothing().when(drawing).basicRemoveAll(anyCollection());
        doNothing().when(drawingView).clearSelection();
        doNothing().when(drawing).add(anyInt(), anyObject());
        doNothing().when(figure1).willChange();
        doNothing().when(figure2).willChange();
        doNothing().when(drawingView).addToSelection((Figure) any());
        when(figure1.getDrawingArea(anyDouble())).thenReturn(mock(Rectangle2D.Double.class));
        when(figure2.getDrawingArea(anyDouble())).thenReturn(mock(Rectangle2D.Double.class));

        // grouping figures
        groupAction.groupFigures(groupFigure, figures);

        // asseting the CompositeFigure has to childrena and the composite figure is added to selection
        assertEquals(2, groupFigure.getChildCount());
        verify(drawingView, times(1)).addToSelection((CompositeFigure) anyObject());
    }

    @Test
    public void testJunit() {
        int x = 0;
        assertTrue("not true", x==0);
    }
}