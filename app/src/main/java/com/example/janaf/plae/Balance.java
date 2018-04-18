package com.example.janaf.plae;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Balance {
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");
    DatabaseReference dataUsers = FirebaseDatabase.getInstance().getReference("pessoas");
    ArrayList<String> games;
    ArrayList<String> prices;
    private final String TAG = "Firebase";
    String id;
    String balance;
    String r;

    public void PastReserves(final String date, final String user){
        games = new ArrayList<>();
        prices = new ArrayList<>();
        dataUsers.orderByChild("email").equalTo(user).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    balance = u.getSaldo();
                    id = u.getId_pessoa();
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
                    games.add(game.getNome_jogo());
                    prices.add(game.getPreco());
                    Log.d(TAG, "nome jogo: " + game.getNome_jogo());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataReserve.orderByChild("email").equalTo(user).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    Log.d(TAG, reservation.getEmail());
                    String d = reservation.getData();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                    try {
                        if((df.parse(d)).before(df.parse(date))){
                            String n = reservation.getNome_jogo();
                            for(int i =0; i<games.size(); i++){
                                if(n.equals(games.get(i).toString())){
                                    decreaseBalance(i);
                                    r = reservation.getId();
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void decreaseBalance(int position){
        float price = Float.parseFloat(prices.get(position).toString());
        float b = Float.parseFloat(balance);
        DecimalFormat decimalFormat = new DecimalFormat(".##");

        b = b-price;
        String balance111 = decimalFormat.format(b);

        dataUsers.child(id).child("saldo").setValue(balance111);
        dataReserve.child(r).removeValue();
    }
}
