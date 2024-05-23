package br.com.alura.Screenmatch.principal;

import br.com.alura.Screenmatch.model.DadosEpisodios;
import br.com.alura.Screenmatch.model.DadosSerie;
import br.com.alura.Screenmatch.model.DadosTemporada;
import br.com.alura.Screenmatch.model.Episodio;
import br.com.alura.Screenmatch.service.ConsumoAPI;
import br.com.alura.Screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String ENDERECO="https://www.omdbapi.com/?t=";
    private final String API_KEY="&apikey=2c14cefc";
    private ConsumoAPI consumoAPI= new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private Scanner sc = new Scanner(System.in);
    public void exibeMenu(){
        System.out.println("Digite o nome da série: ");
        var nomeSerie = sc.nextLine();
        nomeSerie=nomeSerie.replace(" ","+");
        var json = consumoAPI.obterDados(ENDERECO+nomeSerie+API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        for (int i=1; i<=dados.totalTemporadas();i++){
        	json = consumoAPI.obterDados(ENDERECO+nomeSerie+"&season="+i+API_KEY);
        	DadosTemporada dadosTemp = conversor.obterDados(json, DadosTemporada.class);
        	listaTemporadas.add(dadosTemp);
        }

        listaTemporadas.forEach(System.out::println);

//        for(int i=0;i< dados.totalTemporadas();i++){
//            List<DadosEpisodios> listaEp = listaTemp.get(i).episodios();
//            for (int j=0;j<listaEp.size();j++){
//                System.out.println(listaEp.get(j));
//            }
//        }

        listaTemporadas.forEach(temp -> temp.episodios().forEach(ep -> System.out.println(ep.titulo())));
        List<DadosEpisodios> listaEpisodios = listaTemporadas.stream()
                .flatMap( temp -> temp.episodios().stream())
                .collect(Collectors.toList());


        System.out.println("\n TOP 5 EPISÓDIOS");
        listaEpisodios.stream()
                .filter(ep -> !ep.avaliacao().equals("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(temp -> temp.episodios().stream()
                        .map(d -> new Episodio(temp.numeroTemporadas(),d))
                ).collect(Collectors.toList());


        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate databusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(ep -> ep.getDataLancamento()!= null && ep.getDataLancamento().isAfter(databusca))
                .forEach(ep -> System.out.println(
                        "Temporada: "+ ep.getTemporada()+
                                " Episódio: "+ ep.getNumero()+
                                " Título: "+ ep.getTitulo()+
                                " Avaliação: "+ ep.getAvaliacao()+
                                " Data de Lançamento: "+ ep.getDataLancamento().format(fmt)
                ));


    }
}
