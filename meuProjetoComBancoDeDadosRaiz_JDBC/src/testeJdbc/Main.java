package testeJdbc;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
       PersonDAO dao = new PersonDAO();

       Person person1 = new Person("marrrrr");


        List<Person> lista = dao.listarTodos();

        for (Person person : lista){
            System.out.println(person.getName());
        }



    }
}
