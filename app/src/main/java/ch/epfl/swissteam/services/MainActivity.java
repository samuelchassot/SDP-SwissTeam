package ch.epfl.swissteam.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * This class is the MainActivity of the application, this is
 * the home activity that displays the feed of local demands
 *
 * @author Samuel Chassot
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment servicesFragment;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //FIXME Récuperation de la database
        mDataBase = FirebaseDatabase.getInstance().getReference();

        //FIXME création de deux users pour la database
        User simon = new User("swicky","Simon", "Wicky", "Je suis un étudiant de l'epfl", new ArrayList<>(Arrays.asList("IC", "Maths", "Jardinage")));
        User samuel = new User("schassot", "Samuel ", "Chassot", "Salut, je m'appelle Samuel", new ArrayList<>(Arrays.asList("IC", "Allemand", "Anglais")));

        //FIXME Ajouts des users aux catégories
        for (String category : samuel.getCategories_()){
            mDataBase.child("Categories").child(category).child(samuel.getUsername_()).setValue("true");
        }

        for (String category : simon.getCategories_()){
            mDataBase.child("Categories").child(category).child(simon.getUsername_()).setValue("true");
        }

        //FIXME ajouts des users à la base
        //FIXME FireBase sait ajouté des types perso, si il existe un constructeur vide (cf User) et que les getters suivent les conventions (get + nom de l'attributs)
        mDataBase.child("Users").child(samuel.getUsername_()).setValue(samuel);
        mDataBase.child("Users").child(simon.getUsername_()).setValue(simon);

        //FIXME Recherche dans Categories/IC
        mDataBase.child("Categories").child("IC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //FIXME DataSnapshot est une snapshot des données au moment de la requête
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Log.e("USERS", data.getKey());

                    //FIXME Pour chaque user trouvé avant je les cherches dans la base de données USERS
                        mDataBase.child("Users").child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //FIXME j'affiche leur nom
                            if (dataSnapshot.getValue() != null)
                            Log.e("USERS", dataSnapshot.getValue(User.class).getName_());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Useful for stuff
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Useful for stuff
            }
        });

        //FIXME Plus d'infos ici https://firebase.google.com/docs/database/android/read-and-write

        //TODO Load home fragment
        //showHomeFragment();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        /**
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
         **/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case (R.id.button_maindrawer_services) :
                showServicesFragment();
                break;
            case (R.id.button_maindrawer_logout) :
                signOut();
                break;
            case (R.id.button_maindrawer_profile) :
                //show the profile fragment
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Sign out the user from the application.
     */
    private void signOut() {
        SignInActivity.mGoogleSignInClient_.signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * Shows the services Fragment
     */
    private void showServicesFragment(){
        if (this.servicesFragment == null) this.servicesFragment = ServicesFragment.newInstance();
        this.startTransactionFragment(this.servicesFragment);
    }

    /**
     * Initiate the fragment transaction
     * @param fragment the fragment to show
     */
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }
}
