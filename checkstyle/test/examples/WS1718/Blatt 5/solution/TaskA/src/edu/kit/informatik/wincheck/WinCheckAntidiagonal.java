package edu.kit.informatik.wincheck;

public class WinCheckAntidiagonal implements WinCheck {
    @Override
    public int alterI(int i, int offset, Direction direction) {
        switch (direction) {
            case DIRECTION_ONE:
                //Bot Left
                return i + offset;
            case DIRECTION_TWO:
                //Top Right
                return i - offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }

    @Override
    public int alterJ(int j, int offset, Direction direction) {
        switch (direction) {
            case DIRECTION_ONE:
                //Bot Left
                return j - offset;
            case DIRECTION_TWO:
                //Top Right
                return j + offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }
}
