import Phaser from 'phaser';
import * as constants from '../constants';

export class UIScene extends Phaser.Scene {
  constructor() {
    super({ key: 'UIScene', active: true });
  }

  getScoreText(team, scores) {
    const points = scores[team];
    return `${team.replace('-', ' ').toUpperCase()}: ${points}`;
  }

  create() {
    const zeroScores = {
      [constants.TEAM_RED]: 0,
      [constants.TEAM_GREEN]: 0
    };
    this.greenScore = this.add.text(50,
                                    100,
                                    this.getScoreText(constants.TEAM_GREEN, zeroScores),
                                    {font: constants.SCORE_FONT});
    this.redScore = this.add.text(constants.GAME_WIDTH - 225,
                                  100,
                                  this.getScoreText(constants.TEAM_RED, zeroScores),
                                  {font: constants.SCORE_FONT});
    this.scoreTexts = {
      [constants.TEAM_RED]: this.redScore,
      [constants.TEAM_GREEN]: this.greenScore
    };
    document.addEventListener(constants.UPDATE_SCORE_TEXT, (e) => {
      const scores = e.detail.scores;
      for (const [team, txt] of Object.entries(this.scoreTexts)) {
        txt.setText(this.getScoreText(team, scores));
      }
    });
  }
}
