package br.ufrgs.inf.lab.nivel1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ContaTest {
  private static final double DELTA = 0.001;

  @Test
  void contaNovaDeveGuardarTitularESaldoInicial() {
    Conta conta = new Conta("Ana", 100.0);
    assertEquals("Ana", conta.getTitular());
    assertEquals(100.0, conta.getSaldo(), DELTA);
  }

  @Test
  void depositoValidoDeveAumentarOSaldo() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.depositar(50.0);

    assertTrue(ok);
    assertEquals(150.0, conta.getSaldo(), DELTA);
  }

  @Test
  void depositoComValorNegativoDeveSerRecusado() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.depositar(-10.0);

    assertFalse(ok);
    assertEquals(100.0, conta.getSaldo(), DELTA);
  }

  @Test
  void depositoZeroDeveSerRecusado() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.depositar(0.0);

    assertFalse(ok);
    assertEquals(100.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saqueValidoDeveDiminuirOSaldo() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.sacar(30.0);

    assertTrue(ok);
    assertEquals(70.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saqueMaiorQueOSaldoDeveSerRecusado() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.sacar(100.01);

    assertFalse(ok);
    assertEquals(100.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saqueDoSaldoExatoDeveSerPermitidoEZerarAConta() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.sacar(100.0);

    assertTrue(ok);
    assertEquals(0.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saqueZeroDeveSerRecusado() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.sacar(0.0);

    assertFalse(ok);
    assertEquals(100.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saqueNegativoDeveSerRecusado() {
    Conta conta = new Conta("Ana", 100.0);

    boolean ok = conta.sacar(-10.0);

    assertFalse(ok);
    assertEquals(100.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saldoInicialNegativoDeveVirarZero() {
    Conta conta = new Conta("Ana", -1.0);
    assertEquals(0.0, conta.getSaldo(), DELTA);
  }

  @Test
  void saldoInicialZeroDeveSerAceito() {
    Conta conta = new Conta("Ana", 0.0);
    assertEquals(0.0, conta.getSaldo(), DELTA);
  }

}
