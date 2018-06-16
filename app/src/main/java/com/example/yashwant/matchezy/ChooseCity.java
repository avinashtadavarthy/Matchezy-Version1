package com.example.yashwant.matchezy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ChooseCity extends AppCompatActivity {

    String[] cities = {
            "Abu", "Adoni", "Agartala", "Agra", "Ahmadabad", "Ahmadnagar", "Aizawl", "Ajmer", "Akola", "Alappuzha", "Aligarh", "Alipore", "AlipurDuar",
            "Allahabad", "Almora", "Alwar", "Amaravati", "Ambala", "Ambikapur", "Amer", "Amravati", "Amreli", "Amritsar", "Amroha", "Anantapur", "Anantnag",
            "Ara", "Arcot", "Asansol", "Aurangabad", "Ayodhya", "Azamgarh", "Badagara", "Badami", "Baharampur", "Bahraich", "Balaghat", "Balangir", "Baleshwar",
            "Ballari", "Ballia", "Bally", "Balurghat", "Banda", "Bangalore", "Bankura", "BaraBanki", "Baramula", "Baranagar", "Barasat", "Bareilly", "Baripada",
            "Barmer", "Barrackpore", "Baruni", "Barwani", "Basirhat", "Basti", "Batala", "Beawar", "Begusarai", "Belgavi", "Bettiah", "Betul", "Bhadravati",
            "Bhagalpur", "Bhandara", "Bharatpur", "Bharhut", "Bharuch", "Bhatpara", "Bhavnagar", "Bhilai", "Bhilwara", "Bhind", "Bhiwani", "Bhojpur", "Bhopal",
            "Bhubaneshwar", "Bhuj", "Bhusawal", "Bid", "Bidar", "BiharSharif", "Bijnor", "Bikaner", "Bilaspur", "Bilaspur", "Bishnupur", "Bithur", "BodhGaya",
            "Bokaro", "Brahmapur", "Budaun", "BudgeBudge", "Bulandshahr", "Buldana", "Bundi", "Burdwan", "Burhanpur", "Buxar", "Chaibasa", "Chamba",
            "Chandernagore", "Chandigarh", "Chandigarh", "Chandigarh", "Chandragiri", "Chandrapur", "Chapra", "Chengalpattu", "Chennai", "Cherrapunji",
            "Chhatarpur", "Chhindwara", "Chidambaram", "Chikkamagaluru", "Chitradurga", "Chittaurgarh", "Chittoor", "Churu", "Coimbatore", "Cuddalore",
            "Cuttack", "Dalhousie", "Daman", "Damoh", "Darbhanga", "Darjiling", "Datia", "Daulatabad", "Davangere", "DehraDun", "Dehri", "Delhi", "Deoghar",
            "Deoria", "Dewas", "Dhamtari", "Dhanbad", "Dhar", "Dharmapuri", "Dharmshala", "Dhaulpur", "Dhenkanal", "Dhuburi", "Dhule", "Diamond", "Dibrugarh",
            "DinapurNizamat", "Dindigul", "Dispur", "Diu", "Doda", "Dowlaiswaram", "DumDum", "Dumka", "Dungarpur", "Durg", "Durgapur", "Dwarka", "Eluru",
            "Erode", "Etah", "Etawah", "Faizabad", "Faridabad", "Faridkot", "Farrukhabad", "Fatehgarh", "Fatehpur", "FatehpurSikri", "Firozpur",
            "FirozpurJhirka", "Gandhinagar", "Ganganagar", "Gangtok", "Gaya", "Ghaziabad", "Ghazipur", "Giridih", "Godhra", "Gonda", "Gorakhpur",
            "Gulmarg", "Guna", "Guntur", "Gurdaspur", "Gurgaon", "Guwahati", "Gwalior", "Gyalsing", "Hajipur", "Halebid", "Halisahar", "Hamirpur", "Hamirpur",
            "Hansi", "Hanumangarh", "Haora", "Harbour", "Hardoi", "Haridwar", "Hassan", "Hathras", "Hazaribag", "Hisar", "Hoshangabad", "Hoshiarpur",
            "Hubballi-Dharwad", "Hugli", "Hyderabad", "Idukki", "Imphal", "Indore", "IngrajBazar", "Itanagar", "Itarsi", "Jabalpur", "Jagdalpur", "Jaipur",
            "Jaisalmer", "Jalandhar", "Jalaun", "Jalgaon", "Jalor", "Jalpaiguri", "Jamalpur", "Jammu", "Jamnagar", "Jamshedpur", "Jaunpur", "Jhabua",
            "Jhalawar", "Jhansi", "Jharia", "Jhunjhunu", "Jind", "Jodhpur", "Jorhat", "Junagadh", "Kadapa", "Kaithal", "Kakinada", "Kalaburagi", "Kalimpong",
            "Kalyan", "Kamarhati", "Kanchipuram", "Kanchrapara", "Kandla", "Kangra", "Kannauj", "Kanniyakumari", "Kannur", "Kanpur", "Kapurthala", "Karaikal",
            "Karimnagar", "Karli", "Karnal", "Kathua", "Katihar", "Keonjhar", "Khajuraho", "Khambhat", "Khammam", "Khandwa", "Kharagpur", "Khargon", "Kheda",
            "Kishangarh", "KochBihar", "Kochi", "Kodaikanal", "Kohima", "Kolar", "Kolhapur", "Kolkata", "Kollam", "Konark", "Koraput", "Kota", "Kottayam",
            "Kozhikode", "Krishnanagar", "Kullu", "Kumbakonam", "Kurnool", "Kurukshetra", "Lachung", "Lakhimpur", "Lalitpur", "Leh", "Lucknow", "Ludhiana",
            "Lunglei", "Machilipatnam", "Madgaon", "Madhubani", "Madikeri", "Madurai", "Mahabaleshwar", "Mahbubnagar", "Mahe", "Mahesana", "Maheshwar",
            "Mainpuri", "Malda", "Malegaon", "Mamallapuram", "Mandi", "Mandla", "Mandsaur", "Mandya", "Mangaluru", "Mangan", "Matheran", "Mathura",
            "Mattancheri", "Meerut", "Merta", "Mhow", "Midnapore", "Mirzapur-Vindhyachal", "Mon", "Moradabad", "Morena", "Morvi", "Motihari", "Mumbai",
            "Munger", "Murshidabad", "Murwara", "Mussoorie", "Muzaffarnagar", "Muzaffarpur", "Mysuru", "Nabha", "Nadiad", "Nagaon", "Nagappattinam",
            "Nagarjunakonda", "Nagaur", "Nagercoil", "Nagpur", "Nahan", "Nainital", "Nanded", "Narsimhapur", "Narsinghgarh", "Narwar", "Nashik", "Nathdwara",
            "Navadwip", "Navsari", "Neemuch", "NewDelhi", "Nizamabad", "Nowgong", "Okha", "Orchha", "Osmanabad", "Palakkad", "Palanpur", "Palashi",
            "Palayankottai", "Pali", "Panaji", "Pandharpur", "Panihati", "Panipat", "Panna", "Paradip", "Parbhani", "Partapgarh", "Patan", "Patiala",
            "Patna", "Pehowa", "Phalodi", "Phek", "Phulabani", "Pilibhit", "Pithoragarh", "Porbandar", "PortBlair", "Puducherry", "Pudukkottai", "Punch",
            "Pune", "Puri", "Purnia", "Purulia", "Pusa", "Pushkar", "RaeBareli", "Raichur", "Raiganj", "Raipur", "Raisen", "Rajahmundry", "Rajapalaiyam",
            "Rajauri", "Rajgarh", "Rajkot", "Rajmahal", "Rajnandgaon", "Ramanathapuram", "Rampur", "Ranchi", "Ratlam", "Ratnagiri", "Rewa", "Rewari", "Rohtak",
            "Rupnagar", "Sagar", "Saharanpur", "Saharsa", "Salem", "Samastipur", "Sambalpur", "Sambhal", "Sangareddi", "Sangli", "Sangrur", "Santipur",
            "Saraikela", "Sarangpur", "Sasaram", "Satara", "Satna", "SawaiMadhopur", "Sehore", "Seoni", "Sevagram", "Shahdol", "Shahjahanpur", "Shahpura",
            "Shajapur", "Shantiniketan", "Sheopur", "Shillong", "Shimla", "Shivamogga", "Shivpuri", "Shravanabelagola", "Shrirampur", "Shrirangapattana",
            "Sibsagar", "Sikar", "Silchar", "Siliguri", "Silvassa", "Sirohi", "Sirsa", "Sitamarhi", "Sitapur", "Siuri", "Siwan", "Solapur", "Sonipat",
            "Srikakulam", "Srinagar", "Sultanpur", "Surat", "Surendranagar", "Tamluk", "Tehri", "Tezpur", "Thalassery", "Thane", "Thanjavur",
            "Thiruvananthapuram", "Thrissur", "Tinsukia", "Tiruchchirappalli", "Tirunelveli", "Tirupati", "Tiruppur", "Titagarh", "Tonk", "Tumkuru",
            "Tuticorin", "Udaipur", "Udayagiri", "Udhagamandalam", "Udhampur", "Ujjain", "Ulhasnagar", "Una", "Valsad", "Varanasi", "Vasai-Virar", "Vellore",
            "Veraval", "Vidisha", "Vijayawada", "Visakhapatnam", "Vizianagaram", "Warangal", "Wardha", "Wokha", "Yanam", "Yavatmal", "Yemmiganur", "Zunheboto",
            "Algeria", "Argentina", "Australia", "Austria", "Bangladesh", "Belgium", "Brazil", "Bulgaria", "Canada", "China", "Czech Republic", "Denmark",
            "Egypt", "England", "Finland", "France", "Germany", "Greece", "Hungary", "Iceland", "India", "Indonesia", "Indonesia", "Iraq", "Ireland", "Israel",
            "Italy", "Jamaica", "Japan", "Kenya", "Malaysia", "Mexico", "Mexico", "Morocco", "Nepal", "Netherlands", "New Zealand", "Nigeria", "Norway",
            "Pakistan", "Peru", "Phillipines", "Poland", "Portugal", "Qatar", "Russia", "Russia", "Scotland", "Singapore", "South Africa", "South Korea",
            "Spain", "Sri Lanka", "Sweden", "Switzerland", "Taiwan", "Tanzania", "Thailand", "Turkey", "United Arab Emirates", "United Kingdom",
            "United States", "Vietnam", "Wales", "Other"
    };


    private EditText filterText;
    private ListView itemList;
    private ArrayAdapter<String> listAdapter;
    private String category;

    String selectedcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));


        filterText = (EditText)findViewById(R.id.editText);
        itemList = (ListView)findViewById(R.id.listView);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, cities);

        itemList.setAdapter(listAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                filterText.setText(listAdapter.getItem(position));

                Intent intent = new Intent();
                intent.putExtra("chosencity", listAdapter.getItem(position));
                setResult(Activity.RESULT_OK, intent);

                finish();

            }
        });

        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ChooseCity.this.listAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    //shared pref
    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

}
