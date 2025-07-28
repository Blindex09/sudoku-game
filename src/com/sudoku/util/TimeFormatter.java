package com.sudoku.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitários para formatação de tempo e datas.
 */
public class TimeFormatter {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Formata um tempo em segundos para formato MM:SS.
     * 
     * @param totalSeconds tempo total em segundos
     * @return tempo formatado
     */
    public static String formatTime(long totalSeconds) {
        if (totalSeconds < 0) {
            return "00:00";
        }
        
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    
    /**
     * Formata um tempo em milissegundos para formato mais legível.
     * 
     * @param milliseconds tempo em milissegundos
     * @return tempo formatado
     */
    public static String formatTimeFromMillis(long milliseconds) {
        return formatTime(milliseconds / 1000);
    }
    
    /**
     * Calcula e formata a diferença entre duas datas.
     * 
     * @param start data inicial
     * @param end data final
     * @return diferença formatada
     */
    public static String formatDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "00:00";
        }
        
        Duration duration = Duration.between(start, end);
        return formatTime(duration.getSeconds());
    }
    
    /**
     * Formata uma data e hora para exibição.
     * 
     * @param dateTime data e hora
     * @return string formatada
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }
    
    /**
     * Formata apenas a hora.
     * 
     * @param dateTime data e hora
     * @return hora formatada
     */
    public static String formatTimeOnly(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(TIME_FORMATTER);
    }
    
    /**
     * Formata apenas a data.
     * 
     * @param dateTime data e hora
     * @return data formatada
     */
    public static String formatDateOnly(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DATE_FORMATTER);
    }
    
    /**
     * Converte segundos para uma descrição mais amigável.
     * 
     * @param totalSeconds tempo em segundos
     * @return descrição do tempo
     */
    public static String formatTimeDescription(long totalSeconds) {
        if (totalSeconds < 60) {
            return totalSeconds + " segundo" + (totalSeconds != 1 ? "s" : "");
        } else if (totalSeconds < 3600) {
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;
            String result = minutes + " minuto" + (minutes != 1 ? "s" : "");
            if (seconds > 0) {
                result += " e " + seconds + " segundo" + (seconds != 1 ? "s" : "");
            }
            return result;
        } else {
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            String result = hours + " hora" + (hours != 1 ? "s" : "");
            if (minutes > 0) {
                result += " e " + minutes + " minuto" + (minutes != 1 ? "s" : "");
            }
            return result;
        }
    }
    
    /**
     * Calcula o tempo médio por jogada.
     * 
     * @param totalTime tempo total em segundos
     * @param moves número de jogadas
     * @return tempo médio por jogada
     */
    public static String formatAverageTimePerMove(long totalTime, int moves) {
        if (moves == 0) {
            return "N/A";
        }
        
        double averageSeconds = (double) totalTime / moves;
        return String.format("%.1f segundos", averageSeconds);
    }
    
    /**
     * Verifica se um jogo foi rápido (menos de 10 minutos).
     * 
     * @param totalSeconds tempo total
     * @return true se foi rápido
     */
    public static boolean isFastGame(long totalSeconds) {
        return totalSeconds < 600; // 10 minutos
    }
    
    /**
     * Verifica se um jogo foi longo (mais de 30 minutos).
     * 
     * @param totalSeconds tempo total
     * @return true se foi longo
     */
    public static boolean isLongGame(long totalSeconds) {
        return totalSeconds > 1800; // 30 minutos
    }
    
    /**
     * Obtém uma classificação baseada no tempo de jogo.
     * 
     * @param totalSeconds tempo total
     * @return classificação do tempo
     */
    public static String getTimeRating(long totalSeconds) {
        if (totalSeconds < 300) { // 5 minutos
            return "Relâmpago ⚡";
        } else if (totalSeconds < 600) { // 10 minutos
            return "Rápido 🏃";
        } else if (totalSeconds < 1200) { // 20 minutos
            return "Moderado 🚶";
        } else if (totalSeconds < 1800) { // 30 minutos
            return "Calmo 🐌";
        } else {
            return "Muito Calmo 🐢";
        }
    }
}