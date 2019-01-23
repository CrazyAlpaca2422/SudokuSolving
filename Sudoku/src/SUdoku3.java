import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SUdoku3 {
	// big dimension
	public static int dimension;
	// small dimension
	public static int box;
	// the game board
	public static String[][] sudokuPuzzle;
	// holds all of the domains
	public static Set<String> domain = new HashSet<String>();
	//list of possible values a space can be
	public static ArrayList<String> values = new ArrayList<String>();
	// are we finished yet?
	public static boolean done = false;

	
	public static void main(String[] args) throws FileNotFoundException {
		// File sudoku = new File(args[0]);
		File sudoku = new File("test1");
		// create the puzzle
		sudokuPuzzle = createPuzzle(sudoku);
		setDomain();
		
		// solveNormal boolean
		// start with 0, 0 so it will go through whole puzzle
		// hopefully
		solveNormal(sudokuPuzzle);

		// solveMRV boolean
		solveMRV(sudokuPuzzle);

		printSudoku(sudokuPuzzle);
		
	}

	public static void printSudoku(String[][] sudoku) {
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku.length; col++) {
				System.out.print(sudoku[row][col].toString());
			}
			System.out.println();
		}
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

	public static void setDomain() {
		//hold temp domain
		ArrayList<String> nums = new ArrayList<String>();
		int count = 0;
		//set temp array
		for(int num = 1; num <= dimension; num++){
			nums.add(Integer.toString(num));
			count++;
		}
		
		//set row domain
		for(int row = 0; row < dimension; row++){
			for(int col = 0; col < dimension; col++){
				//check row for singles
				rowCheck(row, sudokuPuzzle);
				//temp
				String temp = sudokuPuzzle[row][col];
				//if temp not set
				if(temp.equals("-")){
					for(int i = 0; i < dimension; i++){
						//if assigned spot
						if(!sudokuPuzzle[row][i].equals("-")){
							String blah = sudokuPuzzle[row][i];
							nums.remove(blah);
							//System.out.print(blah);
						}
					}
				}

			}
		}
		
		//set col domain
		for(int row = 0; row < dimension; row++){
			for(int col = 0; col < dimension; col++){
				colCheck(col, sudokuPuzzle);
				String temp = sudokuPuzzle[row][col];
				//if temp not set
				if(temp.equals("-")){
					for(int i = 0; i < dimension; i++){
						//if assigned spot
						if(!sudokuPuzzle[i][col].equals("-")){
							String blah = sudokuPuzzle[i][col];
							nums.remove(blah);
							//System.out.println(blah);
						}
					}
				}
			}
		}
		
		String dom = "";
		//set to string
		for(int k = 0; k < nums.size(); k++){
			dom += nums.get(k);
		}
		
		domain.add(dom);
		
		//set box domain
		//rowM
		//rowm
		//colM
		//colm
		
		//loop through lenght of square
		//go to next
		//
		
	}

	public static void solveNormal(String[][] sudoku) {
		// check if complete
		
		complete(sudoku);
		
		//will look for smallest value in array
		for(int r = 0; r < dimension; r++){
			for(int c = 0; c < dimension; c++){
				
			}
		}

//		if (row >= dimension) {
//			row = 0;
//			col++;
//		}
//
//		// if not empty
//		if (!sudoku[row][col].equals("-")) {
//			// get to next node
//			return solveNormal(sudoku, row + 1, col);
//		}
//
//		for (int i = 0; i <= dimension; i++) {
//			// can place there
//
//			if (validMove(sudoku, row, col, Integer.toString(i))) {
//				sudoku[row][col] = Integer.toString(i);
//				// if solved
//				if (solveNormal(sudoku, row + 1, col)) {
//					return true;
//				}
//			}
//		}
//		// go back because we messed ups
//		sudoku[row][col] = "-";

	}

	public static void solveMRV(String[][] sudoku) {
		
		boolean d = false;
		// check if complete
		d = complete(sudoku);
		while(!d){
			int count = 0;
			String pos = "";
			//will look for smallest value in array
			for(int r = 0; r < dimension; r++){
				for(int c = 0; c < dimension; c++){
					if(sudokuPuzzle[r][c].equals("-")){
						//pos = domain.remove();
						count++;
					}
				}
			}
		}
		//domain is smallest
	}
	
	public static String mrvVar(Set<Integer> dom){
		
		return "blah";
	}

	public static boolean complete(String[][] sudoku) {
		// check for non assigned spaces
		for (int row = 0; row < sudoku.length; row++) {
			for (int col = 0; col < sudoku.length; col++) {
				if (sudoku[row][col].equals("-")) {
					return true;
				}
			}
		}
		return false;
	}

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
						System.out.println(ret);
					}
				}
			}
		}
		return ret;
	}

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
	
	public static boolean validMove(String[][] sudoku, int row, int col, String value) {
		// row check
		for (int i = 0; i < dimension; i++) {
			if (sudoku[row][i].equals(value)) {
				return false;
			}
		}

		// check col
		for (int j = 0; j < dimension; j++) {
			if (sudoku[j][col].equals(value)) {
				return false;
			}
		}

		// not working????
		// check grid
		int row1 = (row / box) * box;
		int col1 = (col / box) * box;
		for (int r = row1; r < dimension; r++) {
			for (int c = col1; c < dimension; c++) {
				if (sudoku[r][c].equals(value)) {
					return false;
				}
			}
		}
		// choice works!
		// yay!!!!
		return true;
	}
	
//	public static void sudokuSolve(String[][] sudoku){
//		//if num of domain is zero
//		if(/*all values set*/){
//			//print
//		} else{
//			//get variable (MRV)
//			//iterate through domain and see if possible
//				//create copy
//				//inset value you are trying
//				//forward checking
//					//update domain (row, col, box)
//					
//			
//		}
//		
//	}

}
