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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Profile_User extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //Firebase
    private FirebaseAuth mAuth;
    private final String TAG = "Firebase";
    private String friendlyName;
    DatabaseReference dataUsers = FirebaseDatabase.getInstance().getReference("pessoas");
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");
    FirebaseUser user;

    //Layout
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    private TextView txt;
    TextView balance;
    ImageView img;

    String b;
    private Date dateD;
    private int interval;
    String formattedDate;
    String id;
    String bl;


    //RecyclerView
    private RecyclerView rv;
    private ArrayList<Game> listGames;
    private ArrayList<Reservation> listReservations;
    String[] hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__user);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        listGames = new ArrayList<>();
        listReservations = new ArrayList<>();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        mAuth = FirebaseAuth.getInstance();

        Resources res = getResources();
        hours = res.getStringArray(R.array.hours_array);
        final ArrayList<String> stringHours = new ArrayList<String>(Arrays.asList(hours));
        user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            formattedDate = extras.getString("date");
        }
        else{
            Calendar c = Calendar.getInstance();;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            formattedDate = df.format(c.getTime());
        }

        img = (ImageView) findViewById(R.id.profile_image);

        if (user != null) {
            friendlyName = user.getEmail();
        }

        try {
            interval = time();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        balance = (TextView) findViewById(R.id.balanceU);
        txt = (TextView) findViewById(R.id.user_name);
        rv = (RecyclerView) findViewById(R.id.user_reserves);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //firebase set balence
        //balance + firebase
        dataUsers.orderByChild("email").equalTo(friendlyName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    bl=u.getSaldo();
                    String i = bl + "€";
                    balance.setText(i);
                    String j = u.getNome();
                    txt.setText(j);
                    id = u.getId_pessoa();
                    Picasso.get().load(u.getImage()).into(img);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //firebase games

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

        //firebase reserves
        dataReserve.orderByChild("email").equalTo(friendlyName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    Log.d(TAG, reservation.getEmail());
                    listReservations.add(reservation);
                }
                MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(0,getApplicationContext(),listGames,listReservations,hours[interval],formattedDate, stringHours, "");
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


    public void addAmount(View v){
        EditText text;
        text = (EditText) findViewById(R.id.amount);
        String value = text.getText().toString();
        Double number = Double.parseDouble(value);


        double balance1 = Double.parseDouble(bl);


        balance1 = balance1 + number;

        b = String.valueOf(balance1);

        txt.setText(b + "€");

        //firebase alter saldo
        dataUsers.child(id).child("saldo").setValue(b);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reserve:
                Intent i = new Intent(getApplicationContext(), Reserves.class);
                startActivity(i);
                break;
            case R.id.profile:

                break;
            case R.id.home:
                Intent k = new Intent(getApplicationContext(), Main_User.class);
                startActivity(k);
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

        int interval = 0;

        for (int i = 0; i < hours.length; i++) {
            d1 = df1.parse(hours[i]);

            if (dateD.after(d1)) {
                return i;
            }
        }
        return hours.length-1;
    }
}
