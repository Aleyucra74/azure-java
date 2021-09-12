package br.com.hireit.projetohireIt.entity;

import br.com.hireit.projetohireIt.tables.OfertasTable;

public class MelhorOferta {

    private OfertasTable ofertas;

    private Double match;

    public MelhorOferta() {
    }

    public MelhorOferta(OfertasTable ofertas, Double match) {
        this.ofertas = ofertas;
        this.match = match;
    }

    public OfertasTable getOfertas() {
        return ofertas;
    }

    public void setOfertas(OfertasTable ofertas) {
        this.ofertas = ofertas;
    }

    public Double getMatch() {
        return match;
    }

    public void setMatch(Double match) {
        this.match = match;
    }
}
