package ch.epfl.swissteam.services;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class DeleteAccountActivity extends NavigationDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteaccount);
        super.onCreateDrawer(CANCEL);

        Button deleteButton = this.findViewById(R.id.button_deleteaccount_deletebutton);
        EditText continueEditText = this.findViewById(R.id.edittext_deleteaccount_continue);

        deleteButton.setOnClickListener(v->{
            String continueConfirmation = continueEditText.getText().toString();
            if(continueConfirmation.equals(getString(R.string.continue_confirmation_deleteaccount))){
                deleteAccount();
            }
        });
    }

    private void deleteAccount(){
        
    }
}
