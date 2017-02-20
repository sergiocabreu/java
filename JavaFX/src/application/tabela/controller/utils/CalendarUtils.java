package application.controller.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
	
	public static String datetimeToString(Date date){
		return calendarToString(date, true, true, true);
	}
	
	public static String dateToString(Date date){
		return calendarToString(date, false, true, true);
	}
	
	public static String timedateToString(Date date){
		return calendarToString(date, true, true, false);
	}
	
	public static String datetimeToFile(Date date){
		return calendarToFile(date, true, true, true);
	}

	public static String calendarToString(Date date, boolean isHour, boolean isDate, boolean isFirstDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		StringBuffer sb = new StringBuffer();
		
		StringBuffer sbHour = new StringBuffer();
		
		if(isHour){
			sbHour.append(putZero(calendar.get(Calendar.HOUR_OF_DAY)));
			sbHour.append(":");
			sbHour.append(putZero(calendar.get(Calendar.MINUTE)));
			sbHour.append(":");
			sbHour.append(putZero(calendar.get(Calendar.SECOND)));
		}

		StringBuffer sbDate = new StringBuffer();
		
		if(isDate){
			sbDate.append(putZero(calendar.get(Calendar.DAY_OF_MONTH)));
			sbDate.append("/");
			sbDate.append(putZero(calendar.get(Calendar.MONTH)+1));
			sbDate.append("/");
			sbDate.append(putZero(calendar.get(Calendar.YEAR)));
		}
		
		if(isDate && !isHour){
			sb.append(sbDate);
		} else if(!isDate && isHour){
			sb.append(sbHour);
		} else if(isDate && isHour && !isFirstDate){
			sb.append(sbHour);
			sb.append(" ");
			sb.append(sbDate);
		} else if(isDate && isHour && isFirstDate){
			sb.append(sbDate);
			sb.append(" ");
			sb.append(sbHour);
		}
		
		return sb.toString().trim();
	}
	
	public static String calendarToFile(Date date, boolean isHour, boolean isDate, boolean isFirstDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		StringBuffer sb = new StringBuffer();
		
		StringBuffer sbHour = new StringBuffer();
		
		if(isHour){
			sbHour.append(putZero(calendar.get(Calendar.HOUR_OF_DAY)));
			sbHour.append(putZero(calendar.get(Calendar.MINUTE)));
			sbHour.append(putZero(calendar.get(Calendar.SECOND)));
		}

		StringBuffer sbDate = new StringBuffer();
		
		if(isDate){
			sbDate.append(putZero(calendar.get(Calendar.DAY_OF_MONTH)));
			sbDate.append("");
			sbDate.append(putZero(calendar.get(Calendar.MONTH)+1));
			sbDate.append("");
			sbDate.append(putZero(calendar.get(Calendar.YEAR)));
		}
		
		if(isDate && !isHour){
			sb.append(sbDate);
		} else if(!isDate && isHour){
			sb.append(sbHour);
		} else if(isDate && isHour && !isFirstDate){
			sb.append(sbHour);
			sb.append("_");
			sb.append(sbDate);
		} else if(isDate && isHour && isFirstDate){
			sb.append(sbDate);
			sb.append("_");
			sb.append(sbHour);
		}
		
		return sb.toString().trim();
	}

	private static String putZero(long value) {
		if(value < 10){
			return "0" + value;
		}
		return "" + value;
	}
	
	public static Calendar stringToDataBR(String dateStr) throws ParseException{
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		Date date = formatador.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar;
	}
	
}
