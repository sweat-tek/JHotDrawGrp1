package org.jhotdraw.samples.draw.app;

import org.aspectj.lang.annotation.Before;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.Disposable;
import org.jhotdraw.api.app.View;
import org.jhotdraw.api.gui.URIChooser;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.*;

public class AbstractApplicationTest{

    @Test
    public void createMenuBarTest(){
        AbstractApplication x = new AbstractApplication() {
            @Override
            protected ActionMap createViewActionMap(View p) {
                return null;
            }

            @Override
            public void show(View v) {

            }

            @Override
            public void hide(View v) {

            }

            @Override
            public boolean isSharingToolsAmongViews() {
                return false;
            }

            @Override
            public Component getComponent() {
                return null;
            }

            @Override
            public JMenu createFileMenu(View v) {
                return null;
            }

            @Override
            public JMenu createEditMenu(View v) {
                return null;
            }

            @Override
            public JMenu createViewMenu(View v) {
                return null;
            }

            @Override
            public JMenu createWindowMenu(View v) {
                return null;
            }

            @Override
            public JMenu createHelpMenu(View v) {
                return null;
            }
        };

        View v = new View() {
            @Override
            public Application getApplication() {
                return null;
            }

            @Override
            public void setApplication(Application newValue) {

            }

            @Override
            public JComponent getComponent() {
                return null;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void setEnabled(boolean newValue) {

            }

            @Override
            public void clear() {

            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean hasUnsavedChanges() {
                return false;
            }

            @Override
            public void markChangesAsSaved() {

            }

            @Override
            public void execute(Runnable worker) {

            }

            @Override
            public void init() {

            }

            @Override
            public void start() {

            }

            @Override
            public void activate() {

            }

            @Override
            public void deactivate() {

            }

            @Override
            public void stop() {

            }

            @Override
            public void dispose() {

            }

            @Override
            public ActionMap getActionMap() {
                return null;
            }

            @Override
            public void setActionMap(ActionMap m) {

            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener l) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener l) {

            }

            @Override
            public void setMultipleOpenId(int newValue) {

            }

            @Override
            public int getMultipleOpenId() {
                return 0;
            }

            @Override
            public boolean isShowing() {
                return false;
            }

            @Override
            public void setShowing(boolean newValue) {

            }

            @Override
            public void setTitle(String newValue) {

            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public void addDisposable(Disposable disposable) {

            }

            @Override
            public void removeDisposable(Disposable disposable) {

            }

            @Override
            public URI getURI() {
                return null;
            }

            @Override
            public void setURI(URI newValue) {

            }

            @Override
            public boolean canSaveTo(URI uri) {
                return false;
            }

            @Override
            public void write(URI uri, URIChooser chooser) throws IOException {

            }

            @Override
            public void read(URI uri, URIChooser chooser) throws IOException {

            }
        };

        //assertTrue(x.createMenuBar(v) != null);

    }

}