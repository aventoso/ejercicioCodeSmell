package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

public class MonederoTest {
  private Cuenta cuenta;
  private Movimiento movimiento;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }



  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }
@Test
  public void retiro(){
    Cuenta cuenta2 = new Cuenta();
    cuenta2.setSaldo(4000);
    movimiento = new Movimiento(LocalDate.now(),2000, false);
    cuenta2.addMovimiento(movimiento);
    assertEquals(2000, cuenta2.getSaldo());
  }

  @Test
  public void consultarExtraccion(){
    movimiento = new Movimiento(LocalDate.now(),2000, false);
    assertFalse(movimiento.isDeposito());
    assertFalse(movimiento.isDeposito());
  }

  @Test
  public void depositoEnUnFehcaYEsDeUnaFecha(){
    Movimiento mov = new Movimiento(LocalDate.now(), 2000, true);
    assertTrue(mov.fueDepositado(LocalDate.now()));
  }

  @Test
  public void sacarDineroHoy(){
    Cuenta cta = new Cuenta(2000);
    cta.sacar(1000);
    assertNotNull(cta.getMovimientos());
  }



}