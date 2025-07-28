package com.sudoku.service;

import com.sudoku.model.Board;
import com.sudoku.model.GameState;
import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes simplificados para a classe GameManager.
 * Testa apenas as funcionalidades básicas que sabemos que existem.
 */
public class GameManagerTest {
    
    /**
     * Executa todos os testes da classe GameManager.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe GameManager (Simplificado)");
        
        testBasicGameCreation();
        testBasicGameFunctionality();
    }
    
    /**
     * Testa criação básica de jogos.
     */
    private static void testBasicGameCreation() {
        runTest("Criar GameManager", () -> {
            GameManager manager = new GameManager();
            assertNotNull(manager, "GameManager deve ser criado");
            return true;
        });
        
        runTest("Criar novo jogo básico", () -> {
            GameManager manager = new GameManager();
            boolean created = manager.startNewGame(GameState.Difficulty.FACIL);
            
            assertTrue(created, "Deve conseguir criar novo jogo");
            assertTrue(manager.hasActiveGame(), "Deve ter jogo ativo");
            return true;
        });
    }
    
    /**
     * Testa funcionalidades básicas disponíveis.
     */
    private static void testBasicGameFunctionality() {
        runTest("Verificar jogo ativo", () -> {
            GameManager manager = new GameManager();
            assertFalse(manager.hasActiveGame(), "Inicialmente não deve ter jogo ativo");
            
            manager.startNewGame(GameState.Difficulty.FACIL);
            assertTrue(manager.hasActiveGame(), "Deve ter jogo ativo após iniciar");
            return true;
        });
        
        runTest("Pausar e resumir jogo", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            boolean paused = manager.pauseGame();
            assertTrue(paused, "Deve conseguir pausar jogo");
            
            boolean resumed = manager.resumeGame();
            assertTrue(resumed, "Deve conseguir resumir jogo");
            return true;
        });
        
        runTest("Reset de jogo", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.MEDIO);
            
            boolean reset = manager.resetGame();
            assertTrue(reset, "Deve conseguir resetar jogo");
            return true;
        });
        
        runTest("Obter estatísticas", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            String stats = manager.getGameStatistics();
            assertNotNull(stats, "Estatísticas não devem ser null");
            return true;
        });
    }
}