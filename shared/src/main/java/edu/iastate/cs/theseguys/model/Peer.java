package edu.iastate.cs.theseguys.model;

public class Peer {

    private String ip;
    private int port;

    public Peer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
