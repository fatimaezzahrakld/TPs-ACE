package ma.projet.soapclient.beans

import java.util.Date

/**
 * Classe de données représentant un compte bancaire
 * 
 * @property id Identifiant unique du compte
 * @property solde Solde actuel du compte
 * @property dateCreation Date de création du compte
 * @property type Type du compte (COURANT ou EPARGNE)
 */
data class Compte(
    val id: Long?,              // Identifiant unique
    val solde: Double,          // Solde du compte
    val dateCreation: Date,     // Date de création
    val type: TypeCompte        // Type de compte (COURANT ou EPARGNE)
)
