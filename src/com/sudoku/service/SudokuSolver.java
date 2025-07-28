package com.sudoku.service;

import com.sudoku.model.Board;
import com.sudoku.model.Cell;

/**
 * Resolvedor de puzzles de Sudoku usando algoritmo de backtracking.
 * Pode ser usado para validar soluções ou fornecer dicas.
 */
public class SudokuSolver {
    
    private int solutionCount;
    private static final int MAX_SOLUTIONS = 2; // Para verificar unicidade
    
    /**
     * Resolve um puzzle de Sudoku.
     * 
     * @param board tabuleiro a resolver
     * @return true se conseguiu resolver
     */
    public boolean solve(Board board) {
        return solveBacktrack(board);
    }
    
    /**
     * Resolve usando algoritmo de backtracking.
     * 
     * @param board tabuleiro
     * @return true se resolvido
     */
    private boolean solveBacktrack(Board board) {
        int[] emptyCell = findEmptyCell(board);
        
        if (emptyCell == null) {
            return true; // Puzzle resolvido
        }
        
        int row = emptyCell[0];
        int col = emptyCell[1];
        
        for (int num = 1; num <= 9; num++) {
            if (board.isValidMove(row, col, num)) {
                board.setValue(row, col, num);
                
                if (solveBacktrack(board)) {
                    return true;
                }
                
                // Backtrack
                board.setValue(row, col, 0);
            }
        }
        
        return false;
    }
    
    /**
     * Encontra a próxima célula vazia.
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
     * Conta o número de soluções possíveis.
     * 
     * @param board tabuleiro
     * @return número de soluções (limitado a MAX_SOLUTIONS)
     */
    public int countSolutions(Board board) {
        solutionCount = 0;
        Board copy = board.copy();
        countSolutionsBacktrack(copy);
        return solutionCount;
    }
    
    /**
     * Conta soluções usando backtracking.
     * 
     * @param board tabuleiro
     */
    private void countSolutionsBacktrack(Board board) {
        if (solutionCount >= MAX_SOLUTIONS) {
            return; // Limitar para evitar muitas iterações
        }
        
        int[] emptyCell = findEmptyCell(board);
        
        if (emptyCell == null) {
            solutionCount++;
            return;
        }
        
        int row = emptyCell[0];
        int col = emptyCell[1];
        
        for (int num = 1; num <= 9; num++) {
            if (board.isValidMove(row, col, num)) {
                board.setValue(row, col, num);
                countSolutionsBacktrack(board);
                board.setValue(row, col, 0);
            }
        }
    }
    
    /**
     * Verifica se um puzzle tem solução única.
     * 
     * @param board tabuleiro
     * @return true se tem exatamente uma solução
     */
    public boolean hasUniqueSolution(Board board) {
        return countSolutions(board) == 1;
    }
    
    /**
     * Verifica se um puzzle é solucionável.
     * 
     * @param board tabuleiro
     * @return true se tem pelo menos uma solução
     */
    public boolean isSolvable(Board board) {
        Board copy = board.copy();
        return solve(copy);
    }
    
    /**
     * Obtém uma dica para o próximo movimento.
     * 
     * @param board tabuleiro atual
     * @return array [row, col, value] ou null se não conseguir dar dica
     */
    public int[] getHint(Board board) {
        Board copy = board.copy();
        
        // Tentar encontrar uma célula que tenha apenas uma possibilidade
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (copy.getCell(row, col).isEmpty()) {
                    int[] candidates = getPossibleValues(copy, row, col);
                    if (candidates.length == 1) {
                        return new int[]{row, col, candidates[0]};
                    }
                }
            }
        }
        
        // Se não encontrou célula óbvia, resolver e pegar a primeira célula vazia
        if (solve(copy)) {
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        return new int[]{row, col, copy.getCell(row, col).getValue()};
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Obtém os valores possíveis para uma célula.
     * 
     * @param board tabuleiro
     * @param row linha
     * @param col coluna
     * @return array com valores possíveis
     */
    public int[] getPossibleValues(Board board, int row, int col) {
        if (!board.getCell(row, col).isEmpty()) {
            return new int[0];
        }
        
        java.util.List<Integer> possible = new java.util.ArrayList<>();
        
        for (int num = 1; num <= 9; num++) {
            if (board.isValidMove(row, col, num)) {
                possible.add(num);
            }
        }
        
        return possible.stream().mapToInt(i -> i).toArray();
    }
    
    /**
     * Verifica se uma solução está correta.
     * 
     * @param board tabuleiro a verificar
     * @return true se a solução estiver correta
     */
    public boolean verifySolution(Board board) {
        if (!board.isSolved()) {
            return false;
        }
        
        // Verificar se não há conflitos
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                int value = board.getCell(row, col).getValue();
                
                // Temporariamente remover o valor
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
        
        return true;
    }
    
    /**
     * Calcula a dificuldade de um puzzle baseado em técnicas necessárias.
     * 
     * @param board tabuleiro
     * @return estimativa de dificuldade (1-10)
     */
    public int estimateDifficulty(Board board) {
        Board copy = board.copy();
        int difficulty = 1;
        int iterations = 0;
        int maxIterations = 100;
        
        while (!copy.isSolved() && iterations < maxIterations) {
            boolean progress = false;
            
            // Técnica 1: Naked Singles (célula com apenas uma possibilidade)
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (copy.getCell(row, col).isEmpty()) {
                        int[] candidates = getPossibleValues(copy, row, col);
                        if (candidates.length == 1) {
                            copy.setValue(row, col, candidates[0]);
                            progress = true;
                        }
                    }
                }
            }
            
            if (!progress) {
                difficulty += 2; // Precisou de técnicas mais avançadas
                // Usar backtracking para continuar
                if (!solve(copy)) {
                    break;
                }
                progress = true;
            }
            
            iterations++;
        }
        
        // Ajustar dificuldade baseado no número de células iniciais
        int filledCells = board.getFilledCells();
        if (filledCells < 20) difficulty += 3;
        else if (filledCells < 25) difficulty += 2;
        else if (filledCells < 30) difficulty += 1;
        
        return Math.min(10, Math.max(1, difficulty));
    }
    
    /**
     * Valida se um movimento específico não quebra as regras do Sudoku.
     * 
     * @param board tabuleiro
     * @param row linha
     * @param col coluna
     * @param value valor
     * @return true se o movimento for válido
     */
    public boolean validateMove(Board board, int row, int col, int value) {
        if (value < 1 || value > 9) {
            return false;
        }
        
        Cell cell = board.getCell(row, col);
        if (cell.isFixed()) {
            return false;
        }
        
        return board.isValidMove(row, col, value);
    }
}