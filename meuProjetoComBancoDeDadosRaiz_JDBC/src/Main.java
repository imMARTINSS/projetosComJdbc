//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.Random;

public class Main {
    public static void main(String[] args) {
//        Random random = new Random();
        List<Integer> rep = new ArrayList<>(Arrays.asList(10, 20, 30, 50));

        Integer numeroIgual = 30;


        if(rep.contains(numeroIgual)){
            System.out.println("igual");
            System.out.println("Numero igual é:" + rep.get(rep.indexOf(numeroIgual)));
            rep.remove(numeroIgual);
        }



        for(Integer l : rep){
            System.out.println(l);
        }





    }
}
