package com.sudoku.model;

/**
 * Representa uma célula individual do tabuleiro de Sudoku.
 * Cada célula pode conter um valor de 0 a 9, onde 0 representa uma célula vazia.
 */
public class Cell {
    private int value;
    private boolean isFixed;
    private boolean[] candidatos;
    
    /**
     * Construtor padrão que cria uma célula vazia.
     */
    public Cell() {
        this.value = 0;
        this.isFixed = false;
        this.candidatos = new boolean[10]; // índices 1-9 para números 1-9
    }
    
    /**
     * Construtor que cria uma célula com um valor específico.
     * 
     * @param value o valor da célula (0-9)
     * @param isFixed indica se a célula é fixa (não pode ser alterada)
     */
    public Cell(int value, boolean isFixed) {
        this.candidatos = new boolean[10]; // índices 1-9 para números 1-9
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("Valor deve estar entre 0 e 9");
        }
        this.value = value;
        this.isFixed = isFixed;
    }
    
    /**
     * Obtém o valor atual da célula.
     * 
     * @return o valor da célula (0-9)
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Define o valor da célula.
     * 
     * @param value o novo valor (0-9)
     * @throws IllegalArgumentException se o valor estiver fora do intervalo válido
     * @throws UnsupportedOperationException se a célula for fixa
     */
    public void setValue(int value) {
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("Valor deve estar entre 0 e 9");
        }
        if (isFixed && this.value != 0) {
            throw new UnsupportedOperationException("Não é possível alterar uma célula fixa");
        }
        this.value = value;
    }
    
    /**
     * Verifica se a célula está vazia.
     * 
     * @return true se a célula estiver vazia (valor = 0)
     */
    public boolean isEmpty() {
        return value == 0;
    }
    
    /**
     * Verifica se a célula é fixa.
     * 
     * @return true se a célula for fixa
     */
    public boolean isFixed() {
        return isFixed;
    }
    
    /**
     * Define se a célula é fixa.
     * 
     * @param fixed true para tornar a célula fixa
     */
    public void setFixed(boolean fixed) {
        this.isFixed = fixed;
    }
    
    /**
     * Adiciona um candidato à célula.
     * 
     * @param numero o número candidato (1-9)
     */
    public void adicionarCandidato(int numero) {
        if (numero >= 1 && numero <= 9) {
            candidatos[numero] = true;
        }
    }
    
    /**
     * Remove um candidato da célula.
     * 
     * @param numero o número candidato (1-9)
     */
    public void removerCandidato(int numero) {
        if (numero >= 1 && numero <= 9) {
            candidatos[numero] = false;
        }
    }
    
    /**
     * Verifica se um número é candidato válido.
     * 
     * @param numero o número a verificar (1-9)
     * @return true se o número for um candidato válido
     */
    public boolean isCandidato(int numero) {
        if (numero >= 1 && numero <= 9) {
            return candidatos[numero];
        }
        return false;
    }
    
    /**
     * Limpa todos os candidatos da célula.
     */
    public void limparCandidatos() {
        for (int i = 1; i <= 9; i++) {
            candidatos[i] = false;
        }
    }
    
    /**
     * Cria uma cópia da célula.
     * 
     * @return uma nova instância de Cell com os mesmos valores
     */
    public Cell copy() {
        Cell nova = new Cell(this.value, this.isFixed);
        System.arraycopy(this.candidatos, 0, nova.candidatos, 0, this.candidatos.length);
        return nova;
    }
    
    @Override
    public String toString() {
        return isEmpty() ? " " : String.valueOf(value);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cell cell = (Cell) obj;
        return value == cell.value && isFixed == cell.isFixed;
    }
    
    @Override
    public int hashCode() {
        return value * 31 + (isFixed ? 1 : 0);
    }
}