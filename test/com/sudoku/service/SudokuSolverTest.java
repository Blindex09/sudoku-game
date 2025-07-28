package com.sudoku.service;

import com.sudoku.model.Board;
import com.sudoku.model.GameState;
import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe SudokuSolver.
 * Testa todas as funcionalidades de resolução de puzzles.
 */
public class SudokuSolverTest {
    
    /**
     * Executa todos os testes da classe SudokuSolver.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe SudokuSolver");
        
        testBasicSolving();
        testSolutionValidation();
        testHints();
        testPossibleValues();
        testSolutionCounting();
        testDifficultyEstimation();
        testMoveValidation();
        testPerformance();
    }
    
    /**
     * Testa resolução básica de puzzles.
     */
    private static void testBasicSolving() {
        runTest("Resolver puzzle simples", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            // Puzzle fácil conhecido
            int[][] easyPuzzle = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };
            
            Board board = new Board(easyPuzzle);
            boolean solved = solver.solve(board);
            
            assertTrue(solved, "Solver deve conseguir resolver puzzle simples");
            assertTrue(board.isSolved(), "Tabuleiro deve estar completamente resolvido");
            assertEquals(81, board.getFilledCells(), "Todas as células devem estar preenchidas");
            return true;
        });
        
        runTest("Verificar se puzzle é solucionável", () -> {
            SudokuSolver solver = new SudokuSolver();
            PuzzleGenerator generator = new PuzzleGenerator();
            
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            assertTrue(solver.isSolvable(puzzle), "Puzzle gerado deve ser solucionável");
            return true;
        });
        
        runTest("Tabuleiro já resolvido", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            // Solução completa válida
            int[][] completeSolution = {
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
            
            Board solvedBoard = new Board(completeSolution);
            boolean result = solver.solve(solvedBoard);
            
            assertTrue(result, "Tabuleiro já resolvido deve retornar true");
            assertTrue(solvedBoard.isSolved(), "Tabuleiro deve permanecer resolvido");
            return true;
        });
        
        runTest("Tabuleiro impossível", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            // Puzzle impossível (dois números iguais na mesma linha)
            int[][] impossiblePuzzle = {
                {1, 1, 0, 0, 0, 0, 0, 0, 0}, // Dois 1s na mesma linha
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            
            Board board = new Board(impossiblePuzzle);
            boolean solvable = solver.isSolvable(board);
            
            assertFalse(solvable, "Puzzle impossível não deve ser solucionável");
            return true;
        });
    }
    
    /**
     * Testa validação de soluções.
     */
    private static void testSolutionValidation() {
        runTest("Verificar solução correta", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            int[][] correctSolution = {
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
            
            Board board = new Board(correctSolution);
            assertTrue(solver.verifySolution(board), "Solução correta deve ser validada");
            return true;
        });
        
        runTest("Rejeitar solução incorreta", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            int[][] incorrectSolution = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1}, // Linha inválida
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {3, 3, 3, 3, 3, 3, 3, 3, 3},
                {4, 4, 4, 4, 4, 4, 4, 4, 4},
                {5, 5, 5, 5, 5, 5, 5, 5, 5},
                {6, 6, 6, 6, 6, 6, 6, 6, 6},
                {7, 7, 7, 7, 7, 7, 7, 7, 7},
                {8, 8, 8, 8, 8, 8, 8, 8, 8},
                {9, 9, 9, 9, 9, 9, 9, 9, 9}
            };
            
            Board board = new Board(incorrectSolution);
            assertFalse(solver.verifySolution(board), "Solução incorreta deve ser rejeitada");
            return true;
        });
        
        runTest("Solução incompleta", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            Board incompleteBoard = new Board();
            incompleteBoard.setValue(0, 0, 5);
            
            assertFalse(solver.verifySolution(incompleteBoard), 
                       "Solução incompleta deve ser rejeitada");
            return true;
        });
    }
    
    /**
     * Testa sistema de dicas.
     */
    private static void testHints() {
        runTest("Obter dica para puzzle simples", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            int[][] puzzle = {
                {5, 3, 4, 6, 7, 8, 9, 1, 0}, // Falta apenas o 2
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
            };
            
            Board board = new Board(puzzle);
            int[] hint = solver.getHint(board);
            
            assertNotNull(hint, "Dica deve ser fornecida");
            assertEquals(3, hint.length, "Dica deve ter 3 elementos [row, col, value]");
            assertEquals(0, hint[0], "Linha da dica deve ser 0");
            assertEquals(8, hint[1], "Coluna da dica deve ser 8");
            assertEquals(2, hint[2], "Valor da dica deve ser 2");
            return true;
        });
        
        runTest("Dica para tabuleiro completo", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            int[][] completeSolution = {
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
            
            Board board = new Board(completeSolution);
            int[] hint = solver.getHint(board);
            
            assertNull(hint, "Não deve fornecer dica para tabuleiro completo");
            return true;
        });
    }
    
    /**
     * Testa cálculo de valores possíveis.
     */
    private static void testPossibleValues() {
        runTest("Valores possíveis em tabuleiro vazio", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board emptyBoard = new Board();
            
            int[] possibleValues = solver.getPossibleValues(emptyBoard, 0, 0);
            
            assertEquals(9, possibleValues.length, "Tabuleiro vazio deve permitir todos os 9 valores");
            
            // Verificar se contém valores 1-9
            for (int i = 1; i <= 9; i++) {
                boolean found = false;
                for (int value : possibleValues) {
                    if (value == i) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found, "Deve conter valor " + i);
            }
            return true;
        });
        
        runTest("Valores possíveis com restrições", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board board = new Board();
            
            // Adicionar algumas restrições
            board.setValue(0, 1, 5); // Mesma linha
            board.setValue(1, 0, 3); // Mesma coluna
            board.setValue(1, 1, 7); // Mesmo subgrid
            
            int[] possibleValues = solver.getPossibleValues(board, 0, 0);
            
            assertTrue(possibleValues.length < 9, "Deve ter menos que 9 valores possíveis");
            
            // Verificar que valores restritos não estão incluídos
            for (int value : possibleValues) {
                assertTrue(value != 5, "Não deve conter 5 (restrição de linha)");
                assertTrue(value != 3, "Não deve conter 3 (restrição de coluna)");
                assertTrue(value != 7, "Não deve conter 7 (restrição de subgrid)");
            }
            return true;
        });
        
        runTest("Valores possíveis para célula preenchida", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board board = new Board();
            board.setValue(0, 0, 8);
            
            int[] possibleValues = solver.getPossibleValues(board, 0, 0);
            
            assertEquals(0, possibleValues.length, "Célula preenchida não deve ter valores possíveis");
            return true;
        });
        
        runTest("Apenas um valor possível", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board board = new Board();
            
            // Preencher linha, coluna e subgrid deixando apenas uma opção
            for (int i = 1; i <= 8; i++) {
                board.setValue(0, i, i); // Linha: valores 1-8
            }
            for (int i = 1; i <= 8; i++) {
                if (i != 9) { // Evitar conflito com posição [0,8]
                    board.setValue(i, 0, 9); // Coluna: todos 9s exceto na linha 0
                }
            }
            
            int[] possibleValues = solver.getPossibleValues(board, 0, 0);
            
            assertEquals(1, possibleValues.length, "Deve ter exatamente um valor possível");
            assertEquals(9, possibleValues[0], "Único valor possível deve ser 9");
            return true;
        });
    }
    
    /**
     * Testa contagem de soluções.
     */
    private static void testSolutionCounting() {
        runTest("Contar soluções de puzzle válido", () -> {
            SudokuSolver solver = new SudokuSolver();
            PuzzleGenerator generator = new PuzzleGenerator();
            
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            int solutionCount = solver.countSolutions(puzzle);
            
            assertTrue(solutionCount >= 1, "Puzzle válido deve ter pelo menos uma solução");
            return true;
        });
        
        runTest("Verificar solução única", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            // Puzzle com quase todas as células preenchidas (deve ter solução única)
            int[][] nearComplete = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 0} // Apenas última célula vazia
            };
            
            Board board = new Board(nearComplete);
            boolean hasUnique = solver.hasUniqueSolution(board);
            
            assertTrue(hasUnique, "Puzzle quase completo deve ter solução única");
            return true;
        });
        
        runTest("Tabuleiro impossível tem zero soluções", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            int[][] impossible = {
                {1, 1, 0, 0, 0, 0, 0, 0, 0}, // Dois 1s na mesma linha
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            
            Board board = new Board(impossible);
            int count = solver.countSolutions(board);
            
            assertEquals(0, count, "Puzzle impossível deve ter zero soluções");
            return true;
        });
    }
    
    /**
     * Testa estimativa de dificuldade.
     */
    private static void testDifficultyEstimation() {
        runTest("Estimar dificuldade de puzzles", () -> {
            SudokuSolver solver = new SudokuSolver();
            PuzzleGenerator generator = new PuzzleGenerator();
            
            Board easyPuzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            Board expertPuzzle = generator.generateSamplePuzzle(GameState.Difficulty.EXPERT);
            
            int easyDifficulty = solver.estimateDifficulty(easyPuzzle);
            int expertDifficulty = solver.estimateDifficulty(expertPuzzle);
            
            assertInRange(easyDifficulty, 1, 10, "Dificuldade deve estar entre 1 e 10");
            assertInRange(expertDifficulty, 1, 10, "Dificuldade deve estar entre 1 e 10");
            
            // Expert geralmente deve ser mais difícil que fácil
            assertTrue(expertDifficulty >= easyDifficulty, 
                      "Puzzle expert deve ter dificuldade >= puzzle fácil");
            return true;
        });
        
        runTest("Dificuldade de tabuleiro vazio", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board emptyBoard = new Board();
            
            int difficulty = solver.estimateDifficulty(emptyBoard);
            assertInRange(difficulty, 1, 10, "Dificuldade deve estar no intervalo válido");
            return true;
        });
    }
    
    /**
     * Testa validação de movimentos.
     */
    private static void testMoveValidation() {
        runTest("Validar movimento correto", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board board = new Board();
            
            boolean valid = solver.validateMove(board, 0, 0, 5);
            assertTrue(valid, "Movimento válido deve ser aceito");
            return true;
        });
        
        runTest("Rejeitar movimento inválido", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board board = new Board();
            board.setValue(0, 1, 5); // Mesmo número na linha
            
            boolean valid = solver.validateMove(board, 0, 0, 5);
            assertFalse(valid, "Movimento que causa conflito deve ser rejeitado");
            return true;
        });
        
        runTest("Validar valores limítrofes", () -> {
            SudokuSolver solver = new SudokuSolver();
            Board board = new Board();
            
            assertFalse(solver.validateMove(board, 0, 0, 0), "Valor 0 deve ser inválido");
            assertTrue(solver.validateMove(board, 0, 0, 1), "Valor 1 deve ser válido");
            assertTrue(solver.validateMove(board, 0, 0, 9), "Valor 9 deve ser válido");
            assertFalse(solver.validateMove(board, 0, 0, 10), "Valor 10 deve ser inválido");
            return true;
        });
        
        runTest("Tentar alterar célula fixa", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            int[][] fixedBoard = {{5, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                 {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                 {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                 {0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0}, 
                                 {0, 0, 0, 0, 0, 0, 0, 0, 0}};
            
            Board board = new Board(fixedBoard);
            
            boolean valid = solver.validateMove(board, 0, 0, 3);
            assertFalse(valid, "Não deve conseguir alterar célula fixa");
            return true;
        });
    }
    
    /**
     * Testa performance básica do solver.
     */
    private static void testPerformance() {
        runTest("Performance de resolução", () -> {
            SudokuSolver solver = new SudokuSolver();
            PuzzleGenerator generator = new PuzzleGenerator();
            
            long startTime = System.currentTimeMillis();
            
            // Resolver múltiplos puzzles
            for (int i = 0; i < 5; i++) {
                Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
                Board copy = puzzle.copy();
                boolean solved = solver.solve(copy);
                assertTrue(solved, "Puzzle " + i + " deve ser resolvido");
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 10000, "Resolução de 5 puzzles deve levar menos de 10 segundos");
            return true;
        });
        
        runTest("Performance de contagem de soluções", () -> {
            SudokuSolver solver = new SudokuSolver();
            
            long startTime = System.currentTimeMillis();
            
            Board emptyBoard = new Board();
            // Adicionar algumas células para evitar explosão combinatória
            emptyBoard.setValue(0, 0, 1);
            emptyBoard.setValue(1, 1, 2);
            emptyBoard.setValue(2, 2, 3);
            
            int count = solver.countSolutions(emptyBoard);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(count >= 0, "Contagem deve ser >= 0");
            assertTrue(duration < 5000, "Contagem de soluções deve levar menos de 5 segundos");
            return true;
        });
    }
}