/**
 * Interface pour un compte
 * @typedef {Object} Compte
 * @property {string} id
 * @property {number} solde
 * @property {string} dateCreation
 * @property {string} type
 */

/**
 * Interface pour une transaction
 * @typedef {Object} Transaction
 * @property {string} id
 * @property {string} type
 * @property {number} montant
 * @property {string} date
 * @property {Compte} compte
 */

/**
 * Interface pour les statistiques de solde
 * @typedef {Object} SoldeStats
 * @property {number} count
 * @property {number} sum
 * @property {number} average
 */

/**
 * Interface pour les statistiques de transaction
 * @typedef {Object} TransactionStats
 * @property {number} count
 * @property {number} sumDepots
 * @property {number} sumRetraits
 */

/**
 * Interface pour les demandes de création de compte
 * @typedef {Object} CompteRequest
 * @property {number} solde
 * @property {string} type
 */

/**
 * Interface pour les demandes de création de transaction
 * @typedef {Object} TransactionRequest
 * @property {string} type
 * @property {number} montant
 * @property {string} compteId
 */

// Export vide pour permettre l'importation du fichier
export {};
