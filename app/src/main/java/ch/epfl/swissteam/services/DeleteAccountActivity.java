package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
            } else {
                Toast.makeText(this, "Check spelling if you want to delete your account", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteAccount(){
        DBUtility.get().getUser(GoogleSignInSingleton.get().getClientUniqueID(), user ->{
            try {
                user.removeFromDB(DBUtility.get().getDb_());
            } catch (Utility.IllegalCallException e) {
                Log.e("REMOVE_USER", "Illegalcall of removeFromDB");
            }
            GoogleSignInSingleton.get().getClient().signOut();
            GoogleSignInSingleton.putUniqueID(null);
            Intent backToLogInIntent = new Intent(this, SignInActivity.class);
            startActivity(backToLogInIntent);
        });

    }
}
