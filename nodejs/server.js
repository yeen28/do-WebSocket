const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app);
const { Server } = require('socket.io');
const io = new Server(server);

// Serve static files
app.use(express.static('public'));

// Store connected users
const users = new Map();

io.on('connection', (socket) => {
    console.log('a user connected');

    // Handle user joining
    socket.on('join', (username) => {
        users.set(socket.id, username);
        io.emit('user joined', username);
        io.emit('user list', Array.from(users.values()));
    });

    // Handle chat messages
    socket.on('chat message', (msg) => {
        const username = users.get(socket.id);
        io.emit('chat message', { username, message: msg });
    });

    // Handle user disconnection
    socket.on('disconnect', () => {
        const username = users.get(socket.id);
        if (username) {
            users.delete(socket.id);
            io.emit('user left', username);
            io.emit('user list', Array.from(users.values()));
        }
        console.log('user disconnected');
    });
});

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
}); 