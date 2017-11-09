/************************************************************************************
 *	file: DynamicFSA
 *	author: Daniel Spencer
 *	class: CS 311 - Formal Languages
 *
 *	assignment: Project 2
 *	date last modified: 11/05/2017
 *
 *	purpose:    To implement the dynamic finite state automaton that recognizes none
 *              initially, but some after reading Input files.
 ************************************************************************************/
import java.io.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//Dynamic finite state automaton Class that uses Transition Mechanism stored in arrayType Data Structor
public class DynamicFSA {
    public int[] swtch;
    public char[] symbol;
    public int[] next;
    public int getSwitch(int i) {return swtch[i];}
    public char getSymbol(int i) {return symbol[i];}
    public int getNext(int i) {return next[i];}
    public int p;

    //constructor
    public DynamicFSA() {
        symbol = new char[1100];
        swtch = new int[54];
        for (int i = 0; i < swtch.length; i++) {
            swtch[i] = -1;
        }
        next = new int[1100];
        for (int i = 0; i < next.length; i++) {
            next[i] = -1;
        }
        p = 0;
    }
    //Flag-- used to verify if keyword input or java file input
    //JAVA key words from inputfile1
    //creates '*' identifiers to recognize keywords
    //uses transition Mechanism
    //transition Mechanism with identifiers "@" for identifers not shown for the first time
    // and "?" for new identifiers. read from InputFile2
    public String transition(String str, int flag) {
        char[] cArray = str.toCharArray();
        int counter = 0;
        char[] cArray2 = new char[cArray.length + 1];
        for (int i = 0; i < cArray.length; ++i) {
            cArray2[i] = cArray[i];
        }

        if(flag == 2) {
            cArray2[cArray.length] = '@';
            int fIndex = getChar(cArray2[counter++]);
            if (fIndex < 0) {
                return null;
            }

            int sIndex = swtch[fIndex];
            if (sIndex < 0) {
                create(cArray2, fIndex, sIndex, counter);
                return str + "? ";
            } else {
                boolean exit = false;
                while (!exit) {
                    if (symbol[sIndex] == cArray2[counter] || (symbol[sIndex] == '*'
                            && cArray2[counter] == '@')) {
                        if (cArray2[counter] != '@') {
                            sIndex++;
                            counter++;
                        } else {
                            return str + symbol[sIndex] + " ";
                        }
                    } else if (next[sIndex] >= 0) {
                        sIndex = next[sIndex];
                    } else {

                        create(cArray2, fIndex, sIndex, counter);
                        return str + "? ";
                    }
                }
            }
        }
        else{
            cArray2[cArray.length] = '*';
            int fIndex = getChar(cArray2[counter++]);
            if (fIndex < 0) {
                return null;
            }
            int sIndex = swtch[fIndex];
            if (sIndex < 0) {
                create(cArray2, fIndex, sIndex, counter);
            }
            else {
                boolean exit = false;
                while (!exit)
                {
                    if (symbol[sIndex] == cArray2[counter]) {
                        if (cArray2[counter] != '*') {
                            sIndex++;
                            counter++;
                        }
                        else {
                            return str + symbol[sIndex] + " ";
                        }
                    }
                    else if (next[sIndex] >= 0) {
                        sIndex = next[sIndex];
                    }
                    else {
                        create(cArray2, fIndex, sIndex, counter);
                        return str + "? ";
                    }
                }
            }
        }
        return null;
    }

    private int getChar(char c) {
        if(c >= 'A' && c <= 'Z'){
            int num = (int)c;
            num -= 65;
            return num;
        }
        else if(c >= 'a' && c <= 'z'){
            int num = (int)c;
            num -= 71;
            return num;
        }
        else if(c == '_'){
            return 52;
        }
        else if(c == '$'){
            return 53;
        }
        else{
            return -1;
        }
    }
    // Creates switch to index into symbols
    private boolean create(char[] cArray, int lIndex, int nIndex, int sIndex) {
        if (swtch[lIndex] < 0) {
            swtch[lIndex] = p;
            for (int i = 1; i < cArray.length; ++i) {
                symbol[p++] = cArray[i];
            }
        }
        else {
            next[nIndex] = p;
            for (int i = sIndex; i < cArray.length; ++i) {
                symbol[p++] = cArray[i];
            }
        }
        return true;
    }




    //----------------------main------------------------------------------------------------
    public static void main(String[] args) throws IOException{

        Scanner inFile = new Scanner(new File("InputFile1.txt"));
        DynamicFSA dfsa = new DynamicFSA();
        char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z', '_','$'};

        try {
            PrintWriter out = new PrintWriter("output.txt");
            while (inFile.hasNext()) {
                dfsa.transition(inFile.next(), 1);
            }
            inFile.close();
            inFile = new Scanner(new File("Inputfile2.txt"));
            String[] input;
            while (inFile.hasNext()) {
                String temp = inFile.nextLine().replaceAll("[^a-zA-Z]", " ");
                input = temp.split(" ");
                for (int i = 0; i < input.length; ++i) {
                    String trans = dfsa.transition(input[i], 2);
                    if (trans != null) {
                        System.out.print(trans);
                    }
                }
                System.out.println();
            }

            System.out.println();

            System.out.printf("%-10s", "\n");
            for(int i = 0; i < 20; ++i){
                System.out.printf("%-5c", alphabet[i]);
            }
            System.out.printf("%-10s", "\nSwitch:");
            for(int i = 0; i < 20; ++i){
                System.out.printf("%-5d", dfsa.getSwitch(i));
            }
            System.out.println();
            System.out.printf("%-10s", "\n");
            for(int i = 20; i < 40; ++i){
                System.out.printf("%-5c", alphabet[i]);
            }
            System.out.printf("%-10s", "\nSwitch:");
            for(int i = 20; i < 40; ++i){
                System.out.printf("%-5d", dfsa.getSwitch(i));
            }
            System.out.println();
            System.out.printf("%-10s", "\n");
            for(int i = 40; i < 54; ++i){
                System.out.printf("%-5c", alphabet[i]);
            }
            System.out.printf("%-10s", "\nSwitch:");
            for(int i = 40; i < 54; ++i){
                System.out.printf("%-5d", dfsa.getSwitch(i));
            }

            System.out.println("\n");

            for(int i = 0; i < 1100;++i){

                if(i != (1100)) {
                    System.out.println();
                    System.out.printf("%-10s", "\n");
                    for (int j = 0; j < 20; ++j ) {
                        System.out.printf("%-5d", i);
                        i++;
                    }
                    //System.out.println(i);
                    i -= 20;
                    //System.out.println(i);
                    System.out.printf("%-10s", "\nsymbol:");
                    for (int j = 0; j < 20; ++j) {
                        System.out.printf("%-5c", dfsa.getSymbol(i));
                        ++i;
                    }
                    i -= 20;

                    System.out.printf("%-10s", "\nnext:");
                    for (int j = 0; j < 20; ++j) {
                        if(dfsa.getNext(i) == -1){
                            System.out.printf("%-5s", " ");
                        }
                        else{
                            System.out.printf("%-5d", dfsa.getNext(i));
                        }
                        ++i;
                    }

//                System.out.println("\n");
                    i -= 1;
                }
//            System.out.printf("%-10s", "\nnext:");
            }

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }
}
