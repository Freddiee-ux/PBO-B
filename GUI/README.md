# **latihan GUI**

<img width="983" height="937" alt="image" src="https://github.com/user-attachments/assets/049be7d7-58c3-41eb-a77b-8ccbec8c1d13" />
<img width="1084" height="1584" alt="image" src="https://github.com/user-attachments/assets/5e4c0a94-5ea4-4baf-ba02-deb857ce9dc8" />

Pada latihan GUI kedua, saya membuat sebuah aplikasi **Simple Image Viewer** menggunakan Java Swing dan AWT. Aplikasi ini berfungsi untuk **membuka file gambar**, menampilkannya, serta memberikan efek sederhana seperti **darker** dan **lighter**.

Program ini terdiri dari beberapa kelas:

1. **OFImage** → Kelas untuk memanipulasi pixel pada gambar (meneruskan BufferedImage)
2. **ImageFileManager** → Kelas untuk memuat gambar dari file
3. **ImagePanel** → Komponen GUI untuk menampilkan gambar
4. **ImageViewer** → Jendela utama yang berisi menu dan tombol aksi

Konsep yang digunakan pada latihan ini meliputi:

* **Swing GUI** (JFrame, JMenu, JButton, JFileChooser, JPanel)
* **Polymorphism paintComponent()**
* **Image processing** dengan manipulasi pixel
* **Event handling** (ActionListener) untuk menghubungkan tombol dengan operasi gambar

Aplikasi memberikan pengalaman dasar dalam membangun aplikasi grafis menggunakan Swing, termasuk pemrosesan event, layout management, dan pengolahan gambar.

---

# **Source Code**

## ✅ **1. OFImage.java**

```java
import java.awt.*;
import java.awt.image.*;

public class OFImage extends BufferedImage
{
    public OFImage(BufferedImage image) {
        super(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);
    }

    public OFImage(int width, int height) {
        super(width, height, TYPE_INT_RGB);
    }

    public void setPixel(int x, int y, Color col) {
        setRGB(x, y, col.getRGB());
    }

    public Color getPixel(int x, int y) {
        return new Color(getRGB(x, y));
    }

    public void darker() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Color c = getPixel(x, y);
                setPixel(x, y, c.darker());
            }
        }
    }

    public void lighter() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Color c = getPixel(x, y);
                setPixel(x, y, c.brighter());
            }
        }
    }
}
```

---

## ✅ **2. ImageFileManager.java**

```java
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class ImageFileManager
{
    public static OFImage loadImage(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            return img != null ? new OFImage(img) : null;
        } catch (IOException e) {
            return null;
        }
    }
}
```

---

## ✅ **3. ImagePanel.java**

```java
import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel
{
    private OFImage image;

    public void setImage(OFImage img) {
        image = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }
}
```

---

## ✅ **4. ImageViewer.java (Main Program)**

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ImageViewer
{
    private JFrame frame;
    private ImagePanel imagePanel;
    private OFImage currentImage;

    public static void main(String[] args) {
        new ImageViewer().buildGUI();
    }

    public void buildGUI() {
        frame = new JFrame("Simple Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imagePanel = new ImagePanel();

        JButton openBtn = new JButton("Open");
        JButton darkBtn = new JButton("Darker");
        JButton lightBtn = new JButton("Lighter");

        openBtn.addActionListener(new OpenListener());
        darkBtn.addActionListener(e -> applyDarker());
        lightBtn.addActionListener(e -> applyLighter());

        JPanel buttons = new JPanel();
        buttons.add(openBtn);
        buttons.add(darkBtn);
        buttons.add(lightBtn);

        frame.add(imagePanel, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                currentImage = ImageFileManager.loadImage(file);

                if (currentImage != null) {
                    imagePanel.setImage(currentImage);
                } else {
                    JOptionPane.showMessageDialog(frame, 
                        "Failed to load image.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void applyDarker() {
        if (currentImage != null) {
            currentImage.darker();
            imagePanel.repaint();
        }
    }

    private void applyLighter() {
        if (currentImage != null) {
            currentImage.lighter();
            imagePanel.repaint();
        }
    }
}
```
