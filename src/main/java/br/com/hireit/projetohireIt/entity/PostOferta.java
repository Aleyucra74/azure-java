package br.com.hireit.projetohireIt.entity;

import br.com.hireit.projetohireIt.tables.OfertasTable;
import br.com.hireit.projetohireIt.tables.TecnologiaOfertaTable;

import java.util.List;

public class PostOferta {

    private OfertasTable oferta;

    private List<TecnologiaOfertaTable> listaTecnologias;

    public PostOferta(OfertasTable oferta, List<TecnologiaOfertaTable> listaTecnologias) {
        this.oferta = oferta;
        this.listaTecnologias = listaTecnologias;
    }

    public PostOferta() {
    }

    public OfertasTable getOferta() {
        return oferta;
    }

    public void setOferta(OfertasTable oferta) {
        this.oferta = oferta;
    }

    public List<TecnologiaOfertaTable> getListaTecnologias() {
        return listaTecnologias;
    }

    public void setListaTecnologias(List<TecnologiaOfertaTable> listaTecnologias) {
        this.listaTecnologias = listaTecnologias;
    }
}
