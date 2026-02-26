package imagenes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;

/**
 * <b>Clase utilitaria para la gestión de imágenes de los productos.</b>
 * <p>Esta clase provee métodos estáticos para facilitar el almacenamiento, 
 * lectura y redimensionamiento de las fotografías de los productos de la panadería. 
 * En lugar de guardar los archivos pesados en la base de datos, los almacena 
 * de forma local en una carpeta específica, utilizando el ID del producto 
 * como nombre de archivo.</p>
 *
 * @author 262722
 * @author 262742
 */
public class ImagenProductoUtil {

    /**
     * Ruta relativa del directorio donde se almacenarán las imágenes de los productos.
     */
    private static final String DIR_PRODUCTOS = "imagenes/productos";

    /**
     * Verifica la existencia del directorio de imágenes y lo crea si no existe.
     * <p>Garantiza que la ruta física esté disponible antes de intentar guardar 
     * o leer cualquier archivo de imagen.</p>
     * * @throws IOException Si ocurre un error de permisos o de entrada/salida al crear la carpeta.
     */
    public static void asegurarDirectorio() throws IOException {
        Path dir = Paths.get(DIR_PRODUCTOS);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }

    /**
     * Guarda físicamente la imagen de un producto en el sistema de archivos.
     * <p>El archivo original se copia al directorio de imágenes y se renombra 
     * automáticamente con el formato <code>{idProducto}.png</code>. Si ya existe 
     * una imagen para ese ID, será reemplazada.</p>
     *
     * @param idProducto El identificador único del producto al que pertenece la imagen.
     * @param archivoImagen El archivo original de la imagen seleccionado por el usuario.
     * @throws IOException Si ocurre un error al copiar o escribir el archivo en el disco.
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
     * Carga y redimensiona la imagen asociada a un producto específico.
     * <p>Busca la imagen guardada como <code>{idProducto}.png</code> y la escala 
     * dinámicamente utilizando un algoritmo que mantiene la proporción original (FIT), 
     * evitando que la foto del producto se vea deformada en la interfaz gráfica.</p>
     *
     * @param idProducto El identificador único del producto cuya imagen se desea cargar.
     * @param w El ancho máximo (width) deseado para el icono.
     * @param h El alto máximo (height) deseado para el icono.
     * @return Un objeto <code>ImageIcon</code> listo para usarse en componentes Swing, 
     * o <code>null</code> si la imagen no existe o hubo un error al leerla.
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