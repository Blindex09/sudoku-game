package com.sudoku;

import com.sudoku.view.GameController;
import com.sudoku.util.GameConfig;

/**
 * Classe principal do Jogo de Sudoku.
 * Ponto de entrada da aplicação.
 * 
 * Para executar o jogo:
 * 1. Compile todas as classes Java
 * 2. Execute: java com.sudoku.Main
 * 
 * Funcionalidades implementadas:
 * - Geração de puzzles com diferentes níveis de dificuldade
 * - Interface de console colorida e intuitiva
 * - Sistema de pontuação baseado em tempo, erros e dicas
 * - Validação de jogadas em tempo real
 * - Sistema de dicas inteligente
 * - Pausar/resumir jogo
 * - Resetar jogo
 * - Resolver automaticamente
 * - Estatísticas detalhadas do jogo
 * - Tratamento robusto de erros
 * 
 * @author Desenvolvedor Senior
 * @version 1.0.0
 */
public class Main {
    
    /**
     * Método principal - ponto de entrada da aplicação.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            // Configuração inicial
            setupApplication(args);
            
            // Criar e iniciar o controlador do jogo
            GameController gameController = new GameController();
            gameController.run();
            
        } catch (Exception e) {
            // Tratamento de erro crítico
            handleCriticalError(e);
        }
    }
    
    /**
     * Configura a aplicação baseado nos argumentos da linha de comando.
     * 
     * @param args argumentos da linha de comando
     */
    private static void setupApplication(String[] args) {
        // Processar argumentos da linha de comando
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "--debug":
                case "-d":
                    System.setProperty("sudoku.debug", "true");
                    System.out.println("Modo debug ativado");
                    break;
                    
                case "--no-colors":
                case "-nc":
                    System.setProperty("sudoku.colors", "false");
                    System.out.println("Cores desabilitadas");
                    break;
                    
                case "--help":
                case "-h":
                    showHelp();
                    System.exit(0);
                    break;
                    
                case "--version":
                case "-v":
                    showVersion();
                    System.exit(0);
                    break;
                    
                default:
                    if (arg.startsWith("-")) {
                        System.err.println("Argumento desconhecido: " + arg);
                        System.err.println("Use --help para ver as opções disponíveis.");
                    }
                    break;
            }
        }
        
        // Configurar encoding para caracteres especiais
        try {
            System.setProperty("file.encoding", "UTF-8");
            
            // Tentar configurar console para UTF-8 (Windows)
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // No Windows, pode ser necessário configurar o console
                // Esta é uma configuração básica que funciona na maioria dos casos
            }
        } catch (Exception e) {
            System.err.println("Aviso: Não foi possível configurar encoding UTF-8");
        }
        
        // Log de inicialização
        GameConfig.debugPrint("Aplicação inicializada com sucesso");
        GameConfig.debugPrint("Java Version: " + System.getProperty("java.version"));
        GameConfig.debugPrint("OS: " + System.getProperty("os.name"));
    }
    
    /**
     * Exibe informações de ajuda.
     */
    private static void showHelp() {
        System.out.println(GameConfig.GAME_TITLE + " v" + GameConfig.GAME_VERSION);
        System.out.println("Desenvolvido por: " + GameConfig.GAME_AUTHOR);
        System.out.println();
        System.out.println("Uso: java com.sudoku.Main [opções]");
        System.out.println();
        System.out.println("Opções:");
        System.out.println("  --debug, -d        Ativa modo debug com informações detalhadas");
        System.out.println("  --no-colors, -nc   Desabilita cores na interface");
        System.out.println("  --help, -h         Exibe esta ajuda");
        System.out.println("  --version, -v      Exibe informações de versão");
        System.out.println();
        System.out.println("Exemplos:");
        System.out.println("  java com.sudoku.Main");
        System.out.println("  java com.sudoku.Main --debug");
        System.out.println("  java com.sudoku.Main --no-colors");
        System.out.println();
        System.out.println("Como jogar:");
        System.out.println("• Use os menus numerados para navegar");
        System.out.println("• Para fazer uma jogada: digite linha coluna valor (ex: 3 7 5)");
        System.out.println("• Para limpar uma célula: use 0 como valor (ex: 3 7 0)");
        System.out.println("• Siga as regras clássicas do Sudoku");
        System.out.println();
        System.out.println("Dificuldades disponíveis:");
        System.out.println("• Fácil: 40 números preenchidos");
        System.out.println("• Médio: 30 números preenchidos");
        System.out.println("• Difícil: 20 números preenchidos");
        System.out.println("• Expert: 17 números preenchidos");
    }
    
    /**
     * Exibe informações de versão.
     */
    private static void showVersion() {
        System.out.println(GameConfig.GAME_TITLE + " v" + GameConfig.GAME_VERSION);
        System.out.println("Desenvolvido por: " + GameConfig.GAME_AUTHOR);
        System.out.println();
        System.out.println("Funcionalidades:");
        System.out.println("• Geração automática de puzzles");
        System.out.println("• 4 níveis de dificuldade");
        System.out.println("• Interface colorida no terminal");
        System.out.println("• Sistema de pontuação");
        System.out.println("• Dicas inteligentes");
        System.out.println("• Validação de jogadas");
        System.out.println("• Pausar/resumir jogo");
        System.out.println("• Estatísticas detalhadas");
        System.out.println();
        System.out.println("Requisitos:");
        System.out.println("• Java 8 ou superior");
        System.out.println("• Terminal com suporte a cores ANSI (recomendado)");
        System.out.println();
        System.out.println("Copyright © 2025 - Todos os direitos reservados");
    }
    
    /**
     * Trata erros críticos que impedem a execução do jogo.
     * 
     * @param e exceção capturada
     */
    private static void handleCriticalError(Exception e) {
        System.err.println("═══ ERRO CRÍTICO ═══");
        System.err.println("O jogo não pode continuar devido ao seguinte erro:");
        System.err.println();
        System.err.println("Tipo: " + e.getClass().getSimpleName());
        System.err.println("Mensagem: " + e.getMessage());
        
        // Mostrar stack trace apenas em modo debug
        String debugMode = System.getProperty("sudoku.debug", "false");
        if ("true".equals(debugMode)) {
            System.err.println();
            System.err.println("Stack trace completo:");
            e.printStackTrace();
        }
        
        System.err.println();
        System.err.println("Possíveis soluções:");
        System.err.println("• Verifique se o Java está instalado corretamente");
        System.err.println("• Certifique-se de que todas as classes foram compiladas");
        System.err.println("• Tente executar com --debug para mais informações");
        System.err.println("• Reinicie o terminal e tente novamente");
        
        System.err.println();
        System.err.println("Se o problema persistir, consulte a documentação.");
        
        // Código de saída indicando erro
        System.exit(1);
    }
    
    /**
     * Método estático para informações do jogo (usado por outras classes se necessário).
     * 
     * @return informações básicas do jogo
     */
    public static String getGameInfo() {
        return GameConfig.getGameInfo();
    }
}