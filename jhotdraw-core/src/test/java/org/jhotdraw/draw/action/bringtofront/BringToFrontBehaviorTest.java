package org.jhotdraw.draw.action.bringtofront;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.action.GivenFigureSelected;
import org.junit.Test;

public class BringToFrontBehaviorTest extends ScenarioTest<GivenFigureSelected, WhenFigureIsBroughtToFront, ThenFigureIsInFront> {

    @Test
    public void figureIsBringToFront(){
        given().aFigureIsSelectedAndSomeAreUnselected();
        when().figureIsBroughtToFront();
        then().figureIsInFront();
    }
}
