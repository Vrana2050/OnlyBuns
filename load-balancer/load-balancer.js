const express = require('express');
const axios = require('axios');
const app = express();

class LoadBalancer {
    constructor() {
        // List of available backend servers
        this.servers = [
            'http://localhost:8082',
            'http://localhost:8083'
        ];
        this.currentIndex = 0;
        this.maxRetries = 3;  // Maximum number of retry attempts
    }

    // Round Robin algorithm implementation
    getNextServer() {
        const server = this.servers[this.currentIndex];
        this.currentIndex = (this.currentIndex + 1) % this.servers.length;
        return server;
    }

    // Retry mechanism
    async handleRequest(req) {
        let attempts = 0;
        let lastError = null;

        while (attempts < this.maxRetries) {
            const server = this.getNextServer();
            try {
                console.log(`Attempting request to server: ${server}`);
                const response = await axios({
                    url: `${server}${req.url}`,
                    method: req.method,
                    headers: { ...req.headers, host: server.split('//')[1] },
                    data: req.body,
                    validateStatus: false // Don't throw on non-2xx responses
                });
                
                console.log(`Success from ${server}`);
                return response;
            } catch (error) {
                console.log(`Attempt ${attempts + 1} failed for ${server}: ${error.message}`);
                lastError = error;
                attempts++;
            }
        }

        throw new Error(`All retry attempts failed. Last error: ${lastError.message}`);
    }
}

const loadBalancer = new LoadBalancer();

// Middleware to parse JSON bodies
app.use(express.json());

// Handle CORS
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
    if (req.method === 'OPTIONS') {
        return res.status(200).end();
    }
    next();
});

// Handle all requests
app.all('*', async (req, res) => {
    try {
        const response = await loadBalancer.handleRequest(req);
        res.status(response.status).send(response.data);
    } catch (error) {
        console.error('Load balancer error:', error.message);
        res.status(500).send('All backend servers are currently unavailable');
    }
});

// Start the load balancer
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Load balancer running on port ${PORT}`);
    console.log('Configured servers:', loadBalancer.servers);
});
