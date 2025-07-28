package com.sudoku.integration;

import com.sudoku.model.Board;
import com.sudoku.model.GameState;
import com.sudoku.service.GameManager;
import com.sudoku.service.PuzzleGenerator;
import com.sudoku.service.SudokuSolver;
import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes de integração para todo o sistema Sudoku.
 * Testa o funcionamento conjunto de todos os componentes.
 */
public class SystemIntegrationTest {
    
    /**
     * Executa todos os testes de integração.
     */
    public static void runAllTests() {
        startSuite("Testes de Integração do Sistema");
        
        testCompleteGameFlow();
        testCrossComponentInteraction();
        testDataConsistency();
        testErrorPropagation();
        testPerformanceIntegration();
        testStateManagement();
        testEndToEndScenarios();
    }
    
    /**
     * Testa fluxo completo de um jogo.
     */
    private static void testCompleteGameFlow() {
        runTest("Jogo completo do início ao fim", () -> {
            // 1. Criar e iniciar jogo
            GameManager manager = new GameManager();
            boolean started = manager.startNewGame(GameState.Difficulty.FACIL);
            assertTrue(started, "Deve conseguir iniciar novo jogo");
            
            // 2. Verificar estado inicial
            assertTrue(manager.hasActiveGame(), "Deve ter jogo ativo");
            GameState state = manager.getGameState();
            assertEquals(GameState.Status.JOGANDO, state.getStatus(), "Status deve ser JOGANDO");
            
            // 3. Fazer algumas jogadas válidas
            Board board = manager.getCurrentBoard();
            int movesCount = 0;
            
            for (int row = 0; row < Board.SIZE && movesCount < 5; row++) {
                for (int col = 0; col < Board.SIZE && movesCount < 5; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (board.isValidMove(row, col, value)) {
                                GameManager.MoveResult result = manager.makeMove(row, col, value);
                                if (result.isSuccess()) {
                                    movesCount++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            assertTrue(movesCount > 0, "Deve ter feito pelo menos uma jogada");
            assertTrue(state.getMoves() >= movesCount, "Contador de jogadas deve refletir jogadas feitas");
            
            // 4. Usar dica
            GameManager.HintResult hint = manager.getHint();
            if (hint != null && hint.isSuccess()) {
                assertTrue(state.getHints() > 0, "Contador de dicas deve incrementar");
            }
            
            // 5. Pausar e resumir
            boolean paused = manager.pauseGame();
            assertTrue(paused, "Deve conseguir pausar jogo");
            assertEquals(GameState.Status.PAUSADO, state.getStatus(), "Status deve ser PAUSADO");
            
            boolean resumed = manager.resumeGame();
            assertTrue(resumed, "Deve conseguir resumir jogo");
            assertEquals(GameState.Status.JOGANDO, state.getStatus(), "Status deve ser JOGANDO");
            
            // 6. Resolver automaticamente
            boolean solved = manager.solveGame();
            assertTrue(solved, "Deve conseguir resolver jogo");
            assertTrue(board.isSolved(), "Tabuleiro deve estar resolvido");
            
            return true;
        });
        
        runTest("Múltiplos jogos consecutivos", () -> {
            GameManager manager = new GameManager();
            
            // Jogar 3 jogos consecutivos
            for (int gameNum = 1; gameNum <= 3; gameNum++) {
                GameState.Difficulty difficulty = GameState.Difficulty.values()[gameNum % 4];
                
                boolean started = manager.startNewGame(difficulty);
                assertTrue(started, "Deve iniciar jogo " + gameNum);
                
                assertEquals(difficulty, manager.getGameState().getDifficulty(), 
                           "Dificuldade deve ser " + difficulty);
                
                // Fazer algumas jogadas
                Board board = manager.getCurrentBoard();
                int moves = 0;
                for (int row = 0; row < 3 && moves < 2; row++) {
                    for (int col = 0; col < 3 && moves < 2; col++) {
                        if (board.getCell(row, col).isEmpty()) {
                            for (int value = 1; value <= 9; value++) {
                                if (board.isValidMove(row, col, value)) {
                                    manager.makeMove(row, col, value);
                                    moves++;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                assertTrue(manager.getGameState().getMoves() >= moves, 
                          "Jogadas devem ser contabilizadas no jogo " + gameNum);
            }
            return true;
        });
    }
    
    /**
     * Testa interações entre componentes.
     */
    private static void testCrossComponentInteraction() {
        runTest("Integração Generator → Solver → Manager", () -> {
            // 1. Gerar puzzle
            PuzzleGenerator generator = new PuzzleGenerator();
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.MEDIO);
            assertNotNull(puzzle, "Generator deve produzir puzzle");
            
            // 2. Verificar se Solver pode validar
            SudokuSolver solver = new SudokuSolver();
            assertTrue(solver.isSolvable(puzzle), "Solver deve confirmar que puzzle é solucionável");
            
            // 3. Manager deve aceitar puzzle
            GameManager manager = new GameManager();
            boolean started = manager.startNewGame(GameState.Difficulty.MEDIO);
            assertTrue(started, "Manager deve aceitar puzzle gerado");
            
            // 4. Solver deve conseguir resolver puzzle do Manager
            Board managerBoard = manager.getCurrentBoard().copy();
            boolean solved = solver.solve(managerBoard);
            assertTrue(solved, "Solver deve resolver puzzle do Manager");
            
            return true;
        });
        
        runTest("Sincronização Board ↔ GameState", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            Board board = manager.getCurrentBoard();
            GameState state = manager.getGameState();
            
            int initialMoves = state.getMoves();
            
            // Fazer jogada através do manager
            boolean foundMove = false;
            for (int row = 0; row < Board.SIZE && !foundMove; row++) {
                for (int col = 0; col < Board.SIZE && !foundMove; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (board.isValidMove(row, col, value)) {
                                GameManager.MoveResult result = manager.makeMove(row, col, value);
                                if (result.isSuccess()) {
                                    foundMove = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            if (foundMove) {
                // Estado deve estar sincronizado
                assertTrue(state.getMoves() > initialMoves, "Estado deve refletir nova jogada");
                
                // Board e estado devem estar consistentes
                assertNotNull(state.getBoard(), "Estado deve ter referência ao tabuleiro");
            }
            
            return true;
        });
        
        runTest("Validação cruzada entre componentes", () -> {
            PuzzleGenerator generator = new PuzzleGenerator();
            SudokuSolver solver = new SudokuSolver();
            
            // Gerar puzzle e verificar com múltiplos validadores
            Board puzzle = generator.generateSamplePuzzle(GameState.Difficulty.DIFICIL);
            
            // Validação do generator
            assertTrue(generator.isValidSudoku(puzzle), "Generator deve validar próprio puzzle");
            
            // Validação do solver
            assertTrue(solver.isSolvable(puzzle), "Solver deve confirmar solucionabilidade");
            
            // Validação interna do board
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (!puzzle.getCell(row, col).isEmpty()) {
                        int value = puzzle.getCell(row, col).getValue();
                        
                        // Remover temporariamente para validar
                        puzzle.getCell(row, col).setFixed(false);
                        puzzle.getCell(row, col).setValue(0);
                        
                        assertTrue(puzzle.isValidMove(row, col, value), 
                                  "Valor original deve ser válido na posição [" + row + "," + col + "]");
                        
                        // Restaurar
                        puzzle.getCell(row, col).setValue(value);
                        puzzle.getCell(row, col).setFixed(true);
                    }
                }
            }
            
            return true;
        });
    }
    
    /**
     * Testa consistência de dados entre componentes.
     */
    private static void testDataConsistency() {
        runTest("Consistência de estado durante jogo", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            GameState state = manager.getGameState();
            Board board = manager.getCurrentBoard();
            
            // Estado inicial deve ser consistente
            assertEquals(board.getFilledCells() > 0, !board.isEmpty(), 
                        "Estado vazio do board deve ser consistente");
            assertEquals(GameState.Status.JOGANDO, state.getStatus(), 
                        "Status deve indicar jogo ativo");
            
            // Fazer jogadas e verificar consistência
            int movesCount = 0;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (board.isValidMove(row, col, value)) {
                                int beforeMoves = state.getMoves();
                                GameManager.MoveResult result = manager.makeMove(row, col, value);
                                
                                if (result.isSuccess()) {
                                    movesCount++;
                                    assertEquals(beforeMoves + 1, state.getMoves(), 
                                               "Contador de jogadas deve incrementar");
                                    assertEquals(value, board.getCell(row, col).getValue(), 
                                               "Valor deve estar no tabuleiro");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            assertTrue(movesCount > 0, "Deve ter feito pelo menos uma jogada");
            return true;
        });
        
        runTest("Integridade após reset", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.MEDIO);
            
            // Capturar estado original
            Board originalBoard = manager.getCurrentBoard().copy();
            int originalCells = originalBoard.getFilledCells();
            
            // Fazer modificações
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (originalBoard.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (manager.getCurrentBoard().isValidMove(row, col, value)) {
                                manager.makeMove(row, col, value);
                                break;
                            }
                        }
                    }
                }
            }
            
            // Reset
            boolean reset = manager.resetGame();
            assertTrue(reset, "Deve conseguir resetar");
            
            // Verificar restauração
            Board resetBoard = manager.getCurrentBoard();
            assertEquals(originalCells, resetBoard.getFilledCells(), 
                        "Número de células deve ser restaurado");
            assertEquals(0, manager.getGameState().getMoves(), 
                        "Contador de jogadas deve ser zerado");
            
            return true;
        });
    }
    
    /**
     * Testa propagação de erros entre componentes.
     */
    private static void testErrorPropagation() {
        runTest("Tratamento de erros em cascata", () -> {
            GameManager manager = new GameManager();
            
            // Tentar operações sem jogo ativo
            GameManager.MoveResult moveResult = manager.makeMove(0, 0, 5);
            assertFalse(moveResult.isSuccess(), "Jogada sem jogo deve falhar graciosamente");
            
            GameManager.HintResult hintResult = manager.getHint();
            if (hintResult != null) {
                assertFalse(hintResult.isSuccess(), "Dica sem jogo deve falhar graciosamente");
            }
            
            boolean paused = manager.pauseGame();
            assertFalse(paused, "Pausar sem jogo deve falhar graciosamente");
            
            // Sistema deve permanecer estável
            assertTrue(manager.startNewGame(GameState.Difficulty.FACIL), 
                      "Deve conseguir iniciar jogo após erros");
            
            return true;
        });
        
        runTest("Recuperação de estados inválidos", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            // Tentar jogadas inválidas
            GameManager.MoveResult result1 = manager.makeMove(-1, 0, 5);
            assertFalse(result1.isSuccess(), "Coordenada inválida deve ser rejeitada");
            
            GameManager.MoveResult result2 = manager.makeMove(0, 0, 10);
            assertFalse(result2.isSuccess(), "Valor inválido deve ser rejeitado");
            
            // Sistema deve continuar funcionando
            assertTrue(manager.isGameActive(), "Jogo deve permanecer ativo após erros");
            
            // Jogada válida deve funcionar
            Board board = manager.getCurrentBoard();
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (board.isValidMove(row, col, value)) {
                                GameManager.MoveResult validResult = manager.makeMove(row, col, value);
                                assertTrue(validResult.isSuccess(), 
                                          "Jogada válida deve funcionar após erros");
                                return true;
                            }
                        }
                    }
                }
            }
            
            return true;
        });
    }
    
    /**
     * Testa performance do sistema integrado.
     */
    private static void testPerformanceIntegration() {
        runTest("Performance de operações integradas", () -> {
            long startTime = System.currentTimeMillis();
            
            GameManager manager = new GameManager();
            
            // Ciclo completo rápido
            for (int i = 0; i < 5; i++) {
                manager.startNewGame(GameState.Difficulty.FACIL);
                
                // Fazer algumas jogadas
                Board board = manager.getCurrentBoard();
                int moves = 0;
                for (int row = 0; row < 3 && moves < 3; row++) {
                    for (int col = 0; col < 3 && moves < 3; col++) {
                        if (board.getCell(row, col).isEmpty()) {
                            for (int value = 1; value <= 9; value++) {
                                if (board.isValidMove(row, col, value)) {
                                    manager.makeMove(row, col, value);
                                    moves++;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                // Usar dica
                manager.getHint();
                
                // Reset
                manager.resetGame();
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 5000, "5 ciclos completos devem levar menos de 5 segundos");
            return true;
        });
        
        runTest("Uso de memória do sistema integrado", () -> {
            Runtime runtime = Runtime.getRuntime();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            
            // Criar múltiplos objetos e interações
            for (int i = 0; i < 10; i++) {
                GameManager manager = new GameManager();
                manager.startNewGame(GameState.Difficulty.MEDIO);
                
                SudokuSolver solver = new SudokuSolver();
                solver.isSolvable(manager.getCurrentBoard());
                
                PuzzleGenerator generator = new PuzzleGenerator();
                generator.generateSamplePuzzle(GameState.Difficulty.FACIL);
            }
            
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = (afterMemory - beforeMemory) / 1024; // KB
            
            assertTrue(memoryUsed < 5000, "Uso de memória deve ser razoável (< 5MB)");
            return true;
        });
    }
    
    /**
     * Testa gerenciamento de estado do sistema.
     */
    private static void testStateManagement() {
        runTest("Transições de estado válidas", () -> {
            GameManager manager = new GameManager();
            
            // NOVO → JOGANDO
            assertFalse(manager.hasActiveGame(), "Inicialmente não deve ter jogo ativo");
            manager.startNewGame(GameState.Difficulty.FACIL);
            assertTrue(manager.hasActiveGame(), "Deve ter jogo ativo após iniciar");
            
            // JOGANDO → PAUSADO → JOGANDO
            assertEquals(GameState.Status.JOGANDO, manager.getGameState().getStatus());
            manager.pauseGame();
            assertEquals(GameState.Status.PAUSADO, manager.getGameState().getStatus());
            manager.resumeGame();
            assertEquals(GameState.Status.JOGANDO, manager.getGameState().getStatus());
            
            // JOGANDO → ABANDONADO
            manager.abandonGame();
            assertEquals(GameState.Status.ABANDONADO, manager.getGameState().getStatus());
            assertFalse(manager.isGameActive(), "Jogo abandonado não deve estar ativo");
            
            return true;
        });
        
        runTest("Persistência de estado durante operações", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            GameState originalState = manager.getGameState();
            GameState.Difficulty originalDifficulty = originalState.getDifficulty();
            
            // Fazer várias operações
            manager.makeMove(0, 0, 5); // Pode falhar, mas não deve corromper estado
            manager.getHint();
            manager.pauseGame();
            manager.resumeGame();
            
            // Estado fundamental deve permanecer
            assertEquals(originalDifficulty, manager.getGameState().getDifficulty(), 
                        "Dificuldade deve permanecer consistente");
            assertNotNull(manager.getCurrentBoard(), "Tabuleiro deve permanecer válido");
            
            return true;
        });
    }
    
    /**
     * Testa cenários end-to-end completos.
     */
    private static void testEndToEndScenarios() {
        runTest("Cenário: Jogador novato", () -> {
            // Simular jogador que usa muitas dicas
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.FACIL);
            
            GameState state = manager.getGameState();
            int initialScore = state.getScore();
            
            // Usar várias dicas
            for (int i = 0; i < 3; i++) {
                GameManager.HintResult hint = manager.getHint();
                if (hint != null && hint.isSuccess()) {
                    // Aplicar dica
                    GameManager.MoveResult move = manager.makeMove(hint.getRow(), hint.getCol(), hint.getValue());
                    assertTrue(move.isSuccess(), "Dica deve resultar em jogada válida");
                }
            }
            
            // Pontuação deve ter diminuído devido às dicas
            assertTrue(state.getScore() < initialScore, "Pontuação deve diminuir com uso de dicas");
            assertTrue(state.getHints() > 0, "Contador de dicas deve refletir uso");
            
            return true;
        });
        
        runTest("Cenário: Jogador experiente", () -> {
            // Simular jogador que resolve rapidamente sem ajuda
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.EXPERT);
            
            GameState state = manager.getGameState();
            
            // Fazer várias jogadas válidas sem dicas
            Board board = manager.getCurrentBoard();
            int successfulMoves = 0;
            
            for (int row = 0; row < Board.SIZE && successfulMoves < 10; row++) {
                for (int col = 0; col < Board.SIZE && successfulMoves < 10; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (board.isValidMove(row, col, value)) {
                                GameManager.MoveResult result = manager.makeMove(row, col, value);
                                if (result.isSuccess()) {
                                    successfulMoves++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            assertTrue(successfulMoves > 0, "Deve conseguir fazer jogadas válidas");
            assertEquals(0, state.getHints(), "Jogador experiente não usa dicas");
            assertTrue(state.getMoves() >= successfulMoves, "Contador deve refletir jogadas");
            
            return true;
        });
        
        runTest("Cenário: Jogo interrompido e retomado", () -> {
            GameManager manager = new GameManager();
            manager.startNewGame(GameState.Difficulty.MEDIO);
            
            // Fazer algumas jogadas
            Board board = manager.getCurrentBoard();
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    if (board.getCell(row, col).isEmpty()) {
                        for (int value = 1; value <= 9; value++) {
                            if (board.isValidMove(row, col, value)) {
                                manager.makeMove(row, col, value);
                                break;
                            }
                        }
                    }
                }
            }
            
            GameState beforePause = manager.getGameState();
            int movesBeforePause = beforePause.getMoves();
            
            // Pausar por "tempo" (simular)
            manager.pauseGame();
            assertEquals(GameState.Status.PAUSADO, beforePause.getStatus());
            
            // Resumir e continuar
            manager.resumeGame();
            assertEquals(GameState.Status.JOGANDO, beforePause.getStatus());
            
            // Estado deve estar preservado
            assertEquals(movesBeforePause, beforePause.getMoves(), 
                        "Jogadas devem estar preservadas após pausa");
            
            return true;
        });
    }
}