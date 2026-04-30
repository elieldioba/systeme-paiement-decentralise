package ga.inptic.monetique.systeme_paiement_decentralise.repository;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Compte;
import ga.inptic.monetique.systeme_paiement_decentralise.model.enums.TypeCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);

    Optional<Compte> findByAdresseWallet(String adresseWallet);

    List<Compte> findByProprietaireId(Long utilisateurId);

    List<Compte> findByTypeCrypto(TypeCrypto typeCrypto);
}