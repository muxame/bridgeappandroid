package net.bridgeint.app.utils;

import android.app.Activity;
import android.content.Intent;

import net.bridgeint.app.models.CountryModel;
import net.bridgeint.app.models.CourseModel;
import net.bridgeint.app.models.DegreeModel;
import net.bridgeint.app.models.FeesModel;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class SharedClass {
    public static boolean isApply = false;
    public static boolean isDevelopmentMode = true;
    public static UserModel userModel = new UserModel();
    public static ArrayList<DegreeModel> degreeModels = new ArrayList<>();
    public static ArrayList<CourseModel> courseModels = new ArrayList<>();
    public static ArrayList<FeesModel> feesModels = new ArrayList<>();
    public static ArrayList<CountryModel> countryModels = new ArrayList<>();
    public static boolean openImagePicker = false;
    public static HashMap<String, String> placesKeywords = new HashMap<>();

    public static HashMap<Integer, UniversityModel> allUniversitiesMap = new HashMap<>();
    public static ArrayList<UniversityModel> allUniversitiesList = new ArrayList<>();
    public static UniversityModel universityModel;

    public static void ParseUniversity(ArrayList<UniversityModel> universityModels) {
        for (int i = 0; i < universityModels.size(); i++) {
            if (!SharedClass.allUniversitiesMap.containsKey(universityModels.get(i).getId())) {
                SharedClass.allUniversitiesMap.put(universityModels.get(i).getId(), universityModels.get(i));
                SharedClass.allUniversitiesList.add(universityModels.get(i));
            }
        }
    }

    public static void ClearAllData() {
        if (degreeModels != null)
            degreeModels.clear();
        if (courseModels != null)
            courseModels.clear();
        if (countryModels != null)
            countryModels.clear();
        if (allUniversitiesMap != null)
            allUniversitiesMap.clear();
        if (allUniversitiesList != null)
            allUniversitiesList.clear();
    }

    public static void logout(Activity context) {
        SharedClass.ClearAllData();
        Utility.logout(context);
        Intent i = context.getBaseContext().getPackageManager().getLaunchIntentForPackage(context.getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        context.finish();
    }

    public static void clearCourseModels(){
        courseModels.clear();
    }
}
