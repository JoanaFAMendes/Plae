package com.example.janaf.plae;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Consult extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "Firebase";
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");

    Toolbar toolbar;

    private ArrayList<Reservation> listReservations;
    private ArrayList<Game> listGames;
    private RecyclerView rv;

    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mAuth = FirebaseAuth.getInstance();

        //recycle view
        rv = (RecyclerView) findViewById(R.id.games_profit);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        listReservations = new ArrayList<>();
        listGames = new ArrayList<>();

        Calendar c = Calendar.getInstance();;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());

        String [] hours = new String []{"0", "1"};
        final ArrayList<String> stringHours = new ArrayList<String>(Arrays.asList(hours));

        dataGames.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Game game = postSnapshot.getValue(Game.class);
                    listGames.add(game);
                    Log.d(TAG, "nome jogo: " + game.getNome_jogo());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataReserve.orderByChild("data").equalTo(formattedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    if(reservation.getData().equals(formattedDate)) {
                        Log.d(TAG, reservation.getEmail());
                        listReservations.add(reservation);
                    }
                }
                MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(4,getApplicationContext(),listGames,listReservations,"",formattedDate,stringHours, "");
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
}
