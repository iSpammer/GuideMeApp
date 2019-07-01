package com.guideme.guideme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guideme.guideme.common.Common;
import com.guideme.guideme.helper.Helper;
import com.guideme.guideme.model.OpenWeatherMap;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

public class MainPage extends AppCompatActivity implements LocationListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button signOut;


    static double lat, lng;
    //weather
    LocationManager locationManager;
    String provider;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();
    int MY_PERMISSION = 0;
    TextView mainWeath;
    ImageView mainWeathIcon;

    //drawer
    private Drawer result;
    private CrossfadeDrawerLayout crossfadeDrawerLayout;


//    private CustomSharedPreference customSharedPreference;


//    private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();

        mainWeath = findViewById(R.id.mainWeath);
        mainWeathIcon = findViewById(R.id.mainWeathIcon);

        //Get Coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null)
            Log.e("TAG", "No Location");
        //drawer
        initialaizeDrawer();
    }

    private void initialaizeDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.home).withIcon(GoogleMaterial.Icon.gmd_home).withDescription(R.string.home_description).withIconTintingEnabled(true).withIdentifier(1);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.about).withIcon(GoogleMaterial.Icon.gmd_info).withDescription(R.string.about_description).withIconTintingEnabled(true).withIdentifier(2);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.update).withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withDescription(R.string.updateinfo).withIconTintingEnabled(true).withIdentifier(3);

        SecondaryDrawerItem s1 = new SecondaryDrawerItem().withName(R.string.share).withIcon(GoogleMaterial.Icon.gmd_share).withIconTintingEnabled(true).withIdentifier(4);
        SecondaryDrawerItem s2 = new SecondaryDrawerItem().withName(R.string.contact).withIcon(GoogleMaterial.Icon.gmd_local_phone).withIconTintingEnabled(true).withIdentifier(5);
        SecondaryDrawerItem s3 = new SecondaryDrawerItem().withName(R.string.github).withIcon(FontAwesome.Icon.faw_github).withIconTintingEnabled(true).withIdentifier(6);
        ProfileDrawerItem profileDrawerItem;

        try {
            //uploads.get(0);
            profileDrawerItem = new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(R.mipmap.ic_launcher_round);


        } catch (Exception e) {
            profileDrawerItem = new ProfileDrawerItem().withName(getResources().getString(R.string.placeholder)).withEmail(getResources().getString(R.string.placeholder)).withIcon(R.mipmap.ic_launcher);
        }
        //placeholder.setImageURI(user.getPhotoUrl());

        //Account Header Builder
        final AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bkgrnd)
                .addProfiles(
                        profileDrawerItem
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

//        if (user.getPhotoUrl() != null) {
//
//            Glide.with(this)
//                    .load(user.getPhotoUrl().toString())
//                    .into(drawerImageIcon);
//        }

        ImageView drawerImageIcon = headerResult.getHeaderBackgroundView();
//        try {
////            Picasso.with(this).load(user.getPhotoUrl()).into(drawerImageIcon);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
        headerResult.getActiveProfile().withIcon(drawerImageIcon.getDrawable());

        headerResult.getHeaderBackgroundView().setImageDrawable(getResources().getDrawable(R.drawable.bkgrnd));

        crossfadeDrawerLayout = new CrossfadeDrawerLayout(this);
        result = new DrawerBuilder()
                .withDrawerLayout(crossfadeDrawerLayout)
                .withHasStableIds(true)
                .withGenerateMiniDrawer(true)
                .withSelectedItem(0)
                .withActivity(this)
                .withAccountHeader(headerResult)
//                .withToolbar(toolbar)
                .withSliderBackgroundColor(Color.WHITE)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentNavigationBarProgrammatically(true)
                .withDrawerWidthDp(72)
                .addDrawerItems(
                        item1.withTextColor(Color.BLACK).withIconColor(Color.BLACK),
                        item2.withTextColor(Color.BLACK).withIconColor(Color.BLACK),
                        item3.withTextColor(Color.BLACK).withIconColor(Color.BLACK),
                        new DividerDrawerItem(),
                        s1.withTextColor(Color.BLACK).withIconColor(Color.BLACK).withSelectable(false),
                        s2.withTextColor(Color.BLACK).withIconColor(Color.BLACK).withSelectable(false),
                        new DividerDrawerItem(),
                        s3.withTextColor(Color.BLACK).withIconColor(Color.BLACK).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.placeholder)
                )
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                            } else {
                                onBackPressed();
                            }
                        } else if (drawerItem.getIdentifier() == 2)
                            Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    aboutFragment).addToBackStack(null).commit();
                        else if (drawerItem.getIdentifier() == 3) {
                            Toast.makeText(getApplicationContext(), "FirsttimesetupActivity", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), FirsttimesetupActivity.class);
//                            startActivity(intent);
                        } else if (drawerItem.getIdentifier() == 4) {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.placeholder);
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, R.string.placeholdertxt);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        } else if (drawerItem.getIdentifier() == 5) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("plain/text");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ispamossama@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Mark10 Support");
                            intent.putExtra(Intent.EXTRA_TEXT, R.string.placeholdertxt);
                            startActivity(Intent.createChooser(intent, ""));
                        } else if (drawerItem.getIdentifier() == 6) {
                            String url = "https://github.com/iSpammer/";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        } else if (drawerItem.getIdentifier() == 99) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        result.closeDrawer();
//                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                        return false;
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                            } else {
                                onBackPressed();
                            }
                        } else if (drawerItem.getIdentifier() == 2) {
                            Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    aboutFragment).addToBackStack("").commit();
                        } else if (drawerItem.getIdentifier() == 3) {
                            Toast.makeText(getApplicationContext(), "FirsttimesetupActivity", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(getApplicationContext(), FirsttimesetupActivity.class);
//                            startActivity(intent);
                        } else if (drawerItem.getIdentifier() == 4) {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.placeholder);
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, R.string.placeholdertxt);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        } else if (drawerItem.getIdentifier() == 5) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("plain/text");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ispamossama@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Mark10 Support");
                            intent.putExtra(Intent.EXTRA_TEXT, R.string.placeholdertxt);
                            startActivity(Intent.createChooser(intent, ""));
                        } else if (drawerItem.getIdentifier() == 6) {
                            String url = "https://github.com/iSpammer/";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        } else if (drawerItem.getIdentifier() == 99) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        if (result.isDrawerOpen())
                            result.closeDrawer();
                        return false;
                    }

                })
                .

                        withOnDrawerListener(new Drawer.OnDrawerListener() {
                            @Override
                            public void onDrawerOpened(View view) {

                            }

                            @Override
                            public void onDrawerClosed(View view) {
//                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                                result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                            }

                            @Override
                            public void onDrawerSlide(View view, float v) {
//                                result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
//                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                        }).build();

        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                crossfadeDrawerLayout.crossfade(400);
                //only close the drawer if we were already faded and want to close it now
                if (isCrossfaded()) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
        result.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.logout).withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(99));
    }

    public void orderGuide(View v) {
        Intent intent = new Intent(MainPage.this, MapsActivity.class);
        startActivity(intent);
    }


    //weather
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.api_req(String.valueOf(lat), String.valueOf(lng)));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetWeather extends AsyncTask<String, Void, String> {
//        ProgressDialog pd = new ProgressDialog(getApplicationContext());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd.setTitle("Please wait...");
//            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (s.contains("Error: Not found city")) {
////                pd.dismiss();
//                return;
//            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>() {
            }.getType();
            openWeatherMap = gson.fromJson(s, mType);
//            pd.dismiss();

//            txtCity.setText(String.format("%s,%s",openWeatherMap.getName(),openWeatherMap.getSys().getCountry()));
//            txtLastUpdate.setText(String.format("Last Updated: %s", Common.getDateNow()));
//            txtDescription.setText(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
//            txtHumidity.setText(String.format("%d%%",openWeatherMap.getMain().getHumidity()));
//            txtTime.setText(String.format("%s/%s",Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset())));
            mainWeath.setText(String.format("%.2f °C", openWeatherMap.getMain().getTemp()));
            Picasso.get()
                    .load(Common.getImage(openWeatherMap.getWeather().get(0).getIcon()))
                    .into(mainWeathIcon);

        }

    }
}

