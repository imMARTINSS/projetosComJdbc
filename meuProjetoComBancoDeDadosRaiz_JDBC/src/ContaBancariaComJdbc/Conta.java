package ContaBancariaComJdbc;

import java.math.BigDecimal;
import java.util.Random;

public class Conta {
    private final String nome;
    private final int numeroConta;
    private final BigDecimal saldo;

    public Conta(String nome, BigDecimal saldo){
        this.nome = nome;
        this.numeroConta = geradorNumeroConta();
        this.saldo = saldo;
    }

    public Conta(String nome,int numeroConta, BigDecimal saldo){
        this.nome = nome;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
    }

    public String getNome(){ return nome;}
    public int getNumeroConta(){return numeroConta;}
    public BigDecimal getSaldo(){return saldo;}

    private static int geradorNumeroConta(){
        Random random = new Random();
        return 100_000 + random.nextInt(900_000);
    }

    @Override
    public String toString() {
        return String.format("Nome: %s\nNumero da sua conta: %d\nSaldo: %.2f\n", nome, numeroConta, saldo);
    }
}

