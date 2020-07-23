//Logan D'Auria (lxd1644@rit.edu)
//Turing Machine Program
//11.30.2018

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TM {
    public static void main(String[] args) {

        int debug = 0;

        //File input
        Scanner input = new Scanner(System.in);
        System.out.print("Turing machine specification file name: ");
        String filename = input.next();
        File file = new File(filename);
        Scanner fileIn = null;
        try {
            fileIn = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        TuringMachine tm = new TuringMachine();

        String line = "";
        //Assign the TM's states(q), alphabet(sigma), start state(s), and accept states(f)
        for(int x = 0; x < 6; x ++){
            line = fileIn.nextLine();
            if(line.charAt(0) == '#') //ignore any lines starting with #
                x--;
            else{
                if(x == 0){ //states
                    tm.q.addAll(Arrays.asList(line.split(" ")));
                }else if(x == 1){ //input alphabet (sigma)
                    tm.sigma.addAll(Arrays.asList(line.split(" ")));
                }else if(x == 2){ //tape alphabet (gamma)
                    tm.gamma.addAll(Arrays.asList(line.split(" ")));
                }else if(x == 3){ //start state
                    tm.s = line.trim();
                }else if(x == 4){ //accept state
                    tm.a = line.trim();
                }else if(x == 5){ //reject state
                    tm.r = line.trim();
                }
            }
        }

        if(debug > 0) {
            System.out.println("Q = " + tm.q);
            System.out.println("Sigma = " + tm.sigma);
            System.out.println("Gamma = " + tm.gamma);
            System.out.println("Start = " + tm.s);
            System.out.println("Accept = " + tm.a);
            System.out.println("Reject = " + tm.r);
        }

        //Assign the TM's transition function (delta)
        while(fileIn.hasNextLine()){
            line = fileIn.nextLine();
            if(line.length() > 0 && line.charAt(0) != '#') {

                while(line.contains("  ")) //gets rid of extra white space in file
                    line = line.replace("  ", " ");

                //Create token list to separate states and symbols
                ArrayList<String> tokens = new ArrayList<>();
                tokens.addAll(Arrays.asList(line.split(" ")));

                //Hash the state-symbol pair with the destination
                line = tokens.get(0) + " " + tokens.get(1);
                tm.delta.put(line, tokens.get(2) + " " + tokens.get(3) + " " + tokens.get(4));
            }
        }

        if(debug > 0) {
            for (String key : tm.delta.keySet()) {
                System.out.println("transition: (" + key + ") -> " + tm.delta.get(key));
            }
        }

        input.nextLine(); //clears input scanner buffer

        System.out.print("--> ");
        while(!(line = input.nextLine()).equals("")){
            process(tm, line);
            System.out.print("-> ");
        }
        System.out.println("Goodbye");
    }


    /**
     * Tests a given string on a TM, while printing the configurations of the tape
     * @param tm: TM to test on
     * @param s: string being tested on TM
     */
    public static void process(TuringMachine tm, String s){
        ArrayList<String> config = new ArrayList<>();
        int head = 0;
        String transition = "";

        //Initialize starting config
        config.add(tm.s);
        config.addAll(Arrays.asList(s.split("")));

        //Apply transitions to the config until a reject or accept state is reached
        while(! config.contains(tm.r)){
            if(head + 1 == config.size()) //adds a space if head reaches end of config
                config.add("u");

            System.out.println(ConfigToString(config));

            //Receives desired transition from state-symbol pair
            String key = config.get(head) + " " + config.get(head + 1);
            if(tm.delta.keySet().contains(key)){
                transition = tm.delta.get(key);
            } else { //Goes to reject state if state-symbol pair does not exist
                config.set(head, tm.r);
                System.out.println(ConfigToString(config));
                return;
            }

            //Tokenizes the transition for separate processing
            ArrayList<String> tokens = new ArrayList<>();
            tokens.addAll(Arrays.asList(transition.split(" ")));

            //Changes the config based on transition
            config.set(head + 1, tokens.get(1));
            config.set(head, tokens.get(0));

            //Swaps the head with a symbol in config; depending on direction (L or R)
            if(tokens.get(2).equals("R")){
                String swap = config.get(head + 1);
                config.set(head + 1, config.get(head));
                config.set(head, swap);
                head++;
            } else if(tokens.get(2).equals("L")){
                if(head > 0) {
                    String swap = config.get(head - 1);
                    config.set(head - 1, config.get(head));
                    config.set(head, swap);
                    head--;
                }
            }

            //Checks if the config reached the accept state
            if(config.contains(tm.a)){
                if(head + 1 == config.size())
                    config.add("u");
                System.out.println(ConfigToString(config));
                return;
            }
        }
        //Prints the config at the end if it was rejected
        if(head + 1 == config.size())
            config.add("u");
        System.out.println(ConfigToString(config));
    }

    /**
     * converts a given config list into desired string format
     */
    public static String ConfigToString(ArrayList<String> cfg){
        String s = "";
        for(String p : cfg){
            s += p;
        }
        return s;
    }
}
