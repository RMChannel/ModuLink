package com.modulink.Model.Prodotto;

import com.modulink.Model.Azienda.AziendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdottoRepository extends JpaRepository<ProdottoEntity, ProdottoID> {
    @Query("SELECT p FROM ProdottoEntity p WHERE p.azienda = :azienda AND p.id_prodotto = (SELECT MAX(p.id_prodotto) FROM ProdottoEntity p2 WHERE p2.azienda = :azienda)")
    ProdottoEntity findMaxByAzienda(@Param("azienda") AziendaEntity azienda);

    List<ProdottoEntity> findAllByAzienda(AziendaEntity azienda);

    @Query("SELECT DISTINCT p.categoria FROM ProdottoEntity p WHERE p.azienda = :azienda")
    List<String> findAllCategoriesByAzienda(AziendaEntity azienda);

    void deleteAllByAzienda(AziendaEntity azienda);
}
