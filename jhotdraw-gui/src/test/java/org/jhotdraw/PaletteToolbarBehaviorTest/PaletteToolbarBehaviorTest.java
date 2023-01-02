package org.jhotdraw.PaletteToolbarBehaviorTest;

import org.junit.Test;

import com.tngtech.jgiven.junit.ScenarioTest;

public class PaletteToolbarBehaviorTest extends ScenarioTest<GivenPaletteToolbarHandlerSet, WhenActionCallbacksCalledInOrder, ThenHandlerCallbacksAreCalled>{

    @Test
    public void ToolbarIsDraggedToNewLocation(){
        given().paletteToolbarHandlerSet();
        when().actionCallbacksCalledInOrder();
        then().handlerCallbacksAreCalled();
    }
}
