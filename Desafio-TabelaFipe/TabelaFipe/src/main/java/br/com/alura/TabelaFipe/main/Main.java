package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.services.ConsumoAPI;

import java.util.Scanner;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private final String URL_BASE="https://parallelum.com.br/fipe/api/v1/";
    private ConsumoAPI consumo= new ConsumoAPI();
    public void exibeMenu(){
        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar:
                """;
        System.out.println(menu);
        var opcao=sc.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("car")){
            endereco= URL_BASE+"carros/marcas";
        }else if(opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        }else{
            endereco = URL_BASE + "caminhoes/marcas";
        }
        var json= consumo.obterDados(endereco);
        System.out.println(json);
    }
}
