package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String NAVIGATION_TAG = "NAV_DRAWER_CLICKED";
    public static final String CANCEL = "Cancel";
    public static final String MAIN = "Main";
    public static final String BACK = "Back";

    private DBUtility util_ = DBUtility.get();

    private DrawerLayout drawer_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onCreateDrawer(String toggleButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer_ = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_.addDrawerListener(toggle);
        toggle.syncState();

        switch(toggleButton){
            case (CANCEL) :
                toggle.setDrawerIndicatorEnabled(false);
                toggle.setHomeAsUpIndicator(R.drawable.ic_toggle_cancel);
                toggle.setToolbarNavigationClickListener(view -> {
                    finish();
                });
                break;
            case (BACK) :
                toggle.setDrawerIndicatorEnabled(false);
                toggle.setHomeAsUpIndicator(R.drawable.ic_toggle_backarrow);
                toggle.setToolbarNavigationClickListener(view -> {
                    finish();
                });
                break;
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        //TextView navHeaderName = (TextView) findViewById(R.id.nav_header_name);
    }

    @Override
    public void onBackPressed() {
        if (drawer_.isDrawerOpen(GravityCompat.START)) {
            drawer_.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        setNavUserName();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        startActivity(new Intent(this, MainActivity.class).putExtra(NAVIGATION_TAG, id));
        drawer_.closeDrawer(GravityCompat.START);
        finish();

        return true;
    }


    /**
     * Set the user name and email in the nav
     */
    private void setNavUserName() {
        util_.getUser(GoogleSignInSingleton.get().getClientUniqueID(), user -> {
            if(user != null){
                ((TextView) findViewById(R.id.nav_header_name)).setText(user.getName_());
                ((TextView) findViewById(R.id.nav_header_email)).setText(user.getEmail_());}
            Picasso.get().load(user.getImageUrl_()).into((ImageView)findViewById(R.id.nav_header_profileimage));
        });
    }





}
