const { Sequelize, DataTypes } = require('sequelize');

// Database configuration
const sequelize = new Sequelize(
    process.env.DB_NAME || 'hoteldb',
    process.env.DB_USER || 'postgres',
    process.env.DB_PASSWORD || 'postgres',
    {
        host: process.env.DB_HOST || 'localhost',
        port: process.env.DB_PORT || 5432,
        dialect: 'postgres',
        logging: false,
        pool: {
            max: 10,
            min: 0,
            acquire: 30000,
            idle: 10000
        }
    }
);

// ==================== MODELS ====================

const Client = sequelize.define('Client', {
    id: {
        type: DataTypes.BIGINT,
        primaryKey: true,
        autoIncrement: true
    },
    nom: {
        type: DataTypes.STRING,
        allowNull: false
    },
    prenom: {
        type: DataTypes.STRING,
        allowNull: false
    },
    email: {
        type: DataTypes.STRING,
        allowNull: false,
        unique: true
    },
    telephone: {
        type: DataTypes.STRING,
        allowNull: false
    }
}, {
    tableName: 'clients',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: 'updated_at'
});

const Chambre = sequelize.define('Chambre', {
    id: {
        type: DataTypes.BIGINT,
        primaryKey: true,
        autoIncrement: true
    },
    numero: {
        type: DataTypes.STRING,
        allowNull: false,
        unique: true
    },
    type: {
        type: DataTypes.ENUM('SIMPLE', 'DOUBLE', 'SUITE', 'DELUXE', 'FAMILIALE'),
        allowNull: false
    },
    prix: {
        type: DataTypes.DECIMAL(10, 2),
        allowNull: false
    },
    disponible: {
        type: DataTypes.BOOLEAN,
        defaultValue: true
    },
    description: {
        type: DataTypes.STRING(1000)
    },
    capacite_max: {
        type: DataTypes.INTEGER
    }
}, {
    tableName: 'chambres',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: 'updated_at'
});

const Reservation = sequelize.define('Reservation', {
    id: {
        type: DataTypes.BIGINT,
        primaryKey: true,
        autoIncrement: true
    },
    date_debut: {
        type: DataTypes.DATEONLY,
        allowNull: false
    },
    date_fin: {
        type: DataTypes.DATEONLY,
        allowNull: false
    },
    statut: {
        type: DataTypes.ENUM('EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'TERMINEE'),
        defaultValue: 'EN_ATTENTE'
    },
    preferences: {
        type: DataTypes.STRING(2000)
    },
    nombre_personnes: {
        type: DataTypes.INTEGER
    },
    prix_total: {
        type: DataTypes.DECIMAL(10, 2)
    },
    commentaires: {
        type: DataTypes.STRING(500)
    }
}, {
    tableName: 'reservations',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: 'updated_at'
});

// ==================== ASSOCIATIONS ====================

Client.hasMany(Reservation, { foreignKey: 'client_id', as: 'reservations' });
Reservation.belongsTo(Client, { foreignKey: 'client_id', as: 'client' });

Chambre.hasMany(Reservation, { foreignKey: 'chambre_id', as: 'reservations' });
Reservation.belongsTo(Chambre, { foreignKey: 'chambre_id', as: 'chambre' });

module.exports = {
    sequelize,
    Client,
    Chambre,
    Reservation
};
