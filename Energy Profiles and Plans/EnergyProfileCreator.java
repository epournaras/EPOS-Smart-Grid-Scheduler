import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EnergyProfileCreator {
	public static void main(String[] args){
		String csvDemographics = "/Users/warrens/data/demographics.txt";
		String csvHouseDemographics = "/Users/warrens/data/HousingDetails.txt";
		String csvHouseApplianceWattage = "/Users/warrens/data/HouseApplianceAverageUsage.txt";
		BufferedReader br = null;
	    String line = "";
	    String csvSplitBy = ",";
	    String[] demographs = new String [5];
	    String[][] houseDemographs = new String [20][7];
	    String houseToUse;
	    String[] houseApplianceWattages = new String [1];
	    boolean useDefault = false;
	    int count = 0;
	    int indexCount = 0;
	    try{
	    	br = new BufferedReader(new FileReader(csvDemographics));
		    while (((line = br.readLine()) != null)){
		    	demographs=line.split(csvSplitBy);
		    }
		    br = new BufferedReader(new FileReader(csvHouseDemographics));
		    while(((line=br.readLine()) != null)){
		    	if(count!=0){
		    		houseDemographs[indexCount] = line.split(csvSplitBy);
		    		indexCount++;
		    	}else{
		    		count++;
		    	}
		    }
	    }catch(Exception e){
	    	
	    }
	    int[] houseCount = {
	    		0,//House 1
	    		0,//House 2
	    		0,//House 3
	    		0,//House 4
	    		0,//House 5
	    		0,//House 6
	    		0,//House 7
	    		0,//House 8
	    		0,//House 9
	    		0,//House 10
	    		0,//House 11
	    		0,//House 12
	    		0,//House 13
	    		0,//House 15
	    		0,//House 16
	    		0,//House 17
	    		0,//House 18
	    		0,//House 19
	    		0,//House 20
	    		0 //House 21
	    		};
	    for(int i = 0; i<houseDemographs.length;i++){
	    	if(demographs[0].equals(houseDemographs[i][1])){
	    		houseCount[i]++;
	    	}
	    	if(demographs[1].equals(houseDemographs[i][6])){
	    		houseCount[i]++;
	    	}
	    	if(demographs[2].equals(houseDemographs[i][4])){
	    		houseCount[i]++;
	    	}
	    	if(demographs[3].equals(houseDemographs[i][5])){
	    		houseCount[i]++;
	    	}
	    }
	    int closestHouseIndex = 0;
	    for(int i = 1; i<houseCount.length;i++){
	    	int newNumber = houseCount[i];
	    	if(newNumber>houseCount[closestHouseIndex]){
	    		closestHouseIndex = i;
	    	}
	    }
	    if(houseCount[closestHouseIndex]<2){
	    	useDefault = true;
	    }else{
	    	if(closestHouseIndex>12){
	    		int houseIndex = closestHouseIndex+2;
	    		houseToUse = ""+houseIndex;
	    	}else{
	    		int houseIndex = closestHouseIndex+1;
	    		houseToUse = ""+houseIndex;
	    	}
	    }
	    count = 0;
	    try{
	    	br = new BufferedReader(new FileReader(csvHouseApplianceWattage));
	    	while (((line = br.readLine()) != null)){
	    		if(!useDefault){
	    			if(count==closestHouseIndex+1){
	    				houseApplianceWattages = line.split(csvSplitBy);
	    			}
	    		}else{
	    			if(count==21){
	    				houseApplianceWattages = line.split(csvSplitBy);
	    			}
	    		}
	    		count++;
		    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    for(int i = 0; i<houseCount.length;i++){
	    	int index = i+1;
	    	if(index>13){
	    		index+=1;
	    	}
	    	System.out.print(index+" "+houseCount[i]+"\n");
	    }
	    String toFile = "";
	    for(int i = 0; i<houseApplianceWattages.length;i++){
	    	toFile+=houseApplianceWattages[i]+",";
	    	System.out.print(houseApplianceWattages[i]+",");
	    }
	    String fileName = "ApplianceWattage.csv";
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
    }
}
