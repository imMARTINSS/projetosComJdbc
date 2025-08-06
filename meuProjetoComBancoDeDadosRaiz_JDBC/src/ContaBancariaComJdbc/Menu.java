package ContaBancariaComJdbc;
import java.util.Scanner;


public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    public void exibir() {
        boasVindas();
        esperar(2000);
        int opcao;
        do {
            System.out.println("\n----------MartinsBank----------");
            System.out.println("Selecione uma opção:");
            System.out.println("1.Cria conta");
            System.out.println("2.Acessar conta");
            System.out.println("3.Cancelar conta");


            System.out.print("\nSelecione uma opção(Digite 0 para sair):");
            opcao = scanner.nextInt();
            executarOp(opcao);
        } while (opcao != 0);
    }


        private void boasVindas(){
            System.out.println("Bem Vindo ao MartinsBank\n");
        }


        private void executarOp(int opcao){
            ContaDAO dao = new ContaDAO("jdbc:mysql://localhost:3306/bank_db", "root", "root");
            switch(opcao){
                    case 0 -> System.out.println("\nObrigado e volte sempre!");

                    case 1 -> {
                        System.out.println("Informe seu nome:");
                        var nome = scanner.nextLine();
                        System.out.println("Informe o valor do primeiro deposito:");
                        var deposito = scanner.nextBigDecimal();
                        Conta conta = dao.criadorConta(nome, deposito);

                        System.out.println("Dados da conta:");
                        System.out.printf("Nome: %s | Numero conta: %d | Saldo:%.2f\n",nome, conta.getNumeroConta(), conta.getSaldo());
                    }
                    case 2 ->{
                        System.out.println("\nInforme o numero da sua conta:");
                        var numero = scanner.nextInt();
                        Conta conta = dao.dadosConta(numero);
                        System.out.printf("\nBem vindo %s\n\n",conta.getNome());
                        esperar(500);
                        System.out.printf("Numero da conta:%d  Saldo:%.2f\n", conta.getNumeroConta(), conta.getSaldo()); // testa o \t pra tabular depois
                        esperar(1000);
                        System.out.println("\nOperações disponiveis:");
                        System.out.println("1.Fazer um deposito");
                        System.out.println("2.Fazer uma transferencia");
                        System.out.println("\nEscolha uma opção:");
                        var op = scanner.nextInt();
                        if(op == 1){
                            System.out.println("Informe o valor para deposito:");
                            var saldo = scanner.nextBigDecimal();
                            System.out.println("\nSaldo anterior:");
                            System.out.printf("Numero da conta:%d  Saldo:%.2f\n", conta.getNumeroConta(), conta.getSaldo()); // testa o \t pra tabular depois
                            dao.depositar(numero, saldo);
                            Conta conta1 = dao.dadosConta(numero);
                            esperar(500);
                            System.out.println("\nSaldo atual:");
                            System.out.printf("Numero da conta:%d  Saldo:%.2f\n", conta1.getNumeroConta(), conta1.getSaldo()); // testa o \t pra tabular depois
                        }

                        if(op == 2){
                            System.out.println("informe o numero da conta de destino:");
                            var destinatario = scanner.nextInt();
                            if(conta.getNumeroConta() != destinatario){
                                System.out.println("Informe o valor da transferencia:");
                                var valor = scanner.nextBigDecimal();
                                System.out.println("\nSaldo anterior:");
                                System.out.printf("Numero da conta:%d  Saldo:%.2f\n", conta.getNumeroConta(), conta.getSaldo()); // testa o \t pra tabular depois
                                dao.transferir(conta.getNumeroConta(), destinatario, valor);
                                Conta conta2 = dao.dadosConta(conta.getNumeroConta());
                                System.out.println("\nSaldo atual:");
                                System.out.printf("\nNumero da conta:%d  Saldo:%.2f\n", conta2.getNumeroConta(), conta2.getSaldo()); // testa o \t pra tabular depois
                            }

                        }

                    }
                    case 3 ->{
                        System.out.println("Informe o numero da sua conta:");
                        var numero = scanner.nextInt();
                        Conta conta = dao.dadosConta(numero);
                        esperar(1000);
                        System.out.println("Deseja prosseguir?(Digite 1 para SIM 2 para NÃO):");
                        var op = scanner.nextInt();
                        if (op == 1){
                            dao.deleteConta(numero);
                            esperar(1000);
                            System.out.printf("\nConta de numero %d cancelada com sucesso", conta.getNumeroConta());
                        }
                        if (op == 2){
                            new Menu().exibir();
                        }
                    }
                }
    }

    private static void esperar(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Erro ao pausar:" + e.getMessage());
        }
    }
}
