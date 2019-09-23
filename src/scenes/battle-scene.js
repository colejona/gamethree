import Phaser from 'phaser';
import { Player } from '../game-objects/player'
import { Flag } from '../game-objects/flag'
import { Goal } from '../game-objects/goal'
import * as constants from '../constants';

const MAP = 'map';

export class BattleScene extends Phaser.Scene {
  constructor() {
    super({ key: 'BattleScene', active: true });
  }

  preload() {
    this.load.spritesheet(constants.PLAYER_GREEN, 'assets/green-link.png', {frameWidth: 24, frameHeight: 32});
    this.load.spritesheet(constants.PLAYER_RED, 'assets/red-link.png', {frameWidth: 24, frameHeight: 32});
    this.load.image(MAP, 'assets/map.png');
    this.load.image(constants.FLAG_RED, 'assets/red-flag.png');
    this.load.image(constants.FLAG_GREEN, 'assets/green-flag.png');
    this.load.image(constants.GOAL_RED, 'assets/red-goal.png');
    this.load.image(constants.GOAL_GREEN, 'assets/green-goal.png');
  }

  sendPlayerPositionBeacon(eventName) {
    document.dispatchEvent(new CustomEvent(eventName, {detail: {position: this.player.getPosition()}}));
  }

  sendFlagPositionBeacon(flag) {
    document.dispatchEvent(new CustomEvent(constants.FLAG_MOVE, {detail:{
      position: flag.getPosition(),
      flag_team_color: flag.team
    }}));
  }

  setupFlagPickUp(player) {
    const opposingFlag = this.getOpposingFlag(player.team);
    player.onCollide(opposingFlag, () => {
      player.pickUpFlag(opposingFlag);
    }, () => { return opposingFlag.canBePickedUp(); });
  }

  getPlayerHoldingFlag(flag) {
    if (this.player.carriedFlag === flag) {
      return this.player;
    }
    let player = null;
    this.otherPlayers.forEach((p) => {
      if (p.carriedFlag === flag) {
        player = p;
      }
    });
    return player;
  }

  handlePlayerJoin(newPlayer) {
    const playerObj = new Player(this, newPlayer.x, newPlayer.y, newPlayer.id, newPlayer.team, {is_self: false});
    this.otherPlayers.push(playerObj);
    this.setupFlagPickUp(playerObj);
    if (playerObj.team !== this.player.team) {
        this.player.onCollide(playerObj, ()=> {
          if (this.player.hasFlag()) {
            const flag = this.player.carriedFlag;
            this.player.dropFlag();
            this.sendFlagPositionBeacon(flag);
          }
          if (playerObj.hasFlag()) {
            playerObj.dropFlag();
          }
        });
    }
  }

  getOpposingFlag(team) {
    switch(team) {
      case constants.TEAM_RED:
        return this.greenFlag;
      case constants.TEAM_GREEN:
        return this.redFlag;
    }
  }

  handleGoal(goal, flag) {
    const holdingPlayer = this.getPlayerHoldingFlag(flag);
    if (holdingPlayer) {
      holdingPlayer.dropFlag();
      if (holdingPlayer === this.player) {
        const points = constants.POINTS_PER_SCORE;
        document.dispatchEvent(new CustomEvent(constants.PLAYER_SCORE, {
          detail:{
            points: points
          }
        }));
        this.scores[holdingPlayer.team] += points;
        this.updateScoreTexts();
      }
    }
    flag.resetPosition();
    this.sendFlagPositionBeacon(flag);
  }

  updateScoreTexts() {
    document.dispatchEvent(new CustomEvent(constants.UPDATE_SCORE_TEXT, {
      detail: {
        scores: this.scores
      }
    }));
  }

  mainCamera() {
    return this.cameras.cameras[0];
  }

  create() {
    this.scores = {
      [constants.TEAM_RED]: 0,
      [constants.TEAM_GREEN]: 0
    };
    this.map = this.add.image(0, 0, MAP);
    this.redGoal = new Goal(this,
                            constants.FLAG_START[constants.TEAM_RED].x,
                            constants.FLAG_START[constants.TEAM_RED].y,
                            constants.TEAM_RED);
    this.redFlag = new Flag(this,
                            constants.FLAG_START[constants.TEAM_RED].x,
                            constants.FLAG_START[constants.TEAM_RED].y,
                            constants.TEAM_RED);
    this.greenGoal = new Goal(this,
                              constants.FLAG_START[constants.TEAM_GREEN].x,
                              constants.FLAG_START[constants.TEAM_GREEN].y,
                              constants.TEAM_GREEN);
    this.greenFlag = new Flag(this,
                              constants.FLAG_START[constants.TEAM_GREEN].x,
                              constants.FLAG_START[constants.TEAM_GREEN].y,
                              constants.TEAM_GREEN);

    this.greenGoal.onCollide(this.redFlag, () => {
      this.handleGoal(this.greenGoal, this.redFlag);
    }, () => { return !this.redFlag.isResetting; });
    this.greenGoal.onCollide(this.greenFlag, () => {
      this.greenFlag.completeReset();
    });

    this.redGoal.onCollide(this.greenFlag, () => {
      this.handleGoal(this.redGoal, this.greenFlag);
    }, () => { return !this.greenFlag.isResetting });
    this.redGoal.onCollide(this.redFlag, () => {
      this.redFlag.completeReset();
    });

    this.player = null;
    this.otherPlayers = [];
    this.flags = [this.redFlag, this.greenFlag];
    this.cursors = this.input.keyboard.createCursorKeys();
    document.addEventListener(constants.PLAYER_WELCOME, (e) => {
      const playerData = e.detail.whoami;
      this.player = new Player(this, playerData.x, playerData.y, playerData.id, playerData.team);
      this.mainCamera().startFollow(this.player.sprite);
      this.setupFlagPickUp(this.player);
    });
    document.addEventListener(constants.PLAYER_JOIN, (e) => {
      this.handlePlayerJoin(e.detail.player);
    });
    document.addEventListener(constants.OTHER_PLAYER_MOVE, (e) => {
      const targetPlayer = e.detail.player;
      const player = this.otherPlayers.find((p) => p.id === targetPlayer.id);
      if (player) {
        player.setTargetPosition(targetPlayer);
      }
    });
    document.addEventListener(constants.SERVER_FLAG_UPDATE, (e) => {
      const targetFlag = e.detail.flag;
      const flag = this.flags.find((f) => f.team === targetFlag.team);
      if (flag) {
        flag.setPosition(targetFlag.x, targetFlag.y);
      }
    });
    document.addEventListener(constants.SERVER_SCORE_UPDATE, (e) => {
      this.scores = e.detail.scores;
      this.updateScoreTexts();
    });
    document.addEventListener(constants.PLAYER_LEAVE, (e) => {
      const id = e.detail.id;
      this.otherPlayers
        .filter((p) => p.id === id)
        .map((p)=> {p.leave()});
      this.otherPlayers = this.otherPlayers.filter((p) => p.id !== id);
    });
    document.dispatchEvent(new Event(constants.PLAYER_LOAD));
  }

  update() {
    // Don't run update loop until server responds to create player
    if (!this.player) {
      return;
    }

    let moving = false;
    if (this.cursors.up.isDown) {
      this.player.moveUp();
      moving = true;
    } else if (this.cursors.down.isDown) {
      this.player.moveDown();
      moving = true;
    } else {
      this.player.stopY();
    }

    if (this.cursors.right.isDown) {
      this.player.moveRight();
      moving = true;
    } else if (this.cursors.left.isDown) {
      this.player.moveLeft();
      moving = true;
    } else {
      this.player.stopX();
    }

    if (moving) {
      this.sendPlayerPositionBeacon(constants.PLAYER_MOVE);
      if (this.player.hasFlag()) {
        this.sendFlagPositionBeacon(this.player.carriedFlag);
      }
    }

    this.otherPlayers.forEach((p) => {
      p.moveToTargetPosition();
    });
  }
}
