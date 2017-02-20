package application.log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.text.Text;
import application.controller.utils.CalendarUtils;
import application.controller.utils.Constants;
import application.log.formatter.CustomFormatter;
 
/**
 * Log customizado
 * 
 * @author Gabriel Tavares
 *
 */
public class Log {
    private static Logger logger;
     
    private static ConsoleHandler consoleHandler;
    private static FileHandler fileHandler;
	private static Text text;
    
    public Log() {
    }
     
    public static synchronized Logger getLogger() {
        if(logger == null){
            final int TAMANHO_EM_BYTES = (int)(10 * Math.pow(1024, 2)); // zero == sem limite
            final int NUM_ARQUIVOS = 2;
             
            try {
                String nameFile = "tce_repo_lcr_%u_%g_" + CalendarUtils.datetimeToFile(new Date()) +".log";
                
                logger = Logger.getLogger(Log.class.getName());
                consoleHandler = new ConsoleHandler();
                fileHandler = new FileHandler(new File(Constants.DIR_LOG, nameFile).getAbsolutePath(), 
//                		TAMANHO_EM_BYTES, NUM_ARQUIVOS, 
                		true);
                                
                logger.setUseParentHandlers(false);
                 
                consoleHandler.setFormatter(new CustomFormatter());
                consoleHandler.setLevel(Level.ALL);             
                fileHandler.setFormatter(new CustomFormatter());
                fileHandler.setLevel(Level.ALL);
                 
                logger.addHandler(consoleHandler);
                logger.addHandler(fileHandler);
                
            } catch (SecurityException e) {
                Log.getLogger().log(Level.SEVERE, e.getMessage(), e);
            } catch (IOException e) {
                Log.getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return logger;
    }
     
    public static synchronized Text getTextAreaLog(){
    	if(text == null){
	    	text = new Text();
		    text.setWrappingWidth(520);
		    text.setId("textlog");
    	}
    	
    	return text;
    }
}
