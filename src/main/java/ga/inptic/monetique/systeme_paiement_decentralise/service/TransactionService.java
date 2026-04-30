package ga.inptic.monetique.systeme_paiement_decentralise.service;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Compte;
import ga.inptic.monetique.systeme_paiement_decentralise.model.Transaction;
import ga.inptic.monetique.systeme_paiement_decentralise.model.enums.StatutTransaction;
import ga.inptic.monetique.systeme_paiement_decentralise.repository.CompteRepository;
import ga.inptic.monetique.systeme_paiement_decentralise.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    public TransactionService(TransactionRepository transactionRepository,
            CompteRepository compteRepository) {
        this.transactionRepository = transactionRepository;
        this.compteRepository = compteRepository;
    }

    @Transactional
    public Transaction effectuerPaiement(Long expediteurId, Long destinataireId, BigDecimal montant) {

        Compte expediteur = compteRepository.findById(expediteurId)
                .orElseThrow(() -> new RuntimeException("Compte expéditeur introuvable."));

        Compte destinataire = compteRepository.findById(destinataireId)
                .orElseThrow(() -> new RuntimeException("Compte destinataire introuvable."));

        // Vérification que les deux comptes sont du même type de crypto
        if (!expediteur.getTypeCrypto().equals(destinataire.getTypeCrypto())) {
            throw new RuntimeException("Les comptes doivent être du même type de cryptomonnaie.");
        }

        // Vérification du solde
        if (expediteur.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde insuffisant pour effectuer ce paiement.");
        }

        // Création de la transaction
        Transaction transaction = new Transaction();
        String reference = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        transaction.setReference(reference);
        transaction.setMontant(montant);
        transaction.setExpediteur(expediteur);
        transaction.setDestinataire(destinataire);
        transaction.setStatut(StatutTransaction.PROCESSING);

        // Génération du hash blockchain (SHA-256 simulé)
        try {
            String donnees = reference
                    + expediteur.getAdresseWallet()
                    + destinataire.getAdresseWallet()
                    + montant.toPlainString()
                    + System.currentTimeMillis();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(donnees.getBytes(StandardCharsets.UTF_8));
            String hashHex = HexFormat.of().formatHex(hashBytes);
            transaction.setHashBlockchain("0x" + hashHex);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du hash blockchain : " + e.getMessage());
        }

        // Débit et crédit des comptes
        expediteur.setSolde(expediteur.getSolde().subtract(montant));
        destinataire.setSolde(destinataire.getSolde().add(montant));

        compteRepository.save(expediteur);
        compteRepository.save(destinataire);

        // Confirmation de la transaction
        transaction.setStatut(StatutTransaction.SUCCESS);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> historiqueParExpediteur(Long compteId) {
        return transactionRepository.findByExpediteurId(compteId);
    }

    public List<Transaction> historiqueParDestinataire(Long compteId) {
        return transactionRepository.findByDestinataireId(compteId);
    }

    public Optional<Transaction> trouverParReference(String reference) {
        return transactionRepository.findByReference(reference);
    }

    public List<Transaction> transactionsParStatut(StatutTransaction statut) {
        return transactionRepository.findByStatut(statut);
    }
}