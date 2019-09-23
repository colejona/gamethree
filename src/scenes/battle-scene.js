import Phaser from 'phaser';
import { Player } from '../game-objects/player'
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
    }

    sendPlayerPositionBeacon(eventName) {
        document.dispatchEvent(new CustomEvent(eventName, {detail: {position: this.player.getPosition()}}));
    }

    handlePlayerJoin(newPlayer) {
        const playerObj = new Player(this, newPlayer.x, newPlayer.y, newPlayer.id);
        this.otherPlayers.push(playerObj);
    }

    mainCamera() {
        return this.cameras.cameras[0];
    }

    create() {
        this.map = this.add.image(0, 0, MAP);

        this.player = null;
        this.otherPlayers = [];
        this.cursors = this.input.keyboard.createCursorKeys();
        document.addEventListener(constants.PLAYER_WELCOME, (e) => {
            const playerData = e.detail.whoami;
            this.player = new Player(this, playerData.x, playerData.y, playerData.id);
            this.mainCamera().startFollow(this.player.sprite);
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
        }

        this.otherPlayers.forEach((p) => {
            p.moveToTargetPosition();
        });
    }
}
