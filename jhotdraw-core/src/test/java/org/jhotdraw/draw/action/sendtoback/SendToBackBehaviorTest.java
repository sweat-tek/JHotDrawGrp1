package org.jhotdraw.draw.action.sendtoback;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.draw.action.GivenFigureSelected;
import org.junit.Test;

public class SendToBackBehaviorTest extends ScenarioTest<GivenFigureSelected, WhenFigureIsSentToBack, ThenFigureIsInBack> {
    @Test
    public void figureIsSentToBack(){
        given().figureIsSelected();
        when().figureIsSentToBack();
        then().figureIsInBack();
    }
}
