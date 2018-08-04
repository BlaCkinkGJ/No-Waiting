package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import java.io.Serializable;
import java.util.Date;

public class Obj_Line implements Serializable {
    public String Public_Key;
    public String Line_Name;
    public int Max_Number;
    public int Current_Enrollment_State = 0;
    public String Opening_Time;
    public String Closing_Time;
    public String Personal_Interval;
    Obj_Line() {

    }

}
