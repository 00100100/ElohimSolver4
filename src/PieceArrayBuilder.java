import java.util.ArrayList;

import Pieces.*;

/**
 * PieceArrayBuilder
 * 
 * generates pieces (that you define)
 * and
 * stores it in an array of Pieces
 * 
 * The backtracking algorithm requires that the pieces are in sorted (by piece) order.
 * 
 * @author LK00100100
 *
 */
public class PieceArrayBuilder {
	
	private ArrayList<Piece> arrayPieces;
	private int id;							//this will provide a unique id to every piece so solutions can be readable.
	private Piece temp;						//holds a piece temporarily.
	private int smallestPieceSize;				//the smallest number of blocks a piece takes up in the array.
	
	public PieceArrayBuilder(){
		
		arrayPieces = new ArrayList<Piece>();
		id = 1;
		smallestPieceSize = 999999999;
		
	}
	
	public void addPieceL(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceL();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	
	}
	
	public void addPieceLReversed(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceLReversed();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	}
	
	public void addPieceSquare(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceSquare();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	}
	
	public void addPieceStraight(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceStraight();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	}
	
	public void addPieceT(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceT();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	}
	
	public void addPieceThunder(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceThunder();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	}
	
	public void addPieceThunderReversed(int quantity){
		//get a new piece, set id, and then add to the arrayPieces.
		for(int i = 0; i < quantity; i++){	
			temp = new PieceThunderReversed();
			temp.setId(id++);			
			arrayPieces.add(temp);
		}
		
		compareSmallestPieceSize(temp);
	}
	
	public ArrayList<Piece> getArrayPieces(){
		return arrayPieces;
	}
	
	public int getSmallestPiece(){
		return smallestPieceSize;
	}
	
	public void compareSmallestPieceSize(Piece thePiece){
		
		if(thePiece == null)
			return;
		
		//set the smallest piece (if you can)
		if(thePiece.getBlockSize() < smallestPieceSize)
			smallestPieceSize = thePiece.getBlockSize();
	}
	
	/**
	 * sort by Pieces with the fewest bubbles.
	 * 
	 * PieceStraight, PieceT, PieceStraight
	 * would become
	 * PieceStraight, PieceStraight, PieceT 
	 * 
	 */
	public void sortPieceArray(){
		
		//TODO Maybe
		
	}
	
	public void printArray(){
		
		System.out.print("arrayPieces: ");
		for(int i = 0; i < arrayPieces.size(); i++){
			arrayPieces.get(i).printPieceType();
			System.out.print(" ");
		}
		
		System.out.println();
	}

}
