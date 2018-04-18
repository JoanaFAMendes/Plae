package com.example.janaf.plae;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main_User extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    //Firebase
    private FirebaseAuth mAuth;
    private final String TAG = "Firebase";
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");
    DatabaseReference dataUsers = FirebaseDatabase.getInstance().getReference("pessoas");

    //Layout
    private TextView balance;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    String[] hours;

    //RecyclerView
    private ArrayList<Game> listGames;
    private ArrayList<Reservation> listReservations;
    private RecyclerView.Adapter gameAdapter;
    private RecyclerView rv;
    private int interval;
    private String formattedDate;
    String friendlyName;
    private Date dateD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__user);

        Resources res = getResources();
        hours = res.getStringArray(R.array.hours_array);
        final ArrayList<String> stringHours = new ArrayList<String>(Arrays.asList(hours));

        listGames = new ArrayList<>();
        listReservations = new ArrayList<>();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        Log.d(TAG, "3");
        balance = (TextView) findViewById(R.id.balence);

        mAuth = FirebaseAuth.getInstance();


        try {
            interval = time();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            friendlyName = user.getEmail();
        }



        //recycle view
        rv = (RecyclerView) findViewById(R.id.user_games);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Calendar c = Calendar.getInstance();;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        Log.d(TAG, "data45" +formattedDate);

        //String to Date
        try {
            dateD = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Balance b = new Balance();
        b.PastReserves(formattedDate,friendlyName);

        //balance + firebase
        dataUsers.orderByChild("email").equalTo(friendlyName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    String i = u.getSaldo() + "€";
                    balance.setText(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*getElementsGame();
        getElementsReservation();*/
        //firebase games and firebase ocuppied (reserves)
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
                    Log.d(TAG, reservation.getEmail());
                    listReservations.add(reservation);
                }
                MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(1,getApplicationContext(),listGames,listReservations,hours[interval],formattedDate,stringHours, "");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_user,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reserve:
                Intent i = new Intent(Main_User.this, Reserves.class);
                startActivity(i);
                break;
            case R.id.profile:
                Intent j = new Intent(Main_User.this, Profile_User.class);
                j.putExtra("date", formattedDate);
                startActivity(j);
                break;
            case R.id.home:

                break;
        }
        return true;
    }

    //returns interval of time
    public int time() throws ParseException {
        Calendar current1 = GregorianCalendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
        String formattedDate = df1.format(current1.getTime());
        Log.d(TAG, "data45" + formattedDate);

        Date d1;

        //String to Date
        try {
            dateD = df1.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < hours.length; i++) {
            d1 = df1.parse(hours[i]);

            if (dateD.after(d1)) {
                return i;
            }
        }
        return hours.length-1;
    }
}
