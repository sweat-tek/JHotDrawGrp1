package org.jhotdraw.draw.action;

// test framework imports
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;



public class GroupScenarioTest extends ScenarioTest<GivenFigures, WhenGrouping, ThenGrouped> {

    @Test
    public void Group_two_selected_figures_into_one() {
        given().twoRandomSelectedFigures();
        when().groupFigures();
        then().figuresGrouped();
    }
}
