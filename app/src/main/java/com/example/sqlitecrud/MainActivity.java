package com.example.sqlitecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button btnSave,btn_show;
//    ImageView imgSun;
    ImageView imgTick;
    LinearLayout linear_cover;
    EditText edt_name, edt_city, edt_age;
    DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTick = findViewById(R.id.img_tick);
        edt_name = findViewById(R.id.edt_name);
        edt_city = findViewById(R.id.edt_city);
        edt_age = findViewById(R.id.edt_age);
        dbHandler = new DbHandler(getApplicationContext());

        btn_show = findViewById(R.id.btn_show);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edt_name.getText().toString();
                String city = edt_city.getText().toString();
                String age = edt_age.getText().toString();

                if (inputAreCorrect(name, city, age)) {

                    hideKeyboard(MainActivity.this);

                    long res = dbHandler.insertPerson(name, city, Integer.parseInt(age));

                    if (res > 0) {
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                        imgTick.setVisibility(View.VISIBLE);
                        imgTick.startAnimation(animation);

                        clearInputs();
                    } else {

                        Toast.makeText(MainActivity.this, "Error in saving data", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ActivityShow.class));
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
//                imgTick.startAnimation(animation);
//                imgTick.setVisibility(View.INVISIBLE);

            }
        });
    }

    private Boolean inputAreCorrect(String name, String city, String age) {
        if (name.isEmpty()) {
            edt_name.setError("enter a name");
            edt_name.requestFocus();
            return false;
        }

        if (city.isEmpty()) {
            edt_city.setError("enter a city");
            edt_city.requestFocus();
            return false;
        }

        if (age.isEmpty()) {
            edt_age.setError("enter age!");
            edt_age.requestFocus();
            return false;
        }

        return true;
    }


    void clearInputs() {
        edt_age.setText("");
        edt_name.setText("");
        edt_city.setText("");

    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
