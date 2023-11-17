package src;

public class PasswordsNoMatch extends PasswordException {
    public PasswordsNoMatch() {
        super("Passwords must match");
    }
}
