const { ApolloServer } = require('@apollo/server');
const { expressMiddleware } = require('@apollo/server/express4');
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { typeDefs } = require('./schema');
const { resolvers } = require('./resolvers');
const { sequelize } = require('./database');
const { collectDefaultMetrics, register } = require('prom-client');

// Configure metrics
collectDefaultMetrics();

const app = express();
const PORT = process.env.PORT || 4000;

async function startServer() {
    // Test database connection
    try {
        await sequelize.authenticate();
        console.log('✅ Database connection established');

        // Sync models
        await sequelize.sync({ alter: true });
        console.log('✅ Database models synchronized');
    } catch (error) {
        console.error('❌ Database connection failed:', error);
    }

    // Create Apollo Server
    const server = new ApolloServer({
        typeDefs,
        resolvers,
        introspection: true,
    });

    await server.start();

    // Apply middleware
    app.use(cors());
    app.use(bodyParser.json());

    // GraphQL endpoint
    app.use('/graphql', expressMiddleware(server, {
        context: async ({ req }) => ({
            // Add context if needed
        }),
    }));

    // Health check endpoint
    app.get('/health', (req, res) => {
        res.json({ status: 'UP', service: 'hotel-graphql-api' });
    });

    // Metrics endpoint for Prometheus
    app.get('/metrics', async (req, res) => {
        res.set('Content-Type', register.contentType);
        res.end(await register.metrics());
    });

    app.listen(PORT, () => {
        console.log(`🚀 GraphQL Server ready at http://localhost:${PORT}/graphql`);
        console.log(`📊 Metrics available at http://localhost:${PORT}/metrics`);
        console.log(`💚 Health check at http://localhost:${PORT}/health`);
    });
}

startServer();
