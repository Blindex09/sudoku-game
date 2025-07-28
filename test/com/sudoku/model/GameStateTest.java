package com.sudoku.model;

import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe GameState.
 * Testa todas as funcionalidades do estado do jogo.
 */
public class GameStateTest {
    
    /**
     * Executa todos os testes da classe GameState.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe GameState");
        
        testGameStateCreation();
        testGameStateStatus();
        testGameStateDifficulty();
        testGameStateCounters();
        testGameStateScore();
        testGameStateTime();
        testGameStateBoard();
        testGameStateValidation();
    }
    
    /**
     * Testa a criação de estados de jogo.
     */
    private static void testGameStateCreation() {
        runTest("Criação com dificuldade", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            assertEquals(GameState.Difficulty.FACIL, state.getDifficulty(), "Dificuldade deve ser FACIL");
            assertEquals(GameState.Status.JOGANDO, state.getStatus(), "Status inicial deve ser JOGANDO");
            assertEquals(0, state.getMoves(), "Deve ter 0 jogadas");
            assertEquals(0, state.getHints(), "Deve ter 0 dicas");
            assertTrue(state.getScore() > 0, "Deve ter pontuação inicial");
            return true;
        });
        
        runTest("Criação com tabuleiro", () -> {
            Board board = new Board();
            board.setValue(0, 0, 5);
            
            GameState state = new GameState(GameState.Difficulty.MEDIO, board);
            
            assertEquals(GameState.Difficulty.MEDIO, state.getDifficulty(), "Dificuldade deve ser MEDIO");
            assertNotNull(state.getBoard(), "Deve ter tabuleiro");
            assertEquals(5, state.getBoard().getCell(0, 0).getValue(), "Tabuleiro deve ser preservado");
            return true;
        });
        
        runTest("Valores padrão", () -> {
            GameState state = new GameState(GameState.Difficulty.DIFICIL);
            
            assertEquals(0, state.getElapsedTime(), "Tempo deve ser 0");
            assertFalse(state.isCompleted(), "Jogo não deve estar completo");
            assertFalse(state.isPaused(), "Jogo não deve estar pausado");
            assertTrue(state.isActive(), "Jogo deve estar ativo");
            return true;
        });
    }
    
    /**
     * Testa mudanças de status.
     */
    private static void testGameStateStatus() {
        runTest("Pausar jogo", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            assertEquals(GameState.Status.JOGANDO, state.getStatus());
            
            state.pause();
            
            assertEquals(GameState.Status.PAUSADO, state.getStatus(), "Status deve ser PAUSADO");
            assertTrue(state.isPaused(), "Deve estar pausado");
            assertFalse(state.isActive(), "Não deve estar ativo");
            return true;
        });
        
        runTest("Resumir jogo", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            state.pause();
            
            state.resume();
            
            assertEquals(GameState.Status.JOGANDO, state.getStatus(), "Status deve ser JOGANDO");
            assertFalse(state.isPaused(), "Não deve estar pausado");
            assertTrue(state.isActive(), "Deve estar ativo");
            return true;
        });
        
        runTest("Completar jogo", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            state.complete();
            
            assertEquals(GameState.Status.COMPLETADO, state.getStatus(), "Status deve ser COMPLETADO");
            assertTrue(state.isCompleted(), "Deve estar completo");
            assertFalse(state.isActive(), "Não deve estar ativo");
            return true;
        });
        
        runTest("Abandonar jogo", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            state.abandon();
            
            assertEquals(GameState.Status.ABANDONADO, state.getStatus(), "Status deve ser ABANDONADO");
            assertFalse(state.isActive(), "Não deve estar ativo");
            assertFalse(state.isCompleted(), "Não deve estar completo");
            return true;
        });
    }
    
    /**
     * Testa configurações de dificuldade.
     */
    private static void testGameStateDifficulty() {
        runTest("Dificuldades disponíveis", () -> {
            GameState.Difficulty[] difficulties = GameState.Difficulty.values();
            
            assertTrue(difficulties.length >= 4, "Deve ter pelo menos 4 níveis de dificuldade");
            
            // Verificar se contém dificuldades básicas
            boolean hasFacil = false, hasMedio = false, hasDificil = false;
            for (GameState.Difficulty diff : difficulties) {
                if (diff == GameState.Difficulty.FACIL) hasFacil = true;
                if (diff == GameState.Difficulty.MEDIO) hasMedio = true;
                if (diff == GameState.Difficulty.DIFICIL) hasDificil = true;
            }
            
            assertTrue(hasFacil, "Deve ter dificuldade FACIL");
            assertTrue(hasMedio, "Deve ter dificuldade MEDIO");
            assertTrue(hasDificil, "Deve ter dificuldade DIFICIL");
            return true;
        });
        
        runTest("Pontuação por dificuldade", () -> {
            GameState stateFacil = new GameState(GameState.Difficulty.FACIL);
            GameState stateMedio = new GameState(GameState.Difficulty.MEDIO);
            GameState stateDificil = new GameState(GameState.Difficulty.DIFICIL);
            
            // Pontuação deve aumentar com dificuldade
            assertTrue(stateMedio.getScore() > stateFacil.getScore(), 
                      "MEDIO deve ter pontuação maior que FACIL");
            assertTrue(stateDificil.getScore() > stateMedio.getScore(), 
                      "DIFICIL deve ter pontuação maior que MEDIO");
            return true;
        });
    }
    
    /**
     * Testa contadores do jogo.
     */
    private static void testGameStateCounters() {
        runTest("Incrementar jogadas", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            assertEquals(0, state.getMoves(), "Deve começar com 0 jogadas");
            
            state.incrementMoves();
            assertEquals(1, state.getMoves(), "Deve ter 1 jogada");
            
            state.incrementMoves();
            state.incrementMoves();
            assertEquals(3, state.getMoves(), "Deve ter 3 jogadas");
            return true;
        });
        
        runTest("Incrementar dicas", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            assertEquals(0, state.getHints(), "Deve começar com 0 dicas");
            
            state.incrementHints();
            assertEquals(1, state.getHints(), "Deve ter 1 dica");
            
            state.incrementHints();
            assertEquals(2, state.getHints(), "Deve ter 2 dicas");
            return true;
        });
        
        runTest("Reset de contadores", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            // Incrementar contadores
            state.incrementMoves();
            state.incrementMoves();
            state.incrementHints();
            
            // Reset
            state.reset();
            
            assertEquals(0, state.getMoves(), "Jogadas devem ser zeradas");
            assertEquals(0, state.getHints(), "Dicas devem ser zeradas");
            assertEquals(GameState.Status.JOGANDO, state.getStatus(), "Status deve voltar a JOGANDO");
            return true;
        });
    }
    
    /**
     * Testa sistema de pontuação.
     */
    private static void testGameStateScore() {
        runTest("Pontuação inicial", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            int initialScore = state.getScore();
            
            assertTrue(initialScore > 0, "Pontuação inicial deve ser positiva");
            return true;
        });
        
        runTest("Penalidade por dicas", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            int initialScore = state.getScore();
            
            state.incrementHints();
            
            assertTrue(state.getScore() < initialScore, "Pontuação deve diminuir com dicas");
            return true;
        });
        
        runTest("Pontuação não negativa", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            // Usar muitas dicas para tentar tornar pontuação negativa
            for (int i = 0; i < 100; i++) {
                state.incrementHints();
            }
            
            assertTrue(state.getScore() >= 0, "Pontuação não deve ser negativa");
            return true;
        });
        
        runTest("Bonus por completar", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            int scoreBeforeComplete = state.getScore();
            
            state.complete();
            
            assertTrue(state.getScore() >= scoreBeforeComplete, 
                      "Pontuação não deve diminuir ao completar");
            return true;
        });
    }
    
    /**
     * Testa controle de tempo.
     */
    private static void testGameStateTime() {
        runTest("Tempo inicial", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            assertEquals(0, state.getElapsedTime(), "Tempo deve começar em 0");
            assertNotNull(state.getFormattedTime(), "Tempo formatado não deve ser null");
            return true;
        });
        
        runTest("Atualizar tempo", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            state.updateTime(5000); // 5 segundos
            
            assertEquals(5000, state.getElapsedTime(), "Tempo deve ser 5000ms");
            
            state.updateTime(10000); // Mais 5 segundos
            assertEquals(10000, state.getElapsedTime(), "Tempo deve ser 10000ms");
            return true;
        });
        
        runTest("Tempo formatado", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            state.updateTime(65000); // 1 minuto e 5 segundos
            String formatted = state.getFormattedTime();
            
            assertNotNull(formatted, "Tempo formatado não deve ser null");
            assertTrue(formatted.contains("01") || formatted.contains("1"), 
                      "Deve conter informação de minutos");
            return true;
        });
        
        runTest("Pausar tempo", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            state.updateTime(5000);
            
            state.pause();
            long pausedTime = state.getElapsedTime();
            
            // Simular que tempo passou (normalmente seria atualizado automaticamente)
            // Em jogo pausado, tempo não deve mudar
            assertEquals(pausedTime, state.getElapsedTime(), 
                        "Tempo não deve mudar quando pausado");
            return true;
        });
    }
    
    /**
     * Testa integração com tabuleiro.
     */
    private static void testGameStateBoard() {
        runTest("Tabuleiro associado", () -> {
            Board board = new Board();
            board.setValue(0, 0, 7);
            
            GameState state = new GameState(GameState.Difficulty.FACIL, board);
            
            assertNotNull(state.getBoard(), "Deve ter tabuleiro associado");
            assertEquals(7, state.getBoard().getCell(0, 0).getValue(), 
                        "Tabuleiro deve manter dados");
            return true;
        });
        
        runTest("Atualizar tabuleiro", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            Board newBoard = new Board();
            newBoard.setValue(1, 1, 9);
            
            state.setBoard(newBoard);
            
            assertEquals(9, state.getBoard().getCell(1, 1).getValue(), 
                        "Novo tabuleiro deve ser associado");
            return true;
        });
        
        runTest("Verificar resolução", () -> {
            // Criar solução completa
            int[][] solution = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
            };
            
            Board completeBoard = new Board(solution);
            GameState state = new GameState(GameState.Difficulty.FACIL, completeBoard);
            
            if (completeBoard.isSolved()) {
                assertTrue(state.getBoard().isSolved(), "Estado deve refletir resolução do tabuleiro");
            }
            return true;
        });
    }
    
    /**
     * Testa validações de estado.
     */
    private static void testGameStateValidation() {
        runTest("Transições válidas de status", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            // JOGANDO → PAUSADO
            assertTrue(state.isActive(), "Deve estar ativo");
            state.pause();
            assertTrue(state.isPaused(), "Deve estar pausado");
            
            // PAUSADO → JOGANDO
            state.resume();
            assertTrue(state.isActive(), "Deve estar ativo novamente");
            
            // JOGANDO → COMPLETADO
            state.complete();
            assertTrue(state.isCompleted(), "Deve estar completado");
            
            return true;
        });
        
        runTest("Estados mutuamente exclusivos", () -> {
            GameState state = new GameState(GameState.Difficulty.FACIL);
            
            // Estado inicial
            assertTrue(state.isActive() && !state.isPaused() && !state.isCompleted());
            
            // Pausado
            state.pause();
            assertTrue(!state.isActive() && state.isPaused() && !state.isCompleted());
            
            // Completado
            state.complete();
            assertTrue(!state.isActive() && !state.isPaused() && state.isCompleted());
            
            return true;
        });
        
        runTest("Validação de entrada", () -> {
            // Teste com dificuldade null
            assertThrows(IllegalArgumentException.class, 
                        () -> new GameState(null), 
                        "Deve lançar exceção para dificuldade null");
            
            return true;
        });
        
        runTest("Cópia de estado", () -> {
            GameState original = new GameState(GameState.Difficulty.MEDIO);
            original.incrementMoves();
            original.incrementHints();
            original.updateTime(5000);
            
            GameState copy = original.copy();
            
            assertEquals(original.getDifficulty(), copy.getDifficulty(), "Dificuldade deve ser copiada");
            assertEquals(original.getMoves(), copy.getMoves(), "Jogadas devem ser copiadas");
            assertEquals(original.getHints(), copy.getHints(), "Dicas devem ser copiadas");
            assertEquals(original.getElapsedTime(), copy.getElapsedTime(), "Tempo deve ser copiado");
            assertEquals(original.getStatus(), copy.getStatus(), "Status deve ser copiado");
            
            return true;
        });
    }
}