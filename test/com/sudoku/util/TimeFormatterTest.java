package com.sudoku.util;

import com.sudoku.test.TestFramework;
import static com.sudoku.test.TestFramework.*;

/**
 * Testes automatizados para a classe TimeFormatter.
 * Testa todas as funcionalidades de formatação de tempo.
 */
public class TimeFormatterTest {
    
    /**
     * Executa todos os testes da classe TimeFormatter.
     */
    public static void runAllTests() {
        startSuite("Testes da Classe TimeFormatter");
        
        testBasicFormatting();
        testSecondsFormatting();
        testMinutesFormatting();
        testHoursFormatting();
        testMillisecondsFormatting();
        testEdgeCases();
        testPerformance();
    }
    
    /**
     * Testa formatação básica de tempo.
     */
    private static void testBasicFormatting() {
        runTest("Formatação básica de segundos", () -> {
            assertEquals("00:00", TimeFormatter.formatTime(0), "0 segundos deve ser 00:00");
            assertEquals("00:01", TimeFormatter.formatTime(1), "1 segundo deve ser 00:01");
            assertEquals("00:30", TimeFormatter.formatTime(30), "30 segundos deve ser 00:30");
            assertEquals("00:59", TimeFormatter.formatTime(59), "59 segundos deve ser 00:59");
            return true;
        });
        
        runTest("Formatação básica de minutos", () -> {
            assertEquals("01:00", TimeFormatter.formatTime(60), "60 segundos deve ser 01:00");
            assertEquals("01:30", TimeFormatter.formatTime(90), "90 segundos deve ser 01:30");
            assertEquals("05:00", TimeFormatter.formatTime(300), "300 segundos deve ser 05:00");
            assertEquals("10:15", TimeFormatter.formatTime(615), "615 segundos deve ser 10:15");
            return true;
        });
        
        runTest("Formatação com zeros à esquerda", () -> {
            assertEquals("00:01", TimeFormatter.formatTime(1), "1 segundo deve ter zero à esquerda");
            assertEquals("00:09", TimeFormatter.formatTime(9), "9 segundos deve ter zero à esquerda");
            assertEquals("01:09", TimeFormatter.formatTime(69), "1:09 deve ter zeros corretos");
            assertEquals("09:09", TimeFormatter.formatTime(549), "9:09 deve ter zeros corretos");
            return true;
        });
    }
    
    /**
     * Testa formatação específica de segundos.
     */
    private static void testSecondsFormatting() {
        runTest("Segundos de 0 a 59", () -> {
            for (int seconds = 0; seconds < 60; seconds++) {
                String formatted = TimeFormatter.formatTime(seconds);
                assertTrue(formatted.startsWith("00:"), "Deve começar com 00:");
                assertTrue(formatted.length() == 5, "Deve ter formato MM:SS");
                
                String secondsPart = formatted.substring(3);
                int parsedSeconds = Integer.parseInt(secondsPart);
                assertEquals(seconds, parsedSeconds, "Segundos deve ser " + seconds);
            }
            return true;
        });
        
        runTest("Formatação de segundos específicos", () -> {
            assertEquals("00:00", TimeFormatter.formatTime(0), "Zero segundos");
            assertEquals("00:05", TimeFormatter.formatTime(5), "5 segundos");
            assertEquals("00:10", TimeFormatter.formatTime(10), "10 segundos");
            assertEquals("00:25", TimeFormatter.formatTime(25), "25 segundos");
            assertEquals("00:45", TimeFormatter.formatTime(45), "45 segundos");
            return true;
        });
    }
    
    /**
     * Testa formatação específica de minutos.
     */
    private static void testMinutesFormatting() {
        runTest("Minutos exatos", () -> {
            assertEquals("01:00", TimeFormatter.formatTime(60), "1 minuto exato");
            assertEquals("02:00", TimeFormatter.formatTime(120), "2 minutos exatos");
            assertEquals("15:00", TimeFormatter.formatTime(900), "15 minutos exatos");
            assertEquals("30:00", TimeFormatter.formatTime(1800), "30 minutos exatos");
            assertEquals("60:00", TimeFormatter.formatTime(3600), "60 minutos exatos");
            return true;
        });
        
        runTest("Minutos com segundos", () -> {
            assertEquals("01:01", TimeFormatter.formatTime(61), "1 minuto e 1 segundo");
            assertEquals("02:30", TimeFormatter.formatTime(150), "2 minutos e 30 segundos");
            assertEquals("05:45", TimeFormatter.formatTime(345), "5 minutos e 45 segundos");
            assertEquals("12:37", TimeFormatter.formatTime(757), "12 minutos e 37 segundos");
            return true;
        });
        
        runTest("Minutos de dois dígitos", () -> {
            assertEquals("10:00", TimeFormatter.formatTime(600), "10 minutos");
            assertEquals("25:30", TimeFormatter.formatTime(1530), "25 minutos e 30 segundos");
            assertEquals("45:15", TimeFormatter.formatTime(2715), "45 minutos e 15 segundos");
            assertEquals("99:59", TimeFormatter.formatTime(5999), "99 minutos e 59 segundos");
            return true;
        });
    }
    
    /**
     * Testa formatação com horas (mais de 60 minutos).
     */
    private static void testHoursFormatting() {
        runTest("Mais de uma hora", () -> {
            assertEquals("60:00", TimeFormatter.formatTime(3600), "1 hora deve ser 60:00");
            assertEquals("120:00", TimeFormatter.formatTime(7200), "2 horas deve ser 120:00");
            assertEquals("90:30", TimeFormatter.formatTime(5430), "1h 30m 30s deve ser 90:30");
            assertEquals("150:45", TimeFormatter.formatTime(9045), "2h 30m 45s deve ser 150:45");
            return true;
        });
        
        runTest("Tempos muito longos", () -> {
            assertEquals("180:00", TimeFormatter.formatTime(10800), "3 horas");
            assertEquals("300:15", TimeFormatter.formatTime(18015), "5 horas e 15 segundos");
            assertEquals("600:30", TimeFormatter.formatTime(36030), "10 horas e 30 segundos");
            assertEquals("1440:00", TimeFormatter.formatTime(86400), "24 horas");
            return true;
        });
    }
    
    /**
     * Testa formatação com millisegundos.
     */
    private static void testMillisecondsFormatting() {
        runTest("Conversão de millisegundos", () -> {
            assertEquals("00:01", TimeFormatter.formatMilliseconds(1000), "1000ms deve ser 00:01");
            assertEquals("00:30", TimeFormatter.formatMilliseconds(30000), "30000ms deve ser 00:30");
            assertEquals("01:00", TimeFormatter.formatMilliseconds(60000), "60000ms deve ser 01:00");
            assertEquals("05:30", TimeFormatter.formatMilliseconds(330000), "330000ms deve ser 05:30");
            return true;
        });
        
        runTest("Millisegundos fracionários", () -> {
            assertEquals("00:01", TimeFormatter.formatMilliseconds(1500), "1500ms deve ser 00:01 (arredondado)");
            assertEquals("00:02", TimeFormatter.formatMilliseconds(2999), "2999ms deve ser 00:02 (arredondado)");
            assertEquals("00:00", TimeFormatter.formatMilliseconds(500), "500ms deve ser 00:00 (arredondado)");
            assertEquals("00:01", TimeFormatter.formatMilliseconds(999), "999ms deve ser 00:00 (arredondado)");
            return true;
        });
        
        runTest("Millisegundos grandes", () -> {
            assertEquals("16:40", TimeFormatter.formatMilliseconds(1000000), "1000000ms deve ser 16:40");
            assertEquals("02:46", TimeFormatter.formatMilliseconds(166666), "166666ms deve ser 02:46");
            return true;
        });
    }
    
    /**
     * Testa casos extremos e limites.
     */
    private static void testEdgeCases() {
        runTest("Valores limítrofes", () -> {
            assertEquals("00:00", TimeFormatter.formatTime(0), "Tempo zero");
            assertEquals("00:01", TimeFormatter.formatTime(1), "Tempo mínimo positivo");
            assertEquals("999:59", TimeFormatter.formatTime(59999), "Tempo muito grande");
            return true;
        });
        
        runTest("Valores negativos", () -> {
            assertEquals("00:00", TimeFormatter.formatTime(-1), "Valor negativo deve ser tratado como 0");
            assertEquals("00:00", TimeFormatter.formatTime(-100), "Valor muito negativo deve ser 0");
            assertEquals("00:00", TimeFormatter.formatMilliseconds(-1000), "Millisegundos negativos devem ser 0");
            return true;
        });
        
        runTest("Valores muito grandes", () -> {
            String result = TimeFormatter.formatTime(Integer.MAX_VALUE);
            assertNotNull(result, "Deve lidar com Integer.MAX_VALUE");
            assertTrue(result.contains(":"), "Deve manter formato MM:SS");
            assertTrue(result.length() >= 5, "Deve ter pelo menos 5 caracteres");
            return true;
        });
        
        runTest("Formatação com duração específica", () -> {
            long startTime = System.currentTimeMillis();
            
            // Simular 1 segundo de duração
            long duration = 1000;
            String formatted = TimeFormatter.formatDuration(startTime, startTime + duration);
            
            assertEquals("00:01", formatted, "Duração de 1 segundo deve ser 00:01");
            return true;
        });
        
        runTest("Duração entre timestamps", () -> {
            long start = 1000000;
            long end = 1061500; // 61.5 segundos depois
            
            String formatted = TimeFormatter.formatDuration(start, end);
            assertEquals("01:01", formatted, "Duração deve ser calculada corretamente");
            return true;
        });
        
        runTest("Duração inválida", () -> {
            long start = 1000000;
            long end = 999000; // End antes do start
            
            String formatted = TimeFormatter.formatDuration(start, end);
            assertEquals("00:00", formatted, "Duração inválida deve retornar 00:00");
            return true;
        });
    }
    
    /**
     * Testa formatação humanizada.
     */
    private static void testHumanizedFormatting() {
        runTest("Formatação humanizada básica", () -> {
            assertEquals("1 segundo", TimeFormatter.formatHumanized(1), "1 segundo");
            assertEquals("30 segundos", TimeFormatter.formatHumanized(30), "30 segundos");
            assertEquals("1 minuto", TimeFormatter.formatHumanized(60), "1 minuto");
            assertEquals("2 minutos", TimeFormatter.formatHumanized(120), "2 minutos");
            return true;
        });
        
        runTest("Formatação humanizada complexa", () -> {
            assertEquals("1 minuto e 30 segundos", TimeFormatter.formatHumanized(90), "1m30s");
            assertEquals("5 minutos e 15 segundos", TimeFormatter.formatHumanized(315), "5m15s");
            assertEquals("1 hora", TimeFormatter.formatHumanized(3600), "1 hora");
            assertEquals("1 hora e 30 minutos", TimeFormatter.formatHumanized(5400), "1h30m");
            return true;
        });
        
        runTest("Formatação humanizada para zero", () -> {
            assertEquals("0 segundos", TimeFormatter.formatHumanized(0), "Zero tempo");
            assertEquals("0 segundos", TimeFormatter.formatHumanized(-5), "Tempo negativo");
            return true;
        });
    }
    
    /**
     * Testa performance da formatação.
     */
    private static void testPerformance() {
        runTest("Performance de formatação básica", () -> {
            long startTime = System.currentTimeMillis();
            
            // Formatar muitos tempos
            for (int i = 0; i < 10000; i++) {
                TimeFormatter.formatTime(i);
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 1000, "Formatação de 10000 tempos deve levar menos de 1 segundo");
            return true;
        });
        
        runTest("Performance de formatação de millisegundos", () -> {
            long startTime = System.currentTimeMillis();
            
            // Formatar muitos tempos em millisegundos
            for (int i = 0; i < 5000; i++) {
                TimeFormatter.formatMilliseconds(i * 1000);
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 500, "Formatação de millisegundos deve ser rápida");
            return true;
        });
        
        runTest("Uso de memória", () -> {
            // Verificar que não há vazamentos de memória óbvios
            Runtime runtime = Runtime.getRuntime();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            
            // Realizar muitas formatações
            for (int i = 0; i < 1000; i++) {
                TimeFormatter.formatTime(i);
                TimeFormatter.formatMilliseconds(i * 1000);
                TimeFormatter.formatHumanized(i);
            }
            
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = afterMemory - beforeMemory;
            
            // Permitir até 1MB de uso adicional
            assertTrue(memoryUsed < 1024 * 1024, "Uso de memória deve ser razoável");
            return true;
        });
    }
    
    /**
     * Testa parsing de tempo.
     */
    private static void testTimeParsing() {
        runTest("Parsing de formato MM:SS", () -> {
            assertEquals(0, TimeFormatter.parseTime("00:00"), "00:00 deve ser 0 segundos");
            assertEquals(30, TimeFormatter.parseTime("00:30"), "00:30 deve ser 30 segundos");
            assertEquals(60, TimeFormatter.parseTime("01:00"), "01:00 deve ser 60 segundos");
            assertEquals(90, TimeFormatter.parseTime("01:30"), "01:30 deve ser 90 segundos");
            assertEquals(3661, TimeFormatter.parseTime("61:01"), "61:01 deve ser 3661 segundos");
            return true;
        });
        
        runTest("Parsing de formatos inválidos", () -> {
            assertEquals(-1, TimeFormatter.parseTime("abc"), "abc deve retornar -1");
            assertEquals(-1, TimeFormatter.parseTime("1:2:3"), "1:2:3 deve retornar -1");
            assertEquals(-1, TimeFormatter.parseTime(""), "String vazia deve retornar -1");
            assertEquals(-1, TimeFormatter.parseTime("1"), "Formato incompleto deve retornar -1");
            return true;
        });
    }
}