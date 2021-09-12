package br.com.hireit.projetohireIt.controller;

import br.com.hireit.projetohireIt.entity.Filtro;
import br.com.hireit.projetohireIt.repository.BuscaRepository;
import br.com.hireit.projetohireIt.repository.DemandaRepository;
import br.com.hireit.projetohireIt.repository.SoftskillDemandaRepository;
import br.com.hireit.projetohireIt.repository.TecnologiaDemandaRepository;
import br.com.hireit.projetohireIt.service.DemandaService;
import br.com.hireit.projetohireIt.tables.BuscasTable;
import br.com.hireit.projetohireIt.tables.DemandasTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class DemandaControllerTest {

    @Autowired
    DemandaController demandaController = new DemandaController();

    @MockBean
    private DemandaService demandaService;

    @MockBean
    private DemandaRepository demandaRepository;

    @MockBean
    private BuscaRepository buscaRepository;

    @MockBean
    private TecnologiaDemandaRepository tecnologiaDemandaRepository;

    @MockBean
    private SoftskillDemandaRepository softskillDemandaRepository;

    @Test
    @DisplayName("GET /demandas - Should return 200 when list is not empty")
    void getDemandasSuccess(){
        List<DemandasTable> listDemanda = Arrays.asList(new DemandasTable(), new DemandasTable(), new DemandasTable());

        Mockito.when(demandaRepository.findAll()).thenReturn(listDemanda);

        ResponseEntity response = demandaController.getDemandas();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("GET /demandas - Should return 204 when list is empty")
    void getDemandasNoContent(){
        Mockito.when(demandaRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity response = demandaController.getDemandas();

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("GET /demandas/{id} - Should return 200 when register exists")
    void getDemandaSuccess(){
        int id = 1;

        Mockito.when(demandaRepository.existsById(id)).thenReturn(true);

        ResponseEntity response = demandaController.getDemanda(id);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("GET /demandas/{id} - Should return 204 when register do not exists")
    void getDemandaNoContent(){
        int id = 1;

        Mockito.when(demandaRepository.existsById(id)).thenReturn(false);

        ResponseEntity response = demandaController.getDemanda(id);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("GET /demandas/usuario/{idUsuario} - Should return 200 when user have Demandas register")
    void getDemandasUsuarioSuccess(){
        int id = 1;
        List<DemandasTable> listDemanda = Arrays.asList(new DemandasTable(), new DemandasTable(), new DemandasTable());

        Mockito.when(demandaRepository.findAllByUsuario(id)).thenReturn(listDemanda);

        ResponseEntity response = demandaController.getDemandasUsuario(id);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("GET /demandas/usuario/{idUsuario} - Should return 204 when user have no Demandas register")
    void getDemandasUsuarioNoContent(){
        int id = 1;

        Mockito.when(demandaRepository.findAllByUsuario(id)).thenReturn(new ArrayList<>());

        ResponseEntity response = demandaController.getDemandasUsuario(id);

        assertEquals(204, response.getStatusCodeValue());
    }

    /*@Test
    @DisplayName("GET /demandas/filtro - Should return 200 when exists register using this filter")
    void getDemandaFiltradoSuccess(){
        Filtro filtro = new Filtro();
        Filtro filtrado = new Filtro();
        List<DemandasTable> listaDemanda = Arrays.asList(new DemandasTable(), new DemandasTable(), new DemandasTable());
        LocalDateTime data = java.time.LocalDateTime.now();
        String hoje = data.getMonthValue() + "/" + (data.getDayOfMonth()+1) +"/"+ data.getYear();

        Mockito.when(demandaService.filtrarDemanda(filtro)).thenReturn(filtrado);
        Mockito.when(buscaRepository.findByTecnologiaAndTipo("Java","Demanda")).thenReturn(new BuscasTable());
        Mockito.when(demandaRepository.findWhere(
                filtrado.getUF(),
                filtrado.getTitulo(),
                filtrado.getData(),
                hoje,
                filtrado.getSalarioMin(),
                filtrado.getSalarioMax(),
                filtrado.getUsuario(),
                filtrado.getTecnologia(),
                filtrado.getExperiencia()
        )).thenReturn(listaDemanda);

        ResponseEntity response = demandaController.getDemandaFiltrado(filtro);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("GET /demandas/filtro - Should return 204 when does not exists register using this filter")
    void getDemandaFiltradoNoContent(){
        Filtro filtro = new Filtro();
        LocalDateTime data = java.time.LocalDateTime.now();
        String hoje = data.getMonthValue() + "/" + (data.getDayOfMonth()+1) +"/"+ data.getYear();
        List<DemandasTable> listDemanda = Arrays.asList(new DemandasTable(), new DemandasTable(), new DemandasTable());

        Filtro filtrado = new Filtro();

        Mockito.when(demandaService.filtrarDemanda(filtro)).thenReturn(filtrado);
        Mockito.when(buscaRepository.findByTecnologiaAndTipo("Java","Demanda")).thenReturn(new BuscasTable());
        Mockito.when(demandaRepository.findWhere(
                filtrado.getUF(),
                filtrado.getTitulo(),
                filtrado.getData(),
                hoje,
                filtrado.getSalarioMin(),
                filtrado.getSalarioMax(),
                filtrado.getUsuario(),
                filtrado.getTecnologia(),
                filtrado.getExperiencia()
        )).thenReturn(listDemanda);

        ResponseEntity response = demandaController.getDemandaFiltrado(filtro);

        assertEquals(200, response.getStatusCodeValue());
    }*/


}
