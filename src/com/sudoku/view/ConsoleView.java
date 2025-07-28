package com.sudoku.view;

import com.sudoku.model.Board;
import com.sudoku.model.Cell;
import com.sudoku.model.GameState;
import com.sudoku.model.GameState.Difficulty;

/**
 * Interface de console para o jogo de Sudoku.
 * Responsável por exibir o tabuleiro e informações do jogo no terminal.
 */
public class ConsoleView {
    
    // Códigos ANSI para cores
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Códigos para formatação
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    
    /**
     * Limpa a tela do console.
     */
    public void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }
    
    /**
     * Exibe o título do jogo.
     */
    public void displayTitle() {
        System.out.println();
        System.out.println(BOLD + BLUE + 
            "╔═══════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + BLUE + 
            "║                    🧩 JOGO DE SUDOKU 🧩                      ║" + RESET);
        System.out.println(BOLD + BLUE + 
            "╚═══════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Exibe o menu principal.
     */
    public void displayMainMenu() {
        System.out.println(BOLD + "═══ MENU PRINCIPAL ═══" + RESET);
        System.out.println("1. " + GREEN + "Novo Jogo" + RESET);
        System.out.println("2. " + YELLOW + "Continuar Jogo" + RESET);
        System.out.println("3. " + BLUE + "Instruções" + RESET);
        System.out.println("4. " + PURPLE + "Estatísticas" + RESET);
        System.out.println("5. " + RED + "Sair" + RESET);
        System.out.println();
        System.out.print("Escolha uma opção (1-5): ");
    }
    
    /**
     * Exibe o menu de dificuldade.
     */
    public void displayDifficultyMenu() {
        System.out.println(BOLD + "═══ ESCOLHA A DIFICULDADE ═══" + RESET);
        System.out.println("1. " + GREEN + "Fácil" + RESET + " (40 números preenchidos)");
        System.out.println("2. " + YELLOW + "Médio" + RESET + " (30 números preenchidos)");
        System.out.println("3. " + RED + "Difícil" + RESET + " (20 números preenchidos)");
        System.out.println("4. " + PURPLE + "Expert" + RESET + " (17 números preenchidos)");
        System.out.println();
        System.out.print("Escolha a dificuldade (1-4): ");
    }
    
    /**
     * Exibe o menu do jogo.
     */
    public void displayGameMenu() {
        System.out.println(BOLD + "═══ OPÇÕES DO JOGO ═══" + RESET);
        System.out.println("1. " + GREEN + "Fazer Jogada" + RESET + " (ex: 3 7 5)");
        System.out.println("2. " + YELLOW + "Obter Dica" + RESET);
        System.out.println("3. " + BLUE + "Pausar/Resumir" + RESET);
        System.out.println("4. " + PURPLE + "Ver Estatísticas" + RESET);
        System.out.println("5. " + CYAN + "Resetar Jogo" + RESET);
        System.out.println("6. " + RED + "Resolver Automaticamente" + RESET);
        System.out.println("7. " + "Voltar ao Menu Principal" + RESET);
        System.out.println();
        System.out.print("Escolha uma opção: ");
    }
    
    /**
     * Exibe o tabuleiro de Sudoku formatado.
     * 
     * @param board tabuleiro a exibir
     */
    public void displayBoard(Board board) {
        System.out.println();
        System.out.println(BOLD + "    1   2   3   4   5   6   7   8   9" + RESET);
        System.out.println("  ┌───────────┬───────────┬───────────┐");
        
        for (int row = 0; row < Board.SIZE; row++) {
            if (row > 0 && row % 3 == 0) {
                System.out.println("  ├───────────┼───────────┼───────────┤");
            }
            
            System.out.print(BOLD + (row + 1) + RESET + " │ ");
            
            for (int col = 0; col < Board.SIZE; col++) {
                if (col > 0 && col % 3 == 0) {
                    System.out.print("│ ");
                }
                
                Cell cell = board.getCell(row, col);
                String cellDisplay = formatCell(cell);
                System.out.print(cellDisplay + " ");
            }
            
            System.out.println("│");
        }
        
        System.out.println("  └───────────┴───────────┴───────────┘");
        System.out.println();
    }
    
    /**
     * Formata uma célula para exibição.
     * 
     * @param cell célula a formatar
     * @return string formatada da célula
     */
    private String formatCell(Cell cell) {
        if (cell.isEmpty()) {
            return CYAN + "·" + RESET;
        } else if (cell.isFixed()) {
            return BOLD + WHITE + cell.getValue() + RESET;
        } else {
            return GREEN + cell.getValue() + RESET;
        }
    }
    
    /**
     * Exibe informações do jogo atual.
     * 
     * @param gameState estado do jogo
     */
    public void displayGameInfo(GameState gameState) {
        System.out.println(BOLD + "═══ INFORMAÇÕES DO JOGO ═══" + RESET);
        System.out.println("Dificuldade: " + YELLOW + gameState.getDifficulty().getDescription() + RESET);
        System.out.println("Status: " + getStatusColor(gameState.getStatus()) + 
                          getStatusText(gameState.getStatus()) + RESET);
        System.out.println("Tempo: " + CYAN + gameState.getFormattedTime() + RESET);
        System.out.println("Jogadas: " + GREEN + gameState.getMoves() + RESET);
        System.out.println("Erros: " + RED + gameState.getErrors() + RESET);
        System.out.println("Dicas: " + PURPLE + gameState.getHints() + RESET);
        System.out.println("Pontuação: " + BOLD + YELLOW + gameState.getScore() + RESET);
        
        // Progresso
        int totalCells = Board.SIZE * Board.SIZE;
        int filledCells = gameState.getBoard().getFilledCells();
        int progress = (filledCells * 100) / totalCells;
        System.out.println("Progresso: " + getProgressBar(progress) + " " + progress + "%");
        System.out.println();
    }
    
    /**
     * Obtém a cor do status do jogo.
     * 
     * @param status status do jogo
     * @return código de cor ANSI
     */
    private String getStatusColor(GameState.Status status) {
        switch (status) {
            case NOVO: return BLUE;
            case JOGANDO: return GREEN;
            case PAUSADO: return YELLOW;
            case CONCLUIDO: return BOLD + GREEN;
            case ABANDONADO: return RED;
            default: return RESET;
        }
    }
    
    /**
     * Obtém o texto do status do jogo.
     * 
     * @param status status do jogo
     * @return texto do status
     */
    private String getStatusText(GameState.Status status) {
        switch (status) {
            case NOVO: return "Novo";
            case JOGANDO: return "Em andamento";
            case PAUSADO: return "Pausado";
            case CONCLUIDO: return "Concluído! 🎉";
            case ABANDONADO: return "Abandonado";
            default: return "Desconhecido";
        }
    }
    
    /**
     * Cria uma barra de progresso visual.
     * 
     * @param percentage percentual de progresso (0-100)
     * @return string com barra de progresso
     */
    private String getProgressBar(int percentage) {
        int filled = percentage / 5; // 20 caracteres = 100%
        StringBuilder bar = new StringBuilder();
        
        bar.append("[");
        for (int i = 0; i < 20; i++) {
            if (i < filled) {
                bar.append(GREEN).append("█").append(RESET);
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        
        return bar.toString();
    }
    
    /**
     * Exibe as instruções do jogo.
     */
    public void displayInstructions() {
        clearScreen();
        displayTitle();
        
        System.out.println(BOLD + "═══ INSTRUÇÕES DO SUDOKU ═══" + RESET);
        System.out.println();
        System.out.println(BOLD + "🎯 OBJETIVO:" + RESET);
        System.out.println("Preencha o tabuleiro 9x9 com números de 1 a 9, seguindo as regras:");
        System.out.println();
        
        System.out.println(BOLD + "📋 REGRAS:" + RESET);
        System.out.println("• Cada " + YELLOW + "linha" + RESET + " deve conter todos os números de 1 a 9");
        System.out.println("• Cada " + BLUE + "coluna" + RESET + " deve conter todos os números de 1 a 9");
        System.out.println("• Cada " + GREEN + "quadrante 3x3" + RESET + " deve conter todos os números de 1 a 9");
        System.out.println("• Números " + BOLD + "não podem se repetir" + RESET + " na mesma linha, coluna ou quadrante");
        System.out.println();
        
        System.out.println(BOLD + "🎮 COMO JOGAR:" + RESET);
        System.out.println("• Para fazer uma jogada, digite: " + CYAN + "linha coluna número" + RESET);
        System.out.println("  Exemplo: " + CYAN + "3 7 5" + RESET + " (coloca o número 5 na linha 3, coluna 7)");
        System.out.println("• Para limpar uma célula, use 0 como número: " + CYAN + "3 7 0" + RESET);
        System.out.println("• Use o menu para obter dicas, pausar ou ver estatísticas");
        System.out.println();
        
        System.out.println(BOLD + "🎨 LEGENDA DO TABULEIRO:" + RESET);
        System.out.println("• " + BOLD + WHITE + "8" + RESET + " - Números fixos (não podem ser alterados)");
        System.out.println("• " + GREEN + "5" + RESET + " - Seus números (podem ser alterados)");
        System.out.println("• " + CYAN + "·" + RESET + " - Células vazias");
        System.out.println();
        
        System.out.println(BOLD + "💡 DICAS:" + RESET);
        System.out.println("• Comece pelas linhas, colunas ou quadrantes com mais números");
        System.out.println("• Procure por células que só podem ter um número possível");
        System.out.println("• Use a opção de dica quando estiver em dúvida");
        System.out.println("• Não tenha pressa - analise bem antes de fazer uma jogada");
        System.out.println();
        
        System.out.print("Pressione Enter para voltar ao menu...");
    }
    
    /**
     * Exibe uma mensagem de sucesso.
     * 
     * @param message mensagem
     */
    public void displaySuccessMessage(String message) {
        System.out.println(GREEN + "✓ " + message + RESET);
    }
    
    /**
     * Exibe uma mensagem de erro.
     * 
     * @param message mensagem
     */
    public void displayErrorMessage(String message) {
        System.out.println(RED + "✗ " + message + RESET);
    }
    
    /**
     * Exibe uma mensagem de informação.
     * 
     * @param message mensagem
     */
    public void displayInfoMessage(String message) {
        System.out.println(BLUE + "ℹ " + message + RESET);
    }
    
    /**
     * Exibe uma mensagem de aviso.
     * 
     * @param message mensagem
     */
    public void displayWarningMessage(String message) {
        System.out.println(YELLOW + "⚠ " + message + RESET);
    }
    
    /**
     * Exibe mensagem de vitória.
     * 
     * @param gameState estado do jogo concluído
     */
    public void displayVictoryMessage(GameState gameState) {
        System.out.println();
        System.out.println(BOLD + GREEN + 
            "🎉═══════════════════════════════════════════════════════════════🎉" + RESET);
        System.out.println(BOLD + GREEN + 
            "🏆                    PARABÉNS! VOCÊ VENCEU!                    🏆" + RESET);
        System.out.println(BOLD + GREEN + 
            "🎉═══════════════════════════════════════════════════════════════🎉" + RESET);
        System.out.println();
        
        System.out.println(BOLD + "📊 ESTATÍSTICAS FINAIS:" + RESET);
        System.out.println("⏱️  Tempo total: " + CYAN + gameState.getFormattedTime() + RESET);
        System.out.println("🎯 Jogadas realizadas: " + GREEN + gameState.getMoves() + RESET);
        System.out.println("❌ Erros cometidos: " + RED + gameState.getErrors() + RESET);
        System.out.println("💡 Dicas utilizadas: " + PURPLE + gameState.getHints() + RESET);
        System.out.println("🌟 Pontuação final: " + BOLD + YELLOW + gameState.getScore() + RESET);
        System.out.println("📈 Dificuldade: " + gameState.getDifficulty().getDescription());
        System.out.println();
    }
    
    /**
     * Exibe prompt para entrada do usuário.
     * 
     * @param prompt texto do prompt
     */
    public void displayPrompt(String prompt) {
        System.out.print(BOLD + "> " + RESET + prompt);
    }
    
    /**
     * Exibe uma linha separadora.
     */
    public void displaySeparator() {
        System.out.println("═".repeat(65));
    }
    
    /**
     * Exibe uma linha em branco.
     */
    public void displayBlankLine() {
        System.out.println();
    }
    
    /**
     * Exibe mensagem de carregamento.
     * 
     * @param message mensagem
     */
    public void displayLoadingMessage(String message) {
        System.out.print(YELLOW + "⏳ " + message + RESET);
        
        // Animação simples
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(" " + GREEN + "Concluído!" + RESET);
    }
    
    /**
     * Exibe mensagem de despedida.
     */
    public void displayGoodbyeMessage() {
        System.out.println();
        System.out.println(BOLD + BLUE + 
            "╔═══════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + BLUE + 
            "║              Obrigado por jogar Sudoku! 🧩                   ║" + RESET);
        System.out.println(BOLD + BLUE + 
            "║                 Até a próxima! 👋                            ║" + RESET);
        System.out.println(BOLD + BLUE + 
            "╚═══════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Exibe o tabuleiro com destacar uma célula específica.
     * 
     * @param board tabuleiro
     * @param highlightRow linha a destacar (1-9)
     * @param highlightCol coluna a destacar (1-9)
     */
    public void displayBoardWithHighlight(Board board, int highlightRow, int highlightCol) {
        System.out.println();
        System.out.println(BOLD + "    1   2   3   4   5   6   7   8   9" + RESET);
        System.out.println("  ┌───────────┬───────────┬───────────┐");
        
        for (int row = 0; row < Board.SIZE; row++) {
            if (row > 0 && row % 3 == 0) {
                System.out.println("  ├───────────┼───────────┼───────────┤");
            }
            
            System.out.print(BOLD + (row + 1) + RESET + " │ ");
            
            for (int col = 0; col < Board.SIZE; col++) {
                if (col > 0 && col % 3 == 0) {
                    System.out.print("│ ");
                }
                
                Cell cell = board.getCell(row, col);
                String cellDisplay;
                
                // Destacar célula específica
                if (row == highlightRow - 1 && col == highlightCol - 1) {
                    cellDisplay = UNDERLINE + BOLD + YELLOW + 
                                 (cell.isEmpty() ? "·" : String.valueOf(cell.getValue())) + 
                                 RESET;
                } else {
                    cellDisplay = formatCell(cell);
                }
                
                System.out.print(cellDisplay + " ");
            }
            
            System.out.println("│");
        }
        
        System.out.println("  └───────────┴───────────┴───────────┘");
        System.out.println();
    }
    
    /**
     * Exibe uma lista de valores possíveis para uma célula.
     * 
     * @param row linha
     * @param col coluna
     * @param possibleValues valores possíveis
     */
    public void displayPossibleValues(int row, int col, int[] possibleValues) {
        if (possibleValues.length == 0) {
            displayWarningMessage(String.format("Nenhum valor possível para a posição (%d, %d)", row, col));
        } else {
            System.out.print(BLUE + "💡 Valores possíveis para (" + row + ", " + col + "): " + RESET);
            for (int i = 0; i < possibleValues.length; i++) {
                System.out.print(GREEN + possibleValues[i] + RESET);
                if (i < possibleValues.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Aguarda que o usuário pressione Enter.
     */
    public void waitForEnter() {
        System.out.print("\nPressione Enter para continuar...");
    }
    
    /**
     * Exibe cabeçalho de uma seção.
     * 
     * @param title título da seção
     */
    public void displaySectionHeader(String title) {
        System.out.println();
        System.out.println(BOLD + "═══ " + title.toUpperCase() + " ═══" + RESET);
    }
}