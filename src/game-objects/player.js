import { GameObject } from './game-object';
import { PLAYER_GREEN, PLAYER_SPEED } from '../constants'

const getFrames = (row) => {
    return [...Array(12).keys()].map(n => n + (12 * row));
}

const LEFT = 'left';
const RIGHT = 'right';
const UP = 'up';
const DOWN = 'down';
const PLAYER_IDLE = 'player-idle-';
const PLAYER_MOVE = 'player-move-';
const TOLERANCE = 5;

export class Player extends GameObject {
    constructor(scene, x, y, id) {
        super(scene, x, y, PLAYER_GREEN);
        this.facing = UP;
        this.setId(id);
        this.setupAnimationsForSpriteSheet(scene, PLAYER_GREEN);
        this.sprite.anims.play(this.getAnimationKey(PLAYER_GREEN, PLAYER_IDLE, this.facing));
        this.sprite.setScale(3, 3);
        this.sprite.overlapX = -50
        this.sprite.overlapY = -50;
        this.targetPosition = null;
        this.sprite.setSize(this.sprite.width * 0.75, this.sprite.height * 0.75, false)
        this.sprite.setOffset(0, this.sprite.height * 0.25);
    }

    getAnimationKey(playerSprite, direction, action) {
        return `${playerSprite}-${direction}${action}`;
    }

    setupAnimationsForSpriteSheet(scene, playerSprite) {
        if (Player.animations.includes(playerSprite)) {
            return;
        }

        Player.animations.push(playerSprite);
        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_IDLE, UP),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: [1]
            }),
            start: 1,
            end: 1,
            frameRate: 1,
            repeat: -1
            });

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_IDLE, DOWN),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: [25]
            }),
            start: 2,
            end: 2,
            frameRate: 1,
            repeat: -1
        });

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_IDLE, LEFT),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: [37]
            }),
            start: 2,
            end: 2,
            frameRate: 1,
            repeat: -1
        });

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_IDLE, RIGHT),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: [13]
            }),
            start: 2,
            end: 2,
            frameRate: 1,
            repeat: -1
        });

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_MOVE, LEFT),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: getFrames(3).concat(getFrames(7))
            }),
            frameRate: 30,
            repeat: -1
        });

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_MOVE, RIGHT),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: getFrames(1).concat(getFrames(5))
            }),
            frameRate: 30,
            repeat: -1
        });1

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_MOVE, UP),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: getFrames(0).concat(getFrames(4))
            }),
            frameRate: 30,
            repeat: -1
        });

        scene.anims.create({
            key: this.getAnimationKey(playerSprite, PLAYER_MOVE, DOWN),
            frames: scene.anims.generateFrameNumbers(playerSprite, {
                frames: getFrames(2).concat(getFrames(6))
            }),
            frameRate: 30,
            repeat: -1
        });
    }

    setId(id) {
        this.id = id;
    }

    playAnimIfChanged(animKey) {
        if (this.sprite.anims.currentAnim.key === animKey) {
            return;
        }

        this.sprite.anims.play(animKey);
    }

    getSpeed() {
        return PLAYER_SPEED;
    }

    moveUp() {
        this.moveGroup.setVelocityY(-this.getSpeed());
        this.updateDirection(UP);
    }

    moveDown() {
        this.moveGroup.setVelocityY(this.getSpeed());
        this.updateDirection(DOWN);
    }

    moveLeft() {
        this.moveGroup.setVelocityX(-this.getSpeed());
        this.updateDirection(LEFT);
    }

    moveRight() {
        this.moveGroup.setVelocityX(this.getSpeed());
        this.updateDirection(RIGHT);
    }

    updateDirection(facing, isStop=false) {
        if (!isStop && facing === this.facing) {
            return;
        }

        if (!this.isStop) {
            this.facing = facing;
        }

        if (this.sprite.body.velocity.x || this.sprite.body.velocity.y) {
            this.playAnimIfChanged(this.getAnimationKey(PLAYER_GREEN, PLAYER_MOVE, this.facing));
        } else {
            this.playAnimIfChanged(this.getAnimationKey(PLAYER_GREEN, PLAYER_IDLE, this.facing));
        }
    }

    stopX() {
        this.moveGroup.setVelocityX(0);
        this.updateDirection(this.facing, true);
    }

    stopY() {
        this.moveGroup.setVelocityY(0);
        this.updateDirection(this.facing, true);
    }

    setTargetPosition(position) {
        this.targetPosition = position;
    }

    moveToTargetPosition() {
        if (this.config.isSelf || !this.targetPosition) {
            return;
        }

        if (Math.abs(this.sprite.y - this.targetPosition.y) <= TOLERANCE) {
            this.stopY();
        } else if (this.sprite.y > this.targetPosition.y) {
            this.moveUp();
        } else {
            this.moveDown();
        }

        if (Math.abs(this.sprite.x - this.targetPosition.x) <= TOLERANCE) {
            this.stopX();
        } else if (this.sprite.x < this.targetPosition.x) {
            this.moveRight();
        } else {
            this.moveLeft();
        }
    }

    leave() {
        this.sprite.destroy();
    }
}

Player.animations = [];
