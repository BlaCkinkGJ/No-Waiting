package kr.ac.pusan.cs.sinbaram.nolinerforuser;

public class RegistUser {
    String User_ID ;
    String State ;
    RegistUser(){

    }

    public void make(String ID, String State) {
        this.User_ID = ID ;
        this.State = State;
    }
}
