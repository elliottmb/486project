package edu.iastate.cs.theseguys.model;

import java.io.Serializable;

/**
 * Class representing a Peer in the network
 *
 */
public class Peer implements Serializable {
    private static final long serialVersionUID = 8990653320527676216L;
    private String ip;
    private int port;

    /**
     * Creates a Peer with the given ip and port number
     * @param ip
     * @param port
     */
    public Peer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Gets the int port
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port with the given int port
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the String ip
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the ip with the given string ip
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Returns the ip and port as a Peer string
     */
    @Override
    public String toString() {
        return "Peer{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
