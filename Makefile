# Makefile para o Jogo de Sudoku
# Autor: Desenvolvedor Senior
# Versão: 1.0.0

# Variáveis
JAVAC = javac
JAVA = java
SRC_DIR = src
BUILD_DIR = build
MAIN_CLASS = com.sudoku.Main
PACKAGE_DIR = com/sudoku

# Diretórios de origem
SOURCES = $(wildcard $(SRC_DIR)/$(PACKAGE_DIR)/*.java) \
          $(wildcard $(SRC_DIR)/$(PACKAGE_DIR)/model/*.java) \
          $(wildcard $(SRC_DIR)/$(PACKAGE_DIR)/service/*.java) \
          $(wildcard $(SRC_DIR)/$(PACKAGE_DIR)/view/*.java) \
          $(wildcard $(SRC_DIR)/$(PACKAGE_DIR)/util/*.java)

# Arquivos de classe correspondentes
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BUILD_DIR)/%.class)

# Regra padrão
all: compile

# Compilar o projeto
compile: $(BUILD_DIR) $(CLASSES)
	@echo "Compilação concluída com sucesso!"

# Criar diretório de build
$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

# Regra para compilar arquivos .java em .class
$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) -d $(BUILD_DIR) -cp $(SRC_DIR) $<

# Executar o jogo
run: compile
	@echo "Iniciando o Jogo de Sudoku..."
	cd $(BUILD_DIR) && $(JAVA) $(MAIN_CLASS)

# Executar com debug
debug: compile
	@echo "Iniciando o Jogo de Sudoku (modo debug)..."
	cd $(BUILD_DIR) && $(JAVA) $(MAIN_CLASS) --debug

# Executar sem cores
no-colors: compile
	@echo "Iniciando o Jogo de Sudoku (sem cores)..."
	cd $(BUILD_DIR) && $(JAVA) $(MAIN_CLASS) --no-colors

# Limpar arquivos compilados
clean:
	@echo "Limpando arquivos compilados..."
	rm -rf $(BUILD_DIR)

# Forçar recompilação
rebuild: clean compile

# Exibir ajuda
help:
	@echo "Comandos disponíveis:"
	@echo "  make compile   - Compila o projeto"
	@echo "  make run       - Compila e executa o jogo"
	@echo "  make debug     - Executa em modo debug"
	@echo "  make no-colors - Executa sem cores"
	@echo "  make clean     - Remove arquivos compilados"
	@echo "  make rebuild   - Limpa e recompila"
	@echo "  make help      - Exibe esta ajuda"

# Verificar se Java está instalado
check-java:
	@which $(JAVA) > /dev/null || (echo "ERRO: Java não encontrado!" && exit 1)
	@which $(JAVAC) > /dev/null || (echo "ERRO: javac não encontrado!" && exit 1)
	@echo "Java encontrado:"
	@$(JAVA) -version

# Declarações de alvos que não são arquivos
.PHONY: all compile run debug no-colors clean rebuild help check-java