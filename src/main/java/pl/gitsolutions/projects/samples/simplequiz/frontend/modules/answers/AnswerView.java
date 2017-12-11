package pl.gitsolutions.projects.samples.simplequiz.frontend.modules.answers;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gitsolutions.projects.samples.simplequiz.backend.dto.TrackInfoDto;
import pl.gitsolutions.projects.samples.simplequiz.backend.integration.AnswerGateway;

/**
 * Created by katgr on 12.11.2017.
 */
public class AnswerView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "answerView";

    private AnswerGrid grid;
    private AnswerForm form;

    @Autowired
    AnswerGateway answerGateway;

    private AnswerViewLogic viewLogic = new AnswerViewLogic(this);

    private AnswerDataProvider dataProvider = new AnswerDataProvider();

//    TODO: do dodania trackform - boczny panel z info o utworze

    public AnswerView() {
        setSizeFull();
        addStyleName("crud-view");
//        HorizontalLayout topLayout = createTopBar();

        grid = new AnswerGrid();
        grid.setDataProvider(dataProvider);
//        grid.setItems(answerGateway.findMyAnswers(1));
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

//        form = new TrackForm(viewLogic,grid);

        VerticalLayout barAndGridLayout = new VerticalLayout();
//        barAndGridLayout.addComponent(topLayout);
        barAndGridLayout.addComponent(grid);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.setExpandRatio(grid, 1);
        barAndGridLayout.setStyleName("crud-main-layout");

        addComponent(barAndGridLayout);
//        addComponent(form);

        viewLogic.init();

    }

    public HorizontalLayout createTopBar(){
        return  null;
    }

    public void editAnswer(TrackInfoDto trackInfoDto) {
        if (trackInfoDto != null) {
//            form.addStyleName("visible");
//            form.setEnabled(true);
        } else {
//            form.removeStyleName("visible");
//            form.setEnabled(false);
        }
//        form.editAnswer(trackInfoDto);
    }
}
