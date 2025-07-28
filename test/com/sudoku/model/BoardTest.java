package com.sudoku.model;

import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe Board.
 * Testa todas as funcionalidades do tabuleiro de Sudoku.
 */
public class BoardTest {
    
    /**
     * Executa todos os testes da classe Board.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe Board");
        
        testBoardCreation();
        testBoardValues();
        testBoardValidation();
        testBoardConstraints();
        testBoardSolution();
        testBoardCopy();
        testBoardConversion();
        testBoardStatistics();
    }
    
    /**
     * Testa a criação de tabuleiros.
     */
    private static void testBoardCreation() {
        runTest("Criação de tabuleiro vazio", () -> {
            Board board = new Board();
            assertTrue(board.isEmpty(), "Tabuleiro deve estar vazio");
            assertEquals(0, board.getFilledCells(), "Deve ter 0 células preenchidas");
            
            // Verificar se todas as células estão vazias
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    Cell cell = board.getCell(row, col);
                    assertNotNull(cell, "Célula não deve ser null");
                    assertTrue(cell.isEmpty(), "Célula deve estar vazia");
                }
            }
            return true;
        });
        
        runTest("Criação com matriz inicial", () -> {
            int[][] initialData = {
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
            
            Board board = new Board(initialData);
            
            assertEquals(5, board.getCell(0, 0).getValue(), "Célula [0,0] deve ter valor 5");
            assertEquals(3, board.getCell(0, 1).getValue(), "Célula [0,1] deve ter valor 3");
            assertTrue(board.getCell(0, 2).isEmpty(), "Célula [0,2] deve estar vazia");
            
            assertFalse(board.isEmpty(), "Tabuleiro não deve estar vazio");
            assertTrue(board.getFilledCells() > 0, "Deve ter células preenchidas");
            return true;
        });
        
        runTest("Matriz inicial inválida", () -> {
            int[][] invalidMatrix = {{1, 2, 3}, {4, 5, 6}}; // Não é 9x9
            
            assertThrows(IllegalArgumentException.class, () -> new Board(invalidMatrix), 
                        "Deve lançar exceção para matriz inválida");
            return true;
        });
    }
    
    /**
     * Testa operações com valores do tabuleiro.
     */
    private static void testBoardValues() {
        runTest("Definir valor válido", () -> {
            Board board = new Board();
            boolean result = board.setValue(0, 0, 5);
            
            assertTrue(result, "Deve conseguir definir valor válido");
            assertEquals(5, board.getCell(0, 0).getValue(), "Valor deve ser definido");
            assertEquals(1, board.getFilledCells(), "Deve ter 1 célula preenchida");
            assertFalse(board.isEmpty(), "Tabuleiro não deve estar vazio");
            return true;
        });
        
        runTest("Limpar célula", () -> {
            Board board = new Board();
            board.setValue(1, 1, 7);
            assertEquals(1, board.getFilledCells(), "Deve ter 1 célula preenchida");
            
            boolean result = board.setValue(1, 1, 0);
            assertTrue(result, "Deve conseguir limpar célula");
            assertTrue(board.getCell(1, 1).isEmpty(), "Célula deve estar vazia");
            assertEquals(0, board.getFilledCells(), "Deve ter 0 células preenchidas");
            return true;
        });
        
        runTest("Posições inválidas", () -> {
            Board board = new Board();
            
            assertThrows(IllegalArgumentException.class, () -> board.getCell(-1, 0), 
                        "Deve lançar exceção para linha negativa");
            assertThrows(IllegalArgumentException.class, () -> board.getCell(0, -1), 
                        "Deve lançar exceção para coluna negativa");
            assertThrows(IllegalArgumentException.class, () -> board.getCell(9, 0), 
                        "Deve lançar exceção para linha >= 9");
            assertThrows(IllegalArgumentException.class, () -> board.getCell(0, 9), 
                        "Deve lançar exceção para coluna >= 9");
            return true;
        });
        
        runTest("Tentar alterar célula fixa", () -> {
            int[][] data = new int[9][9];
            data[0][0] = 5;
            Board board = new Board(data);
            
            // Células com valores iniciais são marcadas como fixas
            boolean result = board.setValue(0, 0, 3);
            assertFalse(result, "Não deve conseguir alterar célula fixa");
            assertEquals(5, board.getCell(0, 0).getValue(), "Valor deve permanecer inalterado");
            return true;
        });
    }
    
    /**
     * Testa validações de jogadas.
     */
    private static void testBoardValidation() {
        runTest("Jogada válida em tabuleiro vazio", () -> {
            Board board = new Board();
            
            for (int value = 1; value <= 9; value++) {
                assertTrue(board.isValidMove(0, 0, value), 
                          "Qualquer valor deve ser válido em tabuleiro vazio");
            }
            return true;
        });
        
        runTest("Validação de linha", () -> {
            Board board = new Board();
            board.setValue(0, 0, 5);
            
            assertFalse(board.isValidMove(0, 1, 5), "Não deve aceitar mesmo número na linha");
            assertTrue(board.isValidMove(0, 1, 3), "Deve aceitar número diferente na linha");
            return true;
        });
        
        runTest("Validação de coluna", () -> {
            Board board = new Board();
            board.setValue(0, 0, 7);
            
            assertFalse(board.isValidMove(1, 0, 7), "Não deve aceitar mesmo número na coluna");
            assertTrue(board.isValidMove(1, 0, 2), "Deve aceitar número diferente na coluna");
            return true;
        });
        
        runTest("Validação de subgrid", () -> {
            Board board = new Board();
            board.setValue(0, 0, 9);
            
            // Testar outras posições do mesmo subgrid 3x3
            assertFalse(board.isValidMove(1, 1, 9), "Não deve aceitar mesmo número no subgrid");
            assertFalse(board.isValidMove(2, 2, 9), "Não deve aceitar mesmo número no subgrid");
            assertTrue(board.isValidMove(1, 1, 4), "Deve aceitar número diferente no subgrid");
            return true;
        });
        
        runTest("Valores inválidos", () -> {
            Board board = new Board();
            
            assertFalse(board.isValidMove(0, 0, 0), "Valor 0 não deve ser válido para jogada");
            assertFalse(board.isValidMove(0, 0, -1), "Valor negativo não deve ser válido");
            assertFalse(board.isValidMove(0, 0, 10), "Valor > 9 não deve ser válido");
            return true;
        });
    }
    
    /**
     * Testa todas as restrições do Sudoku.
     */
    private static void testBoardConstraints() {
        runTest("Restrições completas de linha", () -> {
            Board board = new Board();
            
            // Preencher primeira linha com valores 1-8
            for (int col = 0; col < 8; col++) {
                board.setValue(0, col, col + 1);
            }
            
            // Apenas o valor 9 deve ser válido na última posição
            for (int value = 1; value <= 8; value++) {
                assertFalse(board.isValidMove(0, 8, value), 
                           "Valor " + value + " não deve ser válido (já existe na linha)");
            }
            assertTrue(board.isValidMove(0, 8, 9), "Valor 9 deve ser válido");
            return true;
        });
        
        runTest("Restrições completas de coluna", () -> {
            Board board = new Board();
            
            // Preencher primeira coluna com valores 1-8
            for (int row = 0; row < 8; row++) {
                board.setValue(row, 0, row + 1);
            }
            
            // Apenas o valor 9 deve ser válido na última posição
            for (int value = 1; value <= 8; value++) {
                assertFalse(board.isValidMove(8, 0, value), 
                           "Valor " + value + " não deve ser válido (já existe na coluna)");
            }
            assertTrue(board.isValidMove(8, 0, 9), "Valor 9 deve ser válido");
            return true;
        });
        
        runTest("Restrições completas de subgrid", () -> {
            Board board = new Board();
            
            // Preencher subgrid 3x3 superior esquerdo com valores 1-8
            int value = 1;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (value <= 8) {
                        board.setValue(row, col, value++);
                    }
                }
            }
            
            // Apenas o valor 9 deve ser válido na última posição do subgrid
            for (int testValue = 1; testValue <= 8; testValue++) {
                assertFalse(board.isValidMove(2, 2, testValue), 
                           "Valor " + testValue + " não deve ser válido (já existe no subgrid)");
            }
            assertTrue(board.isValidMove(2, 2, 9), "Valor 9 deve ser válido");
            return true;
        });
    }
    
    /**
     * Testa detecção de soluções.
     */
    private static void testBoardSolution() {
        runTest("Tabuleiro incompleto não é solução", () -> {
            Board board = new Board();
            board.setValue(0, 0, 5);
            
            assertFalse(board.isSolved(), "Tabuleiro incompleto não deve ser considerado resolvido");
            return true;
        });
        
        runTest("Tabuleiro completo válido", () -> {
            // Solução válida conhecida
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
            
            Board board = new Board(solution);
            assertTrue(board.isSolved(), "Solução válida deve ser reconhecida");
            assertEquals(81, board.getFilledCells(), "Deve ter todas as células preenchidas");
            return true;
        });
        
        runTest("Tabuleiro completo inválido", () -> {
            int[][] invalid = new int[9][9];
            
            // Preencher com valores inválidos (todos 1s)
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    invalid[row][col] = 1;
                }
            }
            
            Board board = new Board(invalid);
            assertFalse(board.isSolved(), "Tabuleiro inválido não deve ser considerado resolvido");
            return true;
        });
    }
    
    /**
     * Testa funcionalidade de cópia.
     */
    private static void testBoardCopy() {
        runTest("Cópia de tabuleiro", () -> {
            Board original = new Board();
            original.setValue(0, 0, 5);
            original.setValue(1, 1, 7);
            original.setValue(2, 2, 9);
            
            Board copy = original.copy();
            
            assertEquals(original.getFilledCells(), copy.getFilledCells(), 
                        "Cópia deve ter mesmo número de células preenchidas");
            
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    assertEquals(original.getCell(row, col).getValue(), 
                               copy.getCell(row, col).getValue(),
                               "Valores devem ser iguais na posição [" + row + "," + col + "]");
                }
            }
            return true;
        });
        
        runTest("Independência após cópia", () -> {
            Board original = new Board();
            original.setValue(0, 0, 3);
            
            Board copy = original.copy();
            
            // Modificar original não deve afetar cópia
            original.setValue(0, 1, 6);
            
            assertEquals(3, copy.getCell(0, 0).getValue(), "Cópia deve manter valores originais");
            assertTrue(copy.getCell(0, 1).isEmpty(), "Cópia não deve ser afetada por mudanças no original");
            return true;
        });
    }
    
    /**
     * Testa conversões do tabuleiro.
     */
    private static void testBoardConversion() {
        runTest("Conversão para array", () -> {
            Board board = new Board();
            board.setValue(0, 0, 1);
            board.setValue(4, 4, 5);
            board.setValue(8, 8, 9);
            
            int[][] array = board.toArray();
            
            assertEquals(1, array[0][0], "Array deve conter valores do tabuleiro");
            assertEquals(5, array[4][4], "Array deve conter valores do tabuleiro");
            assertEquals(9, array[8][8], "Array deve conter valores do tabuleiro");
            assertEquals(0, array[1][1], "Células vazias devem ser 0 no array");
            return true;
        });
        
        runTest("Representação string", () -> {
            Board board = new Board();
            board.setValue(0, 0, 5);
            
            String boardString = board.toString();
            assertNotNull(boardString, "ToString não deve retornar null");
            assertTrue(boardString.contains("5"), "String deve conter os valores do tabuleiro");
            assertTrue(boardString.contains("│"), "String deve conter formatação visual");
            return true;
        });
    }
    
    /**
     * Testa estatísticas do tabuleiro.
     */
    private static void testBoardStatistics() {
        runTest("Contagem de células preenchidas", () -> {
            Board board = new Board();
            assertEquals(0, board.getFilledCells(), "Tabuleiro vazio deve ter 0 células");
            
            board.setValue(0, 0, 1);
            assertEquals(1, board.getFilledCells(), "Deve contar células preenchidas");
            
            board.setValue(1, 1, 2);
            assertEquals(2, board.getFilledCells(), "Deve atualizar contagem");
            
            board.setValue(0, 0, 0); // Limpar
            assertEquals(1, board.getFilledCells(), "Deve decrementar ao limpar");
            return true;
        });
        
        runTest("Estado vazio", () -> {
            Board board = new Board();
            assertTrue(board.isEmpty(), "Tabuleiro novo deve estar vazio");
            
            board.setValue(0, 0, 5);
            assertFalse(board.isEmpty(), "Tabuleiro com células não deve estar vazio");
            
            board.setValue(0, 0, 0);
            assertTrue(board.isEmpty(), "Tabuleiro após limpar deve estar vazio");
            return true;
        });
        
        runTest("Limpeza de tabuleiro", () -> {
            int[][] data = {
                {1, 2, 3, 0, 0, 0, 0, 0, 0},
                {4, 5, 6, 0, 0, 0, 0, 0, 0},
                {7, 8, 9, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            
            Board board = new Board(data);
            assertFalse(board.isEmpty(), "Tabuleiro deve ter células preenchidas");
            
            board.clear();
            
            // Células fixas devem permanecer
            assertTrue(board.getCell(0, 0).isFixed(), "Células iniciais devem permanecer fixas");
            assertEquals(1, board.getCell(0, 0).getValue(), "Valores fixos devem permanecer");
            
            // Células adicionadas devem ser removidas (se houver)
            return true;
        });
    }
}