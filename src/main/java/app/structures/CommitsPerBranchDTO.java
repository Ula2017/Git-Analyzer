package app.structures;

/**
 * Created by Karol on 2018-01-18.
 */
public class CommitsPerBranchDTO {
    private String branchName;
    private int value;

    public CommitsPerBranchDTO(String branchName, int value) {
        this.branchName = branchName;
        this.value = value;
    }

    public String getBranchName() {
        return branchName;
    }

    public int getValue() {
        return value;
    }
}
