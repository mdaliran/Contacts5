package com.momid.mainactivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class ColorHelper {

    private static List<Integer> colors;

    private static void addColors() {

        if (colors == null) {
            colors = new ArrayList<>();
        }

        if (colors.isEmpty()) {

            colors.add(R.color.contact_color1);
            colors.add(R.color.contact_color2);
            colors.add(R.color.contact_color3);
            colors.add(R.color.contact_color4);
            colors.add(R.color.contact_color5);
            colors.add(R.color.contact_color6);
            colors.add(R.color.contact_color7);
        }

    }

    public static int getColor() {

        addColors();

        int color = new Random().nextInt(colors.size());
        return colors.get(color);
    }
}
