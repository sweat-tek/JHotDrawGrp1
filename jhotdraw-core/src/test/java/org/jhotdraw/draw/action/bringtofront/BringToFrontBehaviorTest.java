package org.jhotdraw.draw.action.bringtofront;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.action.GivenFiguresToArrange;
import org.junit.Test;

public class BringToFrontBehaviorTest extends ScenarioTest<GivenFiguresToArrange, WhenFigureIsBroughtToFront, ThenFigureIsInFront> {

    @Test
    public void bringingASelectedFigureToTheFrontResultsInJustThatFigureBeingBroughtToTheFront(){
        given().aSelectedFigure()
                .and().someUnselectedFigures();

        when().bringingFiguresToTheFront();

        then().onlySelectedFigureIsInFront();
    }
}
