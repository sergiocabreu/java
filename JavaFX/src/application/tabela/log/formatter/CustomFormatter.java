package application.log.formatter;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import application.controller.utils.CalendarUtils;
import application.controller.utils.Constants;

/**
* Formatter customizado para salvar no arquivo
* 
* @author Gabriel Tavares
*
*/
public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        //String str;
        StringBuffer strBuf = new StringBuffer();
         
        if(logRecord.getLevel().intValue() == Level.WARNING.intValue()){
        //    str = new SimpleFormatter().format(logRecord);
        	strBuf.append(Constants.NEW_LINE);
            strBuf.append("--------------------------------------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("["+ CalendarUtils.timedateToString(new Date()) +"] ");
//            strBuf.append(logRecord.getLevel().getLocalizedName());
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("-----------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            
            strBuf.append(logRecord.getMessage());													// Mensagem de LOG
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("-----------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("> ");
            strBuf.append(logRecord.getSourceClassName()); 											// Classe
            strBuf.append(" (" + logRecord.getSourceMethodName() + ")");          					// Metodo
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("--------------------------------------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            strBuf.append(Constants.NEW_LINE);
            
        } else if(logRecord.getLevel().intValue() == Level.SEVERE.intValue()){
        	strBuf.append(Constants.NEW_LINE);
            strBuf.append("--------------------------------------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("["+ CalendarUtils.timedateToString(new Date()) +"] ");
            strBuf.append(logRecord.getLevel().getLocalizedName());
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("-----------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            
            strBuf.append(logRecord.getMessage());													// Mensagem de LOG
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("-----------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("> ");
            strBuf.append(logRecord.getSourceClassName()); 											// Classe
            strBuf.append(" (" + logRecord.getSourceMethodName() + ")");          					// Metodo
            strBuf.append(Constants.NEW_LINE);
            strBuf.append("--------------------------------------------------------------------------");
            strBuf.append(Constants.NEW_LINE);
            strBuf.append(Constants.NEW_LINE);
            
        } else{
            //str = logRecord.getMessage() + Constants.NEW_LINE;
        	strBuf.append("["+ CalendarUtils.timedateToString(new Date()) +"] " + logRecord.getMessage());
        	strBuf.append(Constants.NEW_LINE);
        }
         
        //return str;     
        return strBuf.toString();
    }
}
