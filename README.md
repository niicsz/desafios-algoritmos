# Soluções CodeVita

Soluções em **Java 8** para 6 problemas de programação competitiva. Cada pasta tem um `Main.java` (classe `Main`, sem `package`) e uma pasta `tests/` com as entradas (`inN.txt`) e saídas esperadas (`outN.txt`) dos exemplos do enunciado.

```bash
cd EX1
javac --release 8 -d out Main.java
java -cp out Main < tests/in1.txt
```

| # | Problema | Ideia principal |
|---|---|---|
| [EX1](EX1/Main.java) | RasikhBox | Simulação: rotação 90° + gravidade por contagem por coluna |
| [EX2](EX2/Main.java) | MaxMatchbox | Offset interno de polígono retilíneo + shoelace, em inteiros (décimos) |
| [EX3](EX3/Main.java) | SmallestRegion | Todas as dobras válidas; menor gap em x × menor gap em y |
| [EX4](EX4/Main.java) | GravityGlide | Simulação ponto a ponto com análise dos braços das rampas em cada ponto |
| [EX5](EX5/Main.java) | ShapeCount | Compressão de coordenadas + somas de prefixo de cobertura |
| [EX6](EX6/Main.java) | PickSmartPlayWise | Grafo de interseções, poda de folhas (2-core), área do ciclo vs. círculo com a sobra |

---

## EX1 — RasikhBox

Uma caixa é uma matriz `M × N` com pedras (`*`) e espaços vazios (`.`). A caixa é girada 90° para a direita ou para a esquerda conforme as instruções. Após o estado inicial e após cada rotação, a gravidade age e as pedras caem para o fundo. Determine a configuração final.

**Restrições:** `3 <= M, N <= 200`

**Entrada:** `M N`; depois `M` linhas com `N` caracteres separados por espaço (`.` ou `*`); depois `K`; depois `K` linhas com `right` ou `left`.

**Saída:** a matriz final.

| Entrada | Saída |
|---|---|
| <pre>4 4<br>* * . .<br>. * . .<br>* * * .<br>. * . .<br>1<br>right</pre> | <pre>. . . .<br>* . . .<br>* * . .<br>* * * *</pre> |
| <pre>3 4<br>. . . .<br>. * . *<br>* * * *<br>1<br>left</pre> | <pre>. . *<br>. . *<br>. * *<br>. * *</pre> |

---

## EX2 — MaxMatchbox

Uma folha com formato retilíneo (polígono só com ângulos retos) é dobrada para cima numa altura `H`, formando uma caixa. Cada lado é recuado `H` para dentro; o volume é `área da base × H`. Cada aresta precisa manter pelo menos 0,1 de comprimento depois da dobra, e `H` deve ser múltiplo de 0,1. Encontre o volume máximo.

**Restrições:** `4 <= N <= 15`, `0 <= x, y <= 25`

**Entrada:** `N` (número de vértices); depois `N` linhas com `x y`, em sequência.

**Saída:** volume máximo com 2 casas decimais.

| Entrada | Saída |
|---|---|
| <pre>6<br>0 0<br>2 0<br>2 2<br>1 2<br>1 1<br>0 1</pre> | `0.31` (H = 0.2) |
| <pre>8<br>0 0<br>8 0<br>8 2<br>5 2<br>5 3<br>3 3<br>3 2<br>0 2</pre> | `3.94` (H = 0.4) |

> Pelos exemplos, a restrição de 0,1 vale para cada aresta original reduzida `H` nas duas pontas: `L − 2H ≥ 0,1`.

---

## EX3 — SmallestRegion

Uma folha retangular tem vários retângulos coloridos que não se sobrepõem. A folha pode ser dobrada na horizontal e/ou na vertical, quantas vezes quiser, sempre em coordenadas inteiras e sem que nenhuma dobra atravesse o meio de um retângulo. Depois de desdobrar, os vincos dividem a folha em regiões retangulares. Encontre a menor área de região possível.

**Restrições:** `4 <= N <= 16`

**Entrada:** `N`; depois `N` linhas `X1 Y1 X2 Y2` (canto inferior esquerdo e superior direito de cada retângulo); por fim `X1 Y1 X2 Y2` da folha.

**Saída:** um inteiro, a menor área.

| Entrada | Saída |
|---|---|
| <pre>6<br>0 0 1 2<br>1 0 3 3<br>3 4 5 5<br>3 0 5 4<br>1 3 3 5<br>0 2 1 5<br>0 0 5 5</pre> | `5` |
| <pre>6<br>10 11 12 14<br>13 10 15 13<br>16 11 18 13<br>13 13 16 15<br>12 15 14 16<br>15 15 17 16<br>10 10 18 16</pre> | `2` |

---

## EX4 — GravityGlide

Uma bola é solta num plano com rampas diagonais (45°). Ela cai em linha reta até encontrar uma rampa e então desliza por ela, gastando 1 de energia por unidade deslizada. Se ficar presa entre duas rampas (formando um V), pode se destravar gastando `x × y` de energia (coordenadas atuais) e continua deslizando. A bola para ao chegar ao chão (y = 0) ou quando a energia acaba. Na borda de uma rampa, a bola fica do lado de fora (o lado voltado para cima).

**Restrições:** `1 <= N <= 50`, `0 <= x, y <= 50`

**Entrada:** `N`; depois `N` linhas `x1 y1 x2 y2` com as pontas de cada rampa; por fim `x y energia` da bola.

**Saída:** posição final `x y`.

| Entrada | Saída |
|---|---|
| <pre>8<br>2 4 5 1<br>1 3 3 1<br>2 5 5 8<br>4 5 6 3<br>1 10 3 8<br>4 10 6 12<br>1 14 3 12<br>3 6 1 8<br>5 15 21</pre> | `2 4` |
| <pre>7<br>1 1 3 3<br>4 1 6 3<br>4 5 5 6<br>2 5 4 3<br>2 2 0 4<br>5 2 7 0<br>5 6 7 4<br>2 7 5</pre> | `4 0` |

> Pelos exemplos, cair não gasta energia; só deslizar e destravar.

---

## EX5 — ShapeCount

Dado um conjunto de segmentos horizontais e verticais (que podem parar no meio do desenho), conte quantos retângulos existem na figura, ou seja, retângulos cujos quatro lados estão totalmente desenhados.

**Restrições:** `6 <= N <= 42`

**Entrada:** `N`; depois `N` linhas `X1 Y1 X2 Y2`.

**Saída:** número de retângulos.

| Entrada | Saída |
|---|---|
| <pre>7<br>0 0 0 4<br>1 0 1 2<br>3 0 3 4<br>4 0 4 4<br>0 4 4 4<br>0 2 4 2<br>0 0 4 0</pre> | `12` |
| <pre>6<br>10 10 14 10<br>10 12 14 12<br>10 14 14 14<br>10 10 10 14<br>12 14 12 10<br>14 10 14 14</pre> | `9` |

---

## EX6 — PickSmartPlayWise

Palitos retos estão num plano. Se alguns deles formam uma figura fechada, Kalyan fica com ela. O computador recebe o resto (palitos não usados e as pontas que passam da figura), pode cortá-los à vontade e tenta formar outra figura fechada. Vence quem tiver a maior área. Se não houver figura fechada, o jogo é `Abandoned`.

**Restrições:** `1 <= N <= 20`, `0 <= x, y <= 50`; no máximo uma figura fechada; palitos extras não cruzam a figura; no máximo duas linhas se cruzam no mesmo ponto; interseções arredondadas para 2 casas.

**Entrada:** `N`; depois `N` linhas `x1 y1 x2 y2`.

**Saída:** `Kalyan`, `Computer` ou `Abandoned`.

| Entrada | Saída |
|---|---|
| <pre>4<br>2 1 2 6<br>5 1 5 6<br>0 2 6 2<br>0 5 6 5</pre> | `Kalyan` |
| <pre>5<br>4 1 4 5<br>5 0 1 4<br>1 2 5 6<br>0 0 2 2<br>5 2 5 5</pre> | `Computer` |
| <pre>5<br>1 2 4 4<br>5 5 5 1<br>1 6 4 4<br>1 4 3 1<br>4 2 6 3</pre> | `Abandoned` |

> A área máxima do computador foi modelada como um círculo com perímetro igual à sobra: `sobra² / (4π)`.
