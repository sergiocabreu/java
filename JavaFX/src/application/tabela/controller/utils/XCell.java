package application.controller.utils;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class XCell extends ListCell<XCellField> {

	private VBox vbox = new VBox();
	private Label label1 = new Label("(empty)");
    private Label label2 = new Label("(empty)");
    private Label label3 = new Label("(empty)");
    private Pane pane = new Pane();
    
    
    public XCell() {
        super();
        
        label1.setFont(new Font("Verdana", 14));
        label2.setFont(new Font("Verdana", 11));
        label3.setFont(new Font("Verdana", 11));
        
        vbox.getChildren().addAll(pane, label1, label2, label3);
        VBox.setVgrow(pane, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(XCellField item, boolean empty) {
        super.updateItem(item, empty);
        
        setText(null);  // No text in label of super class
        
        if (empty) {
            setGraphic(null);
            
        } else {
            if(item != null){
            	label1.setText(item.getField1());
            	label2.setText("CPF: " + item.getField2());
            	label3.setText("Emissor: " + item.getField3());
            }
            
            setGraphic(vbox);
        }
    }
    	
}
