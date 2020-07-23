//Logan D'Auria (lxd1644@rit.edu)
//Turing Machine Class
//11.30.2018

import java.util.ArrayList;
import java.util.Hashtable;

public class TuringMachine {
    public ArrayList<String> q = new ArrayList<String>(); //set of states
    public ArrayList<String> sigma = new ArrayList<String>(); //input alphabet
    public ArrayList<String> gamma = new ArrayList<String>(); //tape alphabet
    public Hashtable<String, String> delta = new Hashtable<String, String>(); //transition function
    public String s; //start state
    public String a; //accept state
    public String r; //reject state
}
