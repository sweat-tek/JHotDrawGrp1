package org.jhotdraw.draw.action.sendtoback;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.assertj.core.api.Condition;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenFigureIsInBack extends Stage<ThenFigureIsInBack> {

    @ExpectedScenarioState
    private DrawingEditor editor;

    @ExpectedScenarioState
    private Set<Figure> selectedFigures;

    public ThenFigureIsInBack figureIsInBack() {
        //Only one figure is selected
        assertThat(editor.getActiveView().getSelectionCount()).isEqualTo(1);

        Figure selected = editor.getActiveView().getSelectedFigures().iterator().next();
        assertThat(selected).is(selected());

        List<Figure> figures = editor.getActiveView().getDrawing().getChildren();

        //Figure should be in the front of the list as it will be drawn first, thereby in back
        Figure figure = figures.get(0);

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
