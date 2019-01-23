package CSE486;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolveSudoku {

	// private static ArrayList<Integer> domain = new ArrayList<Integer>();
	private static int dimensions = 0;
	private static int subGrids = 0;
	private static Node[][] puzzle;
	private static boolean solved = false;

	// added for MRV heuristic
	private static HashSet<Node> constrainedNodes = new HashSet<Node>(200);

	public static void main(String[] args) {
		// for capturing time of program
		long startTime = System.nanoTime();

		File sudFile = new File(args[0]);

		// parse the file and get the sudoku in a matrix
		Node[][] puzzle = parseFile(sudFile);

		// solve the puzzle with recursive backtracking
		// solved = solveWithoutMRV(puzzle, 0, 0);

		// solve the puzzle with recursive backtracking AND MRV
		solved = solveWithMRV(puzzle, 0, 0);

		// print completed puzzle
		if (solved == true) {
			printArray(puzzle);
		}

		// capture and print the runtime length in milliseconds
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		System.out.println("Program finished in: " + duration + " milliseconds.\n");
	}

	// Here the file is parsed, and the Node objects are
	// created and placed into the puzzle global variable
	private static Node[][] parseFile(File sudFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(sudFile))) {

			String currentLine;
			String firstLine;

			// Grab the dimensions of the maze and create the array.
			firstLine = br.readLine();
			dimensions = Integer.parseInt(firstLine.substring(0, 1));
			subGrids = Integer.parseInt(firstLine.substring(2, 3));
			puzzle = new Node[dimensions][dimensions];

			// Note: Removed domain set present in the N-Queens problem
			// Because of how basic the domain of a sudoku problem is
			// (1-dimensions) and because checking such values fits so perfectly
			// in a for loop.
			// for (int i = 1; i <= dimensions; i++) {
			// domain.add(i);
			// }

			int r = 0;
			Node temp;
			while ((currentLine = br.readLine()) != null) {
				for (int c = 0; c < dimensions; c++) {
					if (currentLine.charAt(c) == '-') {
						// let's replace the dashes with zeroes for comparison's
						// sake, and for variable typing's sake
						temp = new Node(0, new HashSet<Integer>(), r, c);
						puzzle[r][c] = temp;
						// Keep track of these "unsolved" nodes
						constrainedNodes.add(temp);

					} else {
						temp = new Node(Character.getNumericValue(currentLine.charAt(c)), new HashSet<Integer>(), r, c);
						puzzle[r][c] = temp;

					}
				}
				r++; // next row for next pass through loop
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		printArray(puzzle);

		return puzzle;

	}

	/*
	 * This is the original code I wrote (see: report). It solves sudoku
	 * recursively. I didn't attempt to track any sort of constraints that would
	 * help with Node selection (via the MRV heuristic). This is simple brute
	 * force at its finest.
	 */
	private static boolean solveWithoutMRV(Node[][] puzzle, int x, int y) {
		// reset x if it hits 9 or 4, and increment/check y to see if the
		// overall looping is complete.
		if (x == dimensions) {
			x = 0;
			y++;
			if (y == dimensions)
				// return true if y equals 4 or 9, as this means the
				// whole puzzle has been looped over and (hopefully) solved
				return true;
		}
		
		// Get current node
		Node currentNode = puzzle[x][y];
		// If the current node's value ISN'T empty
		if (currentNode.getValue() != 0)
			// Check the next Node over
			return solveWithoutMRV(puzzle, x + 1, y);

		// for each value in the domain of 1-4 or 1-9
		for (int v = 1; v <= dimensions; v++) {
			// if the value is viable
			if (viable(puzzle, x, y, v)) {
				puzzle[x][y].setValue(v);
				if (solveWithoutMRV(puzzle, x + 1, y))
					return true;
			}
		}
		// Time to backtrack! Reset val to 0.
		currentNode.setValue(0);
		return false;

	}

	private static boolean viable(Node[][] puzzle, int x, int y, int v) {
		// Check the columns of the puzzle for a matching value
		for (int c = 0; c < dimensions; c++)
			if (v == puzzle[x][c].getValue())
				return false;

		// Check the rows of the puzzle for a matching value
		for (int r = 0; r < dimensions; r++)
			if (v == puzzle[r][y].getValue())
				return false;

		// Now check the SUBGRID for a matching value
		int rOffset = (x / subGrids) * subGrids;
		int cOffset = (y / subGrids) * subGrids;
		for (int b = 0; b < subGrids; b++)
			for (int b2 = 0; b2 < subGrids; b2++)
				if (v == puzzle[rOffset + b][cOffset + b2].getValue())
					return false;

		// no val matches mean the choice is legal
		return true;

	}

	/////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////

	/// EVERYTHING below this point was added after I had working recursive
	/// solution (including the Node class). I added these things
	/// in an attempt to add MRV heuristic that would apply some actual
	/// logic in choosing a node rather than just
	/// incrementing the puzzle's row by 1 and grabbing the next Node up.

	private static boolean solveWithMRV(Node[][] puzzle, int x, int y) {
		// Use constraint lists to pick a node by MRV heuristic
		Node currentNode = mrvFindNode();

		// When there are no constrainedNodes left, mrvFindNode returns
		// a null -- this marks us reaching the end of the constraints
		if (currentNode == null) {
			return true;
		}

		// for each value in the domain of 1-4 or 1-9
		for (int v = 1; v <= dimensions; v++) {
			// Does this node have this domain value in its constraints list?
			// If it does not, proceed.
			if (!currentNode.getConstraints().contains(v)) {

				// Needed to add row and col variables for this reason:
				// No longer just looping over the whole maze and using
				// its indices as my row and col
				if (viable(puzzle, currentNode.getRow(), currentNode.getColumn(), v)) {

					// temporarily change curNode's value to v
					int prevVal = currentNode.getValue();
					constrainedNodes.remove(currentNode);
					currentNode.setValue(v);
					// this helper method will review and amend constraints
					// while also manipulating the puzzle variable
					List<Node> changedNodes = addViableConstraints(currentNode, v);
					// Now that all the constraint work is done, recurse
					if (solveWithMRV(puzzle, x, y)) {
						return true;
					}

					// replace curNode's value with its original value
					// and reset all values so we can backtrack
					currentNode.setValue(prevVal);
					constrainedNodes.add(currentNode);
					// Reset the constraints of manipulated Nodes too
					for (Node node : changedNodes) {
						node.getConstraints().remove(v);

					}
				}
			}
		}
		return false;
	}

	private static Node mrvFindNode() {

		// When there are no constrainedNodes left, mrvFindNode returns
		// a null -- this marks us reaching the end of the constraints
		if (constrainedNodes.size() == 0) {
			return null;
		}

		// Was able to use this by 'implementing Comparable'
		// in the Node class
		// return max value node of constrained nodes
		return Collections.max(constrainedNodes);

	}

	// Helper to add constraints to a given Node
	static private ArrayList<Node> addViableConstraints(Node node, int v) {

		// for building list of neighbors
		ArrayList<Node> changedNodes = new ArrayList<Node>(dimensions);

		// Check columns, similar to logic in viable()
		for (int i = 0; i < dimensions; i++) {
			Node cNode = puzzle[i][node.getColumn()];
			if (cNode.getValue() == 0 && node.getRow() != i && !cNode.getConstraints().contains(v)) {
				cNode.getConstraints().add(v);
				if (!changedNodes.contains(cNode))
					changedNodes.add(cNode);
			}
			Node rNode = puzzle[node.getRow()][i];
			if (rNode.getValue() == 0 && node.getColumn() != i && !rNode.getConstraints().contains(v)) {
				rNode.getConstraints().add(v);
				if (!changedNodes.contains(rNode))
					changedNodes.add(rNode);
			}
		}
		// Now to manipulate the boxes/subGrids...
		int rOffset = (node.getRow() / subGrids) * subGrids;
		int cOffset = (node.getColumn() / subGrids) * subGrids;
		for (int x = rOffset; x < rOffset + subGrids; x++)
			for (int y = cOffset; y < cOffset + subGrids; y++) {
				if (puzzle[x][y].getValue() != 0 || (x == node.getRow() && y == node.getColumn())) {
					// break; - this doesn't work as it exits the entire
					// function!
					continue;
				}

				Node boxNode = puzzle[x][y];
				if (!boxNode.getConstraints().contains(v)) {
					boxNode.getConstraints().add(v); // add value if value not
														// present
					if (!changedNodes.contains(boxNode)) {
						changedNodes.add(boxNode);
					}
				}
			}
		return changedNodes;
	}

	// Tester/helper for printing the maze
	private static void printArray(Node[][] puzzle) {
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[0].length; j++) {
				System.out.print(puzzle[i][j].getValue());
			}
			System.out.println();
		}
		System.out.println();

	}

}
