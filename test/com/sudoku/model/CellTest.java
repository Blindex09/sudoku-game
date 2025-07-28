package com.sudoku.model;

import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe Cell.
 * Testa todas as funcionalidades de uma célula do Sudoku.
 */
public class CellTest {
    
    /**
     * Executa todos os testes da classe Cell.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe Cell");
        
        testCellCreation();
        testCellValues();
        testCellState();
        testCellValidation();
        testCellCopy();
        testCellComparison();
    }
    
    /**
     * Testa a criação de células.
     */
    private static void testCellCreation() {
        runTest("Criação de célula vazia", () -> {
            Cell cell = new Cell();
            
            assertTrue(cell.isEmpty(), "Célula deve estar vazia");
            assertEquals(0, cell.getValue(), "Valor deve ser 0");
            assertFalse(cell.isFixed(), "Célula não deve ser fixa");
            assertFalse(cell.hasNote(), "Célula não deve ter anotações");
            return true;
        });
        
        runTest("Criação com valor inicial", () -> {
            Cell cell = new Cell(5);
            
            assertFalse(cell.isEmpty(), "Célula não deve estar vazia");
            assertEquals(5, cell.getValue(), "Valor deve ser 5");
            assertFalse(cell.isFixed(), "Célula não deve ser fixa por padrão");
            return true;
        });
        
        runTest("Criação com valor e estado fixo", () -> {
            Cell cell = new Cell(7, true);
            
            assertFalse(cell.isEmpty(), "Célula não deve estar vazia");
            assertEquals(7, cell.getValue(), "Valor deve ser 7");
            assertTrue(cell.isFixed(), "Célula deve ser fixa");
            return true;
        });
    }
    
    /**
     * Testa operações com valores.
     */
    private static void testCellValues() {
        runTest("Definir valor válido", () -> {
            Cell cell = new Cell();
            
            for (int value = 1; value <= 9; value++) {
                boolean result = cell.setValue(value);
                assertTrue(result, "Deve aceitar valor " + value);
                assertEquals(value, cell.getValue(), "Valor deve ser definido como " + value);
                assertFalse(cell.isEmpty(), "Célula não deve estar vazia");
            }
            return true;
        });
        
        runTest("Limpar célula", () -> {
            Cell cell = new Cell(3);
            assertFalse(cell.isEmpty(), "Célula deve ter valor");
            
            boolean result = cell.setValue(0);
            assertTrue(result, "Deve conseguir limpar célula");
            assertTrue(cell.isEmpty(), "Célula deve estar vazia");
            assertEquals(0, cell.getValue(), "Valor deve ser 0");
            return true;
        });
        
        runTest("Valores inválidos", () -> {
            Cell cell = new Cell();
            
            assertFalse(cell.setValue(-1), "Não deve aceitar valor negativo");
            assertFalse(cell.setValue(10), "Não deve aceitar valor > 9");
            assertFalse(cell.setValue(100), "Não deve aceitar valor muito grande");
            
            // Célula deve permanecer vazia após tentativas inválidas
            assertTrue(cell.isEmpty(), "Célula deve permanecer vazia");
            return true;
        });
        
        runTest("Modificar célula fixa", () -> {
            Cell cell = new Cell(5, true);
            
            // Tentar alterar valor
            boolean result1 = cell.setValue(3);
            assertFalse(result1, "Não deve conseguir alterar célula fixa");
            assertEquals(5, cell.getValue(), "Valor deve permanecer inalterado");
            
            // Tentar limpar
            boolean result2 = cell.setValue(0);
            assertFalse(result2, "Não deve conseguir limpar célula fixa");
            assertEquals(5, cell.getValue(), "Valor deve permanecer inalterado");
            
            return true;
        });
    }
    
    /**
     * Testa estados da célula.
     */
    private static void testCellState() {
        runTest("Estado vazio", () -> {
            Cell cell = new Cell();
            assertTrue(cell.isEmpty(), "Célula nova deve estar vazia");
            
            cell.setValue(5);
            assertFalse(cell.isEmpty(), "Célula com valor não deve estar vazia");
            
            cell.setValue(0);
            assertTrue(cell.isEmpty(), "Célula após limpar deve estar vazia");
            return true;
        });
        
        runTest("Estado fixo", () -> {
            Cell cell = new Cell();
            assertFalse(cell.isFixed(), "Célula nova não deve ser fixa");
            
            cell.setFixed(true);
            assertTrue(cell.isFixed(), "Célula deve ser marcada como fixa");
            
            cell.setFixed(false);
            assertFalse(cell.isFixed(), "Célula deve ser desmarcada como fixa");
            return true;
        });
        
        runTest("Alterar estado de célula com valor", () -> {
            Cell cell = new Cell(7);
            
            // Marcar como fixa após definir valor
            cell.setFixed(true);
            assertTrue(cell.isFixed(), "Deve conseguir marcar como fixa");
            
            // Tentar alterar valor
            assertFalse(cell.setValue(3), "Não deve alterar célula fixa");
            assertEquals(7, cell.getValue(), "Valor deve permanecer");
            
            // Desmarcar como fixa
            cell.setFixed(false);
            assertTrue(cell.setValue(3), "Deve conseguir alterar após desmarcar fixa");
            assertEquals(3, cell.getValue(), "Valor deve ser alterado");
            
            return true;
        });
    }
    
    /**
     * Testa anotações da célula.
     */
    private static void testCellNotes() {
        runTest("Adicionar anotação", () -> {
            Cell cell = new Cell();
            assertFalse(cell.hasNote(), "Célula não deve ter anotações inicialmente");
            
            cell.addNote(3);
            assertTrue(cell.hasNote(), "Célula deve ter anotações");
            assertTrue(cell.hasNote(3), "Célula deve ter anotação do número 3");
            assertFalse(cell.hasNote(5), "Célula não deve ter anotação do número 5");
            return true;
        });
        
        runTest("Múltiplas anotações", () -> {
            Cell cell = new Cell();
            
            cell.addNote(1);
            cell.addNote(5);
            cell.addNote(9);
            
            assertTrue(cell.hasNote(1), "Deve ter anotação 1");
            assertTrue(cell.hasNote(5), "Deve ter anotação 5");
            assertTrue(cell.hasNote(9), "Deve ter anotação 9");
            assertFalse(cell.hasNote(3), "Não deve ter anotação 3");
            return true;
        });
        
        runTest("Remover anotação", () -> {
            Cell cell = new Cell();
            cell.addNote(2);
            cell.addNote(4);
            cell.addNote(6);
            
            cell.removeNote(4);
            
            assertTrue(cell.hasNote(2), "Deve manter anotação 2");
            assertFalse(cell.hasNote(4), "Não deve ter anotação 4");
            assertTrue(cell.hasNote(6), "Deve manter anotação 6");
            return true;
        });
        
        runTest("Limpar anotações", () -> {
            Cell cell = new Cell();
            cell.addNote(1);
            cell.addNote(3);
            cell.addNote(7);
            
            assertTrue(cell.hasNote(), "Deve ter anotações");
            
            cell.clearNotes();
            
            assertFalse(cell.hasNote(), "Não deve ter anotações após limpar");
            assertFalse(cell.hasNote(1), "Não deve ter anotação específica");
            return true;
        });
        
        runTest("Anotações com valor definido", () -> {
            Cell cell = new Cell();
            cell.addNote(2);
            cell.addNote(8);
            
            // Definir valor deve limpar anotações
            cell.setValue(5);
            
            assertFalse(cell.hasNote(), "Anotações devem ser limpas ao definir valor");
            assertEquals(5, cell.getValue(), "Valor deve estar definido");
            return true;
        });
    }
    
    /**
     * Testa validações da célula.
     */
    private static void testCellValidation() {
        runTest("Valores válidos", () -> {
            Cell cell = new Cell();
            
            for (int value = 0; value <= 9; value++) {
                assertTrue(Cell.isValidValue(value), "Valor " + value + " deve ser válido");
            }
            return true;
        });
        
        runTest("Valores inválidos", () -> {
            assertFalse(Cell.isValidValue(-1), "Valor -1 deve ser inválido");
            assertFalse(Cell.isValidValue(10), "Valor 10 deve ser inválido");
            assertFalse(Cell.isValidValue(-100), "Valor -100 deve ser inválido");
            assertFalse(Cell.isValidValue(100), "Valor 100 deve ser inválido");
            return true;
        });
        
        runTest("Validação antes de definir", () -> {
            Cell cell = new Cell();
            
            // Valores válidos devem ser aceitos
            assertTrue(cell.setValue(5), "Valor válido deve ser aceito");
            
            // Valores inválidos devem ser rejeitados
            int originalValue = cell.getValue();
            assertFalse(cell.setValue(-1), "Valor inválido deve ser rejeitado");
            assertEquals(originalValue, cell.getValue(), "Valor deve permanecer inalterado");
            
            return true;
        });
    }
    
    /**
     * Testa funcionalidade de cópia.
     */
    private static void testCellCopy() {
        runTest("Cópia de célula vazia", () -> {
            Cell original = new Cell();
            Cell copy = original.copy();
            
            assertTrue(copy.isEmpty(), "Cópia deve estar vazia");
            assertEquals(original.getValue(), copy.getValue(), "Valores devem ser iguais");
            assertEquals(original.isFixed(), copy.isFixed(), "Estados fixos devem ser iguais");
            return true;
        });
        
        runTest("Cópia de célula com valor", () -> {
            Cell original = new Cell(7, true);
            Cell copy = original.copy();
            
            assertEquals(original.getValue(), copy.getValue(), "Valores devem ser iguais");
            assertEquals(original.isFixed(), copy.isFixed(), "Estados fixos devem ser iguais");
            assertEquals(original.isEmpty(), copy.isEmpty(), "Estados vazios devem ser iguais");
            return true;
        });
        
        runTest("Cópia com anotações", () -> {
            Cell original = new Cell();
            original.addNote(1);
            original.addNote(5);
            original.addNote(9);
            
            Cell copy = original.copy();
            
            assertEquals(original.hasNote(), copy.hasNote(), "Estado de anotações deve ser igual");
            assertTrue(copy.hasNote(1), "Cópia deve ter anotação 1");
            assertTrue(copy.hasNote(5), "Cópia deve ter anotação 5");
            assertTrue(copy.hasNote(9), "Cópia deve ter anotação 9");
            return true;
        });
        
        runTest("Independência após cópia", () -> {
            Cell original = new Cell(3);
            Cell copy = original.copy();
            
            // Modificar original não deve afetar cópia
            original.setValue(8);
            
            assertEquals(3, copy.getValue(), "Cópia deve manter valor original");
            assertEquals(8, original.getValue(), "Original deve ter novo valor");
            return true;
        });
    }
    
    /**
     * Testa comparações entre células.
     */
    private static void testCellComparison() {
        runTest("Igualdade de células", () -> {
            Cell cell1 = new Cell(5);
            Cell cell2 = new Cell(5);
            Cell cell3 = new Cell(3);
            
            assertTrue(cell1.equals(cell2), "Células com mesmo valor devem ser iguais");
            assertFalse(cell1.equals(cell3), "Células com valores diferentes não devem ser iguais");
            assertFalse(cell1.equals(null), "Célula não deve ser igual a null");
            return true;
        });
        
        runTest("Igualdade com estados diferentes", () -> {
            Cell cell1 = new Cell(5, false);
            Cell cell2 = new Cell(5, true);
            
            // Células são consideradas iguais se têm mesmo valor, independente do estado fixo
            assertTrue(cell1.equals(cell2), "Células com mesmo valor devem ser iguais");
            return true;
        });
        
        runTest("Hash code consistente", () -> {
            Cell cell1 = new Cell(7);
            Cell cell2 = new Cell(7);
            
            if (cell1.equals(cell2)) {
                assertEquals(cell1.hashCode(), cell2.hashCode(), 
                           "Células iguais devem ter mesmo hash code");
            }
            return true;
        });
        
        runTest("Representação string", () -> {
            Cell emptyCell = new Cell();
            Cell filledCell = new Cell(6);
            
            String emptyString = emptyCell.toString();
            String filledString = filledCell.toString();
            
            assertNotNull(emptyString, "String de célula vazia não deve ser null");
            assertNotNull(filledString, "String de célula preenchida não deve ser null");
            
            assertTrue(filledString.contains("6"), "String deve conter o valor da célula");
            return true;
        });
    }
}