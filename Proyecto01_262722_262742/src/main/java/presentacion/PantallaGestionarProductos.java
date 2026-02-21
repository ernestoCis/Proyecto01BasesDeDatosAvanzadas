/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;

/**
 *
 * @author Isaac
 */
public class PantallaGestionarProductos extends JFrame {

    public PantallaGestionarProductos(iUsuarioBO usuarioBO, iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO) {

        setTitle("Panadería - Gestionar productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        // Tarjeta central blanca
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

        // ---- TOP (flecha) ----
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelSuperior.add(btnBack, BorderLayout.WEST);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ---- CENTER ----
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Gestionar productos");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(8));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(18));

        // Tabla
        String[] columnas = {"ID", "Nombre", "Tipo", "Precio", "Estado", "Acción"};

        Object[][] datos = {
            {"1", "Concha Tradicional", "Dulce", "$16", "Activo", "Editar / Desactivar"},
            {"2", "Pan de Mantequilla", "Dulce", "$20", "Activo", "Editar / Desactivar"},
            {"3", "Cuernito Dulce", "Dulce", "$18", "Activo", "Editar / Desactivar"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo visual por ahora
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(680, 250));
        scrollPane.setMaximumSize(new Dimension(680, 250));
        scrollPane.setBorder(new LineBorder(new Color(60, 60, 60), 2));

        panelCentro.add(scrollPane);
        panelCentro.add(Box.createVerticalStrut(20));

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ---- SOUTH (botón + footer) ----
        JButton btnAgregar = crearBotonPequeno("Agregar producto");

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelInferior.add(btnAgregar, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ---- Acciones ----
        btnBack.addActionListener(e -> {
            new MenuEmpleado(usuarioBO, productoBO, cuponBO, pedidoBO).setVisible(true);
            this.dispose();
        });

        btnAgregar.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Abrir pantalla Agregar producto (pendiente)")
        );
    }

    private JButton crearBotonPequeno(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(170, 28));
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
