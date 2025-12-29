import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// Custom metrics
const queryDuration = new Trend('graphql_query_duration');
const mutationDuration = new Trend('graphql_mutation_duration');
const graphqlErrors = new Rate('graphql_errors');

// Test configuration
export const options = {
    scenarios: {
        smoke: {
            executor: 'constant-vus',
            vus: 1,
            duration: '30s',
            startTime: '0s',
        },
        load: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '2m', target: 10 },
                { duration: '5m', target: 10 },
                { duration: '2m', target: 100 },
                { duration: '5m', target: 100 },
                { duration: '2m', target: 500 },
                { duration: '5m', target: 500 },
                { duration: '2m', target: 1000 },
                { duration: '5m', target: 1000 },
                { duration: '2m', target: 0 },
            ],
            startTime: '30s',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500', 'p(99)<1000'],
        graphql_errors: ['rate<0.05'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:4000';

// GraphQL queries and mutations
const queries = {
    getAllReservations: `
        query {
            reservations {
                id
                dateDebut
                dateFin
                statut
                client { id nom prenom }
                chambre { id numero type prix }
            }
        }
    `,
    getReservationById: (id) => `
        query {
            reservation(id: "${id}") {
                id
                dateDebut
                dateFin
                statut
                preferences
                prixTotal
                client { id nom prenom email }
                chambre { id numero type prix disponible }
            }
        }
    `,
    getAllClients: `
        query {
            clients {
                id
                nom
                prenom
                email
                telephone
            }
        }
    `,
    getAvailableRooms: `
        query {
            chambresDisponibles {
                id
                numero
                type
                prix
                description
                capaciteMax
            }
        }
    `,
    searchClients: (name) => `
        query {
            searchClients(nom: "${name}") {
                id
                nom
                prenom
            }
        }
    `,
};

const mutations = {
    createReservation: (data) => `
        mutation {
            createReservation(input: {
                clientId: "${data.clientId}"
                chambreId: "${data.chambreId}"
                dateDebut: "${data.dateDebut}"
                dateFin: "${data.dateFin}"
                preferences: "${data.preferences}"
                nombrePersonnes: ${data.nombrePersonnes}
            }) {
                id
                statut
                prixTotal
            }
        }
    `,
};

function executeGraphQL(query, metricTrend) {
    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const response = http.post(
        `${BASE_URL}/graphql`,
        JSON.stringify({ query }),
        params
    );

    metricTrend.add(response.timings.duration);

    const success = check(response, {
        'status is 200': (r) => r.status === 200,
        'no GraphQL errors': (r) => {
            try {
                const body = JSON.parse(r.body);
                return !body.errors;
            } catch {
                return false;
            }
        },
    });

    graphqlErrors.add(success ? 0 : 1);

    return response;
}

function generateReservationData() {
    const startDay = Math.floor(Math.random() * 20) + 1;
    const duration = Math.floor(Math.random() * 7) + 1;

    return {
        clientId: Math.floor(Math.random() * 10) + 1,
        chambreId: Math.floor(Math.random() * 10) + 1,
        dateDebut: `2024-06-${String(startDay).padStart(2, '0')}`,
        dateFin: `2024-06-${String(Math.min(startDay + duration, 28)).padStart(2, '0')}`,
        preferences: 'k6 GraphQL test',
        nombrePersonnes: Math.floor(Math.random() * 4) + 1,
    };
}

export default function () {
    // Query: Get all reservations
    group('Query: All Reservations', () => {
        executeGraphQL(queries.getAllReservations, queryDuration);
    });
    sleep(0.5);

    // Query: Get single reservation
    group('Query: Single Reservation', () => {
        const id = Math.floor(Math.random() * 8) + 1;
        executeGraphQL(queries.getReservationById(id), queryDuration);
    });
    sleep(0.5);

    // Query: Get all clients
    group('Query: All Clients', () => {
        executeGraphQL(queries.getAllClients, queryDuration);
    });
    sleep(0.5);

    // Query: Available rooms
    group('Query: Available Rooms', () => {
        executeGraphQL(queries.getAvailableRooms, queryDuration);
    });
    sleep(0.5);

    // Query: Search clients
    group('Query: Search Clients', () => {
        executeGraphQL(queries.searchClients('Dupont'), queryDuration);
    });
    sleep(0.5);

    // Mutation: Create reservation
    group('Mutation: Create Reservation', () => {
        const data = generateReservationData();
        executeGraphQL(mutations.createReservation(data), mutationDuration);
    });
    sleep(1);
}

export function handleSummary(data) {
    return {
        'results/k6-graphql-summary.json': JSON.stringify(data, null, 2),
    };
}
