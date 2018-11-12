package chkblnc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class main
    {
        public static boolean verbose=false;

        public static void main(String[] args) throws Exception // does not handle them
            {
                ArrayList<String> adr = new ArrayList<String>();
                // read in data
                //adr = addDummyData();
                adr = readData();
                // check balance
                adr = chkblnc(adr);
                // write data
                writeData(adr);
            }

        private static void writeData(ArrayList<String> adr) throws IOException
            {
                File file = new File ("out.txt");
                if (main.verbose) System.out.println("does out.txt exist?");
                if (!file.exists()) {
                    file.createNewFile();
                    if (main.verbose) System.out.println("creating new out.txt");
                }
                FileWriter fw = new FileWriter (file.getAbsoluteFile());
                BufferedWriter out = new BufferedWriter(fw);
                if (main.verbose) System.out.println("Writing header");
                out.write("---------- BTC address ----------| : Balance in BTC (Satoshi)");
                out.newLine();
                System.out.print("Writing data...");
                for (String write : adr){
                    out.write(write);
                    out.newLine();
                    if (main.verbose) System.out.print(".");
                }
                out.close();
                if (main.verbose) System.out.print("done.");
            }

        private static ArrayList<String> readData() throws IOException
            {
                int i = 0;
                if (main.verbose) System.out.println(System.getProperty("user.dir"));
                File file = new File ("in.txt");
                ArrayList<String> data = new ArrayList<String>();
                System.out.print("Reading input...");
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String str;
                while ((str=in.readLine()) != null){
                    if ((str.length() > 26) && (str.length() < 35)){
                        data.add(str);
                        if (main.verbose) System.out.print(".");
                        i++;
                    }
                    else {
                        if (main.verbose) System.out.print(":"); // line to short/long to be valid address
                    }
                    
                }
                in.close();
                if (main.verbose) System.out.println("\nSuccessfully read " + i + " lines."); else System.out.print("\n");
                return data;
            }

        private static ArrayList<String> chkblnc(ArrayList<String> adr) throws IOException
            {
                ArrayList<String> hilf = new ArrayList<String>();
                for (String check : adr){
                    if (main.verbose) System.out.println("Checking balance for: "+check + "...");
                    URL bc_api = new URL("http://blockchain.info/q/addressbalance/"+check);
                    URLConnection yc = bc_api.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Balance of " + check + " is " + inputLine + " Satoshi");
                        double btc = (Double.parseDouble(inputLine)/100000000);
                        System.out.println("Balance of " + check + " is " + btc + " BTC");
                        hilf.add(check + " : " + btc + " (" + inputLine+ ")");
                        if (main.verbose) System.out.println(check + " - " + inputLine);
                    }
                    in.close();                        
                }
                return hilf;
            }

        private static ArrayList<String> addDummyData()
            {
                ArrayList<String> dummy = new ArrayList<String>();
                // pseudorandomdata from Block #315622 according to blockchain.info
                dummy.add("186Aow5EgRz6WNroi2Eky8c94eZvxpWAEd");
                dummy.add("19QkbYPu81MHkSdBxKXUm39nyH14z27gf6");
                dummy.add("19wLQ9PbptbrhXywyJ1FmmwckmH8x367Ez");
                dummy.add("1MF48CPkGmnCXp54ztp9gTRCscpHzGvEC4");
                dummy.add("1Q147jbV8RFrBiW9JfNgZGNUtGoYnxsGED");
                // this one is actually mine, donations welcome ;)
                dummy.add("1LD6GEDrStmKYUjVnDwqySDqU6BUCo7Boc");
                return dummy;
            }

    }
