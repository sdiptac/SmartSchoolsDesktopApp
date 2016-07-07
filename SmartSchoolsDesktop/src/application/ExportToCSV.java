package application;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ExportToCSV {
	public static boolean export(String name, String[] col, ArrayList<String[]> data) throws FileNotFoundException{
		if(col.length != data.get(0).length){
			return false;
		}
		
		try{
			PrintWriter writer = new PrintWriter(new File(name + "_" + data.get(0)[1] + "_to_" + data.get(data.size()-1)[1] + "_access_point_data.csv"));
			StringBuilder builder = new StringBuilder();
			
			builder.append(Arrays.stream(col).collect(Collectors.joining(", "))).append("\n");
			
			builder.append(data.stream().map(row -> Arrays.stream(row).collect(Collectors.joining(", "))).collect(Collectors.joining("\n")));
			
			writer.write(builder.toString());
			writer.close();
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
}
