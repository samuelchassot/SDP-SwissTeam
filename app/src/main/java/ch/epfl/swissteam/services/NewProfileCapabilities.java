package ch.epfl.swissteam.services;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

public class NewProfileCapabilities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_capabilites);
    }

    public void addNewCapability(View view) {
        //retrieving the LinearLayout of the mainScroll
        LinearLayout ll = findViewById(R.id.mainScrollViewLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        ll.addView(new CapabilitySelection(this), lp);
    }

}

class CapabilitySelection extends TableLayout
                            implements AdapterView.OnItemSelectedListener {

    public CapabilitySelection(NewProfileCapabilities context){
        super(context);
        NPC_ = context;
        subcatArray_ = getResources().obtainTypedArray(R.array.subcat_array);
        generateCapability();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        generateSubcat(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void generateCapability(){

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        //main spinner
        Spinner newSpinner = new Spinner(NPC_);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(NPC_,
                R.array.domains_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newSpinner.setAdapter(adapter);
        newSpinner.setOnItemSelectedListener(this);
        addView(newSpinner);


        //creating keywords field
        keyWordField_ = new EditText(NPC_);
        subcatSpinner_ = new Spinner(NPC_);
        keyWordField_.setHorizontallyScrolling(true);

        TableRow newTableRow = new TableRow(NPC_);
        newTableRow.addView(subcatSpinner_);
        newTableRow.addView(keyWordField_);

        addView(newTableRow);
    }

    private void generateSubcat(int pos){
        //creating the spinner

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(NPC_,
                subcatArray_.getResourceId(pos,R.array.empty_array), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcatSpinner_.setAdapter(adapter);
        keyWordField_.setHint(R.string.enter_key_word);

    }

    private final NewProfileCapabilities NPC_;
    private Spinner subcatSpinner_;
    private EditText keyWordField_;
    private final TypedArray subcatArray_;
}