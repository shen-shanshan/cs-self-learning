public class VersionControl {
    public int bad;

    public VersionControl(int bad) {
        this.bad = bad;
    }

    boolean isBadVersion(int version) {
        if (version == bad) {
            return true;
        } else {
            return false;
        }
    }
}
