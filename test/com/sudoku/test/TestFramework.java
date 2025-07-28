package com.sudoku.test;

/**
 * Framework simples de testes para o sistema Sudoku.
 * Fornece funcionalidades básicas para execução e validação de testes.
 */
public class TestFramework {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static String currentSuite = "";
    
    /**
     * Inicia uma nova suíte de testes.
     * 
     * @param suiteName nome da suíte
     */
    public static void startSuite(String suiteName) {
        currentSuite = suiteName;
        System.out.println("\n=== " + suiteName + " ===");
    }
    
    /**
     * Executa um teste e verifica o resultado.
     * 
     * @param testName nome do teste
     * @param test função de teste
     */
    public static void runTest(String testName, TestFunction test) {
        totalTests++;
        try {
            boolean result = test.run();
            if (result) {
                System.out.println("  ✓ " + testName);
                passedTests++;
            } else {
                System.out.println("  ✗ " + testName + " - FALHOU");
                failedTests++;
            }
        } catch (Exception e) {
            System.out.println("  ✗ " + testName + " - ERRO: " + e.getMessage());
            failedTests++;
        }
    }
    
    /**
     * Verifica se uma condição é verdadeira.
     * 
     * @param condition condição a verificar
     * @param message mensagem de erro
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    /**
     * Verifica se uma condição é falsa.
     * 
     * @param condition condição a verificar
     * @param message mensagem de erro
     */
    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
    
    /**
     * Verifica se dois valores são iguais.
     * 
     * @param expected valor esperado
     * @param actual valor atual
     * @param message mensagem de erro
     */
    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(message + " - Esperado: " + expected + ", Atual: " + actual);
        }
    }
    
    /**
     * Verifica se dois valores inteiros são iguais.
     * 
     * @param expected valor esperado
     * @param actual valor atual
     * @param message mensagem de erro
     */
    public static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " - Esperado: " + expected + ", Atual: " + actual);
        }
    }
    
    /**
     * Verifica se duas strings são iguais.
     * 
     * @param expected string esperada
     * @param actual string atual
     * @param message mensagem de erro
     */
    public static void assertEquals(String expected, String actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(message + " - Esperado: " + expected + ", Atual: " + actual);
        }
    }
    
    /**
     * Verifica se dois valores são diferentes.
     * 
     * @param unexpected valor não esperado
     * @param actual valor atual
     * @param message mensagem de erro
     */
    public static void assertNotEquals(Object unexpected, Object actual, String message) {
        if (unexpected == null && actual == null) {
            throw new AssertionError(message + " - Valores não deveriam ser iguais");
        }
        if (unexpected != null && unexpected.equals(actual)) {
            throw new AssertionError(message + " - Valores não deveriam ser iguais: " + actual);
        }
    }
    
    /**
     * Verifica se um valor é nulo.
     * 
     * @param actual valor a verificar
     * @param message mensagem de erro
     */
    public static void assertNull(Object actual, String message) {
        if (actual != null) {
            throw new AssertionError(message + " - Esperado: null, Atual: " + actual);
        }
    }
    
    /**
     * Verifica se um valor não é nulo.
     * 
     * @param actual valor a verificar
     * @param message mensagem de erro
     */
    public static void assertNotNull(Object actual, String message) {
        if (actual == null) {
            throw new AssertionError(message + " - Valor não deveria ser null");
        }
    }
    
    /**
     * Verifica se uma exceção é lançada.
     * 
     * @param expectedClass classe da exceção esperada
     * @param code código que deve lançar exceção
     * @param message mensagem de erro
     */
    public static void assertThrows(Class<? extends Exception> expectedClass, ThrowingFunction code, String message) {
        try {
            code.run();
            throw new AssertionError(message + " - Esperava exceção: " + expectedClass.getSimpleName());
        } catch (Exception e) {
            if (!expectedClass.isInstance(e)) {
                throw new AssertionError(message + " - Esperada: " + expectedClass.getSimpleName() + 
                                       ", Atual: " + e.getClass().getSimpleName());
            }
        }
    }
    
    /**
     * Verifica se um valor está dentro de um intervalo.
     * 
     * @param value valor a verificar
     * @param min valor mínimo
     * @param max valor máximo
     * @param message mensagem de erro
     */
    public static void assertInRange(int value, int min, int max, String message) {
        if (value < min || value > max) {
            throw new AssertionError(message + " - Valor " + value + " fora do intervalo [" + min + ", " + max + "]");
        }
    }
    
    /**
     * Exibe estatísticas finais dos testes.
     */
    public static void printSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RESUMO DOS TESTES");
        System.out.println("=".repeat(50));
        System.out.println("Total de testes: " + totalTests);
        System.out.println("Testes aprovados: " + passedTests);
        System.out.println("Testes falharam: " + failedTests);
        System.out.println("Taxa de sucesso: " + (totalTests > 0 ? (passedTests * 100 / totalTests) : 0) + "%");
        
        if (failedTests == 0) {
            System.out.println("\n🎉 TODOS OS TESTES PASSARAM!");
        } else {
            System.out.println("\n⚠️  ALGUNS TESTES FALHARAM!");
        }
    }
    
    /**
     * Reseta os contadores de teste.
     */
    public static void reset() {
        totalTests = 0;
        passedTests = 0;
        failedTests = 0;
        currentSuite = "";
    }
    
    /**
     * Obtém o número total de testes.
     */
    public static int getTotalTests() {
        return totalTests;
    }
    
    /**
     * Obtém o número de testes aprovados.
     */
    public static int getPassedTests() {
        return passedTests;
    }
    
    /**
     * Obtém o número de testes falhados.
     */
    public static int getFailedTests() {
        return failedTests;
    }
    
    /**
     * Interface funcional para testes.
     */
    @FunctionalInterface
    public interface TestFunction {
        boolean run() throws Exception;
    }
    
    /**
     * Interface funcional para código que pode lançar exceções.
     */
    @FunctionalInterface
    public interface ThrowingFunction {
        void run() throws Exception;
    }
}