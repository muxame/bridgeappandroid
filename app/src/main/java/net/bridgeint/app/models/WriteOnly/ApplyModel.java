package net.bridgeint.app.models.WriteOnly;

import net.bridgeint.app.models.UniversityModel;

import java.util.ArrayList;

/**
 * Created by DeviceBee on 8/17/2017.
 */

public class ApplyModel {
    public static String degreeId = null;
    public static String courseType = null;
    public static String courseId = null;
//    public static String streamId = null;
    public static String countryId = null;
    public static String fees = null;
    public static UniversityModel universityModel = null;
    public static ArrayList<String> highSchoolTranscript = null;
    public static ArrayList<String> recommendationLetters = null;
    public static ArrayList<String> passportCopies = null;
    public static ArrayList<String> englishProfeciencyTest = null;
    public static ArrayList<String> personalStatment = null;
    public static ArrayList<String> extraDocuments = null;

    public static void clearAll()
    {
        if(degreeId!=null)
        {
            degreeId = null;
        }
        if(courseType!=null)
        {
            courseType = null;
        }
        if(courseId!=null)
        {
            courseId = null;
        }
        if(countryId!=null)
        {
            countryId = null;
        }
        if(universityModel!=null)
        {
            universityModel = null;
        }
//        if(streamId!=null)
//    {
//        universityModel = null;
//    }
        clearAllDoc();
    }

    public static void clearAllDoc()
    {
        if(highSchoolTranscript!=null)
        {
            highSchoolTranscript = null;
        }
        if(recommendationLetters!=null)
        {
            recommendationLetters = null;
        }
        if(passportCopies!=null)
        {
            passportCopies = null;
        }
        if(englishProfeciencyTest!=null)
        {
            englishProfeciencyTest = null;
        }
        if(personalStatment!=null)
        {
            personalStatment = null;
        }
        if(extraDocuments!=null)
        {
            extraDocuments = null;
        }
    }
}
