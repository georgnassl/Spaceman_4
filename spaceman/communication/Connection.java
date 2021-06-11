package spaceman.communication;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Contains a socket and streams on it. The class serves as a container for 'interfaces' to an
 * established connection.
 */
public class Connection implements Closeable {
  private final Socket socket;
  private final ObjectInputStream reader;
  private final ObjectOutputStream writer;

  /**
   * Reads a serializable object on the socket stream.
   *
   * @return The object
   * @throws IOException If there are connection errors
   */
  public Object readObject() throws IOException {
    try {
      return reader.readObject();
    } catch (ClassNotFoundException e) {
      close();
      throw new AssertionError(e);
    }
  }

  /**
   * Writes a serializable object on the socket stream.
   *
   * @param object The object to write
   * @throws IOException If there are connection errors.
   */
  public void writeObject(Object object) throws IOException {
    writer.reset();
    writer.writeObject(object);
    writer.flush();
  }

  /**
   * Creates a new Connection object holding the given socket and streams.
   *
   * @param socket The socket of the connection.
   */
  public Connection(Socket socket) throws IOException {
    this.socket = socket;
    writer = new ObjectOutputStream(socket.getOutputStream());
    reader = new ObjectInputStream(socket.getInputStream());
  }

  @Override
  public void close() throws IOException {
    socket.close();
  }
}
