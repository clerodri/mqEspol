package unicam.pi.mqespol.data;

public class LocalBroker {
    private String host;
    private int port;
    private String username;

    public LocalBroker(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
