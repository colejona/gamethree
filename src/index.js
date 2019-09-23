import { Socket } from './socket'
import Phaser from 'phaser';
import { GAME_WIDTH,
         GAME_HEIGHT,
         PLAYER_LOAD,
         PLAYER_JOIN,
         PLAYER_MOVE,
         PLAYER_WELCOME,
         OTHER_PLAYER_MOVE,
         PLAYER_LEAVE,
         PLAYER_SCORE,
         SERVER_SCORE_UPDATE,
         FLAG_MOVE,
         SERVER_FLAG_UPDATE } from './constants';
import { BattleScene } from './scenes/battle-scene';
import { UIScene } from './scenes/ui-scene';

const socket = new Socket();

const gameConfig = {
  width: GAME_WIDTH,
  height: GAME_HEIGHT,
  physics: {
    default: 'arcade',
    arcade: {
      debug: false
    }
  },
  scene: [ BattleScene, UIScene]
};

window.game = new Phaser.Game(gameConfig);

document.addEventListener(PLAYER_LOAD, () => {
  socket.emit(PLAYER_JOIN, {});
});

document.addEventListener(PLAYER_MOVE, (e) => {
  socket.emit(PLAYER_MOVE, e.detail.position);
});

document.addEventListener(FLAG_MOVE, (e) => {
  socket.emit(FLAG_MOVE, e.detail);
});

document.addEventListener(PLAYER_SCORE, (e) => {
  socket.emit(PLAYER_SCORE, e.detail.points);
});

let handleNewPlayerJoin = (player) => {
  document.dispatchEvent(new CustomEvent(PLAYER_JOIN, {detail: {player: player}}));
};

let handleFlagUpdate = (flag) => {
  document.dispatchEvent(new CustomEvent(SERVER_FLAG_UPDATE, {detail: {flag: flag}}));
};

let handleScoreUpdate = (scores) => {
  document.dispatchEvent(new CustomEvent(SERVER_SCORE_UPDATE, {detail: {scores: scores}}));
}

socket.on(PLAYER_WELCOME, (setupData) => {
  document.dispatchEvent(new CustomEvent(PLAYER_WELCOME, {detail: {whoami: setupData.whoami}}));
  setupData.players.forEach((player) => {
    if (player.id == setupData.whoami.id) {
      return;
    }
    handleNewPlayerJoin(player);
  });
  setupData.flags.forEach((f) => {
    handleFlagUpdate(f);
  });
  handleScoreUpdate(setupData.scores);
});

socket.on(PLAYER_JOIN, (player) => {
  handleNewPlayerJoin(player);
});

socket.on(PLAYER_MOVE, (player) => {
  document.dispatchEvent(new CustomEvent(OTHER_PLAYER_MOVE, {detail: {player: player}}));
});

socket.on(PLAYER_LEAVE, (playerId) => {
  document.dispatchEvent(new CustomEvent(PLAYER_LEAVE, {detail: playerId}));
});

socket.on(SERVER_SCORE_UPDATE, (update) => {
  handleScoreUpdate(update.scores);
});
