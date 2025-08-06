package testeJdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private final String url = "jdbc:mysql://localhost:3306/javonico_db";
    private final String userName = "root";
    private final String password = "root";

    public List<Person> listarTodos(){
        List<Person> personList = new ArrayList<>();
        var sql = "SELECT * FROM pessoas";

        try (Connection connection = DriverManager.getConnection(url, userName, password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ){

            while (rs.next()) {
                var nome = rs.getString("nome");
                personList.add(new Person(nome));
            }

        } catch (SQLException e){
            System.out.println("Erro:" + e.getMessage());
        }

        return personList;
    }

    public void inserirNome(Person person){

        var sql = "INSERT INTO pessoas (nome) VALUES(?)";

        try(Connection connection = DriverManager.getConnection(url, userName, password)) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, person.getName());
            stmt.executeUpdate();

            System.out.println("Nome inserido com sucesso.");

        } catch (SQLException e) {
            System.out.println("Erro:" + e.getMessage());
        }
    }
}
