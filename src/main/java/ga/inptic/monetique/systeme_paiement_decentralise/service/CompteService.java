package ga.inptic.monetique.systeme_paiement_decentralise.service;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Compte;
import ga.inptic.monetique.systeme_paiement_decentralise.model.Utilisateur;
import ga.inptic.monetique.systeme_paiement_decentralise.repository.CompteRepository;
import ga.inptic.monetique.systeme_paiement_decentralise.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import java.security.SecureRandom;

@Service
public class CompteService {

    private final CompteRepository compteRepository;
    private final UtilisateurRepository utilisateurRepository;

    public CompteService(CompteRepository compteRepository,
            UtilisateurRepository utilisateurRepository) {
        this.compteRepository = compteRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Compte creerCompte(Long utilisateurId, Compte compte) {
        Utilisateur proprietaire = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        // Génération automatique du numéro de compte
        compte.setNumeroCompte("CPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Génération automatique du wallet Ethereum via Web3j
        try {
            SecureRandom secureRandom = new SecureRandom();
            ECKeyPair keyPair = Keys.createEcKeyPair(secureRandom);
            String adresseWallet = "0x" + Keys.getAddress(keyPair);
            compte.setAdresseWallet(adresseWallet);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du wallet : " + e.getMessage());
        }

        compte.setProprietaire(proprietaire);
        return compteRepository.save(compte);
    }

    public Optional<Compte> trouverParNumero(String numeroCompte) {
        return compteRepository.findByNumeroCompte(numeroCompte);
    }

    public List<Compte> comptesDUnUtilisateur(Long utilisateurId) {
        return compteRepository.findByProprietaireId(utilisateurId);
    }

    public Compte crediter(Long compteId, BigDecimal montant) {
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable."));

        compte.setSolde(compte.getSolde().add(montant));
        return compteRepository.save(compte);
    }

    public Compte debiter(Long compteId, BigDecimal montant) {
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable."));

        if (compte.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde insuffisant.");
        }

        compte.setSolde(compte.getSolde().subtract(montant));
        return compteRepository.save(compte);
    }
}