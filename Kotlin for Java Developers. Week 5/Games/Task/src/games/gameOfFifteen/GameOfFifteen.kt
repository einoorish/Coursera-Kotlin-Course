package games.gameOfFifteen

import board.*
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteenImpl(initializer)

class GameOfFifteenImpl(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.addInitialPermutation(initializer)
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        for(i in (1..board.width)){
            for(j in (1..board.width)){
                val cell = Cell(i,j)
                val number = board.getCellIndex(cell) + 1

                if(number == 16) return true
                if(board[cell] != number)
                    return false
            }
        }
        return true
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    override fun processMove(direction: Direction) {
        val emptyCell = board.getEmptyCell()
        val availableNeighborCell = when(direction){
            Direction.LEFT -> board.getCellOrNull(emptyCell.i, emptyCell.j+1)
            Direction.RIGHT -> board.getCellOrNull(emptyCell.i, emptyCell.j-1)
            Direction.UP -> board.getCellOrNull(emptyCell.i+1, emptyCell.j)
            Direction.DOWN -> board.getCellOrNull(emptyCell.i-1, emptyCell.j)
        }
        if(availableNeighborCell!=null){
            board[emptyCell] = board[availableNeighborCell]
            board[availableNeighborCell] = null
        }
    }

}

private fun GameBoard<Int?>.addInitialPermutation(initializer: GameOfFifteenInitializer) {
    val permutation = initializer.initialPermutation + listOf(null)
    for(i in (1..width)){
        for(j in (1..width)){
            val cell = Cell(i,j)
            val index = this.getCellIndex(cell)
            this[cell] = permutation[index]
        }
    }
}

private fun GameBoard<Int?>.getEmptyCell(): Cell = this.find { it == null }!!
