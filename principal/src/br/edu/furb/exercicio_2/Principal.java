package br.edu.furb.exercicio_2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Principal {

    private static final String _FOLDER = "assets/";
    private static final String _DATA_TYPE = ".txt";

    public static void main(String[] args) {
        var mapAutores = getMapAutores();
        var conjuntoAutores = getConjuntoAutores(mapAutores);
        String relacaoAutores = montarRelacoes(conjuntoAutores);

        System.out.println(relacaoAutores);
    }

    private static String montarRelacoes(ArrayList<String> conjuntoAutores) {
        StringBuilder relacoes = new StringBuilder();
        for (int i = 0; i < conjuntoAutores.size(); i++) {
            for (int j = i+1; j < conjuntoAutores.size(); j++) {
                relacoes.append(conjuntoAutores.get(i)).append(" - ").append(conjuntoAutores.get(j)).append("\n");
            }
        }
        return relacoes.toString();
    }

    private static HashMap<String, String> getMapAutores(){
        var mapAutores = new HashMap<String, String>();
        String content = getContentFile("Input_Data");
        Matcher matcher = Pattern.compile("Número identificador: ([\\d]*)?[\\n\\r ]*Referência: (.*)?").matcher(content);
        while (matcher.find()) {
            String identificador = matcher.group(1);
            String nomes = matcher.group(2);
            if (nomes.contains("; ")) {
                String[] lNomes = nomes.split("; ");
                for (String nome : lNomes) {
                    if(nome.endsWith(" ")) {
                        nome = nome.substring(0, nome.length()-1);
                    }
                    mapAutores.put(nome, identificador);
                }
            } else {
                mapAutores.put(nomes, identificador);
            }
        }
        return mapAutores;
    }

    private static ArrayList<String> getConjuntoAutores(HashMap<String, String> mapAutores){
        var conjuntoAutores = new ArrayList<String>();
        String content = getContentFile("Relations");

        Matcher matcher = Pattern.compile("Referência: (.*)?[ ]*?\\r\\n*Número identificador: ([\\d\\\"]*)?").matcher(content);
        while (matcher.find()) {
            String identificador = matcher.group(2);
            if (Objects.equals(identificador, "")) {
                String nome = matcher.group(1);
                mapAutores.computeIfPresent(nome , (key, value) -> {
                    conjuntoAutores.add(value);
                    return value;
                });
            } else {
                conjuntoAutores.add(identificador);
            }
        }

        return conjuntoAutores;
    }

    private static String getContentFile(String fileName) {
        File file = new File(_FOLDER + fileName + _DATA_TYPE);
        try (FileInputStream fis = new FileInputStream(file)) {
            return new String(fis.readAllBytes());
        } catch (IOException ioe) {
            System.out.println(ioe);
            return null;
        }
    }

}
