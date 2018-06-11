package kr.ac.pusan.cs.sinbaram.nolinerforadmin;

import java.io.Serializable;

public class User implements Serializable {
    String User_ID ;
    String State ;
    User(){

    }

    public void make(String ID, String State) {
        this.User_ID = ID ;
        this.State = State;
    }
}
