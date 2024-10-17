package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Clase TareasEmpleadoView representa la interfaz gráfica de usuario para gestionar las tareas de un empleado.
 */
public class TareasEmpleadoView extends JFrame {
    private JList<String> listaPorHacer;
    private JList<String> listaEnProgreso;
    private JList<String> listaCompletadas;
    private DefaultListModel<String> modeloPorHacer;
    private DefaultListModel<String> modeloEnProgreso;
    private DefaultListModel<String> modeloCompletadas;

    /**
     * Constructor de la clase TareasEmpleadoView.
     * Configura la interfaz gráfica de usuario y sus componentes.
     */
    public TareasEmpleadoView() {
        setTitle("Tareas del Empleado");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloPorHacer = new DefaultListModel<>();
        modeloEnProgreso = new DefaultListModel<>();
        modeloCompletadas = new DefaultListModel<>();

        listaPorHacer = new JList<>(modeloPorHacer);
        listaEnProgreso = new JList<>(modeloEnProgreso);
        listaCompletadas = new JList<>(modeloCompletadas);

        // Estilo de las listas
        configurarEstiloLista(listaPorHacer);
        configurarEstiloLista(listaEnProgreso);
        configurarEstiloLista(listaCompletadas);

        TareaTransferHandler transferHandler = new TareaTransferHandler();
        configurarDragAndDrop(listaPorHacer, transferHandler);
        configurarDragAndDrop(listaEnProgreso, transferHandler);
        configurarDragAndDrop(listaCompletadas, transferHandler);

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Márgenes
        panel.add(crearPanelConTitulo("Pendiente", listaPorHacer));
        panel.add(crearPanelConTitulo("En progreso", listaEnProgreso));
        panel.add(crearPanelConTitulo("Completada", listaCompletadas));

        // Encabezado
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(136, 171, 247)); // Color #88ABF7FF
        JLabel headerLabel = new JLabel("Tareas", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Añadir espacio alrededor del encabezado
        headerPanel.add(headerLabel);

        // Layout principal
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Configura el estilo de una lista JList.
     *
     * @param list La lista a la que se le aplicará el estilo.
     */
    private void configurarEstiloLista(JList<String> list) {
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        list.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(136, 171, 247), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));
    }

    /**
     * Configura el arrastrar y soltar (drag and drop) para una lista JList.
     *
     * @param list La lista a configurar.
     * @param handler El manejador de transferencia para el drag and drop.
     */
    private void configurarDragAndDrop(JList<String> list, TransferHandler handler) {
        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(handler);
    }

    /**
     * Crea un panel con un título y un componente.
     *
     * @param titulo El título del panel.
     * @param componente El componente a agregar al panel.
     * @return El panel creado.
     */
    private JPanel crearPanelConTitulo(String titulo, JComponent componente) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(titulo, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(136, 171, 247));
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(componente), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(136, 171, 247), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));
        panel.setBackground(new Color(245, 245, 245)); // Fondo claro para los paneles
        return panel;
    }

    /**
     * Importa una lista de tareas y las distribuye en las listas correspondientes.
     *
     * @param tareas Un arreglo bidimensional de cadenas con los datos de las tareas.
     */
    public void importarListaTareas(String[][] tareas) {
        modeloPorHacer.clear();
        modeloEnProgreso.clear();
        modeloCompletadas.clear();

        for (String[] tarea : tareas) {
            if (tarea.length >= 2) {
                String nombre = tarea[0];
                String estado = tarea[1];
                switch (estado) {
                    case "Pendiente":
                        modeloPorHacer.addElement(nombre);
                        break;
                    case "En progreso":
                        modeloEnProgreso.addElement(nombre);
                        break;
                    case "Completada":
                        modeloCompletadas.addElement(nombre);
                        break;
                }
            } else {
                System.err.println("Error: El array 'tarea' no tiene suficientes elementos");
            }
        }
    }

    /**
     * Clase interna TareaTransferHandler que maneja la transferencia de datos para el drag and drop.
     */
    private class TareaTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JList<String> list = (JList<String>) c;
            return new StringSelection(list.getSelectedValue());
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) return false;

            try {
                String nombreTarea = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                JList<String> targetList = (JList<String>) support.getComponent();
                DefaultListModel<String> targetModel = (DefaultListModel<String>) targetList.getModel();

                String targetState = getEstadoFromList(targetList);
                if (targetState == null) return false;

                // Obtener el estado actual de la tarea antes de moverla
                String estadoAnterior = getEstadoAnterior(nombreTarea);

                if (eliminarTareaDeLista(nombreTarea, modeloPorHacer)) {
                    // La tarea estaba en la lista de pendientes
                    if (!"Pendiente".equals(targetState)) {
                        modeloPorHacer.removeElement(nombreTarea);
                        modeloPorHacer.removeElement(nombreTarea); // Necesario debido a un bug en DefaultListModel
                    }
                } else if (eliminarTareaDeLista(nombreTarea, modeloEnProgreso)) {
                    // La tarea estaba en la lista de en progreso
                    if (!"En progreso".equals(targetState)) {
                        modeloEnProgreso.removeElement(nombreTarea);
                        modeloEnProgreso.removeElement(nombreTarea); // Necesario debido a un bug en DefaultListModel
                    }
                } else if (eliminarTareaDeLista(nombreTarea, modeloCompletadas)) {
                    // La tarea estaba en la lista de completadas
                    if (!"Completada".equals(targetState)) {
                        modeloCompletadas.removeElement(nombreTarea);
                        modeloCompletadas.removeElement(nombreTarea); // Necesario debido a un bug en DefaultListModel
                    }
                } else {
                    // La tarea no estaba en ninguna lista, probablemente un error
                    return false;
                }

                // Añadir la tarea a la nueva lista
                int index = dl.getIndex();
                if (index < 0 || index >= targetModel.size()) {
                    targetModel.addElement(nombreTarea);
                } else {
                    targetModel.add(index, nombreTarea);
                }

                // Disparar el evento de tarea movida con el estado anterior correcto
                firePropertyChange("tareaMovida", null, new String[]{nombreTarea, estadoAnterior, targetState});
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Obtiene el estado anterior de una tarea dado su nombre.
         *
         * @param nombreTarea El nombre de la tarea.
         * @return El estado anterior de la tarea.
         */
        private String getEstadoAnterior(String nombreTarea) {
            if (modeloPorHacer.contains(nombreTarea)) {
                return "Pendiente";
            } else if (modeloEnProgreso.contains(nombreTarea)) {
                return "En progreso";
            } else if (modeloCompletadas.contains(nombreTarea)) {
                return "Completada";
            }
            return null; // La tarea no se encuentra en ninguna lista
        }

        /**
         * Obtiene el estado correspondiente a una lista dada.
         *
         * @param list La lista de la que se obtiene el estado.
         * @return El estado correspondiente a la lista.
         */
        private String getEstadoFromList(JList<String> list) {
            if (list == listaPorHacer) return "Pendiente";
            if (list == listaEnProgreso) return "En progreso";
            if (list == listaCompletadas) return "Completada";
            return null;
        }

        /**
         * Elimina una tarea de una lista dado su modelo.
         *
         * @param tarea El nombre de la tarea.
         * @param modelo El modelo de la lista.
         * @return true si la tarea fue eliminada, false en caso contrario.
         */
        private boolean eliminarTareaDeLista(String tarea, DefaultListModel<String> modelo) {
            int index = modelo.indexOf(tarea);
            if (index != -1) {
                modelo.remove(index);
                return true;
            }
            return false;
        }
    }

    /**
     * Agrega un listener para el evento de mover tareas.
     *
     * @param listener El ActionListener a agregar.
     */
    public void addTareaMovidaListener(ActionListener listener) {
        addPropertyChangeListener("tareaMovida", evt -> {
            String[] info = (String[]) evt.getNewValue();
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "tareaMovida|" + info[0] + "|" + info[1] + "|" + info[2]));
        });
    }
}
