package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.auxiliar.ErrorHandler;
import br.com.hireit.projetohireIt.auxiliar.FilaObj;
import br.com.hireit.projetohireIt.entity.Filtro;
import br.com.hireit.projetohireIt.entity.PostOferta;
import br.com.hireit.projetohireIt.repository.*;
import br.com.hireit.projetohireIt.service.OfertaService;
import br.com.hireit.projetohireIt.tables.BuscasTable;
import br.com.hireit.projetohireIt.tables.OfertasTable;
import br.com.hireit.projetohireIt.tables.TecnologiaOfertaTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ofertas")
public class OfertaController {

    private ErrorHandler error;
    FilaObj<PostOferta> filaOfertas = new FilaObj<>(300);
    OfertaService ofertaService = new OfertaService();

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private BuscaRepository buscaRepository;

    @Autowired
    private TecnologiaOfertaRepository tecnologiaOfertaRepository;

    @Scheduled(fixedDelay = 1000L * 30)
    private void lerFila(){
        System.out.println("Lendo fila...");
        if(!filaOfertas.isEmpty()){
            salvarTecnologias(filaOfertas.poll());
        }
    }

    @GetMapping
    public ResponseEntity getOfertas(){
        List<OfertasTable> listaDemandas = ofertaRepository.findAll();
        if(listaDemandas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaDemandas);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getOferta(@PathVariable int id){
        if(ofertaRepository.existsById(id)){
            return ResponseEntity.status(200).body(ofertaRepository.findById(id));
        }else{
            return ResponseEntity.status(204).build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity getOfertasUsuario(@PathVariable int idUsuario){
        List<OfertasTable> listaDemandas = ofertaRepository.findAllByUsuario(idUsuario);
        if(listaDemandas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaDemandas);
        }
    }

    @GetMapping("/filtro")
    public ResponseEntity getDemandaFiltrado(@RequestBody Filtro filtro){
        BuscasTable busca = buscaRepository.findByTecnologiaAndTipo(filtro.getTecnologia(), "Oferta");
        if (busca != null) {
            busca.setQuantidade(busca.getQuantidade()+1);
            buscaRepository.save(busca);
        }

        Filtro filtrado = ofertaService.filtrarDemanda(filtro);

        LocalDateTime data = java.time.LocalDateTime.now();
        String hoje = data.getMonthValue() + "/" + (data.getDayOfMonth()+1) +"/"+ data.getYear();

        List<OfertasTable> listaOfertas = ofertaRepository.findWhere(
                filtrado.getUF(),
                filtrado.getData(),
                hoje,
                filtrado.getUsuario(),
                filtrado.getTecnologia(),
                filtrado.getExperiencia()
        );

        if(listaOfertas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaOfertas);
        }

    }

    @PostMapping()
    public ResponseEntity postOfertas(@Valid @RequestBody PostOferta oferta, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(error.getErrors(bindingResult));
        }

        oferta.getOferta().setCreatedAt(java.time.LocalDateTime.now());
        OfertasTable ofertasTable = ofertaRepository.save(oferta.getOferta());

        filaOfertas.insert(new PostOferta(
                ofertasTable,
                oferta.getListaTecnologias()
        ));

        return ResponseEntity.status(202).body(oferta);
    }

    private void salvarTecnologias(PostOferta postOferta){
        for(TecnologiaOfertaTable tecnologia: postOferta.getListaTecnologias()){
            tecnologia.setOfertas(postOferta.getOferta());
            tecnologiaOfertaRepository.save(tecnologia);
        }
    }
}
