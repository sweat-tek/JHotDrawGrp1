package org.jhotdraw.PaletteToolbarBehaviorTest;

import static org.junit.Assert.assertEquals;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import java.awt.Point;

public class ThenHandlerCallbacksAreCalled extends Stage<ThenHandlerCallbacksAreCalled>{
    
    @ExpectedScenarioState
    CallbackReturn callbackReturn;

    public ThenHandlerCallbacksAreCalled handlerCallbacksAreCalled(){

        assertEquals(callbackReturn.dragToCall,  1);

        assertEquals(callbackReturn.DragToOrigin,   new Point(0,0));

        assertEquals(callbackReturn.DragToPosition,   new Point(1,1));


        assertEquals(callbackReturn.floatAtCall,  1);

        assertEquals(callbackReturn.FloatAtOrigin, new Point(0,0));

        assertEquals(callbackReturn.FloatAtPosition,   new Point(2,2));

        return self();
    }
}
