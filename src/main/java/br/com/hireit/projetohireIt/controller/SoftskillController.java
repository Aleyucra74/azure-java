package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.repository.SoftskillRepository;
import br.com.hireit.projetohireIt.tables.SoftSkillsTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/softskills")
public class SoftskillController {

    @Autowired
    private SoftskillRepository softskillRepository;

    @GetMapping
    public ResponseEntity getSoftskills(){
        List<SoftSkillsTable> listaSoftskills = softskillRepository.findAll();

        if(listaSoftskills.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaSoftskills);
        }

    }
}
