package com.example.janaf.plae;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText txPass, txEmail;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG ="Firebase";
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("pessoas");
    String username;
    boolean admin = false;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txEmail = findViewById(R.id.txtEmail);
        txPass = findViewById(R.id.txtPass);
        img = (ImageView) findViewById(R.id.logo);
        txEmail.requestFocus();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(admin==false) {
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Intent i = new Intent(getApplicationContext(), Main_User.class);
                        startActivity(i);
                    }
                    else{
                        Intent a = new Intent(getApplicationContext(), Main_Admin.class);
                        startActivity(a);
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = txEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txEmail.setError("Required.");
            valid = false;
        } else {
            txEmail.setError(null);
        }

        String password = txPass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txPass.setError("Required.");
            valid = false;
        } else {
            txPass.setError(null);
        }

        return valid;
    }

    public void signIn(View view) {
        username = txEmail.getText().toString();
        String password = txPass.getText().toString();

        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, "ERRO LOGIN", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        database.orderByChild("email").equalTo(username).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    if(u.getEmail().equals(username)) {
                        admin = u.isAdmin();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
