/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imagenes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Isaac
 */
public class ImagenProductoUtil {

    private static final String DIR_PRODUCTOS = "imagenes/productos";

    /**
     * Crea la carpeta si no existe.
     */
    public static void asegurarDirectorio() throws IOException {
        Path dir = Paths.get(DIR_PRODUCTOS);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }

    /**
     * Guarda la imagen como {id}.png
     */
    public static void guardarImagenProducto(int idProducto, File archivoImagen) throws IOException {
        if (archivoImagen == null) return;

        asegurarDirectorio();

        Path destino = Paths.get(DIR_PRODUCTOS, idProducto + ".png");

        Files.copy(
                archivoImagen.toPath(),
                destino,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    /**
     * Carga la imagen por id.
     */
    public static ImageIcon cargarIconoProducto(int idProducto, int w, int h) {
        try {
            File f = new File(DIR_PRODUCTOS + "/" + idProducto + ".png");
            if (!f.exists()) return null;

            BufferedImage original = ImageIO.read(f);
            if (original == null) return null;

            int ow = original.getWidth();
            int oh = original.getHeight();

            // Escalado manteniendo proporción (FIT)
            double sx = (double) w / ow;
            double sy = (double) h / oh;
            double s = Math.min(sx, sy);

            int nw = (int) Math.round(ow * s);
            int nh = (int) Math.round(oh * s);

            Image scaled = original.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            return null;
        }
    }
}