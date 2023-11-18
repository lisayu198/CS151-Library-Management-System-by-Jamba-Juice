package src.loginUI;

public class PasswordsNoMatch extends PasswordException {
    public PasswordsNoMatch() {
        super("Passwords must match");
    }
}
