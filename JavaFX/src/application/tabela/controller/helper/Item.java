package application.controller.helper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString()
@EqualsAndHashCode(exclude={"arquivo"})
@NoArgsConstructor
public class Item {

	@Getter
	private StringProperty caminho = null;
	
	@Getter
	private StringProperty arquivo = null;
		
	
	public Item(String arquivo, String caminho) {
		this.arquivo = new SimpleStringProperty(arquivo);
		this.caminho = new SimpleStringProperty(caminho);
	}
	
}
