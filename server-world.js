class WorldObj {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

class Player extends WorldObj {
    constructor(id, x, y) {
        super(x, y);
        this.id = id;
    }

    movePlayer(x, y) {
        this.x = x;
        this.y = y;
    }
}

class ServerWorld {
    constructor () {
        this.players = new Map();
    }

    newPlayerId() {
        return Math.random().toString(36).substr(2, 9);
    }

    getPlayer(id) {
        return this.players.get(id);
    }

    allPlayers() {
        return Array.from(this.players.values());
    }

    newPlayer(x, y) {
        const player = new Player(this.newPlayerId(), x, y);
        this.players.set(player.id, player);
        return player;
    }

    removePlayer(playerId) {
        this.players.delete(playerId);
    }
}

module.exports = ServerWorld;
