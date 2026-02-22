/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author Isaac
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DialogConfirmacion extends JDialog {

    private boolean confirmado = false;

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

        JLabel lbl = new JLabel("<html><div style='text-align:center;'>" + mensaje + "</div></html>");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lbl, BorderLayout.CENTER);

        JButton btnSi = crearBoton("Sí");
        JButton btnNo = crearBoton("No");

        btnSi.addActionListener(e -> {
            confirmado = true;
            dispose();
        });

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

    public boolean isConfirmado() {
        return confirmado;
    }

    public static boolean confirmar(Component parent, String mensaje) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        DialogConfirmacion d = new DialogConfirmacion(w, mensaje);
        d.setVisible(true);
        return d.isConfirmado();
    }
}