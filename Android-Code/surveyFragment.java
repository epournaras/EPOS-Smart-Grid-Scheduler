package com.example.application.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.application.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.provider.Settings.Secure;
/**
 * Created by warrens on 13.03.17.
 */

public class surveyFragment extends android.support.v4.app.DialogFragment {
    final String PREFS_NAME = "MyPrefsFile";

    private String selectedAnswerOne;
    private String selectedAnswerTwo;
    private String selectedAnswerThree;
    private String selectedAnswerFour;
    private String selectedAnswerFive;
    private String selectedAnswerSix;
    private String selectedAnswerSeven;
    private String selectedAnswerEight;
    private String selectedAnswerNine;
    private String selectedAnswerTen;

    private String[] questionOneAnswers;
    private String[] questionTwoAnswers;
    private String[] questionThreeAnswers;
    private String[] questionFourAnswers;
    private String[] questionFiveAnswers;
    private String[] questionSixAnswers;
    private String[] questionSevenAnswers;
    private String[] questionEightAnswers;
    private String[] questionNineAnswers;
    private String[] questionTenAnswers;
    private String[] questionElevenAnswers;
    private CheckBox qElevenOne, qElevenTwo,qElevenThree,qElevenFour,qElevenFive,qElevenSix,qElevenSeven,qElevenEight,qElevenNine,qElevenTen,qElevenEleven,qElevenTwelve,qElevenThirteen,qElevenFourteen;
    private SeekBar qTwelveSimpleSeekBar,
                    qThirteenSimpleSeekBar,
                    qFourteenSimpleSeekBar,
                    qFifteenOneSimpleSeekBar,qFifteenTwoSimpleSeekBar,qFifteenThreeSimpleSeekBar,qFifteenFourSimpleSeekBar,qFifteenFiveSimpleSeekBar,
                    qSixteenOneSimpleSeekBar,qSixteenTwoSimpleSeekBar,
                    qSeventeenOneSimpleSeekBar,qSeventeenTwoSimpleSeekBar,qSeventeenThreeSimpleSeekBar,qSeventeenFourSimpleSeekBar,
                    qEighteenSimpleSeekBar,
                    qNineteenSimpleSeekBar,
                    qTwentySimpleSeekBar,
                    qTwentyoneSimpleSeekBar,
                    qTwentytwoSimpleSeekBar,
                    qTwentythreeSimpleSeekBar,
                    qTwentyfourSimpleSeekBar,
                    qTwentyfiveSimpleSeekBar,
                    qTwentysixOneSimpleSeekBar,qTwentysixTwoSimpleSeekBar,qTwentysixThreeSimpleSeekBar,qTwentysixFourSimpleSeekBar,qTwentysixFiveSimpleSeekBar,qTwentysixSixSimpleSeekBar,qTwentysixSevenSimpleSeekBar,
                    qTwentysevenSimpleSeekBar,
                    qTwentyeightOneSimpleSeekBar,qTwentyeightTwoSimpleSeekBar,qTwentyeightThreeSimpleSeekBar,qTwentyeightFourSimpleSeekBar,qTwentyeightFiveSimpleSeekBar,qTwentyeightSixSimpleSeekBar,
                    qTwentyeightSevenSimpleSeekBar,qTwentyeightEightSimpleSeekBar,qTwentyeightNineSimpleSeekBar,qTwentyeightTenSimpleSeekBar,qTwentyeightElevenSimpleSeekBar,qTwentyeightTwelveSimpleSeekBar,
                    qTwentyeightThirteenSimpleSeekBar,qTwentyeightFourteenSimpleSeekBar;
    private int qTwelveProgressChangedValue,
            qThirteenProgressChangedValue,
            qFourteenProgressChangedValue,
            qFifteenOneProgressChangedValue,qFifteenTwoProgressChangedValue,qFifteenThreeProgressChangedValue,qFifteenFourProgressChangedValue,qFifteenFiveProgressChangedValue,
            qSixteenOneProgressChangedValue,qSixteenTwoProgressChangedValue,
            qSeventeenOneProgressChangedValue,qSeventeenTwoProgressChangedValue,qSeventeenThreeProgressChangedValue,qSeventeenFourProgressChangedValue,
            qEighteenProgressChangedValue,
            qNineteenProgressChangedValue,
            qTwentyProgressChangedValue,
            qTwentyoneProgressChangedValue,
            qTwentytwoProgressChangedValue,
            qTwentythreeProgressChangedValue,
            qTwentyfourProgressChangedValue,
            qTwentyfiveProgressChangedValue,
            qTwentysixOneProgressChangedValue,qTwentysixTwoProgressChangedValue,qTwentysixThreeProgressChangedValue,qTwentysixFourProgressChangedValue,qTwentysixFiveProgressChangedValue,qTwentysixSixProgressChangedValue,qTwentysixSevenProgressChangedValue,
            qTwentysevenProgressChangedValue,
            qTwentyeightOneProgressChangedValue,qTwentyeightTwoProgressChangedValue,qTwentyeightThreeProgressChangedValue,qTwentyeightFourProgressChangedValue,qTwentyeightFiveProgressChangedValue,qTwentyeightSixProgressChangedValue,
            qTwentyeightSevenProgressChangedValue,qTwentyeightEightProgressChangedValue,qTwentyeightNineProgressChangedValue,qTwentyeightTenProgressChangedValue,qTwentyeightElevenProgressChangedValue,qTwentyeightTwelveProgressChangedValue,
            qTwentyeightThirteenProgressChangedValue,qTwentyeightFourteenProgressChangedValue;
    private boolean bElevenOne, bElevenTwo,bElevenThree,bElevenFour,bElevenFive,bElevenSix,bElevenSeven,bElevenEight,bElevenNine,bElevenTen,bElevenEleven,bElevenTwelve,bElevenThirteen,bElevenFourteen= false;
    private String android_id;
    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);
        final Context context = getActivity();
        View v = inflater.inflate(R.layout.fragment_survey, container, false);
        String title = "QID, Answer\n";
        int numberOfQuestions = 28;
        String[] QIDs = new String[numberOfQuestions];
        for(int i = 0; i<QIDs.length;i++){
            QIDs[i] = ""+i;
        }

        questionOneAnswers = new String[101];
        questionOneAnswers[0] = "-";
        for(int i = 99;i>=0;i--){
            if(i<10){
                questionOneAnswers[questionOneAnswers.length-1-i] = "190"+i;
            }else{
                questionOneAnswers[questionOneAnswers.length-1-i] = "19"+i;
            }
        }

        questionTwoAnswers = new String[]{
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

        questionThreeAnswers = new String[]{
                "-",
                "Male",
                "Female",
                "Other"
        };

        questionFourAnswers = new String[]{
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

        questionFiveAnswers = new String[]{
                "-",
                "Level 1 - Primary Education",
                "Level 2 - Lower Secondary Education",
                "Level 3 - Upper Secondary Education",
                "Level 4 - Post Secondary non-tertiary education",
                "Level 5 - Short Cycle tertiary education",
                "Level 6 - Bachelor or equivalent",
                "Level 7 - Master or equivalent",
                "Level 8 - Doctoral or equivalent"
        };

        questionSixAnswers = new String[]{
                "-",
                "Employee",
                "Student",
                "Worker (casual work, zero hour contract)",
                "Self-employed",
                "Agency Worker",
                "Apprentice",
                "Intern",
                "Volunteer",
                "Umbrella Company",
                "Unemployed"
        };

        questionSevenAnswers = new String[]{
                "-",
                "Detached",
                "Semi-detached",
                "Mid-terrace",
                "Apartment/Flat",
                "Other"
        };

        questionEightAnswers = new String[]{
                "-",
                "1 Bed",
                "2 Bed",
                "3 Bed",
                "4 Bed",
                "5 Bed",
                "6 Bed +"
        };
        questionNineAnswers = new String[]{
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
        questionTenAnswers = new String[]{
                "-",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6+"
        };

        questionElevenAnswers = new String[]{
                "Washing Machine",
                "Tumble Dryer",
                "Computer (Desktop)",
                "Computer (Laptop)",
                "Oven",
                "Hob",
                "Electric Shower",
                "Dishwasher",
                "Electric heater",
                "Air conditioner",
                "Kettle",
                "Microwave",
                "Freezer",
                "Fridge"
        };

        qElevenOne = (CheckBox)v.findViewById((R.id.chk1));
        qElevenTwo= (CheckBox)v.findViewById((R.id.chk2));
        qElevenThree= (CheckBox)v.findViewById((R.id.chk3));
        qElevenFour= (CheckBox)v.findViewById((R.id.chk4));
        qElevenFive= (CheckBox)v.findViewById((R.id.chk5));
        qElevenSix= (CheckBox)v.findViewById((R.id.chk6));
        qElevenSeven= (CheckBox)v.findViewById((R.id.chk7));
        qElevenEight= (CheckBox)v.findViewById((R.id.chk8));
        qElevenNine= (CheckBox)v.findViewById((R.id.chk9));
        qElevenTen= (CheckBox)v.findViewById((R.id.chk10));
        qElevenEleven= (CheckBox)v.findViewById((R.id.chk11));
        qElevenTwelve= (CheckBox)v.findViewById((R.id.chk12));
        qElevenThirteen= (CheckBox)v.findViewById((R.id.chk13));
        qElevenFourteen= (CheckBox)v.findViewById((R.id.chk14));

        qElevenOne.setText(questionElevenAnswers[0]);
        qElevenTwo.setText(questionElevenAnswers[1]);
        qElevenThree.setText(questionElevenAnswers[2]);
        qElevenFour.setText(questionElevenAnswers[3]);
        qElevenFive.setText(questionElevenAnswers[4]);
        qElevenSix.setText(questionElevenAnswers[5]);
        qElevenSeven.setText(questionElevenAnswers[6]);
        qElevenEight.setText(questionElevenAnswers[7]);
        qElevenNine.setText(questionElevenAnswers[8]);
        qElevenTen.setText(questionElevenAnswers[9]);
        qElevenEleven.setText(questionElevenAnswers[10]);
        qElevenTwelve.setText(questionElevenAnswers[11]);
        qElevenThirteen.setText(questionElevenAnswers[12]);
        qElevenFourteen.setText(questionElevenAnswers[13]);

        qElevenOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenOne = true;
                }else{
                    bElevenOne = false;
                }
            }
        });

        qElevenTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenTwo = true;
                }else{
                    bElevenTwo = false;
                }
            }
        });

        qElevenThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenThree = true;
                }else{
                    bElevenThree = false;
                }
            }
        });

        qElevenFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenFour = true;
                }else{
                    bElevenFour = false;
                }
            }
        });

        qElevenFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenFive = true;
                }else{
                    bElevenFive = false;
                }
            }
        });

        qElevenSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenSix = true;
                }else{
                    bElevenSix = false;
                }
            }
        });

        qElevenSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenSeven = true;
                }else{
                    bElevenSeven = false;
                }
            }
        });

        qElevenEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenEight = true;
                }else{
                    bElevenEight = false;
                }
            }
        });

        qElevenNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenNine = true;
                }else{
                    bElevenNine = false;
                }
            }
        });

        qElevenTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenTen = true;
                }else{
                    bElevenTen = false;
                }
            }
        });

        qElevenEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenEleven = true;
                }else{
                    bElevenEleven = false;
                }
            }
        });

        qElevenTwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenTwelve = true;
                }else{
                    bElevenTwelve = false;
                }
            }
        });

        qElevenThirteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenThirteen = true;
                }else{
                    bElevenThirteen = false;
                }
            }
        });

        qElevenFourteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bElevenFourteen = true;
                }else{
                    bElevenFourteen = false;
                }
            }
        });


        ArrayAdapter<CharSequence> adapterActive;
        final Spinner activeDrp = (Spinner)v.findViewById(R.id.spinnerAnswers);
        adapterActive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionOneAnswers);
        adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrp.setAdapter(adapterActive);
        selectedAnswerOne = activeDrp.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveOne;
        final Spinner activeDrpOne = (Spinner)v.findViewById(R.id.spinnerAnswers1);
        adapterActiveOne = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwoAnswers);
        adapterActiveOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpOne.setAdapter(adapterActiveOne);
        selectedAnswerTwo = activeDrpOne.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwo;
        final Spinner activeDrpTwo = (Spinner)v.findViewById(R.id.spinnerAnswers2);
        adapterActiveTwo = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionThreeAnswers);
        adapterActiveTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwo.setAdapter(adapterActiveTwo);
        selectedAnswerThree = activeDrpTwo.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveThree;
        final Spinner activeDrpThree = (Spinner)v.findViewById(R.id.spinnerAnswers3);
        adapterActiveThree = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionFourAnswers);
        adapterActiveThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpThree.setAdapter(adapterActiveThree);
        selectedAnswerFour = activeDrpThree.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFour;
        final Spinner activeDrpFour = (Spinner)v.findViewById(R.id.spinnerAnswers4);
        adapterActiveFour = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionFiveAnswers);
        adapterActiveFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFour.setAdapter(adapterActiveFour);
        selectedAnswerFive = activeDrpFour.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFive;
        final Spinner activeDrpFive = (Spinner)v.findViewById(R.id.spinnerAnswers5);
        adapterActiveFive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionSixAnswers);
        adapterActiveFive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFive.setAdapter(adapterActiveFive);
        selectedAnswerSix = activeDrpFive.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveSix;
        final Spinner activeDrpSix = (Spinner)v.findViewById(R.id.spinnerAnswers6);
        adapterActiveSix = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionSevenAnswers);
        adapterActiveSix.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpSix.setAdapter(adapterActiveSix);
        selectedAnswerSeven = activeDrpSix.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveSeven;
        final Spinner activeDrpSeven = (Spinner)v.findViewById(R.id.spinnerAnswers7);
        adapterActiveSeven = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionEightAnswers);
        adapterActiveSeven.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpSeven.setAdapter(adapterActiveSeven);
        selectedAnswerEight = activeDrpSeven.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveEight;
        final Spinner activeDrpEight = (Spinner)v.findViewById(R.id.spinnerAnswers8);
        adapterActiveEight = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionNineAnswers);
        adapterActiveEight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpEight.setAdapter(adapterActiveEight);
        selectedAnswerNine = activeDrpEight.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveNine;
        final Spinner activeDrpNine = (Spinner)v.findViewById(R.id.spinnerAnswers9);
        adapterActiveNine = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTenAnswers);
        adapterActiveNine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpNine.setAdapter(adapterActiveNine);
        selectedAnswerTen = activeDrpNine.getSelectedItem().toString();

        qTwelveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q12SeekBar);
        qTwelveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwelveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qThirteenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q13SeekBar);
        qThirteenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qThirteenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qFourteenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q14SeekBar);
        qFourteenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qFourteenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qFifteenOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q151SeekBar);
        qFifteenOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qFifteenOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qFifteenTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q152SeekBar);
        qFifteenTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qFifteenTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qFifteenThreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q153SeekBar);
        qFifteenThreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qFifteenThreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qFifteenFourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q154SeekBar);
        qFifteenFourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qFifteenFourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qFifteenFiveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q155SeekBar);
        qFifteenFiveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qFifteenFiveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qSixteenOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q161SeekBar);
        qSixteenOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qSixteenOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qSixteenTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q162SeekBar);
        qSixteenTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qSixteenTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qSeventeenOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q171SeekBar);
        qSeventeenOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qSeventeenOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qSeventeenTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q172SeekBar);
        qSeventeenTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qSeventeenTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qSeventeenThreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q173SeekBar);
        qSeventeenThreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qSeventeenThreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qSeventeenFourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q174SeekBar);
        qSeventeenFourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qSeventeenFourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qEighteenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q18SeekBar);
        qEighteenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qEighteenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qNineteenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q19SeekBar);
        qNineteenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qNineteenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentySimpleSeekBar=(SeekBar)v.findViewById(R.id.Q20SeekBar);
        qTwentySimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyoneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q21SeekBar);
        qTwentyoneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyoneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentytwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q22SeekBar);
        qTwentytwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentytwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentythreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q23SeekBar);
        qTwentythreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentythreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyfourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q24SeekBar);
        qTwentyfourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyfourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyfiveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q25SeekBar);
        qTwentyfiveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyfiveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q261SeekBar);
        qTwentysixOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q262SeekBar);
        qTwentysixTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixThreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q263SeekBar);
        qTwentysixThreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixThreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixFourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q264SeekBar);
        qTwentysixFourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixFourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixFiveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q265SeekBar);
        qTwentysixFiveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixFiveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixSixSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q266SeekBar);
        qTwentysixSixSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixSixProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysixSevenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q267SeekBar);
        qTwentysixSevenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysixSevenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentysevenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q27SeekBar);
        qTwentysevenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentysevenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q281SeekBar);
        qTwentyeightOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q282SeekBar);
        qTwentyeightTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightThreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q283SeekBar);
        qTwentyeightThreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightThreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightFourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q284SeekBar);
        qTwentyeightFourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightFourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightFiveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q285SeekBar);
        qTwentyeightFiveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightFiveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightSixSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q286SeekBar);
        qTwentyeightSixSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightSixProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightSevenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q287SeekBar);
        qTwentyeightSevenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightSevenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightEightSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q288SeekBar);
        qTwentyeightEightSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightEightProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightNineSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q289SeekBar);
        qTwentyeightNineSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightNineProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightTenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q2810SeekBar);
        qTwentyeightTenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightTenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightElevenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q2811SeekBar);
        qTwentyeightElevenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightElevenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightTwelveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q2812SeekBar);
        qTwentyeightTwelveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightTwelveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightThirteenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q2813SeekBar);
        qTwentyeightThirteenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightThirteenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwentyeightFourteenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q2814SeekBar);
        qTwentyeightFourteenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwentyeightFourteenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /*
         *Demographic data should be stored in the following way:
         * "QID,Answer
         *  [QID],[Answer]
         *  [QID],[Answer]
         *  [QID],[Answer]
         *       .
         *       .
         *       .
         *  [QID],[Answer]
         *  [QID],[Answer]"
         * Where QID is the Question ID.
         * The Name of the file should be the following:
         *  USERID-DEMOGRAPHICS.txt
         */
        Button b = (Button) v.findViewById(R.id.Submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                selectedAnswerOne = activeDrp.getSelectedItem().toString();
                selectedAnswerTwo = activeDrpOne.getSelectedItem().toString();
                selectedAnswerThree = activeDrpTwo.getSelectedItem().toString();
                selectedAnswerFour = activeDrpThree.getSelectedItem().toString();
                selectedAnswerFive = activeDrpFour.getSelectedItem().toString();
                selectedAnswerSix = activeDrpFive.getSelectedItem().toString();
                selectedAnswerSeven = activeDrpSix.getSelectedItem().toString();
                selectedAnswerEight = activeDrpSeven.getSelectedItem().toString();
                selectedAnswerNine = activeDrpEight.getSelectedItem().toString();
                selectedAnswerTen = activeDrpNine.getSelectedItem().toString();
                String passwordCheck = "";
                EditText textView = (EditText)v.findViewById(R.id.editText);
                String password = textView.getText().toString();
                try{
                    FileInputStream fisPass = context.openFileInput("PasswordMain.txt");
                    int ch;
                    StringBuilder builder = new StringBuilder();
                    while ((ch = fisPass.read()) != -1) {
                        builder.append((char) ch);
                    }
                    passwordCheck = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(password.equals(passwordCheck)){
                    android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
                    boolean[] multichoiceAnswers = new boolean[]{
                            bElevenOne,
                            bElevenTwo,
                            bElevenThree,
                            bElevenFour,
                            bElevenFive,
                            bElevenSix,
                            bElevenSeven,
                            bElevenEight,
                            bElevenNine,
                            bElevenTen,
                            bElevenEleven,
                            bElevenTwelve,
                            bElevenThirteen,
                            bElevenFourteen
                    };
                    String[] Answers  = new String[]{
                            selectedAnswerOne,
                            selectedAnswerTwo,
                            selectedAnswerThree,
                            selectedAnswerFour,
                            selectedAnswerFive,
                            selectedAnswerSix,
                            selectedAnswerSeven,
                            selectedAnswerEight,
                            selectedAnswerNine,
                            selectedAnswerTen,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    };
                    String submitString = title;
                    for(int i = 0; i<QIDs.length;i++){
                        if(i<10){
                            submitString+=QIDs[i]+","+Answers[i]+"\n";
                        }else{
                            if(i==10){
                                for(int j = 0; j<multichoiceAnswers.length;j++){
                                    if(multichoiceAnswers[j]){
                                        submitString+=QIDs[i]+","+questionElevenAnswers[j]+"\n";
                                    }
                                }
                            }else{
                                switch(i){
                                    case 11:
                                        submitString+=QIDs[i]+","+qTwelveProgressChangedValue+"\n";
                                        break;
                                    case 12:
                                        submitString+=QIDs[i]+","+qThirteenProgressChangedValue+"\n";
                                        break;
                                    case 13:
                                        submitString+=QIDs[i]+","+qFourteenProgressChangedValue+"\n";
                                        break;
                                    case 14:
                                        submitString+=QIDs[i]+","+qFifteenOneProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qFifteenTwoProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qFifteenThreeProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qFifteenFourProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qFifteenFiveProgressChangedValue+"\n";
                                        break;
                                    case 15:
                                        submitString+=QIDs[i]+","+qSixteenOneProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qSixteenTwoProgressChangedValue+"\n";
                                        break;
                                    case 16:
                                        submitString+=QIDs[i]+","+qSeventeenOneProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qSeventeenTwoProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qSeventeenThreeProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qSeventeenFourProgressChangedValue+"\n";
                                        break;
                                    case 17:
                                        submitString+=QIDs[i]+","+qEighteenProgressChangedValue+"\n";
                                        break;
                                    case 18:
                                        submitString+=QIDs[i]+","+qNineteenProgressChangedValue+"\n";
                                        break;
                                    case 19:
                                        submitString+=QIDs[i]+","+qTwentyProgressChangedValue+"\n";
                                        break;
                                    case 20:
                                        submitString+=QIDs[i]+","+qTwentyoneProgressChangedValue+"\n";
                                        break;
                                    case 21:
                                        submitString+=QIDs[i]+","+qTwentytwoProgressChangedValue+"\n";
                                        break;
                                    case 22:
                                        submitString+=QIDs[i]+","+qTwentythreeProgressChangedValue+"\n";
                                        break;
                                    case 23:
                                        submitString+=QIDs[i]+","+qTwentyfourProgressChangedValue+"\n";
                                        break;
                                    case 24:
                                        submitString+=QIDs[i]+","+qTwentyfiveProgressChangedValue+"\n";
                                        break;
                                    case 25:
                                        submitString+=QIDs[i]+","+qTwentysixOneProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentysixTwoProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentysixThreeProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentysixFourProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentysixFiveProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentysixSixProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentysixSevenProgressChangedValue+"\n";
                                        break;
                                    case 26:
                                        submitString+=QIDs[i]+","+qTwentysevenProgressChangedValue+"\n";
                                        break;
                                    case 27:
                                        submitString+=QIDs[i]+","+qTwentyeightOneProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightTwoProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightThreeProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightFourProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightFiveProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightSixProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightSevenProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightEightProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightNineProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightTenProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightElevenProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightTwelveProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightThirteenProgressChangedValue+"\n";
                                        submitString+=QIDs[i]+","+qTwentyeightFourteenProgressChangedValue+"\n";
                                        break;
                                }
                            }
                        }
                    }
                    String fileName = android_id+"DEMOGRAPHICS.txt";
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    FileOutputStream fOut=null;
                    File file1 = new File(root+ File.separator + fileName);
                    if(!file1.exists()) {
                        try {
                            file1.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        fOut = new FileOutputStream(file1);
                        fOut.write(submitString.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String toastString = "Submitted";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                    SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    settings.edit().putBoolean("unanswered",false).commit();
                    dismiss();
                }else{
                    String toastString = "Incorrect Password";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                }
            }
        });
        return v;
    }
}
