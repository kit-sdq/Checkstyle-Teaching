package edu.kit.informatik.wincheck;

public class WinCheckHorizontal implements WinCheck {
    @Override
    public int alterI(int i, int offset, Direction direction) {
        return i;
    }

    @Override
    public int alterJ(int j, int offset, Direction direction) {
        switch (direction) {
            case DIRECTION_ONE:
                //Left
                return j - offset;
            case DIRECTION_TWO:
                //Right
                return j + offset;
            default:
                throw new IllegalArgumentException("You called the method with an impossible direction!");
        }
    }
}
