package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.auxiliar.ErrorHandler;
import br.com.hireit.projetohireIt.repository.TecnologiaOfertaRepository;
import br.com.hireit.projetohireIt.repository.TecnologiaRepository;
import br.com.hireit.projetohireIt.tables.TecnologiaOfertaTable;
import br.com.hireit.projetohireIt.tables.TecnologiasTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tecnologias")
public class TecnologiaController {

    private ErrorHandler error = new ErrorHandler();

    @Autowired
    private TecnologiaRepository tecnologiaRepository;

    @Autowired
    private TecnologiaOfertaRepository tecnologiaOfertaRepository;

    @GetMapping
    public ResponseEntity getTecnologias(){
        List<TecnologiasTable> listaTecnologias = tecnologiaRepository.findAll();

        if(listaTecnologias.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaTecnologias);
        }
    }

    @GetMapping("/oferta/{idOferta}")
    public ResponseEntity getTecnologiasOferta(@PathVariable int idOferta){
        List<TecnologiaOfertaTable> listaTecnologias = tecnologiaOfertaRepository.findByFkoferta(idOferta);

        if(listaTecnologias.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaTecnologias);
        }
    }

    @PostMapping("/oferta")
    public ResponseEntity postTecnologiasOferta(
            @Valid @RequestBody TecnologiaOfertaTable tecnologia,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(error.getErrors(bindingResult));
        }

        if(tecnologiaRepository.existsById(tecnologia.getOfertas().getIdOferta())){
            tecnologiaOfertaRepository.save(tecnologia);
            return ResponseEntity.status(201).build();
        }else{
            return ResponseEntity.status(404).body("Oferta não existe!");
        }

    }
}
