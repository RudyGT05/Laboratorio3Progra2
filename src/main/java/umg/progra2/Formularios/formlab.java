package umg.progra2.Formularios;

import umg.progra2.DataBase.Model.ProductoModel;
import umg.progra2.DataBase.Service.ProductoService;
import umg.progra2.Reportes.PdfReporte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class formlab extends JFrame {
    private JTextField txtDescripcion, txtOrigen, txtPrecio, txtExistencia, txtCodigo;
    private JButton buttonInsertar, buttonActualizar, buttonEliminar, buttonPDF, buttonExistencia, buttonPais, buttonPrecio, buttonAgrupado;
    private ProductoService productoService;

    public formlab() {
        productoService = new ProductoService(); // Inicializa el servicio
        setTitle("Gestión de Productos");
        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2));

        txtCodigo = new JTextField();
        txtDescripcion = new JTextField();
        txtOrigen = new JTextField();
        txtPrecio = new JTextField();
        txtExistencia = new JTextField();

        buttonInsertar = new JButton("Insertar Producto");
        buttonActualizar = new JButton("Actualizar Producto");
        buttonEliminar = new JButton("Eliminar Producto");
        buttonPDF = new JButton("Generar Reporte General");
        buttonExistencia = new JButton("Reporte Existencia < 20");
        buttonPais = new JButton("Reporte por País (Escribe el pais a buscar");
        buttonPrecio = new JButton("Reporte Precio > 2000");
        buttonAgrupado = new JButton("Reporte Agrupado por País");

        formPanel.add(new JLabel("Código:"));
        formPanel.add(txtCodigo);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(txtDescripcion);
        formPanel.add(new JLabel("Origen:"));
        formPanel.add(txtOrigen);
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(txtPrecio);
        formPanel.add(new JLabel("Existencia:"));
        formPanel.add(txtExistencia);
        formPanel.add(buttonInsertar);
        formPanel.add(buttonActualizar);
        formPanel.add(buttonEliminar);

        add(formPanel, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(5, 1));

        JLabel lblPais = new JLabel("País:");
        JTextField txtPais = new JTextField();

        filterPanel.add(lblPais);
        filterPanel.add(txtPais);
        filterPanel.add(buttonExistencia);
        filterPanel.add(buttonPrecio);
        filterPanel.add(buttonAgrupado);
        filterPanel.add(buttonPDF);
        filterPanel.add(buttonPais);

        add(filterPanel, BorderLayout.NORTH);

        buttonInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String descripcion = txtDescripcion.getText();
                    String origen = txtOrigen.getText();
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int existencia = Integer.parseInt(txtExistencia.getText());

                    productoService.agregarProducto(descripcion, origen, precio, existencia);

                    JOptionPane.showMessageDialog(null, "Producto agregado exitosamente.");
                    limpiarCampos(); // Limpia los campos después de agregar el producto
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar el producto: " + ex.getMessage());
                }
            }
        });

        buttonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigo = Integer.parseInt(txtCodigo.getText());
                    String descripcion = txtDescripcion.getText();
                    String origen = txtOrigen.getText();
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int existencia = Integer.parseInt(txtExistencia.getText());

                    productoService.actualizarProducto(codigo, descripcion, origen, precio, existencia);

                    JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente.");
                    limpiarCampos(); // Limpia los campos después de actualizar el producto
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + ex.getMessage());
                }
            }
        });

        buttonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigo = Integer.parseInt(txtCodigo.getText());
                    productoService.eliminarProducto(codigo);

                    JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.");
                    limpiarCampos(); // Limpia los campos después de eliminar el producto
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un código válido.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage());
                }
            }
        });

        buttonExistencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<ProductoModel> productosFiltrados = productoService.obtenerGenericos(
                            "existencia < 20 ORDER BY id_producto ASC");
                    new PdfReporte().generateProductReport(productosFiltrados, "C:/Users/Rudyg/Documents/report_existencia.pdf", false);
                    JOptionPane.showMessageDialog(null, "Reporte de existencia generado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
                }
            }
        });

        buttonAgrupado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<ProductoModel> productosFiltrados = productoService.obtenerAgruapdos(
                            "origen, id_producto ASC");
                    new PdfReporte().generateProductReport(productosFiltrados, "C:/Users/Rudyg/Documents/report_agrupado.pdf", false);
                    JOptionPane.showMessageDialog(null, "Reporte agrupado por país generado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
                }
            }
        });

        buttonPais.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pais = txtPais.getText();
                try {
                    List<ProductoModel> productosFiltrados = productoService.obtenerGenericos(
                            "origen = '" + pais + "' ORDER BY precio DESC");
                    new PdfReporte().generateProductReport(productosFiltrados, "C:/Users/Rudyg/Documents/report_pais.pdf", false);
                    JOptionPane.showMessageDialog(null, "Reporte por país generado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
                }
            }
        });

        buttonPrecio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<ProductoModel> productosFiltrados = productoService.obtenerGenericos(
                            "precio > 2000 ORDER BY precio DESC");
                    new PdfReporte().generateProductReport(productosFiltrados, "C:/Users/Rudyg/Documents/report_precio.pdf", false);
                    JOptionPane.showMessageDialog(null, "Reporte de precio generado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
                }
            }
        });

        buttonPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<ProductoModel> productos = productoService.obtenerAgruapdos(
                            "id_producto ASC"); // Asegúrate de que este es el ID correcto
                    new PdfReporte().generateProductReport(productos, "C:/Users/Rudyg/Documents/report_general.pdf", false);
                    JOptionPane.showMessageDialog(null, "Reporte general generado exitosamente");
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + exception.getMessage());
                }
            }
        });
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtOrigen.setText("");
        txtPrecio.setText("");
        txtExistencia.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            formlab formulario = new formlab();
            formulario.setVisible(true);
        });
    }
}

