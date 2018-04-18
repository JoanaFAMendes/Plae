package com.example.janaf.plae;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Main_Admin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "Firebase";
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");


    Toolbar toolbar;


    //RecyclerView
    private ArrayList<Reservation> listReservations;
    private ArrayList<Game> listGames;
    private RecyclerView.Adapter gameAdapter;
    private RecyclerView rv;
    private int interval;
    private String formattedDate;
    String friendlyName;
    private Date dateD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__admin);

        listReservations = new ArrayList<>();
        listGames = new ArrayList<>();

        String [] hours = new String []{"0", "1"};
        final ArrayList<String> stringHours = new ArrayList<String>(Arrays.asList(hours));


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //recycle view
        rv = (RecyclerView) findViewById(R.id.admin_games);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();

        dataReserve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    Log.d(TAG, reservation.getEmail());
                    listReservations.add(reservation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Game game = postSnapshot.getValue(Game.class);
                    listGames.add(game);
                    Log.d(TAG, "nome jogo: " + game.getNome_jogo());
                }
                MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(3,getApplicationContext(),listGames,listReservations,"0","0",stringHours, "");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
                rv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                break;
            case R.id.consult:
                Intent k = new Intent(getApplicationContext(), Consult.class);
                startActivity(k);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        // Fazer o logout
        mAuth.signOut();
        // Chamar primeira atividade (login)
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }


    public void click(View v){
        Intent j = new Intent(getApplicationContext(), Main_Admin.class);
        startActivity(j);
    }

    //add game
    public void clickAdd(View v){
        Intent j = new Intent(getApplicationContext(), AddGame.class);
        startActivity(j);
    }
}
