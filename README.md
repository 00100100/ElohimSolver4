ElohimSolver4
=============
TODO: needs one big clean up and unit tests

<b>WHAT IT DOES</b>
Same as ElohimSolver3 but takes bubbles of minSize and pushes pieces into them

This program will take a pool of pieces and jam them in every which way possible into the board. Unfortunately, this is very,
very slow. Some partial solutions are bad and shouldn't be investigated further. This program will "prune" bad partial
solutions and backtrack.

The pruning methods are: 1) In a sorted list of pieces, if the current piece to place down is the same as the previous piece
(was calculated), skip to the next piece to avoid recalculation.

2) If the current piece does not fit even with all its rotations, backtrack.

3-1) After placing a piece, check around the immediate area around the piece to see if there are any "bubbles" of spaces which
are of a size that are too small (a bubble of two blocks cannot fit any of the 4-block tetris pieces). If there is a "bubble"
that's smaller than a certain size, backtrack. For one space, the bubbleCheck() will only look only as far as a certain number
so it does look through the whole board. Also there is a 2d grid to store what has been bubbledChecked and the size it
encountered. This is checked before looking around the board for the bubble size.

3-2) For every bubble formed from placing that one piece, see if there are enough correct pieces remaining to fill these
bubbles. If there aren't, backtrack.

This code is significantly faster than than ElohimSolver3 because it cuts down on the recursive solution combination search.

Feel free to improve this work.

<b>INSTALL</b>
I used Eclipse to make this. Download this code and import it to your Eclipse to run it.

LK00100100@gmail.com
