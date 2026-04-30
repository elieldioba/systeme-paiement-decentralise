package ga.inptic.monetique.systeme_paiement_decentralise.controller;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Transaction;
import ga.inptic.monetique.systeme_paiement_decentralise.model.enums.StatutTransaction;
import ga.inptic.monetique.systeme_paiement_decentralise.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /api/transactions/payer?expediteurId=1&destinataireId=2&montant=0.5
    @PostMapping("/payer")
    public ResponseEntity<?> effectuerPaiement(@RequestParam Long expediteurId,
            @RequestParam Long destinataireId,
            @RequestParam BigDecimal montant) {
        try {
            Transaction transaction = transactionService.effectuerPaiement(
                    expediteurId, destinataireId, montant);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/transactions/expediteur/{compteId}
    @GetMapping("/expediteur/{compteId}")
    public ResponseEntity<List<Transaction>> historiqueExpediteur(@PathVariable Long compteId) {
        return ResponseEntity.ok(transactionService.historiqueParExpediteur(compteId));
    }

    // GET /api/transactions/destinataire/{compteId}
    @GetMapping("/destinataire/{compteId}")
    public ResponseEntity<List<Transaction>> historiqueDestinataire(@PathVariable Long compteId) {
        return ResponseEntity.ok(transactionService.historiqueParDestinataire(compteId));
    }

    // GET /api/transactions/reference/{reference}
    @GetMapping("/reference/{reference}")
    public ResponseEntity<?> trouverParReference(@PathVariable String reference) {
        return transactionService.trouverParReference(reference)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/transactions/statut/{statut}
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Transaction>> parStatut(@PathVariable StatutTransaction statut) {
        return ResponseEntity.ok(transactionService.transactionsParStatut(statut));
    }
}