package ch.epfl.swissteam.services;

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
import android.widget.TextView;

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
        generateCapability();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        generateSubcat();
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void generateCapability(){
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


        TableRow newTableRow = new TableRow(NPC_);
        newTableRow.addView(subcatSpinner_);
        newTableRow.addView(keyWordField_);
        addView(newTableRow);
    }

    public void generateSubcat(){
        //creating the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(NPC_,
                R.array.subcat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcatSpinner_.setAdapter(adapter);
        keyWordField_.setHint(R.string.enter_key_word);

    }

    private NewProfileCapabilities NPC_;
    private Spinner subcatSpinner_;
    private EditText keyWordField_;
}