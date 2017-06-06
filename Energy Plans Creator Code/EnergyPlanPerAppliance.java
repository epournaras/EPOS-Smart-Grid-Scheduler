import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EnergyPlanPerAppliance {
	private static String[] actionFileNames  ={
            "Hob",//1
            "Oven",//2
            "Tumble_Dryer",//6
            "Washing_Machine",//7
            "Computer",//0
            "Kettle",//4
            "Dishwasher",//3
            "Shower"//5
            };
	public static void main(String[] args){
		String firstHalf = "";//Phone's android ID
		String secondHalf ="";//Date and time that the plans were created
		String csvWU = "/Users/warrens/data/EnergyProfileApplianceWattage.csv";//Energy Profile csv created by Energy Profile creator
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		String[] demographicInfo = new String[8];
		try{
			br = new BufferedReader(new FileReader(csvWU));
			while (((line = br.readLine()) != null)){
				demographicInfo = line.split(csvSplitBy);
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
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
		int[] demographicInfoInts = new int[demographicInfo.length];
		for(int i = 0; i<demographicInfo.length;i++){
			char[] array = demographicInfo[i].toCharArray();
			int info =0;
			int multiplier = 1;
			for(int j=0;j<array.length;j++){
				int index = array.length-1-j;
				info+=Character.getNumericValue(array[index])*multiplier;
				multiplier*=10;
			}
			demographicInfoInts[i] = info;
		}
		for(int index = 0;index<demographicInfo.length;index++){
			String csv = "/Users/warrens/data/"+firstHalf+actionFileNames[index]+secondHalf+".txt";
			ArrayList<String[]> plans = new ArrayList<String[]>();
			String title="";
			int count = 0;
			try{
				br = new BufferedReader(new FileReader(csv));
				while (((line = br.readLine()) != null)){
					if(count == 0){
						title = line;
					}else{
						plans.add(line.split(csvSplitBy));
					}
				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
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
			String[][] plansArray = new String[plans.size()][];
			plans.toArray(plansArray);
			int demoAppIndex = 0;
			switch(index){
			case 0:
				demoAppIndex = 1;
				break;	
			case 1:
				demoAppIndex = 2;
				break;
			case 2:
				demoAppIndex = 6;
				break;
			case 3:
				demoAppIndex = 7;
				break;
			case 4:
				demoAppIndex = 0;
				break;
			case 5:
				demoAppIndex = 4;
				break;
			case 6:
				demoAppIndex = 3;	
				break;
			case 7:
				demoAppIndex = 5;
				break;
			}
			String energyPlans = title+"\n";
			for(int i = 0; i<plansArray.length;i++){
				    energyPlans +="Plan "+i+",";
				    for(int j = 0; j<plansArray[i].length;j++){
					if(j>0){
						if(j==plansArray[i].length-1){
							if(plansArray[i][j].equals("1")){
								int wattUsage = demographicInfoInts[demoAppIndex]*60;
								energyPlans+=wattUsage+"\n";
							}else{
								energyPlans+="0\n";
							}
						}else{
							if(plansArray[i][j].equals("1")){
								int wattUsage = demographicInfoInts[demoAppIndex]*60;
								energyPlans+=wattUsage+",";
							}else{
								energyPlans+="0,";
							}
						}
					}	
				}
			}
			String fileName = firstHalf+actionFileNames[index]+secondHalf+"-EnergyPlans.csv";
			FileWriter fileWriter = null;
			try{
				fileWriter = new FileWriter(fileName);
				fileWriter.append(energyPlans);
			}catch(Exception e){
			
			}finally{
				try {
					fileWriter.flush();
					fileWriter.close();           
				} catch (IOException e) {  
					System.out.println("Error while flushing/closing fileWriter !!!");
					e.printStackTrace();	                
				}
			}
		}
	}
}
