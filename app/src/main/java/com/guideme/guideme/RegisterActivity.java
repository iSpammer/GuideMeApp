package com.guideme.guideme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";


    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;


    private Button btnSignUp;
    private EditText edtEmail;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtRePassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        rlayout.setAnimation(animation);
        btnSignUp = findViewById(R.id.btSignUp);
        edtUserName = findViewById(R.id.etUsername);
        edtEmail = findViewById(R.id.etEmail);
        edtPassword = findViewById(R.id.etPassword);
        edtRePassword = findViewById(R.id.etRePassword);


        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = edtUserName.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Error Registering", Toast.LENGTH_SHORT).show();
                } else {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    System.out.println("ERRORHERE");
                    String userId = user.getUid();

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Verification mail sent succesfully, please confirm your account", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error sending verification mail", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Log.e("PASSED", userId);
                    user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Travelers").child(userId);
                    user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Travelers").child(userId).child("name").child(name);
                    user_db.setValue(true);

                    user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Travelers").child(userId).child("avatar").child("none");
                    user_db.setValue(true);

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
//                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        btnSignUp.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnSignUp.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = edtUserName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confPassword = edtPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            edtUserName.setError("at least 3 characters");
            valid = false;
        } else {
            edtUserName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            edtPassword.setError("password must be more than 6 characters long or greater");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        if (!password.equals(confPassword)) {
            edtPassword.setError("Password doesn't match");
            valid = false;
        } else {
            edtRePassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
