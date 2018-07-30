package kr.ac.pusan.cs.sinbaram.nolinerforuser;

import java.io.Serializable;

public class Obj_User implements Serializable {
    String User_ID ;
    String State ;
    Obj_User(){

    }

    public void make(String ID, String State) {
        this.User_ID = ID ;
        this.State = State;
    }
}
