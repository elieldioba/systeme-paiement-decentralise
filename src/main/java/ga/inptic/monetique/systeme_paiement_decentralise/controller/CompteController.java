package ga.inptic.monetique.systeme_paiement_decentralise.controller;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Compte;
import ga.inptic.monetique.systeme_paiement_decentralise.service.CompteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    // POST /api/comptes/creer/{utilisateurId}
    @PostMapping("/creer/{utilisateurId}")
    public ResponseEntity<?> creerCompte(@PathVariable Long utilisateurId,
            @RequestBody Compte compte) {
        try {
            Compte nouveau = compteService.creerCompte(utilisateurId, compte);
            return ResponseEntity.ok(nouveau);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/comptes/utilisateur/{utilisateurId}
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<Compte>> comptesDUnUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(compteService.comptesDUnUtilisateur(utilisateurId));
    }

    // GET /api/comptes/{numeroCompte}
    @GetMapping("/{numeroCompte}")
    public ResponseEntity<?> trouverParNumero(@PathVariable String numeroCompte) {
        return compteService.trouverParNumero(numeroCompte)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/comptes/crediter/{compteId}?montant=0.5
    @PutMapping("/crediter/{compteId}")
    public ResponseEntity<?> crediter(@PathVariable Long compteId,
            @RequestParam BigDecimal montant) {
        try {
            Compte compte = compteService.crediter(compteId, montant);
            return ResponseEntity.ok(compte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/comptes/debiter/{compteId}?montant=0.5
    @PutMapping("/debiter/{compteId}")
    public ResponseEntity<?> debiter(@PathVariable Long compteId,
            @RequestParam BigDecimal montant) {
        try {
            Compte compte = compteService.debiter(compteId, montant);
            return ResponseEntity.ok(compte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}