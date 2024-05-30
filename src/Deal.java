package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * DORNOD
 * A Text-Based Deal or No Deal Clone
 * By Josiah Parrott
 */

public class Deal {

        //ToC--------------//
        //Constants        //
        //Printer Helpers  //
        //Input Helpers    //
        //Case Helpers     //
        //Banker Helpers   //
        //Rounds           //
        //Main             //
        //-----------------//

        //CONSTANTS-------------------------------------//
        int NUMCASES = 26;              //-- Total number of cases
        int DEFCLEARLENGTH = 30;
        String revline = "\033[F\r";    //-- Reverse line token
        //String clearline = "                                                            "; //-- Whole lotta white space
        //CONSTANTS-------------------------------------//

        
        //PRINTER HELPERS-------------------------------//

        /**
         * Shorter sout print bc why is it so stupidly long and annoying to type
         * @param msg
         */
        public void printLine(String msg) { System.out.print(msg); }

        /**
         * Clear n lines
         * @param n
         */
        public void clearLine(int row, int col) {
                for (int i = 0; i < row; i++) {
                        printLine(revline);
                        for (int j = 0; j < col; j++) {
                                System.out.print(" ");
                        }
                        //printLine(clearline);
                }
        }

        public void printTitle(Scanner s) {
                printLine("██████   ██████  ██████  ███    ██  ██████  ██████  \n");
                printLine("██   ██ ██    ██ ██   ██ ████   ██ ██    ██ ██   ██ \n");
                printLine("██   ██ ██    ██ ██████  ██ ██  ██ ██    ██ ██   ██ \n");
                printLine("██   ██ ██    ██ ██   ██ ██  ██ ██ ██    ██ ██   ██ \n");
                printLine("██████   ██████  ██   ██ ██   ████  █████  ██████  \n");
                pressEnter(8, 52, s);
        }

        /**
         * Print out the case list
         * @param cases
         * @param cl
         */
        public void casePrintOut(Case[] cases, int cl) {
                clearLine(cl, 30);
                printLine("\n  ");
                for (int i = 0; i < cases.length; i++) {
                        if (i == 6 || i == 19) {
                                printLine("\n");
                        } else if (i == 13) {
                                printLine("\n  ");
                        }

                        if (cases[i].getCanBePicked()) {
                                System.out.print(cases[i].getNum());

                                if (cases[i].getNum() > 9) {
                                        printLine("  ");
                                } else {
                                        printLine("   ");
                                }
                        } else {
                                printLine("X   ");
                        }
                }
        }

        /**
         * Press enter prompt, go back clearlines lines
         * @param clearlines
         * @param s
         */
        public void pressEnter(int clearlines, int length, Scanner s) {
                String input;
                while (true) {
                        printLine("\npress ENTER\n");
                        input = s.nextLine();
                        if (input.isEmpty()) {
                                clearLine(clearlines, length);
                                break;
                        } else {
                                clearLine(3, length);
                        }
                }
        }

        public String formatAmount(double amount) {
                String format;
                if (amount < 1) {
                        format = String.format("%.2f", amount);
                } else {
                        format = String.format("%,.0f", amount);
                }
                return format;
        }
        //PRINTER HELPERS-------------------------------//



        //INPUT HELPERS---------------------------------//

        /**
         * Determine if the input is valid
         * @param input
         * @param s
         * @return
         */
        public int inputCheck(String input, Scanner s) {
                int inputInt = -1;

                if (!isNumeric(input) || input == null) {
                        printLine("That's not a case!\n");
                        pressEnter(7, DEFCLEARLENGTH, s);
                } else {
                        int temp = Integer.parseInt(input);
                        if (temp > NUMCASES) {
                                printLine("That's not a case!\n");
                                pressEnter(7, DEFCLEARLENGTH, s);
                        } else {
                                inputInt = temp;
                        }
                }
                return inputInt;
        }

        /**
         * Determine if the input is numeric
         * @param strNum
         * @return
         */
        public static boolean isNumeric(String strNum) {
                if (strNum == null) {
                        return false;
                }
                try {
                        int i = Integer.parseInt(strNum);
                } catch (NumberFormatException nfe) {
                        return false;
                }
                return true;
        }
        //INPUT HELPERS---------------------------------//
        
        

        //CASE HELPERS----------------------------------//

        /**
         * Set up cases
         * @return
         */
        public Case[] setCases() {
                Case[] caseList = new Case[NUMCASES];                   //-- List of cases
                ArrayList<Double> caseAmounts = new ArrayList<>();      //-- List of amounts

                //-- All them numbers
                double[] amounts = {0.01,1,5,10,25,50,75,100,200,300,400,500,750,1000,5000,10000,25000,50000,75000,100000,200000,300000,400000,500000,750000,1000000};

                for (int i = 0; i < amounts.length; i++)
                        caseAmounts.add(amounts[i]);

                Collections.shuffle(caseAmounts); //-- Shuffle up the amounts

                //-- Assign the amounts to the cases
                for (int i = 0; i < NUMCASES; i++) {
                        caseList[i] = new Case((i+1), caseAmounts.get(i));
                        caseList[i].setCanBePicked(true);
                }

                return caseList;
        }

        public Case pickCase(Case[] cases, int caseNum, Scanner s) {
                String input;
                int inputInt = 0;
                Case thisCase = cases[caseNum - 1];

                if (thisCase.getCanBePicked()) {
                        thisCase.setCanBePicked(false);
                        return thisCase;
                } else {
                        printLine("You already chose that case!\n");
                        pressEnter(7, DEFCLEARLENGTH, s);                    

                        while (true) {
                                printLine("\nPick a different case:\n");
                                input = s.nextLine();
        
                                inputInt = inputCheck(input, s);
                                if (inputInt > 0) {break;}
                        }

                        Case newCase = pickCase(cases, Integer.parseInt(input), s);
                        return newCase;
                }
        }    
        //CASE HELPERS----------------------------------//
        
          
        
        //BANKER HELPERS--------------------------------//

        /**
         * Determine the bankers offer
         * @param cases
         * @param round
         * @return
         */
        public double offer(Case[] cases, Case yourCase, int round) {
                double avg = 0;
                int casesLeft = 0;
                int offer;

                //-- Find the average --//
                for (int i = 0; i < cases.length; i++) {
                        if (cases[i].getCanBePicked()) {
                                avg += cases[i].getAmount();
                                casesLeft++;
                        }
                }

                avg += yourCase.getAmount();
                casesLeft++;

                avg = (avg / casesLeft);


                //-- Get the offer --//
                double bigOffer = avg * (0.15 + (((1 - 0.15) / 9) * (round - 1)));

                if (bigOffer > 10000) { //-- Round to the nearest 1000
                        offer = (int) ((bigOffer + 999) / 1000) * 1000;
                } else {                //-- Round to the nearest 100
                        offer = (int) ((bigOffer + 99) / 100) * 100;
                }

                return offer;
        }

        /**
         * You want the offer or naw?
         * @param offer
         * @param yourCase
         * @param s
         * @return
         */
        public boolean banker(double offer, Case yourCase, Scanner s) {
                String input;
                String fOffer = formatAmount(offer);                    //-- Formatted offer
                String fYourCase = formatAmount(yourCase.getAmount());  //--Formatted case amount

                printLine("\nThe banker is offering you $" + fOffer + "\n");

                while (true) {
                        printLine("\nDEAL...OR NO DEAL?\n");
                        input = s.nextLine();

                        if (input.equalsIgnoreCase("deal") || input.equalsIgnoreCase("no deal")) { break; }
                        clearLine(3, DEFCLEARLENGTH);
                }

                if (input.equalsIgnoreCase("deal")) {
                        printLine("YOU JUST WON $" + fOffer + "!!!!\n");
                        printLine("Your case number " + yourCase.getNum() + " had $" + fYourCase + " in it!\n");
                        return false;
                } else if (input.equalsIgnoreCase("no deal")) {
                        printLine("You just turned down the banker's offer of $" + fOffer + "!\n");
                        pressEnter(9, 52, s);
                        return true;
                }
                return false;
        }
        //BANKER HELPERS--------------------------------//



        //ROUNDS----------------------------------------//

        /**
         * Pick your case
         * @param cases
         * @param s
         * @return
         */
        public Case initRound(Case[] cases, Scanner s) {
                String input;
                int inputInt = 0;

                System.out.println("\nWelcome to DORNOD!");
                casePrintOut(cases, 0);
                System.out.println();
                while (true) {
                        printLine("\nPlease choose your case:\n");
                        input = s.nextLine();

                        inputInt = inputCheck(input, s);
                        if (inputInt > 0) {break;}

                }
                Case yourCase = pickCase(cases, inputInt, s);
                casePrintOut(cases, 8);
                printLine("\n\nYou chose case number: " + yourCase.getNum() + "\n");
                pressEnter(5, DEFCLEARLENGTH, s);
                return yourCase;
        }

        /**
         * Open up numPicks cases
         * @param cases
         * @param yourCase
         * @param numPicks
         * @param round
         * @param s
         * @return
         */
        public boolean regRound(Case[] cases, Case yourCase, int numPicks, int round, Scanner s) {
                String input;
                int inputInt = 0;
                for (int i = numPicks; i > 0; i--) {
                        while (true) {
                                printLine("\nOpen up case " + (numPicks - (i-1)) + "/" + numPicks + ":\n");
                                input = s.nextLine();

                                inputInt = inputCheck(input, s);
                                if (inputInt > 0) {break;}
                        }

                        pickCase(cases, inputInt, s);

                        casePrintOut(cases, 8);
                        printLine("\n\nCase number " + inputInt + " had $" + formatAmount(cases[inputInt - 1].getAmount()) + "\n");
                        pressEnter(5, 50, s);
                }
                if (round > 4) { round = 4; }
                return banker(offer(cases, yourCase, round), yourCase, s);
        }

        /**
         * Ride or die with yourCase
         * @param yourCase
         * @param s
         */
        public void finalRound(Case yourCase, Scanner s) {
                String fYourCase = formatAmount(yourCase.getAmount()); //--Formatted case amount

                System.out.println("\nOnly two cases left...");
                pressEnter(5, DEFCLEARLENGTH, s);
                System.out.println("\nYou chose to go all the way with case number " + yourCase.getNum() + ".");
                pressEnter(5, 50, s);
                System.out.println("\nYOU");
                pressEnter(5, 5, s);
                System.out.println("\nWON");
                pressEnter(5, 5, s);
                System.out.println("\n" + fYourCase + "!!!!");
        }
        //ROUNDS----------------------------------------//

        
        
        
        
        public static void main(String[] args) {
                Deal play = new Deal();

                Case[] cases = play.setCases();
                Case yourCase = new Case(0, 0);

                Scanner s = new Scanner(System.in);

                play.printTitle(s);
                
                int regrounds = 6;
                boolean keepPlaying = true;

                for (int round = 0; round <= 10;) {
                        if (round == 0) {               //-- Pick your case
                                yourCase = play.initRound(cases, s);
                                round++;
                        } else if (round <= 5) {        //-- Open x cases at a time
                                keepPlaying = play.regRound(cases, yourCase, regrounds, round, s);
                                regrounds--;
                                round++;
                                if (!keepPlaying) {
                                        round = 11; 
                                }
                        } else if (round <= 9) {        //-- Open 1 case at a time
                                keepPlaying = play.regRound(cases, yourCase, 1, round, s);
                                round++;
                                if (!keepPlaying) {
                                        round = 11; 
                                }
                        } else if (round == 10) {       //-- Ride or die with your case
                                play.finalRound(yourCase, s);
                                round++;
                        }

                }

                s.close();
        }
}