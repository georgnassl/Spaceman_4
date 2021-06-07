package spaceman.client;

import javax.swing.SwingUtilities;
import spaceman.client.controller.Controller;
import spaceman.client.controller.SwingController;

public class Main {

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(Main::showSpacemanGui);
  }

  private static void showSpacemanGui() {
    Controller controller = new SwingController();
    controller.start();
  }
}
