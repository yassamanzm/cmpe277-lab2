package com.sjsu.cmpe277.campusmap.controller;
import android.graphics.Color;
/**
 * Created by BharathKumar on 10/27/2016.
 */

public class ColorTool {
    public boolean closeMatch (int color1, int color2, int tolerance) {
        if ((int) Math.abs (Color.red (color1) - Color.red (color2)) > tolerance ) return false;
        if ((int) Math.abs (Color.green (color1) - Color.green (color2)) > tolerance ) return false;
        if ((int) Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance ) return false;
        return true;
    }
}
