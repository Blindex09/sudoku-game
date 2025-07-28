#!/bin/bash
# Script para compilar e executar o Jogo de Sudoku
# Autor: Desenvolvedor Senior
# Versão: 1.0.0

echo "==================================="
echo "   JOGO DE SUDOKU - COMPILADOR"
echo "==================================="
echo

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "ERRO: Java não encontrado!"
    echo "Por favor, instale o Java 8 ou superior."
    echo "Ubuntu/Debian: sudo apt install openjdk-11-jdk"
    echo "macOS: brew install openjdk@11"
    exit 1
fi

echo "Java encontrado. Verificando versão..."
java -version

echo
echo "Compilando código fonte..."

# Criar diretório de saída se não existir
mkdir -p build

# Compilar todas as classes Java
javac -d build -cp src src/com/sudoku/*.java src/com/sudoku/model/*.java src/com/sudoku/service/*.java src/com/sudoku/view/*.java src/com/sudoku/util/*.java

if [ $? -ne 0 ]; then
    echo
    echo "ERRO: Falha na compilação!"
    echo "Verifique se todos os arquivos .java estão presentes."
    exit 1
fi

echo
echo "Compilação concluída com sucesso!"
echo

# Perguntar se deseja executar imediatamente
read -p "Deseja executar o jogo agora? (s/n): " executar
if [[ $executar == "s" || $executar == "S" ]]; then
    echo
    echo "Iniciando o jogo..."
    echo
    cd build
    java com.sudoku.Main
    cd ..
else
    echo
    echo "Para executar o jogo manualmente:"
    echo "  cd build"
    echo "  java com.sudoku.Main"
    echo
    echo "Ou execute ./run.sh"
fi

echo
echo "Pressione Enter para continuar..."
read