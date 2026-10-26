package br.ufrgs.inf.lab.nivel3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AnalisadorEmprestimoTest {
  private static final double DELTA = 0.001;
  private final AnalisadorEmprestimo analisador = new AnalisadorEmprestimo();
  @ParameterizedTest
  @CsvSource({
    "-1, 5000, 1000, 12", "1001, 5000, 1000, 12",
    "700, 0, 1000, 12", "700, -1, 1000, 12",
    "700, 5000, 0, 12", "700, 5000, -1, 12",
    "700, 5000, 1000, 0", "700, 5000, 1000, 361"
  })
  void entradasInvalidasDevemSerRecusadas(int score, double renda, double valor, int parcelas) {
    assertEquals(DecisaoEmprestimo.DADOS_INVALIDOS,
        analisador.analisar(score, renda, valor, parcelas));
  }

  @ParameterizedTest
  @CsvSource({
    "0, 1, NEGADO", "299, 12, NEGADO",
    "300, 12, ANALISE_MANUAL", "599, 12, ANALISE_MANUAL",
    "600, 12, APROVADO", "800, 12, APROVADO",
    "1000, 360, APROVADO"
  })
  void scoreEParcelasNosLimitesValidos(int score, int parcelas, DecisaoEmprestimo esperado) {
    assertEquals(esperado, analisador.analisar(score, 10000.0, 100.0, parcelas));
  }

  @ParameterizedTest
  @CsvSource({"300, 0.36", "599, 0.36", "600, 0.24", "799, 0.24", "800, 0.12", "1000, 0.12"})
  void taxaAnualDeveRespeitarFaixas(int score, double esperada) {
    assertEquals(esperada, analisador.taxaAnual(score), 0.0000001);
  }

  @ParameterizedTest
  @CsvSource({"1000, 0.01, 1, 1010.0", "1000, 0.02, 12, 94.5595966229514", "12000, 0.01, 12, 1066.18546414010"})
  void parcelaPriceDeveTerValorEsperado(double valor, double taxa, int meses, double esperada) {
    // valores calculados antes para comparar com a parcela
    assertEquals(esperada, analisador.calcularParcela(valor, taxa, meses), DELTA);
  }

  @ParameterizedTest
  @CsvSource({
    "800, 2019.99, NEGADO", "800, 2020.0, ANALISE_MANUAL", "800, 2020.01, ANALISE_MANUAL",
    "800, 3366.66, ANALISE_MANUAL", "800, 3366.67, APROVADO",
    "600, 2040.0, ANALISE_MANUAL", "600, 3400.0, APROVADO",
    "599, 10000.0, ANALISE_MANUAL"
  })
  void decisaoDeveConsiderarParcelaRendaEScore(int score, double renda, DecisaoEmprestimo esperada) {
    // a parcela muda conforme os juros de cada score
    assertEquals(esperada, analisador.analisar(score, renda, 1000.0, 1));
  }
  @Test
  void comprometimentoExatoDe30PorcentoDeveSerAprovado() {
    // parcela de 1020 e renda de 3400: 30%
    assertEquals(DecisaoEmprestimo.APROVADO, analisador.analisar(600, 3400.0, 1000.0, 1));
  }

  @Test
  void comprometimentoExatoDe50PorcentoDeveIrParaAnalise() {
    // parcela de 1020 e renda de 2040: 50%
    assertEquals(DecisaoEmprestimo.ANALISE_MANUAL, analisador.analisar(600, 2040.0, 1000.0, 1));
  }

  @Test
  void dadosInvalidosTemPrioridadeSobreScoreBaixo() {
    assertEquals(DecisaoEmprestimo.DADOS_INVALIDOS, analisador.analisar(200, 0.0, 1000.0, 12));
  }

}
