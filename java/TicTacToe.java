import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.ArrayList;
import java.lang.reflect.Array;

public class TicTacToe {
    /*
     * 
     * Author: Zhongxuan(Daniel) Wang
     * Email: cndanielwang@gmail.com
     * 
     * User player: First player + use 'X' Comp player: Second player + use 'O'
     * 
     */

    public static int[][] board = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
    public static int[] best;

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

            if (check(board, coo)) {
                System.out.println("Bad input");
                continue;
            }

            userturn(coo);

            if (!aiturn())
                break;

            printboard();
        }

        System.out.println("Program running finished");
    }

    /**
     * User's turn
     * 
     * @param coo coordinate
     */
    public static void userturn(int[] coo) {
        downchess(board, coo, -1);
        if (checkwin(board, -1)) {
            printboard();
            System.out.println("\nYou won!\n");
        }
    }

    public static boolean aiturn() {
        int[][] grids = checkavi(board);
        int aviGridNum = grids.length;

        // 2d Array clone
        int[][] states = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, states[i], 0, 3);
        }
        int[] sinf = abp(states, aviGridNum, 1);

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
     * @param state the board.
     * @param coo the coordinate
     * @return True: If occupied. false otherwise.
     */
    public static boolean check(int[][] state, int[] coo) {
        return (coo[0] < state.length && coo[1] < state[0].length) && state[coo[0]][coo[1]] != 0;
    }

    /**
     * Check if the player is won.
     * 
     * @param state the board
     * @param player player
     * @return true : If the player is won false otherwise.
     */
    public static boolean checkwin(int[][] state, int player) {
        // If you changed the game, you can simply change the array below.
        int[][] winstate = { { state[0][0], state[0][1], state[0][2] }, { state[1][0], state[1][1], state[1][2] },
                { state[2][0], state[2][1], state[2][2] }, { state[0][0], state[1][0], state[2][0] },
                { state[0][1], state[1][1], state[2][1] }, { state[0][2], state[1][2], state[2][2] },
                { state[0][0], state[1][1], state[2][2] }, { state[2][0], state[1][1], state[0][2] }, };
        for (int[] coo : winstate) {
            if (coo[0] == coo[1] && coo[1] == coo[2] && coo[0] == player) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check available cells
     * 
     * @param state the board
     * @return an array of integer shows the coordinate of the empty cells
     */
    public static int[] @NotNull [] checkavi(int[][] state) {
        ArrayList<Integer> emptycells = new ArrayList<>();
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
     * @param state the board
     * @param coo coordinate
     * @param player player
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

    /**
     * Get coordinate of input x,y
     * 
     * @param coo coordinate
     * @return the array of coordinate.
     */
    public static int[] getcood(String coo) {
        try {
            return new int[] { Integer.parseInt(coo.split(",")[0]) - 1,
                    Integer.parseInt(coo.split(",")[1]) - 1 };
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
