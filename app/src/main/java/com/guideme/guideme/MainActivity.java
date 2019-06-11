package com.guideme.guideme;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_SIGNUP = 0;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference users_db;



    private ImageButton btRegister;
    private ImageView circle1;
    TextView tvLogin;
    private Pair[] pairs = new Pair[1];

    private Button btnLogin;
    private Button btnGoogleLogin;
    private Button btnFacebookLogin;
    private EditText edtEmail;
    private EditText edtPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        circle1     = findViewById(R.id.circle1);
        btnLogin = findViewById(R.id.btLogin);
        btnGoogleLogin = findViewById(R.id.btGoogle);
        btnFacebookLogin = findViewById(R.id.btFacebook);

        edtEmail = findViewById(R.id.etEMail);
        edtPassword = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){
                    Intent intent = new Intent(MainActivity.this, MainPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        btRegister.setOnClickListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            Intent a = new Intent(MainActivity.this, RegisterActivity.class);
            pairs[0] = new Pair<View,String> (tvLogin,"login");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,this.pairs);
            startActivity(a,activityOptions.toBundle());
        }
    }



    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        // TODO: Implement your own authentication logic here.


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Error Logging in", Toast.LENGTH_SHORT).show();
                }
                else{
                    String userId = mAuth.getCurrentUser().getUid();
                    users_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Travelers").child(userId);
                    users_db.setValue(true);

                }
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
