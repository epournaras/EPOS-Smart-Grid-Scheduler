import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PastScheduleFileSplitter {
	public static void main(String[] args){
		String csv = "PastSchedules.txt";
		String csvWU = "ApplianceWattage.csv";
		BufferedReader br = null;
	    String line = "";
	    String csvSplitBy = ",";
	    String[][] schedules = new String[1441][1];
	    String[] demographicInfo = new String[5];
	    int count = 0;
	    try{
	    	br = new BufferedReader(new FileReader(csv));
		    while (((line = br.readLine()) != null)){
		    	schedules[count]=line.split(csvSplitBy);
		    	count ++;
		    }
		    br = new BufferedReader(new FileReader(csvWU));
		    while (((line = br.readLine()) != null)){
		    	demographicInfo = line.split(csvSplitBy);
		    }
	    } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.print("Done fcked up");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Done fcked up");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	    int numberOfSchedules = (schedules[0].length-1)/8;
	    String[][][] splitSchedules = new String[numberOfSchedules][8][1440];
	    for(int i = 0; i<splitSchedules.length;i++){
	    	for(int j = 0; j<splitSchedules[i].length;j++){
	    		for(int q = 0; q<splitSchedules[i][j].length;q++){
	    			splitSchedules[i][j][q] = schedules[q+1][j+1];
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
	    int[][][] usageInfo = new int[splitSchedules.length][8][1440];
	    for(int i = 0; i < usageInfo.length ;i++){
	    	for(int j = 0;j<usageInfo[i].length ;j++){
	    		for(int q = 0; q<usageInfo[i][j].length;q++){
	    			switch(splitSchedules[i][j][q]){
	    			case "0":
	    				usageInfo[i][j][q]=0;
	    				break;
	    			case "1":
	    				usageInfo[i][j][q]=demographicInfoInts[j]*60;
	    				break;
	    			default:
	    				usageInfo[i][j][q]=0;
	    				break;
	    			}
	    		}
	    	}
	    }
	    String title = schedules[0][0];
	    for(int i = 1; i<schedules.length;i++){
	    	title+=","+schedules[0][i];
	    }
	    String[] lines = new String[1441];
	    lines[0] = title;
	    for(int w = 1; w<lines.length;w++){
	    	lines[w] = schedules[w][0];
	    }
	    
	    for(int i = 0;i<usageInfo.length;i++){
	    	for(int j = 0;j<usageInfo[i].length ;j++){
	    		for(int q = 0; q<usageInfo[i][j].length;q++){
	    			lines[q+1]+=","+usageInfo[i][j][q];
	    		}
	    	}	    	
	    }
	    
	    String toFile = "";
	    for(int i =0; i<lines.length;i++){
	    	toFile+=lines[i]+"\n";
	    }
	    String fileName = "EnergyPlans.csv";
	    FileWriter fileWriter = null;
        try{
        	fileWriter = new FileWriter(fileName);
        	fileWriter.append(toFile);
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
	    /*
	     * Schedules[0] is going to be the title including the dates that the schedule was created.
	     * Schedules[X][0] will be the time of day
	     * 1-8 will be the first schedule, there can be between 5 and 30 schedules per date.
	     */
	}
}
