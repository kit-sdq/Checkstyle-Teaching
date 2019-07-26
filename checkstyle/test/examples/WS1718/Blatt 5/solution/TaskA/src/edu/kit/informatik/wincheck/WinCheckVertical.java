package edu.kit.informatik.wincheck;

public class WinCheckVertical implements WinCheck {
    @Override
    public int alterI(int i, int offset, Direction direction) throws IllegalArgumentException {
        switch (direction) {
            case DIRECTION_ONE:
                //Up
                return i - offset;
            case DIRECTION_TWO:
                //Down
                return i + offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }

    @Override
    public int alterJ(int j, int offset, Direction direction) {
        return j;
    }
}
