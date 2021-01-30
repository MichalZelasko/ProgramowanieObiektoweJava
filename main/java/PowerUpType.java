public enum PowerUpType {
    TwoMovePerRound,
    TwoFieldsBullet,
    DoublePowerBullet,
    BouncingBullet,
    Immortality,
    ExtraLife;

    public PowerUpType fromInt(int n) {
        switch (n) {
            case 1:
                return TwoMovePerRound;
            case 2:
                return TwoFieldsBullet;
            case 3:
                return DoublePowerBullet;
            case 4:
                return BouncingBullet;
            case 5:
                return ExtraLife;
            case 6:
                return Immortality;
        }
        throw new IllegalArgumentException("Incorrect power up type argument");
    }

    public int toInt() {
        switch (this) {
            case TwoMovePerRound:
                return 1;
            case TwoFieldsBullet:
                return 2;
            case DoublePowerBullet:
                return 3;
            case BouncingBullet:
                return 4;
            case ExtraLife:
                return 5;
            case Immortality:
                return 6;
        }
        throw new IllegalArgumentException("Incorrect power up type argument");
    }

    public String toString() {
        switch (this) {
            case TwoMovePerRound:
                return "Two Move Per Round";
            case TwoFieldsBullet:
                return "Double speed Bullet";
            case DoublePowerBullet:
                return "Double Power Bullet";
            case BouncingBullet:
                return "Bouncing Bullet";
            case ExtraLife:
                return "Immortality";
            case Immortality:
                return "ExtraLife";
        }
        throw new IllegalArgumentException("Incorrect power up type argument");
    }
}
