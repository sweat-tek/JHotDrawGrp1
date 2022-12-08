package org.jhotdraw.draw.action.bringtofront;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.assertj.core.api.Condition;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenFigureIsInFront extends Stage<ThenFigureIsInFront> {

    @ExpectedScenarioState
    private DrawingEditor editor;

    @ExpectedScenarioState
    private Set<Figure> selectedFigures;

    @ExpectedScenarioState
    private Set<Figure> nonselectedFigures;

    public ThenFigureIsInFront onlySelectedFigureIsInFront() {
        //Only one figure is selected
        assertThat(editor.getActiveView().getSelectionCount()).isEqualTo(1);

        Figure selected = editor.getActiveView().getSelectedFigures().iterator().next();
        assertThat(selected).is(selected());

        List<Figure> figures = editor.getActiveView().getDrawing().getChildren();

        //Figure should be the last in the list as it will be drawn last, thereby in the front
        Figure figure = figures.get(figures.size()-1);

        assertThat(figure).isSameAs(selected); //It should be the same object

        return self();
    }

    private Condition<Figure> selected(){
        return new Condition<Figure>(){
            @Override
            public boolean matches(Figure value) {
                return selectedFigures.contains(value);
            }
        };
    }

}
