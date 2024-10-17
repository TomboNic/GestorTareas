package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Clase TareasPMView representa la interfaz gráfica de usuario para un gestor de tareas de Project Manager.
 */
public class TareasPMView extends JFrame {
    private final JTextField nombreTareaField;
    private final JTextField descripcionField;
    private final JTextField fechaField;
    private final JButton borrarButton;
    private final JButton modificarButton;
    private final JTable tablaTareas;
    private final DefaultTableModel modeloTabla;
    private final JComboBox<String> asignadoAComboBox;
    private final JComboBox<String> prioridadBox;
    private final JTextField buscarField;
    private final JComboBox<String> filtroComboBox;
    private final JComboBox<String> empleadoEstadisticasComboBox;
    private JLabel porcentajeTareasLabel;
    private final List<ActionListener> listeners = new ArrayList<>();
    public static final int COL_TITULO = 0;
    public static final int COL_ASIGNADO = 1;
    public static final int COL_ESTADO = 2;
    public static final int COL_PRIORIDAD = 3;
    public static final int COL_FECHA = 4;
    public static final int COL_DESCRIPCION = 5;

    private static final String FILTRO_TITULO = "Título";
    private static final String FILTRO_FECHA = "Fecha Límite";
    private static final String FILTRO_PRIORIDAD = "Prioridad";
    private static final String FILTRO_ESTADO = "Estado";
    private static final String FILTRO_ASIGNADO = "Asignado a";

    /**
     * Constructor de la clase TareasPMView.
     * Configura la interfaz gráfica de usuario y sus componentes.
     */
    public TareasPMView() {
        // Configuración de la ventana
        setTitle("Gestor de Tareas - Project Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        // Panel principal y configuración de diseño
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(panelPrincipal);

        // Panel para crear tareas
        JPanel panelCrearTareas = new JPanel(new GridBagLayout());
        panelCrearTareas.setBackground(new Color(136, 171, 247));
        panelCrearTareas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Crear Tarea"));
        panelPrincipal.add(panelCrearTareas, BorderLayout.WEST);

        // Configuración del diseño del panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Inicialización de filas
        int fila = 0;
        // Añadir campos para crear tareas
        agregarCampo(panelCrearTareas, gbc, "Nombre:", nombreTareaField = new JTextField(), fila++);
        agregarCampo(panelCrearTareas, gbc, "Descripción:", descripcionField = new JTextField(), fila++);
        agregarCampo(panelCrearTareas, gbc, "Fecha Límite (DD/MM/YYYY):", fechaField = new JTextField(), fila++);
        agregarCampo(panelCrearTareas, gbc, "Prioridad:", prioridadBox = new JComboBox<>(new String[]{"Alta", "Media", "Baja"}), fila++);
        agregarCampo(panelCrearTareas, gbc, "Asignar a:", asignadoAComboBox = new JComboBox<>(new String[]{"Empleado 1", "Empleado 2", "Empleado 3"}), fila++);

        // Botón para crear tarea
        JButton crearButton = new JButton("Crear Tarea");
        crearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        crearButton.setBackground(new Color(218, 228, 247));
        crearButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCrearTareas.add(crearButton, gbc);

        // Botones para borrar y modificar tarea
        borrarButton = new JButton("Borrar Tarea");
        borrarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        borrarButton.setBackground(new Color(218, 228, 247));
        borrarButton.setForeground(Color.BLACK);
        modificarButton = new JButton("Modificar Tarea");
        modificarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        modificarButton.setBackground(new Color(218, 228, 247));
        modificarButton.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = fila + 1;
        gbc.gridwidth = 1;
        panelCrearTareas.add(borrarButton, gbc);
        gbc.gridx = 1;
        panelCrearTareas.add(modificarButton, gbc);

        // Panel vacío para ajuste de diseño
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(136, 171, 247));
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCrearTareas.add(emptyPanel, gbc);

        // Panel para listar tareas
        JPanel panelListaTareas = new JPanel(new BorderLayout());
        panelListaTareas.setBackground(new Color(240, 240, 240));
        panelListaTareas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Lista de Tareas"));
        panelPrincipal.add(panelListaTareas, BorderLayout.CENTER);

        // Modelo de tabla y configuración de tabla
        modeloTabla = new DefaultTableModel(new String[]{"Título", "Asignado a", "Estado", "Prioridad", "Fecha Límite", "Descripción"}, 0);
        tablaTareas = new JTable(modeloTabla);
        tablaTareas.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaTareas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaTareas.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        panelListaTareas.add(scrollPane, BorderLayout.CENTER);


        // Panel para búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.setBackground(new Color(240, 240, 240));
        buscarField = new JTextField(20);
        buscarField.setFont(new Font("Arial", Font.PLAIN, 12));
        filtroComboBox = new JComboBox<>(new String[]{"Título", "Asignado a", "Estado", "Prioridad", "Fecha Límite"});
        filtroComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        JButton mostrarTodasButton = new JButton("Mostrar todas");
        mostrarTodasButton.setFont(new Font("Arial", Font.PLAIN, 12));
        mostrarTodasButton.setBackground(new Color(218, 228, 247));
        mostrarTodasButton.setForeground(Color.BLACK);
        panelBusqueda.add(buscarField);
        panelBusqueda.add(filtroComboBox);
        panelBusqueda.add(mostrarTodasButton);
        panelListaTareas.add(panelBusqueda, BorderLayout.NORTH);

        // Panel para estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelEstadisticas.setBackground(new Color(240, 240, 240));
        panelEstadisticas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Estadísticas"));
        panelPrincipal.add(panelEstadisticas, BorderLayout.SOUTH);

        // Configuración del panel de estadísticas
        panelEstadisticas.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // ComboBox para seleccionar empleado en estadísticas
        empleadoEstadisticasComboBox = new JComboBox<>(new String[]{"Todos los empleados"});
        empleadoEstadisticasComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstadisticas.add(empleadoEstadisticasComboBox);

        // Botón para ver estadísticas
        JButton verEstadisticasButton = new JButton("Ver estadísticas");
        verEstadisticasButton.setFont(new Font("Arial", Font.PLAIN, 12));
        verEstadisticasButton.setBackground(new Color(218, 228, 247));
        verEstadisticasButton.setForeground(Color.BLACK);
        panelEstadisticas.add(verEstadisticasButton);

        // Label para mostrar el porcentaje de tareas completadas
        porcentajeTareasLabel = new JLabel("Porcentaje de tareas completadas: 0%");
        porcentajeTareasLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panelEstadisticas.add(porcentajeTareasLabel);

        // Clasificador de filas para la tabla
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tablaTareas.setRowSorter(sorter);

        // Acción del botón crear tarea
        crearButton.addActionListener(e -> {
            notifyListeners("crearTarea");
            nombreTareaField.setText("");
            descripcionField.setText("");
            fechaField.setText("");
        });

        // Acción del botón borrar tarea
        borrarButton.addActionListener(e -> {
            int filaSeleccionada = tablaTareas.getSelectedRow();
            if (filaSeleccionada != -1) {
                // Convertir el índice de la fila seleccionada al índice del modelo
                filaSeleccionada = tablaTareas.convertRowIndexToModel(filaSeleccionada);
                String nombreTarea = (String) modeloTabla.getValueAt(filaSeleccionada, COL_TITULO);
                int confirmacion = JOptionPane.showConfirmDialog(
                        TareasPMView.this,
                        "¿Estás seguro de que quieres borrar la tarea '" + nombreTarea + "'?",
                        "Confirmar borrado",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmacion == JOptionPane.YES_OPTION) {
                    notifyListeners("borrarTarea");
                }
            } else {
                JOptionPane.showMessageDialog(
                        TareasPMView.this,
                        "Por favor, selecciona una tarea para borrar.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        // Acción del botón ver estadísticas
        verEstadisticasButton.addActionListener(e -> {
            notifyListeners("verEstadisticas");
        });

        // Acción del botón mostrar todas las tareas
        mostrarTodasButton.addActionListener(e -> buscarTareas("", ""));

        // Acción del campo de búsqueda
        buscarField.addActionListener(e -> buscarTareas(buscarField.getText(), Objects.requireNonNull(filtroComboBox.getSelectedItem()).toString()));
        setVisible(true);

        // Detectar clics en la tabla para habilitar botones de borrar y modificar
        tablaTareas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaTareas.getSelectedRow();
                if (filaSeleccionada != -1) {
                    borrarButton.setEnabled(true);
                    modificarButton.setEnabled(true);
                }
            }
        });

        // Acción del botón modificar tarea
        modificarButton.addActionListener(e -> {
            notifyListeners("modificarTarea");
        });
    }

    /**
     * Método para agregar campo en el panel con GridBagLayout.
     *
     * @param panel El panel al que se agrega el campo.
     * @param gbc El objeto GridBagConstraints para el layout.
     * @param etiqueta La etiqueta del campo.
     * @param componente El componente del campo.
     * @param fila El número de fila en el que se coloca el campo.
     */
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, Component componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel etiquetaLabel = new JLabel(etiqueta);
        etiquetaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(etiquetaLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        componente.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(componente, gbc);
    }

    /**
     * Actualiza la lista de empleados en el ComboBox de asignación.
     *
     * @param nombresEmpleados Un arreglo de cadenas con los nombres de los empleados.
     */
    public void actualizarEmpleadosEnComboBox(String[] nombresEmpleados) {
        DefaultComboBoxModel<String> modelCrearTareas = new DefaultComboBoxModel<>(nombresEmpleados);
        asignadoAComboBox.setModel(modelCrearTareas);

        String[] opcionesEstadisticas = new String[nombresEmpleados.length + 1];
        opcionesEstadisticas[0] = "Todos los empleados";
        System.arraycopy(nombresEmpleados, 0, opcionesEstadisticas, 1, nombresEmpleados.length);
        DefaultComboBoxModel<String> modelEstadisticas = new DefaultComboBoxModel<>(opcionesEstadisticas);
        empleadoEstadisticasComboBox.setModel(modelEstadisticas);
    }

    /**
     * Actualiza el porcentaje de tareas completadas.
     *
     * @param porcentaje El porcentaje de tareas completadas.
     */
    public void actualizarPorcentajeTareas(double porcentaje) {
        porcentajeTareasLabel.setText(String.format("Porcentaje de tareas completadas por %s: %.2f%%",
                empleadoEstadisticasComboBox.getSelectedItem(), porcentaje));
    }

    /**
     * Agrega una lista de tareas a la tabla.
     *
     * @param tareas Un arreglo bidimensional de cadenas con los datos de las tareas.
     */
    public void agregarListaTareaATabla(String[][] tareas) {
        DefaultTableModel nuevoModelo = (DefaultTableModel) tablaTareas.getModel();
        nuevoModelo.setRowCount(0);

        for (String[] tarea : tareas) {
            if (tarea.length == 6) {
                nuevoModelo.addRow(tarea);
            } else {
                System.err.println("Error en los datos de la tarea: " + Arrays.toString(tarea));
            }
        }
    }

    /**
     * Agrega una tarea a la tabla.
     *
     * @param Titulo El título de la tarea.
     * @param fechaLimite La fecha límite de la tarea.
     * @param prioridad La prioridad de la tarea.
     * @param estado El estado de la tarea.
     * @param asignadoA La persona a la que se asigna la tarea.
     * @param descripcion La descripción de la tarea.
     */
    public void agregarTareaATabla(String Titulo, String fechaLimite, String prioridad, String estado, String asignadoA, String descripcion) {
        modeloTabla.addRow(new Object[]{Titulo, asignadoA, estado, prioridad, fechaLimite, descripcion});
    }

    /**
     * Notifica a los listeners sobre un evento.
     *
     * @param evento El evento a notificar.
     */
    private void notifyListeners(String evento) {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, evento);
        for (ActionListener listener : listeners) {
            listener.actionPerformed(e);
        }
    }

    /**
     * Obtiene el valor del campo de nombre de tarea.
     *
     * @return El texto del campo de nombre de tarea.
     */
    public String getNombreTareaField() {
        return nombreTareaField.getText();
    }

    /**
     * Obtiene el valor del campo de descripción.
     *
     * @return El texto del campo de descripción.
     */
    public String getDescripcionField() {
        return descripcionField.getText();
    }

    /**
     * Obtiene el valor del campo de fecha.
     *
     * @return El texto del campo de fecha.
     */
    public String getFechaField() {
        return fechaField.getText();
    }

    /**
     * Obtiene el valor del ComboBox de prioridad.
     *
     * @return La opción seleccionada en el ComboBox de prioridad.
     */
    public String getPrioridadBox() {
        return Objects.requireNonNull(prioridadBox.getSelectedItem()).toString();
    }

    /**
     * Obtiene el valor del ComboBox de asignación.
     *
     * @return La opción seleccionada en el ComboBox de asignación.
     */
    public String getAsignadoAComboBox() {
        return Objects.requireNonNull(asignadoAComboBox.getSelectedItem()).toString();
    }

    /**
     * Obtiene el modelo de la tabla.
     *
     * @return El modelo de la tabla.
     */
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    /**
     * Obtiene la tabla de tareas.
     *
     * @return La tabla de tareas.
     */
    public JTable getTablaTareas() {
        return tablaTareas;
    }

    /**
     * Obtiene el empleado seleccionado para estadísticas.
     *
     * @return La opción seleccionada en el ComboBox de estadísticas.
     */
    public String getEmpleadoSeleccionadoParaEstadisticas() {
        return Objects.requireNonNull(empleadoEstadisticasComboBox.getSelectedItem()).toString();
    }

    /**
     * Agrega un listener para las opciones de acciones.
     *
     * @param listener El ActionListener a agregar.
     */
    public void addOpcionListener(ActionListener listener) {
        listeners.add(listener);
    }

    /**
     * Busca tareas en la tabla basado en un texto y un filtro.
     *
     * @param texto El texto a buscar.
     * @param filtro El filtro a aplicar en la búsqueda.
     */
    public void buscarTareas(String texto, String filtro) {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tablaTareas.getRowSorter();
        RowFilter<Object, Object> rowFilter = null;

        int columnaIndex;
        switch (filtro) {
            case FILTRO_TITULO:
                columnaIndex = COL_TITULO;
                rowFilter = RowFilter.regexFilter("(?i)" + texto, columnaIndex);
                break;
            case FILTRO_FECHA:
                columnaIndex = COL_FECHA;
                rowFilter = RowFilter.regexFilter(".*" + texto + ".*", columnaIndex);
                break;
            case FILTRO_PRIORIDAD:
                columnaIndex = COL_PRIORIDAD;
                rowFilter = new RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        String value = entry.getStringValue(columnaIndex);
                        return value.equalsIgnoreCase(texto);
                    }
                };
                break;
            case FILTRO_ESTADO:
                columnaIndex = COL_ESTADO;
                rowFilter = new RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry<? extends Object, ? extends Object> entry) {
                        String value = entry.getStringValue(columnaIndex);
                        return value.equalsIgnoreCase(texto);
                    }
                };
                break;
            case FILTRO_ASIGNADO:
                columnaIndex = COL_ASIGNADO;
                rowFilter = RowFilter.regexFilter("(?i)" + texto, columnaIndex);
                break;
            default:
                columnaIndex = COL_TITULO;
                rowFilter = RowFilter.regexFilter("(?i)" + texto, columnaIndex);
        }

        sorter.setRowFilter(rowFilter);
    }
}
