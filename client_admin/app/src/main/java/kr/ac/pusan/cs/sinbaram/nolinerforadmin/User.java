package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import java.io.Serializable;

public class User implements Serializable {
    public String User_ID ;
    public String State ;
    public String Enter;
    User(){

    }

    public void make(String ID, String State) {
        this.User_ID = ID ;
        this.State = State;
    }
}
