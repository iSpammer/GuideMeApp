package com.guideme.guideme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button signOut;

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

//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP))
//        {
//            getBackground().setAlpha(0);
//        }
//        else
//        {
//            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
//        }

        mAuth = FirebaseAuth.getInstance();


//        if(!mAuth.getCurrentUser().isEmailVerified()){
//            Intent intent = new Intent(MainPage.this, FirstTimeSetupActivity.class);
//            startActivity(intent);
//            finish();
//        }

//        signOut = findViewById(R.id.signOut);

//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });

        //drawer
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
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.placeholder);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.placeholdertxt);
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
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.placeholder);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.placeholdertxt);
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


    //weather releated stuffs
}
