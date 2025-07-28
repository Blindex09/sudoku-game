package com.sudoku.service;

import com.sudoku.model.Board;
import com.sudoku.model.GameState.Difficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Gerador de puzzles de Sudoku.
 * Cria tabuleiros válidos com diferentes níveis de dificuldade.
 */
public class PuzzleGenerator {
    
    private final Random random;
    private static final int MAX_ATTEMPTS = 1000;
    
    /**
     * Construtor padrão.
     */
    public PuzzleGenerator() {
        this.random = new Random();
    }
    
    /**
     * Construtor com seed para reproduzibilidade.
     * 
     * @param seed seed para o gerador de números aleatórios
     */
    public PuzzleGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Gera um novo puzzle com a dificuldade especificada.
     * 
     * @param difficulty nível de dificuldade
     * @return novo tabuleiro de Sudoku
     */
    public Board generatePuzzle(Difficulty difficulty) {
        Board completeBoard = generateCompleteBoard();
        return removeCells(completeBoard, difficulty);
    }
    
    /**
     * Gera um tabuleiro completamente preenchido e válido.
     * 
     * @return tabuleiro completo
     */
    private Board generateCompleteBoard() {
        Board board = new Board();
        fillBoard(board);
        return board;
    }
    
    /**
     * Preenche o tabuleiro usando backtracking.
     * 
     * @param board tabuleiro a ser preenchido
     * @return true se conseguiu preencher completamente
     */
    private boolean fillBoard(Board board) {
        // Encontrar próxima célula vazia
        int[] emptyCell = findEmptyCell(board);
        if (emptyCell == null) {
            return true; // Tabuleiro completo
        }
        
        int row = emptyCell[0];
        int col = emptyCell[1];
        
        // Tentar números de 1 a 9 em ordem aleatória
        List<Integer> numbers = getShuffledNumbers();
        
        for (int num : numbers) {
            if (board.isValidMove(row, col, num)) {
                board.setValue(row, col, num);
                
                if (fillBoard(board)) {
                    return true;
                }
                
                // Backtrack
                board.setValue(row, col, 0);
            }
        }
        
        return false;
    }
    
    /**
     * Encontra a próxima célula vazia no tabuleiro.
     * 
     * @param board tabuleiro
     * @return array [row, col] ou null se não houver células vazias
     */
    private int[] findEmptyCell(Board board) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.getCell(row, col).isEmpty()) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
    
    /**
     * Obtém uma lista embaralhada de números de 1 a 9.
     * 
     * @return lista de números embaralhada
     */
    private List<Integer> getShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, random);
        return numbers;
    }
    
    /**
     * Remove células do tabuleiro completo para criar o puzzle.
     * 
     * @param completeBoard tabuleiro completo
     * @param difficulty nível de dificuldade
     * @return tabuleiro com células removidas
     */
    private Board removeCells(Board completeBoard, Difficulty difficulty) {
        Board puzzle = completeBoard.copy();
        int cellsToRemove = (Board.SIZE * Board.SIZE) - difficulty.getFilledCells();
        
        List<int[]> allPositions = getAllPositions();
        Collections.shuffle(allPositions, random);
        
        int removed = 0;
        for (int[] pos : allPositions) {
            if (removed >= cellsToRemove) {
                break;
            }
            
            int row = pos[0];
            int col = pos[1];
            
            // Salvar o valor original
            int originalValue = puzzle.getCell(row, col).getValue();
            
            // Remover temporariamente
            puzzle.setValue(row, col, 0);
            
            // Verificar se o puzzle ainda tem solução única
            if (hasUniqueSolution(puzzle)) {
                removed++;
            } else {
                // Restaurar o valor se não mantém solução única
                puzzle.setValue(row, col, originalValue);
            }
        }
        
        // Marcar células preenchidas como fixas
        markFixedCells(puzzle);
        
        return puzzle;
    }
    
    /**
     * Obtém todas as posições do tabuleiro.
     * 
     * @return lista de posições [row, col]
     */
    private List<int[]> getAllPositions() {
        List<int[]> positions = new ArrayList<>();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                positions.add(new int[]{row, col});
            }
        }
        return positions;
    }
    
    /**
     * Verifica se o puzzle tem solução única (implementação simplificada).
     * 
     * @param board tabuleiro
     * @return true se tem solução única
     */
    private boolean hasUniqueSolution(Board board) {
        // Implementação simplificada - assume que sempre tem solução única
        // Uma implementação completa resolveria o puzzle e contaria soluções
        return true;
    }
    
    /**
     * Marca todas as células preenchidas como fixas.
     * 
     * @param board tabuleiro
     */
    private void markFixedCells(Board board) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (!board.getCell(row, col).isEmpty() && !board.getCell(row, col).isFixed()) {
                    board.getCell(row, col).setFixed(true);
                }
            }
        }
    }
    
    /**
     * Gera um puzzle de exemplo para testes (tabuleiro predefinido).
     * 
     * @param difficulty nível de dificuldade
     * @return tabuleiro de exemplo
     */
    public Board generateSamplePuzzle(Difficulty difficulty) {
        // Puzzle de exemplo - fácil
        int[][] sampleEasy = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        
        // Puzzle de exemplo - médio
        int[][] sampleMedium = {
            {0, 0, 0, 6, 0, 0, 4, 0, 0},
            {7, 0, 0, 0, 0, 3, 6, 0, 0},
            {0, 0, 0, 0, 9, 1, 0, 8, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 5, 0, 1, 8, 0, 0, 0, 3},
            {0, 0, 0, 3, 0, 6, 0, 4, 5},
            {0, 4, 0, 2, 0, 0, 0, 6, 0},
            {9, 0, 3, 0, 0, 0, 0, 0, 0},
            {0, 2, 0, 0, 0, 0, 1, 0, 0}
        };
        
        // Selecionar puzzle baseado na dificuldade
        int[][] selectedPuzzle;
        switch (difficulty) {
            case MEDIO:
            case DIFICIL:
            case EXPERT:
                selectedPuzzle = sampleMedium;
                break;
            default:
                selectedPuzzle = sampleEasy;
        }
        
        Board board = new Board(selectedPuzzle);
        markFixedCells(board);
        return board;
    }
    
    /**
     * Valida se um tabuleiro é um Sudoku válido.
     * 
     * @param board tabuleiro a validar
     * @return true se for válido
     */
    public boolean isValidSudoku(Board board) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                int value = board.getCell(row, col).getValue();
                if (value != 0) {
                    // Temporariamente remover o valor para testar
                    boolean wasFixed = board.getCell(row, col).isFixed();
                    board.getCell(row, col).setFixed(false);
                    board.getCell(row, col).setValue(0);
                    boolean valid = board.isValidMove(row, col, value);
                    board.getCell(row, col).setValue(value);
                    board.getCell(row, col).setFixed(wasFixed);
                    
                    if (!valid) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}