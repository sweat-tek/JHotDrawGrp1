package org.jhotdraw.JDisclosureToolbarBehaviorTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jhotdraw.gui.DisclosureIcon;
import org.jhotdraw.gui.JDisclosureToolBar;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenToolbarShown extends Stage<GivenToolbarShown>{

    @ProvidedScenarioState
    JDisclosureToolBar toolbar;

    @ProvidedScenarioState
    JButton button = null;


    public GivenToolbarShown toolbarShown(){

        JPanel jpanel = new JPanel();

        toolbar = new JDisclosureToolBar();

        jpanel.add(toolbar);
        
        Component[] components = toolbar.getComponents();

        for(int x = 0 ; x < components.length ; x++){
            if(components[x] instanceof JButton){
                button = (JButton) components[x];
            }
        }

        assertNotNull(button);

        Object val = button.getClientProperty(DisclosureIcon.CURRENT_STATE_PROPERTY);

        assertEquals(1, val);

        return self();
    }
}
