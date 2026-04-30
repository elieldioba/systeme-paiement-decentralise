package ga.inptic.monetique.systeme_paiement_decentralise.model;

import ga.inptic.monetique.systeme_paiement_decentralise.model.enums.StatutTransaction;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal montant;

    private String hashBlockchain;

    @Column(nullable = false)
    private LocalDateTime dateTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTransaction statut;

    @ManyToOne
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Compte expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Compte destinataire;

    public Transaction() {
        this.dateTransaction = LocalDateTime.now();
        this.statut = StatutTransaction.PENDING;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getHashBlockchain() {
        return hashBlockchain;
    }

    public void setHashBlockchain(String hashBlockchain) {
        this.hashBlockchain = hashBlockchain;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public StatutTransaction getStatut() {
        return statut;
    }

    public void setStatut(StatutTransaction statut) {
        this.statut = statut;
    }

    public Compte getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(Compte expediteur) {
        this.expediteur = expediteur;
    }

    public Compte getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(Compte destinataire) {
        this.destinataire = destinataire;
    }
}