package com.sudoku.model;

import java.util.Arrays;

/**
 * Representa o tabuleiro de Sudoku 9x9.
 * Contém a lógica para validação de jogadas e manipulação do estado do jogo.
 */
public class Board {
    public static final int SIZE = 9;
    public static final int SUBGRID_SIZE = 3;
    
    private Cell[][] grid;
    private int filledCells;
    
    /**
     * Construtor que cria um tabuleiro vazio.
     */
    public Board() {
        this.grid = new Cell[SIZE][SIZE];
        this.filledCells = 0;
        initializeBoard();
    }
    
    /**
     * Construtor que cria um tabuleiro com base em uma matriz de inteiros.
     * 
     * @param initialBoard matriz 9x9 com os valores iniciais (0 para células vazias)
     */
    public Board(int[][] initialBoard) {
        this();
        loadBoard(initialBoard);
    }
    
    /**
     * Inicializa o tabuleiro com células vazias.
     */
    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = new Cell();
            }
        }
    }
    
    /**
     * Carrega um tabuleiro a partir de uma matriz de inteiros.
     * 
     * @param board matriz 9x9 com os valores
     */
    public void loadBoard(int[][] board) {
        if (board.length != SIZE || board[0].length != SIZE) {
            throw new IllegalArgumentException("Tabuleiro deve ser 9x9");
        }
        
        filledCells = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int value = board[row][col];
                if (value < 0 || value > 9) {
                    throw new IllegalArgumentException("Valores devem estar entre 0 e 9");
                }
                
                grid[row][col] = new Cell(value, value != 0);
                if (value != 0) {
                    filledCells++;
                }
            }
        }
    }
    
    /**
     * Obtém a célula na posição especificada.
     * 
     * @param row linha (0-8)
     * @param col coluna (0-8)
     * @return a célula na posição especificada
     */
    public Cell getCell(int row, int col) {
        validatePosition(row, col);
        return grid[row][col];
    }
    
    /**
     * Define um valor em uma posição específica.
     * 
     * @param row linha (0-8)
     * @param col coluna (0-8)
     * @param value valor (0-9)
     * @return true se a jogada foi válida e realizada
     */
    public boolean setValue(int row, int col, int value) {
        validatePosition(row, col);
        
        Cell cell = grid[row][col];
        if (cell.isFixed()) {
            return false;
        }
        
        int oldValue = cell.getValue();
        
        // Se estamos removendo um valor
        if (value == 0) {
            cell.setValue(0);
            if (oldValue != 0) {
                filledCells--;
            }
            return true;
        }
        
        // Verificar se a jogada é válida
        if (!isValidMove(row, col, value)) {
            return false;
        }
        
        cell.setValue(value);
        if (oldValue == 0) {
            filledCells++;
        }
        
        return true;
    }
    
    /**
     * Verifica se uma jogada é válida na posição especificada.
     * 
     * @param row linha (0-8)
     * @param col coluna (0-8)
     * @param value valor a verificar (1-9)
     * @return true se a jogada for válida
     */
    public boolean isValidMove(int row, int col, int value) {
        if (value < 1 || value > 9) {
            return false;
        }
        
        return isValidInRow(row, value) && 
               isValidInColumn(col, value) && 
               isValidInSubgrid(row, col, value);
    }
    
    /**
     * Verifica se um valor é válido em uma linha.
     * 
     * @param row linha a verificar
     * @param value valor a verificar
     * @return true se o valor não existir na linha
     */
    private boolean isValidInRow(int row, int value) {
        for (int col = 0; col < SIZE; col++) {
            if (grid[row][col].getValue() == value) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Verifica se um valor é válido em uma coluna.
     * 
     * @param col coluna a verificar
     * @param value valor a verificar
     * @return true se o valor não existir na coluna
     */
    private boolean isValidInColumn(int col, int value) {
        for (int row = 0; row < SIZE; row++) {
            if (grid[row][col].getValue() == value) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Verifica se um valor é válido em um subgrid 3x3.
     * 
     * @param row linha da célula
     * @param col coluna da célula
     * @param value valor a verificar
     * @return true se o valor não existir no subgrid
     */
    private boolean isValidInSubgrid(int row, int col, int value) {
        int startRow = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int startCol = (col / SUBGRID_SIZE) * SUBGRID_SIZE;
        
        for (int i = startRow; i < startRow + SUBGRID_SIZE; i++) {
            for (int j = startCol; j < startCol + SUBGRID_SIZE; j++) {
                if (grid[i][j].getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Verifica se o tabuleiro está completo e correto.
     * 
     * @return true se o Sudoku estiver resolvido
     */
    public boolean isSolved() {
        if (filledCells != SIZE * SIZE) {
            return false;
        }
        
        // Verificar todas as linhas, colunas e subgrids
        for (int i = 0; i < SIZE; i++) {
            if (!isRowComplete(i) || !isColumnComplete(i)) {
                return false;
            }
        }
        
        // Verificar todos os subgrids
        for (int row = 0; row < SIZE; row += SUBGRID_SIZE) {
            for (int col = 0; col < SIZE; col += SUBGRID_SIZE) {
                if (!isSubgridComplete(row, col)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Verifica se uma linha está completa (contém números 1-9).
     * 
     * @param row linha a verificar
     * @return true se a linha estiver completa
     */
    private boolean isRowComplete(int row) {
        boolean[] numbers = new boolean[10];
        for (int col = 0; col < SIZE; col++) {
            int value = grid[row][col].getValue();
            if (value == 0 || numbers[value]) {
                return false;
            }
            numbers[value] = true;
        }
        return true;
    }
    
    /**
     * Verifica se uma coluna está completa (contém números 1-9).
     * 
     * @param col coluna a verificar
     * @return true se a coluna estiver completa
     */
    private boolean isColumnComplete(int col) {
        boolean[] numbers = new boolean[10];
        for (int row = 0; row < SIZE; row++) {
            int value = grid[row][col].getValue();
            if (value == 0 || numbers[value]) {
                return false;
            }
            numbers[value] = true;
        }
        return true;
    }
    
    /**
     * Verifica se um subgrid está completo (contém números 1-9).
     * 
     * @param startRow linha inicial do subgrid
     * @param startCol coluna inicial do subgrid
     * @return true se o subgrid estiver completo
     */
    private boolean isSubgridComplete(int startRow, int startCol) {
        boolean[] numbers = new boolean[10];
        for (int i = startRow; i < startRow + SUBGRID_SIZE; i++) {
            for (int j = startCol; j < startCol + SUBGRID_SIZE; j++) {
                int value = grid[i][j].getValue();
                if (value == 0 || numbers[value]) {
                    return false;
                }
                numbers[value] = true;
            }
        }
        return true;
    }
    
    /**
     * Valida se uma posição está dentro dos limites do tabuleiro.
     * 
     * @param row linha
     * @param col coluna
     */
    private void validatePosition(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Posição inválida: (" + row + ", " + col + ")");
        }
    }
    
    /**
     * Obtém o número de células preenchidas.
     * 
     * @return número de células preenchidas
     */
    public int getFilledCells() {
        return filledCells;
    }
    
    /**
     * Verifica se o tabuleiro está vazio.
     * 
     * @return true se não houver células preenchidas
     */
    public boolean isEmpty() {
        return filledCells == 0;
    }
    
    /**
     * Limpa o tabuleiro, removendo todos os valores não fixos.
     */
    public void clear() {
        filledCells = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = grid[row][col];
                if (!cell.isFixed()) {
                    cell.setValue(0);
                } else if (cell.getValue() != 0) {
                    filledCells++;
                }
            }
        }
    }
    
    /**
     * Cria uma cópia do tabuleiro.
     * 
     * @return nova instância de Board com o mesmo estado
     */
    public Board copy() {
        Board newBoard = new Board();
        newBoard.filledCells = this.filledCells;
        
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                newBoard.grid[row][col] = this.grid[row][col].copy();
            }
        }
        
        return newBoard;
    }
    
    /**
     * Converte o tabuleiro em uma matriz de inteiros.
     * 
     * @return matriz 9x9 com os valores do tabuleiro
     */
    public int[][] toArray() {
        int[][] array = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                array[row][col] = grid[row][col].getValue();
            }
        }
        return array;
    }
    
    /**
     * Obtém uma representação textual do tabuleiro.
     * 
     * @return string formatada do tabuleiro
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────────┬─────────┬─────────┐\n");
        
        for (int row = 0; row < SIZE; row++) {
            if (row > 0 && row % SUBGRID_SIZE == 0) {
                sb.append("├─────────┼─────────┼─────────┤\n");
            }
            
            sb.append("│ ");
            for (int col = 0; col < SIZE; col++) {
                if (col > 0 && col % SUBGRID_SIZE == 0) {
                    sb.append("│ ");
                }
                
                Cell cell = grid[row][col];
                if (cell.isEmpty()) {
                    sb.append("  ");
                } else {
                    sb.append(cell.getValue()).append(" ");
                }
            }
            sb.append("│\n");
        }
        
        sb.append("└─────────┴─────────┴─────────┘");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Board board = (Board) obj;
        return Arrays.deepEquals(this.toArray(), board.toArray());
    }
    
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(toArray());
    }
}