package com.sudoku.view;

import com.sudoku.model.GameState;
import com.sudoku.model.GameState.Difficulty;
import com.sudoku.service.GameManager;
import com.sudoku.service.GameManager.HintResult;
import com.sudoku.service.GameManager.MoveResult;
import com.sudoku.util.GameConfig;
import com.sudoku.util.InputValidator;
import com.sudoku.util.TimeFormatter;

import java.util.Scanner;

/**
 * Controlador principal do jogo de Sudoku.
 * Gerencia a interação entre o usuário, a interface e a lógica do jogo.
 */
public class GameController {
    
    private final Scanner scanner;
    private final ConsoleView view;
    private final InputValidator inputValidator;
    private final GameManager gameManager;
    private boolean isRunning;
    
    /**
     * Construtor do controlador.
     */
    public GameController() {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
        this.inputValidator = new InputValidator(scanner);
        this.gameManager = new GameManager();
        this.isRunning = true;
    }
    
    /**
     * Inicia o jogo e executa o loop principal.
     */
    public void start() {
        view.clearScreen();
        view.displayTitle();
        view.displayInfoMessage(GameConfig.WELCOME_MESSAGE);
        view.displayBlankLine();
        
        while (isRunning) {
            try {
                showMainMenu();
            } catch (Exception e) {
                view.displayErrorMessage("Erro inesperado: " + e.getMessage());
                GameConfig.debugPrint("Erro no loop principal: " + e.toString());
                view.waitForEnter();
                inputValidator.waitForEnter();
            }
        }
        
        cleanup();
    }
    
    /**
     * Exibe e processa o menu principal.
     */
    private void showMainMenu() {
        view.clearScreen();
        view.displayTitle();
        
        // Mostrar informações do jogo atual se houver
        if (gameManager.hasActiveGame()) {
            view.displaySectionHeader("Jogo Atual");
            view.displayGameInfo(gameManager.getCurrentGame());
            view.displayBlankLine();
        }
        
        view.displayMainMenu();
        
        int choice = inputValidator.readInt(1, 5, "");
        
        switch (choice) {
            case 1:
                startNewGame();
                break;
            case 2:
                continueGame();
                break;
            case 3:
                showInstructions();
                break;
            case 4:
                showStatistics();
                break;
            case 5:
                exitGame();
                break;
        }
    }
    
    /**
     * Inicia um novo jogo.
     */
    private void startNewGame() {
        view.clearScreen();
        view.displayTitle();
        view.displayDifficultyMenu();
        
        int difficultyChoice = inputValidator.readInt(1, 4, "");
        Difficulty difficulty = getDifficultyFromChoice(difficultyChoice);
        
        view.displayLoadingMessage("Gerando novo puzzle");
        
        boolean success = gameManager.startNewGame(difficulty);
        
        if (success) {
            view.displaySuccessMessage("Novo jogo iniciado com sucesso!");
            playGame();
        } else {
            view.displayErrorMessage("Erro ao iniciar novo jogo. Tente novamente.");
            view.waitForEnter();
            inputValidator.waitForEnter();
        }
    }
    
    /**
     * Converte escolha numérica em dificuldade.
     * 
     * @param choice escolha do usuário (1-4)
     * @return dificuldade correspondente
     */
    private Difficulty getDifficultyFromChoice(int choice) {
        switch (choice) {
            case 1: return Difficulty.FACIL;
            case 2: return Difficulty.MEDIO;
            case 3: return Difficulty.DIFICIL;
            case 4: return Difficulty.EXPERT;
            default: return Difficulty.FACIL;
        }
    }
    
    /**
     * Continua um jogo existente.
     */
    private void continueGame() {
        if (!gameManager.hasActiveGame()) {
            view.displayWarningMessage("Não há jogo ativo para continuar.");
            view.displayInfoMessage("Inicie um novo jogo primeiro.");
            view.waitForEnter();
            inputValidator.waitForEnter();
            return;
        }
        
        GameState game = gameManager.getCurrentGame();
        if (game.isPaused()) {
            gameManager.resumeGame();
            view.displaySuccessMessage("Jogo resumido!");
        }
        
        playGame();
    }
    
    /**
     * Loop principal do jogo.
     */
    private void playGame() {
        while (gameManager.hasActiveGame() && gameManager.getCurrentGame().isPlaying()) {
            try {
                view.clearScreen();
                view.displayTitle();
                
                GameState currentGame = gameManager.getCurrentGame();
                view.displayGameInfo(currentGame);
                view.displayBoard(currentGame.getBoard());
                
                // Verificar se o jogo foi concluído
                if (currentGame.isCompleted()) {
                    view.displayVictoryMessage(currentGame);
                    view.waitForEnter();
                    inputValidator.waitForEnter();
                    break;
                }
                
                view.displayGameMenu();
                
                int choice = inputValidator.readInt(1, 7, "");
                
                switch (choice) {
                    case 1:
                        makeMove();
                        break;
                    case 2:
                        getHint();
                        break;
                    case 3:
                        togglePauseResume();
                        break;
                    case 4:
                        showGameStatistics();
                        break;
                    case 5:
                        resetGame();
                        break;
                    case 6:
                        solvePuzzle();
                        break;
                    case 7:
                        return; // Voltar ao menu principal
                }
                
            } catch (Exception e) {
                view.displayErrorMessage("Erro durante o jogo: " + e.getMessage());
                GameConfig.debugPrint("Erro no loop do jogo: " + e.toString());
                view.waitForEnter();
                inputValidator.waitForEnter();
            }
        }
    }
    
    /**
     * Processa uma jogada do usuário.
     */
    private void makeMove() {
        view.displaySectionHeader("Fazer Jogada");
        view.displayInfoMessage("Digite as coordenadas e o valor (linha coluna valor)");
        view.displayInfoMessage("Exemplo: 3 7 5 (coloca 5 na linha 3, coluna 7)");
        view.displayInfoMessage("Use 0 como valor para limpar uma célula");
        view.displayBlankLine();
        
        int[] move = inputValidator.readMove("Jogada: ");
        
        if (move == null) {
            view.displayInfoMessage("Jogada cancelada.");
            return;
        }
        
        int row = move[0];
        int col = move[1];
        int value = move[2];
        
        // Mostrar valores possíveis se for uma célula vazia
        if (value == 0 || !gameManager.isValidMove(row, col, value)) {
            int[] possibleValues = gameManager.getPossibleValues(row, col);
            view.displayPossibleValues(row, col, possibleValues);
        }
        
        MoveResult result = gameManager.makeMove(row, col, value);
        
        if (result.isSuccess()) {
            view.displaySuccessMessage(result.getMessage());
            
            // Mostrar tabuleiro atualizado brevemente
            if (value != 0) {
                view.displayBoardWithHighlight(gameManager.getCurrentGame().getBoard(), row, col);
                try {
                    Thread.sleep(1000); // Pausa para mostrar a jogada
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            view.displayErrorMessage(result.getMessage());
            view.waitForEnter();
            inputValidator.waitForEnter();
        }
    }
    
    /**
     * Solicita uma dica para o jogador.
     */
    private void getHint() {
        view.displaySectionHeader("Obter Dica");
        
        boolean confirm = inputValidator.readYesNo("Tem certeza que deseja usar uma dica? (Isso reduzirá sua pontuação)");
        
        if (!confirm) {
            view.displayInfoMessage("Dica cancelada.");
            return;
        }
        
        HintResult hint = gameManager.getHint();
        
        if (hint.isSuccess()) {
            view.displaySuccessMessage(hint.getMessage());
            
            // Destacar a célula da dica no tabuleiro
            if (hint.getRow() > 0 && hint.getCol() > 0) {
                view.displayBoardWithHighlight(gameManager.getCurrentGame().getBoard(), 
                                             hint.getRow(), hint.getCol());
            }
            
            view.waitForEnter();
            inputValidator.waitForEnter();
        } else {
            view.displayErrorMessage(hint.getMessage());
            view.waitForEnter();
            inputValidator.waitForEnter();
        }
    }
    
    /**
     * Alterna entre pausar e resumir o jogo.
     */
    private void togglePauseResume() {
        GameState currentGame = gameManager.getCurrentGame();
        
        if (currentGame.isPlaying()) {
            boolean success = gameManager.pauseGame();
            if (success) {
                view.displayInfoMessage("Jogo pausado.");
                view.displayWarningMessage("O tempo foi pausado. Escolha 'Continuar Jogo' para resumir.");
                view.waitForEnter();
                inputValidator.waitForEnter();
            }
        } else if (currentGame.isPaused()) {
            boolean success = gameManager.resumeGame();
            if (success) {
                view.displaySuccessMessage("Jogo resumido! O tempo continua correndo.");
            }
        }
    }
    
    /**
     * Exibe estatísticas detalhadas do jogo atual.
     */
    private void showGameStatistics() {
        view.clearScreen();
        view.displayTitle();
        view.displaySectionHeader("Estatísticas do Jogo");
        
        GameState currentGame = gameManager.getCurrentGame();
        
        // Estatísticas básicas
        view.displayGameInfo(currentGame);
        
        // Estatísticas calculadas
        long elapsedTime = currentGame.getElapsedTime();
        String timeRating = TimeFormatter.getTimeRating(elapsedTime);
        String avgTimePerMove = TimeFormatter.formatAverageTimePerMove(elapsedTime, currentGame.getMoves());
        
        view.displayBlankLine();
        System.out.println("📊 Estatísticas Detalhadas:");
        System.out.println("⏱️  Classificação do tempo: " + timeRating);
        System.out.println("📈 Tempo médio por jogada: " + avgTimePerMove);
        System.out.println("🎯 Taxa de acerto: " + calculateAccuracyRate(currentGame) + "%");
        System.out.println("💡 Eficiência: " + calculateEfficiency(currentGame));
        
        // Progresso do tabuleiro
        int totalCells = 81;
        int filledCells = currentGame.getBoard().getFilledCells();
        int remainingCells = totalCells - filledCells;
        
        view.displayBlankLine();
        System.out.println("📋 Progresso do Tabuleiro:");
        System.out.println("✅ Células preenchidas: " + filledCells + "/" + totalCells);
        System.out.println("⭕ Células restantes: " + remainingCells);
        
        view.waitForEnter();
        inputValidator.waitForEnter();
    }
    
    /**
     * Calcula a taxa de acerto do jogador.
     * 
     * @param game estado do jogo
     * @return taxa de acerto em percentual
     */
    private double calculateAccuracyRate(GameState game) {
        int totalMoves = game.getMoves();
        if (totalMoves == 0) return 100.0;
        
        int correctMoves = totalMoves - game.getErrors();
        return (correctMoves * 100.0) / totalMoves;
    }
    
    /**
     * Calcula a eficiência do jogador.
     * 
     * @param game estado do jogo
     * @return classificação da eficiência
     */
    private String calculateEfficiency(GameState game) {
        double accuracy = calculateAccuracyRate(game);
        int hintsUsed = game.getHints();
        
        if (accuracy >= 95 && hintsUsed <= 1) {
            return "Excelente 🌟";
        } else if (accuracy >= 85 && hintsUsed <= 3) {
            return "Boa 👍";
        } else if (accuracy >= 70 && hintsUsed <= 5) {
            return "Regular 👌";
        } else {
            return "Precisa melhorar 📚";
        }
    }
    
    /**
     * Reseta o jogo atual.
     */
    private void resetGame() {
        view.displaySectionHeader("Resetar Jogo");
        
        boolean confirm = inputValidator.readYesNo("Tem certeza que deseja resetar o jogo? Todo o progresso será perdido.");
        
        if (confirm) {
            boolean success = gameManager.resetGame();
            if (success) {
                view.displaySuccessMessage("Jogo resetado com sucesso!");
                view.displayInfoMessage("O tabuleiro foi restaurado ao estado inicial.");
            } else {
                view.displayErrorMessage("Erro ao resetar o jogo.");
            }
        } else {
            view.displayInfoMessage("Reset cancelado.");
        }
        
        view.waitForEnter();
        inputValidator.waitForEnter();
    }
    
    /**
     * Resolve o puzzle automaticamente.
     */
    private void solvePuzzle() {
        view.displaySectionHeader("Resolver Automaticamente");
        view.displayWarningMessage("Esta ação irá resolver o puzzle automaticamente.");
        view.displayWarningMessage("Sua pontuação será zerada e o jogo será marcado como resolvido automaticamente.");
        
        boolean confirm = inputValidator.readYesNo("Tem certeza que deseja continuar?");
        
        if (confirm) {
            view.displayLoadingMessage("Resolvendo puzzle");
            
            boolean success = gameManager.solvePuzzle();
            
            if (success) {
                view.displaySuccessMessage("Puzzle resolvido automaticamente!");
                view.displayBoard(gameManager.getCurrentGame().getBoard());
                view.displayInfoMessage("Jogo concluído. Pontuação: 0");
            } else {
                view.displayErrorMessage("Não foi possível resolver o puzzle automaticamente.");
            }
        } else {
            view.displayInfoMessage("Resolução automática cancelada.");
        }
        
        view.waitForEnter();
        inputValidator.waitForEnter();
    }
    
    /**
     * Exibe as instruções do jogo.
     */
    private void showInstructions() {
        view.displayInstructions();
        inputValidator.waitForEnter();
    }
    
    /**
     * Exibe estatísticas gerais (placeholder para futuras implementações).
     */
    private void showStatistics() {
        view.clearScreen();
        view.displayTitle();
        view.displaySectionHeader("Estatísticas Gerais");
        
        // Estatísticas do jogo atual
        if (gameManager.hasActiveGame()) {
            view.displayInfoMessage("Estatísticas do jogo atual:");
            System.out.println(gameManager.getGameStatistics());
        } else {
            view.displayInfoMessage("Nenhum jogo ativo no momento.");
        }
        
        view.displayBlankLine();
        
        // Informações do sistema
        view.displayInfoMessage("Informações do Jogo:");
        System.out.println(GameConfig.getGameInfo());
        
        view.displayBlankLine();
        view.displayInfoMessage("Configurações:");
        System.out.println("• Suporte a cores: " + (GameConfig.shouldUseColors() ? "Ativado" : "Desativado"));
        System.out.println("• Modo debug: " + (GameConfig.DEBUG_MODE ? "Ativado" : "Desativado"));
        System.out.println("• Mostrar valores possíveis: " + (GameConfig.SHOW_POSSIBLE_VALUES ? "Sim" : "Não"));
        
        view.waitForEnter();
        inputValidator.waitForEnter();
    }
    
    /**
     * Processa a saída do jogo.
     */
    private void exitGame() {
        view.displaySectionHeader("Sair do Jogo");
        
        // Verificar se há jogo ativo
        if (gameManager.hasActiveGame() && gameManager.getCurrentGame().isPlaying()) {
            view.displayWarningMessage("Há um jogo em andamento!");
            boolean saveAndExit = inputValidator.readYesNo("Deseja pausar o jogo antes de sair?");
            
            if (saveAndExit) {
                gameManager.pauseGame();
                view.displaySuccessMessage("Jogo pausado. Você pode continuar na próxima vez.");
            } else {
                boolean confirmExit = inputValidator.readYesNo("Tem certeza que deseja sair sem salvar?");
                if (!confirmExit) {
                    return; // Voltar ao menu
                }
                gameManager.abandonGame();
                view.displayInfoMessage("Jogo abandonado.");
            }
        }
        
        boolean confirmExit = inputValidator.readYesNo("Tem certeza que deseja sair do jogo?");
        
        if (confirmExit) {
            view.displayGoodbyeMessage();
            isRunning = false;
        }
    }
    
    /**
     * Limpa recursos e finaliza o jogo.
     */
    private void cleanup() {
        try {
            // Fechar scanner
            if (scanner != null) {
                scanner.close();
            }
            
            // Log de finalização (se debug estiver ativo)
            GameConfig.debugPrint("Jogo finalizado com sucesso");
            
        } catch (Exception e) {
            System.err.println("Erro durante limpeza: " + e.getMessage());
        }
    }
    
    /**
     * Método para tratamento de erro geral.
     * 
     * @param e exceção capturada
     * @param context contexto onde ocorreu o erro
     */
    private void handleError(Exception e, String context) {
        view.displayErrorMessage("Erro em " + context + ": " + e.getMessage());
        GameConfig.debugPrint("Erro detalhado em " + context + ": " + e.toString());
        
        if (e instanceof RuntimeException) {
            view.displayWarningMessage("Erro crítico detectado. Recomenda-se reiniciar o jogo.");
        }
        
        view.waitForEnter();
        inputValidator.waitForEnter();
    }
    
    /**
     * Método para validar estado do jogo antes de operações críticas.
     * 
     * @return true se o estado do jogo está válido
     */
    private boolean validateGameState() {
        try {
            if (gameManager == null) {
                view.displayErrorMessage("Gerenciador de jogo não inicializado.");
                return false;
            }
            
            if (gameManager.hasActiveGame()) {
                GameState currentGame = gameManager.getCurrentGame();
                if (currentGame == null) {
                    view.displayErrorMessage("Estado do jogo inválido.");
                    return false;
                }
                
                if (currentGame.getBoard() == null) {
                    view.displayErrorMessage("Tabuleiro não inicializado.");
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            handleError(e, "validação do estado do jogo");
            return false;
        }
    }
    
    /**
     * Exibe menu de opções avançadas (para desenvolvimento/debug).
     */
    private void showAdvancedOptions() {
        if (!GameConfig.DEBUG_MODE) {
            return;
        }
        
        view.displaySectionHeader("Opções Avançadas (Debug)");
        System.out.println("1. Validar estado do jogo");
        System.out.println("2. Mostrar informações técnicas");
        System.out.println("3. Forçar coleta de lixo");
        System.out.println("4. Voltar");
        
        int choice = inputValidator.readInt(1, 4, "Escolha: ");
        
        switch (choice) {
            case 1:
                boolean valid = validateGameState();
                view.displayInfoMessage("Estado do jogo: " + (valid ? "Válido" : "Inválido"));
                break;
                
            case 2:
                showTechnicalInfo();
                break;
                
            case 3:
                System.gc();
                view.displayInfoMessage("Coleta de lixo executada.");
                break;
                
            case 4:
                return;
        }
        
        view.waitForEnter();
        inputValidator.waitForEnter();
    }
    
    /**
     * Exibe informações técnicas do sistema.
     */
    private void showTechnicalInfo() {
        view.displaySectionHeader("Informações Técnicas");
        
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("Sistema:");
        System.out.println("• Java Version: " + System.getProperty("java.version"));
        System.out.println("• OS: " + System.getProperty("os.name"));
        System.out.println("• Memória Total: " + (totalMemory / 1024 / 1024) + " MB");
        System.out.println("• Memória Usada: " + (usedMemory / 1024 / 1024) + " MB");
        System.out.println("• Memória Livre: " + (freeMemory / 1024 / 1024) + " MB");
        
        view.displayBlankLine();
        
        if (gameManager.hasActiveGame()) {
            GameState game = gameManager.getCurrentGame();
            System.out.println("Jogo Atual:");
            System.out.println("• Status: " + game.getStatus());
            System.out.println("• Células preenchidas: " + game.getBoard().getFilledCells());
            System.out.println("• Tempo decorrido: " + game.getFormattedTime());
        }
    }
    
    /**
     * Método principal de execução do controlador.
     * Ponto de entrada alternativo com tratamento de erro.
     */
    public void run() {
        try {
            start();
        } catch (Exception e) {
            handleError(e, "execução principal");
            view.displayErrorMessage("Erro crítico. O jogo será encerrado.");
            cleanup();
        }
    }
}