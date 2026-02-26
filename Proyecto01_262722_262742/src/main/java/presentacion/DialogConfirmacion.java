/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 * <h1>DialogConfirmacion</h1>
 *
 * <p>
 * Clase que representa un cuadro de diálogo modal personalizado para solicitar
 * confirmación al usuario.
 * </p>
 *
 * <p>
 * Este componente muestra un mensaje centrado y dos opciones:
 * <b>"Sí"</b> y <b>"No"</b>. El resultado se almacena internamente en una
 * variable booleana que puede ser consultada posteriormente.
 * </p>
 *
 * <h2>Características</h2>
 * <ul>
 * <li>Ventana modal (bloquea la ventana padre).</li>
 * <li>Diseño visual consistente con la aplicación.</li>
 * <li>Retorna un valor booleano indicando la decisión del usuario.</li>
 * </ul>
 *
 * <p>
 * Se utiliza comúnmente para confirmar acciones críticas como: eliminar
 * registros, cerrar sesión o cancelar pedidos.
 * </p>
 *
 * @author 262722, 2627242
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DialogConfirmacion extends JDialog {

    /**
     * Indica si el usuario confirmó la acción.
     * <p>
     * true → El usuario presionó "Sí".<br>
     * false → El usuario presionó "No" o cerró el diálogo.
     * </p>
     */
    private boolean confirmado = false;

    /**
     * <p>
     * Constructor que crea el diálogo de confirmación.
     * </p>
     *
     * <p>
     * Configura la ventana como modal, aplica estilos visuales personalizados y
     * agrega los botones de acción.
     * </p>
     *
     * @param owner ventana padre que invoca el diálogo
     * @param mensaje mensaje que se mostrará al usuario
     */
    public DialogConfirmacion(Window owner, String mensaje) {
        super(owner, "Confirmar", ModalityType.APPLICATION_MODAL);

        setSize(360, 200);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 18, 18, 18)
        ));
        card.setPreferredSize(new Dimension(320, 150));
        base.add(card);

        card.setLayout(new BorderLayout(0, 10));

        /**
         * Etiqueta que muestra el mensaje centrado utilizando HTML para
         * permitir alineación y mejor presentación.
         */
        JLabel lbl = new JLabel("<html><div style='text-align:center;'>" + mensaje + "</div></html>");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lbl, BorderLayout.CENTER);

        JButton btnSi = crearBoton("Sí");
        JButton btnNo = crearBoton("No");

        /**
         * Evento del botón "Sí". Establece confirmado en true y cierra el
         * diálogo.
         */
        btnSi.addActionListener(e -> {
            confirmado = true;
            dispose();
        });

        /**
         * Evento del botón "No". Establece confirmado en false y cierra el
         * diálogo.
         */
        btnNo.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        panelBtns.setOpaque(false);
        panelBtns.add(btnSi);
        panelBtns.add(btnNo);

        card.add(panelBtns, BorderLayout.SOUTH);
    }

    /**
     * <p>
     * Método auxiliar para crear botones con estilo uniforme dentro del
     * diálogo.
     * </p>
     *
     * @param text texto que mostrará el botón
     * @return botón configurado con estilos personalizados
     */
    private JButton crearBoton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(90, 32));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(2, 10, 2, 10)
        ));
        return b;
    }

    /**
     * Obtiene el resultado de la confirmación.
     *
     * @return true si el usuario confirmó, false en caso contrario
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * <p>
     * Método estático de conveniencia para mostrar el diálogo y obtener el
     * resultado en una sola línea de código.
     * </p>
     *
     * <p>
     * Ejemplo de uso:
     * </p>
     *
     * <pre>
     * boolean resultado = DialogConfirmacion.confirmar(this, "¿Desea eliminar el pedido?");
     * </pre>
     *
     * @param parent componente desde el cual se invoca el diálogo
     * @param mensaje mensaje que se mostrará
     * @return true si el usuario presiona "Sí", false si presiona "No"
     */
    public static boolean confirmar(Component parent, String mensaje) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        DialogConfirmacion d = new DialogConfirmacion(w, mensaje);
        d.setVisible(true);
        return d.isConfirmado();
    }
}
