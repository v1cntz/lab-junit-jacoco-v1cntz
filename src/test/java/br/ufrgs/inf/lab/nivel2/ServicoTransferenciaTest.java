package br.ufrgs.inf.lab.nivel2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import br.ufrgs.inf.lab.nivel1.Conta;
import org.junit.jupiter.api.BeforeEach;

class ServicoTransferenciaTest {
  private static final double DELTA = 0.001;
  private ServicoTransferencia servico;
  private Conta origem;
  private Conta destino;

  @BeforeEach
  void preparar() {
    servico = new ServicoTransferencia();
    origem = new Conta("Origem", 10_000.0);
    destino = new Conta("Destino", 0.0);
  }

  @Test
  void transferenciaPequenaDeveSerAceitaSemTarifa() {

    StatusTransferencia status = servico.transferir(origem, destino, 200.0);

    assertEquals(StatusTransferencia.OK, status);
    assertEquals(0.0, servico.calcularTarifa(200.0), DELTA);
    assertEquals(9800.0, origem.getSaldo(), DELTA);
    assertEquals(200.0, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaAcimaDe1000PagaTarifaDe1Porcento() {

    StatusTransferencia status = servico.transferir(origem, destino, 2000.0);

    assertEquals(StatusTransferencia.OK, status);
    assertEquals(20.0, servico.calcularTarifa(2000.0), DELTA);
    assertEquals(7980.0, origem.getSaldo(), DELTA);
    assertEquals(2000.0, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaDeExatamente1000NaoPagaTarifa() {

    StatusTransferencia status = servico.transferir(origem, destino, 1000.0);

    assertEquals(StatusTransferencia.OK, status);
    assertEquals(0.0, servico.calcularTarifa(1000.0), DELTA);
    assertEquals(9000.0, origem.getSaldo(), DELTA);
    assertEquals(1000.0, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaLogoAcimaDe1000PagaTarifa() {

    StatusTransferencia status = servico.transferir(origem, destino, 1000.01);

    assertEquals(StatusTransferencia.OK, status);
    assertEquals(10.0001, servico.calcularTarifa(1000.01), DELTA);
    assertEquals(8989.9899, origem.getSaldo(), DELTA);
    assertEquals(1000.01, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaDeExatamente5000DeveSerPermitida() {

    StatusTransferencia status = servico.transferir(origem, destino, 5000.0);

    assertEquals(StatusTransferencia.OK, status);
    assertEquals(50.0, servico.calcularTarifa(5000.0), DELTA);
    assertEquals(4950.0, origem.getSaldo(), DELTA);
    assertEquals(5000.0, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaAcimaDoLimiteDeveSerRecusada() {
    StatusTransferencia status = servico.transferir(origem, destino, 5000.01);
    assertEquals(StatusTransferencia.ACIMA_DO_LIMITE, status);
    assertEquals(10_000.0, origem.getSaldo(), DELTA);
    assertEquals(0.0, destino.getSaldo(), DELTA);
  }

  @Test
  void mesmaContaDeveSerRecusada() {
    StatusTransferencia status = servico.transferir(origem, origem, 200.0);
    assertEquals(StatusTransferencia.MESMA_CONTA, status);
    assertEquals(10_000.0, origem.getSaldo(), DELTA);
    assertEquals(0.0, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaZeroDeveSerRecusada() {
    StatusTransferencia status = servico.transferir(origem, destino, 0.0);
    assertEquals(StatusTransferencia.VALOR_INVALIDO, status);
    assertEquals(10_000.0, origem.getSaldo(), DELTA);
    assertEquals(0.0, destino.getSaldo(), DELTA);
  }

  @Test
  void transferenciaNegativaDeveSerRecusada() {
    StatusTransferencia status = servico.transferir(origem, destino, -10.0);
    assertEquals(StatusTransferencia.VALOR_INVALIDO, status);
    assertEquals(10_000.0, origem.getSaldo(), DELTA);
    assertEquals(0.0, destino.getSaldo(), DELTA);
  }

  @Test
  void mesmaContaTemPrioridadeSobreValorInvalido() {
    StatusTransferencia status = servico.transferir(origem, origem, 0.0);
    assertEquals(StatusTransferencia.MESMA_CONTA, status);
    assertEquals(10_000.0, origem.getSaldo(), DELTA);
    assertEquals(0.0, destino.getSaldo(), DELTA);
  }

  @Test
  void saldoInsuficienteParaValorMaisTarifaNaoAlteraNenhumaConta() {
    origem = new Conta("Origem", 2000.0);
    destino = new Conta("Destino", 150.0);
    StatusTransferencia status = servico.transferir(origem, destino, 2000.0);
    assertEquals(StatusTransferencia.SALDO_INSUFICIENTE, status);
    assertEquals(2000.0, origem.getSaldo(), DELTA);
    assertEquals(150.0, destino.getSaldo(), DELTA);
  }

  @Test
  void saldoExatoParaValorMaisTarifaDeveSerAceito() {
    origem = new Conta("Origem", 2020.0);
    destino = new Conta("Destino", 150.0);
    StatusTransferencia status = servico.transferir(origem, destino, 2000.0);
    assertEquals(StatusTransferencia.OK, status);
    assertEquals(0.0, origem.getSaldo(), DELTA);
    assertEquals(2150.0, destino.getSaldo(), DELTA);
  }

}
