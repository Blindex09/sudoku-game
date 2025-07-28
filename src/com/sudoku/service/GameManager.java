package com.sudoku.service;

import com.sudoku.model.Board;
import com.sudoku.model.GameState;
import com.sudoku.model.GameState.Difficulty;
import com.sudoku.model.GameState.Status;

/**
 * Gerenciador principal do jogo de Sudoku.
 * Coordena todas as operações entre o modelo, serviços e interface.
 */
public class GameManager {
    
    private GameState currentGame;
    private final PuzzleGenerator puzzleGenerator;
    private final SudokuSolver solver;
    
    /**
     * Construtor padrão.
     */
    public GameManager() {
        this.puzzleGenerator = new PuzzleGenerator();
        this.solver = new SudokuSolver();
        this.currentGame = new GameState();
    }
    
    /**
     * Inicia um novo jogo com a dificuldade especificada.
     * 
     * @param difficulty nível de dificuldade
     * @return true se o jogo foi iniciado com sucesso
     */
    public boolean startNewGame(Difficulty difficulty) {
        try {
            Board puzzle = puzzleGenerator.generateSamplePuzzle(difficulty);
            
            if (!puzzleGenerator.isValidSudoku(puzzle)) {
                return false;
            }
            
            currentGame = new GameState(difficulty);
            currentGame.startNewGame(puzzle);
            
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao iniciar novo jogo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Carrega um jogo a partir de um tabuleiro fornecido.
     * 
     * @param boardData matriz 9x9 com o estado do tabuleiro
     * @param difficulty dificuldade do jogo
     * @return true se carregado com sucesso
     */
    public boolean loadGame(int[][] boardData, Difficulty difficulty) {
        try {
            Board board = new Board(boardData);
            
            if (!puzzleGenerator.isValidSudoku(board)) {
                return false;
            }
            
            currentGame = new GameState(difficulty);
            currentGame.startNewGame(board);
            
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao carregar jogo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Executa uma jogada.
     * 
     * @param row linha (1-9)
     * @param col coluna (1-9)
     * @param value valor (0-9, onde 0 limpa a célula)
     * @return resultado da jogada
     */
    public MoveResult makeMove(int row, int col, int value) {
        // Converter para índices baseados em 0
        int boardRow = row - 1;
        int boardCol = col - 1;
        
        // Validar entrada
        if (boardRow < 0 || boardRow >= 9 || boardCol < 0 || boardCol >= 9) {
            return new MoveResult(false, "Posição inválida. Use números de 1 a 9.");
        }
        
        if (value < 0 || value > 9) {
            return new MoveResult(false, "Valor inválido. Use números de 0 a 9.");
        }
        
        if (currentGame.getStatus() != Status.JOGANDO) {
            return new MoveResult(false, "Jogo não está em andamento.");
        }
        
        // Verificar se a célula é fixa
        if (currentGame.getBoard().getCell(boardRow, boardCol).isFixed()) {
            return new MoveResult(false, "Esta célula não pode ser alterada.");
        }
        
        // Executar jogada
        boolean success = currentGame.makeMove(boardRow, boardCol, value);
        
        if (!success) {
            if (value != 0) {
                return new MoveResult(false, "Jogada inválida. Número já existe na linha, coluna ou quadrante.");
            } else {
                return new MoveResult(false, "Não foi possível limpar a célula.");
            }
        }
        
        // Verificar se o jogo foi concluído
        if (currentGame.isCompleted()) {
            return new MoveResult(true, "Parabéns! Você resolveu o Sudoku!");
        }
        
        return new MoveResult(true, "Jogada realizada com sucesso.");
    }
    
    /**
     * Solicita uma dica para o jogador.
     * 
     * @return resultado da dica
     */
    public HintResult getHint() {
        if (currentGame.getStatus() != Status.JOGANDO) {
            return new HintResult(false, "Jogo não está em andamento.", -1, -1, -1);
        }
        
        int[] hint = solver.getHint(currentGame.getBoard());
        
        if (hint == null) {
            return new HintResult(false, "Não foi possível fornecer uma dica.", -1, -1, -1);
        }
        
        // Usar a dica no estado do jogo
        currentGame.useHint(hint[0], hint[1]);
        
        // Converter para coordenadas baseadas em 1
        return new HintResult(true, 
            String.format("Dica: Na posição (%d, %d) coloque o número %d", 
                         hint[0] + 1, hint[1] + 1, hint[2]),
            hint[0] + 1, hint[1] + 1, hint[2]);
    }
    
    /**
     * Pausa o jogo atual.
     * 
     * @return true se pausado com sucesso
     */
    public boolean pauseGame() {
        if (currentGame.getStatus() == Status.JOGANDO) {
            currentGame.pauseGame();
            return true;
        }
        return false;
    }
    
    /**
     * Resume o jogo pausado.
     * 
     * @return true se resumido com sucesso
     */
    public boolean resumeGame() {
        if (currentGame.getStatus() == Status.PAUSADO) {
            currentGame.resumeGame();
            return true;
        }
        return false;
    }
    
    /**
     * Reseta o jogo atual para o estado inicial.
     * 
     * @return true se resetado com sucesso
     */
    public boolean resetGame() {
        if (currentGame != null) {
            currentGame.reset();
            return true;
        }
        return false;
    }
    
    /**
     * Abandona o jogo atual.
     * 
     * @return true se abandonado com sucesso
     */
    public boolean abandonGame() {
        if (currentGame != null && currentGame.isPlaying()) {
            currentGame.abandonGame();
            return true;
        }
        return false;
    }
    
    /**
     * Resolve automaticamente o puzzle (para fins de demonstração).
     * 
     * @return true se resolvido com sucesso
     */
    public boolean solvePuzzle() {
        if (currentGame.getStatus() != Status.JOGANDO) {
            return false;
        }
        
        Board solution = currentGame.getBoard().copy();
        boolean solved = solver.solve(solution);
        
        if (solved) {
            // Copiar solução para o jogo atual (mantendo células fixas)
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (currentGame.getBoard().getCell(row, col).isEmpty()) {
                        currentGame.makeMove(row, col, solution.getCell(row, col).getValue());
                    }
                }
            }
        }
        
        return solved;
    }
    
    /**
     * Valida se uma jogada específica é válida sem executá-la.
     * 
     * @param row linha (1-9)
     * @param col coluna (1-9)
     * @param value valor (1-9)
     * @return true se a jogada for válida
     */
    public boolean isValidMove(int row, int col, int value) {
        if (currentGame == null) {
            return false;
        }
        
        int boardRow = row - 1;
        int boardCol = col - 1;
        
        if (boardRow < 0 || boardRow >= 9 || boardCol < 0 || boardCol >= 9) {
            return false;
        }
        
        return solver.validateMove(currentGame.getBoard(), boardRow, boardCol, value);
    }
    
    /**
     * Obtém os valores possíveis para uma célula.
     * 
     * @param row linha (1-9)
     * @param col coluna (1-9)
     * @return array com valores possíveis
     */
    public int[] getPossibleValues(int row, int col) {
        if (currentGame == null) {
            return new int[0];
        }
        
        int boardRow = row - 1;
        int boardCol = col - 1;
        
        if (boardRow < 0 || boardRow >= 9 || boardCol < 0 || boardCol >= 9) {
            return new int[0];
        }
        
        return solver.getPossibleValues(currentGame.getBoard(), boardRow, boardCol);
    }
    
    /**
     * Obtém o estado atual do jogo.
     * 
     * @return estado do jogo
     */
    public GameState getCurrentGame() {
        return currentGame;
    }
    
    /**
     * Verifica se há um jogo em andamento.
     * 
     * @return true se há jogo ativo
     */
    public boolean hasActiveGame() {
        return currentGame != null && 
               (currentGame.getStatus() == Status.JOGANDO || 
                currentGame.getStatus() == Status.PAUSADO);
    }
    
    /**
     * Obtém estatísticas do jogo atual.
     * 
     * @return string formatada com estatísticas
     */
    public String getGameStatistics() {
        if (currentGame == null) {
            return "Nenhum jogo ativo.";
        }
        
        return currentGame.getGameStats();
    }
    
    /**
     * Classe para representar o resultado de uma jogada.
     */
    public static class MoveResult {
        private final boolean success;
        private final String message;
        
        public MoveResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return message;
        }
    }
    
    /**
     * Classe para representar o resultado de uma dica.
     */
    public static class HintResult {
        private final boolean success;
        private final String message;
        private final int row;
        private final int col;
        private final int value;
        
        public HintResult(boolean success, String message, int row, int col, int value) {
            this.success = success;
            this.message = message;
            this.row = row;
            this.col = col;
            this.value = value;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public int getRow() {
            return row;
        }
        
        public int getCol() {
            return col;
        }
        
        public int getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return message;
        }
    }
}