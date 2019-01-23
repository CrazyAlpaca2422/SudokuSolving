import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Sudoku2 {
	//big dimension
	public static int dimension;
	//small dimension
	public static int box;
	//the game board
	public static String[][] sudokuPuzzle;
	//holds all of the domains
	public static Set<Integer> domain = new HashSet<Integer>(500);
	//are we finished yet?
	public static boolean done = false;

	public static void main(String[] args) throws FileNotFoundException {
		// File sudoku = new File(args[0]);
		File sudoku = new File("test1");
		//create the puzzle
		sudokuPuzzle = createPuzzle(sudoku);
		
		//solveNormal boolean
		//start with 0, 0 so it will go through whole puzzle
		//hopefully
		done = solveNormal(sudokuPuzzle, 0, 0);
		
		//solveMRV boolean
		
		if(done){
			printSudoku(sudokuPuzzle);
		}
		
		
		//sudokuSolver(sudokuPuzzle);
	}
	
	public static String[][] createPuzzle(File sud) throws FileNotFoundException {
		int row = 0;
		Scanner scan = new Scanner(sud);

		String[] first = scan.nextLine().split(" ");
		dimension = Integer.parseInt(first[0]);
		box = Integer.parseInt(first[1]);

		String[][] puzzle = new String[dimension][dimension];

		while (scan.hasNextLine()) {
			String cur = scan.nextLine();
			String[] line = cur.split("");
			for (int col = 0; col < line.length; col++) {
				puzzle[row][col] = line[col];
			}
			row++;
		}
		printSudoku(puzzle);
		System.out.println();
		return puzzle;
	}

	

	public static void printSudoku(String[][] sudoku) {
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku.length; col++) {
				System.out.print(sudoku[row][col].toString());
			}
			System.out.println();
		}
	}

	public static boolean solveNormal(String[][] sudoku, int row, int col) {
		
		//check if complete
		complete(sudoku);
		
		if(row >= dimension){
			row = 0;
			col++;
		}
		
		//if not empty
		if(!sudoku[row][col].equals("-")){
			//get to next node
			return solveNormal(sudoku, row+1, col);
		}
		
		for(int i = 0; i <= dimension; i++){
			//can place there
			
			if(validMove(sudoku, row, col, Integer.toString(i))){
				sudoku[row][col] = Integer.toString(i);
				//if solved
				if(solveNormal(sudoku, row + 1, col)){
					return true;
				}
			}
		}
		//go back because we messed ups
		sudoku[row][col] = "-";
		
		return false;
	}
	
	public static boolean complete(String[][] sudoku){
		//check for non assigned spaces
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku.length; col++) {
				if(sudoku[row][col].equals("-")){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean validMove(String[][] sudoku, int row, int col, String value){
		
		//row check
		for (int i = 0; i < dimension; i++) {
			   if (sudoku[row][i].equals(value)){
				    return false;
			   }
		}
		
		//check col
		for (int j = 0; j < dimension; j++) {
			   if (sudoku[j][col].equals(value)){
				    return false;
			   }
		}
		
		//not working????
		//check grid
		int row1 = (row / box) * box;
		int col1 = (col / box) * box;
		for (int  r = row1; r < dimension; r++) {
			for(int c = col1; c < dimension; c++){
			   if (sudoku[r][c].equals(value)){
				    return false;
			   }
			}
		}
		//choice works!
		//yay!!!!
		return true;
	}

	//next cell method
	
	public static void solveSudoku(int row, int col){
		
	}
	
	public static void sudokuVariable(){
		//sets all pissible moves for all the spaces
		int value;
		for(int i = 0; i < dimension; i++){
			domain.add(i);
		}
		value = 1;
	}

	
	public static int getVariable(int blah){
		int min = dimension + 1;
		int which = -1;
		for (int i = 0; i < dimension; i++){
			
		}
		
		
		return 0;
	}
}
