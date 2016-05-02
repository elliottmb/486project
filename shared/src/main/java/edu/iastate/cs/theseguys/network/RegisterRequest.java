package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used for requesting registration of a new user
 *
 */
public class RegisterRequest extends AbstractMessage {
    private static final long serialVersionUID = -7803515103821448698L;
    private final String username;
    private final String password;

    /**
     * Creates a new RegisterRequest with the given username and password
     * @param username
     * @param password
     */
    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets current username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets current password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username and password as a register request
     */
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
