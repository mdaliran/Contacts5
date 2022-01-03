package com.momid.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static List<Integer> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);

        colors.add(R.color.contact_color1);
        colors.add(R.color.contact_color2);
        colors.add(R.color.contact_color3);
        colors.add(R.color.contact_color4);
        colors.add(R.color.contact_color5);
        colors.add(R.color.contact_color6);
        colors.add(R.color.contact_color7);

        finish();
    }

    public static int getColor() {

        int color = new Random().nextInt(colors.size());
        return colors.get(color);
    }
}
