package com.example.scheduler.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.scheduler.R;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;

/**
 * Created by warrens on 10.08.17.
 */

public class surveyFragment extends DialogFragment {

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        Context context = getActivity();
        int nextScreen;
        String surveyProgress = "0";
        try{
            FileInputStream fis = getActivity().openFileInput("surveyProgress.txt");
            int chr;
            StringBuilder builder = new StringBuilder();
            while ((chr = fis.read()) != -1) {
                builder.append((char) chr);
            }
            surveyProgress = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        nextScreen = Integer.parseInt(surveyProgress);
        switch(nextScreen){
            case 1:
                v = inflater.inflate(R.layout.survey_fragment_part_one, container, false);
                nextScreen = 2;
                final String[] QIDs = {"1","2","3","4"};
                TextView txBirthYearQ = (TextView) v.findViewById(R.id.birthYearQ);
                final Spinner spBirthYearA = (Spinner) v.findViewById(R.id.birthYearA);
                TextView txCountryOfOriginQ = (TextView)v.findViewById(R.id.countryOfOriginQ);
                final Spinner spCountryOfOriginA = (Spinner)v.findViewById(R.id.countryOfOriginA);
                TextView txGenderQ = (TextView)v.findViewById(R.id.genderQ);
                final Spinner spGenderA = (Spinner)v.findViewById(R.id.genderA);
                TextView txCountryOfResidenceQ = (TextView)v.findViewById(R.id.countryOfResidenceQ);
                final Spinner spCountryOfResidenceA = (Spinner)v.findViewById(R.id.countryOfResidenceA);

                txBirthYearQ.setText("What year were you born?");
                txCountryOfOriginQ.setText("In which country were you born?");
                txGenderQ.setText("What is your gender?");
                txCountryOfResidenceQ.setText("In which country have you lived the longest?");

                String[] birthYears = new String[101];
                birthYears[0] = "-";
                for(int i = 99;i>-1;i--){
                    if(i>=10){
                        birthYears[100-i] = "19"+i;
                    }else{
                        birthYears[100-i] = "190"+i;
                    }

                }
                String[] genders = {"-","male","female","other"};
                String[] countries = {
                        "-",
                        "Afghanistan",
                        "Albania",
                        "Algeria",
                        "American Samoa",
                        "Andorra",
                        "Angola",
                        "Anguilla",
                        "Antarctica",
                        "Antigua and Barbuda",
                        "Argentina",
                        "Armenia",
                        "Aruba",
                        "Australia",
                        "Austria",
                        "Azerbaijan",
                        "Bahrain",
                        "Bangladesh",
                        "Barbados",
                        "Belarus",
                        "Belgium",
                        "Belize",
                        "Benin",
                        "Bermuda",
                        "Bhutan",
                        "Bolivia",
                        "Bosnia and Herzegovina",
                        "Botswana",
                        "Bouvet Island",
                        "Brazil",
                        "British Indian Ocean Territory",
                        "British Virgin Islands",
                        "Brunei",
                        "Bulgaria",
                        "Burkina Faso",
                        "Burundi",
                        "Cambodia",
                        "Cameroon",
                        "Canada",
                        "Cape Verde",
                        "Cayman Islands",
                        "Central African Republic",
                        "Chad",
                        "Chile",
                        "China",
                        "Christmas Island",
                        "Cocos (Keeling) Islands",
                        "Colombia",
                        "Comoros",
                        "Congo",
                        "Cook Islands",
                        "Costa Rica",
                        "Cote d'Ivoire",
                        "Croatia",
                        "Cuba",
                        "Cyprus",
                        "Czech Republic",
                        "Democratic Republic of the Congo",
                        "Denmark",
                        "Djibouti",
                        "Dominica",
                        "Dominican Republic",
                        "East Timor",
                        "Ecuador",
                        "Egypt",
                        "El Salvador",
                        "Equatorial Guinea",
                        "Eritrea",
                        "Estonia",
                        "Ethiopia",
                        "Faeroe Islands",
                        "Falkland Islands",
                        "Fiji",
                        "Finland",
                        "Former Yugoslav Republic of Macedonia",
                        "France",
                        "French Guiana",
                        "French Polynesia",
                        "French Southern Territories",
                        "Gabon",
                        "Georgia",
                        "Germany",
                        "Ghana",
                        "Gibraltar",
                        "Greece",
                        "Greenland",
                        "Grenada",
                        "Guadeloupe",
                        "Guam",
                        "Guatemala",
                        "Guinea",
                        "Guinea-Bissau",
                        "Guyana",
                        "Haiti",
                        "Heard Island and McDonald Islands",
                        "Honduras",
                        "Hong Kong",
                        "Hungary",
                        "Iceland",
                        "India",
                        "Indonesia",
                        "Iran",
                        "Iraq",
                        "Ireland",
                        "Israel",
                        "Italy",
                        "Jamaica",
                        "Japan",
                        "Jordan",
                        "Kazakhstan",
                        "Kenya",
                        "Kiribati",
                        "Kuwait",
                        "Kyrgyzstan",
                        "Laos",
                        "Latvia",
                        "Lebanon",
                        "Lesotho",
                        "Liberia",
                        "Libya",
                        "Liechtenstein",
                        "Lithuania",
                        "Luxembourg",
                        "Macau",
                        "Madagascar",
                        "Malawi",
                        "Malaysia",
                        "Maldives",
                        "Mali",
                        "Malta",
                        "Marshall Islands",
                        "Martinique",
                        "Mauritania",
                        "Mauritius",
                        "Mayotte",
                        "Mexico",
                        "Micronesia",
                        "Moldova",
                        "Monaco",
                        "Mongolia",
                        "Montenegro",
                        "Montserrat",
                        "Morocco",
                        "Mozambique",
                        "Myanmar",
                        "Namibia",
                        "Nauru",
                        "Nepal",
                        "Netherlands",
                        "Netherlands Antilles",
                        "New Caledonia",
                        "New Zealand",
                        "Nicaragua",
                        "Niger",
                        "Nigeria",
                        "Niue",
                        "Norfolk Island",
                        "North Korea",
                        "Northern Marianas",
                        "Norway",
                        "Oman",
                        "Pakistan",
                        "Palau",
                        "Panama",
                        "Papua New Guinea",
                        "Paraguay",
                        "Peru",
                        "Philippines",
                        "Pitcairn Islands",
                        "Poland",
                        "Portugal",
                        "Puerto Rico",
                        "Qatar",
                        "Reunion",
                        "Romania",
                        "Russia",
                        "Rwanda",
                        "Sqo Tome and Principe",
                        "Saint Helena",
                        "Saint Kitts and Nevis",
                        "Saint Lucia",
                        "Saint Pierre and Miquelon",
                        "Saint Vincent and the Grenadines",
                        "Samoa",
                        "San Marino",
                        "Saudi Arabia",
                        "Senegal",
                        "Serbia",
                        "Seychelles",
                        "Sierra Leone",
                        "Singapore",
                        "Slovakia",
                        "Slovenia",
                        "Solomon Islands",
                        "Somalia",
                        "South Africa",
                        "South Georgia and the South Sandwich Islands",
                        "South Korea",
                        "South Sudan",
                        "Spain",
                        "Sri Lanka",
                        "Sudan",
                        "Suriname",
                        "Svalbard and Jan Mayen",
                        "Swaziland",
                        "Sweden",
                        "Switzerland",
                        "Syria",
                        "Taiwan",
                        "Tajikistan",
                        "Tanzania",
                        "Thailand",
                        "The Bahamas",
                        "The Gambia",
                        "Togo",
                        "Tokelau",
                        "Tonga",
                        "Trinidad and Tobago",
                        "Tunisia",
                        "Turkey",
                        "Turkmenistan",
                        "Turks and Caicos Islands",
                        "Tuvalu",
                        "Virgin Islands",
                        "Uganda",
                        "Ukraine",
                        "United Arab Emirates",
                        "United Kingdom",
                        "United States",
                        "United States Minor Outlying Islands",
                        "Uruguay",
                        "Uzbekistan",
                        "Vanuatu",
                        "Vatican City",
                        "Venezuela",
                        "Vietnam",
                        "Wallis and Futuna",
                        "Western Sahara",
                        "Yemen",
                        "Yugoslavia",
                        "Zambia",
                        "Zimbabwe"
                };

                ArrayAdapter<String> birthYearQ;
                ArrayAdapter<String> countryOfOriginQ;
                ArrayAdapter<String> genderQ;
                ArrayAdapter<String> countryOfResidenceQ;

                final String selectedBirthYear;
                final String selectedCountryOfOrigin;
                final String selectedGender;
                final String selectedCountryOfResidence;

                birthYearQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,birthYears);
                birthYearQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spBirthYearA.setAdapter(birthYearQ);
                selectedBirthYear = spBirthYearA.getSelectedItem().toString();

                countryOfOriginQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,countries);
                countryOfOriginQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCountryOfOriginA.setAdapter(countryOfOriginQ);
                selectedCountryOfOrigin = spCountryOfOriginA.getSelectedItem().toString();

                genderQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,genders);
                genderQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spGenderA.setAdapter(genderQ);
                selectedGender = spGenderA.getSelectedItem().toString();

                countryOfResidenceQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,countries);
                countryOfResidenceQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCountryOfResidenceA.setAdapter(countryOfResidenceQ);
                selectedCountryOfResidence = spCountryOfResidenceA.getSelectedItem().toString();
                final int pass = nextScreen;
                Button b  =(Button)v.findViewById(R.id.toPartTwo);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String submitAnswers  = QIDs[0]+","+selectedBirthYear+"\n"
                                +QIDs[1]+","+selectedCountryOfOrigin+"\n"
                                +QIDs[2]+","+selectedGender+"\n"
                                +QIDs[3]+","+selectedCountryOfResidence+"\n";
                        //ToDo submit answers
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = pass+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
            case 2:
                v = inflater.inflate(R.layout.survey_fragment_part_two, container, false);
                final String[] QID = {"5","6","7","8"};
                TextView txEducationQ = (TextView) v.findViewById(R.id.educationLevelQ);
                txEducationQ.setText("What is the highest level of education you have completed?");
                TextView txEmploymentStatusQ = (TextView) v.findViewById(R.id.employmentStatusQ);
                txEmploymentStatusQ.setText("Which of the following best describes your employment status?");
                TextView txHouseTypeQ = (TextView) v.findViewById(R.id.houseTypeQ);
                txHouseTypeQ.setText("What type of house do you live in?");
                TextView txHouseSizeQ = (TextView) v.findViewById(R.id.houseSizeQ);
                txHouseSizeQ.setText("What size is your house?");

                final Spinner spEducationA = (Spinner) v.findViewById(R.id.educationLevelA);
                final Spinner spHouseTypeA = (Spinner)v.findViewById(R.id.houseTypeA);
                final Spinner spHouseSizeA = (Spinner) v.findViewById(R.id.houseSizeA);

                final String selectedEducationLevel;
                final String selectedHouseType;
                final String selectedHouseSize;

                ArrayAdapter<String> educationLevelQ;
                ArrayAdapter<String> houseTypeQ;
                ArrayAdapter<String> houseSizeQ;

                String[] educationLevels = {
                        "-",
                        "Level 1 - Primary Education",
                        "Level 2 - Lower Secondary Education",
                        "Level 3 - Upper Secondary Education",
                        "Level 4 - Post-Secondary Non-Tertiary Education",
                        "Level 5 - Short Cycle Tertiary Education",
                        "Level 6 - Bachelor's or equivalent level",
                        "Level 7 - Master's or equivalent level",
                        "Level 8 - Doctoral or equivalent level"
                };

                educationLevelQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,educationLevels);
                educationLevelQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEducationA.setAdapter(educationLevelQ);
                selectedEducationLevel = spEducationA.getSelectedItem().toString();

                String[] houseTypes = {
                        "-",
                        "Detached",
                        "Semi-detached",
                        "Mid-terrace",
                        "Apartment/Flat",
                        "Other"
                };

                houseTypeQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,houseTypes);
                houseTypeQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spHouseTypeA.setAdapter(houseTypeQ);
                selectedHouseType = spHouseTypeA.getSelectedItem().toString();

                String[] houseSizes = {
                        "-",
                        "1 Bed",
                        "2 Bed",
                        "3 Bed",
                        "4 Bed",
                        "5 Bed",
                        "6 Bed +"
                };

                houseSizeQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,houseSizes);
                houseSizeQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spHouseSizeA.setAdapter(houseSizeQ);
                selectedHouseSize = spHouseSizeA.getSelectedItem().toString();


                final Button[] bEmploymentStatusA = new Button[5];
                bEmploymentStatusA[0] = (Button)v.findViewById(R.id.employmentStatusA1);
                bEmploymentStatusA[0].setText("Full-time Employee");
                bEmploymentStatusA[1] = (Button)v.findViewById(R.id.employmentStatusA2);
                bEmploymentStatusA[1].setText("Part-Time Employee");
                bEmploymentStatusA[2] = (Button)v.findViewById(R.id.employmentStatusA3);
                bEmploymentStatusA[2].setText("Self-Employed");
                bEmploymentStatusA[3] = (Button)v.findViewById(R.id.employmentStatusA4);
                bEmploymentStatusA[3].setText("In Education");
                bEmploymentStatusA[4] = (Button)v.findViewById(R.id.employmentStatusA5);
                bEmploymentStatusA[4].setText("Unemployed");
                for(int i = 0; i<bEmploymentStatusA.length;i++){
                    final int index = i;
                    bEmploymentStatusA[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(bEmploymentStatusA[index].isPressed()){
                                bEmploymentStatusA[index].setPressed(false);
                            }else{
                                bEmploymentStatusA[index].setPressed(true);
                                for(int j = 0;j<bEmploymentStatusA.length;j++){
                                    if(j!=index){
                                        bEmploymentStatusA[j].setPressed(false);
                                    }
                                }
                            }
                        }
                    });
                }
                nextScreen = 3;
                final int passTwo= nextScreen;
                Button next = (Button) v.findViewById(R.id.toPartThree);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selectedEmploymentStatus = "-";
                        for(int i = 0; i<bEmploymentStatusA.length;i++){
                            if(bEmploymentStatusA[i].isPressed()){
                                selectedEmploymentStatus = bEmploymentStatusA[i].getText().toString();
                                break;
                            }
                        }
                        String submitAnswers = QID[0]+","+selectedEducationLevel+"\n"+
                                                QID[1]+","+selectedEmploymentStatus+"\n"+
                                                QID[2]+","+selectedHouseType+"\n"+
                                                QID[3]+","+selectedHouseSize+"\n";
                        //ToDo submit answers.
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passTwo+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
            case 3:
                v = inflater.inflate(R.layout.survey_fragment_part_three, container, false);
                final String[] QIDS = {
                        "5",
                        "6"
                };
                String[] houseAges = new String[]{
                        "-",
                        "Pre 1900s",
                        "1900 - 1909",
                        "1910 - 1919",
                        "1920 - 1929",
                        "1930 - 1939",
                        "1940 - 1949",
                        "1950 - 1959",
                        "1960 - 1969",
                        "1970 - 1979",
                        "1980 - 1989",
                        "1990 - 1999",
                        "2000 - 2009",
                        "2010+"
                };
                String[] numberOfOccupants = new String[]{
                        "-",
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6+"
                };
                TextView txHouseAgeQ = (TextView) v.findViewById(R.id.houseAgeQ);
                txHouseAgeQ.setText("Approximately when was your house built?");
                TextView txHouseOccupantNumberQ = (TextView) v.findViewById(R.id.houseOccupantNumberQ);
                txHouseOccupantNumberQ.setText("How many people live in your house?");

                final Spinner spHouseAgeA = (Spinner) v.findViewById(R.id.houseAgeA);
                final Spinner spHouseOccupantNumberA = (Spinner) v.findViewById(R.id.houseOccupantNumberA);

                ArrayAdapter<String> houseAgeQ;
                ArrayAdapter<String> houseOccupantNumberQ;

                final String selectedHouseAge;
                final String selectedOccupantNumber;

                houseAgeQ = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,houseAges);
                houseAgeQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spHouseAgeA.setAdapter(houseAgeQ);
                selectedHouseAge = spHouseAgeA.getSelectedItem().toString();

                houseOccupantNumberQ = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,numberOfOccupants);
                houseOccupantNumberQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spHouseOccupantNumberA.setAdapter(houseOccupantNumberQ);
                selectedOccupantNumber = spHouseOccupantNumberA.getSelectedItem().toString();

                nextScreen = 4;
                final int passThree = nextScreen;
                Button bFour = (Button) v.findViewById(R.id.toPartFour);
                bFour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String submit = QIDS[0]+","+selectedHouseAge+"\n"+
                                        QIDS[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passThree+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
//ToDo move to next screen.
                break;
            case 4:
                v = inflater.inflate(R.layout.survey_fragment_part_four, container, false);
                final String[] Q = {
                        "11-1",
                        "11-2",
                        "11-3",
                        "11-4",
                        "11-5",
                        "11-6",
                        "11-7",
                        "11-8",
                        "11-9",
                        "11-10",
                        "11-11",
                        "11-12",
                        "11-13",
                        "11-14"
                };

                TextView txWhichAppliancesQ = (TextView)v.findViewById(R.id.appliancesOwnedQ);
                txWhichAppliancesQ.setText("Which appliances do you have at home?");

                final CheckBox cbWashingMachine = (CheckBox)v.findViewById(R.id.washingMachine);
                cbWashingMachine.setText("Washing Machine");
                final CheckBox cbTumbleDryer = (CheckBox)v.findViewById(R.id.tumbleDryer);
                cbTumbleDryer.setText("Tumble Dryer");
                final CheckBox cbComputerLaptop = (CheckBox)v.findViewById(R.id.laptop);
                cbComputerLaptop.setText("Computer(Laptop)");
                final CheckBox cbComputerDesktop = (CheckBox)v.findViewById(R.id.desktop);
                cbComputerDesktop.setText("Computer(Desktop)");
                final CheckBox cbOven = (CheckBox)v.findViewById(R.id.oven);
                cbOven.setText("Oven");
                final CheckBox cbHob = (CheckBox)v.findViewById(R.id.hob);
                cbHob.setText("Hob");
                final CheckBox cbElectricShower = (CheckBox)v.findViewById(R.id.electricShower);
                cbElectricShower.setText("Electric Shower");
                final CheckBox cbDishwasher = (CheckBox)v.findViewById(R.id.dishwasher);
                cbDishwasher.setText("Dishwasher");
                final CheckBox cbElectricHeater = (CheckBox)v.findViewById(R.id.electricHeater);
                cbElectricHeater.setText("Electric Heater");
                final CheckBox cbAirConditioner = (CheckBox)v.findViewById(R.id.airConditioner);
                cbAirConditioner.setText("Air Conditioner");
                final CheckBox cbKettle = (CheckBox)v.findViewById(R.id.kettle);
                cbKettle.setText("Kettle");
                final CheckBox cbMicrowave = (CheckBox)v.findViewById(R.id.microwave);
                cbMicrowave.setText("Microwave");
                final CheckBox cbFreezer = (CheckBox)v.findViewById(R.id.freezer);
                cbFreezer.setText("Freezer");
                final CheckBox cbFridge = (CheckBox)v.findViewById(R.id.fridge);
                cbFridge.setText("Fridge");

                nextScreen = 5;
                final int passFour = nextScreen;
                Button bFive = (Button) v.findViewById(R.id.toPartFive);
                bFive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * "Hob",0
                         * "Oven",1
                         * "TumbleDryer",2
                         * "WashingMachine",3
                         * "Computer",4
                         * "Kettle",5
                         * "DishWasher",6
                         * "Shower"7
                         */
                        String[] enableTable = new String[8];
                        String washingMachine,tumbleDryer,computerLaptop,computerDesktop,oven,hob,electricShower,dishWasher,electricHeater,airConditioner,kettle,microwave,freezer,fridge;
                        if(cbWashingMachine.isChecked()){
                            washingMachine = "true";
                            enableTable[3] = "WashingMachine,true";
                        }else{
                            washingMachine = "false";
                            enableTable[3] = "WashingMachine,false";
                        }
                        if(cbTumbleDryer.isChecked()){
                            tumbleDryer = "true";
                            enableTable[2] = "TumbleDryer,true";
                        }else{
                            tumbleDryer = "false";
                            enableTable[2] = "TumbleDryer,false";
                        }
                        if(cbComputerLaptop.isChecked()){
                            computerLaptop = "true";
                            enableTable[4] = "Computer,true";
                        }else{
                            computerLaptop = "false";
                            enableTable[4] = "Computer,false";
                        }
                        if(cbComputerDesktop.isChecked()){
                            computerDesktop = "true";
                            enableTable[4] = "Computer,true";
                        }else{
                            computerDesktop = "false";
                            enableTable[4] = "Computer,false";
                        }
                        if(cbOven.isChecked()){
                            oven = "true";
                            enableTable[1] = "Oven,true";
                        }else{
                            oven = "false";
                            enableTable[1] = "Oven,false";
                        }
                        if(cbHob.isChecked()){
                            hob = "true";
                            enableTable[0] = "Hob,true";
                        }else{
                            hob = "false";
                            enableTable[0] = "Hob,false";
                        }
                        if(cbElectricShower.isChecked()){
                            electricShower = "true";
                            enableTable[7] = "Shower,true";
                        }else{
                            electricShower = "false";
                            enableTable[7] = "Shower,false";
                        }
                        if(cbDishwasher.isChecked()){
                            dishWasher = "true";
                            enableTable[6] = "Dishwasher,true";
                        }else{
                            dishWasher = "false";
                            enableTable[6] = "Dishwasher,true";
                        }
                        if(cbElectricHeater.isChecked()){
                            electricHeater = "true";
                        }else{
                            electricHeater = "false";
                        }
                        if(cbAirConditioner.isChecked()){
                            airConditioner = "true";
                        }else{
                            airConditioner = "false";
                        }
                        if(cbKettle.isChecked()){
                            kettle = "true";
                            enableTable[5] = "Kettle,true";
                        }else{
                            kettle = "false";
                            enableTable[5] = "Kettle,false";
                        }
                        if(cbMicrowave.isChecked()){
                            microwave = "true";
                        }else{
                            microwave = "false";
                        }
                        if(cbFreezer.isChecked()){
                            freezer = "true";
                        }else{
                            freezer = "false";
                        }
                        if(cbFridge.isChecked()){
                            fridge = "true";
                        }else{
                            fridge = "false";
                        }
                        String submit =
                                Q[0]+","+washingMachine+"\n"+
                                Q[1]+","+tumbleDryer+"\n"+
                                Q[2]+","+computerLaptop+"\n"+
                                Q[3]+","+computerDesktop+"\n"+
                                Q[4]+","+oven+"\n"+
                                Q[5]+","+hob+"\n"+
                                Q[6]+","+electricShower+"\n"+
                                Q[7]+","+dishWasher+"\n"+
                                Q[8]+","+electricHeater+"\n"+
                                Q[9]+","+airConditioner+"\n"+
                                Q[10]+","+kettle+"\n"+
                                Q[11]+","+microwave+"\n"+
                                Q[12]+","+freezer+"\n"+
                                Q[13]+","+fridge+"\n";
                        String enableTableData=enableTable[0];
                        for(int q = 1; q<enableTable.length;q++){
                            enableTableData+="\n"+enableTable[q];
                        }
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("appliancesEnabledDataFile.txt",Context.MODE_PRIVATE);
                            fos.write(enableTableData.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passFour+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        //ToDo move to next screen.
                    }
                });
                break;
            case 5:
                v = inflater.inflate(R.layout.survey_fragment_part_five, container, false);

                nextScreen = 6;
                final int passFive = nextScreen;
                Button bSix = (Button) v.findViewById(R.id.toPartSix);
                bSix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passFive+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case 6:
                v = inflater.inflate(R.layout.survey_fragment_part_six, container, false);
                nextScreen = 7;
                final int passSix = nextScreen;
                Button bSeven = (Button) v.findViewById(R.id.toPartSeven);
                bSeven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passSix+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
            case 7:
                v = inflater.inflate(R.layout.survey_fragment_part_seven, container, false);
                nextScreen = 8;
                final int passSeven = nextScreen;
                Button bEight = (Button) v.findViewById(R.id.toPartEight);
                bEight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passSeven+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
            case 8:
                v = inflater.inflate(R.layout.survey_fragment_part_eight, container, false);
                nextScreen = 9;
                final int passEight = nextScreen;
                Button bNine = (Button) v.findViewById(R.id.toPartNine);
                bNine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passEight+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
            case 9:
                v = inflater.inflate(R.layout.survey_fragment_part_nine, container, false);
                nextScreen = 10;
                final int passNine = nextScreen;
                Button bTen = (Button) v.findViewById(R.id.toPartTen);
                bTen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passNine+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
            case 10:
                v = inflater.inflate(R.layout.survey_fragment_part_ten, container, false);
                Button bDismiss= (Button) v.findViewById(R.id.finished);
                bDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                    }
                });
                //ToDo move to next screen.
                break;
            default:
                v = inflater.inflate(R.layout.survey_fragment_part_one, container, false);
                nextScreen = 2;
                final int passTen = nextScreen;
                Button bEleven = (Button) v.findViewById(R.id.toPartTwo);
                bEleven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String submit = Q[0]+","+selectedHouseAge+"\n"+
//                                Q[1]+","+selectedOccupantNumber+"\n";
                        //ToDO submit
                        try{
                            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",Context.MODE_PRIVATE);
                            String nextScreen = passTen+"";
                            fos.write(nextScreen.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                //ToDo move to next screen.
                break;
        }

        try{
            String next = ""+nextScreen;
            FileOutputStream fos = getActivity().openFileOutput("surveyProgress.txt",context.MODE_PRIVATE);
            fos.write(next.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
        return v;
    }
}
