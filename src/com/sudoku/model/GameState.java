package com.sudoku.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa o estado atual de um jogo de Sudoku.
 * Contém informações sobre o tabuleiro, tempo, pontuação e status do jogo.
 */
public class GameState {
    
    public enum Difficulty {
        FACIL(40, "Fácil"),
        MEDIO(30, "Médio"),
        DIFICIL(20, "Difícil"),
        EXPERT(17, "Expert");
        
        private final int filledCells;
        private final String description;
        
        Difficulty(int filledCells, String description) {
            this.filledCells = filledCells;
            this.description = description;
        }
        
        public int getFilledCells() {
            return filledCells;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Status {
        NOVO,
        JOGANDO,
        PAUSADO,
        CONCLUIDO,
        ABANDONADO
    }
    
    private Board board;
    private Board originalBoard;
    private Difficulty difficulty;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long elapsedSeconds;
    private int moves;
    private int hints;
    private int errors;
    private int score;
    
    /**
     * Construtor padrão que cria um novo jogo.
     */
    public GameState() {
        this.board = new Board();
        this.difficulty = Difficulty.FACIL;
        this.status = Status.NOVO;
        this.elapsedSeconds = 0;
        this.moves = 0;
        this.hints = 0;
        this.errors = 0;
        this.score = 0;
    }
    
    /**
     * Construtor que cria um jogo com dificuldade específica.
     * 
     * @param difficulty nível de dificuldade
     */
    public GameState(Difficulty difficulty) {
        this();
        this.difficulty = difficulty;
    }
    
    /**
     * Inicia um novo jogo com o tabuleiro especificado.
     * 
     * @param initialBoard tabuleiro inicial
     */
    public void startNewGame(Board initialBoard) {
        this.board = initialBoard.copy();
        this.originalBoard = initialBoard.copy();
        this.status = Status.JOGANDO;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
        this.elapsedSeconds = 0;
        this.moves = 0;
        this.hints = 0;
        this.errors = 0;
        this.score = calculateInitialScore();
    }
    
    /**
     * Calcula a pontuação inicial baseada na dificuldade.
     * 
     * @return pontuação inicial
     */
    private int calculateInitialScore() {
        return difficulty.getFilledCells() * 10;
    }
    
    /**
     * Executa uma jogada no tabuleiro.
     * 
     * @param row linha
     * @param col coluna
     * @param value valor
     * @return true se a jogada foi válida
     */
    public boolean makeMove(int row, int col, int value) {
        if (status != Status.JOGANDO) {
            return false;
        }
        
        Cell cell = board.getCell(row, col);
        if (cell.isFixed()) {
            return false;
        }
        
        boolean wasValid = board.setValue(row, col, value);
        
        if (wasValid) {
            moves++;
            
            // Verificar se a jogada está correta comparando com a solução
            // (assumindo que temos uma forma de verificar isso)
            if (value != 0 && !isCorrectMove(row, col, value)) {
                errors++;
                score = Math.max(0, score - 5); // Penalidade por erro
            } else if (value != 0) {
                score += 10; // Pontos por jogada correta
            }
            
            // Verificar se o jogo foi concluído
            if (board.isSolved()) {
                finishGame();
            }
        }
        
        return wasValid;
    }
    
    /**
     * Verifica se uma jogada está correta (implementação simplificada).
     * Em um jogo real, isso compararia com a solução conhecida.
     * 
     * @param row linha
     * @param col coluna
     * @param value valor
     * @return true se a jogada estiver correta
     */
    private boolean isCorrectMove(int row, int col, int value) {
        // Implementação simplificada: apenas verifica se a jogada é válida
        // Em uma implementação completa, compararia com a solução conhecida
        return board.isValidMove(row, col, value);
    }
    
    /**
     * Finaliza o jogo com sucesso.
     */
    private void finishGame() {
        this.status = Status.CONCLUIDO;
        this.endTime = LocalDateTime.now();
        
        // Bônus por completar o jogo
        int timeBonus = Math.max(0, 1000 - (int) getElapsedTime());
        int errorPenalty = errors * 20;
        int hintPenalty = hints * 10;
        
        score = score + timeBonus - errorPenalty - hintPenalty;
        score = Math.max(0, score);
    }
    
    /**
     * Pausa o jogo.
     */
    public void pauseGame() {
        if (status == Status.JOGANDO) {
            status = Status.PAUSADO;
            updateElapsedTime();
        }
    }
    
    /**
     * Resume o jogo pausado.
     */
    public void resumeGame() {
        if (status == Status.PAUSADO) {
            status = Status.JOGANDO;
            startTime = LocalDateTime.now().minusSeconds(elapsedSeconds);
        }
    }
    
    /**
     * Abandona o jogo atual.
     */
    public void abandonGame() {
        this.status = Status.ABANDONADO;
        this.endTime = LocalDateTime.now();
        updateElapsedTime();
    }
    
    /**
     * Atualiza o tempo decorrido.
     */
    private void updateElapsedTime() {
        if (startTime != null) {
            LocalDateTime currentTime = endTime != null ? endTime : LocalDateTime.now();
            elapsedSeconds = java.time.Duration.between(startTime, currentTime).getSeconds();
        }
    }
    
    /**
     * Usa uma dica (revela uma célula).
     * 
     * @param row linha
     * @param col coluna
     * @return true se a dica foi usada com sucesso
     */
    public boolean useHint(int row, int col) {
        if (status != Status.JOGANDO) {
            return false;
        }
        
        Cell cell = board.getCell(row, col);
        if (cell.isFixed() || !cell.isEmpty()) {
            return false;
        }
        
        // Aqui seria necessário ter acesso à solução do puzzle
        // Por enquanto, vamos simular uma dica válida
        hints++;
        score = Math.max(0, score - 15); // Penalidade por usar dica
        
        return true;
    }
    
    /**
     * Obtém o tempo decorrido em segundos.
     * 
     * @return tempo decorrido
     */
    public long getElapsedTime() {
        if (status == Status.JOGANDO || status == Status.PAUSADO) {
            updateElapsedTime();
        }
        return elapsedSeconds;
    }
    
    /**
     * Formata o tempo decorrido como string.
     * 
     * @return tempo formatado (MM:SS)
     */
    public String getFormattedTime() {
        long totalSeconds = getElapsedTime();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    // Getters e Setters
    
    public Board getBoard() {
        return board;
    }
    
    public Board getOriginalBoard() {
        return originalBoard;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public int getMoves() {
        return moves;
    }
    
    public int getHints() {
        return hints;
    }
    
    public int getErrors() {
        return errors;
    }
    
    public int getScore() {
        return score;
    }
    
    /**
     * Verifica se o jogo está em andamento.
     * 
     * @return true se o jogo estiver sendo jogado
     */
    public boolean isPlaying() {
        return status == Status.JOGANDO;
    }
    
    /**
     * Verifica se o jogo foi concluído com sucesso.
     * 
     * @return true se o jogo foi resolvido
     */
    public boolean isCompleted() {
        return status == Status.CONCLUIDO;
    }
    
    /**
     * Verifica se o jogo está pausado.
     * 
     * @return true se o jogo estiver pausado
     */
    public boolean isPaused() {
        return status == Status.PAUSADO;
    }
    
    /**
     * Obtém estatísticas do jogo como string formatada.
     * 
     * @return string com estatísticas do jogo
     */
    public String getGameStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("Dificuldade: ").append(difficulty.getDescription()).append("\n");
        stats.append("Status: ").append(getStatusDescription()).append("\n");
        stats.append("Tempo: ").append(getFormattedTime()).append("\n");
        stats.append("Jogadas: ").append(moves).append("\n");
        stats.append("Erros: ").append(errors).append("\n");
        stats.append("Dicas: ").append(hints).append("\n");
        stats.append("Pontuação: ").append(score).append("\n");
        
        if (startTime != null) {
            stats.append("Iniciado em: ").append(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
        
        return stats.toString();
    }
    
    /**
     * Obtém a descrição do status atual.
     * 
     * @return descrição do status
     */
    private String getStatusDescription() {
        switch (status) {
            case NOVO: return "Novo";
            case JOGANDO: return "Em andamento";
            case PAUSADO: return "Pausado";
            case CONCLUIDO: return "Concluído";
            case ABANDONADO: return "Abandonado";
            default: return "Desconhecido";
        }
    }
    
    /**
     * Reseta o jogo para o estado inicial.
     */
    public void reset() {
        if (originalBoard != null) {
            this.board = originalBoard.copy();
            this.status = Status.JOGANDO;
            this.startTime = LocalDateTime.now();
            this.endTime = null;
            this.elapsedSeconds = 0;
            this.moves = 0;
            this.hints = 0;
            this.errors = 0;
            this.score = calculateInitialScore();
        }
    }
    
    @Override
    public String toString() {
        return "GameState{" +
                "difficulty=" + difficulty +
                ", status=" + status +
                ", time=" + getFormattedTime() +
                ", moves=" + moves +
                ", score=" + score +
                '}';
    }
}