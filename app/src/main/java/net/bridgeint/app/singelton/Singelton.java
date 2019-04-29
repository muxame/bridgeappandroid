package net.bridgeint.app.singelton;

/**
 * Created by laptop on 23/05/2017.
 */

public class Singelton {
    private boolean flagDegree;
    private boolean flagCourse;
    private boolean flagCountry;
    private static Singelton ourInstance = new Singelton();
    public static Singelton getInstance() {
        return ourInstance;
    }

    public boolean isFlagDegree() {
        return flagDegree;
    }

    public void setFlagDegree(boolean flagDegree) {
        this.flagDegree = flagDegree;
    }

    public boolean isFlagCourse() {
        return flagCourse;
    }

    public void setFlagCourse(boolean flagCourse) {
        this.flagCourse = flagCourse;
    }

    public boolean isFlagCountry() {
        return flagCountry;
    }

    public void setFlagCountry(boolean flagCountry) {
        this.flagCountry = flagCountry;
    }
}
