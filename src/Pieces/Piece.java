package Pieces;

/**
 * This class represents one "piece." In the future Sigil-game updates,
 * there may be more bizarre pieces. To account for this, this
 * class is extend-able so you can make your own wacky pieces of variable size.
 * 
 * To add a custom piece:
 * 1) this class must be extended. 
 * 2) have a constructor and that one abstract print method.
 * Note: use other pieces as an example.
 * 
 * There is no need to alter this class's code.
 * 
 * Note: I put a lot of "public variables" cause I don't feel like typing a million getters and setters.
 * 
 * @author LK00100100
 *
 */
public abstract class Piece {

	public int id;	//unique piece ID
	public String pieceType; //type of piece

	public int row;	//# rows occupied
	public int col;	//# cols occupied

	//shows the space the piece is occupying.
	//0 is unoccupied, nonzero is occupied.
	public int[][] space;

	public int rotations;		//# of possible (unique) rotations

	//which rotation number you're on.
	//1 is the minimum starting rotation.
	public int currentRotation;	

	/**
	 * sets the id & updates the matrix
	 * 
	 * @param theId - the new id
	 */
	public void setId(int theId){
		id = theId;

		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(space[i][j] != 0)
					space[i][j] = id;
			}
		}

	}

	/**
	 * rotate clockwise once.
	 * updates space[][], row, col.
	 */
	public void rotate() {
		if(currentRotation == rotations)
			currentRotation = 1;
		else
			currentRotation++;

		int[][] newSpace = new int[col][row];	//the new rotated space matrix.

		//copy the old space into the new to rotate it 90 degrees clockwise.
		//XX  will become XX
		//XO			  OX
		int tempCol = row - 1;
		int tempRow;
		for(int i = 0; i < row; i++){
			tempRow = 0;

			for(int j = 0; j < col; j++){
				newSpace[tempRow++][tempCol] = space[i][j];
			}
			tempCol--;
		}

		//the rotated matrix is the new one.
		space = newSpace;
		int temp = row;
		row = col;
		col = temp;

	}

	/**
	 * rotates a space Matrix clockwise once.
	 * 
	 * @param theSpace - the int[][] space to rotate
	 */
	public static int[][] rotate(int[][] theSpace){

		int spaceRow = theSpace.length;
		int spaceCol = theSpace[0].length;

		int[][] newSpace = new int[spaceCol][spaceRow];	//the new rotated space matrix.

		//copy the old space into the new to rotate it 90 degrees clockwise.
		//XX  will become XX
		//XO			  OX
		int tempCol = spaceRow - 1;
		int tempRow;
		for(int i = 0; i < spaceRow; i++){
			tempRow = 0;

			for(int j = 0; j < spaceCol; j++){
				newSpace[tempRow++][tempCol] = theSpace[i][j];
			}
			tempCol--;
		}
		
		return newSpace;

	}

	/**
	 * checks the spaces. returns true, if true 
	 *
	 * @param theSpace
	 * @return boolean - true if same.
	 */
	public boolean isSpaceEquals(int[][] theSpace){

		//range checked
		if(row != theSpace.length)
			return false;
		if(col != theSpace[0].length)
			return false;

		//compare box by box
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				//if one is occupied and the other isn't, return false
				if(space[i][j] == 0 &&  theSpace[i][j] !=0)
					return false;
				if(space[i][j] != 0 &&  theSpace[i][j] ==0)
					return false;
				
				
			}
		}

		return true;

	}

	/**
	 * clears the space matrix
	 */
	public void clearSpace(){
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				space[i][j] = 0;

	}

	/**
	 * @return int - the number of blocks a Piece is made up of. (PieceT = 4 blocks)
	 */
	public int getBlockSize(){
		int blocks = 0;

		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				if(space[i][j] == id)
					blocks++;
			}
		}

		return blocks;

	}

	/**
	 * this prints the space matrix
	 */
	public void printPiece(){
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(space[i][j] == 0)
					System.out.print("0");
				else
					System.out.print("X");
			}
			System.out.println();
		}

	}

	/**
	 * compares two piece types
	 * 
	 * @param otherPiece - the other Piece to compare with
	 * @return boolean - true if same type; false if not.
	 */
	public boolean equals(Piece otherPiece){

		if(this.pieceType.equals(otherPiece.pieceType))
			return true;

		return false;

	}

	/**
	 * prints the pieceType
	 */
	public abstract void printPieceType();

}
