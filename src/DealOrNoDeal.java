import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Text-Based Deal or No Deal Game
 *
 * @author Josiah Parrott
 */

public class DealOrNoDeal {

    /**
     * Loads the case amounts from the file
     * into 26 cases and randomizes the amounts
     *
     * @return  List of randomized cases
     */
    public Case[] setCases() {
        File file = new File("CaseAmounts.txt");
        Scanner s = null;
        ArrayList<String> amounts = new ArrayList<>();

        Case[] caseList = new Case[26];

        for (int i = 0; i < 26; i++) {
            caseList[i] = new Case(0, 0);
        }

        try {
            s = new Scanner(file);

            while (s.hasNext()) {
                amounts.add(s.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
            }
        }

        Collections.shuffle(amounts);


        for (int i = 0; i < caseList.length; i++) {
            caseList[i].setNum(i + 1);
            caseList[i].setAmount(Integer.parseInt(amounts.get(i)));
        }

        return caseList;
    }

    /**
     * Takes in case from case list.
     * If the case can be picked, switch
     * to can't be picked, and return the case.
     *
     * @param cases List of cases
     * @param caseNum   Number of case being picked
     * @return  The case that was picked
     */
    public Case pickCase(Case[] cases, int caseNum) {
        Scanner s = new Scanner(System.in);

        Case thisCase = cases[caseNum - 1];
        if (thisCase.canBPicked) {
            thisCase.setCanBPicked(false);
            return thisCase;
        } else {
            System.out.println("You already chose that case!");
            System.out.println("Pick a different case:");
            String input = s.nextLine();
            Case fooCase = pickCase(cases, Integer.parseInt(input));
            return fooCase;
        }
    }

    /**
     * Prints out the case list in
     * a grid. If the case can be
     * picked then print the number,
     * else print an 'X'.
     *
     * @param cases List of cases
     */
    public void casePrintOut(Case[] cases) {
        System.out.println();
        for (int i = 0; i < cases.length; i++) {
            if (i == 6 || i == 13 || i == 19) {
                System.out.println();
            }
            if (cases[i].canBPicked) {
                System.out.print(cases[i].getNum());
                System.out.print(" ");
            } else {
                System.out.print("X");
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Formula to determine the bank offer based
     * on the sum of the numbers over 100,000 left
     * the sum of the numbers under that left, and
     * the amount of cases left.
     *
     * @param sumBigLeft    Sum of big numbers left
     * @param sumSmallLeft  Sum of small numbers left
     * @param amountLeft    Amount of cases left
     * @return  The offer
     */
    public int bankOffer(int sumBigLeft, int sumSmallLeft, int amountLeft) {
        int offer;

        if (amountLeft == 19) {
            offer = (int) ((0.0027 * sumBigLeft) + (0.05 * sumSmallLeft));
        } else if (amountLeft == 14) {
            offer = (int) ((0.0144 * sumBigLeft) + (0.09 * sumSmallLeft));
        } else if (amountLeft == 10) {
            offer = (int) ((0.0273 * sumBigLeft) + (0.13 * sumSmallLeft));
        } else if (amountLeft == 7) {
            offer = (int) ((0.0442 * sumBigLeft) + (0.17 * sumSmallLeft));
        } else if (amountLeft == 5) {
            offer = (int) ((0.062 * sumBigLeft) + (0.2 * sumSmallLeft));
        } else if (amountLeft == 4) {
            offer = (int) ((0.1025 * sumBigLeft) + (0.25 * sumSmallLeft));
        } else if (amountLeft == 3) {
            offer = (int) ((0.1683 * sumBigLeft) + (0.33 * sumSmallLeft));
        } else if (amountLeft == 2) {
            offer = (int) ((0.305 * sumBigLeft) + (0.5 * sumSmallLeft));
        } else if (amountLeft == 1) {
            offer = (int) ((0.355 * sumBigLeft) + (0.5 * sumSmallLeft));
        } else {
            offer = 0;
        }

        if (offer >= 1000) {
            return (int) (Math.round(offer / 1000.0) * 1000);
        } else if (offer >= 100) {
            return (int) (Math.round(offer / 100.0) * 100);
        } else if (offer >= 10) {
            return (int) (Math.round(offer / 10.0) * 10);
        } else {
            return 1;
        }
    }

    /**
     * The first round where the
     * user chooses their case.
     *
     * @param cases List of cases
     * @return  The user's case
     */
    public Case initRound(Case[] cases) {
        String input;
        Scanner s = new Scanner(System.in);

        System.out.println("Welcome to Deal or No Deal!");
        casePrintOut(cases);
        System.out.println("Please choose your case: ");
        input = s.nextLine();
        Case yourCase = pickCase(cases, Integer.parseInt(input));

        System.out.println("You chose case number: " + yourCase.getNum());
        return yourCase;
    }

    /**
     * The standard rounds where the user picks a certain amount of
     * cases each round.
     *
     * @param cases The list of cases
     * @param yourCase  The user's case
     * @param numCases  Number of cases needed to be picked that round
     * @param amountLeft    The amount of total cases left
     * @param casesPickedBig    List of big amounts picked
     * @param casesPickedSmall  List of small amounts picked
     * @return  Deal or No Deal with bank offer
     */
    public boolean regRound(Case[] cases, Case yourCase, int numCases, int amountLeft, LinkedList<Case> casesPickedBig, LinkedList<Case> casesPickedSmall) {
        Scanner s = new Scanner(System.in);

        int sumBig;
        int sumSmall;
        int sumBigLeft = 0;
        int sumSmallLeft = 0;


        for (int i = numCases; i > 0; i--) {
            sumBig = 0;
            sumSmall = 0;

            System.out.println();
            casePrintOut(cases);
            System.out.println("Please choose " + i + " cases: ");
            String input = s.nextLine();
            Case pickedCase = pickCase(cases, Integer.parseInt(input));

            if (pickedCase.getAmount() >= 100000) {
                casesPickedBig.add(pickedCase);
            } else {
                casesPickedSmall.add(pickedCase);
            }

            if (!casesPickedBig.isEmpty()) {
                for (Case aCase : casesPickedBig) {
                    sumBig = sumBig + aCase.getAmount();
                }
            }
            if (!casesPickedSmall.isEmpty()) {
                for (Case aCase : casesPickedSmall) {
                    sumSmall = sumSmall + aCase.getAmount();
                }
            }

            sumBigLeft = 3250000 - sumBig;
            sumSmallLeft = 168416 - sumSmall;

            System.out.println(cases[Integer.parseInt(input) - 1]);
        }

        return dealOrNoDeal(bankOffer(sumBigLeft, sumSmallLeft, amountLeft), yourCase);
    }

    /**
     * The final round that happens if the user never
     * takes a deal and goes all the way with their
     * chosen case.
     *
     * @param yourCase  The user's case
     */
    public void finalRound(Case yourCase) {
        System.out.println("Only two cases left...");
        System.out.println("You chose to go all the way with case number " + yourCase.getNum() + ".");
        System.out.println("YOU\nWON\n$" + yourCase.getAmount() + "!!!!");
    }

    /**
     * The part that happens after each round where
     * the banker offers the user a deal and the user
     * decides to take the deal or keep playing.
     *
     * @param bankOffer The banker's offer
     * @param yourCase  The user's case
     * @return  Whether the user took the deal or not
     */
    public boolean dealOrNoDeal(int bankOffer, Case yourCase) {
        Scanner s = new Scanner(System.in);
        System.out.println("\nThe Banker is offering you $" + bankOffer);
        System.out.println("DEAL?... or NO DEAL?");
        String input = s.nextLine();
        if (input.equalsIgnoreCase("deal")) {
            System.out.println("YOU JUST WON $" + bankOffer + "!!!!");
            System.out.println("Your case number " + yourCase.getNum() + " had $" + yourCase.getAmount() + " in it!");
            return false;
        } else {
            System.out.println("You just turned down the Banker's offer of $" + bankOffer + "!");
            return true;
        }
    }

    public static void main(String[] args) {
        DealOrNoDeal play = new DealOrNoDeal();

        Case[] cases = play.setCases();

        LinkedList<Case> casesPickedBig = new LinkedList<>();
        LinkedList<Case> casesPickedSmall = new LinkedList<>();

        int round = 0;
        int amountLeft = 26;

        Case yourCase = null;

        boolean keepPlaying;

        while (round == 0) {
            yourCase = play.initRound(cases);
            amountLeft--;
            round++;
        }

        while (round == 1) {
            for (int i = 6; i > 1; i--) {
                amountLeft = amountLeft - i;
                keepPlaying = play.regRound(cases, yourCase, i, amountLeft, casesPickedBig, casesPickedSmall);
                if (!keepPlaying) {
                    return;
                }
            }
            round++;
        }

        while (round == 2) {
            keepPlaying = play.regRound(cases, yourCase, 1, amountLeft, casesPickedBig, casesPickedSmall);
            amountLeft--;
            if (!keepPlaying) {
                return;
            }
            if (amountLeft == 1) {
                round++;
            }
        }

        while (round == 3) {
            play.finalRound(yourCase);
            return;
        }
    }
}
