#!/bin/bash

echo "=== COMPILAÇÃO DOS TESTES DO SISTEMA SUDOKU ==="
echo

# Criar diretório de build se não existir
if [ ! -d "build" ]; then
    mkdir -p build
    echo "Diretório build criado."
fi

echo "Compilando sistema principal..."
javac -d build -cp src src/com/sudoku/*.java src/com/sudoku/model/*.java src/com/sudoku/service/*.java src/com/sudoku/view/*.java src/com/sudoku/util/*.java

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação do sistema principal!"
    exit 1
fi

echo "Sistema principal compilado com sucesso!"

echo
echo "Compilando framework de testes..."
javac -d build -cp build test/com/sudoku/test/TestFramework.java

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação do framework de testes!"
    exit 1
fi

echo
echo "Compilando testes unitários..."
javac -d build -cp build test/com/sudoku/model/*.java test/com/sudoku/service/*.java test/com/sudoku/util/*.java

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação dos testes unitários!"
    exit 1
fi

echo
echo "Compilando testes de integração..."
javac -d build -cp build test/com/sudoku/integration/*.java

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação dos testes de integração!"
    exit 1
fi

echo
echo "Compilando executor principal de testes..."
javac -d build -cp build test/com/sudoku/test/AllTests.java

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação do executor de testes!"
    exit 1
fi

echo
echo "=== COMPILAÇÃO CONCLUÍDA COM SUCESSO! ==="
echo "Todos os testes foram compilados e estão prontos para execução."
echo
echo "Para executar os testes, use: ./run-tests.sh"
echo