package com.modulink.Model.Prodotto;

import java.util.Objects;

public class ProdottoID {
    private int id_prodotto;
    private int azienda;

    public ProdottoID() {}

    public ProdottoID(int id_prodotto, int azienda) {
        this.id_prodotto = id_prodotto;
        this.azienda = azienda;
    }

    public int getId_prodotto() {
        return id_prodotto;
    }

    public void setId_prodotto(int id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public int getAzienda() {
        return azienda;
    }

    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProdottoID that = (ProdottoID) o;
        return id_prodotto == that.id_prodotto && azienda == that.azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_prodotto, azienda);
    }
}
