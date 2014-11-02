package Pieces;

/**
 * Piece T
 *
 *  +
 * +++
 * 
 * @author LK00100100
 *
 */
public class PieceT extends Piece {

	public PieceT(){
		id = -1;
		pieceType = "PieceT";

		row = 2;
		col = 3;
		
	    space = new int[row][col];
	    
	    //set space to 0.
	    clearSpace();
	    
	    //input the space with a non-zero number, which will be
	    //replaced with an ID number for solution identification
	    space[1][0] = id;
	    space[1][1] = id;
	    space[1][2] = id;
	    space[0][1] = id;
		
		rotations = 4;
		currentRotation = 1;
		
	}

	@Override
	public void printPieceType() {
		System.out.print("T");
	}
	
}
