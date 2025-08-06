package ContaBancariaComJdbc;
import java.math.BigDecimal;
import java.sql.*;


public class ContaDAO {
    private final String url;
    private final String username;
    private final String password;

    public ContaDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Conta criadorConta(String nome, BigDecimal saldo){
        Conta conta;
        do {
            conta = new Conta(nome, saldo);
        }while (numeroContaExistente(conta.getNumeroConta()));
        return adicionaConta(conta);
    }

    private Boolean numeroContaExistente(int numero){
        var sql = "SELECT 1 FROM conta WHERE numeroConta = ?";
        try(Connection connection = getConnection()){
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Conta adicionaConta(Conta conta) {
        String sql = "INSERT INTO conta (nome, numeroConta, saldo) VALUES(?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, conta.getNome());
            stmt.setInt(2, conta.getNumeroConta());
            stmt.setBigDecimal(3, conta.getSaldo());

            int linhaAfetada = stmt.executeUpdate();

            if (linhaAfetada > 0) {
                System.out.println("Conta criada com sucesso!\n");
            } else {
                System.out.println("Erro ao criar conta!");
            }

        } catch (SQLException e) {
            System.out.println("ERRO:" + e.getMessage());
        }
        return conta;
    }

    public void depositar(int numero, BigDecimal valor) {

        try (Connection connection = getConnection()) {
            String sql = "UPDATE conta SET saldo = saldo + ? WHERE numeroConta = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, valor);
            stmt.setInt(2,  numero);

            if(valor.compareTo(BigDecimal.ZERO) > 0) {
                int linhaAfetada = stmt.executeUpdate();
                if (linhaAfetada > 0) {
                    System.out.println("Deposito concluido com sucesso!");
                } else {
                    System.out.println("\nErro no deposito!");
                }
            }
        } catch (SQLException e) {
            System.out.println("ERRO SQL:" + e.getMessage());
        }
    }


    public void transferir(int numeroContaTransfe, int numeroContaRecebedora, BigDecimal valor) {
        String TransferSql = "UPDATE conta SET saldo = saldo - ? WHERE numeroConta = ? AND saldo >= ?";
        String recebTransfer = "UPDATE conta SET saldo = saldo + ? WHERE numeroConta = ?";

        if(valor.compareTo(BigDecimal.ZERO) > 0){
            try (Connection connection = getConnection()) {
                connection.setAutoCommit(false); // inicia a transação

                if (numeroContaTransfe == numeroContaRecebedora) {
                    throw new IllegalArgumentException("A conta de origem não pode ser igual à conta de destino.");
                }

                // Debita da conta de origem
                try (PreparedStatement transferStmt = connection.prepareStatement(TransferSql)) {
                    transferStmt.setBigDecimal(1, valor);
                    transferStmt.setInt(2, numeroContaTransfe);
                    transferStmt.setBigDecimal(3, valor);

                    int linhasAfetadas = transferStmt.executeUpdate();

                    if (linhasAfetadas == 0) {
                        connection.rollback();
                        throw new RuntimeException("Falha ao debitar: saldo insuficiente ou conta inexistente.");
                    }
                }
                        //Credita na conta
                        try (PreparedStatement receberStmt = connection.prepareStatement(recebTransfer)) {
                            receberStmt.setBigDecimal(1, valor);
                            receberStmt.setInt(2, numeroContaRecebedora);

                            int linhasReceberAfetadas = receberStmt.executeUpdate();


                            if (linhasReceberAfetadas == 0) {
                                connection.rollback();
                                throw new RuntimeException("Falha ao creditar: conta de destino inexistente.");
                            }
                        }

                        // Confirma a transação
                        connection.commit();
                        System.out.printf("Transferecia de %.2f de %d para conta %d realizada com sucesso!\n", valor, numeroContaTransfe, numeroContaTransfe);

            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        } else {System.out.println("Valor invalido!");}

    }
    public Conta dadosConta(int numeroConta){
        String sql = "SELECT nome, numeroConta, saldo FROM conta WHERE numeroConta = ?";

        try(Connection connection = getConnection()){
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, numeroConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                var nome = rs.getString("nome");
                var numero = rs.getInt("numeroConta");
                var saldo = rs.getBigDecimal("saldo");

                return new Conta(nome, numero, saldo);


            } else{System.out.println("Dados não encontrados!");}

        } catch (SQLException e) {
            System.out.println("ERRO:" + e.getMessage());
        }
        return null;
    }

    public void deleteConta(int numero){
        String sql = "DELETE FROM conta WHERE numeroConta = ?";
        try(Connection connection = getConnection()){
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, numero);
            var linhaAfetada = stmt.executeUpdate();

            if(linhaAfetada > 0) System.out.println("Conta cancelada com sucesso!");
            else {System.out.println("Erro ao cancelar conta");}

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
