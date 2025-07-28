#!/bin/bash
# Script para executar o Jogo de Sudoku
# Certifique-se de que o projeto foi compilado primeiro

echo "==================================="
echo "    JOGO DE SUDOKU - EXECUTOR"
echo "==================================="
echo

# Verificar se o diretório build existe
if [ ! -d "build" ]; then
    echo "ERRO: Diretório build não encontrado!"
    echo "Execute ./compile.sh primeiro para compilar o projeto."
    exit 1
fi

# Verificar se a classe principal existe
if [ ! -f "build/com/sudoku/Main.class" ]; then
    echo "ERRO: Classe principal não encontrada!"
    echo "Execute ./compile.sh primeiro para compilar o projeto."
    exit 1
fi

echo "Iniciando o Jogo de Sudoku..."
echo

# Executar o jogo
cd build
java com.sudoku.Main "$@"
cd ..

echo
echo "Jogo finalizado."
echo "Pressione Enter para continuar..."
read