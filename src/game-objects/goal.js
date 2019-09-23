import { GameObject } from './game-object';
import { TEAM_RED, TEAM_GREEN, GOAL_RED, GOAL_GREEN } from '../constants'

const SPRITE_MAPPING = {
  [TEAM_RED]: GOAL_RED,
  [TEAM_GREEN]: GOAL_GREEN
};

export class Goal extends GameObject {
  constructor(scene, x, y, team) {
    super(scene, x, y, team, SPRITE_MAPPING[team]);
  }
}
