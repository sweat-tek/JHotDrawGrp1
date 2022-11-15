package org.jhotdraw.JDisclosureToolbarBehaviorTest;

import static org.junit.Assert.assertNotNull;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import org.jhotdraw.gui.JDisclosureToolBar;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class WhenToolbarButtonPressed extends Stage<WhenToolbarButtonPressed>{


    @ExpectedScenarioState
    @ProvidedScenarioState
    JDisclosureToolBar toolbar;

    @ExpectedScenarioState
    @ProvidedScenarioState
    JButton button;

    public WhenToolbarButtonPressed toolbarButtonPressed(){

        button.getActionListeners()[0].actionPerformed(new ActionEvent(button, 0, null));

        return self();
    }
    
}
