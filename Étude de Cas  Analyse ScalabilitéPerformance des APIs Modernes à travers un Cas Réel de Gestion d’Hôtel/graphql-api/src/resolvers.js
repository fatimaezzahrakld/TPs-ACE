const { Client, Chambre, Reservation, sequelize } = require('./database');
const { Op } = require('sequelize');

const resolvers = {
    // ==================== QUERIES ====================
    Query: {
        // Client queries
        clients: async () => {
            return await Client.findAll();
        },

        client: async (_, { id }) => {
            return await Client.findByPk(id);
        },

        clientByEmail: async (_, { email }) => {
            return await Client.findOne({ where: { email } });
        },

        searchClients: async (_, { nom }) => {
            return await Client.findAll({
                where: {
                    [Op.or]: [
                        { nom: { [Op.iLike]: `%${nom}%` } },
                        { prenom: { [Op.iLike]: `%${nom}%` } }
                    ]
                }
            });
        },

        // Chambre queries
        chambres: async () => {
            return await Chambre.findAll();
        },

        chambre: async (_, { id }) => {
            return await Chambre.findByPk(id);
        },

        chambreByNumero: async (_, { numero }) => {
            return await Chambre.findOne({ where: { numero } });
        },

        chambresDisponibles: async () => {
            return await Chambre.findAll({ where: { disponible: true } });
        },

        chambresByType: async (_, { type }) => {
            return await Chambre.findAll({ where: { type } });
        },

        chambresDisponiblesPourDates: async (_, { dateDebut, dateFin }) => {
            const reservedChambreIds = await Reservation.findAll({
                attributes: ['chambre_id'],
                where: {
                    statut: { [Op.ne]: 'ANNULEE' },
                    [Op.and]: [
                        { date_debut: { [Op.lte]: dateFin } },
                        { date_fin: { [Op.gte]: dateDebut } }
                    ]
                }
            });

            const ids = reservedChambreIds.map(r => r.chambre_id);

            return await Chambre.findAll({
                where: {
                    disponible: true,
                    id: { [Op.notIn]: ids.length > 0 ? ids : [0] }
                }
            });
        },

        // Reservation queries
        reservations: async () => {
            return await Reservation.findAll({
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        reservation: async (_, { id }) => {
            return await Reservation.findByPk(id, {
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        reservationsByClient: async (_, { clientId }) => {
            return await Reservation.findAll({
                where: { client_id: clientId },
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        reservationsByChambre: async (_, { chambreId }) => {
            return await Reservation.findAll({
                where: { chambre_id: chambreId },
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        reservationsByStatus: async (_, { statut }) => {
            return await Reservation.findAll({
                where: { statut },
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        reservationsByDateRange: async (_, { startDate, endDate }) => {
            return await Reservation.findAll({
                where: {
                    date_debut: { [Op.gte]: startDate },
                    date_fin: { [Op.lte]: endDate }
                },
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        reservationsEnCours: async () => {
            const today = new Date().toISOString().split('T')[0];
            return await Reservation.findAll({
                where: {
                    date_fin: { [Op.gte]: today },
                    statut: { [Op.ne]: 'ANNULEE' }
                },
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ],
                order: [['date_debut', 'ASC']]
            });
        }
    },

    // ==================== MUTATIONS ====================
    Mutation: {
        // Client mutations
        createClient: async (_, { input }) => {
            return await Client.create(input);
        },

        updateClient: async (_, { id, input }) => {
            const client = await Client.findByPk(id);
            if (!client) throw new Error('Client non trouvé');
            await client.update(input);
            return client;
        },

        deleteClient: async (_, { id }) => {
            const client = await Client.findByPk(id);
            if (!client) throw new Error('Client non trouvé');
            await client.destroy();
            return true;
        },

        // Chambre mutations
        createChambre: async (_, { input }) => {
            return await Chambre.create({
                ...input,
                capacite_max: input.capaciteMax
            });
        },

        updateChambre: async (_, { id, input }) => {
            const chambre = await Chambre.findByPk(id);
            if (!chambre) throw new Error('Chambre non trouvée');
            await chambre.update({
                ...input,
                capacite_max: input.capaciteMax
            });
            return chambre;
        },

        deleteChambre: async (_, { id }) => {
            const chambre = await Chambre.findByPk(id);
            if (!chambre) throw new Error('Chambre non trouvée');
            await chambre.destroy();
            return true;
        },

        updateChambreDisponibilite: async (_, { id, disponible }) => {
            const chambre = await Chambre.findByPk(id);
            if (!chambre) throw new Error('Chambre non trouvée');
            await chambre.update({ disponible });
            return chambre;
        },

        // Reservation mutations
        createReservation: async (_, { input }) => {
            // Validate dates
            if (new Date(input.dateDebut) >= new Date(input.dateFin)) {
                throw new Error('La date de début doit être avant la date de fin');
            }

            // Check room availability
            const overlapping = await Reservation.findOne({
                where: {
                    chambre_id: input.chambreId,
                    statut: { [Op.ne]: 'ANNULEE' },
                    [Op.and]: [
                        { date_debut: { [Op.lte]: input.dateFin } },
                        { date_fin: { [Op.gte]: input.dateDebut } }
                    ]
                }
            });

            if (overlapping) {
                throw new Error('La chambre n\'est pas disponible pour les dates sélectionnées');
            }

            // Get room price for total calculation
            const chambre = await Chambre.findByPk(input.chambreId);
            const nights = Math.ceil((new Date(input.dateFin) - new Date(input.dateDebut)) / (1000 * 60 * 60 * 24));
            const prixTotal = parseFloat(chambre.prix) * nights;

            const reservation = await Reservation.create({
                client_id: input.clientId,
                chambre_id: input.chambreId,
                date_debut: input.dateDebut,
                date_fin: input.dateFin,
                preferences: input.preferences,
                nombre_personnes: input.nombrePersonnes,
                commentaires: input.commentaires,
                prix_total: prixTotal,
                statut: 'EN_ATTENTE'
            });

            return await Reservation.findByPk(reservation.id, {
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        updateReservation: async (_, { id, input }) => {
            const reservation = await Reservation.findByPk(id);
            if (!reservation) throw new Error('Réservation non trouvée');

            await reservation.update({
                client_id: input.clientId || reservation.client_id,
                chambre_id: input.chambreId || reservation.chambre_id,
                date_debut: input.dateDebut || reservation.date_debut,
                date_fin: input.dateFin || reservation.date_fin,
                preferences: input.preferences !== undefined ? input.preferences : reservation.preferences,
                nombre_personnes: input.nombrePersonnes !== undefined ? input.nombrePersonnes : reservation.nombre_personnes,
                commentaires: input.commentaires !== undefined ? input.commentaires : reservation.commentaires
            });

            return await Reservation.findByPk(id, {
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        },

        deleteReservation: async (_, { id }) => {
            const reservation = await Reservation.findByPk(id);
            if (!reservation) throw new Error('Réservation non trouvée');
            await reservation.destroy();
            return true;
        },

        updateReservationStatus: async (_, { id, statut }) => {
            const reservation = await Reservation.findByPk(id);
            if (!reservation) throw new Error('Réservation non trouvée');
            await reservation.update({ statut });
            return await Reservation.findByPk(id, {
                include: [
                    { model: Client, as: 'client' },
                    { model: Chambre, as: 'chambre' }
                ]
            });
        }
    },

    // ==================== FIELD RESOLVERS ====================
    Client: {
        reservations: async (parent) => {
            return await Reservation.findAll({
                where: { client_id: parent.id },
                include: [{ model: Chambre, as: 'chambre' }]
            });
        },
        createdAt: (parent) => parent.created_at?.toISOString(),
        updatedAt: (parent) => parent.updated_at?.toISOString()
    },

    Chambre: {
        reservations: async (parent) => {
            return await Reservation.findAll({
                where: { chambre_id: parent.id },
                include: [{ model: Client, as: 'client' }]
            });
        },
        capaciteMax: (parent) => parent.capacite_max,
        createdAt: (parent) => parent.created_at?.toISOString(),
        updatedAt: (parent) => parent.updated_at?.toISOString()
    },

    Reservation: {
        dateDebut: (parent) => parent.date_debut,
        dateFin: (parent) => parent.date_fin,
        nombrePersonnes: (parent) => parent.nombre_personnes,
        prixTotal: (parent) => parseFloat(parent.prix_total) || null,
        createdAt: (parent) => parent.created_at?.toISOString(),
        updatedAt: (parent) => parent.updated_at?.toISOString()
    }
};

module.exports = { resolvers };
