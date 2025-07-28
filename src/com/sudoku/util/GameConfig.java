package com.sudoku.util;

/**
 * Configurações e constantes do jogo de Sudoku.
 */
public class GameConfig {
    
    // Configurações do jogo
    public static final String GAME_TITLE = "Jogo de Sudoku";
    public static final String GAME_VERSION = "1.0.0";
    public static final String GAME_AUTHOR = "Desenvolvedor Senior";
    
    // Configurações do tabuleiro
    public static final int BOARD_SIZE = 9;
    public static final int SUBGRID_SIZE = 3;
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 9;
    public static final int EMPTY_CELL_VALUE = 0;
    
    // Configurações de pontuação
    public static final int POINTS_PER_CORRECT_MOVE = 10;
    public static final int PENALTY_PER_ERROR = 5;
    public static final int PENALTY_PER_HINT = 15;
    public static final int TIME_BONUS_BASE = 1000;
    public static final int ERROR_PENALTY_MULTIPLIER = 20;
    public static final int HINT_PENALTY_MULTIPLIER = 10;
    
    // Configurações de dificuldade
    public static final int EASY_FILLED_CELLS = 40;
    public static final int MEDIUM_FILLED_CELLS = 30;
    public static final int HARD_FILLED_CELLS = 20;
    public static final int EXPERT_FILLED_CELLS = 17;
    
    // Configurações de tempo
    public static final int FAST_GAME_THRESHOLD = 600;  // 10 minutos
    public static final int LONG_GAME_THRESHOLD = 1800; // 30 minutos
    
    // Configurações de interface
    public static final boolean USE_COLORS = true;
    public static final boolean SHOW_POSSIBLE_VALUES = true;
    public static final boolean AUTO_SAVE = false;
    
    // Mensagens do jogo
    public static final String WELCOME_MESSAGE = "Bem-vindo ao " + GAME_TITLE + "!";
    public static final String GOODBYE_MESSAGE = "Obrigado por jogar! Até a próxima!";
    public static final String INVALID_MOVE_MESSAGE = "Jogada inválida!";
    public static final String VICTORY_MESSAGE = "Parabéns! Você resolveu o Sudoku!";
    public static final String GAME_OVER_MESSAGE = "Jogo encerrado.";
    
    // Configurações de validação
    public static final int MAX_ATTEMPTS_PUZZLE_GENERATION = 1000;
    public static final int MAX_HINTS_PER_GAME = 10;
    public static final boolean ALLOW_INVALID_MOVES = false;
    
    // Configurações de display
    public static final String CELL_SEPARATOR = " ";
    public static final String ROW_SEPARATOR = "─";
    public static final String COL_SEPARATOR = "│";
    public static final String CORNER_CHAR = "┼";
    public static final String EMPTY_CELL_DISPLAY = "·";
    
    // Códigos ANSI para cores (podem ser desabilitados)
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    
    // Configurações de arquivo (para futuras implementações)
    public static final String SAVE_FILE_EXTENSION = ".sudoku";
    public static final String CONFIG_FILE_NAME = "sudoku.config";
    public static final String STATS_FILE_NAME = "sudoku.stats";
    
    /**
     * Verifica se as cores devem ser usadas na interface.
     * 
     * @return true se cores devem ser usadas
     */
    public static boolean shouldUseColors() {
        return USE_COLORS && isColorSupported();
    }
    
    /**
     * Verifica se o terminal suporta cores ANSI.
     * 
     * @return true se cores são suportadas
     */
    private static boolean isColorSupported() {
        String term = System.getenv("TERM");
        String os = System.getProperty("os.name");
        
        // Verificação básica para suporte a cores
        return term != null && !term.equals("dumb") && 
               (os.contains("Windows") || term.contains("color") || term.contains("xterm"));
    }
    
    /**
     * Obtém a cor ANSI apropriada ou string vazia se cores estão desabilitadas.
     * 
     * @param ansiCode código ANSI da cor
     * @return código ANSI ou string vazia
     */
    public static String getColor(String ansiCode) {
        return shouldUseColors() ? ansiCode : "";
    }
    
    /**
     * Obtém o número máximo de dicas baseado na dificuldade.
     * 
     * @param filledCells número de células preenchidas inicialmente
     * @return número máximo de dicas
     */
    public static int getMaxHints(int filledCells) {
        if (filledCells >= EASY_FILLED_CELLS) {
            return 3; // Fácil
        } else if (filledCells >= MEDIUM_FILLED_CELLS) {
            return 5; // Médio
        } else if (filledCells >= HARD_FILLED_CELLS) {
            return 7; // Difícil
        } else {
            return MAX_HINTS_PER_GAME; // Expert
        }
    }
    
    /**
     * Calcula a pontuação inicial baseada na dificuldade.
     * 
     * @param filledCells número de células preenchidas
     * @return pontuação inicial
     */
    public static int calculateInitialScore(int filledCells) {
        return filledCells * POINTS_PER_CORRECT_MOVE;
    }
    
    /**
     * Verifica se uma coordenada é válida.
     * 
     * @param coordinate coordenada a verificar
     * @return true se válida
     */
    public static boolean isValidCoordinate(int coordinate) {
        return coordinate >= 1 && coordinate <= BOARD_SIZE;
    }
    
    /**
     * Verifica se um valor de Sudoku é válido.
     * 
     * @param value valor a verificar
     * @return true se válido
     */
    public static boolean isValidSudokuValue(int value) {
        return value >= EMPTY_CELL_VALUE && value <= MAX_VALUE;
    }
    
    /**
     * Obtém mensagem de ajuda para comandos.
     * 
     * @return string com ajuda
     */
    public static String getHelpMessage() {
        return "Comandos disponíveis:\n" +
               "  - Para fazer uma jogada: linha coluna valor (ex: 3 7 5)\n" +
               "  - Para limpar uma célula: linha coluna 0 (ex: 3 7 0)\n" +
               "  - Digite 'cancelar' ou 'voltar' para retornar ao menu\n" +
               "  - Use os números do menu para navegar pelas opções";
    }
    
    /**
     * Obtém informações sobre o jogo.
     * 
     * @return string com informações
     */
    public static String getGameInfo() {
        return GAME_TITLE + " v" + GAME_VERSION + "\n" +
               "Desenvolvido por: " + GAME_AUTHOR + "\n" +
               "Um jogo clássico de Sudoku implementado em Java";
    }
    
    // Configurações para desenvolvimento/debug
    public static final boolean DEBUG_MODE = false;
    public static final boolean SHOW_SOLVE_STEPS = false;
    public static final boolean ENABLE_CHEAT_CODES = false;
    
    /**
     * Método para debug - só executa se DEBUG_MODE estiver ativo.
     * 
     * @param message mensagem de debug
     */
    public static void debugPrint(String message) {
        if (DEBUG_MODE) {
            System.out.println("[DEBUG] " + message);
        }
    }
}