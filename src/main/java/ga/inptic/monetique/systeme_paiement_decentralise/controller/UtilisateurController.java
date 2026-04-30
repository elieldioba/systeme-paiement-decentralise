package ga.inptic.monetique.systeme_paiement_decentralise.controller;

import ga.inptic.monetique.systeme_paiement_decentralise.model.Utilisateur;
import ga.inptic.monetique.systeme_paiement_decentralise.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // POST /api/utilisateurs/inscrire
    @PostMapping("/inscrire")
    public ResponseEntity<?> inscrire(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur nouvel = utilisateurService.inscrire(utilisateur);
            return ResponseEntity.ok(nouvel);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // GET /api/utilisateurs
    @GetMapping
    public ResponseEntity<List<Utilisateur>> tousLesUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.tousLesUtilisateurs());
    }

    // GET /api/utilisateurs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> trouverParId(@PathVariable Long id) {
        return utilisateurService.trouverParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/utilisateurs/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès.");
    }
}