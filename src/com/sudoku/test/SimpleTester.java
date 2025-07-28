package com.sudoku.test;

import com.sudoku.model.Board;
import com.sudoku.model.Cell;
import com.sudoku.model.GameState;
import com.sudoku.service.GameManager;
import com.sudoku.service.PuzzleGenerator;
import com.sudoku.service.SudokuSolver;
import com.sudoku.util.GameConfig;

/**
 * Classe simples de testes para validar funcionalidades básicas.
 * Execute esta classe para verificar se o sistema está funcionando corretamente.
 * 
 * Para executar:
 * 1. Compile: javac -cp build -d build src/com/sudoku/test/SimpleTester.java
 * 2. Execute: java -cp build com.sudoku.test.SimpleTester
 */
public class SimpleTester {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("=== TESTE DO SISTEMA SUDOKU ===");
        System.out.println("Executando testes básicos...\n");
        
        try {
            testCellBasics();
            testBoardBasics();
            testGameStateBasics();
            testPuzzleGenerator();
            testSudokuSolver();
            testGameManager();
            
            System.out.println("\n=== RESULTADO DOS TESTES ===");
            System.out.println("Testes executados: " + testsTotal);
            System.out.println("Testes aprovados: " + testsPassed);
            System.out.println("Taxa de sucesso: " + (testsPassed * 100 / testsTotal) + "%");
            
            if (testsPassed == testsTotal) {
                System.out.println("✓ TODOS OS TESTES PASSARAM!");
                System.out.println("O sistema está funcionando corretamente.");
            } else {
                System.out.println("✗ ALGUNS TESTES FALHARAM!");
                System.out.println("Verifique a implementação.");
            }
            
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO durante os testes:");
            e.printStackTrace();
        }
    }
    
    private static void testCellBasics() {
        System.out.println("Testando classe Cell...");
        
        // Teste 1: Criação de célula vazia
        assertTest("Cell vazia", () -> {
            Cell cell = new Cell();
            return cell.isEmpty() && cell.getValue() == 0 && !cell.isFixed();
        });
        
        // Teste 2: Definir valor
        assertTest("Cell setValue", () -> {
            Cell cell = new Cell();
            cell.setValue(5);
            return cell.getValue() == 5 && !cell.isEmpty();
        });
        
        // Teste 3: Célula fixa
        assertTest("Cell fixa", () -> {
            Cell cell = new Cell(7, true);
            return cell.getValue() == 7 && cell.isFixed();
        });
        
        // Teste 4: Validação de valor inválido
        assertTest("Cell valor inválido", () -> {
            Cell cell = new Cell();
            try {
                cell.setValue(10); // Valor inválido
                return false;
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
    }
    
    private static void testBoardBasics() {
        System.out.println("Testando classe Board...");
        
        // Teste 1: Tabuleiro vazio
        assertTest("Board vazio", () -> {
            Board board = new Board();
            return board.isEmpty() && board.getFilledCells() == 0;
        });
        
        // Teste 2: Validação de jogada
        assertTest("Board validação", () -> {
            Board board = new Board();
            return board.isValidMove(0, 0, 5); // Primeira jogada sempre válida
        });
        
        // Teste 3: Jogada conflitante
        assertTest("Board conflito linha", () -> {
            Board board = new Board();
            board.setValue(0, 0, 5);
            return !board.isValidMove(0, 1, 5); // Mesmo número na linha
        });
        
        // Teste 4: Conversão para array
        assertTest("Board toArray", () -> {
            Board board = new Board();
            board.setValue(0, 0, 5);
            int[][] array = board.toArray();
            return array[0][0] == 5;
        });
    }
    
    private static void testGameStateBasics() {
        System.out.println("Testando classe GameState...");
        
        // Teste 1: Criação de novo jogo
        assertTest("GameState novo", () -> {
            GameState game = new GameState();
            return game.getStatus() == GameState.Status.NOVO;
        });
        
        // Teste 2: Formatação de tempo
        assertTest("GameState tempo", () -> {
            GameState game = new GameState();
            return game.getFormattedTime().equals("00:00");
        });
        
        // Teste 3: Estatísticas iniciais
        assertTest("GameState estatísticas", () -> {
            GameState game = new GameState();
            return game.getMoves() == 0 && game.getErrors() == 0 && game.getHints() == 0;
        });
    }
    
    private static void testPuzzleGenerator() {
        System.out.println("Testando PuzzleGenerator...");
        
        // Teste 1: Geração de puzzle de exemplo
        assertTest("PuzzleGenerator exemplo", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            return puzzle != null && puzzle.getFilledCells() > 0;
        });
        
        // Teste 2: Validação de Sudoku
        assertTest("PuzzleGenerator validação", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            return generator.isValidSudoku(puzzle);
        });
    }
    
    private static void testSudokuSolver() {
        System.out.println("Testando SudokuSolver...");
        
        // Teste 1: Verificar se pode resolver puzzle
        assertTest("SudokuSolver solucionável", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            SudokuSolver solver = new SudokuSolver();
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            return solver.isSolvable(puzzle);
        });
        
        // Teste 2: Obter valores possíveis
        assertTest("SudokuSolver valores possíveis", () -> {
            Board board = new Board();
            SudokuSolver solver = new SudokuSolver();
            int[] possible = solver.getPossibleValues(board, 0, 0);
            return possible.length == 9; // Célula vazia pode ter qualquer valor
        });
        
        // Teste 3: Validar movimento
        assertTest("SudokuSolver validar movimento", () -> {
            Board board = new Board();
            SudokuSolver solver = new SudokuSolver();
            return solver.validateMove(board, 0, 0, 5);
        });
    }
    
    private static void testGameManager() {
        System.out.println("Testando GameManager...");
        
        // Teste 1: Iniciar novo jogo
        assertTest("GameManager novo jogo", () -> {
            GameManager manager = new GameManager();
            return manager.startNewGame(GameState.Difficulty.FACIL);
        });
        
        // Teste 2: Verificar jogo ativo
        assertTest("GameManager jogo ativo", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            return manager.hasActiveGame();
        });
        
        // Teste 3: Pausar jogo
        assertTest("GameManager pausar", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            return manager.pauseGame();
        });
        
        // Teste 4: Obter estatísticas
        assertTest("GameManager estatísticas", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            String stats = manager.getGameStatistics();
            return stats != null && !stats.isEmpty();
        });
    }
    
    /**
     * Método utilitário para executar e validar um teste.
     */
    private static void assertTest(String testName, TestFunction test) {
        testsTotal++;
        try {
            boolean result = test.run();
            if (result) {
                System.out.println("  ✓ " + testName);
                testsPassed++;
            } else {
                System.out.println("  ✗ " + testName + " - FALHOU");
            }
        } catch (Exception e) {
            System.out.println("  ✗ " + testName + " - ERRO: " + e.getMessage());
        }
    }
    
    /**
     * Interface funcional para testes.
     */
    @FunctionalInterface
    interface TestFunction {
        boolean run() throws Exception;
    }
}