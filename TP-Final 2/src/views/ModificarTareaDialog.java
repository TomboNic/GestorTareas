package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La clase ModificarTareaDialog proporciona una interfaz gráfica para modificar los detalles de una tarea.
 * Esta clase extiende JDialog y permite al usuario editar el título, la persona asignada, el estado,
 * la prioridad, la fecha límite y la descripción de una tarea existente.
 *
 * <p>El diálogo se muestra de manera modal, lo que significa que bloquea la interacción con la ventana principal
 * hasta que el usuario confirma o cancela los cambios. Los componentes del diálogo incluyen campos de texto,
 * áreas de texto y cuadros combinados (comboboxes) para la entrada de datos.</p>
 *
 * <p>La clase también proporciona métodos para recuperar los valores modificados y determinar si el usuario
 * confirmó los cambios o canceló la operación.</p>
 *
 */
public class ModificarTareaDialog extends JDialog {
    private final JTextField tituloField;
    private JComboBox<String> asignadoAComboBox;
    private JComboBox<String> estadoComboBox;
    private final JComboBox<String> prioridadComboBox;
    private final JTextField fechaLimiteField;
    private final JTextArea descripcionArea;
    private boolean confirmado = false;
    private static final Color CUSTOM_COLOR = new Color(136, 171, 247);

    /**
     * Constructor para la clase ModificarTareaDialog.
     *
     * @param owner      Frame propietario del diálogo.
     * @param titulo     Título de la tarea.
     * @param asignadoA  Persona asignada a la tarea.
     * @param estado     Estado de la tarea.
     * @param prioridad  Prioridad de la tarea.
     * @param fechaLimite Fecha límite de la tarea.
     * @param descripcion Descripción de la tarea.
     */
    public ModificarTareaDialog(Frame owner, String titulo, String asignadoA, String estado, String prioridad, String fechaLimite, String descripcion) {
        super(owner, "Modificar Tarea", true);

        // Configuración del diseño y tamaño de la ventana
        setLayout(new BorderLayout(10, 10));
        setSize(400, 400);
        setLocationRelativeTo(owner);

        // Panel principal con GridBagLayout
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo para el título de la tarea
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Título:"), gbc);
        tituloField = new JTextField(20);
        tituloField.setBorder(new LineBorder(CUSTOM_COLOR));
        gbc.gridx = 1;
        panelPrincipal.add(tituloField, gbc);
        tituloField.setText(titulo);

        // ComboBox para seleccionar a quién está asignada la tarea
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Asignado a:"), gbc);
        asignadoAComboBox = new JComboBox<>();
        asignadoAComboBox.setBorder(new LineBorder(CUSTOM_COLOR));
        gbc.gridx = 1;
        panelPrincipal.add(asignadoAComboBox, gbc);
        asignadoAComboBox.setSelectedItem(asignadoA);

        // ComboBox para seleccionar el estado de la tarea
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Estado:"), gbc);
        estadoComboBox = new JComboBox<>(new String[]{"Pendiente", "En progreso", "Completada"});
        estadoComboBox.setBorder(new LineBorder(CUSTOM_COLOR));
        gbc.gridx = 1;
        panelPrincipal.add(estadoComboBox, gbc);
        estadoComboBox.setSelectedItem(estado);

        // ComboBox para seleccionar la prioridad de la tarea
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Prioridad:"), gbc);
        prioridadComboBox = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        prioridadComboBox.setBorder(new LineBorder(CUSTOM_COLOR));
        gbc.gridx = 1;
        panelPrincipal.add(prioridadComboBox, gbc);
        prioridadComboBox.setSelectedItem(prioridad);

        // Campo para la fecha límite de la tarea
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelPrincipal.add(new JLabel("Fecha Límite:"), gbc);
        fechaLimiteField = new JTextField(20);
        fechaLimiteField.setBorder(new LineBorder(CUSTOM_COLOR));
        gbc.gridx = 1;
        panelPrincipal.add(fechaLimiteField, gbc);
        fechaLimiteField.setText(fechaLimite);

        // Área de texto para la descripción de la tarea
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelPrincipal.add(new JLabel("Descripción:"), gbc);
        descripcionArea = new JTextArea(5, 20);
        descripcionArea.setBorder(new LineBorder(CUSTOM_COLOR));
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descripcionArea);
        gbc.gridx = 1;
        panelPrincipal.add(scrollPane, gbc);
        descripcionArea.setText(descripcion);

        add(panelPrincipal, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(CUSTOM_COLOR);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Configuración de botones
        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.setBackground(Color.WHITE);
        confirmarButton.setForeground(Color.BLACK);
        confirmarButton.setFocusPainted(false);
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmado = true;
                setVisible(false);
            }
        });
        panelBotones.add(confirmarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(Color.WHITE);
        cancelarButton.setForeground(Color.BLACK);
        cancelarButton.setFocusPainted(false);
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmado = false;
                setVisible(false);
            }
        });
        panelBotones.add(cancelarButton);

        add(panelBotones, BorderLayout.SOUTH);

        pack();
    }

    /**
     * Obtiene el título modificado de la tarea.
     *
     * @return El título de la tarea como una cadena.
     */
    public String getTitulo() {
        return tituloField.getText();
    }

    /**
     * Obtiene la persona asignada modificada de la tarea.
     *
     * @return El nombre de la persona asignada como una cadena.
     */
    public String getAsignadoA() {
        return (String) asignadoAComboBox.getSelectedItem();
    }

    /**
     * Obtiene el estado modificado de la tarea.
     *
     * @return El estado de la tarea como una cadena.
     */
    public String getEstado() {
        return (String) estadoComboBox.getSelectedItem();
    }

    /**
     * Obtiene la prioridad modificada de la tarea.
     *
     * @return La prioridad de la tarea como una cadena.
     */
    public String getPrioridad() {
        return (String) prioridadComboBox.getSelectedItem();
    }

    /**
     * Obtiene la fecha límite modificada de la tarea.
     *
     * @return La fecha límite de la tarea como una cadena.
     */
    public String getFechaLimite() {
        return fechaLimiteField.getText();
    }

    /**
     * Obtiene la descripción modificada de la tarea.
     *
     * @return La descripción de la tarea como una cadena.
     */
    public String getDescripcion() {
        return descripcionArea.getText();
    }

    /**
     * Verifica si el usuario confirmó los cambios en el diálogo.
     *
     * @return true si el usuario hizo clic en el botón "Confirmar", false si hizo clic en "Cancelar" o cerró el diálogo.
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Actualiza la lista de empleados disponibles en el cuadro combinado.
     *
     * @param nombresEmpleados Un array de cadenas que contiene los nombres de los empleados.
     */
    public void actualizarEmpleadosEnComboBox(String[] nombresEmpleados) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(nombresEmpleados);
        asignadoAComboBox.setModel(model);
    }
}
