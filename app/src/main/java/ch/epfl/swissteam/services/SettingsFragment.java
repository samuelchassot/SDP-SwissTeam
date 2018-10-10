package ch.epfl.swissteam.services;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsFragment extends Fragment {

    private Spinner language_;
    private ArrayAdapter<String> adapter_;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        language_ = (Spinner) getView().findViewById(R.id.language_spinner);
        adapter_ = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_arrays));
        language_.setAdapter(adapter_);

        if (LanguageHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en")) {
            language_.setSelection(adapter_.getPosition("English"));
        } else if (LanguageHelper.getLanguage(MainActivity.this).equalsIgnoreCase("fr")) {
            language_.setSelection(adapter_.getPosition("French"));
        }

        language_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        LanguageHelper.setLocale(MainActivity.this, "en");
                        break;
                    case 1:
                        LanguageHelper.setLocale(MainActivity.this, "fr");
                        break;
                }
                startActivity(MainActivity.this, MainActivity.class);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
