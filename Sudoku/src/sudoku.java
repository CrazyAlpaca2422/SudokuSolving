import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class sudoku {

	/**
	 * Holds the value for the big dimension
	 */
	public static int dimension;

	/**
	 * dimension for box
	 */
	public static int box;

	/**
	 * holds the solution
	 */
	public static String[][] sudokuPuzzle;

	/**
	 * all possible values a spot can be
	 */
	public static ArrayList<String> values = new ArrayList<String>();

	/**
	 * contains locations of all blank spaces in string format: "rowcol"
	 */
	public static ArrayList<String> blankSpaces = new ArrayList<String>();

	/**
	 * possible numbers each spot can have
	 */
	public static String[][] possibleMoves;

	/**
	 * all possible boards
	 */
	public static Set<String[][]> boards;

	/**
	 * holds all of the domains
	 */
	public static Set<String> domain = new HashSet<String>();

	/**
	 * This program solves a sudoku puzzle
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File sudoku = new File(args[0]);
		
		long startTime = System.nanoTime();

		//File sudoku = new File("test1");
		sudokuPuzzle = createPuzzle(sudoku);
		possibleMoves = new String[dimension][dimension];
		blankSpaces(sudokuPuzzle);
		createValues(dimension);
		sudokuSolver(sudokuPuzzle);
		printSudoku(sudokuPuzzle);

		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		//divide by 1000000 to get milliseconds.
		System.out.println(duration/1000000);
	}

	/**
	 * load all possible values into the arrayList
	 * 
	 * @param dim
	 */
	public static void createValues(int dim) {
		for (int i = 1; i <= dimension; i++) {
			values.add(Integer.toString(i));
		}
	}

	/**
	 * parses in the values and creates the 2D Array for the sudoku
	 * 
	 * @param sud
	 * @return
	 * @throws FileNotFoundException
	 */
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
		//printSudoku(puzzle);
		//System.out.println();
		return puzzle;
	}

	/**
	 * prints the sudoku
	 * 
	 * @param sudoku
	 */
	public static void printSudoku(String[][] sudoku) {
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku.length; col++) {
				System.out.print(sudoku[row][col].toString());
			}
			System.out.println();
		}
	}

	/**
	 * creates a new copy of the game board
	 * 
	 * @param sudoku
	 * @return
	 */
	public static String[][] createNewCopyOfBoard(String[][] sudoku) {
		String[][] copy = new String[dimension][dimension];

		for (int row = 0; row < copy.length; row++) {
			for (int col = 0; col < copy.length; col++) {
				copy[row][col] = sudoku[row][col];
			}
		}
		return copy;
	}

	/**
	 * assigns the locations of the blank spaces
	 * 
	 * @param sudoku
	 */
	public static void blankSpaces(String[][] sudoku) {
		String position = "";
		for (int row = 0; row < dimension; row++) {
			for (int col = 0; col < dimension; col++) {
				if (sudoku[row][col].equals("-")) {
					position = Integer.toString(row) + Integer.toString(col);
					blankSpaces.add(position);
				}
			}
		}
	}

	/**
	 * checks the row for singles, then adds possible moves for the row value
	 * 
	 * @param row
	 * @param sudoku
	 * @return
	 */
	public static String rowCheck(int row, String[][] sudoku) {
		// holds all of the nums currently in the row
		ArrayList<String> nums = new ArrayList<String>();
		String ret = "";
		int pos = 0;
		for (int col = 0; col < dimension; col++) {
			if (!sudoku[row][col].contains("-")) {
				nums.add(sudoku[row][col]);
			} else {
				pos = col;
			}
		}
		int numval = values.size() - nums.size();
		// means not full
		if (numval > 0) {
			for (int col1 = 0; col1 < dimension; col1++) {
				if (numval == 1) {
					if (!nums.contains(values.get(col1))) {
						sudokuPuzzle[row][pos] = values.get(col1);
						return ret;
					}
				} else {
					if (!nums.contains(values.get(col1))) {
						// numsReturn.add(values.get(col));
						ret += values.get(col1);
						//System.out.println(ret);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * checks the col for singles, then adds possible moves for the col value
	 * 
	 * @param col
	 * @param sudoku
	 * @return
	 */
	public static String colCheck(int col, String[][] sudoku) {
		// holds all of the nums currently in the row
		ArrayList<String> nums = new ArrayList<String>();
		String ret = "";
		int pos = 0;
		for (int row = 0; row < dimension; row++) {
			if (!sudoku[row][col].contains("-")) {
				nums.add(sudoku[row][col]);
			} else {
				pos = row;
			}
		}
		int numval = values.size() - nums.size();
		// means not full
		if (numval > 0) {
			for (int row = 0; row < dimension; row++) {
				if (numval == 1) {
					if (!nums.contains(values.get(row))) {
						sudokuPuzzle[pos][col] = values.get(row);
						return ret;
					}
				} else {
					if (!nums.contains(values.get(row))) {
						ret += values.get(row);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * checks the box for singles, then adds possible moves for the box value
	 * 
	 * @param row
	 * @param col
	 * @param sudoku
	 * @return
	 */
	public static String boxCheck(int row, int col, String[][] sudoku) {
		ArrayList<String> nums = new ArrayList<String>();
		String ret = "";
		int posr = 0;
		int posc = 0;
		row = (row / box) * box;
		col = (col / box) * box;
		for (int r = 0; r < box; r++) {
			for (int c = 0; c < box; c++) {
				if (!sudoku[row][col].contains("-")) {
					nums.add(sudoku[row][col]);
				} else {
					posr = row;
					posc = col;
				}
			}
		}
		int numval = values.size() - nums.size();
		// means not full
		if (numval > 0) {
			for (int r = 0; r < dimension; r++) {
				if (numval == 1) {
					if (!nums.contains(values.get(row))) {
						sudokuPuzzle[posr][posc] = values.get(row);
						return ret;
					}
				} else {
					if (!nums.contains(values.get(row))) {
						ret += values.get(row);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Checks for moves
	 * 
	 * @param sudoku
	 */
	public static void sudokuSolver(String[][] sudoku) {
		// remove nums from domains
		checkForSingles(sudoku);
		if (complete(sudokuPuzzle)) {
			return;
		}
	}

	/**
	 * checks if board is complete
	 * 
	 * @param sudoku
	 * @return
	 */
	public static boolean complete(String[][] sudoku) {
		// check for non assigned spaces
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku.length; col++) {
				if (sudoku[row][col].equals("-")) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * check for singles
	 * 
	 * @param sudoku
	 */
	public static void checkForSingles(String[][] sudoku) {
		String rowValue = "";
		String colValue = "";
		String boxValue = "";
		String allMoves;
		int blankRow = 0;
		int blankCol = 0;

		for (int i = 0; i < blankSpaces.size(); i++) {
			// check row values
			blankRow = Integer.parseInt(blankSpaces.get(i).substring(0, 1));
			// System.out.println(blankRow);
			blankCol = Integer.parseInt(blankSpaces.get(i).substring(1, 2));
			//System.out.println(blankCol);
			rowValue += rowCheck(blankRow, sudoku);
			// set if only 1
			if (rowValue.length() == 1) {
				sudokuPuzzle[blankRow][blankCol] = rowValue;
			}
			// check col values
			colValue += colCheck(blankCol, sudoku);
			// set if only 1
			if (colValue.length() == 1) {
				sudokuPuzzle[blankRow][blankCol] = colValue;
			}
			// check box values
			boxValue += boxCheck(blankRow, blankCol, sudoku);
			// set if only 1
			if (boxValue.length() == 1) {
				sudokuPuzzle[blankRow][blankCol] = boxValue;
			}

			// create the moves string
			// seperate method
			possibleMoves[blankRow][blankCol] = rowValue + colValue + boxValue;
		}
	}

	/**
	 * sets all possible domain values
	 */
	public static void setDomain() {
		// hold temp domain
		ArrayList<String> nums = new ArrayList<String>();
		int count = 0;
		// set temp array
		for (int num = 1; num <= dimension; num++) {
			nums.add(Integer.toString(num));
			count++;
		}
		// set row domain
		for (int row = 0; row < dimension; row++) {
			for (int col = 0; col < dimension; col++) {
				// check row for singles
				rowCheck(row, sudokuPuzzle);
				// temp
				String temp = sudokuPuzzle[row][col];
				// if temp not set
				if (temp.equals("-")) {
					for (int i = 0; i < dimension; i++) {
						// if assigned spot
						if (!sudokuPuzzle[row][i].equals("-")) {
							String blah = sudokuPuzzle[row][i];
							nums.remove(blah);
							// System.out.print(blah);
						}
					}
				}

			}
		}
		// set col domain
		for (int row = 0; row < dimension; row++) {
			for (int col = 0; col < dimension; col++) {
				colCheck(col, sudokuPuzzle);
				String temp = sudokuPuzzle[row][col];
				// if temp not set
				if (temp.equals("-")) {
					for (int i = 0; i < dimension; i++) {
						// if assigned spot
						if (!sudokuPuzzle[i][col].equals("-")) {
							String blah = sudokuPuzzle[i][col];
							nums.remove(blah);
							// System.out.println(blah);
						}
					}
				}
			}
		}
		String dom = "";
		// set to string
		for (int k = 0; k < nums.size(); k++) {
			dom += nums.get(k);
		}
		domain.add(dom);
	}

	/**
	 * This solves the puzzle uising MRV
	 * @param sudoku
	 */
	public static void solveMRV(String[][] sudoku) {

		boolean d = false;
		// check if complete
		d = complete(sudoku);
		while (!d) {
			int count = 0;
			String pos = "";
			// will look for smallest value in array
			for (int r = 0; r < dimension; r++) {
				for (int c = 0; c < dimension; c++) {
					if (sudokuPuzzle[r][c].equals("-")) {
						// pos = domain.remove();
						count++;
					}
				}
			}
		}
		// domain is smallest
	}
}
