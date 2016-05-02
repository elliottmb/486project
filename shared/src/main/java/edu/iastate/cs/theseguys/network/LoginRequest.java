package edu.iastate.cs.theseguys.network;

/**
 * AbstractMessage extension used to request a login
 */
public class LoginRequest extends AbstractMessage {
    private static final long serialVersionUID = -100458915682459958L;
    private final String username;
    private final String password;
    private final int port;

    /**
     * Creates a LoginRequest with the given username, password, and port number
     * @param username
     * @param password
     * @param port
     */
    public LoginRequest(String username, String password, int port) {
        this.username = username;
        this.password = password;
        this.port = port;
    }

    /**
     * Returns the username, password, and port as a LoginRequest string
     */
    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                '}';
    }

    /**
     * Returns the String username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the String password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the int port
     * @return port
     */
    public int getPort() {
        return port;
    }

}
