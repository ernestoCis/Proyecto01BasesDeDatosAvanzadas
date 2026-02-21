/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author Isaac
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;

public class MenuEmpleado extends JFrame {

    private iProductoBO productoBO;
    private iCuponBO cuponBO;
    private iPedidoBO pedidoBO;
    private iUsuarioBO usuarioBO;

    public MenuEmpleado(iUsuarioBO usuarioBO, iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO) {
        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;

        setTitle("Panadería - Menú Empleado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        panelPrincipal.setPreferredSize(new Dimension(760, 540));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        base.add(panelPrincipal, gbc);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Selecciona el tipo Acción:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(40));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(12));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(28));

        JButton btnGestionarPedidos = crearBotonGrande("Gestionar pedidos");
        btnGestionarPedidos.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnGestionarProductos = crearBotonGrande("Gestionar productos");
        btnGestionarProductos.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(btnGestionarPedidos);
        panelCentro.add(Box.createVerticalStrut(18));
        panelCentro.add(btnGestionarProductos);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));
        footer.setBorder(new EmptyBorder(0, 4, 0, 0));

        JButton btnCerrarSesion = crearBotonPequeno("Cerrar Sesión");

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelInferior.add(btnCerrarSesion, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ===== Navegación (por ahora solo abrir pantallas) =====
        btnGestionarPedidos.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Ir a Gestionar pedidos (pendiente pantalla)");
            // Cuando exista:
            // new PantallaGestionarPedidos().setVisible(true);
            // this.dispose();
        });

        btnGestionarProductos.addActionListener(e -> {
            new PantallaGestionarProductos(usuarioBO, productoBO, cuponBO, pedidoBO).setVisible(true);
            this.dispose();
        });
        btnCerrarSesion.addActionListener(e -> {
            new Menu(usuarioBO, productoBO, cuponBO, pedidoBO).setVisible(true);
            this.dispose();
        });
    }

    private JButton crearBotonGrande(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(320, 60));
        b.setMaximumSize(new Dimension(320, 60));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(10, 18, 10, 18)
        ));
        return b;
    }

    private JButton crearBotonPequeno(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 28));
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
}
