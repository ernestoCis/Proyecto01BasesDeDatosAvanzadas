/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imagenes;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

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
    public static ImageIcon cargarIconoProducto(int idProducto) {
        File f = Paths.get(DIR_PRODUCTOS, idProducto + ".png").toFile();

        if (f.exists()) {
            return new ImageIcon(f.getPath());
        }

        return null;
    }
}