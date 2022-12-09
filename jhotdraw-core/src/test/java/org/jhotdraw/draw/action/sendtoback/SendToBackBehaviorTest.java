package org.jhotdraw.draw.action.sendtoback;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.action.GivenFiguresToArrange;
import org.junit.Test;

public class SendToBackBehaviorTest extends ScenarioTest<GivenFiguresToArrange, WhenFigureIsSentToBack, ThenFigureIsInBack> {
    @Test
    public void sendingASelectedFigureToTheBackResultsInJustThatFigureBeingSentToTheBack(){
        given().aSelectedFigure()
                .and().someUnselectedFigures();

        when().sendingFiguresToTheBack();

        then().onlySelectedFigureIsInBack();
    }
}
