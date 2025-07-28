@echo off
REM Script para executar o Jogo de Sudoku
REM Certifique-se de que o projeto foi compilado primeiro

echo ===================================
echo     JOGO DE SUDOKU - EXECUTOR
echo ===================================
echo.

REM Verificar se o diretório build existe
if not exist "build" (
    echo ERRO: Diretório build não encontrado!
    echo Execute compile.bat primeiro para compilar o projeto.
    pause
    exit /b 1
)

REM Verificar se a classe principal existe
if not exist "build\com\sudoku\Main.class" (
    echo ERRO: Classe principal não encontrada!
    echo Execute compile.bat primeiro para compilar o projeto.
    pause
    exit /b 1
)

echo Iniciando o Jogo de Sudoku...
echo.

REM Executar o jogo
cd build
java com.sudoku.Main %*
cd ..

echo.
echo Jogo finalizado.
pause