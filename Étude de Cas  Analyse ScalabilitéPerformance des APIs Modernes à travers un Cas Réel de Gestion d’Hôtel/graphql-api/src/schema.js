const { gql } = require('graphql-tag');

const typeDefs = gql`
    # ==================== ENUMS ====================
    
    enum StatutReservation {
        EN_ATTENTE
        CONFIRMEE
        ANNULEE
        TERMINEE
    }

    enum TypeChambre {
        SIMPLE
        DOUBLE
        SUITE
        DELUXE
        FAMILIALE
    }

    # ==================== TYPES ====================

    type Client {
        id: ID!
        nom: String!
        prenom: String!
        email: String!
        telephone: String!
        reservations: [Reservation!]
        createdAt: String
        updatedAt: String
    }

    type Chambre {
        id: ID!
        numero: String!
        type: TypeChambre!
        prix: Float!
        disponible: Boolean!
        description: String
        capaciteMax: Int
        equipements: [String!]
        reservations: [Reservation!]
        createdAt: String
        updatedAt: String
    }

    type Reservation {
        id: ID!
        client: Client!
        chambre: Chambre!
        dateDebut: String!
        dateFin: String!
        statut: StatutReservation!
        preferences: String
        nombrePersonnes: Int
        prixTotal: Float
        commentaires: String
        createdAt: String
        updatedAt: String
    }

    # ==================== INPUTS ====================

    input ClientInput {
        nom: String!
        prenom: String!
        email: String!
        telephone: String!
    }

    input ClientUpdateInput {
        nom: String
        prenom: String
        email: String
        telephone: String
    }

    input ChambreInput {
        numero: String!
        type: TypeChambre!
        prix: Float!
        disponible: Boolean
        description: String
        capaciteMax: Int
        equipements: [String!]
    }

    input ChambreUpdateInput {
        numero: String
        type: TypeChambre
        prix: Float
        disponible: Boolean
        description: String
        capaciteMax: Int
        equipements: [String!]
    }

    input ReservationInput {
        clientId: ID!
        chambreId: ID!
        dateDebut: String!
        dateFin: String!
        preferences: String
        nombrePersonnes: Int
        commentaires: String
    }

    input ReservationUpdateInput {
        clientId: ID
        chambreId: ID
        dateDebut: String
        dateFin: String
        preferences: String
        nombrePersonnes: Int
        commentaires: String
    }

    # ==================== QUERIES ====================

    type Query {
        # Client queries
        clients: [Client!]!
        client(id: ID!): Client
        clientByEmail(email: String!): Client
        searchClients(nom: String!): [Client!]!

        # Chambre queries
        chambres: [Chambre!]!
        chambre(id: ID!): Chambre
        chambreByNumero(numero: String!): Chambre
        chambresDisponibles: [Chambre!]!
        chambresByType(type: TypeChambre!): [Chambre!]!
        chambresDisponiblesPourDates(dateDebut: String!, dateFin: String!): [Chambre!]!

        # Reservation queries
        reservations: [Reservation!]!
        reservation(id: ID!): Reservation
        reservationsByClient(clientId: ID!): [Reservation!]!
        reservationsByChambre(chambreId: ID!): [Reservation!]!
        reservationsByStatus(statut: StatutReservation!): [Reservation!]!
        reservationsByDateRange(startDate: String!, endDate: String!): [Reservation!]!
        reservationsEnCours: [Reservation!]!
    }

    # ==================== MUTATIONS ====================

    type Mutation {
        # Client mutations
        createClient(input: ClientInput!): Client!
        updateClient(id: ID!, input: ClientUpdateInput!): Client!
        deleteClient(id: ID!): Boolean!

        # Chambre mutations
        createChambre(input: ChambreInput!): Chambre!
        updateChambre(id: ID!, input: ChambreUpdateInput!): Chambre!
        deleteChambre(id: ID!): Boolean!
        updateChambreDisponibilite(id: ID!, disponible: Boolean!): Chambre!

        # Reservation mutations
        createReservation(input: ReservationInput!): Reservation!
        updateReservation(id: ID!, input: ReservationUpdateInput!): Reservation!
        deleteReservation(id: ID!): Boolean!
        updateReservationStatus(id: ID!, statut: StatutReservation!): Reservation!
    }
`;

module.exports = { typeDefs };
