package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }//ya esta predefinido, borrar uno de los dos

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void addMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
    this.saldo = this.calcularValor(movimiento);
  }

  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (masDeTres()) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    addMovimiento(new Movimiento(LocalDate.now(), cuanto, true));
  }

  public void sacar(double cuanto) {//siento que el metodo es muy largo, pero es duda, no certeza
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {//usar la variable saldo
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    addMovimiento(new Movimiento(LocalDate.now(), cuanto, false));
  }


  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  private boolean masDeTres(){
    return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3;
  }

  public double calcularValor(Movimiento movimiento) {
    return this.getSaldo() + (movimiento.isDeposito()? +movimiento.getMonto(): -movimiento.getMonto());
  }
}
