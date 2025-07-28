package com.sudoku.util;

import java.util.Scanner;

/**
 * Utilitário para validação e leitura de entrada do usuário.
 * Fornece métodos seguros para ler diferentes tipos de dados do console.
 */
public class InputValidator {
    
    private final Scanner scanner;
    private static final String INVALID_INPUT_MESSAGE = "Entrada inválida. Tente novamente.";
    
    /**
     * Construtor que aceita um Scanner.
     * 
     * @param scanner scanner para leitura de entrada
     */
    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Lê um número inteiro dentro de um intervalo específico.
     * 
     * @param min valor mínimo (inclusivo)
     * @param max valor máximo (inclusivo)
     * @param prompt mensagem para o usuário
     * @return número inteiro válido
     */
    public int readInt(int min, int max, String prompt) {
        while (true) {
            System.out.print(prompt);
            
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Número deve estar entre " + min + " e " + max + ".");
                }
            } else {
                System.out.println(INVALID_INPUT_MESSAGE);
                scanner.nextLine(); // Limpar entrada inválida
            }
        }
    }
    
    /**
     * Lê um número inteiro sem restrições de intervalo.
     * 
     * @param prompt mensagem para o usuário
     * @return número inteiro
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                return value;
            } else {
                System.out.println(INVALID_INPUT_MESSAGE);
                scanner.nextLine(); // Limpar entrada inválida
            }
        }
    }
    
    /**
     * Lê uma string não vazia.
     * 
     * @param prompt mensagem para o usuário
     * @return string não vazia
     */
    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Entrada não pode estar vazia.");
            }
        }
    }
    
    /**
     * Lê uma string qualquer (pode ser vazia).
     * 
     * @param prompt mensagem para o usuário
     * @return string
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Lê uma resposta sim/não.
     * 
     * @param prompt mensagem para o usuário
     * @return true para sim, false para não
     */
    public boolean readYesNo(String prompt) {
        while (true) {
            String input = readNonEmptyString(prompt + " (s/n): ").toLowerCase();
            
            if (input.equals("s") || input.equals("sim") || input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("não") || input.equals("nao") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Responda com 's' (sim) ou 'n' (não).");
            }
        }
    }
    
    /**
     * Lê coordenadas de uma jogada no formato "linha coluna valor".
     * 
     * @param prompt mensagem para o usuário
     * @return array [linha, coluna, valor] ou null se cancelado
     */
    public int[] readMove(String prompt) {
        while (true) {
            String input = readString(prompt);
            
            // Permitir cancelamento
            if (input.isEmpty() || input.equalsIgnoreCase("cancelar") || input.equalsIgnoreCase("voltar")) {
                return null;
            }
            
            String[] parts = input.split("\\s+");
            
            if (parts.length != 3) {
                System.out.println("Formato inválido. Use: linha coluna valor (ex: 3 7 5)");
                System.out.println("Ou digite 'cancelar' para voltar ao menu.");
                continue;
            }
            
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                int value = Integer.parseInt(parts[2]);
                
                if (row < 1 || row > 9) {
                    System.out.println("Linha deve estar entre 1 e 9.");
                    continue;
                }
                
                if (col < 1 || col > 9) {
                    System.out.println("Coluna deve estar entre 1 e 9.");
                    continue;
                }
                
                if (value < 0 || value > 9) {
                    System.out.println("Valor deve estar entre 0 e 9 (0 para limpar).");
                    continue;
                }
                
                return new int[]{row, col, value};
                
            } catch (NumberFormatException e) {
                System.out.println("Use apenas números. Formato: linha coluna valor (ex: 3 7 5)");
            }
        }
    }
    
    /**
     * Lê coordenadas de uma posição no formato "linha coluna".
     * 
     * @param prompt mensagem para o usuário
     * @return array [linha, coluna] ou null se cancelado
     */
    public int[] readPosition(String prompt) {
        while (true) {
            String input = readString(prompt);
            
            if (input.isEmpty() || input.equalsIgnoreCase("cancelar") || input.equalsIgnoreCase("voltar")) {
                return null;
            }
            
            String[] parts = input.split("\\s+");
            
            if (parts.length != 2) {
                System.out.println("Formato inválido. Use: linha coluna (ex: 3 7)");
                System.out.println("Ou digite 'cancelar' para voltar ao menu.");
                continue;
            }
            
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                
                if (row < 1 || row > 9 || col < 1 || col > 9) {
                    System.out.println("Linha e coluna devem estar entre 1 e 9.");
                    continue;
                }
                
                return new int[]{row, col};
                
            } catch (NumberFormatException e) {
                System.out.println("Use apenas números. Formato: linha coluna (ex: 3 7)");
            }
        }
    }
    
    /**
     * Aguarda que o usuário pressione Enter.
     */
    public void waitForEnter() {
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Valida se uma string representa um número inteiro.
     * 
     * @param str string a validar
     * @return true se for um número inteiro válido
     */
    public static boolean isValidInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Valida se uma coordenada está dentro dos limites do tabuleiro.
     * 
     * @param coordinate coordenada (1-9)
     * @return true se estiver dentro dos limites
     */
    public static boolean isValidCoordinate(int coordinate) {
        return coordinate >= 1 && coordinate <= 9;
    }
    
    /**
     * Valida se um valor de Sudoku é válido.
     * 
     * @param value valor (0-9)
     * @return true se for válido
     */
    public static boolean isValidSudokuValue(int value) {
        return value >= 0 && value <= 9;
    }
    
    /**
     * Limpa o buffer de entrada para evitar problemas com entrada pendente.
     */
    public void clearInputBuffer() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                break;
            }
        }
    }
}