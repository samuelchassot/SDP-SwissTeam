package ch.epfl.swissteam.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DBUtility {

    private DatabaseReference db_;
    private static DBUtility instance;

    private DBUtility(DatabaseReference db_){
        this.db_ = db_;
    }
    public static DBUtility get(){
        if (instance == null){
            instance = new DBUtility(FirebaseDatabase.getInstance().getReference());
        }
        return instance;
    }





    public List<User> getUsersFromCategory(String category){
        //FIXME Recherche dans Categories/IC
        db_.child("Categories").child("IC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //FIXME DataSnapshot est une snapshot des données au moment de la requête
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Log.e("USERS", data.getKey());

                    //FIXME Pour chaque user trouvé avant je les cherches dans la base de données USERS
                    db_.child("Users").child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
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

        return null;
    }
}
