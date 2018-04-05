/**
 * 
 */
package irc.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author chkrr00k
 *
 */
public class JFXframe extends Application {
	
	private Label topic;
	private TextField inputMessage;
	private TextArea persons;
	private TextArea chatMessages;
	private TilePane channelPane;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			this.topic = new Label("IRC:CID FX version");
			topic.setId("topic");
			root.setTop(topic);
			
			this.inputMessage = new TextField();
			root.setBottom(inputMessage);
			
			this.persons = new TextArea();
			persons.setId("persons");
			persons.setEditable(false);
			root.setRight(persons);
			
			this.chatMessages = new TextArea();
			chatMessages.setEditable(false);
			chatMessages.setText("<chkrr00k> i'm enjoing this client\n<DrMilgram> me too!");
			chatMessages.setWrapText(true);
			root.setCenter(chatMessages);
			
			Button b = new Button();
		    b.setText("#channel");
		    
		    Button b2 = new Button();
		    b2.setText("#channel2");
		   			
			this.channelPane = new TilePane();
			channelPane.setPrefColumns(1);
			channelPane.getChildren().addAll(b, b2);
			root.setLeft(channelPane);
		
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.DECORATED);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

