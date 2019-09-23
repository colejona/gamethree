export class GameObject {
    constructor(scene, x, y, sprite) {
        this.startX = x;
        this.startY = y;
        this.sprite = scene.physics.add.sprite(x, y, sprite);
        this.scene = scene;
        this.moveGroup = scene.physics.add.group();
        this.moveGroup.add(this.sprite);
    }

    addChild(gameObject) {
        this.moveGroup.add(gameObject.sprite);
    }

    removeChild(gameObject) {
        this.moveGroup.remove(gameObject.sprite);
    }

    onCollide(target, callback, preCallbackCheck=null) {
        this.scene.physics.add.overlap(this.sprite, target.sprite, callback, preCallbackCheck, this);
    }

    getPosition() {
        return {
            x: this.sprite.x,
            y: this.sprite.y
        };
    }

    setPosition(x, y) {
        this.sprite.x = x;
        this.sprite.y = y;
    }
}
