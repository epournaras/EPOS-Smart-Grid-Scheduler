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
    final String PREFS_NAME = "MyPrefs";

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
    private String selectedAnswerEleven;
    private String selectedAnswerTwelve;
    private String selectedAnswerThirteen;
    private String selectedAnswerFourteen;
    private String selectedAnswerFifteen;
    private String selectedAnswerSixteen;
    private String selectedAnswerSeventeen;
    private String selectedAnswerEighteen;
    private String selectedAnswerNineteen;
    private String selectedAnswerTwenty;
    private String selectedAnswerTwentyOne;
    private String selectedAnswerTwentyTwo;
    private String selectedAnswerTwentyThree;
    private String selectedAnswerTwentyFour;
    private String selectedAnswerTwentyFive;
    private String selectedAnswerTwentySix;
    private String selectedAnswerTwentySeven;
    private String selectedAnswerTwentyEight;

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
    private String[] questionTwelveAnswers;
    private String[] questionThirteenAnswers;
    private String[] questionFourteenAnswers;
    private String[] questionFifteenAnswers;
    private String[] questionSixteenAnswers;
    private String[] questionSeventeenAnswers;
    private String[] questionEighteenAnswers;
    private String[] questionNineteenAnswers;
    private String[] questionTwentyAnswers;
    private String[] questionTwentyOneAnswers;
    private String[] questionTwentyTwoAnswers;
    private String[] questionTwentyThreeAnswers;
    private String[] questionTwentyFourAnswers;
    private String[] questionTwentyFiveAnswers;
    private String[] questionTwentySixAnswers;
    private String[] questionTwentySevenAnswers;
    private String[] questionTwentyEightAnswers;
    private CheckBox qElevenOne, qElevenTwo,qElevenThree,qElevenFour,qElevenFive,qElevenSix,qElevenSeven,qElevenEight,qElevenNine,qElevenTen,qElevenEleven,qElevenTwelve,qElevenThirteen,qElevenFourteen,
                    qFifteenOne,qFifteenTwo,qFifteenThree,qFifteenFour,qFifteenFive,
                    qSixteenOne,qSixteenTwo,
                    qSeventeenOne,qSeventeenTwo,qSeventeenThree,qSeventeenFour,
                    qTwentySixOne,qTwentySixTwo,qTwentySixThree,qTwentySixFour,qTwentySixFive,qTwentySixSix,qTwentySixSeven,
                    qTwentyEightOne, qTwentyEightTwo,qTwentyEightThree,qTwentyEightFour,qTwentyEightFive,qTwentyEightSix,qTwentyEightSeven,qTwentyEightEight,qTwentyEightNine,qTwentyEightTen,qTwentyEightEleven,
                    qTwentyEightTwelve,qTwentyEightThirteen,qTwentyEightFourteen,qTwentyEightFifteen;
    private boolean bElevenOne, bElevenTwo,bElevenThree,bElevenFour,bElevenFive,bElevenSix,bElevenSeven,bElevenEight,bElevenNine,bElevenTen,bElevenEleven,bElevenTwelve,bElevenThirteen,bElevenFourteen,
            bFifteenOne,bFifteenTwo,bFifteenThree,bFifteenFour,bFifteenFive,
            bSixteenOne,bSixteenTwo,
            bSeventeenOne,bSeventeenTwo,bSeventeenThree,bSeventeenFour,
            bTwentySixOne,bTwentySixTwo,bTwentySixThree,bTwentySixFour,bTwentySixFive,bTwentySixSix,bTwentySixSeven,
            bTwentyEightOne, bTwentyEightTwo,bTwentyEightThree,bTwentyEightFour,bTwentyEightFive,bTwentyEightSix,bTwentyEightSeven,bTwentyEightEight,bTwentyEightNine,bTwentyEightTen,bTwentyEightEleven,
            bTwentyEightTwelve,bTwentyEightThirteen,bTwentyEightFourteen,bTwentyEightFifteen = false;
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

        questionOneAnswers = new String[100];
        for(int i = 99;i>=0;i--){
            questionOneAnswers[questionOneAnswers.length-1-i] = "19"+i;
        }
        questionTwoAnswers = new String[]{"Afghanistan",
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
                "Male",
                "Female",
                "Other"
        };
        questionFourAnswers = new String[]{"Afghanistan",
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
                "Detached",
                "Semi-detached",
                "Mid-terrace",
                "Apartment/Flat",
                "Other"
        };
        questionEightAnswers = new String[]{
                "1 Bed",
                "2 Bed",
                "3 Bed",
                "4 Bed",
                "5 Bed",
                "6 Bed +"
        };
        questionNineAnswers = new String[]{
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

        questionTwelveAnswers = new String[]{
                "Yes - extremely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionThirteenAnswers = new String[]{
                "Yes - extremely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionFourteenAnswers = new String[]{
                "Yes - extremely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionFifteenAnswers = new String[]{
                "Reduce my energy bill",
                "Contribute to grid reliability, e.g prevent a blackout",
                "Protect the environment",
                "Others do, so I do",
                "Others do not, so I do"
        };

        questionSixteenAnswers = new String[]{
                "Lowering the consumption of appliances",
                "Shifting the consumption of appliances at different tiems, e.g. during off-peak night times."
        };

        questionSeventeenAnswers = new String[]{
                "Feeling cold in cold winters or feeling warm in warm summers.",
                "Extra costs for special equipment and appliances.",
                "Changing my overall lifestyle at home.",
                "Doing my daily residential activities at different and maybe undesirable times."
        };

        questionEighteenAnswers = new String[]{
                "Yes - extremely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionNineteenAnswers = new String[]{
                "Yes - extremely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionTwentyAnswers = new String[]{
                "Yes - extremely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionTwentyOneAnswers = new String[]{
                "Yes - Definitely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };
        questionTwentyTwoAnswers = new String[]{
                "Yes - Definitely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };
        questionTwentyThreeAnswers = new String[]{
                "Yes - Definitely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };
        questionTwentyFourAnswers = new String[]{
                "Yes - Definitely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };
        questionTwentyFiveAnswers = new String[]{
                "Yes - Definitely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        questionTwentySixAnswers = new String[]{
                "30 minutes ahead",
                "1 hour ahead",
                "3 hours ahead",
                "6 hours ahead",
                "12 hours ahead",
                "24 hours ahead",
                "1 week ahead"
        };

        questionTwentySevenAnswers = new String[]{
                "6 - Very High Discomfort",
                "5 - High Discomfort",
                "4 - Moderate Discomfort",
                "3 - Light Discomfort",
                "2 - Minimal Discomfort",
                "1 - No Discomfort"
        };

        questionTwentyEightAnswers = new String[]{
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
                "Fridge",
                "Lighting"
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

        qFifteenOne= (CheckBox)v.findViewById((R.id.Q15chk1));
        qFifteenTwo= (CheckBox)v.findViewById((R.id.Q15chk2));
        qFifteenThree= (CheckBox)v.findViewById((R.id.Q15chk3));
        qFifteenFour= (CheckBox)v.findViewById((R.id.Q15chk4));
        qFifteenFive= (CheckBox)v.findViewById((R.id.Q15chk5));

        qFifteenOne.setText(questionFifteenAnswers[0]);
        qFifteenTwo.setText(questionFifteenAnswers[1]);
        qFifteenThree.setText(questionFifteenAnswers[2]);
        qFifteenFour.setText(questionFifteenAnswers[3]);
        qFifteenFive.setText(questionFifteenAnswers[4]);

        qFifteenOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bFifteenOne = true;
                }else{
                    bFifteenOne = false;
                }
            }
        });

        qFifteenTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bFifteenTwo = true;
                }else{
                    bFifteenTwo = false;
                }
            }
        });

        qFifteenThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bFifteenThree = true;
                }else{
                    bFifteenThree = false;
                }
            }
        });

        qFifteenFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bFifteenFour = true;
                }else{
                    bFifteenFour = false;
                }
            }
        });

        qFifteenFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bFifteenFive = true;
                }else{
                    bFifteenFive = false;
                }
            }
        });

        qSixteenOne= (CheckBox)v.findViewById((R.id.Q16chk1));
        qSixteenTwo= (CheckBox)v.findViewById((R.id.Q16chk2));

        qSixteenOne.setText(questionSixteenAnswers[0]);
        qSixteenTwo.setText(questionSixteenAnswers[1]);

        qSixteenOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bSixteenOne = true;
                }else{
                    bSixteenOne = false;
                }
            }
        });

        qSixteenTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bSixteenTwo = true;
                }else{
                    bSixteenTwo = false;
                }
            }
        });

        qSeventeenOne= (CheckBox)v.findViewById((R.id.Q17chk1));
        qSeventeenTwo= (CheckBox)v.findViewById((R.id.Q17chk2));
        qSeventeenThree= (CheckBox)v.findViewById((R.id.Q17chk3));
        qSeventeenFour= (CheckBox)v.findViewById((R.id.Q17chk4));

        qSeventeenOne.setText(questionSeventeenAnswers[0]);
        qSeventeenTwo.setText(questionSeventeenAnswers[1]);
        qSeventeenThree.setText(questionSeventeenAnswers[2]);
        qSeventeenFour.setText(questionSeventeenAnswers[3]);

        qSeventeenOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bSeventeenOne = true;
                }else{
                    bSeventeenOne = false;
                }
            }
        });

        qSeventeenTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bSeventeenTwo = true;
                }else{
                    bSeventeenTwo = false;
                }
            }
        });

        qSeventeenThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bSeventeenThree = true;
                }else{
                    bSeventeenThree = false;
                }
            }
        });

        qSeventeenFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bSeventeenFour = true;
                }else{
                    bSeventeenFour = false;
                }
            }
        });

        qTwentySixOne= (CheckBox)v.findViewById((R.id.Q26chk1));
        qTwentySixTwo= (CheckBox)v.findViewById((R.id.Q26chk2));
        qTwentySixThree= (CheckBox)v.findViewById((R.id.Q26chk3));
        qTwentySixFour= (CheckBox)v.findViewById((R.id.Q26chk4));
        qTwentySixFive= (CheckBox)v.findViewById((R.id.Q26chk5));
        qTwentySixSix= (CheckBox)v.findViewById((R.id.Q26chk6));
        qTwentySixSeven= (CheckBox)v.findViewById((R.id.Q26chk7));

        qTwentySixOne.setText(questionTwentySixAnswers[0]);
        qTwentySixTwo.setText(questionTwentySixAnswers[1]);
        qTwentySixThree.setText(questionTwentySixAnswers[2]);
        qTwentySixFour.setText(questionTwentySixAnswers[3]);
        qTwentySixFive.setText(questionTwentySixAnswers[4]);
        qTwentySixSix.setText(questionTwentySixAnswers[5]);
        qTwentySixSeven.setText(questionTwentySixAnswers[6]);

        qTwentySixOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixOne = true;
                }else{
                    bTwentySixOne = false;
                }
            }
        });

        qTwentySixTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixTwo = true;
                }else{
                    bTwentySixTwo = false;
                }
            }
        });

        qTwentySixThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixThree = true;
                }else{
                    bTwentySixThree = false;
                }
            }
        });

        qTwentySixFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixFour = true;
                }else{
                    bTwentySixFour = false;
                }
            }
        });

        qTwentySixFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixFive = true;
                }else{
                    bTwentySixFive = false;
                }
            }
        });

        qTwentySixSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixSix = true;
                }else{
                    bTwentySixSix = false;
                }
            }
        });

        qTwentySixSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentySixSeven = true;
                }else{
                    bTwentySixSeven = false;
                }
            }
        });

        qTwentyEightOne= (CheckBox)v.findViewById((R.id.Q28chk1));
        qTwentyEightTwo= (CheckBox)v.findViewById((R.id.Q28chk2));
        qTwentyEightThree= (CheckBox)v.findViewById((R.id.Q28chk3));
        qTwentyEightFour= (CheckBox)v.findViewById((R.id.Q28chk4));
        qTwentyEightFive= (CheckBox)v.findViewById((R.id.Q28chk5));
        qTwentyEightSix= (CheckBox)v.findViewById((R.id.Q28chk6));
        qTwentyEightSeven= (CheckBox)v.findViewById((R.id.Q28chk7));
        qTwentyEightEight= (CheckBox)v.findViewById((R.id.Q28chk8));
        qTwentyEightNine= (CheckBox)v.findViewById((R.id.Q28chk9));
        qTwentyEightTen= (CheckBox)v.findViewById((R.id.Q28chk10));
        qTwentyEightEleven= (CheckBox)v.findViewById((R.id.Q28chk11));
        qTwentyEightTwelve= (CheckBox)v.findViewById((R.id.Q28chk12));
        qTwentyEightThirteen= (CheckBox)v.findViewById((R.id.Q28chk13));
        qTwentyEightFourteen= (CheckBox)v.findViewById((R.id.Q28chk14));
        qTwentyEightFifteen= (CheckBox)v.findViewById((R.id.Q28chk15));

        qTwentyEightOne.setText(questionTwentyEightAnswers[0]);
        qTwentyEightTwo.setText(questionTwentyEightAnswers[1]);
        qTwentyEightThree.setText(questionTwentyEightAnswers[2]);
        qTwentyEightFour.setText(questionTwentyEightAnswers[3]);
        qTwentyEightFive.setText(questionTwentyEightAnswers[4]);
        qTwentyEightSix.setText(questionTwentyEightAnswers[5]);
        qTwentyEightSeven.setText(questionTwentyEightAnswers[6]);
        qTwentyEightEight.setText(questionTwentyEightAnswers[7]);
        qTwentyEightNine.setText(questionTwentyEightAnswers[8]);
        qTwentyEightTen.setText(questionTwentyEightAnswers[9]);
        qTwentyEightEleven.setText(questionTwentyEightAnswers[10]);
        qTwentyEightTwelve.setText(questionTwentyEightAnswers[11]);
        qTwentyEightThirteen.setText(questionTwentyEightAnswers[12]);
        qTwentyEightFourteen.setText(questionTwentyEightAnswers[13]);
        qTwentyEightFifteen.setText(questionTwentyEightAnswers[14]);

        qTwentyEightOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightOne = true;
                }else{
                    bTwentyEightOne = false;
                }
            }
        });

        qTwentyEightTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightTwo = true;
                }else{
                    bTwentyEightTwo = false;
                }
            }
        });

        qTwentyEightThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightThree = true;
                }else{
                    bTwentyEightThree = false;
                }
            }
        });

        qTwentyEightFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightFour = true;
                }else{
                    bTwentyEightFour = false;
                }
            }
        });

        qTwentyEightFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightFive = true;
                }else{
                    bTwentyEightFive = false;
                }
            }
        });

        qTwentyEightSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightSix = true;
                }else{
                    bTwentyEightSix = false;
                }
            }
        });

        qTwentyEightSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightSeven = true;
                }else{
                    bTwentyEightSeven = false;
                }
            }
        });

        qTwentyEightEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightEight = true;
                }else{
                    bTwentyEightEight = false;
                }
            }
        });

        qTwentyEightNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightNine = true;
                }else{
                    bTwentyEightNine = false;
                }
            }
        });

        qTwentyEightTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightTen = true;
                }else{
                    bTwentyEightTen = false;
                }
            }
        });

        qTwentyEightEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightEleven = true;
                }else{
                    bTwentyEightEleven = false;
                }
            }
        });

        qTwentyEightTwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightTwelve = true;
                }else{
                    bTwentyEightTwelve = false;
                }
            }
        });

        qTwentyEightThirteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightThirteen = true;
                }else{
                    bTwentyEightThirteen = false;
                }
            }
        });

        qTwentyEightFourteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightFourteen = true;
                }else{
                    bTwentyEightFourteen = false;
                }
            }
        });

        qTwentyEightFifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    bTwentyEightFifteen = true;
                }else{
                    bTwentyEightFifteen = false;
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
        activeDrpSeven.setAdapter(adapterActiveSix);
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

        ArrayAdapter<CharSequence> adapterActiveEleven;
        final Spinner activeDrpEleven = (Spinner)v.findViewById(R.id.spinnerAnswers11);
        adapterActiveEleven = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwelveAnswers);
        adapterActiveEleven.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpEleven.setAdapter(adapterActiveEleven);
        selectedAnswerTwelve = activeDrpEleven.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwelve;
        final Spinner activeDrpTwelve = (Spinner)v.findViewById(R.id.spinnerAnswers12);
        adapterActiveTwelve = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionThirteenAnswers);
        adapterActiveTwelve.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwelve.setAdapter(adapterActiveTwelve);
        selectedAnswerThirteen = activeDrpTwelve.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveThirteen;
        final Spinner activeDrpThirteen = (Spinner)v.findViewById(R.id.spinnerAnswers13);
        adapterActiveThirteen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionFourteenAnswers);
        adapterActiveThirteen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpThirteen.setAdapter(adapterActiveThirteen);
        selectedAnswerFourteen = activeDrpThirteen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveSeventeen;
        final Spinner activeDrpSeventeen = (Spinner)v.findViewById(R.id.spinnerAnswers17);
        adapterActiveSeventeen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionEighteenAnswers);
        adapterActiveSeventeen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpSeventeen.setAdapter(adapterActiveSeventeen);
        selectedAnswerEighteen = activeDrpSeventeen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveEighteen;
        final Spinner activeDrpEighteen = (Spinner)v.findViewById(R.id.spinnerAnswers18);
        adapterActiveEighteen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionNineteenAnswers);
        adapterActiveEighteen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpEighteen.setAdapter(adapterActiveEighteen);
        selectedAnswerNineteen = activeDrpEighteen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveNineteen;
        final Spinner activeDrpNineteen = (Spinner)v.findViewById(R.id.spinnerAnswers19);
        adapterActiveNineteen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentyAnswers);
        adapterActiveNineteen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpNineteen.setAdapter(adapterActiveNineteen);
        selectedAnswerTwenty = activeDrpNineteen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwenty;
        final Spinner activeDrpTwenty = (Spinner)v.findViewById(R.id.spinnerAnswers20);
        adapterActiveTwenty = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentyOneAnswers);
        adapterActiveTwenty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwenty.setAdapter(adapterActiveTwenty);
        selectedAnswerTwentyOne = activeDrpTwenty.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwentyOne;
        final Spinner activeDrpTwentyOne = (Spinner)v.findViewById(R.id.spinnerAnswers21);
        adapterActiveTwentyOne = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentyTwoAnswers);
        adapterActiveTwentyOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwentyOne.setAdapter(adapterActiveTwentyOne);
        selectedAnswerTwentyTwo = activeDrpTwentyOne.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwentyTwo;
        final Spinner activeDrpTwentyTwo = (Spinner)v.findViewById(R.id.spinnerAnswers22);
        adapterActiveTwentyTwo = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentyThreeAnswers);
        adapterActiveTwentyTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwentyTwo.setAdapter(adapterActiveTwentyTwo);
        selectedAnswerTwentyThree = activeDrpTwentyTwo.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwentyThree;
        final Spinner activeDrpTwentyThree = (Spinner)v.findViewById(R.id.spinnerAnswers23);
        adapterActiveTwentyThree= new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentyFourAnswers);
        adapterActiveTwentyThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwentyThree.setAdapter(adapterActiveTwentyThree);
        selectedAnswerTwentyFour = activeDrpTwentyThree.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwentyFour;
        final Spinner activeDrpTwentyFour = (Spinner)v.findViewById(R.id.spinnerAnswers24);
        adapterActiveTwentyFour = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentyFiveAnswers);
        adapterActiveTwentyFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwentyFour.setAdapter(adapterActiveTwentyFour);
        selectedAnswerTwentyFive = activeDrpTwentyFour.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwentySix;
        final Spinner activeDrpTwentySix = (Spinner)v.findViewById(R.id.spinnerAnswers26);
        adapterActiveTwentySix = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwentySevenAnswers);
        adapterActiveTwentySix.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwentySix.setAdapter(adapterActiveTwentySix);
        selectedAnswerTwentySeven = activeDrpTwentySix.getSelectedItem().toString();



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
                    boolean[][] multichoiceAnswers = new boolean[][]{
                            {bElevenOne, bElevenTwo,bElevenThree,bElevenFour,bElevenFive,bElevenSix,bElevenSeven,bElevenEight,bElevenNine,bElevenTen,bElevenEleven,bElevenTwelve,bElevenThirteen,bElevenFourteen},
                            {bFifteenOne,bFifteenTwo,bFifteenThree,bFifteenFour,bFifteenFive},
                            {bSixteenOne,bSixteenTwo},{bSeventeenOne,bSeventeenTwo,bSeventeenThree,bSeventeenFour},
                            {bTwentySixOne,bTwentySixTwo,bTwentySixThree,bTwentySixFour,bTwentySixFive,bTwentySixSix,bTwentySixSeven},
                            {bTwentyEightOne, bTwentyEightTwo,bTwentyEightThree,bTwentyEightFour,bTwentyEightFive,bTwentyEightSix,bTwentyEightSeven,
                                    bTwentyEightEight,bTwentyEightNine,bTwentyEightTen,bTwentyEightEleven,bTwentyEightTwelve,bTwentyEightThirteen,bTwentyEightFourteen, bTwentyEightFifteen}
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
                            selectedAnswerTwelve,
                            selectedAnswerThirteen,
                            selectedAnswerFourteen,
                            null,
                            null,
                            null,
                            selectedAnswerEighteen,
                            selectedAnswerNineteen,
                            selectedAnswerTwenty,
                            selectedAnswerTwentyOne,
                            selectedAnswerTwentyTwo,
                            selectedAnswerTwentyThree,
                            selectedAnswerTwentyFour,
                            selectedAnswerTwentyFive,
                            null,
                            selectedAnswerTwentySeven,
                            null
                    };
                    String submitString = title;
                    for(int i = 0; i<QIDs.length;i++){
                        if(i!=10&&i!=14&&i!=15&&i!=16&&i!=25&&i!=27){
                            submitString+=QIDs[i]+","+Answers[i]+"\n";
                        }else{
                            switch(i){
                                case 10:
                                    for(int j = 0; j<multichoiceAnswers[0].length;j++){
                                        if(multichoiceAnswers[0][j]){
                                            submitString+=QIDs[i]+","+questionElevenAnswers[j]+"\n";
                                        }
                                    }
                                    break;
                                case 14:
                                    for(int j = 0; j<multichoiceAnswers[1].length;j++){
                                        if(multichoiceAnswers[1][j]){
                                            submitString+=QIDs[i]+","+questionFifteenAnswers[j]+"\n";
                                        }
                                    }
                                    break;
                                case 15:
                                    for(int j = 0; j<multichoiceAnswers[2].length;j++){
                                        if(multichoiceAnswers[2][j]){
                                            submitString+=QIDs[i]+","+questionSixteenAnswers[j]+"\n";
                                        }
                                    }
                                    break;
                                case 16:
                                    for(int j = 0; j<multichoiceAnswers[3].length;j++){
                                        if(multichoiceAnswers[3][j]){
                                            submitString+=QIDs[i]+","+questionSeventeenAnswers[j]+"\n";
                                        }
                                    }
                                    break;
                                case 25:
                                    for(int j = 0; j<multichoiceAnswers[4].length;j++){
                                        if(multichoiceAnswers[4][j]){
                                            submitString+=QIDs[i]+","+questionTwentySixAnswers[j]+"\n";
                                        }
                                    }
                                    break;
                                case 27:
                                    for(int j = 0; j<multichoiceAnswers[5].length;j++){
                                        if(multichoiceAnswers[5][j]){
                                            submitString+=QIDs[i]+","+questionTwentyEightAnswers[j]+"\n";
                                        }
                                    }
                                    break;
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
