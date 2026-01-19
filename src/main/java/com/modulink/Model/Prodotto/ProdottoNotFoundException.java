package com.modulink.Model.Prodotto;

public class ProdottoNotFoundException extends Exception {
    public ProdottoNotFoundException() {
        super("Il prodotto indicato non è stato trovato");
    }
}
