package com.sudoku.util;

import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe InputValidator.
 * Testa todas as funcionalidades de validação de entrada.
 */
public class InputValidatorTest {
    
    /**
     * Executa todos os testes da classe InputValidator.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe InputValidator");
        
        testIntegerValidation();
        testRangeValidation();
        testStringValidation();
        testBooleanValidation();
        testPositionValidation();
        testValueValidation();
        testEdgeCases();
    }
    
    /**
     * Testa validação de inteiros.
     */
    private static void testIntegerValidation() {
        runTest("Validar inteiros válidos", () -> {
            assertTrue(InputValidator.isValidInteger("123"), "123 deve ser inteiro válido");
            assertTrue(InputValidator.isValidInteger("0"), "0 deve ser inteiro válido");
            assertTrue(InputValidator.isValidInteger("-5"), "-5 deve ser inteiro válido");
            assertTrue(InputValidator.isValidInteger("1"), "1 deve ser inteiro válido");
            return true;
        });
        
        runTest("Rejeitar inteiros inválidos", () -> {
            assertFalse(InputValidator.isValidInteger("abc"), "abc não deve ser inteiro válido");
            assertFalse(InputValidator.isValidInteger("12.5"), "12.5 não deve ser inteiro válido");
            assertFalse(InputValidator.isValidInteger(""), "String vazia não deve ser inteiro válido");
            assertFalse(InputValidator.isValidInteger(" "), "Espaço não deve ser inteiro válido");
            assertFalse(InputValidator.isValidInteger("12a"), "12a não deve ser inteiro válido");
            return true;
        });
        
        runTest("Parsing de inteiros", () -> {
            assertEquals(123, InputValidator.parseInt("123"), "Parse de 123");
            assertEquals(0, InputValidator.parseInt("0"), "Parse de 0");
            assertEquals(-5, InputValidator.parseInt("-5"), "Parse de -5");
            
            // Teste com valor padrão para entradas inválidas
            assertEquals(999, InputValidator.parseInt("abc", 999), "Parse inválido deve retornar padrão");
            assertEquals(42, InputValidator.parseInt("", 42), "String vazia deve retornar padrão");
            return true;
        });
    }
    
    /**
     * Testa validação de intervalos.
     */
    private static void testRangeValidation() {
        runTest("Validar dentro do intervalo", () -> {
            assertTrue(InputValidator.isInRange(5, 1, 10), "5 deve estar no intervalo [1,10]");
            assertTrue(InputValidator.isInRange(1, 1, 10), "1 deve estar no intervalo [1,10]");
            assertTrue(InputValidator.isInRange(10, 1, 10), "10 deve estar no intervalo [1,10]");
            assertTrue(InputValidator.isInRange(0, -5, 5), "0 deve estar no intervalo [-5,5]");
            return true;
        });
        
        runTest("Rejeitar fora do intervalo", () -> {
            assertFalse(InputValidator.isInRange(0, 1, 10), "0 não deve estar no intervalo [1,10]");
            assertFalse(InputValidator.isInRange(11, 1, 10), "11 não deve estar no intervalo [1,10]");
            assertFalse(InputValidator.isInRange(-1, 1, 10), "-1 não deve estar no intervalo [1,10]");
            return true;
        });
        
        runTest("Validação de coordenadas Sudoku", () -> {
            assertTrue(InputValidator.isValidSudokuCoordinate(0), "0 deve ser coordenada válida");
            assertTrue(InputValidator.isValidSudokuCoordinate(8), "8 deve ser coordenada válida");
            assertTrue(InputValidator.isValidSudokuCoordinate(4), "4 deve ser coordenada válida");
            
            assertFalse(InputValidator.isValidSudokuCoordinate(-1), "-1 não deve ser coordenada válida");
            assertFalse(InputValidator.isValidSudokuCoordinate(9), "9 não deve ser coordenada válida");
            assertFalse(InputValidator.isValidSudokuCoordinate(15), "15 não deve ser coordenada válida");
            return true;
        });
        
        runTest("Validação de valores Sudoku", () -> {
            assertTrue(InputValidator.isValidSudokuValue(1), "1 deve ser valor válido");
            assertTrue(InputValidator.isValidSudokuValue(9), "9 deve ser valor válido");
            assertTrue(InputValidator.isValidSudokuValue(5), "5 deve ser valor válido");
            
            assertFalse(InputValidator.isValidSudokuValue(0), "0 não deve ser valor válido para jogada");
            assertFalse(InputValidator.isValidSudokuValue(-1), "-1 não deve ser valor válido");
            assertFalse(InputValidator.isValidSudokuValue(10), "10 não deve ser valor válido");
            return true;
        });
    }
    
    /**
     * Testa validação de strings.
     */
    private static void testStringValidation() {
        runTest("Validar strings não vazias", () -> {
            assertTrue(InputValidator.isNonEmptyString("abc"), "abc deve ser string não vazia");
            assertTrue(InputValidator.isNonEmptyString("123"), "123 deve ser string não vazia");
            assertTrue(InputValidator.isNonEmptyString("a"), "a deve ser string não vazia");
            assertTrue(InputValidator.isNonEmptyString(" a "), " a  deve ser string não vazia");
            return true;
        });
        
        runTest("Rejeitar strings vazias", () -> {
            assertFalse(InputValidator.isNonEmptyString(""), "String vazia deve ser rejeitada");
            assertFalse(InputValidator.isNonEmptyString("   "), "String com espaços deve ser rejeitada");
            assertFalse(InputValidator.isNonEmptyString("\t\n"), "String com whitespace deve ser rejeitada");
            return true;
        });
        
        runTest("Validar comprimento de string", () -> {
            assertTrue(InputValidator.hasValidLength("abc", 1, 5), "abc deve ter comprimento válido [1,5]");
            assertTrue(InputValidator.hasValidLength("a", 1, 5), "a deve ter comprimento válido [1,5]");
            assertTrue(InputValidator.hasValidLength("abcde", 1, 5), "abcde deve ter comprimento válido [1,5]");
            
            assertFalse(InputValidator.hasValidLength("", 1, 5), "String vazia deve falhar [1,5]");
            assertFalse(InputValidator.hasValidLength("abcdef", 1, 5), "abcdef deve falhar [1,5]");
            return true;
        });
        
        runTest("Sanitização de entrada", () -> {
            assertEquals("abc", InputValidator.sanitizeInput("  abc  "), "Deve remover espaços");
            assertEquals("ABC", InputValidator.sanitizeInput("abc", true), "Deve converter para maiúscula");
            assertEquals("abc", InputValidator.sanitizeInput("ABC", false), "Deve converter para minúscula");
            assertEquals("", InputValidator.sanitizeInput("   "), "Deve retornar vazio para whitespace");
            return true;
        });
    }
    
    /**
     * Testa validação de booleanos.
     */
    private static void testBooleanValidation() {
        runTest("Validar valores booleanos verdadeiros", () -> {
            assertTrue(InputValidator.parseBoolean("true"), "true deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("TRUE"), "TRUE deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("yes"), "yes deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("YES"), "YES deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("y"), "y deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("Y"), "Y deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("1"), "1 deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("s"), "s deve ser verdadeiro");
            assertTrue(InputValidator.parseBoolean("S"), "S deve ser verdadeiro");
            return true;
        });
        
        runTest("Validar valores booleanos falsos", () -> {
            assertFalse(InputValidator.parseBoolean("false"), "false deve ser falso");
            assertFalse(InputValidator.parseBoolean("FALSE"), "FALSE deve ser falso");
            assertFalse(InputValidator.parseBoolean("no"), "no deve ser falso");
            assertFalse(InputValidator.parseBoolean("NO"), "NO deve ser falso");
            assertFalse(InputValidator.parseBoolean("n"), "n deve ser falso");
            assertFalse(InputValidator.parseBoolean("N"), "N deve ser falso");
            assertFalse(InputValidator.parseBoolean("0"), "0 deve ser falso");
            return true;
        });
        
        runTest("Valores booleanos inválidos", () -> {
            assertFalse(InputValidator.parseBoolean("maybe"), "maybe deve retornar falso como padrão");
            assertFalse(InputValidator.parseBoolean(""), "String vazia deve retornar falso");
            assertFalse(InputValidator.parseBoolean("2"), "2 deve retornar falso");
            assertFalse(InputValidator.parseBoolean("abc"), "abc deve retornar falso");
            return true;
        });
    }
    
    /**
     * Testa validação específica de posições.
     */
    private static void testPositionValidation() {
        runTest("Parsing de coordenadas válidas", () -> {
            int[] coords1 = InputValidator.parseCoordinates("1 1");
            assertNotNull(coords1, "Deve parsear coordenadas válidas");
            assertEquals(2, coords1.length, "Deve ter 2 elementos");
            assertEquals(0, coords1[0], "Primeira coordenada deve ser 0 (base 0)");
            assertEquals(0, coords1[1], "Segunda coordenada deve ser 0 (base 0)");
            
            int[] coords2 = InputValidator.parseCoordinates("5 7");
            assertEquals(4, coords2[0], "Primeira coordenada deve ser 4");
            assertEquals(6, coords2[1], "Segunda coordenada deve ser 6");
            return true;
        });
        
        runTest("Rejeitar coordenadas inválidas", () -> {
            assertNull(InputValidator.parseCoordinates("0 1"), "Coordenada 0 deve ser rejeitada");
            assertNull(InputValidator.parseCoordinates("10 5"), "Coordenada 10 deve ser rejeitada");
            assertNull(InputValidator.parseCoordinates("1"), "Formato incompleto deve ser rejeitado");
            assertNull(InputValidator.parseCoordinates("a b"), "Letras devem ser rejeitadas");
            assertNull(InputValidator.parseCoordinates("1 2 3"), "Muitos valores devem ser rejeitados");
            return true;
        });
        
        runTest("Parsing de jogada completa", () -> {
            int[] move = InputValidator.parseMove("1 1 5");
            assertNotNull(move, "Deve parsear jogada válida");
            assertEquals(3, move.length, "Deve ter 3 elementos");
            assertEquals(0, move[0], "Linha deve ser 0 (base 0)");
            assertEquals(0, move[1], "Coluna deve ser 0 (base 0)");
            assertEquals(5, move[2], "Valor deve ser 5");
            
            int[] clearMove = InputValidator.parseMove("3 7 0");
            assertEquals(0, clearMove[2], "Deve aceitar 0 para limpeza");
            return true;
        });
        
        runTest("Rejeitar jogadas inválidas", () -> {
            assertNull(InputValidator.parseMove("0 1 5"), "Linha 0 deve ser rejeitada");
            assertNull(InputValidator.parseMove("1 0 5"), "Coluna 0 deve ser rejeitada");
            assertNull(InputValidator.parseMove("1 1 10"), "Valor 10 deve ser rejeitado");
            assertNull(InputValidator.parseMove("1 1"), "Jogada incompleta deve ser rejeitada");
            assertNull(InputValidator.parseMove("a b c"), "Letras devem ser rejeitadas");
            return true;
        });
    }
    
    /**
     * Testa validação de valores específicos.
     */
    private static void testValueValidation() {
        runTest("Validar valores de célula", () -> {
            assertTrue(InputValidator.isValidCellValue(0), "0 deve ser válido (célula vazia)");
            assertTrue(InputValidator.isValidCellValue(1), "1 deve ser válido");
            assertTrue(InputValidator.isValidCellValue(9), "9 deve ser válido");
            assertTrue(InputValidator.isValidCellValue(5), "5 deve ser válido");
            
            assertFalse(InputValidator.isValidCellValue(-1), "-1 não deve ser válido");
            assertFalse(InputValidator.isValidCellValue(10), "10 não deve ser válido");
            assertFalse(InputValidator.isValidCellValue(15), "15 não deve ser válido");
            return true;
        });
        
        runTest("Validar opções de menu", () -> {
            assertTrue(InputValidator.isValidMenuOption("1", 1, 5), "1 deve ser opção válida [1,5]");
            assertTrue(InputValidator.isValidMenuOption("5", 1, 5), "5 deve ser opção válida [1,5]");
            assertTrue(InputValidator.isValidMenuOption("3", 1, 5), "3 deve ser opção válida [1,5]");
            
            assertFalse(InputValidator.isValidMenuOption("0", 1, 5), "0 não deve ser opção válida [1,5]");
            assertFalse(InputValidator.isValidMenuOption("6", 1, 5), "6 não deve ser opção válida [1,5]");
            assertFalse(InputValidator.isValidMenuOption("a", 1, 5), "a não deve ser opção válida");
            return true;
        });
        
        runTest("Validar dificuldades", () -> {
            assertTrue(InputValidator.isValidDifficulty("1"), "1 deve ser dificuldade válida");
            assertTrue(InputValidator.isValidDifficulty("2"), "2 deve ser dificuldade válida");
            assertTrue(InputValidator.isValidDifficulty("3"), "3 deve ser dificuldade válida");
            assertTrue(InputValidator.isValidDifficulty("4"), "4 deve ser dificuldade válida");
            
            assertFalse(InputValidator.isValidDifficulty("0"), "0 não deve ser dificuldade válida");
            assertFalse(InputValidator.isValidDifficulty("5"), "5 não deve ser dificuldade válida");
            assertFalse(InputValidator.isValidDifficulty("a"), "a não deve ser dificuldade válida");
            return true;
        });
    }
    
    /**
     * Testa casos extremos e limites.
     */
    private static void testEdgeCases() {
        runTest("Valores null", () -> {
            assertFalse(InputValidator.isValidInteger(null), "null não deve ser inteiro válido");
            assertFalse(InputValidator.isNonEmptyString(null), "null não deve ser string válida");
            assertFalse(InputValidator.parseBoolean(null), "null deve retornar false");
            assertNull(InputValidator.parseCoordinates(null), "null deve retornar null");
            return true;
        });
        
        runTest("Strings com caracteres especiais", () -> {
            assertFalse(InputValidator.isValidInteger("12@3"), "12@3 não deve ser inteiro");
            assertFalse(InputValidator.isValidInteger("1.2.3"), "1.2.3 não deve ser inteiro");
            assertTrue(InputValidator.isNonEmptyString("@#$"), "@#$ deve ser string não vazia");
            assertEquals("@#$", InputValidator.sanitizeInput("  @#$  "), "Deve manter caracteres especiais");
            return true;
        });
        
        runTest("Números muito grandes", () -> {
            assertTrue(InputValidator.isValidInteger("2147483647"), "Integer.MAX_VALUE deve ser válido");
            assertTrue(InputValidator.isValidInteger("-2147483648"), "Integer.MIN_VALUE deve ser válido");
            
            // Overflow deve ser tratado
            try {
                InputValidator.parseInt("9999999999999999999");
                // Se chegou aqui, deve retornar algum valor padrão
            } catch (NumberFormatException e) {
                // Exceção é aceitável para overflow
            }
            return true;
        });
        
        runTest("Whitespace complexo", () -> {
            assertEquals("abc", InputValidator.sanitizeInput("\t abc \n"), "Deve lidar com tabs e newlines");
            assertFalse(InputValidator.isNonEmptyString("\u00A0"), "Non-breaking space deve ser rejeitado");
            assertEquals("", InputValidator.sanitizeInput("\r\n\t "), "Diversos whitespaces devem resultar em vazio");
            return true;
        });
        
        runTest("Parsing robusto", () -> {
            // Deve lidar graciosamente com entradas malformadas
            assertNull(InputValidator.parseCoordinates("1,2"), "Separador incorreto deve ser rejeitado");
            assertNull(InputValidator.parseCoordinates("1  2"), "Múltiplos espaços podem causar problemas");
            
            int[] result = InputValidator.parseCoordinates("1 2");
            assertNotNull(result, "Formato normal deve funcionar");
            
            assertEquals(0, InputValidator.parseInt("abc", 0), "Parse inválido deve usar padrão");
            return true;
        });
        
        runTest("Performance com strings grandes", () -> {
            StringBuilder bigString = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                bigString.append("a");
            }
            
            String huge = bigString.toString();
            
            long startTime = System.currentTimeMillis();
            boolean result = InputValidator.isNonEmptyString(huge);
            long endTime = System.currentTimeMillis();
            
            assertTrue(result, "String grande deve ser válida");
            assertTrue(endTime - startTime < 100, "Validação deve ser rápida");
            return true;
        });
    }
}