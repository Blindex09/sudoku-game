Jogo de Sudoku em Java

Um jogo completo de Sudoku implementado em Java com interface de console intuitiva e funcionalidades avançadas.

FUNCIONALIDADES PRINCIPAIS

• Geração automática de puzzles válidos
• 4 níveis de dificuldade (Fácil, Médio, Difícil, Expert)
• Interface colorida e intuitiva no terminal
• Sistema de pontuação baseado em desempenho
• Validação de jogadas em tempo real
• Sistema de dicas inteligente
• Pausar e resumir jogos
• Resetar jogo para estado inicial
• Resolver automaticamente
• Estatísticas detalhadas de jogo
• Tratamento robusto de erros

REQUISITOS DO SISTEMA

• Java 8 ou superior
• Terminal com suporte a cores ANSI (recomendado)
• Sistema operacional: Windows, macOS ou Linux
• Pelo menos 32MB de memória RAM disponível

COMO COMPILAR E EXECUTAR

1. Navegue até o diretório do projeto
   cd sudoku-game

2. Compile todas as classes Java
   javac -d . src/com/sudoku/*.java src/com/sudoku/model/*.java src/com/sudoku/service/*.java src/com/sudoku/view/*.java src/com/sudoku/util/*.java

3. Execute o jogo
   java com.sudoku.Main

OPÇÕES DE LINHA DE COMANDO

--debug, -d        Ativa modo debug com informações detalhadas
--no-colors, -nc   Desabilita cores na interface
--help, -h         Exibe ajuda
--version, -v      Exibe informações de versão

Exemplos:
java com.sudoku.Main --debug
java com.sudoku.Main --no-colors

COMO JOGAR

1. Inicie o jogo e escolha "Novo Jogo"
2. Selecione o nível de dificuldade desejado
3. Use o menu numerado para navegar pelas opções
4. Para fazer uma jogada:
   - Digite: linha coluna valor (exemplo: 3 7 5)
   - Isso coloca o número 5 na linha 3, coluna 7
5. Para limpar uma célula:
   - Digite: linha coluna 0 (exemplo: 3 7 0)
6. Use as opções do menu para:
   - Obter dicas quando necessário
   - Pausar/resumir o jogo
   - Ver estatísticas
   - Resetar o jogo

REGRAS DO SUDOKU

• Preencha o tabuleiro 9x9 com números de 1 a 9
• Cada linha deve conter todos os números de 1 a 9
• Cada coluna deve conter todos os números de 1 a 9
• Cada quadrante 3x3 deve conter todos os números de 1 a 9
• Números não podem se repetir na mesma linha, coluna ou quadrante

SISTEMA DE PONTUAÇÃO

• +10 pontos por jogada correta
• -5 pontos por erro
• -15 pontos por dica utilizada
• Bônus de tempo (até 1000 pontos)
• Penalidade por erros e dicas no final

NÍVEIS DE DIFICULDADE

• Fácil: 40 números preenchidos inicialmente
• Médio: 30 números preenchidos inicialmente
• Difícil: 20 números preenchidos inicialmente
• Expert: 17 números preenchidos inicialmente

INTERFACE DO JOGO

O jogo utiliza cores para facilitar a identificação:
• Números brancos em negrito: células fixas (não podem ser alteradas)
• Números verdes: suas jogadas (podem ser alteradas)
• Pontos ciano: células vazias
• Destaques amarelos: célula selecionada ou dica

ESTRUTURA DO PROJETO

src/
├── com/
│   └── sudoku/
│       ├── Main.java                 # Classe principal
│       ├── model/                    # Modelos de dados
│       │   ├── Cell.java            # Célula individual
│       │   ├── Board.java           # Tabuleiro 9x9
│       │   └── GameState.java       # Estado do jogo
│       ├── service/                  # Lógica de negócio
│       │   ├── GameManager.java     # Gerenciador principal
│       │   ├── PuzzleGenerator.java # Gerador de puzzles
│       │   └── SudokuSolver.java    # Resolvedor de puzzles
│       ├── view/                     # Interface do usuário
│       │   ├── ConsoleView.java     # Interface de console
│       │   └── GameController.java  # Controlador principal
│       └── util/                     # Utilitários
│           ├── InputValidator.java  # Validação de entrada
│           ├── TimeFormatter.java   # Formatação de tempo
│           └── GameConfig.java      # Configurações

RESOLUÇÃO DE PROBLEMAS

Problema: Cores não aparecem no terminal
Solução: Execute com --no-colors ou use um terminal que suporte ANSI

Problema: Caracteres especiais não aparecem corretamente
Solução: Configure seu terminal para UTF-8

Problema: Erro de compilação
Solução: Verifique se o Java está instalado e se todas as classes estão no local correto

Problema: Erro de memória
Solução: Execute com mais memória: java -Xmx128m com.sudoku.Main

CONTRIBUIÇÕES

Este projeto foi desenvolvido como um exemplo completo de programação orientada a objetos em Java, demonstrando:
• Arquitetura MVC (Model-View-Controller)
• Programação orientada a objetos
• Tratamento de exceções
• Validação de entrada
• Interface de usuário no terminal
• Algoritmos de backtracking
• Estruturas de dados

LICENÇA

Este projeto é fornecido como material educacional e pode ser usado para fins de aprendizado e desenvolvimento.

AUTOR

Desenvolvedor Senior
Versão 1.0.0
2025

AGRADECIMENTOS

Obrigado por usar o Jogo de Sudoku! Esperamos que você se divirta jogando e aprendendo com o código.

Para suporte ou dúvidas, consulte o código-fonte ou a documentação inline presente em todas as classes.