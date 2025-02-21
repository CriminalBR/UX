import java.util.Scanner;

public class Main {

    public static final int NOME = 0;
    private static final int CPF = 1;
    public static final int ANO = 2;

    public static void main(String[] args) {

        String[] pessoa1 = criarPessoa(  "Pessoa 1",  "09666897989", "2003");
        String[] pessoa2 = criarPessoa(  "Pessoa 2",  "15418641521", "2010");


        int anoPessoa1 = Integer.parseInt(pessoa1[ANO]);
        int anoPessoa2 = Integer.parseInt(pessoa2[ANO]);

        if (anoPessoa1 < anoPessoa2){
            System.out.println(pessoa1[NOME] + " é mais velho que " + pessoa2[NOME]);
        } else {
            System.out.println(pessoa2[NOME] + " é mais velho que " + pessoa1[ANO]);
        }

    }

    private static String[] criarPessoa(String nome, String cpf, String ano) {
        String[] pessoa = new String[3];

        pessoa[NOME] = nome ;
        pessoa[CPF] = cpf;
        pessoa[ANO] = ano;
        return pessoa;
    }


}
