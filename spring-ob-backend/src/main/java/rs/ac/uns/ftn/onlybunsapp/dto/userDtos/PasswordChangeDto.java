package rs.ac.uns.ftn.onlybunsapp.dto.userDtos;

public class PasswordChangeDto {
    String newPassword;
    String oldPassword;

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
