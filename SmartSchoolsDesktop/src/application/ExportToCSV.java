package application;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class ExportToCSV {
	public static boolean export(String name, String[] col, String[][] data) throws FileNotFoundException{
		if(col.length != data[0].length){
			return false;
		}
		PrintWriter writer = new PrintWriter(new File(name + "Data.csv"));
		Date date = Calendar.getInstance().getTime();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < col.length; i++){
			builder.append(col[i]);
			builder.append(',');
		}
		builder.append('\n');
		for(int row = 0; row < data.length; row++){
			for(int column = 0; column < data[0].length; column++){
				builder.append(data[row][column]);
				builder.append(',');
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append('\n');
		}
		builder.append(date);
		writer.write(builder.toString());
		writer.close();
		return true;
	}
}
