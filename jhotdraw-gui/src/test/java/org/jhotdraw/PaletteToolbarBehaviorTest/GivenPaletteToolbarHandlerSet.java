package org.jhotdraw.PaletteToolbarBehaviorTest;

import java.awt.Point;

import org.jhotdraw.gui.JDisclosureToolBar;
import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler;
import org.jhotdraw.gui.plaf.palette.PaletteToolbarHandler.PaletteToolbarHandlerCallback;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenPaletteToolbarHandlerSet extends Stage<GivenPaletteToolbarHandlerSet>{


    @ProvidedScenarioState
    PaletteToolbarHandler handler;

    @ProvidedScenarioState
    CallbackReturn callbackReturn;

    
    public GivenPaletteToolbarHandlerSet paletteToolbarHandlerSet(){

        callbackReturn = new CallbackReturn();

        handler = new PaletteToolbarHandler( new JDisclosureToolBar(), new PaletteToolbarHandlerCallback(){

            @Override
            public void floatAt(Point position, Point origin) {
                callbackReturn.floatAtCall++;
                callbackReturn.FloatAtOrigin = origin;
                callbackReturn.FloatAtPosition = position;
            }

            @Override
            public void dragTo(Point position, Point origin) {
                callbackReturn.dragToCall++;
                callbackReturn.DragToOrigin = origin;
                callbackReturn.DragToPosition = position;
            }

            @Override
            public void focusGained(int index) {}});

        return self();
    }
}
