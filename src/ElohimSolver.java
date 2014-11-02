import java.util.ArrayList;
import java.util.Scanner;

import Pieces.*;

/**
 * ElohimSolver
 * 
 * solves a game called "Sigil of Elohim" and its tetris box puzzles.
 * the game was made by DevolverDigital. Available for PC/Mobile for free.
 * 
 * This code is easily modifiable so that you can:
 * 1) Have a box with "holes" (not tested).
 * 2) Have diverse Pieces (not tested).
 * 
 * This code was created for part A of the game.
 * 
 * Please note, "bruteforcing backtracking" may take a while to produce the solution.
 * Turning off System.printStuff makes things faster.
 * 
 * Note: Not tested for 4-block tetris pieces.
 * 
 * Note: It takes at most 20 minutes for 2-8 & 3-8 (for my computer at least)
 * 
 * Note: The order of the sorted pieces heavily affects the program.
 * Straight pieces followed by square pieces have a lower chance of leaving bubbles.
 * If they go first, the program is faster.
 * Maybe? Sorting by piece quantity is better?
 * 
 * Pruning ideas?
 * - bubbles that are not divisible by a certain number "like 4"
 * 
 * email - LK00100100@gmail.com
 * 
 * @author LK00100100
 * @version 4
 *
 */
public class ElohimSolver {

	private static long startTime;				//stop-watch for the program
	private static int smallestPieceSize;	//the smallest piece size (in terms of blocks). a "PieceT" has 4 blocks.

	public static void main(String[] args) {

		//read some standard input (aka the console).
		Scanner sc = new Scanner(System.in);

		ArrayList<Piece> pieceArray; 			//stores the pieces. (hopefully, you input them in piece order)
		PieceArrayBuilder pf = new PieceArrayBuilder();

		//for the box
		int rows;
		int cols;
		int[][] box;

		//get user input on piece quantity.
		//There is no error checking, cause let's face it... that's work.
		System.out.println("Programmer Note: There isn't any input error checking cause I am lazy");

		System.out.println("How many Straight Pieces: ");
		pf.addPieceStraight(sc.nextInt());
		System.out.println("How many Square Pieces: ");
		pf.addPieceSquare(sc.nextInt());
		System.out.println("How many L Pieces: ");
		pf.addPieceL(sc.nextInt());
		System.out.println("How many L Reversed Pieces: ");
		pf.addPieceLReversed(sc.nextInt());
		System.out.println("How many T Pieces: ");
		pf.addPieceT(sc.nextInt());
		System.out.println("How many Thunder Pieces: ");
		pf.addPieceThunder(sc.nextInt());
		System.out.println("How many Thunder Reversed Pieces: ");
		pf.addPieceThunderReversed(sc.nextInt());

		//sort pieces
		//TODO SORT
		pieceArray = pf.getArrayPieces();
		pf.printArray();

		smallestPieceSize = pf.getSmallestPiece();

		//get box size and make one.
		System.out.println("How many rows in the box: ");
		rows = sc.nextInt();
		System.out.println("How many columns in the box: ");
		cols = sc.nextInt();
		box = new int[rows][cols];

		//clears the box matrix (note, you can add holes after these loops)
		zeroIntMatrix(box);

		startTime = System.currentTimeMillis();

		findSolution(box, pieceArray);

		//print time duration of algorithm
		printStopwatchTime(startTime);

		System.out.println("No solution was found");

		sc.close();

	}

	private static void findSolution(int[][] currentBox, ArrayList<Piece> piecesRemaining){

		if(isSolution(piecesRemaining)){
			solutionFound(currentBox);
			System.exit(0);	//end program immediately.
		}
		else{//no solution, process recursively

			ArrayList<BubbleData> bubbleData = new ArrayList<BubbleData>();
			Piece currentPiece2;	//a piece that fits in a bubble (out of many bubbles)

			//go through all the remaining pieces.
			for(int i = 0; i < piecesRemaining.size(); i++){

				Piece currentPiece = piecesRemaining.get(i);

				//check if the previous piece is the same as current.
				//if so, just go to the next piece since we don't want to recalculate the same things.
				//note: assumed piecesRemaining is sorted.
				if(i > 0){
					if(currentPiece.equals(piecesRemaining.get(i - 1))){
						continue;
					}
				}

				boolean fittedOnce = false;	//if at least one piece's rotation cannot be placed anywhere, backtrack.
				
				//go through all rotations of this piece
				for(int j = 0; j < currentPiece.rotations; j++){

					//go through all rows of currentBox (except the last ones where the pieces can't fit)
					for(int row = 0; row < currentBox.length - currentPiece.row + 1; row++){

						//go through all columns of currentBox (except the last ones where the pieces can't fit)
						for(int col = 0; col < currentBox[0].length - currentPiece.col + 1; col++){

							//if we can fit the piece.
							if(fitPiece(currentBox, currentPiece, row, col)){
								Piece pieceRemoved = piecesRemaining.remove(i);

								/*
								System.out.println("currentBox");
								printIntMatrix(currentBox);
								System.out.println();
								System.out.println("currentPieceId: " + currentPiece.id);
								System.out.println();
								 */

								//if placing the piece caused a small bad "bubbles" of space, then don't do the recursion.
								//the remaining Bubbles WILL have a quantity of pieces from remainingPieces that'll fit in them.
								if(bubbleCheckInit(pieceRemoved, piecesRemaining, currentBox, row, col, smallestPieceSize, bubbleData) == false){
									fittedOnce = true;

									//for every good bubble, jam a good piece in it (recursively)
									for(int currentBubble = 0; currentBubble < bubbleData.size(); currentBubble++){
										/*
										System.out.println("after BCI bubbleData's size's: " + bubbleData.size());
										printRemainingPieces(piecesRemaining);
										System.out.println();
										*/
		
										for(int x = 0; x < piecesRemaining.size(); x++){

											currentPiece2 = piecesRemaining.get(x);
											//if it's the same as the last piece, continue
											if(x > 0){
												if(currentPiece2.equals(piecesRemaining.get(x - 1))){
													continue;
												}
											}

											//if it's the same piece type as the current bubble's pieceType
											//jam the piece in.
											if(currentPiece2.pieceType.equals(bubbleData.get(currentBubble).PieceType)){

												//rotate the piece so it fits.
												for(int r = 0; r < currentPiece2.rotations; r++){
													if(fitPiece(currentBox, currentPiece2, bubbleData.get(currentBubble).row, bubbleData.get(currentBubble).col))
														break;
													else
														currentPiece2.rotate();
												}

												//remove the bubbled pieces	
												bubbleData.get(currentBubble).position = x;
												bubbleData.get(currentBubble).thePieceFitted = piecesRemaining.remove(x);

												/*
												System.out.println("Fitting in this piece: ");
												printIntMatrix(bubbleData.get(currentBubble).thePieceFitted.space);
												System.out.println();
												*/
												break; //go to the next bubble

											}

										}


									}

									//update variables, recursively go deeper into the rabbit hole...
									findSolution(currentBox, piecesRemaining);

									//remove all of the pieces that filled the bubbles.
									for(int x = bubbleData.size() - 1; x >= 0; x--){
										currentPiece2 = bubbleData.get(x).thePieceFitted;

										removePiece(currentBox, currentPiece2, bubbleData.get(x).row, bubbleData.get(x).col);
										piecesRemaining.add(bubbleData.get(x).position, currentPiece2);
										bubbleData.remove(x);
									}


								} 

								//when we backtracked from some recursion,
								//remove the piece from the box,
								//and add it back to the pool of pieces
								removePiece(currentBox, currentPiece, row, col);
								piecesRemaining.add(i, pieceRemoved);

							}
							//else do nothing

						}

					}

					//rotate piece
					currentPiece.rotate();

				}//end - for rotations

				//if the current piece cannot fit at all (despite rotations) and without creating bad "bubbles" of small space.
				//then don't bother going through the remaining pieces.
				if(fittedOnce == false){
					break;	//break out of - for(go through all the remaining pieces.);
				}

			}

		}//end else(not a solution)

	}

	/**
	 * bubbleCheckInit
	 * 
	 * Ex: placing a T in a 2x3 box creates two "bubbles" of size 1.
	 * If the smallest remaining piece is of size > 1, then return true, there is a bad bubble.
	 * 
	 * 0+0	0 = bubble
	 * +++	+ = piece
	 * 
	 * Then it checks the remaining bubbles of minSize and determines if there are pieces from piecesRemaining that can fit in all bubbles.
	 * 
	 * Ex: If there's 2 PieceL bubbles but only 1 PieceL in remainingPieces, return true (bad bubbles)
	 * If there's 2 PieceL bubbles but 3 PieceL in remainingPieces, return false
	 * 
	 * @param lastPiece - the Piece just placed
	 * @param piecesRemaining - the list of Pieces remaining
	 * @param currentBox - the currentBox state
	 * @param rowPlaced - top row of Piece placed
	 * @param colPlaced - left col of Piece placed
	 * @param minSize - the minimum Size of the bubble has to be.
	 * @return boolean - true if a bubble smaller than minSize exists or if we don't have enough of the right Pieces in remainingPieces for the bubbles.
	 */
	private static boolean bubbleCheckInit(Piece lastPiece, ArrayList<Piece> piecesRemaining, int[][] currentBox, int rowPlaced, int colPlaced, int minSize, ArrayList<BubbleData> bubbleData){

		//keeps track of the "bubble numbers" (the size the space can fit. limited to minSize)
		int[][] bubbleBox = new int[currentBox.length][currentBox[0].length];

		//initialize the bubbleBox
		//the lastPiece is centered.
		//-1 = occupied
		//0 = unchecked
		//>0 = checked, with biggest bubble size found (limited by minSize).
		for(int row = 0; row < bubbleBox.length; row++){
			for(int col = 0; col < bubbleBox[0].length; col++){
				//if the spot is occupied.
				if(currentBox[row][col] != 0 ){
					//fill in bubbleBox as occupied.
					bubbleBox[row][col] = -1 ;
				}
				else{
					//fill in bubbleBox as unoccupied (never done recursion).
					bubbleBox[row][col] = 0;
				}

			}
		}

		//top left corner of the search space (used in for loop below)
		int row = rowPlaced - 1;
		int col = colPlaced - 1;

		int retVal;

		//go through every space ONLY around the piece.
		for(int i = 0; i < lastPiece.row + 2; i++){
			for(int j = 0; j < lastPiece.col + 2; j++){

				//valid range check
				if(row + i < bubbleBox.length && col + j < bubbleBox[0].length){
					if(row + i >= 0 && col + j >= 0){
						//if this space is unoccupied and hasn't been checked.
						if(bubbleBox[row + i][col + j] == 0){

							if((retVal = bubbleCheck(bubbleBox, row + i, col + j, 0, minSize)) < minSize){

								/*
								System.out.println("Small Bubble Found: " + retVal);
								printIntMatrix(bubbleBox);
								System.out.println();
								*/
								return true;
							}
							bubbleBox[row + i][col + j] = retVal;

							//set every positive value to the retVal
							for(int x = 0; x < bubbleBox.length; x++){
								for(int y = 0; y < bubbleBox[0].length; y++){
									//if the spot is occupied.
									if(bubbleBox[x][y] > 0 ){
										bubbleBox[x][y] = retVal;
									}

								}
							}

						}
					}
				}

			}
		}	

		//part two of the bubble Check.
		//see if any of the remaining pieces types can fit in the min-size bubbles.

		//this will keep track of what part of the bubbleBox has been checked
		//cuts down on recursion.
		boolean[][] isChecked = new boolean[bubbleBox.length][bubbleBox[0].length];
		for(int x = 0; x < isChecked.length; x++){
			for(int y = 0; y < isChecked[0].length; y++){
				isChecked[x][y] = false;
			}
		}

		//this will fill out what the piece looks like.
		int[][] pieceSpace = new int[bubbleBox.length][bubbleBox[0].length];
		int[][] pieceSpaceTrimmed;	//pieceSpace without all of the useless walls.
		zeroIntMatrix(pieceSpace);

		//clone the array so you don't mess with the original
		ArrayList<Piece> piecesRemainingClone = new ArrayList<Piece>();
		for(int i = 0; i < piecesRemaining.size(); i++){
			piecesRemainingClone.add(piecesRemaining.get(i));
		}
		
		//go through all spaces in bubbleBox
		for(int x = 0; x < bubbleBox.length; x++){
			for(int y = 0; y < bubbleBox[0].length; y++){
				//if this box has bubble of minsize
				if(isChecked[x][y] == false && bubbleBox[x][y] == minSize){
					//get the piece
					bubbleToPiece(bubbleBox, isChecked, pieceSpace, x, y, minSize);

					BubbleData someBubble = new BubbleData();
					//trim the pieceSpace of all of the leading white space.
					//and update BubbleData's upper left corner coordinates
					pieceSpaceTrimmed = trimIntMatrix(pieceSpace, someBubble);

					//does this piece exist in the remaining pieces?
					//if it doesn't return true (bad bubble).
					if((retVal = doesPieceExist(piecesRemainingClone, pieceSpaceTrimmed, someBubble)) != -1){

						//fill out bubbleData
						bubbleData.add(someBubble);
						
						/*
						zeroIntMatrix(pieceSpace);
						System.out.println("Piece exists:");
						printIntMatrix(pieceSpaceTrimmed);
						printRemainingPieces(piecesRemaining);
						*/
						
						//remove this from the pool of available pieces
						//so we don't have 3 PieceL bubbles but 1 PieceL remaining seen as "good"
						piecesRemainingClone.remove(retVal);
						
					}
					else{
						/*
						System.out.println("Piece does not exist:");
						printIntMatrix(bubbleBox);
						System.out.println("Piece:");
						printIntMatrix(pieceSpaceTrimmed);
						System.out.println();
						printRemainingPieces(piecesRemaining);
						*/

						//empty out bubbleData since they're all bad now.
						for(int i = bubbleData.size() - 1; i >= 0 ; i--){
							bubbleData.remove(i);
						}

						return true;	//there is a bad bubble.
					}


				}
				else{
					isChecked[x][y] = true;
				}
			}
		}

		return false;
	}

	/**
	 * checks to see if thePiece is in the list of remainingPieces
	 * 
	 * @param remainingPieces - list of pieces
	 * @param thePiece - the piece to check
	 * @return int - the position in remainingPieces where the piece is.
	 */
	private static int doesPieceExist(ArrayList<Piece> remainingPieces, int[][] pieceSpace, BubbleData bubbleData){

		for(int i = 0; i < remainingPieces.size(); i++){
			Piece currentPiece = remainingPieces.get(i);

			//if this is the same as the last piece, continue
			if(i > 0){
				if(currentPiece.equals(remainingPieces.get(i - 1))){
					continue;
				}
			}

			//compare the two.
			//a piece can have only a max of currentPiece rotations
			for(int r = 0; r < currentPiece.rotations; r++){
				if(currentPiece.isSpaceEquals(pieceSpace)){
					bubbleData.PieceType = currentPiece.pieceType;
					return i;
				}
				else{
					pieceSpace = Piece.rotate(pieceSpace);
				}

			}

		}

		return -1;
	}

	/**
	 * this finds bubbles and alters the bubbleBox
	 * 
	 * @param bubbleBox - keeps track of the size of the bubbles.
	 * @param row - row to check for bubbleBox
	 * @param col - col to check for bubbleBox
	 * @param currentSize - current bubble size
	 * @param minSize - the bubble has to be at least this size. this is used to stop recursion.
	 * @return
	 */
	private static int bubbleCheck(int[][] bubbleBox, int row, int col, int currentSize, int minSize){

		//check the bubbleBox for a value, so we don't have to use too much recursion.
		int retVal;

		//check valid range for bubbleBox
		if(row < 0 || col < 0) 
			return currentSize;
		if(row >= bubbleBox.length || col >= bubbleBox[0].length) 
			return currentSize;

		//if this bubbleBox space is occupied by a piece or void, just go back.
		if(bubbleBox[row][col] == -1)
			return currentSize;
		//if this space has a bubble size number, return the bigger number;
		else if(bubbleBox[row][col] > 0){
			if(bubbleBox[row][col] > currentSize)
				return bubbleBox[row][col];
			else
				return currentSize;
		}

		//else no bubble number, fill it in.
		bubbleBox[row][col] = ++currentSize;

		//if we've reached the minSize + 1 limit, just return. no need to keep going deeper.
		// minSize + 1 says there could more than the minimum.
		if(currentSize == minSize + 1)
			return currentSize;

		//check left (first)
		//if the square we checked has a larger bubble number, then the current square
		//has the larger bubble number.
		retVal = bubbleCheck(bubbleBox, row, col - 1, currentSize, minSize);
		if(retVal > currentSize){
			currentSize = retVal;
			bubbleBox[row][col] = currentSize;

			if(currentSize == minSize + 1)
				return currentSize;
		}

		//check up (second)
		retVal = bubbleCheck(bubbleBox, row - 1, col, currentSize, minSize);
		if(retVal > currentSize){
			currentSize = retVal;
			bubbleBox[row][col] = currentSize;

			if(currentSize == minSize + 1)
				return currentSize;
		}

		//check right
		retVal = bubbleCheck(bubbleBox, row, col + 1, currentSize, minSize);
		if(retVal > currentSize){
			currentSize = retVal;
			bubbleBox[row][col] = currentSize;

			if(currentSize == minSize + 1)
				return currentSize;
		}

		//check down
		retVal = bubbleCheck(bubbleBox, row + 1, col, currentSize, minSize);
		if(retVal > currentSize){
			currentSize = retVal;
			bubbleBox[row][col] = currentSize;

			if(currentSize == minSize + 1)
				return currentSize;
		}

		return currentSize;
	}


	/**
	 * translates a bubble to a Piece's space (int[][]) recursively
	 * modifies isSearched & pieceMatrix
	 * 
	 * @param bubbleBox - what bubbles are where (we only care about the bubbles of minSize)
	 * @param isSearched - what parts have been searched. cuts down on recursion.
	 * @param pieceMatrix - the piece we are trying to pull from this one bubble.
	 * @param row - current Row
	 * @param col - current column
	 * @param minSize - the target bubble size we care about.
	 */
	private static void bubbleToPiece(int[][] bubbleBox, boolean[][] isChecked, int[][] pieceMatrix, int row, int col, int minSize){

		//check for valid range
		if(row < 0 || col < 0)
			return;
		if(row >= bubbleBox.length || col >= bubbleBox[0].length)
			return;

		//we checked this box.
		isChecked[row][col] = true;

		//if this is not part of the bubble, go back
		if(bubbleBox[row][col] != minSize){
			return;
		}
		//else is part of the bubble.

		pieceMatrix[row][col] = minSize;

		//check up

		if(row - 1 >= 0 && isChecked[row - 1][col] == false)
			bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row - 1, col, minSize);
		//check down
		if(row + 1 < bubbleBox.length && isChecked[row + 1][col] == false)
			bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row + 1, col, minSize);	
		//check left
		if(col - 1 >= 0 && isChecked[row][col - 1] == false)
			bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row, col - 1, minSize);
		//check right
		if(col + 1 < bubbleBox[0].length && isChecked[row][col + 1] == false)
			bubbleToPiece(bubbleBox, isChecked, pieceMatrix, row, col + 1, minSize);

		return;
	}

	/**
	 * trims the walls of the matrix that are filled with 0's.
	 * fills out topRow and leftCol of the someBubble
	 * 
	 * 000
	 * 010 becomes [1]
	 * 000
	 * 
	 * @param theMatrix
	 * @param someBubble - bubbleData's row and col to fill out
	 * @return another Matrix which is trimmed.
	 */
	private static int[][] trimIntMatrix(int[][] theMatrix, BubbleData someBubble){

		int topRow = 0;
		int bottomRow = 0;
		int leftCol = 0;
		int rightCol = 0;

		boolean hasNothing = true;

		//find the top of the matrix with stuff in it
		for(int i = 0; i < theMatrix.length; i++){
			for(int j = 0; j < theMatrix[0].length; j++){
				if(theMatrix[i][j] > 0){
					hasNothing = false;
					topRow = i;
				}
			}	

			if(hasNothing == false)
				break;

		}
		if(hasNothing == true)
			return null;

		//find the bottom of the matrix with stuff in it
		hasNothing = true;
		for(int i = theMatrix.length - 1; i >= 0 ; i--){
			for(int j = 0; j < theMatrix[0].length; j++){
				if(theMatrix[i][j] > 0){
					hasNothing = false;
					bottomRow = i;
				}
			}	

			if(hasNothing == false)
				break;

		}
		if(hasNothing == true)
			return null;

		//find the left of the matrix with stuff in it
		hasNothing = true;
		for(int j = 0; j < theMatrix[0].length; j++){
			for(int i = 0; i < theMatrix.length; i++){
				if(theMatrix[i][j] > 0){
					hasNothing = false;
					leftCol = j;
				}

			}	

			if(hasNothing == false)
				break;

		}
		if(hasNothing == true)
			return null;

		//find the right of the matrix with stuff in it
		hasNothing = true;
		for(int j = theMatrix[0].length - 1; j >= 0 ; j--){
			for(int i = 0; i < theMatrix.length; i++){
				if(theMatrix[i][j] > 0){
					hasNothing = false;
					rightCol = j;
				}
			}	

			if(hasNothing == false)
				break;

		}
		if(hasNothing == true)
			return null;

		//fill out the bubbleData
		someBubble.row = topRow;
		someBubble.col = leftCol;

		//copy theMatrix into the smaller trimmed one.
		int trimmed[][] = new int[(bottomRow - topRow) + 1][(rightCol - leftCol) + 1];

		for(int i = 0; i < trimmed.length; i++){
			for(int j = 0; j < trimmed[0].length;j++){
				trimmed[i][j] = theMatrix[topRow + i][leftCol + j];
			}
		}

		return trimmed;
	}

	/**
	 * fitPiece
	 * 
	 * attempts to jam in a piece at the set coordinates. returns true if fits, false if not.
	 * 
	 * @param currentBox - the box that will be modified with the fit (if it fits) piece.
	 * @param thePiece - the piece to attempt to fit
	 * @param row of the box.
	 * @param column of the box.
	 * @return boolean - success or failure of piece fitting
	 */
	private static boolean fitPiece(int[][] currentBox, Piece thePiece, int row, int col){

		try{
			//attempt to see if we can fit thePiece in the currentBox.
			for(int i = 0; i < thePiece.row; i++){
				for(int j = 0; j < thePiece.col; j++){

					//if piece section exists, remove it from the currentBox
					if(thePiece.space[i][j] != 0){
						//currentBox has that spot occupied
						if(currentBox[row + i][col + j] != 0)
							return false;
					}

				}
			}
		}
		catch(ArrayIndexOutOfBoundsException ex){//the fun lazy way.
			return false;
		}

		//put the piece in.
		for(int i = 0; i < thePiece.row; i++){
			for(int j = 0; j < thePiece.col; j++){

				//if piece section exists, remove it from the currentBox
				if(thePiece.space[i][j] != 0){
					//currentBox has that spot occupied
					currentBox[row + i][col + j] = thePiece.id;
				}

			}
		}

		//System.out.println("piece Id added was: " + thePiece.id);
		return true;

	}

	/**
	 * removePiece
	 * 
	 * removes the piece
	 * it is implied that the piece exists when you call this method
	 * 
	 * @param currentBox
	 * @param thePiece
	 * @param row - currentBox's row
	 * @param col - currentBox's column
	 */
	private static void removePiece(int[][] currentBox, Piece thePiece, int row, int col) {

		//go through every square in the piece and only remove a piece section if
		//the piece section exists

		for(int i = 0; i < thePiece.row; i++){
			for(int j = 0; j < thePiece.col; j++){

				//if piece section exists, remove it from the currentBox
				if(thePiece.space[i][j] != 0){
					currentBox[row + i][col + j] = 0;
				}

			}
		}

		//System.out.println("piece Id removed was: " + thePiece.id);
	}

	/**
	 * isSolution
	 * 
	 * checks to see if this is the solution
	 * 
	 * @param piecesRemaining
	 * @return boolean - true on solution; false if not.
	 */
	private static boolean isSolution(ArrayList<Piece> piecesRemaining){

		if(piecesRemaining.size() == 0)
			return true;

		return false;

	}

	/**
	 * solutionFound
	 * 
	 * a solution was found. Print it out
	 * 
	 * @param currentBox
	 */
	private static void solutionFound(int[][] currentBox){
		printIntMatrix(currentBox);

		printStopwatchTime(startTime);

	}

	//fills an int matrix will zeroes
	private static void zeroIntMatrix(int[][] a){

		for(int x = 0; x < a.length; x++){
			for(int y = 0; y < a[0].length; y++){
				a[x][y] = 0;
			}			
		}
	}

	private static void printStopwatchTime(long timeStart){
		long endTime = System.currentTimeMillis() - timeStart;
		long second = (endTime / 1000) % 60;
		long minute = (endTime / (1000 * 60)) % 60;
		long hour = (endTime / (1000 * 60 * 60)) % 24;
		System.out.printf("Duration (HH/MM/SS): %2d:%2d:%2d ", hour, minute, second);
	}

	private static void printIntMatrix(int[][] theMatrix){
		for(int i = 0; i < theMatrix.length; i++){
			for(int j = 0; j < theMatrix[0].length; j++){
				System.out.printf("%2d ", theMatrix[i][j]);
			}
			System.out.println();
		}
	}

	
	@SuppressWarnings("unused")
	//mainly for testing purposes.
	private static void printRemainingPieces(ArrayList<Piece> thePieces){

		for(int i = 0; i < thePieces.size(); i++){
			System.out.print(thePieces.get(i).pieceType + " ");
		}

		System.out.println();

	}

}
