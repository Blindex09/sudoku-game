@echo off
REM Script para compilar e executar o Jogo de Sudoku
REM Autor: Desenvolvedor Senior
REM Versão: 1.0.0

echo ===================================
echo    JOGO DE SUDOKU - COMPILADOR
echo ===================================
echo.

REM Verificar se Java está instalado
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERRO: Java não encontrado!
    echo Por favor, instale o Java 8 ou superior.
    echo Baixe em: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo Java encontrado. Verificando versão...
java -version

echo.
echo Compilando código fonte...

REM Criar diretório de saída se não existir
if not exist "build" mkdir build

REM Compilar todas as classes Java
javac -d build -cp src src\com\sudoku\*.java src\com\sudoku\model\*.java src\com\sudoku\service\*.java src\com\sudoku\view\*.java src\com\sudoku\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERRO: Falha na compilação!
    echo Verifique se todos os arquivos .java estão presentes.
    pause
    exit /b 1
)

echo.
echo Compilação concluída com sucesso!
echo.

REM Perguntar se deseja executar imediatamente
set /p executar="Deseja executar o jogo agora? (s/n): "
if /i "%executar%"=="s" (
    echo.
    echo Iniciando o jogo...
    echo.
    cd build
    java com.sudoku.Main
    cd ..
) else (
    echo.
    echo Para executar o jogo manualmente:
    echo   cd build
    echo   java com.sudoku.Main
    echo.
    echo Ou execute run.bat
)

echo.
pause