package org.jhotdraw.PaletteToolbarBehaviorTest;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.plaf.InsetsUIResource;

import org.jhotdraw.gui.JDisclosureToolBar;
import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

@RunWith(MockitoJUnitRunner.class)
public class WhenActionCallbacksCalledInOrder extends Stage<WhenActionCallbacksCalledInOrder>{

    @ExpectedScenarioState
    PaletteToolbarHandler handler;

    @ExpectedScenarioState
    @ProvidedScenarioState
    CallbackReturn callbackReturn;

    @Mock
    MouseEvent mousePressedEvent;

    @Mock
    MouseEvent mouseDraggedEvent;

    @Mock
    JDisclosureToolBar toolbar;

    @Mock
    Component component;

    @BeforeScenario
    public void init() {
        MockitoAnnotations.initMocks(this);
        // after initMocks() you can use the standard mockito functionality to mock methods and instances.
    }

    public WhenActionCallbacksCalledInOrder actionCallbacksCalledInOrder(){

        Insets insets = new InsetsUIResource(1, 1, 1, 1);

        org.mockito.Mockito.when(toolbar.isEnabled()).thenReturn(true);
        org.mockito.Mockito.when(toolbar.getWidth()).thenReturn(3);
        org.mockito.Mockito.when(toolbar.getHeight()).thenReturn(3);
        org.mockito.Mockito.when(toolbar.getBorder()).thenReturn(null);
        org.mockito.Mockito.when(toolbar.getInsets()).thenReturn(insets);
        org.mockito.Mockito.when(mousePressedEvent.getSource()).thenReturn(toolbar);
        org.mockito.Mockito.when(mousePressedEvent.getX()).thenReturn(1);
        org.mockito.Mockito.when(mousePressedEvent.getY()).thenReturn(1);

        handler.mousePressed(mousePressedEvent);

        

        org.mockito.Mockito.when(mouseDraggedEvent.getComponent()).thenReturn(component);
        org.mockito.Mockito.when(component.getLocationOnScreen()).thenReturn(new Point(0,0));
        org.mockito.Mockito.when(mouseDraggedEvent.getPoint()).thenReturn(new Point(1,1));

        handler.mouseDragged(mouseDraggedEvent);

        org.mockito.Mockito.when(mouseDraggedEvent.getComponent()).thenReturn(component);
        org.mockito.Mockito.when(component.getLocationOnScreen()).thenReturn(new Point(0,0));
        org.mockito.Mockito.when(mousePressedEvent.getPoint()).thenReturn(new Point(2,2));

        handler.mouseReleased(mousePressedEvent);

        return self();
    }
}
