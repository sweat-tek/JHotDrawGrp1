package org.jhotdraw.samples.draw;

import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.Disposable;
import org.jhotdraw.api.app.View;
import org.jhotdraw.api.gui.URIChooser;
import org.jhotdraw.samples.draw.app.AbstractApplication;
import org.testng.annotations.Test;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertTrue;


public class MainTest {

    @Test

    public void testMain() {
        String[] args = {};
        Main.main(args);


    }

}