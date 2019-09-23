const primitives = {
    GAME_WIDTH: 1000,
    GAME_HEIGHT: 600,
    PLAYER_LOAD: 'player-load',
    PLAYER_JOIN: 'player-join',
    PLAYER_MOVE: 'player-move',
    FLAG_MOVE: 'flag-move',
    OTHER_PLAYER_MOVE: 'other-player-move',
    PLAYER_WELCOME: 'player-welcome',
    PLAYER_LEAVE: 'player-leave',
    TEAM_GREEN: 'team-green',
    TEAM_RED: 'team-red',
    PLAYER_RED: 'player-red',
    PLAYER_GREEN: 'player-green',
    FLAG_RED: 'flag-red',
    FLAG_GREEN: 'flag-green',
    GOAL_RED: 'goal-red',
    GOAL_GREEN: 'goal-green',
    PLAYER_SPEED: 300,
    PLAYER_SPEED_WITH_FLAG: 215,
    SERVER_FLAG_UPDATE: 'flag-update',
    PLAYER_SCORE: 'player-score',
    SERVER_SCORE_UPDATE: 'score-update',
    POINTS_PER_SCORE: 1,
    SCORE_FONT: '25px sans-serif',
    UPDATE_SCORE_TEXT: 'update-score-text',
    GAME_LOG_PREFIX: '[GameTwo]'
};

const constants = Object.assign(primitives, {
    FLAG_START: {
    [primitives.TEAM_RED] : {
      x: primitives.GAME_WIDTH / 2 + 400,
      y: primitives.GAME_HEIGHT / 2
    },
    [primitives.TEAM_GREEN] : {
      x: primitives.GAME_WIDTH / 2 - 400,
      y: primitives.GAME_HEIGHT / 2
    }
  }
});

module.exports = constants;
