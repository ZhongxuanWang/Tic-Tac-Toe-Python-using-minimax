import java.util.Scanner;
import java.util.ArrayList;
import java.lang.reflect.Array;

public class TicTacToe {
    /*
     * 
     * Author: Zhongxuan(Daniel) Wang Website: dlearninglab.com Email:
     * cndanielwang@gmail.com
     * 
     * User player: First player + use 'X' Comp player: Second player + use 'O'
     * 
     */

    public static int[][] board = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
    public static int[] bestmove = new int[2];

    public static void main(String[] args) {
        Scanner scr = new Scanner(System.in);

        clearScreen();
        printboard();

        while (true) {
            System.out.print("y,x:");
            // Get coordinate from user and convert it into int array
            int[] coo = getcood(scr.nextLine());
            // If there's a reported error, do not proceed the remaining lines.
            if (coo[0] == -1)
                continue;

            // Including:
            // check whether occupied --> down the chess --> check if won
            if (!userturn(coo))
                break;

            if (!aiturn())
                break;

            printboard();
        }

        System.out.println("Program running finished");
    }

    /**
     * User's turn
     * 
     * @param coo
     * @return false: If the program doesn't want to continue executing
     * @return true: If the program wants to continue executing
     */
    public static boolean userturn(int[] coo) {
        if (checkocc(board, coo)) {
            System.out.println("Bad input");
            return true;
        }
        downchess(board, coo, -1);
        if (checkwin(board, -1)) {
            printboard();
            System.out.println("\nYou won!\n");
            return false;
        }
        return true;
    }

    public static boolean aiturn() {
        int[][] grids = checkavi(board);
        int gsize = grids.length;

        // 2d Array clone
        int[][] states = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                states[i][j] = board[i][j];
            }
        }
        int[] sinf = abp(states, gsize, 1);

        downchess(board, new int[]{sinf[0],sinf[1]}, 1);

        if (checkwin(board, 1)) {
            System.out.println("You Lost!");
            return false;
        }
        if(size(checkavi(board)) == 0) {
            System.out.println("Draw!");
            return false;
        }
        return true;
    }

    public static int[] abp(int[][] state, int depth, int player) {
        int[] best;
        if (player == 1) {
            best = new int[] {-1, -1, -1};
        } else {
            best = new int[] {-1, -1, 1};
        }

        if (checkwin(state, 1))
            return new int[] {-1, -1, 1};
        
        if (checkwin(state, -1))
            return new int[] {-1, -1, -1};

        if (depth == 0)
            return new int[] {-1, -1, 0};

        for (int[] c : checkavi(state)) {

            state[c[0]][c[1]] = player;
            int[] score = abp(state, depth-1, -player);
            state[c[0]][c[1]] = 0;

            score[0] = c[0];
            score[1] = c[1];

            if (player == 1) {
                if (score[2] > best[2]) {
                    best = score;
                }
            } else {
                if (score[2] < best[2]) {
                    best = score;
                }
            }
        }
        return best;
    }


    /**
     * Print the board
     */
    public static void printboard() {
        char c = 'a';
        System.out.println("\n  1 2 3");
        for (int i = 0; i <= 2; i++) {
            System.out.print(i + 1);
            for (int j = 0; j <= 2; j++) {
                if (board[i][j] == 0)
                    c = '-';
                if (board[i][j] == 1)
                    c = 'O';
                if (board[i][j] == -1)
                    c = 'X';
                System.out.print(" " + c);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Clear the screen
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Check if the board is occupied.
     * 
     * @param c
     * @return True: If occupied.
     * @return False: If not occupied
     */
    public static boolean checkocc(int[][] state, int[] coo) {
        return state[coo[0]][coo[1]] != 0 ? true : false;
    }

    /**
     * Check if the player is won.
     * 
     * @param state
     * @param player
     * @return true : If the player is won
     * @return false : If the player is not won
     */
    public static boolean checkwin(int[][] state, int player) {
        // If you changed the game, you can simply change the array below.
        int[][] winstate = { { state[0][0], state[0][1], state[0][2] }, { state[1][0], state[1][1], state[1][2] },
                { state[2][0], state[2][1], state[2][2] }, { state[0][0], state[1][0], state[2][0] },
                { state[0][1], state[1][1], state[2][1] }, { state[0][2], state[1][2], state[2][2] },
                { state[0][0], state[1][1], state[2][2] }, { state[2][0], state[1][1], state[0][2] }, };
        for (int[] coo : winstate) {
            if (coo[0] == coo[1] && coo[1] == coo[2] && coo[0] == coo[2] && coo[0] == player) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check available cells
     * 
     * @param state
     * @return an array of integer shows the coordinate of the empty cells
     */
    public static int[][] checkavi(int[][] state) {
        ArrayList<Integer> emptycells = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0)
                    emptycells.add(coo2num(new int[]{i,j}));
            }
        }
        int[][] ret = new int[emptycells.size()][2];
        for (int i = 0; i < emptycells.size(); i ++) {
            ret[i] = num2coo(emptycells.get(i)).clone();
        }
        return ret;
    }

    /**
     * Down the chess
     * 
     * @param state
     * @param coo
     * @param player
     */
    public static void downchess(int[][] state, int[] coo, int player) {
        try {
            state[coo[0]][coo[1]] = player;
        } catch (Exception e) {
            System.out.println("Please give me a coordinate within the board.");
        }
    }


    // For convenience....
    public static int[] num2coo(int num)
    {
        return new int[] {num/3, num%3};
    }

    public static int coo2num(int[] coo)
    {
        return coo[0]*3 + coo[1];
    }

    // ---------get
    /**
     * Get coodinate of input x,y
     * 
     * @param coordinate
     * @return the array of coordinate.
     */
    public static int[] getcood(String coo) {
        try {
            int[] inte = new int[] { Integer.parseInt(coo.split(",")[0]) - 1, Integer.parseInt(coo.split(",")[1]) - 1 };
            return inte;
        } catch (Exception e) {
            System.out.println("Bad Input");
            return new int[] { -1 };
        }
    }

    public static int size(Object object) {
        if (!object.getClass().isArray()) {
            return 1;
        }

        int size = 0;
        for (int i = 0; i < Array.getLength(object); i++) {
            size += size(Array.get(object, i));
        }
        return size;
    }
}
