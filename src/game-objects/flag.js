import { GameObject } from './game-object';
import { TEAM_RED, TEAM_GREEN, FLAG_RED, FLAG_GREEN } from '../constants'

const SPRITE_MAPPING = {
  [TEAM_RED]: FLAG_RED,
  [TEAM_GREEN]: FLAG_GREEN
};

export class Flag extends GameObject {
  constructor(scene, x, y, team) {
    super(scene, x, y, team, SPRITE_MAPPING[team]);
    this.sprite.setSize(this.sprite.width / 2, this.sprite.height)
    this.isHeld = false;
    this.isResetting = false;
    this.holder = null;

  }

  pickUp(holder) {
    this.holder = holder;
    this.isHeld = true;
  }

  drop() {
    this.holder = null;
    this.isHeld = false;
  }

  resetPosition() {
    super.resetPosition();
    this.isResetting = true;
  }

  completeReset() {
    this.isResetting = false;
  }

  canBePickedUp() {
    return !(this.isHeld || this.isResetting);
  }
}
