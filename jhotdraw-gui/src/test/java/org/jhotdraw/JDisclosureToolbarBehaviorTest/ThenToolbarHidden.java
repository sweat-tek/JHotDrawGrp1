package org.jhotdraw.JDisclosureToolbarBehaviorTest;

import static org.junit.Assert.assertEquals;


import javax.swing.JButton;

import org.jhotdraw.gui.DisclosureIcon;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

public class ThenToolbarHidden extends Stage<ThenToolbarHidden>{

    @ExpectedScenarioState
    JButton button;

    public ThenToolbarHidden toolbarHidden(){
        
        Object val = button.getClientProperty(DisclosureIcon.CURRENT_STATE_PROPERTY);

        assertEquals(0, val);

        return self();
    }   
}
