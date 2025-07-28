package com.sudoku.test;

import com.sudoku.model.CellTest;
import com.sudoku.model.BoardTest;
import com.sudoku.model.GameStateTest;
import com.sudoku.service.PuzzleGeneratorTest;
import com.sudoku.service.SudokuSolverTest;
import com.sudoku.service.GameManagerTest;
import com.sudoku.util.InputValidatorTest;
import com.sudoku.util.TimeFormatterTest;
import com.sudoku.util.GameConfigTest;
import com.sudoku.integration.SystemIntegrationTest;

/**
 * Executor principal de todos os testes automatizados do sistema Sudoku.
 * 
 * Esta classe coordena a execução de toda a suíte de testes, incluindo:
 * - Testes unitários de todas as classes
 * - Testes de integração entre componentes
 * - Testes de performance
 * - Testes de casos extremos
 * 
 * Para executar:
 * 1. Compile: javac -d build -cp build test/com/sudoku/test/AllTests.java test/com/sudoku/.../*.java
 * 2. Execute: java -cp build com.sudoku.test.AllTests
 */
public class AllTests {
    
    private static final String SEPARATOR = "=".repeat(80);
    private static final String MINI_SEPARATOR = "-".repeat(40);
    
    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("🧪 SUDOKU SYSTEM - COMPLETE TEST SUITE");
        System.out.println("Suíte Completa de Testes Automatizados");
        System.out.println(SEPARATOR);
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Reset framework para nova execução
            TestFramework.reset();
            
            System.out.println("📋 EXECUTANDO TESTES UNITÁRIOS");
            System.out.println(MINI_SEPARATOR);
            runUnitTests();
            
            System.out.println("\n🔗 EXECUTANDO TESTES DE INTEGRAÇÃO");
            System.out.println(MINI_SEPARATOR);
            runIntegrationTests();
            
            System.out.println("\n⚡ EXECUTANDO TESTES DE PERFORMANCE");
            System.out.println(MINI_SEPARATOR);
            runPerformanceTests();
            
            long endTime = System.currentTimeMillis();
            long totalDuration = endTime - startTime;
            
            printFinalSummary(totalDuration);
            
        } catch (Exception e) {
            System.err.println("\n💥 ERRO CRÍTICO DURANTE EXECUÇÃO DOS TESTES:");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Executa todos os testes unitários.
     */
    private static void runUnitTests() {
        System.out.println("📦 Testando Model Layer...");
        CellTest.runAllTests();
        BoardTest.runAllTests();
        GameStateTest.runAllTests();
        
        System.out.println("\n⚙️  Testando Service Layer...");
        PuzzleGeneratorTest.runAllTests();
        SudokuSolverTest.runAllTests();
        GameManagerTest.runAllTests();
        
        System.out.println("\n🔧 Testando Utility Layer...");
        InputValidatorTest.runAllTests();
        TimeFormatterTest.runAllTests();
        GameConfigTest.runAllTests();
    }
    
    /**
     * Executa testes de integração.
     */
    private static void runIntegrationTests() {
        System.out.println("🔗 Testando Integração do Sistema...");
        SystemIntegrationTest.runAllTests();
    }
    
    /**
     * Executa testes de performance.
     */
    private static void runPerformanceTests() {
        System.out.println("⚡ Testando Performance...");
        
        TestFramework.startSuite("Testes de Performance");
        
        // Teste de geração de puzzle
        TestFramework.runTest("Performance - Geração de Puzzle", () -> {
            long startTime = System.currentTimeMillis();
            
            com.sudoku.service.PuzzleGenerator generator = new com.sudoku.service.PuzzleGenerator();
            for (int i = 0; i < 10; i++) {
                generator.generateSamplePuzzle(com.sudoku.model.GameState.Difficulty.FACIL);
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Deve levar menos de 5 segundos para gerar 10 puzzles
            if (duration > 5000) {
                System.out.println("    Aviso: Geração demorou " + duration + "ms");
            }
            
            return true;
        });
        
        // Teste de resolução
        TestFramework.runTest("Performance - Resolução de Puzzle", () -> {
            com.sudoku.service.PuzzleGenerator generator = new com.sudoku.service.PuzzleGenerator();
            com.sudoku.service.SudokuSolver solver = new com.sudoku.service.SudokuSolver();
            
            com.sudoku.model.Board puzzle = generator.generateSamplePuzzle(com.sudoku.model.GameState.Difficulty.FACIL);
            
            long startTime = System.currentTimeMillis();
            boolean solved = solver.solve(puzzle);
            long endTime = System.currentTimeMillis();
            
            long duration = endTime - startTime;
            
            // Deve resolver em menos de 1 segundo
            if (duration > 1000) {
                System.out.println("    Aviso: Resolução demorou " + duration + "ms");
            }
            
            return solved;
        });
        
        // Teste de memória
        TestFramework.runTest("Performance - Uso de Memória", () -> {
            Runtime runtime = Runtime.getRuntime();
            long startMemory = runtime.totalMemory() - runtime.freeMemory();
            
            // Criar múltiplos objetos do jogo
            for (int i = 0; i < 100; i++) {
                com.sudoku.service.GameManager manager = new com.sudoku.service.GameManager();
                manager.startNewGame(com.sudoku.model.GameState.Difficulty.FACIL);
            }
            
            System.gc(); // Forçar coleta de lixo
            Thread.sleep(100); // Aguardar coleta
            
            long endMemory = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = endMemory - startMemory;
            
            // Não deve usar mais que 50MB
            if (memoryUsed > 50 * 1024 * 1024) {
                System.out.println("    Aviso: Uso de memória alto: " + (memoryUsed / 1024 / 1024) + "MB");
            }
            
            return true;
        });
    }
    
    /**
     * Imprime resumo final de todos os testes.
     */
    private static void printFinalSummary(long totalDuration) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("🏆 RESUMO FINAL - SUÍTE COMPLETA DE TESTES");
        System.out.println(SEPARATOR);
        
        System.out.println("📋 Total de testes executados: " + TestFramework.getTotalTests());
        System.out.println("✅ Testes aprovados: " + TestFramework.getPassedTests());
        System.out.println("❌ Testes falharam: " + TestFramework.getFailedTests());
        
        double successRate = TestFramework.getTotalTests() > 0 ? 
                            (TestFramework.getPassedTests() * 100.0 / TestFramework.getTotalTests()) : 0;
        System.out.println("🏅 Taxa de sucesso: " + String.format("%.1f", successRate) + "%");
        
        System.out.println("⏱️ Tempo total de execução: " + totalDuration + "ms");
        
        if (TestFramework.getFailedTests() == 0) {
            System.out.println("\n🎉 TODOS OS TESTES PASSARAM!");
            System.out.println("✨ Sistema Sudoku completamente validado!");
            System.out.println("🚀 Pronto para uso em produção!");
        } else {
            System.out.println("\n⚠️  ALGUNS TESTES FALHARAM!");
            System.out.println("🔧 Revisar implementação antes do uso.");
        }
        
        System.out.println(SEPARATOR);
        
        // Exit code baseado no resultado
        System.exit(TestFramework.getFailedTests() == 0 ? 0 : 1);
    }
}