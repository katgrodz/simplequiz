package pl.gitsolutions.projects.samples.simplequiz.frontend.modules.task;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import pl.gitsolutions.projects.samples.simplequiz.backend.dto.TrackInfoDto;
import pl.gitsolutions.projects.samples.simplequiz.backend.integration.AnswerGateway;
import pl.gitsolutions.projects.samples.simplequiz.backend.integration.TrackGateway;
import pl.gitsolutions.projects.samples.simplequiz.backend.model.jpa.Answer;
import pl.gitsolutions.projects.samples.simplequiz.backend.model.jpa.Track;

import java.util.List;

/**
 * Created by katgr on 12.11.2017.
 */
public class TaskView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "taskView";

    private Integer currentTrack;
    private String taskDescription = "  Quess the song and artist.";
    private String trackUrl = "";

    private Button tipFirstBtn = new Button("first letters");
    private Label tipFirstText = new Label();
    private Button tipSecondBtn = new Button("different tip");
    private Label tipSecondText = new Label();
    private TextField artistField = new TextField();
    private TextField titleField = new TextField();
    private Label countLbl = new Label();
    private Button sendBtn = new Button("Save");
    private Button prevBtn = new Button("prev");
    private Button nextBtn = new Button("next");

    private TrackInfoDto trackInfo = new TrackInfoDto();

    public TaskView(TrackGateway trackGateway, AnswerGateway answerGateway) {

        if (currentTrack == null) {
            currentTrack = 0;
        }

        List<Track> tracks = trackGateway.tracksInQuiz(1L);
        int tracksCount = tracks.size();

        CustomLayout taskContent = new CustomLayout("taskView");
        taskContent.setStyleName("about-content");

        taskContent.addComponent(
                new Label(VaadinIcons.INFO_CIRCLE.getHtml()
                        + taskDescription, ContentMode.HTML),"task-description");

        setSizeFull();
        setMargin(false);

        countLbl.setValue("1 /  " + tracksCount);
        taskContent.addComponent(countLbl,"task-counter");

        Audio sample = new Audio();
        trackUrl = tracks.get(currentTrack).getTrackUrl();
        final Resource audioResource = new ExternalResource(trackUrl);
        sample.setSource(audioResource);
        sample.setHtmlContentAllowed(true);
        sample.setAutoplay(true);
        sample.setAltText("Can't play media");
        taskContent.addComponent(sample,"task-player");

        taskContent.addComponent(new Label("Artist"), "task-artist-label");
        taskContent.addComponent(artistField,"task-artist-field");
        taskContent.addComponent(new Label("Title"), "task-title-label");
        taskContent.addComponent(titleField,"task-title-field");
        taskContent.addComponent(new Label("Answer"), "task-answer");
        taskContent.addComponent(sendBtn,"task-button1");

        HorizontalLayout navigationPanel = new HorizontalLayout();

        navigationPanel.addComponent(prevBtn);
        navigationPanel.addComponent(nextBtn);
        taskContent.addComponent(navigationPanel,"task-button2");

        HorizontalLayout tipOnePanel = new HorizontalLayout();
        tipOnePanel.addComponent(tipFirstBtn);
        tipOnePanel.addComponent(tipFirstText);
        taskContent.addComponent(tipOnePanel,"task-button3");

        HorizontalLayout tipTwoPanel = new HorizontalLayout();

        tipSecondBtn.setVisible(false);
        tipSecondBtn.setDescription("ex. year, similarity, etc.");
        tipTwoPanel.addComponent(tipSecondBtn);
        tipTwoPanel.addComponent(tipSecondText);
        taskContent.addComponent(tipTwoPanel,"task-button4");


        setStyleName("about-view");
        addComponent(taskContent);
        setComponentAlignment(taskContent, Alignment.TOP_CENTER);

        Answer alreadySavedAnswer = answerGateway.findAnswerDetails(tracks.get(currentTrack).getId(),1L);

        if (alreadySavedAnswer != null && alreadySavedAnswer.getId() != null) {
            titleField.setValue(alreadySavedAnswer.getAnsweredTitle());
            artistField.setValue(alreadySavedAnswer.getAnsweredArtist());
        }

        sendBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Answer answer = new Answer();

                answer.setAnsweredTitle(titleField.getValue());
                answer.setAnsweredArtist(artistField.getValue());

                if (tipFirstText.getValue() != "") {
                    answer.setAnswerTime("10");
                }
                answer.setTrackId(tracks.get(currentTrack).getId());
                answer.setUserId(1L);

                answerGateway.saveAnswer(answer);
                sendBtn.setCaption("Saved");
            }
        });

        prevBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (currentTrack == 0) {
                    currentTrack = currentTrack;
                } else {
                    currentTrack = currentTrack - 1;

                    trackUrl = tracks.get(currentTrack).getTrackUrl();
                    final Resource audioResource = new ExternalResource(trackUrl);
                    sample.setSource(audioResource);
                    sample.setHtmlContentAllowed(true);
                    sample.setAltText("Can't play media");
                    taskContent.addComponent(sample,"task-player");

                    tipFirstText.setValue(tracks.get(currentTrack).getTipOne());
                    tipSecondText.setValue(tracks.get(currentTrack).getTipTwo());

                    clearTips();
                    clearForm();

                    Answer alreadySavedAnswer = answerGateway.findAnswerDetails(tracks.get(currentTrack).getId(),1L);

                    if (alreadySavedAnswer != null && alreadySavedAnswer.getId() != null) {
                        titleField.setValue(alreadySavedAnswer.getAnsweredTitle());
                        artistField.setValue(alreadySavedAnswer.getAnsweredArtist());
                    }

                    countLbl.setValue(String.valueOf(currentTrack+1) + " / " + tracksCount);
                }
            }
        });

        nextBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if (currentTrack == tracks.size() - 1) {
                    currentTrack = currentTrack;
                } else {
                    taskContent.removeComponent("task-player");
                    currentTrack = currentTrack + 1;

                    trackUrl = tracks.get(currentTrack).getTrackUrl();
                    final Resource audioResource = new ExternalResource(trackUrl);
                    sample.setSource(audioResource);
                    sample.setHtmlContentAllowed(true);
                    sample.setAltText("Can't play media");
                    taskContent.addComponent(sample,"task-player");


                    tipFirstText.setValue(tracks.get(currentTrack).getTipOne());
                    tipSecondText.setValue(tracks.get(currentTrack).getTipTwo());

                    clearTips();
                    clearForm();

                    Answer alreadySavedAnswer = answerGateway.findAnswerDetails(tracks.get(currentTrack).getId(),1L);

                    if (alreadySavedAnswer != null && alreadySavedAnswer.getId() != null) {
                        titleField.setValue(alreadySavedAnswer.getAnsweredTitle());
                        artistField.setValue(alreadySavedAnswer.getAnsweredArtist());
                    }

                    countLbl.setValue(String.valueOf(currentTrack+1) + " / " + tracksCount);
                }
            }
        });

        tipFirstBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                tipFirstText.setValue(tracks.get(currentTrack).getTipOne());
//                artistField.setValue(tracks.get(currentTrack).getTipOne());
//                tipSecondBtn.setVisible(true);
            }
        });

        tipSecondBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                tipSecondText.setValue(tracks.get(currentTrack).getTipTwo());
            }
        });
    }
    private void clearTips(){
        tipSecondBtn.setVisible(false);
        tipSecondText.setValue("");
        tipSecondText.setVisible(false);

        tipFirstText.setValue("");
    }

    private void clearForm(){
        titleField.setValue("");
        artistField.setValue("");
        sendBtn.setCaption("Save");
    }
}
