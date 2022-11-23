package org.jhotdraw.JDisclosureToolbarBehaviorTest;

import org.junit.Test;

import com.tngtech.jgiven.junit.ScenarioTest;

public class JDisclosureToolbarBehaviorTest extends ScenarioTest<GivenToolbarShown, WhenToolbarButtonPressed, ThenToolbarHidden>{


    @Test
    public void ToolbarIsHidden(){
        given().toolbarShown();
        when().toolbarButtonPressed();
        then().toolbarHidden();
    }
}
