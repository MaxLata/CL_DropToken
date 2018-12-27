import java.util.*;

public class DropToken {
  final int dims = 4;         // size of sides (assumes square board)
  int[][] board;              // 2D array for board
  int[] lowestOpen;           // 1D array keeping track of lowest open slot
  List<Integer> validPlays;   // list of valid "puts"
  Boolean P1;                 // keeps track of turns

  // Constructor
  public DropToken() {
    board = new int[dims][dims];
    lowestOpen = new int[dims];
    Arrays.fill(lowestOpen, dims - 1);      // initializes array to lowest slot
    validPlays = new ArrayList<Integer>();
    P1 = true;
  }

  /********* PUT *********/
  public void puts(int column) {
    // if there are still open spots, fill the lowest
    if (lowestOpen[column] >= 0) {
      System.out.println("OK");
      fillSlot(lowestOpen[column], column);

      // if the board has been filled without a win, it's a draw. Exit.
      if (validPlays.size() == dims * dims) {
        System.out.println("DRAW");
        printBoard();
        System.exit(0);
      }
    } else {
      System.out.println("ERROR");
    }
  }

  // fills slots in board
  public void fillSlot(int row, int column) {
    int fill;

    // fills slot with a 1 or 2 depending on player
    if (P1) {
      fill = 1;
    } else {
      fill = 2;
    }

    board[row][column] = fill;        // fills in slot
    P1 = !P1;                         // switches turn
    validPlays.add(column + 1);       // adds to running list of moves (actual column number)
    lowestOpen[column]--;             // moves lowest slot in column up one

    // check wins, but only after a win is possible
    if (validPlays.size() >= (2 * dims) - 1) {
      checkWin(row, column, fill);
    }
  }

  /********* GET *********/
  // Prints the list of valid plays
  public void printPlays() {
    for (Integer play : validPlays) {
      System.out.println(play);
    }
  }

  /********* BOARD *********/
  // Prints the board to the command line
  public void printBoard() {
    // loop over 2d array
    for (int i = 0; i < dims; i++) {
      System.out.print("| ");
      for (int j = 0; j < dims; j++) {
        System.out.print(board[i][j] + " ");
      }
      System.out.println();
    }

    // print bottom with column numbers
    System.out.println("+--------");
    System.out.println("  1 2 3 4");
  }

  /********* Helper functions to check game states *********/
  public void checkWin(int row, int column, int player) {
    checkHorizontal(row, column, player);
    checkVertical(row, column, player);
    checkDiagonal1(row, column, player);
    checkDiagonal2(row, column, player);
  }

  // Checks for a horizontal win
  public void checkHorizontal(int row, int column, int player) {
    for (int i = 0; i < dims; i++) {
      if (board[i][column] != player) {
        return;
      }
    }

    win();
  }

  // Checks for a vertical win
  public void checkVertical(int row, int column, int player) {
    // Only checks for win if possible (if column is full)
    if (lowestOpen[column] == 0) {
      for (int i = dims - 1; i >= 0; i--) {
        if (board[row][i] != player) {
          return;
        }
      }

      win();
    }
  }

  // Checks for diagonal win, from top left to bottom right
  public void checkDiagonal1(int row, int column, int player) {
    if (row == column) {
      for (int i = 0; i < dims; i++) {
        if (board[i][i] != player) {
          return;
        }
      }

      win();
    }
  }

  // Checks for diagonal win, from top right to bottom left
  public void checkDiagonal2(int row, int column, int player) {
    if (row + column == dims - 1) {
      for (int i = 0; i < dims; i++) {
        if (board[i][dims - 1 - i] != player) {
          return;
        }
      }

      win();
    }
  }

  // Prints the win message and exits
  public void win() {
    System.out.println("WIN");
    printBoard();
    System.exit(0);
  }

  public static void main(String[] args) {
    DropToken dt = new DropToken();
    Scanner scanner = new Scanner(System.in);

    // loop over standard input
    while(scanner.hasNext()) {
      String command = scanner.nextLine().trim().toUpperCase();

      // regex asserts that put has one and only one argument between 1 and 4
      // and that there is at least one space between put and the argument
      if (command.matches("PUT\\s+[1-4]$")) {
        int column = Integer.parseInt(command.split("\\s+")[1]);
        dt.puts(column - 1);
      } else if (command.equals("GET")) {
        dt.printPlays();
      } else if (command.equals("BOARD")) {
        dt.printBoard();
      } else if (command.equals("EXIT")) {
        System.exit(0);
      }
    }
  }
}
