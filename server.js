const express = require('express');
const port = process.env.PORT || 5000;
const app = express();
const winston = require('winston');
const expressWinston = require('express-winston');
const server = app.listen(port, () => console.log(`Listening on port ${port}`));
const io = require('socket.io')(server);
const ServerWorld = require('./server-world');
const constants = require('./src/constants');
const logger = require('./logger');

app.use(express.static('build'));
app.use(expressWinston.logger({
    transports: [
        new winston.transports.Console()
    ],
    format: winston.format.combine(
        winston.format.colorize(),
        winston.format.json()
    ),
    meta: true, // optional: control whether you want to log the meta data about the request (default to true)
    msg: 'HTTP {{req.method}} {{req.url}}', // optional: customize the default logging message. E.g. "{{res.statusCode}} {{req.method}} {{res.responseTime}}ms {{req.url}}"
    expressFormat: true, // Use the default Express/morgan request formatting. Enabling this will override any msg if true. Will only output colors with colorize set to true
    colorize: false, // Color the text and status code, using the Express/morgan color palette (text: gray, status: default green, 3XX cyan, 4XX yellow, 5XX red).
    ignoreRoute: function () { return false; } // optional: allows to skip some log messages based on request and/or response
}));
app.get('/', (req, resp) => {resp.sendFile(__dirname + '/build/index.html')});

io.set('origins', '*:*');

const worldInstance = new ServerWorld();

io.on('connection', socket => {
    let playerInstance;

    socket.on(constants.PLAYER_JOIN, () => {
        playerInstance = worldInstance.newPlayer(constants.GAME_WIDTH / 2 - 5, constants.GAME_HEIGHT / 2 - 5);
        const worldPayload = {whoami: playerInstance, players: worldInstance.allPlayers};
        logger.info(`Add Player ${playerInstance.id} to world: ${JSON.stringify(worldPayload)}`);
        socket.emit(constants.PLAYER_WELCOME, worldPayload);
        socket.broadcast.emit(constants.PLAYER_JOIN, playerInstance);
    });

    socket.on(constants.PLAYER_MOVE, (position) => {
        if (playerInstance && position && position.x && position.y) {
            playerInstance.movePlayer(position.x, position.y);
            socket.broadcast.emit(constants.PLAYER_MOVE, playerInstance);
        }
    });

    socket.on('disconnect', () => {
        if (playerInstance && playerInstance.id) {
            worldInstance.removePlayer(playerInstance.id);
            socket.broadcast.emit(constants.PLAYER_LEAVE, {id: playerInstance.id});
            logger.info(`Player ${playerInstance.id} has left the server...`);
        }
    });
});
