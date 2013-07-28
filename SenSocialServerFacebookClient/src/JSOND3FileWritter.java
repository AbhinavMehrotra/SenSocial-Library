import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSOND3FileWritter {


	public static void insertData(String message, String context, String time, double latitude, double longitude){
		try {
			File file=new File("D3Data.json");
			Boolean flag=file.createNewFile();
			if(flag){
				//new file created, so add the root 
				try {
					JSONObject obj=  new JSONObject();
					obj.put("name", "SenSocial");
					obj.put("value", 100);
					JSONArray arr=new JSONArray();
					obj.put("children", arr);
					FileWriter f= new FileWriter("D3Data.json");
					f.write(obj.toString());
					f.flush();
					f.close();
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
			//read the file
			BufferedReader br=null;
			String str="";
			try {	 
				String sCurrentLine;
				br = new BufferedReader(new FileReader("D3Data.json"));
				while ((sCurrentLine = br.readLine()) != null) {
					str+=sCurrentLine;
				}	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			//append child to json array
			try {		 
				JSONObject json = new JSONObject(str);
				JSONArray children=json.getJSONArray("children");
				JSONObject child=new JSONObject();
				child.put("message", message);
				child.put("value", 25);
				child.put("modality", context);
				child.put("latitude", latitude);
				child.put("longitude", longitude);
				children.put(child);
				
				FileWriter f= new FileWriter("D3Data.json");
				f.write(json.toString());
				f.flush();
				f.close();
				
		 	} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
