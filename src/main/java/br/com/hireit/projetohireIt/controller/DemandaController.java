package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.auxiliar.ErrorHandler;
import br.com.hireit.projetohireIt.entity.Filtro;
import br.com.hireit.projetohireIt.entity.PostDemanda;
import br.com.hireit.projetohireIt.repository.*;
import br.com.hireit.projetohireIt.service.DemandaService;
import br.com.hireit.projetohireIt.tables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/demandas")
public class DemandaController {

    private DemandaService demandaService = new DemandaService();
    private ErrorHandler error = new ErrorHandler();

    @Autowired
    private DemandaRepository demandaRepository;

    @Autowired
    private BuscaRepository buscaRepository;

    @Autowired
    private TecnologiaDemandaRepository tecnologiaDemandaRepository;

    @Autowired
    private SoftskillDemandaRepository softskillDemandaRepository;

    @GetMapping
    public ResponseEntity getDemandas(){
        List<DemandasTable> listaDemandas = demandaRepository.findAll();
        if(listaDemandas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaDemandas);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getDemanda(@PathVariable int id){
        if(demandaRepository.existsById(id)){
            return ResponseEntity.status(200).body(demandaRepository.findById(id));
        }else{
            return ResponseEntity.status(204).build();
        }

    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity getDemandasUsuario(@PathVariable int idUsuario){
        List<DemandasTable> listaDemandas = demandaRepository.findAllByUsuario(idUsuario);
        if(listaDemandas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaDemandas);
        }

    }

    @GetMapping("/filtro")
    public ResponseEntity getDemandaFiltrado(@RequestBody Filtro filtro){
        BuscasTable busca = buscaRepository.findByTecnologiaAndTipo(filtro.getTecnologia(), "Demanda");
        if (busca != null) {
            busca.setQuantidade(busca.getQuantidade()+1);
            buscaRepository.save(busca);
        }

        Filtro filtrado = demandaService.filtrarDemanda(filtro);

        LocalDateTime data = java.time.LocalDateTime.now();
        String hoje = data.getMonthValue() + "/" + (data.getDayOfMonth()+1) +"/"+ data.getYear();

        List<DemandasTable> listaDemandas = demandaRepository.findWhere(
                filtrado.getUF(),
                filtrado.getTitulo(),
                filtrado.getData(),
                hoje,
                filtrado.getSalarioMin(),
                filtrado.getSalarioMax(),
                filtrado.getUsuario(),
                filtrado.getTecnologia(),
                filtrado.getExperiencia()
        );

        if(listaDemandas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaDemandas);
        }

    }

    @GetMapping("/filtro-simples")
    public ResponseEntity getDemandaFiltradoSimples(@RequestParam String titulo){

        List<DemandasTable> listaDemandas = demandaRepository.findWhereSimple(titulo);

        if(listaDemandas.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaDemandas);
        }
    }

    @PostMapping
    public ResponseEntity postDemandas(@Valid @RequestBody PostDemanda postDemanda, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(error.getErrors(bindingResult));
        }

        postDemanda.getDemandas().setCreatedAt(java.time.LocalDateTime.now());
        DemandasTable demanda = demandaRepository.save(postDemanda.getDemandas());

        for(TecnologiaDemandaTable tecnologia: postDemanda.getTecnologias()){
            tecnologia.setDemandas(demanda);
            tecnologiaDemandaRepository.save(tecnologia);
        }

        for(SoftSkillDemandaTable softskill: postDemanda.getSoftskills()){
            softskill.setDemanda(demanda);
            softskillDemandaRepository.save(softskill);
        }

        return ResponseEntity.status(201).body("Projeto criado com sucesso!");
    }

    @GetMapping("/softskill/{fkDemanda}")
    public ResponseEntity getSoftskill(@PathVariable int fkDemanda){
        List<SoftSkillDemandaTable> listaSoftskillsDemanda = softskillDemandaRepository.findByFkdemanda(fkDemanda);
        if(listaSoftskillsDemanda.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaSoftskillsDemanda);
        }
    }

}
