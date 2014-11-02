package Pieces;

/**
 * Piece L Reversed
 *
 *  X
 *  X
 * XX
 * 
 * @author LK00100100
 *
 */

public class PieceLReversed extends Piece {

	public PieceLReversed(){
		id = -1;
		pieceType = "PieceLReversed";

		row = 3;
		col = 2;
		
	    space = new int[row][col];
	    
	    //set space to 0.
	    clearSpace();
	    
	    //input the space with a non-zero number, which will be
	    //replaced with an ID number for solution identification
	    space[0][1] = id;
	    space[1][1] = id;
	    space[2][0] = id;
	    space[2][1] = id;
		
		rotations = 4;
		currentRotation = 1;
		
	}

	@Override
	public void printPieceType() {
		System.out.print("LReversed");
	}
}
