package com.modulink.Model.Prodotto;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdottoService {
    private final ProdottoRepository prodottoRepository;

    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    @Transactional
    public void save(ProdottoEntity prodotto) {
        ProdottoEntity pMax=prodottoRepository.findMaxByAzienda(prodotto.getAzienda());
        if(pMax==null) prodotto.setId_prodotto(0);
        else prodotto.setId_prodotto(pMax.getId_prodotto()+1);
        prodottoRepository.save(prodotto);
    }

    @Transactional
    public void delete(int idProdotto, AziendaEntity azienda) throws ProdottoNotFoundException {
        Optional<ProdottoEntity> prodotto = prodottoRepository.findById(new ProdottoID(idProdotto, azienda.getId_azienda()));
        if(prodotto.isEmpty()) throw new ProdottoNotFoundException();
        prodottoRepository.delete(prodotto.get());
    }

    @Transactional
    public void updateProdotto(ProdottoEntity prodotto) {
        prodottoRepository.save(prodotto);
    }

    public List<ProdottoEntity> findAllByAzienda(AziendaEntity azienda) {
        return prodottoRepository.findAllByAzienda(azienda);
    }

    public List<String> findAllCategoriesByAzienda(AziendaEntity azienda) {
        return prodottoRepository.findAllCategoriesByAzienda(azienda);
    }

    public ProdottoEntity findById(ProdottoID id) throws ProdottoNotFoundException {
        Optional<ProdottoEntity> prodotto = prodottoRepository.findById(id);
        if(prodotto.isEmpty()) throw new ProdottoNotFoundException();
        return prodotto.get();
    }
}
