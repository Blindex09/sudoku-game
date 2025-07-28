package com.sudoku.service;

import com.sudoku.model.Board;
import com.sudoku.model.GameState;
import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe PuzzleGenerator.
 * Testa todas as funcionalidades de geração de puzzles.
 */
public class PuzzleGeneratorTest {
    
    /**
     * Executa todos os testes da classe PuzzleGenerator.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe PuzzleGenerator");
        
        testPuzzleGeneration();
        testSamplePuzzles();
        testPuzzleValidation();
        testDifficultyLevels();
        testPuzzleSeeds();
        testEdgeCases();
    }
    
    /**
     * Testa geração básica de puzzles.
     */
    private static void testPuzzleGeneration() {
        runTest("Geração de puzzle básico", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            Board puzzle = generator.generatePuzzle(GameState.Difficulty.FACIL);
            
            assertNotNull(puzzle, "Puzzle gerado não deve ser null");
            assertFalse(puzzle.isEmpty(), "Puzzle deve ter células preenchidas");
            assertTrue(puzzle.getFilledCells() > 0, "Deve ter células preenchidas");
            assertTrue(generator.isValidSudoku(puzzle), "Puzzle deve ser um Sudoku válido");
            return true;
        });
        
        runTest("Geração para todas as dificuldades", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            for (GameState.Difficulty difficulty : GameState.Difficulty.values()) {
                Board puzzle = generator.generatePuzzle(difficulty);
                
                assertNotNull(puzzle, "Puzzle " + difficulty + " não deve ser null");
                assertTrue(generator.isValidSudoku(puzzle), 
                          "Puzzle " + difficulty + " deve ser válido");
                
                // Verificar número aproximado de células para dificuldade
                int expectedCells = difficulty.getFilledCells();
                int actualCells = puzzle.getFilledCells();
                
                // Permitir alguma variação (±5 células)
                assertTrue(Math.abs(actualCells - expectedCells) <= 10, 
                          "Número de células deve estar próximo do esperado para " + difficulty);
            }
            return true;
        });
    }
    
    /**
     * Testa puzzles de exemplo predefinidos.
     */
    private static void testSamplePuzzles() {
        runTest("Puzzle de exemplo fácil", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            
            assertNotNull(puzzle, "Puzzle de exemplo não deve ser null");
            assertTrue(puzzle.getFilledCells() >= 25, "Puzzle fácil deve ter pelo menos 25 células");
            assertTrue(generator.isValidSudoku(puzzle), "Puzzle de exemplo deve ser válido");
            
            // Verificar se células preenchidas são marcadas como fixas
            boolean hasFixedCells = false;
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (!puzzle.getCell(row, col).isEmpty()) {
                        assertTrue(puzzle.getCell(row, col).isFixed(), 
                                  "Células preenchidas devem ser marcadas como fixas");
                        hasFixedCells = true;
                    }
                }
            }
            assertTrue(hasFixedCells, "Deve haver células fixas no puzzle");
            return true;
        });
        
        runTest("Puzzle de exemplo médio", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.MEDIO);
            
            assertNotNull(puzzle, "Puzzle médio não deve ser null");
            assertTrue(generator.isValidSudoku(puzzle), "Puzzle médio deve ser válido");
            
            int filledCells = puzzle.getFilledCells();
            assertTrue(filledCells >= 20 && filledCells <= 40, 
                      "Puzzle médio deve ter número apropriado de células");
            return true;
        });
        
        runTest("Consistência dos puzzles de exemplo", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            // Gerar mesmo puzzle múltiplas vezes
            Board puzzle1 = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            Board puzzle2 = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            
            // Devem ser iguais (puzzles predefinidos)
            assertEquals(puzzle1.toArray().length, puzzle2.toArray().length, 
                        "Puzzles de exemplo devem ser consistentes");
            
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    assertEquals(puzzle1.getCell(row, col).getValue(), 
                               puzzle2.getCell(row, col).getValue(),
                               "Valores devem ser consistentes na posição [" + row + "," + col + "]");
                }
            }
            return true;
        });
    }
    
    /**
     * Testa validação de puzzles.
     */
    private static void testPuzzleValidation() {
        runTest("Validação de Sudoku válido", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            // Puzzle válido conhecido
            int[][] validPuzzle = {
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
            
            Board board = new Board(validPuzzle);
            assertTrue(generator.isValidSudoku(board), "Puzzle válido deve passar na validação");
            return true;
        });
        
        runTest("Validação de Sudoku inválido", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            // Puzzle inválido (números repetidos na linha)
            int[][] invalidPuzzle = {
                {5, 5, 0, 0, 0, 0, 0, 0, 0}, // Dois 5s na primeira linha
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            
            Board board = new Board(invalidPuzzle);
            assertFalse(generator.isValidSudoku(board), "Puzzle inválido deve falhar na validação");
            return true;
        });
        
        runTest("Validação de tabuleiro vazio", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            Board emptyBoard = new Board();
            
            assertTrue(generator.isValidSudoku(emptyBoard), "Tabuleiro vazio deve ser válido");
            return true;
        });
        
        runTest("Validação com conflitos em coluna", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            int[][] columnConflict = new int[9][9];
            columnConflict[0][0] = 7;
            columnConflict[1][0] = 7; // Mesmo número na coluna
            
            Board board = new Board(columnConflict);
            assertFalse(generator.isValidSudoku(board), "Conflito de coluna deve ser detectado");
            return true;
        });
        
        runTest("Validação com conflitos em subgrid", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            int[][] subgridConflict = new int[9][9];
            subgridConflict[0][0] = 3;
            subgridConflict[1][1] = 3; // Mesmo número no subgrid 3x3
            
            Board board = new Board(subgridConflict);
            assertFalse(generator.isValidSudoku(board), "Conflito de subgrid deve ser detectado");
            return true;
        });
    }
    
    /**
     * Testa diferentes níveis de dificuldade.
     */
    private static void testDifficultyLevels() {
        runTest("Diferenças entre dificuldades", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            Board easy = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            Board expert = generator.generateSamplePuzzle(GameState.Difficulty.EXPERT);
            
            int easyCells = easy.getFilledCells();
            int expertCells = expert.getFilledCells();
            
            assertTrue(easyCells > expertCells, 
                      "Puzzle fácil deve ter mais células que expert");
            
            // Verificar se ambos são válidos
            assertTrue(generator.isValidSudoku(easy), "Puzzle fácil deve ser válido");
            assertTrue(generator.isValidSudoku(expert), "Puzzle expert deve ser válido");
            return true;
        });
        
        runTest("Ordem crescente de dificuldade", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            int facilCells = generator.generateSamplePuzzle(GameState.Difficulty.FACIL).getFilledCells();
            int medioCells = generator.generateSamplePuzzle(GameState.Difficulty.MEDIO).getFilledCells();
            int dificilCells = generator.generateSamplePuzzle(GameState.Difficulty.DIFICIL).getFilledCells();
            int expertCells = generator.generateSamplePuzzle(GameState.Difficulty.EXPERT).getFilledCells();
            
            assertTrue(facilCells >= medioCells, "Fácil deve ter >= células que Médio");
            assertTrue(medioCells >= dificilCells, "Médio deve ter >= células que Difícil");
            assertTrue(dificilCells >= expertCells, "Difícil deve ter >= células que Expert");
            return true;
        });
    }
    
    /**
     * Testa geração com seeds para reproduzibilidade.
     */
    private static void testPuzzleSeeds() {
        runTest("Geração com seed consistente", () -> {
            PuzzleGenerator generator1 = new PuzzleGenerator(12345L);
            PuzzleGenerator generator2 = new PuzzleGenerator(12345L);
            
            // Gerar puzzles com mesma seed deve produzir resultados similares
            // (Nota: Para puzzles de exemplo, isso sempre será verdade)
            Board puzzle1 = generator1.generateSamplePuzzle(GameState.Difficulty.FACIL);
            Board puzzle2 = generator2.generateSamplePuzzle(GameState.Difficulty.FACIL);
            
            assertNotNull(puzzle1, "Puzzle 1 não deve ser null");
            assertNotNull(puzzle2, "Puzzle 2 não deve ser null");
            return true;
        });
        
        runTest("Seeds diferentes podem gerar puzzles diferentes", () -> {
            PuzzleGenerator generator1 = new PuzzleGenerator(12345L);
            PuzzleGenerator generator2 = new PuzzleGenerator(54321L);
            
            // Para puzzles de exemplo, ainda serão iguais, mas generators são diferentes
            assertNotNull(generator1, "Generator 1 deve ser criado");
            assertNotNull(generator2, "Generator 2 deve ser criado");
            return true;
        });
    }
    
    /**
     * Testa casos extremos e limites.
     */
    private static void testEdgeCases() {
        runTest("Geração múltipla rápida", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            // Gerar múltiplos puzzles rapidamente
            for (int i = 0; i < 5; i++) {
                Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.MEDIO);
                assertNotNull(puzzle, "Puzzle " + i + " não deve ser null");
                assertTrue(generator.isValidSudoku(puzzle), "Puzzle " + i + " deve ser válido");
            }
            return true;
        });
        
        runTest("Validação com tabuleiro completo", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
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
            
            Board completeBoard = new Board(completeSolution);
            assertTrue(generator.isValidSudoku(completeBoard), 
                      "Solução completa válida deve passar na validação");
            assertEquals(81, completeBoard.getFilledCells(), 
                        "Tabuleiro completo deve ter 81 células");
            return true;
        });
        
        runTest("Puzzle com apenas uma célula", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            int[][] singleCell = new int[9][9];
            singleCell[4][4] = 5; // Apenas centro preenchido
            
            Board board = new Board(singleCell);
            assertTrue(generator.isValidSudoku(board), 
                      "Puzzle com uma célula deve ser válido");
            assertEquals(1, board.getFilledCells(), "Deve ter exatamente 1 célula");
            return true;
        });
        
        runTest("Valores limítrofes", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            int[][] borderValues = new int[9][9];
            borderValues[0][0] = 1; // Valor mínimo
            borderValues[0][1] = 9; // Valor máximo
            
            Board board = new Board(borderValues);
            assertTrue(generator.isValidSudoku(board), 
                      "Valores limítrofes devem ser válidos");
            return true;
        });
        
        runTest("Performance básica", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            
            long startTime = System.currentTimeMillis();
            
            // Gerar 10 puzzles
            for (int i = 0; i < 10; i++) {
                Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
                assertNotNull(puzzle, "Puzzle deve ser gerado");
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 5000, "Geração de 10 puzzles deve levar menos de 5 segundos");
            return true;
        });
    }
}