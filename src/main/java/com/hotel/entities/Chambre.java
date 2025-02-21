package com.hotel.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le numéro de la chambre ne peut pas être nul")
    @Min(value = 1, message = "Le numéro de la chambre doit être supérieur à 0")
    private int numero;

    @NotNull(message = "Le nombre de places ne peut pas être nul")
    @Min(value = 1, message = "Le nombre de places doit être supérieur à 0")
    private int nbPlaces;

    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.0", message = "Le prix doit être supérieur ou égal à 0")
    private double prix;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La catégorie ne peut pas être nulle")
    private Categorie categorie;

    // Getters et Setters
    public enum Categorie {
        BASIQUE, STANDARD, VIP
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
