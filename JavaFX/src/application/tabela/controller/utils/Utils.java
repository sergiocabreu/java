package application.controller.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.text.MaskFormatter;

public class Utils {

	// convert InputStream to String
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public static URL getResourceFX(String resource) {
		return application.Main.class.getResource(resource);
	}

	public static void criarDiretorios() {
		File dirLog = new File(Constants.DIR_LOG);
		if(!dirLog.exists()){
			dirLog.mkdir();
		}
	}
	
	public static String removeMask(String str) { 
 		if (str == null)
			return "";

		return str.replaceAll("[-/().]", "");
	}
	
	public static String formatCPF(String cpf) {
		return format("###.###.###-##", cpf);
	}
	
	private static String format(String pattern, Object value) {
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static long getFileSizeInMegaBytes(File file) {
		// Get length of file in bytes
		long fileSizeInBytes = file.length();
		
		// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
		long fileSizeInKB = fileSizeInBytes / 1024;
		
		// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
		long fileSizeInMB = fileSizeInKB / 1024;
		
		return fileSizeInMB;
	}
	
	public static  void showAlert(String title, String header, String body, AlertType type, Image ico) {
		title = title != null ? title : "";
		header = header != null ? header : "";
		body = body != null ? body : "";
		type = type != null ? type : AlertType.NONE;
		
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(body);

		// Icone da Janela
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(ico);

		alert.showAndWait();
	}
	
	/**
	 * Clona um InputStream
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Fake code simulating the copy
		// You can generally do better with nio if you need...
		// And please, unlike me, do something about the Exceptions :D
		byte[] buffer = new byte[1024];
		int len;
		while ((len = input.read(buffer)) > -1 ) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();

		// Open new InputStreams using the recorded bytes
		// Can be repeated as many times as you wish
		return baos.toByteArray(); 
	}
}
