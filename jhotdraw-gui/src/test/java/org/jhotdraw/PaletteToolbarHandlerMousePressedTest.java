package org.jhotdraw;

import javax.swing.JToolBar;
import javax.swing.plaf.InsetsUIResource;

import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler;
import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler.PaletteToolbarHandlerCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.awt.event.*;
import java.awt.*;

@RunWith(MockitoJUnitRunner.class)
public class PaletteToolbarHandlerMousePressedTest {

    @Mock
    JToolBar toolbar;
    
    @Mock
    PaletteToolbarHandlerCallback callback;

    @Mock
    MouseEvent event;

    PaletteToolbarHandler handler; 

    @Test
    public void OutsideLeft(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(1);
        when(event.getY()).thenReturn(3);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertTrue("",handler.isArmed());
    }

    @Test
    public void OutsideRight(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(4);
        when(event.getY()).thenReturn(3);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertTrue("",handler.isArmed());
    }

    @Test
    public void OutsideTop(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(3);
        when(event.getY()).thenReturn(1);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertTrue("",handler.isArmed());
    }

    @Test
    public void OutsideBottom(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(3);
        when(event.getY()).thenReturn(4);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertTrue("",handler.isArmed());
    }

    @Test
    public void InsideLeft(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(2);
        when(event.getY()).thenReturn(3);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertFalse("",handler.isArmed());
    }

    @Test
    public void InsideRight(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(3);
        when(event.getY()).thenReturn(3);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertFalse("",handler.isArmed());
    }

    @Test
    public void InsideTop(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(3);
        when(event.getY()).thenReturn(2);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertFalse("",handler.isArmed());
    }

    @Test
    public void InsideBottom(){
        Insets insets = new InsetsUIResource(1, 1, 1, 1);
    
        when(toolbar.isEnabled()).thenReturn(true);
        when(toolbar.getWidth()).thenReturn(5);
        when(toolbar.getHeight()).thenReturn(5);
        when(toolbar.getBorder()).thenReturn(null);
        when(toolbar.getInsets()).thenReturn(insets);
        when(event.getSource()).thenReturn(toolbar);
        when(event.getX()).thenReturn(3);
        when(event.getY()).thenReturn(3);

        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.mousePressed(event);

        assertFalse("",handler.isArmed());
    }
    
}
