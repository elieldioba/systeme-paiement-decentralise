package ga.inptic.monetique.systeme_paiement_decentralise.model;

import ga.inptic.monetique.systeme_paiement_decentralise.model.enums.TypeCrypto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "comptes")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCompte;

    @Column(nullable = false)
    private String adresseWallet;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal solde;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCrypto typeCrypto;

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur proprietaire;

    public Compte() {
        this.solde = BigDecimal.ZERO;
        this.dateCreation = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getAdresseWallet() {
        return adresseWallet;
    }

    public void setAdresseWallet(String adresseWallet) {
        this.adresseWallet = adresseWallet;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public TypeCrypto getTypeCrypto() {
        return typeCrypto;
    }

    public void setTypeCrypto(TypeCrypto typeCrypto) {
        this.typeCrypto = typeCrypto;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }
}