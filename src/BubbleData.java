import Pieces.Piece;

public class BubbleData {
	
	//the upper left corner of the bubble
	public int row;
	public int col;
	
	public String PieceType;	//what piece type is required to fit here
	
	public Piece thePieceFitted;//the piece you fit in this bubble
	public int position;		//array position that you took this out of Piece Remaining
	
}
