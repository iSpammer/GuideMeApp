package ispam.mark13r.GuideMe;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mind.simplelogin.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btRegister;
    private ImageView circle1;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        circle1     = findViewById(R.id.circle1);

        btRegister.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            Intent a = new Intent(MainActivity.this, RegisterActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View,String> (tvLogin,"login");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
            startActivity(a,activityOptions.toBundle());
        }
    }
}
