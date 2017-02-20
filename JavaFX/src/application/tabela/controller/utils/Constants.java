package application.controller.utils;


public class Constants {

	// -- Diretorios da Aplicacao
	public static final String DIR_BASE = "";
	public static final String DIR_RESOURCES = DIR_BASE + "controller/";
	public static final String DIR_CSS = DIR_RESOURCES + "styles/";
	public static final String DIR_FXML = DIR_RESOURCES + "fxml/";
	public static final String DIR_IMAGES = DIR_RESOURCES + "images/";
	
	
	public static final String NEW_LINE = System.getProperty("line.separator");

	// -- Diretorios da Aplicacao
	public static final String DIR_LOG = "logs";
	
	// -- Arquivos de Layout
	public static final String FILE_ROOT_FXML = DIR_FXML + "RootLayout.fxml";
	public static final String FILE_MAIN_FXML = DIR_FXML + "MainLayout.fxml";
	public static final String FILE_CONF_DIALOG_FXML = DIR_FXML + "ConfDialog.fxml";

	// -- Arquivos de Estilo
	public static final String FILE_CSS = DIR_CSS + "styles.css";
	public static final String FILE_FIELDSET_CSS = DIR_CSS + "fieldset.css";

	// -- Arquivos de Imagem
	public static final String LOGO = DIR_IMAGES + "logo.png";
	public static final String IMG_LOGO = DIR_IMAGES + "logo_tce.png";
	public static final String IMG_DRAGDROP = DIR_IMAGES + "img_dragdrop.png";
	public static final String ICO_PLUS = DIR_IMAGES + "ico_add.png";
	public static final String ICO_MINUS = DIR_IMAGES + "ico_del.png";

	// -- Arquivos de Properties
	public static final String PERFERENCES_PROPERTIES = "preferences.properties";
	
	
	// -- Cores
	public static final String COR_VERDE = "#4CDB35";
	public static final String COR_VERMELHO = "#DB354C";
	public static final String COR_AMARELO = "#F0B951";
	public static final String COR_CINZA = "#9D9D9D";
	public static final String COR_AZUL = "#002D49";
	public static final String COR_AZUL_LIGHT = "#005C96";

	public static final int COR_VERDE_HEX = 0x4CDB35;
	public static final int COR_VERMELHO_HEX = 0xDB354C;
	public static final int COR_AMARELO_HEX = 0xF0B951;
	public static final int COR_CINZA_HEX = 0x9D9D9D;
	public static final int COR_AZUL_HEX = 0x002D49;
	public static final int COR_AZUL_LIGHT_HEX = 0x005C96;
	
	
	public static final String DATA_COMPILACAO = "21/08/2015";
	
}