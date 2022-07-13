package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardIml(width)

open class SquareBoardImpl(final override val width: Int): SquareBoard{

    protected val cells: MutableList<Cell> = mutableListOf()

    init {
        for(i in 1..width){
            for(j in 1..width) {
                cells.add(Cell(i, j))
            }
        }
    }

    private fun getCellIndex(cell: Cell): Int{
        return (cell.i-1)*width + cell.j - 1
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        val index = getCellIndex(Cell(i,j))
        return cells.getOrNull(index)
    }

    override fun getCell(i: Int, j: Int): Cell {
        val index = getCellIndex(Cell(i,j))
        return cells[index]
    }

    override fun getAllCells(): Collection<Cell> {
       return cells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val start = i - 2 + Math.min(jRange.first, jRange.last)
        val end = i - 1 + Math.max(jRange.first, jRange.last)
        require(start>-1)

        val lastRowElIndex = start + width

        if(jRange.first <= jRange.last)
            return cells.subList(start, (Math.min(end, lastRowElIndex)))
        else
            return cells.subList(i+jRange.last-2, i+jRange.first-1).reversed()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val result = mutableListOf<Cell>()
        val startCol = Math.min(iRange.first, iRange.last)
        val endCol = Math.max(iRange.first, iRange.last)
        require(startCol>0)

        val start = getCellIndex(Cell(startCol, j))
        val end = getCellIndex(Cell(Math.min(endCol, width), j))

        for (i in start..end step width){
            result.add(cells.get(i))
        }

        if(iRange.first > iRange.last)
            return result.reversed()

        return result
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val cellIndex = getCellIndex(this)
        return when(direction){
            UP -> cells.getOrNull(cellIndex-width)
            DOWN -> cells.getOrNull(cellIndex+width)
            RIGHT -> cells.getOrNull(cellIndex+1)
            LEFT -> cells.getOrNull(cellIndex-1)
        }
    }
}


class GameBoardIml<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val cellsAndValues: MutableMap<Cell, T?> = mutableMapOf()

    init {
        for (cell in cells){
            cellsAndValues[cell] = null
        }
    }

    override fun get(cell: Cell): T? {
       return cellsAndValues.getOrDefault(cell, null)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellsAndValues.values.all(predicate)
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellsAndValues.values.any(predicate)

    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellsAndValues.filterValues(predicate).keys.firstOrNull()
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellsAndValues.filterValues(predicate).keys
    }

    override fun set(cell: Cell, value: T?) {
        cellsAndValues[cell] = value
    }

}