package spaceman.client.view.game;

import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.Spaceman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

class ImageDisplay extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 4230162211355981591L;

    private static final String IMAGE_FILE_NAME = "saucer.png";

    private final ImageIcon fullImage;
    private final int maxCountdownValue;

    private int currentCountdownValue;

    ImageDisplay(final Spaceman model, final Image spacemanImage) {
        model.addPropertyChangeListener(this);
        fullImage = new ImageIcon(spacemanImage);
        GameState state = model.getState();
        maxCountdownValue = state.getMaximumCountdownValue();
        adjustTo(state);
    }

    public static ImageDisplay create(final Spaceman model) {
        try (final InputStream imageStream = ImageDisplay.class.getResourceAsStream(IMAGE_FILE_NAME)) {

            Objects.requireNonNull(imageStream);
            return new ImageDisplay(model, ImageIO.read(imageStream));

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("GameState")) {
            adjustTo((GameState) evt.getNewValue());
        }
    }

    private void adjustTo(final GameState state) {
        currentCountdownValue = state.getCountdownValue();
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Dimension targetDimension = getTargetImageDimensions();
        final int targetWidth = targetDimension.width;
        final int targetHeight = targetDimension.height;

        final Image imageToDraw = scaleImage(fullImage.getImage(), targetWidth, targetHeight);

        final int imagePieces = maxCountdownValue;
        final int imagePiecesToShow = maxCountdownValue - currentCountdownValue;
        final int startX = 0;
        final int startY = 0;
        final int clipWidth = targetWidth;
        final int clipHeight = targetHeight / imagePieces * imagePiecesToShow;
        g.clipRect(startX, startY, clipWidth, clipHeight);

        g.drawImage(imageToDraw, startX, startY, this);
    }

    private Dimension getTargetImageDimensions() {
        final int imageWidth = fullImage.getIconWidth();
        final int imageHeight = fullImage.getIconHeight();
        final float aspectRatio = ((float) imageWidth) / imageHeight;

        final int availableWidth = getWidth();
        final int availableHeight = getHeight();

        final float ratioAvailable = ((float) availableWidth) / availableHeight;
        final int targetWidth;
        final int targetHeight;
        if (ratioAvailable > aspectRatio) {
            // more width available than necessary; height is the important factor
            if (availableHeight < fullImage.getIconHeight()) {
                targetHeight = availableHeight;
                targetWidth = (int) (targetHeight * aspectRatio);
            } else {
                targetHeight = imageHeight;
                targetWidth = imageWidth;
            }
        } else {
            // more height available than necessary ; width is the important factor
            if (availableWidth < fullImage.getIconWidth()) {
                targetWidth = availableWidth;
                targetHeight = (int) (targetWidth / aspectRatio);
            } else {
                targetHeight = imageHeight;
                targetWidth = imageWidth;
            }
        }

        return new Dimension(targetWidth, targetHeight);
    }

    private Image scaleImage(final Image original, int targetWidth, int targetHeight) {
        BufferedImage scaled =
                new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D scalingGraphics = scaled.createGraphics();
        scalingGraphics.drawImage(original, 0, 0, targetWidth, targetHeight, this);
        scalingGraphics.dispose();
        return scaled;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(fullImage.getIconWidth(), fullImage.getIconHeight());
    }
}
