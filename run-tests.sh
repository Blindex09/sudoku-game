#!/bin/bash

echo "=== EXECUÇÃO DA SUÍTE COMPLETA DE TESTES ==="
echo

# Verificar se build existe
if [ ! -d "build" ]; then
    echo "ERRO: Diretório build não encontrado!"
    echo "Execute ./compile-tests.sh primeiro."
    exit 1
fi

# Verificar se classes principais existem
if [ ! -f "build/com/sudoku/Main.class" ]; then
    echo "ERRO: Sistema principal não compilado!"
    echo "Execute ./compile-tests.sh primeiro."
    exit 1
fi

# Verificar se testes existem
if [ ! -f "build/com/sudoku/test/AllTests.class" ]; then
    echo "ERRO: Testes não compilados!"
    echo "Execute ./compile-tests.sh primeiro."
    exit 1
fi

echo "Iniciando execução dos testes..."
echo

# Definir configurações da JVM para melhor performance
JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseG1GC"

# Executar todos os testes
cd build
java $JAVA_OPTS com.sudoku.test.AllTests
TEST_RESULT=$?
cd ..

echo
if [ $TEST_RESULT -eq 0 ]; then
    echo "=== TODOS OS TESTES PASSARAM! ==="
    echo "Sistema Sudoku validado com sucesso!"
    echo
    echo "O sistema está pronto para uso."
else
    echo "=== ALGUNS TESTES FALHARAM! ==="
    echo "Verifique os resultados acima para detalhes."
    echo
    echo "Recomenda-se revisar o código antes do uso."
fi

echo
exit $TEST_RESULT