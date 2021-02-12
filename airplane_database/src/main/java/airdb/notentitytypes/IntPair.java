package airdb.notentitytypes;

import java.util.Objects;

public class IntPair {
    private final int arg1;
    private final int arg2;

    public IntPair(int arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public int getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair intPair = (IntPair) o;
        return arg1 == intPair.arg1 &&
                arg2 == intPair.arg2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arg1, arg2);
    }
}
