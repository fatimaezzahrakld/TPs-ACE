import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// Custom metrics
const reservationCreated = new Counter('reservations_created');
const reservationErrors = new Rate('reservation_errors');
const reservationDuration = new Trend('reservation_duration');

// Test configuration
export const options = {
    scenarios: {
        // Smoke test
        smoke: {
            executor: 'constant-vus',
            vus: 1,
            duration: '30s',
            startTime: '0s',
            tags: { scenario: 'smoke' },
        },
        // Load test
        load: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '2m', target: 10 },    // Ramp up to 10 users
                { duration: '5m', target: 10 },   // Stay at 10 users
                { duration: '2m', target: 100 },  // Ramp up to 100 users
                { duration: '5m', target: 100 },  // Stay at 100 users
                { duration: '2m', target: 500 },  // Ramp up to 500 users
                { duration: '5m', target: 500 },  // Stay at 500 users
                { duration: '2m', target: 1000 }, // Ramp up to 1000 users
                { duration: '5m', target: 1000 }, // Stay at 1000 users
                { duration: '2m', target: 0 },    // Ramp down
            ],
            startTime: '30s',
            tags: { scenario: 'load' },
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500', 'p(99)<1000'],
        http_req_failed: ['rate<0.01'],
        reservation_errors: ['rate<0.05'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

// Test data
function generateReservation() {
    const startDay = Math.floor(Math.random() * 30) + 1;
    const duration = Math.floor(Math.random() * 7) + 1;

    return {
        clientId: Math.floor(Math.random() * 10) + 1,
        chambreId: Math.floor(Math.random() * 10) + 1,
        dateDebut: `2024-0${Math.floor(Math.random() * 9) + 1}-${String(startDay).padStart(2, '0')}`,
        dateFin: `2024-0${Math.floor(Math.random() * 9) + 1}-${String(Math.min(startDay + duration, 28)).padStart(2, '0')}`,
        preferences: 'Test reservation from k6',
        nombrePersonnes: Math.floor(Math.random() * 4) + 1,
        commentaires: `Load test - VU: ${__VU}, Iteration: ${__ITER}`,
    };
}

export default function () {
    // GET all reservations
    group('Get All Reservations', () => {
        const response = http.get(`${BASE_URL}/api/reservations`);

        check(response, {
            'status is 200': (r) => r.status === 200,
            'response has reservations': (r) => {
                try {
                    const body = JSON.parse(r.body);
                    return Array.isArray(body);
                } catch {
                    return false;
                }
            },
        });

        reservationDuration.add(response.timings.duration);

        if (response.status !== 200) {
            reservationErrors.add(1);
        } else {
            reservationErrors.add(0);
        }
    });

    sleep(1);

    // GET single reservation
    group('Get Reservation by ID', () => {
        const id = Math.floor(Math.random() * 8) + 1;
        const response = http.get(`${BASE_URL}/api/reservations/${id}`);

        check(response, {
            'status is 200 or 404': (r) => r.status === 200 || r.status === 404,
        });

        reservationDuration.add(response.timings.duration);
    });

    sleep(1);

    // POST create reservation
    group('Create Reservation', () => {
        const payload = JSON.stringify(generateReservation());
        const params = {
            headers: { 'Content-Type': 'application/json' },
        };

        const response = http.post(`${BASE_URL}/api/reservations`, payload, params);

        const success = check(response, {
            'status is 201 or 400': (r) => r.status === 201 || r.status === 400,
        });

        if (response.status === 201) {
            reservationCreated.add(1);
        }

        reservationDuration.add(response.timings.duration);
    });

    sleep(1);

    // GET reservations by client
    group('Get Reservations by Client', () => {
        const clientId = Math.floor(Math.random() * 10) + 1;
        const response = http.get(`${BASE_URL}/api/reservations/client/${clientId}`);

        check(response, {
            'status is 200': (r) => r.status === 200,
        });

        reservationDuration.add(response.timings.duration);
    });

    sleep(1);

    // GET available rooms
    group('Get Available Rooms', () => {
        const response = http.get(`${BASE_URL}/api/chambres/available`);

        check(response, {
            'status is 200': (r) => r.status === 200,
        });

        reservationDuration.add(response.timings.duration);
    });

    sleep(1);
}

export function handleSummary(data) {
    return {
        'results/k6-rest-summary.json': JSON.stringify(data, null, 2),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}

function textSummary(data, options) {
    const { metrics, root_group } = data;

    let output = '\n=== k6 REST API Test Results ===\n\n';

    output += `Total Requests: ${metrics.http_reqs?.values?.count || 0}\n`;
    output += `Failed Requests: ${metrics.http_req_failed?.values?.fails || 0}\n`;
    output += `Avg Duration: ${(metrics.http_req_duration?.values?.avg || 0).toFixed(2)}ms\n`;
    output += `P95 Duration: ${(metrics.http_req_duration?.values?.['p(95)'] || 0).toFixed(2)}ms\n`;
    output += `P99 Duration: ${(metrics.http_req_duration?.values?.['p(99)'] || 0).toFixed(2)}ms\n`;
    output += `Reservations Created: ${metrics.reservations_created?.values?.count || 0}\n`;

    return output;
}
