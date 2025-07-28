@echo off
echo === COMPILACAO DOS TESTES DO SISTEMA SUDOKU ===
echo.

REM Criar diretório de build se não existir
if not exist "build" (
    mkdir build
    echo Diretorio build criado.
)

echo Compilando sistema principal...
javac -d build -cp src src\com\sudoku\*.java src\com\sudoku\model\*.java src\com\sudoku\service\*.java src\com\sudoku\view\*.java src\com\sudoku\util\*.java

if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha na compilacao do sistema principal!
    pause
    exit /b 1
)

echo Sistema principal compilado com sucesso!

echo.
echo Compilando framework de testes...
javac -d build -cp build test\com\sudoku\test\TestFramework.java

if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha na compilacao do framework de testes!
    pause
    exit /b 1
)

echo.
echo Compilando testes unitarios...
javac -d build -cp build test\com\sudoku\model\*.java test\com\sudoku\service\*.java test\com\sudoku\util\*.java

if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha na compilacao dos testes unitarios!
    pause
    exit /b 1
)

echo.
echo Compilando testes de integracao...
javac -d build -cp build test\com\sudoku\integration\*.java

if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha na compilacao dos testes de integracao!
    pause
    exit /b 1
)

echo.
echo Compilando executor principal de testes...
javac -d build -cp build test\com\sudoku\test\AllTests.java

if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha na compilacao do executor de testes!
    pause
    exit /b 1
)

echo.
echo === COMPILACAO CONCLUIDA COM SUCESSO! ===
echo Todos os testes foram compilados e estao prontos para execucao.
echo.
echo Para executar os testes, use: run-tests.bat
echo.
pause