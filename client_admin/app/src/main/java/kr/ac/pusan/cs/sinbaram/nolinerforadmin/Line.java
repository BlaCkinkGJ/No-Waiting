package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Line implements Serializable {
    public String Public_Key;
    public String Line_Name;
    public int Max_Number;
    public int Current_Enrollment_State = 0;
    public String Opening_Time;
    public String Closing_Time;
    public String Personal_Interval;
    public String Public_ID;
    Line() {

    }

}
