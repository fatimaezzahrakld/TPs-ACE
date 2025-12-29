-- Database initialization script
-- This script runs when PostgreSQL container starts for the first time

-- Create database if not exists (already created by POSTGRES_DB env var)
-- Just add any additional setup here

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE hoteldb TO postgres;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Display message
DO $$
BEGIN
    RAISE NOTICE 'Hotel database initialized successfully!';
END $$;
