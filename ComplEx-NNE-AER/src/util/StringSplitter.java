package util;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter {
	public static String[] split(String separator, String original){
		char[] separator_char = separator.toCharArray();
		for(int i=1; i<separator_char.length; i++){
			original = original.replace(separator_char[i], separator_char[0]);
		}
		original = original.replaceAll("(\\(x,y\\))|(\\(y,x\\))|(\\(y,z\\))|(\\(x,z\\))|(\\(z,x\\))","");
		return original.split(separator.substring(0, 1));
	}
	
	public static String[] RemoveEmptyEntries(String[] original){
		int len = original.length;
		List<String> list = new ArrayList<String>(len);
		for(int i=0; i<len; i++){
			if(original[i] != null && !original[i].equals("")){
				list.add(original[i]);
			}
		}
		String[] result = new String[list.size()];
		for(int i=0; i<list.size(); i++){
			result[i] = list.get(i).trim();
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		String a="/cfdsfs";
		System.out.print(a.split("/")[1]);
	}
}
