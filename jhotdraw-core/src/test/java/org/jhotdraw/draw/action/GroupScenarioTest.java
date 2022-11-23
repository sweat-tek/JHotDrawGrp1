package org.jhotdraw.draw.action;

// test framework imports
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class GroupScenarioTest extends ScenarioTest<GivenFigures, WhenGrouping, ThenGrouped> {

    @Test
    public void Group_two_selected_figures_into_one() {
        given().twoRandomSelectedFigures();
        when().groupingFigures();
        then().figuresAreGrouped();
    }
}
