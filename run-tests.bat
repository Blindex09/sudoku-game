@echo off
echo === EXECUCAO DA SUITE COMPLETA DE TESTES ===
echo.

REM Verificar se build existe
if not exist "build" (
    echo ERRO: Diretorio build nao encontrado!
    echo Execute compile-tests.bat primeiro.
    pause
    exit /b 1
)

REM Verificar se classes principais existem
if not exist "build\com\sudoku\Main.class" (
    echo ERRO: Sistema principal nao compilado!
    echo Execute compile-tests.bat primeiro.
    pause
    exit /b 1
)

REM Verificar se testes existem
if not exist "build\com\sudoku\test\AllTests.class" (
    echo ERRO: Testes nao compilados!
    echo Execute compile-tests.bat primeiro.
    pause
    exit /b 1
)

echo Iniciando execucao dos testes...
echo.

REM Definir configurações da JVM para melhor performance
set JAVA_OPTS=-Xms128m -Xmx512m -XX:+UseG1GC

REM Executar todos os testes
cd build
java %JAVA_OPTS% com.sudoku.test.AllTests

set TEST_RESULT=%ERRORLEVEL%
cd ..

echo.
if %TEST_RESULT% equ 0 (
    echo === TODOS OS TESTES PASSARAM! ===
    echo Sistema Sudoku validado com sucesso!
    echo.
    echo O sistema esta pronto para uso.
) else (
    echo === ALGUNS TESTES FALHARAM! ===
    echo Verifique os resultados acima para detalhes.
    echo.
    echo Recomenda-se revisar o codigo antes do uso.
)

echo.
pause
exit /b %TEST_RESULT%