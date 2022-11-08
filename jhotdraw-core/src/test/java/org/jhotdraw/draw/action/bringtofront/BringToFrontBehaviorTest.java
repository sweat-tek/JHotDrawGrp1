package org.jhotdraw.draw.action.bringtofront;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.action.GivenFigureSelected;
import org.junit.Test;

public class BringToFrontBehaviorTest extends ScenarioTest<GivenFigureSelected, WhenFigureIsBringToFront, ThenFigureIsInFront> {

    @Test
    public void figureIsBringToFront(){
        given().figureIsSelected();
        when().figureIsBringToFront();
        then().figureIsInFront();
    }
}
