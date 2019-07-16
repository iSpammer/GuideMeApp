package com.guideme.guideme;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.util.Arrays;

public class OrderTaxiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_taxi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayout noFavoritePlaces = findViewById(R.id.noFavoritePlaces);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Book a Ride");

        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("evlvdjIz8Guu4Hka2dFfcoiNdm98kwry") //This is necessary
                .setServerToken("")
                .setRedirectUri("YOUR_REDIRECT_URI") //This is necessary if you'll be using implicit grant
                .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS)) //Your scopes for authentication here
                .build();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
