const constants = require('./src/constants');

class WorldObj {
  constructor(x, y, team) {
    this.x = x;
    this.y = y;
    this.team = team;
  }
}

class Flag extends WorldObj {}

class Player extends WorldObj {
  constructor(id, x, y, team) {
    super(x, y, team);
    this.id = id;
  }

  movePlayer(x, y) {
    this.x = x;
    this.y = y;
  }
}

class Team {
  constructor(name) {
    this.name = name;
    this.score = 0;
  }

  addScore(points) {
    this.score += points;
  }

  resetScore() {
    this.score = 0;
  }
}

class ServerWorld {
  constructor (teams) {
    this.teams = teams.map((t) => {
      return new Team(t);
    });
    this.flags = teams.map((t) => {
      return new Flag(constants.FLAG_START[t].x, constants.FLAG_START[t].y, t);
    });
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

  getTeams() {
    return this.teams;
  }

  getScores() {
    return this.teams.reduce((scores, t) => {
      scores[t.name] = t.score;
      return scores;
    }, {});
  }

  newPlayer(x, y) {
    const team = findTeamWithFewestPlayers(this);
    const player = new Player(this.newPlayerId(), x, y, team);
    this.players.set(player.id, player);
    return player;
  }

  removePlayer(playerId) {
    this.players.delete(playerId);
  }

  updateScore(teamName, points) {
    const team = this.teams.find((t) => t.name === teamName);
    if (team) {
      team.addScore(points);
    }
  }

  updateFlagPosition(flag) {
    const f = this.flags.find((f) => f.team == flag.flag_team_color);
    if (f) {
      f.x = flag.position.x;
      f.y = flag.position.y;
    }
  }

}

const findTeamWithFewestPlayers = (serverWorld) => {
  const playersByTeam = serverWorld.allPlayers().reduce((teams, player) => {
    if (teams[player.team] === undefined) {
      teams[player.team] = 1;
    } else {
      teams[player.team]++;
    }
    return teams;
  }, {});

  serverWorld.getTeams().map((team) => {
    if (playersByTeam[team.name] === undefined) {
      playersByTeam[team.name] = 0;
    }
  });

  return Object.keys(playersByTeam).reduce((teamWithFewest, nextTeam) => {
    const difference = playersByTeam[teamWithFewest] - playersByTeam[nextTeam];
    if (difference < 0) {
      return teamWithFewest;
    } else if (difference === 0) {
      const rand = Math.random();
      if (rand >= 0.5) {
        return teamWithFewest;
      } else {
        return nextTeam;
      }
    } else {
      return nextTeam;
    }
  });
}

module.exports = ServerWorld;
