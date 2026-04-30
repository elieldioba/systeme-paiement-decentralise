package ga.inptic.monetique.systeme_paiement_decentralise.repository;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Transaction;
import ga.inptic.monetique.systeme_paiement_decentralise.model.enums.StatutTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReference(String reference);

    Optional<Transaction> findByHashBlockchain(String hashBlockchain);

    List<Transaction> findByExpediteurId(Long compteId);

    List<Transaction> findByDestinataireId(Long compteId);

    List<Transaction> findByStatut(StatutTransaction statut);
}