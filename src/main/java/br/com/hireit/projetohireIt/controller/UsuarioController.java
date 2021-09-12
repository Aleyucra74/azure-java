package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.auxiliar.ErrorHandler;
import br.com.hireit.projetohireIt.entity.Empresa;
import br.com.hireit.projetohireIt.entity.UsuarioLogin;
import br.com.hireit.projetohireIt.repository.UsuarioRepository;
import br.com.hireit.projetohireIt.tables.UsuariosTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    public List<UsuariosTable> usuariosLogados = new ArrayList<>();
    private Empresa empresa = new Empresa();
    private ErrorHandler error;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity postUsuario(@Valid @RequestBody UsuariosTable usuario, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(error.getErrors(bindingResult));
        }

        usuario.setClassificacao(new BigDecimal(0.0));
        usuarioRepository.save(usuario);

        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
    }

    @GetMapping
    public ResponseEntity getUsuarios(){
        List<UsuariosTable> listaUsuarios = usuarioRepository.findAll();

        if(listaUsuarios.isEmpty()){
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(200).body(listaUsuarios);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUsuario(@PathVariable int id){
        if(usuarioRepository.existsById(id)){
            return ResponseEntity.status(200).body(usuarioRepository.findById(id));
        }else{
            return ResponseEntity.status(204).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity putUsuario(
            @PathVariable int id,
            @Valid @RequestBody UsuariosTable usuario,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(error.getErrors(bindingResult));
        }

        Optional<UsuariosTable> usuariosTable = usuarioRepository.findById(id);

        if(usuariosTable.isPresent()){
            return ResponseEntity.status(204).body(usuariosTable
                    .map(record -> {
                        record.setNome(usuario.getNome());
                        record.setEmail(usuario.getEmail());
                        record.setDescricao(usuario.getDescricao());
                        record.setClassificacao(usuario.getClassificacao());
                        record.setTelefone(usuario.getTelefone());
                        record.setLocalizacao(usuario.getLocalizacao());
                        UsuariosTable updated = usuarioRepository.save(record);
                        return "Dados do usuário alterados com sucesso!";
                    }));
        }else{
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUsuario(@PathVariable int id){
        Optional<UsuariosTable> usuariosTable = usuarioRepository.findById(id);

        if(usuariosTable.isPresent()){
            usuarioRepository.deleteById(id);
            return ResponseEntity.status(204).build();
        }else{
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UsuarioLogin usuario){
        UsuariosTable usuarios = usuarioRepository.findByEmailAndSenha(usuario.getEmail(),usuario.getSenha());
        String respostaLogin = empresa.logar(usuarios, usuariosLogados);
        if(respostaLogin.equals("Usuário logado com sucesso!")){
            return ResponseEntity.status(200).body("Usuário logado com sucesso");
        }else{
            return ResponseEntity.status(400).body("Usuário ou Senha incorretos");
        }
    }

    @GetMapping("/email")
    public ResponseEntity getDadosUsuarios(@RequestParam String email){
        UsuariosTable usuariosTable = usuarioRepository.findUsuarioByEmail(email);

        if(usuariosTable.getIdUsuario() != 0){
            return ResponseEntity.status(200).body(usuariosTable);
        }else{
            return ResponseEntity.status(204).build();
        }

    }

}
