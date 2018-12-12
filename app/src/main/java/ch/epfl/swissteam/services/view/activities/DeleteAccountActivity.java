package ch.epfl.swissteam.services.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.utils.Utils;

/**
 * Activity shown when the user want to delete his/her account. Shows some warning and ask
 * for confirmation. User needs to enter the word "CONTINUE" to be able to delete.
 */
public class DeleteAccountActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deleteaccount);
        super.onCreateDrawer(CANCEL);

        Button deleteButton = this.findViewById(R.id.button_deleteaccount_deletebutton);
        EditText continueEditText = this.findViewById(R.id.edittext_deleteaccount_continue);

        deleteButton.setOnClickListener(v->{
            String continueConfirmation = continueEditText.getText().toString();
            if(continueConfirmation.equals(getString(R.string.settings_continue_confirmation_deleteaccount))){
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
            } catch (Utils.IllegalCallException e) {
                Log.e("REMOVE_USER", "Illegalcall of removeFromDB");
            }
            GoogleSignInSingleton.get().getClient().signOut();
            GoogleSignInSingleton.putUniqueID(null);
            Intent backToLogInIntent = new Intent(this, SignInActivity.class);
            startActivity(backToLogInIntent);
        });

    }
}
