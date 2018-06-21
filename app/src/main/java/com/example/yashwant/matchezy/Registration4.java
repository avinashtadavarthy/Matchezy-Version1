package com.example.yashwant.matchezy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.scalified.fab.ActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

public class Registration4 extends AppCompatActivity {


    EditText editText_work,editText_annual,editText_college,editText_edu,editText_desig;
    private TextInputLayout inputLayoutWorking, inputLayoutCollege, inputLayoutAnnual,inputLayoutEdu, inputLayoutDesig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);

        AndroidNetworking.initialize(this);

        
        inputLayoutAnnual=(TextInputLayout)findViewById(R.id.inputLayoutAnnualIncome);
        inputLayoutCollege=(TextInputLayout)findViewById(R.id.inputLayoutCollege);
        inputLayoutEdu=(TextInputLayout)findViewById(R.id.inputLayoutEdu);
        inputLayoutWorking=(TextInputLayout)findViewById(R.id.inputLayoutWorking);
        inputLayoutDesig=(TextInputLayout)findViewById(R.id.inputLayoutDesignation);

        editText_annual=(EditText)findViewById(R.id.editText_annual);
        editText_college=(EditText)findViewById(R.id.editTextCollege);
        editText_edu=(EditText)findViewById(R.id.editTextEdu);
        editText_work=(EditText)findViewById(R.id.editTextWorking);
        editText_desig=(EditText)findViewById(R.id.editTextDesignation);

        editText_edu.setShowSoftInputOnFocus(false);

        editText_edu.addTextChangedListener(new Registration4.MyTextWatcher(editText_edu));
        editText_college.addTextChangedListener(new Registration4.MyTextWatcher(editText_college));
        editText_work.addTextChangedListener(new Registration4.MyTextWatcher(editText_work));
        editText_annual.addTextChangedListener(new Registration4.MyTextWatcher(editText_annual));
        editText_desig.addTextChangedListener(new Registration4.MyTextWatcher(editText_desig));


        //add validation for designation and change up stuff, then make a user on server, fb error handling


        editText_edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                education();
            }
        });


        editText_edu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    education();
                }

            }
        });


        editText_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company();
            }
        });

        editText_work.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    company();
                }

            }
        });


        editText_college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                college();
            }
        });

        editText_college.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    college();
                }

            }
        });


        editText_desig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                designation();
            }
        });

        editText_desig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    designation();
                }

            }
        });




        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next3);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });

        /*editText_education= (EditText)findViewById(R.id.edit_edu);
        editText_income =(EditText)findViewById(R.id.edit_income);*/
    }

    private void submitForm() {
        if (!validateedu()) {
            return;
        }

        if (!validatecollege()) {
            return;
        }

        if(!validatework()) {
            return;
        }

        if(!validatedesig()) {
            return;
        }

        if(!validateannual()) {
            return;
        }

        storeSPData("education", editText_edu.getText().toString().trim());
        storeSPData("college", editText_college.getText().toString().trim());
        storeSPData("work", editText_work.getText().toString().trim());
        storeSPData("desig", editText_desig.getText().toString().trim());
        storeSPData("annual_income", editText_annual.getText().toString().trim());



        Intent intent = new Intent(getApplicationContext(),Registration_Interests.class);
        startActivity(intent);

    }

    private void education() {

        final String[] items = {"Aeronautical Engineering", "B.Arch", "BCA", "BE", "B.Plan", "B.Sc IT/ Computer Science", "B.Tech.",
                "Other Bachelor Degree in Engineering / Computers", "B.S.(Engineering)", "M.Arch.", "MCA", "ME", "M.Sc. IT / Computer Science",
                "M.S.(Engg.)", "M.Tech.", "PGDCA", "Other Masters Degree in Engineering / Computers", "Aviation Degree", "B.A.", "B.Com.", "B.Ed.",
                "BFA", "BFT", "BLIS", "B.M.M.", "B.Sc.", "B.S.W", "B.Phil.", "Other Bachelor Degree in Arts / Science / Commerce", "M.A.", "MCom", "M.Ed.",
                "MFA", "MLIS", "M.Sc.", "MSW", "M.Phil.", "Other Master Degree in Arts / Science / Commerce", "BBA", "BFM (Financial Management)",
                "BHM (Hotel Management)", "Other Bachelor Degree in Management", "MBA", "MFM (Financial Management)", "MHM  (Hotel Management)",
                "MHRM (Human Resource Management)", "PGDM", "Other Master Degree in Management", "B.A.M.S.", "BDS", "BHMS", "BSMS", "BPharm", "BPT",
                "BUMS", "BVSc", "MBBS", "B.Sc. Nursing", "Other Bachelor Degree in Medicine", "MDS", "MD / MS (Medical)", "M.Pharm", "MPT", "MVSc",
                "Other Master Degree in Medicine", "BGL", "B.L.", "LL.B.", "Other Bachelor Degree in Legal", "LL.M.", "M.L.", "Other Master Degree in  Legal",
                "CA", "CFA (Chartered Financial Analyst)", "CS", "ICWA", "Other Degree in Finance", "IAS", "IES", "IFS", "IRS", "IPS", "Other Degree in Service",
                "Ph.D.", "Diploma", "Polytechnic", "Trade School", "Others in Diploma"};

        //MultiSelectModel edu = new MultiSelectModel(1,"helo");

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        User.getInstance().populateModel(edu,items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Education") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .multiSelectList(edu) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        /*for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(Registration4.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }*/

                        editText_edu.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }


    private void company() {

        final String[] items = {
                "N/A", "A. T. Kearney", "A.P. Moller Maersk", "A.T. Kearney", "A2Z Group", "ABB", "Abbott India", "Abbott Nutrition International", "ABC Consultants", "ABECL", "Abercrombie & Fitch", "ABG Cement Ltd", "Abhijeet Group", "Abhishek Industries (Trident Group)", "ABN AMRO Bank", "ABP Pvt Ltd", "AbsolutData", "AC Nielsen", "ACC Limited", "Accel Frontline Limited", "Accenture",
                "Accountant", "Accretive Health", "Acer", "ACG Worldwide", "ACH Management Consultant Pvt. Ltd.", "Acme Telepower Ltd", "Acropetal Technologies Limited", "ACS - A Xerox Company", "Actis", "Actuate Business Consulting", "Adani Group", "Adani Wilmar Limited", "Adecco", "Adfactors PR", "ADFC Pvt Ltd", "Adhunik Group", "Adidas", "Aditi Technologies", "Aditya Birla Group", "Adobe", "ADP",
                "Adroit Financial Services", "Advance Group", "Advertising", "Aegis Limited", "Aegon Religare", "Aetna", "AF-Mercados EMI", "Affine Analytics", "Affinity Express", "AGC Networks - Essar Group", "Agilent Technologies", "Agro Tech Foods", "AGS Transact Technologies", "AIPL", "Air India Limited", "Air Liquide", "Aircel", "Aircom International", "Airtel", "Ajanta Pharma", "Akamai Technologies",
                "AkzoNobel India Ltd", "Al Futtaim", "Alankit Assignments Ltd", "Alcatel Lucent", "Alchemist Group", "Alcor", "Alembic Pharmaceuticals", "Alere", "Alexandria Equities Management", "Alghanim Industries", "Alibaba.com", "Alkem Laboratories Limited", "Allahabad Bank", "Allcargo Logistics", "AllCheckDeals India Private Limited", "Allegis Group", "Allegro Advisors", "Allergan India Private Limited", "Alliance University", "Allied Blenders & Distillers Pvt. Ltd", "Allied Digital Services Ltd.",
                "allscripts", "Allsec Technologies", "Almondz Global Securities", "Alok Industries", "Alp COnsulting Ltd", "Alpha Payment Services", "Alstom", "Altamount Capital Management", "Altisource", "Altruist Technologies", "Amadeus", "Amalgamated Bean Coffee Trading Company", "Amar Ujala Publications", "Amara Raja Batteries Limited", "Amazan Agro Products Ltd.", "Amazon.com", "Amba Research", "Ambit Holdings", "Ambuja Cements Limited", "AMD", "Amdocs",
                "American Express", "Ameriprise Financial", "Amicorp Advisory Services", "Amira Group", "Amity Business School", "Amity University", "Amra Renal Care", "Amrapali Group", "Amrop India", "AMUL", "Amway", "ANALEC Infotech Private Limited", "Analytics Quotient", "Anand Rathi Group", "Anant Raj Group", "ANB Consulting", "Anchor Electricals", "ANDHRA BANK", "Aneja Associates", "Angel Broking", "ANI Technologies Pvt Ltd",
                "Anlage HRO Services", "Anmsoft technologies", "Annik Technology Services", "Ansal API", "Antal International", "Anthelio Business Technologies", "Anthelio Healthcare Solutions", "Antique Stock Broking", "Anvil Share & Stock Broking Pvt. Ltd", "any organisation", "ANZ", "AOL", "Aon Hewitt", "Aon", "Apalya Technologies", "APM Terminals", "Apollo Health and Lifestyle", "Apollo Hospitals", "Apollo Munich Health Insurance", "Apollo Tyres Ltd", "AppLabs",
                "Apple", "Applied Materials", "Applied Research International", "AppsDaily Solutions Private Limited", "Aptara Corp", "Aptech", "Aqua Logistics", "Aranca", "ARC Financial Services", "ArcelorMittal", "Arcil", "Areva", "Ariba", "Aricent Technologies", "Arihant Capital Markets Ltd", "Aris Global", "Arivum Business Solutions", "Army", "Arshiya International Ltd", "Arshiya Supply Chain Management", "Artech Infosystems",
                "Arthur D. Little", "Artsana India Pvt Ltd", "Arval India Private Limited", "Arvind Lifestyle Brands", "ASA & Associates", "Asahi India", "Ascendas", "Ashika Stock Broking Ltd", "Ashok Leyland", "Ashok Piramal Group", "Asia MotorWorks", "Asian Business Exhibition & Conferences Ltd", "Asian Development Bank", "Asian Institute Of Management", "Asian Market Securities", "Asian Paints", "Asit C. Mehta Investment Interrmediates Ltd.", "Aspire Systems", "Aspiring Minds Assessment Private Limited", "Asset Reconstruction Company (India) Limited", "Assistant Manager",
                "Associated Chambers of Commerce & Industry of India", "AstraZeneca", "AT & T", "AT Kearney", "ATC India Tower Corporation", "Athena Demwe Power Limited", "Atlas Copco India Limited", "Atos", "Attero Recycling", "Atul Limited", "Auctus Advisors", "Audi", "Audit", "AUM Capital", "aurionPro", "Aurobindo Pharma", "Auroch Investment Managers", "Auronova Consulting Private Ltd.", "AuthBridge Research Services", "Autodesk India Pvt Ltd", "Automobile",
                "Avalon Consulting", "Avaya GlobalConnect", "Avendus Capital", "Avery Dennison", "Avika Group", "Aviva Life Insurance", "Avnet Technology Solutions", "AVON Beauty Products India", "Avon Beauty Products", "AXA Business Services", "AXA Technology Services", "AXA", "Axiom Consulting", "Axis Bank", "Axtria", "AZ Research Partners", "Azure Power", "B Braun Medical India Pvt Limited", "B&K Securities", "BA Continuum India Pvt. Ltd.", "Babyoye.com",
                "Bacardi India", "Bahwan CyberTek Pvt Ltd", "Bain & Company", "Bajaj Allianz", "Bajaj Auto Limited", "Bajaj Capital", "Bajaj Corp", "Bajaj Electricals", "Bajaj Finance", "Balaji Telefilms Limited", "Ballarpur Industries Limited", "Balmer Lawrie & Co Ltd", "Bangalore International Airport Limited", "Bank Muscat", "Bank of America", "Bank of Baroda", "Bank of India", "Bank of Maharashtra", "Bank of Tokyo", "Banking", "BankMuscat",
                "Barclays", "Bartronics", "BASES", "BASF", "Basix", "Baskin Robbins", "Bata", "Batlivala & Karani Securities (India) Pvt. Ltd", "Bausch & Lomb","Baxter", "Bayer", "BBC", "BCG", "BDO Consulting", "Beacon Insurance Brokers Pvt. Ltd", "Beam Global", "Bechtel Corporation", "Becton", "Beetel Teletech","Bekaert Industries Private Limited", "BEML Ltd", "Benchmark Six Sigma", "Berger Paints",
                "Berggruen Group", "Berkadia Commercial Mortgage", "Berkshire India", "Beroe Inc", "Bertelsmann Marketing Services India", "Bertling Logistics India Pvt Ltd", "Best Engineering Aids & Consultancies Pvt Ltd", "beStylish.com", "BG Group", "BGR Energy Systems", "Bharat Aluminium Company Limited", "Bharat Bijlee", "Bharat Electronics", "Bharat Forge", "Bharat Heavy Electricals Limited", "Bharat Petroleum Corporation Limited", "Bharat Sanchar Nigam Limited", "Bharti Airtel", "Bharti Axa Life Insurance", "Bharti Infratel", "Bharti Realty",
                "Bharti Walmart", "Bhushan Power & Steel Ltd", "BIBA Apparels", "Big 4 Consulting Firm", "Big Bazaar", "BigShoeBazaar India Pvt Limited", "Bikanervala Foods Private Ltd", "Bilcare Ltd", "BIMTECH", "Binani Industries Limited", "Biocon", "Birla Corporation Limited", "Birla Research & Life Sciences", "Birla Sun Life Insurance", "Birla Tyre", "BIRLA TYRES", "Birlasoft", "Bisleri International Pvt Ltd", "BlackBerry", "BlackRock", "Blend Financial Services Limited",
                "Bloomberg", "Blue Dart Express", "Blue Ocean Market Intelligence", "Blue Star Limited", "Bluedart Express Ltd", "BMA Wealth Creators", "BMR & Associates", "BMR Advisors", "BMW India", "BNP Paribas", "BNY Mellon", "BOC India Limited", "Boehringer Ingelheim", "Boeing", "Bombardier Transportation India Limited", "Bombay Dyeing", "Bombay Stock Exchange", "Bonanza Portfolio Limited", "Bookadda.com", "Booz & Company", "Bosch",
                "Boston Analytics", "Boutique Investment bank", "BPO", "BPTP", "Brady Corporation", "Brakes India Ltd", "Brand Capital", "Brandscapes Worldwide", "Brescon Corporate Advisors (P) Ltd", "Brickwork India", "Brics Securities", "Brightpoint India", "Bristlecone", "Bristol Myers Squibb", "Britannia", "British Council", "BRITISH HIGH COMMISSION", "British Telecom", "Broadcom", "Broadridge Financial Solution", "Broking",
                "BS Limited", "BSES", "BSNL", "BSR & Co.", "BT Global Services", "BT", "Bunge India", "Bureau Veritas", "Business Analyst", "Business Development Bureau (India) Private Limited", "Business Octane Solutions Pvt. Ltd.", "Business Standard Ltd", "Business Standard", "Butterfield Fulcrum", "Butterfly Appliances", "buytheprice.com", "C&C Constructions Limited", "C-Quest Capital", "C3i Support Services", "CA Firm", "CA Technologies",
                "CA", "Cable & Wireless WorldWide", "Cadbury", "Cadence Design Systems", "Cadila", "CAE Simulation Technologies", "Cafe Coffee Day", "Cairn Energy", "Cairn India", "Caliber Point", "Callaway Golf Company", "Calypso Technologies Limited", "Camlin Ltd", "Canara Bank", "Canara HSBC Oriental Bank of Commerce Life Insurance Company Limited", "Canara Robeco", "Canon", "Capco Technologies", "Capgemini", "Capillary Technologies", "Capita",
                "Capital Code India Private Limited", "Capital First Limited", "Capital Foods Limited", "Capital Fortunes", "Capital IQ", "Capital Market Publishers", "Capital One", "Capital Square Advisors Pvt Ltd", "Capitalvia", "Capstone Securities Analysis", "Capvent India Advisors Private Ltd", "CarDekho.com", "CARE Hospitals", "CARE Ratings", "CARE", "Career Avenues", "Career Launcher", "Careerist Management Consultant Pvt. Ltd.", "CareerNet Technologies Pvt Ltd", "CareerNet", "Cargill India Private Limited",
                "Carl Zeiss", "Carlsberg", "Carlson Wagonlit Travels", "Carnation Auto India Pvt. Ltd.", "Carrefour", "Carrier", "Cartesian Consultancy", "CarWale.com", "Carzonrent India Pvt. Ltd.", "Case-Mate", "Castrol", "Caterpillar", "Catwalk", "CavinKare", "CB Richard Ellis", "CBRE Group", "CDAC", "Cease Fire Industries", "CEAT", "CEB", "Cedar Management Consulting",
                "Cegedim India Pvt Ltd", "Centaurus Financial Services", "Central Bank of India", "Central Government", "Centre for Monitoring Indian Economy Pvt. Ltd.", "Centrum Broking Pvt Ltd", "Centrum Capital Limited", "Centrum Wealth Management", "Centum Learning", "Centum Workskills India Ltd", "Century Plyboards (India) Ltd", "CenturyLink", "Cerebrus Consultants", "Cerner Healthcare Solutions", "CEVA Logistics", "CFA", "CGI Group", "CGN & Associates", "Chambal Fertilisers and Chemicals Limited", "Channelplay Limited", "Cheers Interactive",
                "CHEP India", "Chinatrust Commercial Bank", "Choice Solutions Limited", "Chokshi & Chokshi", "Cholamandalam Investment and Finance Company Limited", "Cholamandalam Securities", "Cholayil Private Limited", "Christ University", "Cians Analytics", "CIBER", "CII", "Cinemax India Limited", "Cinepolis India Pvt. Ltd.", "Cipla", "Cisco", "Citi Financial Consumer Finance India Ltd", "Citi Group", "Citi Research", "Citibank", "Citicorp Finance (India) Limited (CFIL)", "Citicorp Services India Limited",
                "Citigate Dewe Rogerson", "Citigroup", "CitiusTech", "Citrix Systems", "Citrus Check Inns", "Citrus Payment Solutions", "City Union Bank", "CK Birla Group", "CL Educate", "CLAAS India Pvt. Ltd.", "Classic Stripes Pvt Ltd", "Cleartrip.com", "Clickable Inc.", "Clifford Chance LLP", "Clockwork Business Solutions Pvt. Ltd.", "Clover Infotech Pvt Ltd", "CLP India Private Limited", "CLSA", "Club Mahindra", "CMC Limited", "CMMI 5 Level Company",
                "CMS Info Systems", "CNBC-TV18", "CNN-IBN", "Coal India Limited", "Coastal Projects Limited", "Coca Cola", "CoCubes Technologies Pvt. Ltd", "Coffee Day Beverages", "Cognilytics", "Cognizant Technology Solutions", "Colgate Palmolive", "Collabera", "Colliers International", "ColorPlus Fashions Ltd", "Colt Technologies", "Columbia Asia Hospital", "Columbia University", "CommonFloor.com", "Commonwealth Bank of Australia", "Compass Group", "CompuCom CSI Systems",
                "Compuware India", "Comverse Network Systems", "Comviva Technologies", "Concept Management Consulting Limited", "Confederation of Indian Industry", "Confi", "Confidential", "Consim Info Pvt Ltd", "Construction", "Consultancy", "Consultant", "Consulting", "Consumer Goods", "Contakt Tech Solutions", "Contract Advertising", "Convergys", "Copal Partners", "Corbus India Limited", "CORE Education & Technologies", "CoreEL Technologies", "CoreLogic Global Services",
                "Coromandel International Limited", "Corporation Bank", "Cortaal Global", "Cosmos Bank", "Country Club", "Cox & Kings", "CPA Global", "Cranes Software", "Credence Analytics", "Credit Agricole CIB", "Credit Analysis and Research Limited", "Credit Pointe Services Private Ltd.", "Credit Suisse", "CreditPointe", "CRISIL", "CRMIT Solutions", "Croma Retail", "Crompton Greaves", "Cross Tab Marketing Services", "Crossdomain Solutions Private Limited", "CRY",
                "Cryo-Save India Limited", "Cryobanks International", "CSC", "CSG", "CSS Corp", "Cubastion Consulting", "Cucine Lube India", "Cummins", "Cura Technologies", "Currently Unemployed", "Cushman & Wakefield", "Customer Centria Enterprise Pvt Ltd", "CustomerXPs Software Pvt Ltd", "Cvent", "Cybage Software", "Cyber Media Research Limited", "Cybertech Systems & Software", "Cygnus Business Consulting & Research", "Cypress Semiconductor", "D B Corp Ltd", "D&B TransUnion",
                "D. E. Shaw & Co", "Dabur", "Daikin Airconditioning India Private Limited", "Daimler India Commercial Vehicles", "Dainik Bhaskar", "Dainik Jagran", "Daiwa Asset Management", "Daiwa Capital Market India Pvt.Ltd", "Dalal & Broacha Pvt. Ltd.", "Dalal Street Investments Ltd.", "Dale Carnegie Training", "Dalmia Bharat", "Damco", "Damodar Valley Corporation", "Dana Group", "Danfoss Industries", "Danone", "Darashaw", "Dassault Systems", "Datamatics", "Datamonitor",
                "Datawise Consultants", "Dax Networks Limited", "DB Schenker", "DBOI", "DBS Bank", "DCB Bank", "DCM Shriram Consolidated", "DDB Mudra Group", "Dealsandyou.com", "Decathlon", "Decimal Point Analytics", "Defence", "Defiance Technologies", "Delhi International Airport", "Delhi Stock Exchange", "Delhi University", "Delhivery", "Dell", "Deloitte", "Delphi", "Delta Electronics",
                "Delta Power Solutions", "Demag Cranes AG", "DEN Networks Limited", "Dena Bank", "Denave India Private Ltd", "DENSO", "Dentsu Inc.", "DenuoSource", "Derivium Tradition", "Destimoney Enterprises Pvt. Ltd.", "Det Norske Veritas", "Deutsche Bank", "Developement Credit Bank", "Development Bank of Singapore", "Devenio Optimus Advisors", "Devyani International", "Dewan Housing Finance Corporation Limited", "Dexter Consultancy Private Limited", "DFM Foods Limited", "Dhanlaxmi Bank", "Dhanush Infotech",
                "Dharampal Satyapal Limited", "DHFL", "DHL Express", "DIAB Core Materials Pvt Ltd", "Diageo", "Diebold", "DIESL", "Digicall Teleservices", "Digitas", "Diligent Media Corporation", "Dimension Data", "Dinshaws Dairy Foods", "Dion Global Solutions", "Directi", "Direxions Marketing Solutions", "Dish TV", "Dishnet Wireless Limited", "Disney UTV", "DLF Pramerica Life Insurance", "DLF", "DMV Business and Market Research",
                "DNA", "Dodsal E&C India Pvt Ltd", "Dolat Capital Market Pvt. Ltd.", "Dolcera", "Dolphin Group Of Companies", "Donear Industries Ltd.", "Doshion Private Limited", "Dow Chemical", "Dow Corning", "Dow Jones Consulting India Private Limited", "DP World", "Dr. Reddy's Laboratories", "Draft FCB Ulka", "DRDO", "Drewry Maritime Services", "Drishti Soft", "Drive India Enterprise Solutions Limited", "Droege Group", "Drshti Strategic Research Services Pvt. Ltd", "DS Group", "DSCL",
                "DSM India", "DSM Sinochem Pharmaceuticals", "DSP BlackRock Investment Managers", "DTDC Courier & Cargo Ltd", "DTZ International", "Dun & Bradstreet", "Dunia Finance LLC", "Dunnhumby", "DuPont", "DY Works", "Dynamic Orbits", "E-Meditek", "Earth Infrastructures Ltd.", "East India Securities", "Eaton Corporation", "eBay", "eBIZ.com", "Ebusinessware", "eClerx", "Ecommerce", "Economic Times",
                "Edelman", "Edelweiss Capital", "Edelweiss Financial Services", "Edelweiss Securities", "Edelweiss Tokio Life Insurance", "Edelweiss", "Edenred", "Education", "Educational Initiatives", "Educomp", "EduKart.com", "Edusys Services", "Eduvisors", "Edwards Lifesciences", "Egis India", "Eicher", "EICL Limited", "EID Parry", "Eka Software", "Eko India Financial Services Private Limited", "Elara Capital",
                "Elder Health Care Limited", "Elecon", "Electronic Arts", "Electronica Finance Limited", "Electrosteel Steels Limited", "Electrotherm", "Elephant Design", "Eli Lilly and Company", "ELitecore Technologies", "Elixir Consulting", "Elixir Web solutions", "Elixir", "Elsevier", "Emaar MGF", "Emami Limited", "eMart solutions", "Embee Software", "Embitel", "embryoFund", "EMC Corporation", "Emcure Pharamaceuticals",
                "Emergent Ventures", "Emerson Network Power", "Emerson Process Management", "Emerson", "emids Technologies", "Emirates NBD", "Emkay Global Financial Services", "Emkor Solutions", "Empire Industries Ltd", "EmPower Research", "Empyrean Partners", "Enam Asset Management Company", "Enam Securities", "Encore Capital Group", "Endeavor Careers", "Enercon India", "Energo Engineering Projects", "engage4more (India) Private Limited", "Engineering", "Engineers India LImited", "ENIL - Radio Mirchi",
                "enStage", "Entercoms", "Entertainment Network India Limited", "Entertainment World Developers", "Entrepreneur", "Envision Financial Systems", "Enzen Global Solutions", "Eos Capital Advisors", "Epitome Global Services", "Equifax Credit Information Services", "Equirus Capital", "Equirus Securities", "Equitas Micro Finance", "Era Group", "Ericsson", "Ernst & Young", "EROS Group", "Escorts Agri Machinery", "Escorts Limited", "Espirito Santo Investment Bank", "ESPN Software India Private Limited",
                "Ess Dee Aluminium", "Essar Group", "Essel Group", "Essex Lake Group", "Estee Advisors", "Ester Industries Limited", "Eta Ascon", "ETA Engineering", "ETA General Pvt Ltd", "Etisalat DB Telecom", "Etoos Academy Private Limted", "Eureka Forbes", "Euro Asia Consulting", "Euronet Services India Private Limited", "Euronet Worldwide", "Evalueserve", "Eveready Indusries India Limited", "Everest Group", "Everonn Education", "Evotec India Private Limited", "EX - Army",
                "Executive Access  India", "EXECUTIVE SEARCH", "ExeLAN Networking Technologies", "Exemplarr Worldwide", "Exevo India", "Exide Industries", "Exilant Technologies", "EXL Service", "Expedia", "Experian Credit Information Company", "Export House", "Export Import Bank of India", "Express KCS", "Extramarks Education", "ExxonMobil", "Eye Q Vision Pvt Ltd", "FabFurnish.com", "Fabindia Overseas Pvt Ltd", "Facebook", "Factset Research Systems", "Fair Isaac Corporation (FICO)",
                "Falcon Tyres Limited", "Family Business", "FamilyCredit Limited", "Fareast Mercantile Co. Ltd.", "Fareportal India", "Fashionandyou.com", "FCS Software Solution Ltd", "Fedbank Financial Services Ltd", "Fedders Lloyd Corporation Limited", "Federal Bank", "Federal Mogul", "Federation of Indian Chambers of Commerce & Industry (FICCI)", "FedEx", "Feedback Business Consulting", "Feedback Infra", "Fenesta Building Systems", "Fenwal India Pvt Ltd", "Fermenta Biotech Limited", "Ferrero", "Fetise Retail Pvt Ltd", "Fiat India Automobiles Ltd",
                "Fidelity", "Fiduciary Euromax Capital Markets", "FieldFresh Foods Pvt Ltd", "FIITJEE", "Finalytics Consultancy", "Finance", "Financial Inclusion and Network Operations Ltd. (FINO)", "Financial Software & Systems", "Financial Technologies", "Fingerprints Fashions Private Limited", "FinIQ Consulting", "Fios Data Check Asia Pvt Ltd", "FIRE Capital Fund", "Firefly e Ventures", "Firepro Systems Private Limited", "Firmenich Aromatics", "First Advantage", "First American Financial Corporation", "First Blue Home Finance", "First Data", "First Flight Couriers Ltd",
                "First Global Securities", "First Gulf Bank", "Firstcry.com", "FirstRand Bank", "Firstsource Solutions", "Firstsource", "FIS", "Fiserv", "Fitch Ratings", "Fitness First India Private Limited", "Flagstone Reinsurance", "Flair Pens", "Flareum Technologies", "Flextronics", "Flipkart.com", "FLSmidth", "Fluor Daniel India", "Flytxt Mobile Solutions", "FMCG", "FMS", "Focus Comtrade Pvt. Ltd",
                "Focus Shares and Securities", "Food Corporation of India", "Foodpanda", "Forbes & Company Ltd.", "Forbes Marshall", "Force Motors Limited", "Ford Business Service Center", "Ford Motor Company", "FORE School of Management", "Foresight Software Solutions", "Forrester Research", "Fortis Healthcare", "Fortune 500", "Fortune Financial Services", "Fossil Inc.", "Four Soft Ltd.", "Fractal Analytics", "Franchise India", "Francorp", "Frankfinn Aviation Pvt Ltd", "Frankfinn Institute of Air Hostess Training",
                "Franklin Templeton", "FranklinCovey", "FREECULTR.COM", "Freelance", "Freescale Semiconductor", "Fresenius Kabi", "Fresher", "Freudenberg", "Frigoglass Group", "Frontier Business Systems", "Frost & Sullivan", "Fujifilm", "Fujitsu", "Fulcrum Worldwide", "Fullerton India Credit Company Limited", "Fullerton Securities & Wealth Advisors Ltd", "Future Axiom Telecom Limited", "Future Capital Holdings Limited", "Future Focus Infotech Pvt Ltd", "Future Generali India Life Insurance Company Limited", "Future Group",
                "Future Human Development Limited", "Future Supply Chain Solutions Limited", "Future Value Retail Limited", "Futures First", "Futurestep", "G4S", "Gabriel India Limited", "GAIL", "Galaxy Surfactants", "Gallagher Offshore Support Services Pvt Ltd", "Gallup", "Gameloft", "Gameshastra", "Gammon India Limited", "Gandour India Food Processing", "Gartner", "Garware Wall Ropes", "Gateway Rail Freight Limited", "Gati-Kintetsu Express Private Limited", "Gaursons India Limited", "GE Capital",
                "GE Money", "GE Oil & Gas", "GE", "Gemalto Digital Security Private Limited", "General Mills", "General Motors", "Genesis Burson-Marsteller", "Genesis Luxury", "Genius Consultant Limited", "Genpact", "Gensol Consultant Pvt Ltd", "Geodesic", "Geojit BNP Paribas Financial Services", "Geometric", "GEP", "GEPL CAPITAL", "German MNC", "Gerson Lehrman Group", "Getit Infoservices", "GFB Great Foods Ltd.", "GFK Mode",
                "Ghalla & Bhansali Chartered Accountants", "GHCL Limited", "GIC Housing Finance", "giftcart.com", "Gilbert Tweed Associates", "GIM", "Gini & Jony Ltd", "Gitanjali Gems Limited", "Gitanjali Group", "Gitanjali Lifestyle", "Gkb Rx Lens Pvt Ltd", "GlaxoSmithKline", "Glenmark Pharmaceuticals", "Globacom", "Global Analytics", "Global e Procure", "Global eProcure", "Global Group", "Global Groupware Solutions", "Global Innovsource", "Global Markets Center",
                "Global Talent Track", "GlobalData", "GlobalLogic", "GlobalScholar", "Globe Capital Market", "Globeop Financial Services", "Glodyne Technoserve Limited", "GLOPORE IMS", "GMJ & Co", "Gmmco Limited", "GMR - Delhi International Airport(P) Limited", "GMR Energy Limited", "GMR Group", "GMR Hyderabad International Airport", "GMR Infrastructure Limited", "Go Airlines", "Godfrey Phillips India Ltd", "Godrej & Boyce Mfg. Co. Ltd.", "Godrej Consumer Products", "Godrej Hersheys Ltd", "Godrej Industries Limited",
                "Godrej Infotech", "Godrej Properties", "Gokaldas Exports", "Gokhale Institute of Politics & Economics", "GoldenSource", "Goldman Sachs", "Goldstone Technologies Limited", "Goodyear", "Google", "Government of Gujarat", "Government of India", "Grail Research", "Grainger India", "Grameen Financial Services", "Grant Thornton", "Granules India limited", "GrapeCity India Private Limited", "Grasim Industries Limited", "Grassik Search", "Great Lakes Institute of Management", "Greaves Cotton Limited",
                "Green Infra Limited", "Greenback Forex", "Greenply Industries Limited", "Greycells18 Media Ltd", "Group Concorde", "Groupe Danone", "GroupM", "Groupon India", "GT Nexus", "GTL Infrastructure Limited", "GTL limited", "Guggenheim Transparent Value", "Gujarat Gas Company Limited", "Gulf Oil", "Gunnebo India Private Limited", "GupShup Technology", "GVK Biosciences", "GVK EMRI", "GVK Mumbai International Airport Limited", "GVK Power & Infrastructure Limited", "GVK",
                "Gyansys", "H & R Johnson", "Haier Appliances", "Hakuhodo Percept", "Haldia Petrochemicals Limited", "Haldiram", "Halliburton", "Hamdard", "Hamilton Housewares", "Handygo Technologies", "Hanil Automotive India Pvt Ltd", "Hanmer MSL", "Hansa Research", "Happiest Minds", "Haribhakti & Co", "Harman International", "Hathway", "Hatsun Agro Products Limited", "Havas Media", "Havells India", "Hawkins Cookers Limited",
                "Hay Group", "Hays", "HBL Global", "HBL Power Systems Limited", "HCL Infosystems", "HCL Technologies", "HDB Financial services ltd", "HDB Financial services", "HDFC Bank", "HDFC Life", "HDIL", "Headhonchos", "Headstrong", "Healthcare", "Healthkart.com", "HEC Paris", "Heidelberg", "Heidrick & Struggles", "Heinz", "Helios & Matheson", "Helix Advisors",
                "Hella India Electronics", "Hem Securities Limited", "Henkel", "Heritage Foods", "Hero Corporate Services", "Hero Mindmine", "Hero MotoCorp", "Hershey India", "Hettich", "Hewitt Associates", "Hewlett Packard", "Hexaware Technologies", "HFCL", "HGS", "Hidesign", "High Mark Credit Information Services", "HighStreetLabels.com", "HIL Ltd.", "HILTI INDIA", "Hilton", "Hindalco",
                "Hinduja Group", "Hindustan Aeronautics Limited", "Hindustan Coca-Cola Beverages", "Hindustan Construction Company", "Hindustan Copper Limited", "Hindustan Latex Family Planning Promotion Trust", "Hindustan Motors", "Hindustan Petroleum Corporation", "Hindustan Times", "Hindustan Zinc Limited", "Hindusthan National Glass & Industries", "Hindware", "Hiranandani Infrastructure and Real Estate Company", "Hitachi", "HLFPPT", "HLL Lifecare", "HNG Float Glass Ltd.", "HomeShop18.com", "Homeward Residential", "Honda", "Honeywell",
                "Hoppr", "Hospitality", "HPCL", "HR Anexi", "HR Consulting", "HR", "HSBC", "HT Media", "HTC", "Huawei", "Hughes Communication", "Hungama Digital Media Entertainment", "Hunt Partners", "Huntsman International", "Huntsmen & Barons", "Hushbabies.com", "Husys", "Hutchison Global Services", "Hyatt", "Hyderabad Industries Limited", "HyperCITY Retail",
                "HYTHRO POWER CORPORATION LIMITED", "Hyundai", "i-maritime Consultancy", "i3 Consulting", "IAF", "IBEXI Solutions", "Ibibo Group", "Ibibo Web Private Limited", "IBM", "IBS Hyderabad", "IBS Software Services", "ICAI", "ICFAI", "ICI Paints", "ICICI Bank", "ICICI Lombard", "ICICI Prudential Life Insurance", "ICICI Securities", "ICRA", "iCreate Software", "IDBI Bank",
                "IDBI Capital", "IDBI Federal Life Insurance Company", "IDBI Intech", "IDC", "Idea Cellular", "idea7 Sewells", "Ideacts Innovation", "IDFC", "iDiscoveri Education", "IE Business School", "iFAST Financial India", "IFB Industries Limited", "IFCI", "IFFCO Tokio General Insurance", "IFFCO", "IFMR", "iGate", "IILM", "IIM Ahmedabad", "IIM Bangalore", "IIM Calcutta",
                "IIM Indore", "IIM Kozhikode", "IIM Lucknow", "IIM Raipur", "IIM Rohtak", "IIM Shillong", "IIM Trichy", "IIM", "IIMA", "IIMB", "iimjobs", "iimjobs.com", "IIML", "IIMnet Ventures", "iipm", "IISWBM", "IIT Bombay", "IIT Delhi", "IIT kanpur", "IIT kharagpur", "IIT Roorkee",
                "IKYA Human Capital Solutions", "Ikya Human Capital", "IL&FS", "IMImobile", "IMRB", "IMS Health", "IMS Learning Resources", "IMS", "IMT Ghaziabad", "IMT Nagpur", "IMT", "iNautix Technologies", "Indegene Lifesystems", "Independant", "Independent Consultant", "Independent", "India Factoring & Finance Solutions Private Limited", "India Homes", "India Infoline", "India Medtronic Pvt. Ltd.", "India Power Corporation Limited",
                "India Today Group", "India Yamaha Motor", "Indiabulls", "IndiaCan", "IndiaFirst Life Insurance", "IndiaHomes", "IndiaMart InterMesh Ltd", "Indian Air Force", "Indian Army", "Indian Bank", "Indian Commodity Exchange Limited", "INDIAN IMMUNOLOGICALS LIMITED", "Indian Institute of Foreign Trade", "Indian Institute of Management Ahmedabad", "Indian Institute of management bangalore", "Indian Institute of Management", "Indian institute of planning and management", "Indian Navy","Indian Oil Corporation Limited", "INDIAN OVERSEAS BANK", "Indian Petro Group", "INDIAN STATISTICAL INSTITUTE",
                "Indiaplaza.com", "Indiaproperty.com", "Indigo Consulting", "IndiGo", "Indofil Industries Limited", "Indraprastha Gas Limited", "Inductis", "Indus Balaji Private Equity", "Indus Business Academy", "Indus Towers", "Indus Valley Partners", "IndusInd Bank", "Infibeam.com", "Infinite Computer Solutions", "Infiniti Retail - TATA Croma", "Infinity Research", "Info Edge", "Infocom Network Ltd.", "Infogain", "Infosys BPO", "Infosys",
                "Infotech Enterprises Limited", "Infraline Energy", "Infrasoft Technologies", "ING Investment Management", "ING VYSYA Bank", "ING Vysya Life Insurance", "Ingersoll Rand", "Ingram Micro", "Inkfruit.com", "InMobi", "Innovative B2B Logistics", "Innoz Technologies", "INOX Group", "Institute of Chartered accountants of India", "Institute of public enterprise", "Insurance", "Insync Analytics", "Intas Pharmaceuticals", "Integreon", "Intel", "Intelenet Global Services",
                "Intellecap", "InterContinental Hotels Group", "InterGlobe Technologies", "Interglobe", "International Tractors Limited", "Intex Technologies India Ltd", "IntraSoft Technologies Limited", "Intuit", "Invesco", "Investment Banking", "Investors Clinic", "Ipsos", "iQor", "IREO", "Irevna", "IRIS Business Services Limited", "ISB Hyderabad", "Isolux Corsan", "Ispat Industries Ltd.", "ISRO", "IT",
                "ITC Infotech", "ITC", "ITM", "iTrust Financial Advisors", "ITW India Ltd", "ITW Signode", "Itz Cash Card Ltd", "iYogi Technical Services Pvt Ltd", "Jabong.com", "Jagran Prakashan Ltd", "Jagran Solutions", "Jaguar Land Rover", "Jain Group", "Jaiprakash Associates Limited", "Janalakshmi Financial Services", "Jardine Lloyd Thompson", "jaypee business school", "Jaypee Capital", "Jaypee Group", "JBM GROUP", "JCO GAS PIPE LIMITED",
                "JDA Software", "Jet Airways", "Jindal Group", "Jindal Steel and Power Limited", "jk tyre & industries ltd.", "JM Financial", "Job seeker", "John Deere", "Johnson & Johnson", "Johnson Controls", "Jones Lang LaSalle", "JP Morgan", "JRG securities", "JSW Steel", "Jubilant Group", "Jubilant Life Sciences", "July Systems", "Jumbo Electronics", "Juniper Networks", "Justdial", "JWT",
                "Jyothy Laboratories Limited", "Kalkitech", "Kalpataru Limited", "Kancor Ingredients LTD", "Kansai Nerolac Paints", "Kantar Operations", "Karur Vyasya Bank", "Karvy", "Keane India", "KEC INTERNATIONAL LIMITED", "KEC International Ltd", "KEC International Ltd.", "KEC INTERNATIONAL", "KEI Industries Limited", "Kellogg", "Kelly Services India Pvt. Ltd.", "Kelly Services", "Kennametal India Limited", "Kent RO Systems Ltd.", "Keynote Corporate Services Limited", "Khaitan & Co",
                "Kimberly-Clark", "Kingdom of Dreams", "Kingfisher Airlines", "Kiran Energy", "Kirloskar", "Kitara Capital", "Kline & Company", "Knight Frank", "Knowlarirty Communications", "Knowledgefaber", "Kochar Infotech", "Kodak", "Kohinoor Group", "Kohler", "Kokuyo Camlin Ltd", "Komli Media", "Kony Labs", "Kotak Life Insurance", "Kotak Mahindra Bank", "Kotak Securities", "KPIT Cummins",
                "KPMG", "KPO", "Kraft Foods", "KRChoksey Shares & Securities ltd.", "KSK Energy Ventures Limited", "Kuoni India", "KyaZoonga.com", "L&T Finance", "L&T Infotech", "L&T Infrastructure Finance Company", "L&T", "L'Oreal", "Lafarge", "Lakme Lever", "Lakshmi Precision Screws Limited", "LAKSHMI VILAS BANK", "Lanco Infratech", "Lanco Solar Power", "Landmark Group", "Lanxess India", "LatentView Analytics",
                "Lava International", "Law Firm", "Lawrence & Mayo", "Lear Corporation", "Legal", "Legrand", "Leighton Welspun", "Lenovo", "Lenskart.com", "Leo Burnett", "Letsbuy.com", "Levi Strauss", "LG Electronics", "LIC", "Life Insurance", "Life Technologies", "Lifestyle", "Lilliput", "Linc Pen & Plastics", "Lintas", "LionBridge Technologies",
                "Liqvid eLearning Services", "LKP Securities Ltd", "LKP Securities", "Lloyds Banking Group", "Lodha & Co.", "Lodha Group", "Logica", "Logistics", "Logitech", "Lohia Group", "London Business School", "London School of Economics", "LongHouse Consulting", "looking for a job", "Looking for job", "Loop Mobile", "Lotte", "Louis Dreyfus Commodities India Pvt. Ltd.", "Lovely Professional University", "Lowe Lintas", "Loylty Rewardz",
                "Lucintel", "Lumata Digital", "Luminous Power Technologies", "Lupin", "Luxor", "Luxottica", "M3M", "Ma Foi", "Macleods Pharmaceuticals", "Macmillan", "Macquarie", "Madison", "Madura Coats", "Madura Fashion & Lifestyle", "Maersk", "Magma Fincorp Limited", "Magna Infotech", "Mahindra & Mahindra Financial Services", "Mahindra & Mahindra", "Mahindra Comviva", "Mahindra Holidays & Resorts",
                "Mahindra Satyam", "MakeMyTrip.com", "Manappuram", "Manhattan Associates", "Manipal Business Solutions", "Manipal Global Education Services", "Manipal Group", "Manpower Group", "Mansukh Securities & Finance Limited", "Manthan Services", "Manthan Systems", "Manufacturing", "Mapmyindia", "MAQ Software", "MARG Limited", "Marico", "Market Insight Consultants", "Marketing", "marketRx", "MarketsandMarkets", "Markit",
                "Marks & Spencer", "Marriott", "Mars", "Marsh India", "MART", "Maruti Suzuki India Limited", "Marwadi Shares & Finance Limited", "Mary Kay Cosmetics", "Masan Group", "Mastek", "Matrimony.com", "Matrix Cellular", "Mattel Toys", "Mawana Sugars", "Max Bupa Health Insurance", "Max Healthcare", "Max Hypermarket", "Max Life Insurance", "Max New York Life Insurance", "MaxRange Retail Pvt Ltd", "mba college",
                "MBA Fresher", "McAfee", "McCANN Erickson", "McCann", "McDonald's", "McKinsey & Company", "MCX Stock Exchange", "MDI Gurgaon", "MDI", "Mecklai Financial Services Ltd", "Mecon Limited", "MedPlus", "Medtronic", "Mercedes Benz", "Mercer", "Merck Limited", "meritnation.com", "Meritus Analytics", "Meru Cabs", "Metlife", "MetricStream",
                "METRO Cash & Carry", "Metropolis Healthcare Ltd", "MHRIL", "MICA", "Michael Page International", "Michelin", "Microland Ltd", "Microland", "Micromax", "Microsec Capital Limited", "Microsoft", "Miebach Consulting", "Millward Brown", "Mindfire Solutions", "MINDLANCE", "Mindteck", "Mindtree", "Ministry of Defence", "Mirah Group", "Misys", "MIT",
                "Mitsubishi", "Mizuho Corporate Bank", "mjunction services limited", "Mjunction Services Ltd", "MModal Global Services", "MMTC Limited", "MMTC Ltd.", "mnc", "Modi Group", "Mogae Media", "Monitor Group", "Monnet Group", "Monsanto", "Monster.com", "Morarka Organic Foods Ltd", "Morgan Stanley", "Moser Baer", "Mother Dairy", "Motherson Group", "Motilal Oswal", "Motorola Mobility",
                "Motorola", "Mott MacDonald", "Mount Talent Consulting", "MphasiS", "MPS Group of Companies", "MRF", "MSCI", "MSD Pharmaceuticals", "MSFL", "MSL India", "MTR Foods", "MTS", "Mu Sigma", "Multi Commodity Exchange Of India", "Murugappa Group", "Muthoot Fincorp", "MVL Telecom", "MXV Consulting", "Mylan", "Myntra.com", "Naaptol.com",
                "Nagarjuna Oil Corporation Limited", "Nagarro", "Narang Group", "Narayana Hrudayalaya", "NASSCOM", "National Dairy Development Board", "National Institute for Smart Government", "National Institute of Smart Government", "National Instruments", "National Payments Corporation of India", "National Securities Depository Limited", "National Securities Depository Ltd", "Navatar Group", "NBFC", "NCDEX", "NCR Corporation India Pvt Ltd", "NCR CORPORATION", "NDTV", "NEC HCL System Technologies", "NEC", "Neilsoft Ltd.",
                "Neilsoft", "Ness Technologies", "Nestle", "Net 4 India Limited", "NetAmbit", "NetApp", "Netscribes", "Network 18", "Network 21", "Network18 Group", "Neuerth Group", "New Holland Fiat India", "Newgen Software", "Next Education", "Next Retail", "ngo", "Nielsen", "Nihilent Technologies", "NIIT", "Nike", "nil",
                "Nilkamal Limited", "Nimbus", "Nippon Paints", "Nirmal Bang", "NISG", "Nissan", "Nitco Tiles", "NITIE", "Nityo Infotech", "Nivea", "NMIMS University", "NMIMS", "NMIMS", "no experience", "Nokia Siemens Networks", "Nokia", "Nomura", "None", "Northern Trust", "Not employed", "Not working currently","Not Working",
                "Not yet", "nothing", "NourishCo", "Novartis", "Novozymes", "NPTI", "NSDL", "NSE", "NSI INFINIUM GLOBAL PVT LTD", "NSN", "NTPC", "NTT Data", "Nucleus Software Exports", "Nucsoft Ltd.", "Nvidia", "o3 Capital", "Oak Capital Management", "Oberoi Group of Hotels", "Oberoi Group", "Obopay Mobile", "octamec Group",
                "Ocwen", "OfficeYes.com", "OFSS", "Ogilvy & Mather", "OLA cabs", "Olam International", "Oliver Wyman", "Olympus Imaging", "Omnitech Infosolutions", "One97 Communications", "Onestopm.com", "ONGC", "Onicra Credit Rating Agency of India", "Onida", "OnMobile", "Opera Solutions", "OPI", "Oracle", "Orange Business Services", "Orbis Financial Corporation Limited", "Orbit Corporation Limited",
                "Orchid Chemicals & Pharmaceuticals", "Organization", "Orient cement", "Oriental Bank of Commerce", "Oriflame", "Osram", "other", "Others", "OTIS Elevator", "Outlook", "Outsource Partners International", "Own business", "Own", "Oxfam India", "Oxigen Services", "Oyster Learning", "Ozone group", "PA Consulting Group", "PA Consulting", "Pace Power Systems Pvt Ltd", "Pall Corporation",
                "Panacea Biotec", "Panasonic", "Pangea3", "Pantaloon Retail", "Parag Milk Foods", "Paramount Farms", "Paras Pharmaceuticals", "PAREXEL", "Parikar Business and Knowledge Services", "Park Plaza", "Parle Agro", "Parle Products", "Parry Agro Industries", "Parsons Brinckerhoff", "Path Infotech", "Pathfinder", "Patni Computer Systems", "Paul Merchants", "PAYBACK", "Payoda", "PayPal",
                "PBO Plus Consulting", "PCS Securities", "PCS Technology", "PE Electronics", "Pearson Education", "Pearson", "Peninsula Land", "Penna Cement Industries", "Pentair", "People Interactive", "Peoplestrong HR", "Pepperfry.com", "PepsiCo", "Percept", "Perfect Relations", "Perfect Research", "Perfetti Van Melle", "Perfint Healthcare", "Pernod Ricard", "Perot Systems", "Persistent Systems",
                "Petrofac", "Petroleum and Natural Gas Regulatory Board", "Pfizer", "Phadnis Infrastructure", "pharma", "Pharmaceutical", "Pharmaceuticals", "Philip Morris", "Philips Electronics India Ltd", "Philips Healthcare", "Philips Lighting", "Philips", "Phillip Capital", "Photon Infotech", "PI Industries", "Piaggio", "Pidilite Industries", "PINC Research", "PineBridge Investments (formerly AIG Investmenets)", "Piramal", "Pitney Bowes Software",
                "PKF Sridhar & Santhanam", "Planet M Retail", "Planezy", "Planman Consulting", "Planman HR", "Planman Media", "Planning Commission", "plugHR", "PNB Gilts Ltd","PNB Housing Finance Limited", "PNB Investment Services Limited", "PNB MetLife", "Polaris Financial Technology Ltd", "Policybazaar.com", "Polycab Wires & Cables", "Polycom", "Population Services International", "Power Exchange India Limited", "Power Finance Corporation", "Powerwave Technologies", "Prabhudas Liladhar", "Practo",
                "Pramati Technologies", "Pratibha Industries", "Praxair India Pvt Ltd", "Praxis Business School", "PricewaterhouseCoopers", "Prime Focus Limited", "Principal", "Private", "Probe Equity Research", "Procter & Gamble", "Progressive Digital Media", "Progressive Infotech Pvt Ltd", "Progressive Infotech", "Progressive Media Group", "Promed Group", "PropEquity Analytics", "Properji", "Proptiger Realty", "Protiviti Consulting", "Prudential", "PTC",
                "Publicis", "Pubmatic", "PUG Securities", "Pulsar Knowledge Centre", "Puma", "Punj Lloyd", "Punjab National Bank", "PVR", "Pylon Management Consulting", "Pyramid IT Consulting", "Pyro Group", "QAI", "Quadrangle", "Qualcomm", "Quant Broking Private Limited", "Quant Capital", "Quatrro", "Quest Diagnostics", "Quintiles", "Rabobank", "Radico Khaitan",
                "Radio Mirchi", "Rainman Consulting", "Ramco Systems", "Ramky Group", "Ranbaxy", "Randstad", "RateGain", "Ratnakar Bank", "Rave Technologies", "Raychem RPG", "Raymond", "Real Estate", "Realization Technologies", "Reckitt Benckiser", "Red Bull", "redBus.in", "Rediff.com", "Rediffusion - Y&R", "Redington", "Redwood Associates", "Reebok",
                "Reed Elsevier", "rei agro limited", "Relaxo Footwear", "Reliance Brands Limited", "Reliance Broadcast Network Limited", "Reliance Capital", "Reliance Communications", "Reliance Digital", "Reliance Industries Limited", "Reliance Infrastructure", "Reliance Life Insurance", "Reliance Power", "Reliance Retail", "Reliance Securities", "Reliance", "Religare", "Relio Quick", "Renault", "Renoir Consulting", "Reserve Bank of India", "Resultrix",
                "Resurgent India Limited", "Retail", "Reval Analytics", "Ricoh India", "Right Management", "RIMS", "Rio Tinto India", "Risk Management Solutions", "RITES Ltd", "RMS", "Roamware", "Rocket Internet", "Rockwell Automation", "Rocsearch", "Rolta India", "Roubini Global Economics", "Royal Bank of Scotland", "Royal Dutch Shell", "Royal Orchid", "Royal Sundaram", "RPG Group",
                "RR Donnelley & Sons", "RS Software", "Ruchi Soya", "Ruia Group", "RUMI Education", "S Tel Private Limited", "S.S. Kothari Mehta & Co.", "SABMiller", "Sabre Holdings", "Safexpress", "Sahara", "Sahyadri Hospitals Ltd", "Sai Info System", "SAIL", "Sainsbury's", "Saint Gobain", "Sales", "Samsonite South Asia Pvt Ltd", "Samsung", "SanDisk India", "Sandoz Private Limited",
                "Sanofi Pasteur", "SAP", "Sapient", "SAR Group", "Saraswat Co-Operative Bank", "Saregama", "SAS", "Sasken", "Satyam", "SAUD BAHWAN AUTOMOTIVE", "Saviance Technologies", "Saxo Bank", "SBI Capital Markets", "SC Johnson", "Schindler India Pvt Ltd", "Schlumberger", "Schneider Electric", "School of Inspired Leadership", "SCMHRD", "SCMLD", "Scope International",
                "Sears Holdings", "SEBI", "Self Employed", "self", "Self-employed", "Serco", "Servion Global Solutions", "SG Analytics", "SGS India Pvt Ltd", "SGS", "Shapoorji Pallonji", "Sharekhan", "Sharp Business System", "Shell", "Sherwin Williams Paints", "Shipping Corporation of India", "ShopClues.com", "Shoppers Stop", "Shree Cement", "Shree Renuka Sugars Limited", "Shriram Group",
                "SHRM India", "SHRM", "SIDBI", "Siemens", "Sify", "Simplex Infrastructure", "Simplilearn", "SIMS", "Singhi Advisors", "sipl", "SITA", "Sitel", "Siva Group", "Siva Ventures Limited", "SJ Seymour", "SKF India Limited", "SKF India Ltd", "SKP Group", "SKS Microfinance", "SMALL INDUSTRIES DEVELOPMENT BANK OF INDIA", "SMARTANALYST",
                "SMC", "SME Rating Agency of India Limited", "Smile Group", "SMJ Capital", "Snapdeal.com", "SNL Financial", "Sobha Developers", "Societe Generale", "Sodexo", "Softcell", "Sokrati", "Solution digitas", "Solutions Middle East", "Sonata Software", "Sony", "SourceHov", "South Indian Bank", "SP Jain School of Global Management", "SPA Merchant Bankers Ltd", "Spanco", "Spandana Sphoorty Financial Limited",
                "Spandana Sphoorty Financial Ltd", "Spencer's Retail", "Spice Group", "SPJIMR", "sportsnlife.com", "SREI", "SRF Limited", "SRM University", "SS&C GlobeOp", "ST Ericsson", "Standard & Poor's", "Standard Chartered Bank", "Stanley Black & Decker", "STAR CJ", "Star India Pvt Ltd", "star news", "Star Union Dai-ichi Life Insurance", "Starcom", "State Bank of Hyderabad", "State Bank of India", "State Street Global Advisors",
                "STCI", "Steria", "Sterlite", "STOCK HOLDING CORPORATION OF INDIA LTD", "Strategic Decisions Group", "Stryker", "Student", "Su-Kam Power", "Subex Limited", "Subex", "Subros Ltd.", "Suguna Foods", "Sulekha.com", "Sumedha Fiscal Services Ltd", "Sumitomo Mitsui Banking Corporation", "Sun Life Financial", "sundaram finance limited", "Sundaram Finance LTD", "SunGard", "Sunidhi Securities", "SunLife Financial",
                "Suntec Business Solutions", "Supermax", "Supertech Limited", "Surya Pharmaceutical Limited", "Surya Roshni Limited", "Sutherland Global Services", "Sutherland", "Suvidhaa Infoserve", "Suzlon", "Suzuki Motorcycle", "Swiss Re", "Symantec", "SYMBIOSIS INSTITUTE OF INTERNATIONAL BUSINESS", "Symbiosis Institute of International Business", "Symphony Services","Symphony Teleca", "Syncapse", "SYNDICATE BANK", "Synechron Technologies", "Synechron", "Synergy Consulting Inc.", "Syngenta India Limited",
                "Syntel", "synygy", "Systematix Shares & stocks (I) Ltd", "systematix Shares & stocks", "TAJ GROUP", "TAKE SOLUTIONS", "Takshashila Consulting", "Talisma Corporation", "Tally Solutions", "Target Corporation", "Tata Administrative Services", "Tata Advanced Systems", "Tata AIA", "Tata Autocomp Systems", "Tata Business Support Service", "Tata Capital", "Tata Chemicals", "Tata Communications", "Tata Consultancy Services", "Tata Group", "Tata Interactive Systems",
                "Tata International Ltd", "Tata Motors", "Tata Power", "Tata Steel", "Tata Strategic Management Group", "Tata Technologies", "Tata Teleservices", "Taurus Mutual Fund", "TCS", "TE Connectivity", "Teach For India", "TeamLease", "Tech Mahindra", "Technip", "Technopak Advisors", "Techprocess Solutions Ltd", "Tecnova", "Tecpro Systems Limited", "Tecpro Systems Ltd", "Tejas Networks", "Telco Construction Equipment Co. Ltd.",
                "Telecom", "Telenor", "Teleperformance", "Temenos India", "Temenos", "TERI University", "Tesco", "test", "testing", "Tetra Pak", "Texas Instruments", "The Akshaya Patra Foundation", "The Alchemists Ark Pvt Ltd", "The Alchemists Ark Pvt. Ltd.", "The Bank of New York Mellon", "The Bombay Dyeing & Mfg Co Ltd", "The Boston Consulting Group", "The Coca-Cola Company", "The Federal Bank", "The Hackett Group", "The Himalaya Drug Company",
                "The Institute Of Chartered Accountants Of India", "The Manipal group", "The Mobile Store", "The Nielsen Company", "The Park Hotels", "The Shipping Corporation of India Ltd.", "The Smart Cube", "The South Indian Bank Ltd", "The South Indian Bank Ltd.", "The World Bank", "Thermax", "ThinkLink Supply Chain Services", "Thomas Cook", "Thomson-Reuters", "ThoughtWorks", "Thyrocare", "ThyssenKrupp", "TI Cycles of India", "Tikona Digital Networks", "TIL Limited", "Tilaknagar Industries",
                "TIME", "Times Group", "Timex Group", "Titan Industries", "TNS", "TNT", "Torrent Group", "Torry Harris Business Solutions", "Torus Business Solutions Pvt Ltd", "Torus Insurance", "Toshiba", "Towers Watson", "Toyota", "tradeindia.com", "Trafigura", "TransGraph Consulting", "Transparent Value", "Transport Corporation of India", "TransUnion", "Trefis", "TresVista Financial Services",
                "TRF", "Trianz", "Trident Limited", "Trimax IT Infrastructure & Services Ltd", "Trimax IT Infrastructure & Services Ltd.", "Triton Group", "TRPC", "Tube Investments of India", "Tulip Telecom", "Tupperware", "Turner India", "TVC Sky Shop", "TVS", "Twinings", "Tyco", "U2opia Mobile", "UB Group", "UBS", "UCO Bank", "Uflex", "Ugam Solutions",
                "UHG", "ujjivan financial services pvt Ltd", "UltraTech Cement Limited", "Unemployed", "Unicon", "Unifi Capital", "Unilever", "Uninor", "Union Bank of India", "Unisys", "Unitech", "United Breweries", "United Health Group", "United Spirits Limited", "United Technologies Corporation", "UnitedLex", "Universal Consulting", "universal sompo general insurance", "Universal Success Enterprises", "University", "UPES",
                "Urbantouch.com", "US MNC", "USHA International", "UST Global", "USV Limited", "UT Worldwide Inc", "UTI", "Utsav Fashion", "UTU", "V2 Solutions", "Validor Capital", "Value and Budget Housing Corporation Pvt Ltd", "ValueFirst", "Valuelabs", "Valvoline", "Valyoo Technologies", "Vardhman Group", "Vatika Group", "Vayam Technologies Limited", "vCustomer", "Vedanta Resources",
                "Verity Knowledge Solutions", "Verizon", "Vertex", "VertX Solutions", "VFS", "Via.com", "Viacom", "Videocon", "Vijaya Bank", "Violet Arch Capital Advisors", "Violet Arch Securities", "Violet Arch", "Viom Networks", "VIP Industries", "Virgin", "Virtusa Consulting", "VISA", "Vishal Retail", "Vistaar Finance", "Vistasoft", "Visteon",
                "Vizury Interactive", "VLCC", "Vmware", "Vodafone", "Volkswagen", "Voltas", "Volvo Eicher", "VRS Foods", "Wadia Group", "Walchandnagar Industries limited", "Walmart", "Walt Dinsey", "Walt Disney", "WaterHealth India Pvt Ltd", "WAY2WEALTH BROKERS PVT LTD", "Way2Wealth", "Welingkar Institute", "Welingkar", "Wells Fargo", "Welspun", "WeP",
                "Western Union", "Wheels India Ltd", "Whirlpool", "Williams Lea", "WILLIS", "Winshuttle", "Wipro Consumer Care & Lighting", "Wipro", "Wizcraft", "WNS", "Wockhardt", "World Bank", "World Gold Council", "Worlds Window Group", "Wrigley", "Wunderman", "Xavient Information Systems", "Xchanging", "Xebia", "Xerox", "XIMB",
                "XL DYNAMICS", "XL India", "XLRI", "Y2CF", "Yahoo", "Yakult Danone", "Yamaha Motor", "YASH Technologies", "Yatra.com", "Yebhi.com", "Yepme.com", "YES Bank", "YKK", "Yodlee", "Yum Brands", "Yum! Brands", "Zacks Investment Research", "Zamil Infra", "Zee", "Zenith", "Zensar Technologies",
                "Zinnov", "Zomato", "Zovi.com", "ZS Associates", "ZTE", "Zycus", "Zydus", "Zylog", "Zyme Solutions", "Zynga", "Bangalore", "Dickinson and Company","Gurgaon", "Mumbai", "Pune", "The Nielsen Company", "20:20 MSL", "24/7 Inc.", "3i Infotech", "3M India", "9.9 Media"
        };

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        User.getInstance().populateModel(edu,items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Company") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .multiSelectList(edu) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        /*for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(Registration4.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }*/

                        editText_work.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }

    private void college() {
        final String[] items = {
                "Indian Institute of Management, Ahmedabad (IIMA)", "Indian Institute of Management, Bangalore (IIMB)", "Indian Institute of Management, Calcutta (IIMC)", "Indian Institute of Management, Indore (IIMI)", "Indian Institute of Management, Kozhikode (IIMK)", "Indian Institute of Management, Lucknow (IIML)",
                "Indian Institute of Management Shillong (IIMS)", "Indian Institute of Management Rohtak (IIMRohtak)", "Indian Institute of Management Ranchi (IIMR)", "Indian Institute of Management Raipur (IIMRaipur)", "Indian Institute of Management Tiruchirappalli (IIMT)", "Indian Institute of Management Udaipur (IIMU)",
                "Indian Institute of Management Kashipur (IIMKashipur)", "Jamnalal Bajaj Institute of Management Studies (JBIMS)", "Birla Institute of Technology and Science (BITS), Pilani", "Birla Institute of Technology, Mesra", "Institute of Chartered Accountants of India (ICAI)", "Indian School of Business, Hyderabad (ISB)",
                "Indian Institute of Technology, Delhi (IITD)", "Indian Institute of Technology, Bombay (IITB)", "Indian Institute of Technology, Madras (IITM)", "Indian Institute of Technology, Roorkee (IITR)", "Indian Institute of Technology, Kanpur (IITK)", "Indian Institute of Technology, Kharagpur (IITKGP)",
                "Indian Institute of Technology, Guwahati (IITG)", "Indian Institute of Technology, Varanasi (IIT BHU)", "Indian Institute of Technology, Bhubaneswar (IITBBS)", "Indian Institute of Technology, Gandhinagar (IITGN)", "Indian Institute of Technology, Hyderabad (IITH)", "Indian Institute of Technology, Indore (IITI)",
                "Indian Institute of Technology, Jodhpur (IITJ)", "Indian Institute of Technology, Mandi (IITMandi)", "Indian Institute of Technology, Patna (IITP)", "Indian Institute of Technology, Ropar (IITRPR)", "Indian Institute of Science (IISc)", "Institute of Company Secretaries of India (ICSI)",
                "B. S. Abdur Rahman University", "Anna University", "Visvesvaraya Technological University", "Birsa Institute of Technology Sindri", "Osmania University", "College of Engineering, Guindy",
                "University of Mumbai", "Uttarakhand Technical University", "Delhi Technological University", "Dhirubhai Ambani Institute of Information and Communication Technology", "Malaviya National Institute of Technology, Jaipur", "Government College of Engineering, Amravati",
                "Harcourt Butler Technological Institute Kanpur (HBTI)", "Walchand Institute of Technology", "Guru Gobind Singh Indraprastha University", "Indian Institute of Information Technology, Allahabad (IITA)", "IIT, Dhanbad", "Institute of Technology, Nirma University",
                "International Institute of Information Technology, Hyderabad", "University of Pune", "Manipal Institute of Technology Manipal University", "M.S.Ramaiah Institute of Technology", "Mepco Schlenk Engineering College", "Model Engineering College",
                "Motilal Nehru National Institute of Technology, Allahabad (NIT)", "National Institute of Construction Management and Research", "National Institute of Engineering Visvesvaraya Technological University", "National Institute of Technology Srinagar (NIT)", "National Institute of Technology, Calicut (NIT)", "National Institute of Technology, Durgapur (NIT)",
                "National Institute of Technology, Hamirpur (NIT)", "National Institute of Technology Karnataka (NIT)", "National Institute of Technology, Rourkela (NIT)", "National Institute of Technology, Silchar (NIT)", "National Institute of Technology, Tiruchirappalli (NIT)", "National Institute of Technology, Warangal (NIT)",
                "Netaji Subhas Institute of Technology (NSIT)", "P.S.G. College of Technology", "P.E.S. College of Engineering", "P.E.S. Institute of Technology", "PEC University of Technology", "R.V. College of Engineering",
                "Rajagiri School of Engineering & Technology", "Sardar Vallabhbhai National Institute of Technology, Surat", "Shri Mata Vaishno Devi University", "Sir M. Visvesvaraya Institute of Technology", "SSN College of Engineering", "SRM University",
                "Thadomal Shahani Engineering College", "Thapar University", "Veermata Jijabai Technological Institute", "Vellore Institute of Technology", "Visvesvaraya National Institute of Technology", "West Bengal University of Technology",
                "Xavier Institute Of Management - Bhubaneswar (XIMB)", "Xavier Institute of Management and Entrepreneurship (XIME)", "XLRI - Xavier School Of Management, Jamshedpur (XLRI)", "Institute of Cost and Management Accountants of India (ICMAI)", "Management Development Institute (MDI)", "Narsee Monjee Institute of Management Studies (NMIMS)",
                "Faculty of Management Studies (FMS) - Delhi University", "National Institute of Industrial Engineering (NITIE)", "SP Jain Institute of Management - Research (SPJIMR)", "Symbiosis Institute of Business Management (SIBM)", "TA Pai Management Institute (TAPMI)", "Tata Institute of Social Sciences (TISS)",
                "Indian Institute of Foreign Trade (IIFT)", "Mudra Institute of Communications, Ahmedabad (MICA)", "Institute of Rural Management Anand (IRMA)", "University Business School, Chandigarh (UBS)", "Symbiosis Centre for Management and Human Resource Development (SCMHRD)", "Institute of Management Technology (IMT), Ghaziabad",
                "International Management Institute (IMI), New Delhi", "Birla Institute of Management Technology (BIMTECH), Greater Noida", "Lal Bahadur Shastri Institute of Management (LBSIM), New Delhi", "Fore School of Management Delhi", "KJ Somaiya Institute of Management Studies and Research, Mumbai", "ICFAI Business School (IBS), Hyderabad",
                "ICFAI, Gurgaon", "Institute of Management Technology (IMT), Nagpur", "Symbiosis Institute of International Business, Pune", "ISB&M Pune", "Loyola Institute of Business Administration (LIBA), Chennai", "BIM Trichy",
                "Lal Bahadur Shastri Institute of Management (LBSIM), New Delhi", "Institute of Management, Nirma University, Ahmedabad", "SIMS", "Welingkar Institute of Management Development & Research, Mumbai", "Indian Institute of Social Welfare and Business Management (IISWBM), Kolkata", "Delhi School of Economics (DSE)",
                "Great Lakes Institute of Management", "Institute for Financial Management and Research (IFMR)", "Columbia Business School", "Harvard Business School", "Johnson Graduate School of Management, Cornell University", "The Wharton School of the University of Pennsylvania",
                "Tuck School of Business at Dartmouth", "Yale School of Management", "INSEAD (France or Singapore Campus)", "Kellogg School of Management - Northwestern University", "London Business School, UK", "London School of Economics (LSE)",
                "MIT Sloan School of Management", "NYU Stern School of Business", "Stanford Graduate School of Business", "ESCP Europe Business School", "University of Oxford - Said, UK", "University of Cambridge - Judge, UK",
                "IE Business School, Spain", "HEC Paris, France", "Esade Business School, Spain", "Iese Business School, Spain", "IMD, Switzerland", "University of St Gallen, Switzerland",
                "Rotterdam School of Management, Erasmus University, Netherlands", "SDA Bocconi, Italy", "Asian Institute of Management (AIM), Philippines", "Haas School of Business, University of California Berkeley", "National University Of Singapore(NUS) Business School", "Institute of Management Technology (IMT) Dubai",
                "Department of Financial Studies, University of Delhi", "Goa Institute of Management (GIM)", "Dr. B. R. Ambedkar National Institute of Technology (NIT) Jalandhar", "MYRA School of Business", "Maulana Azad National Institute of Technology (MANIT) Bhopal", "National Institute of Technology, Kurukshetra (NIT KKR)",
                "Faculty of Management Studies, Banaras Hindu University (FMS, BHU)", "National Institute of Technology (NIT), Patna", "Indian Institute of Management, Visakhapatnam", "Indian Institute of Forest Management, Bhopal", "Other", "IIM-Nagpur",
                "IIM-Vishakhapatnam", "IFIM Business School, Bangalore", "ACADGILD", "Association of Chartered Certified Accountants(ACCA)", "All India Management Association (AIMA)", "American Academy of Financial Management(AAFM)",
                "Aon Hewitt Learning Center (AHLC)", "APMG", "Babson College", "BRIDGE School of Management", "CASI Global", "CIANS Academy",
                "Copal Amba", "Cornell University", "Dale Carnegie Training India", "DIGIPERFORM Digital Marketing Institute", "NIIT", "Digital Vidya",
                "Easyskillz", "Edupristine", "Edureka", "Edvancer", "EY Learning Solution", "Financial Planning Academy", "Great Lakes Institute of Management",
                "Finitiatives Learning India (FLIP)", "Forum for Emotional Intelligences Learning (FEIL)", "Frankfurt School of Finance & Management", "Global Institute of Intellectual Property(GIIP)", "Global Risk Management Institute", "GlobalSkillup",
                "ICICIdirect Centre For Financial Learning", "IIM Indian Institute of Management Calcutta + NIIT Imperia", "Imarticus Learning", "IMS Pro School", "Indian Institute of eCommerce", "Institute for Growth and Organizational Dynamics(IGOD)",
                "Institute of Product Leadership (IPL)", "Ivy Professional School", "iZenBridge", "Jack Welch Management Institute", "Jigsaw Academy", "KPMG",
                "Luxury Connect Business School", "Manipal Global Education", "Middle Earth HR", "MISB Bocconi", "MIT Professional Education", "MIT Sloan School of Management",
                "National Stock Exchange(NSE)", "National University of Juridical Sciences, Kolkata", "New York Institute of Finance", "Parsons School of Design", "Pearl Academy", "PEOPLECERT",
                "PMI", "Praxis Business School", "QAI Global Institute", "Quint Wellington Redwood", "Society for Human Resource Management (SHRM)", "SP Jain School of Global Management",
                "SP Jain School of Global Technology", "SP Jain School of High Technology", "The Strategy Academy", "The Wallstreet School India", "UCLA Anderson School of Management", "University of Adelaide",
                "University of British Columbia", "University of California Riverside & Fore School of Management", "University of Michigan", "UpGrad", "USC Viterbi School of Engineering", "VarSigma", "Wharton , University of Pennsylvania", "World HR Board", "Woxsen School of Business"
        };

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        User.getInstance().populateModel(edu,items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select College") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .multiSelectList(edu) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        /*for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(Registration4.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }*/

                        editText_college.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }

    private void designation() {

       final CharSequence[] items = {
               "Chairman", "Vice Chairman", "Chairman cum Managing Director", "Managing Director", "Sr. Vice president ", "Vice President", "General Manager",
               "Joint General Manager", "Deputy General Manager", "Asst. General Manager", "Chief Manager", "Sr. Manager", "Manager", "Joint Manager", "Deputy Manager",
               "Asst. Manager", "Sr. Officer", "Officer", "Jr. Officer", "Sr. Associate", "Associate", "Jr. Associate", "Assistant ", "Trainee Engineer", "Software Engineer",
               "Programmer Analyst", "Senior Software Engineer", "System Analyst", "Project Lead", "Project Manager", "Program Manager ", "Team Lead", "Senior Team Lead",
               "Account Manager", "Architect", "Technical Specialist", "Deliver Manager", "Delivery Head", "Business Analyst", "Delivery Partner"
       };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration4.this);
        alertDialogBuilder.setTitle("Choose Designation");
        int position;
        switch (editText_desig.getText().toString()) {
            case "Chairman":
                position = 0;
                break;
            case "Vice Chairman":
                position = 1;
                break;
            case "Chairman cum Managing Director":
                position = 2;
                break;
            case "Managing Director":
                position = 3;
                break;
            case "Sr. Vice president ":
                position = 4;
                break;
            case "Vice President":
                position = 5;
                break;
            case "General Manager":
                position = 6;
                break;
            case "Joint General Manager":
                position = 7;
                break;
            case "Deputy General Manager":
                position = 8;
                break;
            case "Asst. General Manager":
                position = 9;
                break;
            case "Chief Manager":
                position = 10;
                break;
            case "Sr. Manager":
                position = 11;
                break;
            case "Manager":
                position = 12;
                break;
            case "Joint Manager":
                position = 13;
                break;
            case "Deputy Manager":
                position = 14;
                break;
            case "Asst. Manager":
                position = 15;
                break;
            case "Sr. Officer":
                position = 16;
                break;
            case "Officer":
                position = 17;
                break;
            case "Jr. Officer":
                position = 18;
                break;
            case "Sr. Associate":
                position = 19;
                break;
            case "Associate":
                position = 20;
                break;
            case "Jr. Associate":
                position = 21;
                break;
            case "Assistant ":
                position = 22;
                break;
            case "Trainee Engineer":
                position = 23;
                break;
            case "Software Engineer":
                position = 24;
                break;
            case "Programmer Analyst":
                position = 25;
                break;
            case "Senior Software Engineer":
                position = 26;
                break;
            case "System Analyst":
                position = 27;
                break;
            case "Project Lead":
                position = 28;
                break;
            case "Project Manager":
                position = 29;
                break;
            case "Program Manager ":
                position = 30;
                break;
            case "Team Lead":
                position = 31;
                break;
            case "Senior Team Lead":
                position = 32;
                break;
            case "Account Manager":
                position = 33;
                break;
            case "Architect":
                position = 34;
                break;
            case "Technical Specialist":
                position = 35;
                break;
            case "Deliver Manager":
                position = 36;
                break;
            case "Delivery Head":
                position = 37;
                break;
            case "Business Analyst":
                position = 38;
                break;
            case "Delivery Partner":
                position = 39;
                break;
            default:
                position = -1;
                break;
        }
        alertDialogBuilder
                .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        editText_desig.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private boolean validateedu() {
        if (editText_edu.getText().toString().trim().isEmpty()) {
            inputLayoutEdu.setError("Enter education");
            requestFocus(editText_edu);
            return false;
        } else {
            inputLayoutCollege.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatework() {
        if(editText_work.getText().toString().trim().isEmpty()) {
            inputLayoutWorking.setError("Enter work");
            requestFocus(editText_work);
            return false;
        }

        else {
            inputLayoutWorking.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatedesig() {
        if(editText_desig.getText().toString().trim().isEmpty()) {
            inputLayoutDesig.setError("Enter designation");
            requestFocus(editText_desig);
            return false;
        } else {
            inputLayoutDesig.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateannual() {
        if (editText_annual.getText().toString().trim().isEmpty()) {
            inputLayoutAnnual.setError("Enter annual income");
            requestFocus(editText_annual);
            return false;
        } else {
            inputLayoutAnnual.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatecollege() {
        if (editText_college.getText().toString().trim().isEmpty()) {
            inputLayoutCollege.setError("Enter college");
            requestFocus(editText_college);
            return false;
        } else {
            inputLayoutCollege.setErrorEnabled(false);
        }

        return true;
    }




    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editTextEdu:
                    validateedu();
                    break;


                case R.id.editTextCollege:
                    validatecollege();
                    break;

                case R.id.editTextWorking:
                    validatework();
                    break;

                case R.id.editTextDesignation:
                    validatedesig();
                    break;


                case R.id.editText_annual:
                    validateannual();
                    break;

            }
        }
    }



    //Shared Preferences
    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;

    }

}
