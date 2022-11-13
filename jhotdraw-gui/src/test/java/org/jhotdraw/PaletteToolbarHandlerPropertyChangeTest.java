package org.jhotdraw;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;

import javax.swing.JToolBar;

import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler;
import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler.PaletteToolbarHandlerCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.awt.*;

@RunWith(MockitoJUnitRunner.class)
public class PaletteToolbarHandlerPropertyChangeTest {

    @Mock
    JToolBar toolbar;
    
    @Mock
    PaletteToolbarHandlerCallback callback;

    @Mock
    PropertyChangeEvent event;

    @Mock
    JToolBar.Separator separator;

    PaletteToolbarHandler handler; 


    @Test
    public void SizeNull(){
        Component[] components = new Component[] {separator};
        when(event.getPropertyName()).thenReturn("orientation");
        when(event.getNewValue()).thenReturn(JToolBar.VERTICAL);
        when(toolbar.getComponents()).thenReturn(components);
        when(separator.getSeparatorSize()).thenReturn(null);
        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.propertyChange(event);
        verify(separator).setOrientation(JToolBar.HORIZONTAL);
    }

    @Test
    public void SizeNotSquare(){

        Component[] components = new Component[] {separator};
        when(event.getPropertyName()).thenReturn("orientation");
        when(event.getNewValue()).thenReturn(JToolBar.VERTICAL);
        when(toolbar.getComponents()).thenReturn(components);
        when(separator.getSeparatorSize()).thenReturn(new Dimension(0,1));
        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.propertyChange(event);
        verify(separator).setOrientation(JToolBar.VERTICAL);

    }
    
    @Test
    public void SizeSquareVertical(){
        Component[] components = new Component[] {separator};
        when(event.getPropertyName()).thenReturn("orientation");
        when(event.getNewValue()).thenReturn(JToolBar.VERTICAL);
        when(toolbar.getComponents()).thenReturn(components);
        when(separator.getSeparatorSize()).thenReturn(new Dimension(0,0));
        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.propertyChange(event);
        verify(separator).setOrientation(JToolBar.HORIZONTAL);
    }

    @Test
    public void SizeSquareHorizontal(){
        Component[] components = new Component[] {separator};
        when(event.getPropertyName()).thenReturn("orientation");
        when(event.getNewValue()).thenReturn(JToolBar.HORIZONTAL);
        when(toolbar.getComponents()).thenReturn(components);
        when(separator.getSeparatorSize()).thenReturn(new Dimension(0,0));
        handler = new PaletteToolbarHandler(toolbar, callback); 
        handler.propertyChange(event);
        verify(separator).setOrientation(JToolBar.VERTICAL);
    }
    
}
