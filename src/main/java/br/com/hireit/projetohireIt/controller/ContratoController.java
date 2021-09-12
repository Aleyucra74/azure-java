package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.auxiliar.ErrorHandler;
import br.com.hireit.projetohireIt.repository.*;
import br.com.hireit.projetohireIt.tables.ContratosTable;
import br.com.hireit.projetohireIt.tables.PropostasTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private ErrorHandler error = new ErrorHandler();

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private DemandaRepository demandaRepository;

    @PostMapping
    public ResponseEntity postContrato(@Valid @RequestBody ContratosTable contrato, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(error.getErrors(bindingResult));
        }

        int fkDemanda = contrato.getDemandas().getIdDemanda();
        int fkOferta = contrato.getOfertas().getIdOferta();

        if(demandaRepository.existsById(fkDemanda) && ofertaRepository.existsById(fkOferta)){
            if(contratoRepository.findContratoByDemandaAndOferta(fkDemanda, fkOferta).isEmpty()){
                contrato.setDataInicio(java.time.LocalDateTime.now());
                contratoRepository.save(contrato);
            }else{
                return ResponseEntity.status(400).body("Esse contrato ja foi criado");
            }
        }else{
            return ResponseEntity.status(404).body("Demanda e oferta devem existir para criar um contrato");
        }

        List<PropostasTable> proposta = propostaRepository.findProposta(
                fkDemanda
        );

        propostaRepository.deleteAll(proposta);

        return ResponseEntity.status(201).build();
    }
}
