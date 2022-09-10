# ChessProject

A recreation of Chess within Java, using the console as a display.

--Display--

The chess board is displayed as a grid through the console, mimicking a real chess board.
The horizontal axis consists of 8 spaces, labelled a-h, going left to right.
The vertical axis consists of 8 spaces, labelled 1-8, going from bottom to top.

The pieces are displayed as 3 character sequences, like so:
King = Color-Kg, ex: BKg
Queen = Color-Qn, ex: WQn
Rook = Color-originSpace-R, ex. BaR
Bishop = Color-originSpace-B, ex. WfB
Knight = Color-originSpace-K, ex. BgK
Pawn = Color-originSpace-P, ex. WeP

--Gameplay--

The game begins by asking each player's name. When the game asks a given player to move:
Choose the space containing the target piece, using letter-number, ex. a2
Choose the goal space, also using <letter><number>, ex. a4

The game should move the target piece to the goal space unless:
The move is invalid/illegal (outside of piece's/the board's range),
The move places the king in check
