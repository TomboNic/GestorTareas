import model.Archivos;
import model.Tareas;
import views.*;
import static views.TareasPMView.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * La clase Controller gestiona la interacción entre la interfaz de usuario (vistas)
 * y el modelo de datos. Maneja las acciones del usuario, actualiza las vistas e interactúa
 * con el almacenamiento de datos.
 *
 * @author Tu Nombre
 * @version 1.0
 * @since 2023-06-09
 */
public class Controller implements ActionListener {

    private static final String USUARIOS_FILE = "src/usuarios.csv";
    private static final String TAREAS_FILE = "src/tareas.csv";

    private LoginView loginView;
    private CrearUsrView crearUsrView;
    private TareasPMView tareasPMView;
    private TareasEmpleadoView tareasEmpleadoView;
    private DefaultTableModel modeloTabla;

    public Controller() {
        iniciarAplicacion();
    }

    /**
     * Inicializa la aplicación configurando la vista de login.
     */
    private void iniciarAplicacion() {
        loginView = new LoginView();
        loginView.addOpcionListener(this);
    }

    /**
     * Maneja los eventos de acción desencadenados por componentes en las vistas.
     *
     * @param e el ActionEvent que ocurrió
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "ingresar":
                manejarIngresoUsuario();
                break;
            case "registrarse":
                manejarRegistroUsuario();
                break;
            case "crearUsuario":
                manejarCreacionUsuario();
                break;
            case "crearTarea":
                manejarCreacionTarea();
                break;
            case "borrarTarea":
                try {
                    manejarEliminacionTarea();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "modificarTarea":
                manejarModificacionTarea();
                break;
            case "verEstadisticas":
                manejarEstadisticas();
                break;
        }
    }

    /**
     * Maneja el login de usuario verificando las credenciales y mostrando las vistas apropiadas.
     */
    private void manejarIngresoUsuario() {
        String tipoUsuario = verificarCredenciales();

        switch (tipoUsuario) {
            case "Empleado":
                String nombreUsuario = loginView.getUsuarioField();
                mostrarVistaEmpleado(nombreUsuario);
                break;
            case "Project Manager":
                mostrarVistaTareasPM();
                break;
            case "ninguno":
                mostrarMensajeError("Credenciales inválidas");
                break;
        }
    }

    /**
     * Verifica las credenciales del usuario contra los datos de usuarios almacenados.
     *
     * @return el tipo de usuario si las credenciales son válidas, "ninguno" en caso contrario
     */
    private String verificarCredenciales() {
        String usuario = loginView.getUsuarioField();
        String contrasenia = loginView.getContraseniaField();
        List<String[]> usuarios = Archivos.getListaUsuarios(USUARIOS_FILE);

        for (String[] u : usuarios) {
            if (u[0].equals(usuario) && u[1].equals(contrasenia)) {
                loginView.dispose();
                return u[2];
            }
        }
        return "ninguno";
    }

    /**
     * Muestra la vista de tareas para un Project Manager.
     */
    private void mostrarVistaTareasPM() {
        tareasPMView = new TareasPMView();
        tareasPMView.addOpcionListener(this);
        tareasPMView.setVisible(true);

        manejarEstadisticas();

        String[] nombresEmpleados = Archivos.getNombreUsuarios(USUARIOS_FILE);
        tareasPMView.actualizarEmpleadosEnComboBox(nombresEmpleados);

        SwingUtilities.invokeLater(() -> {
            String[][] listaTareas = Archivos.getListaTareas(TAREAS_FILE);
            tareasPMView.agregarListaTareaATabla(listaTareas);
        });
    }

    /**
     * Muestra la vista de tareas para un empleado.
     *
     * @param usuario el nombre de usuario del empleado
     */
    private void mostrarVistaEmpleado(String usuario) {
        tareasEmpleadoView = new TareasEmpleadoView();
        tareasEmpleadoView.setVisible(true);
        List<Tareas> tareas = Archivos.getTasksByAssignedUser(TAREAS_FILE, usuario);
        actualizarTareasEmpleado(tareas);

        tareasEmpleadoView.addTareaMovidaListener(e -> {
            String[] parts = e.getActionCommand().split("\\|");
            String nombreTarea = parts[1];
            String estadoAnterior = parts[2];
            String nuevoEstado = parts[3];
            System.out.println("Tarea '" + nombreTarea + "' movida del estado '" + estadoAnterior + "' al estado '" + nuevoEstado + "'");

            Archivos.actualizarEstadoTarea(TAREAS_FILE, nombreTarea, nuevoEstado);

            // Volver a cargar las tareas del empleado para reflejar los cambios
            List<Tareas> tareasActualizadas = Archivos.getTasksByAssignedUser(TAREAS_FILE, usuario);
            actualizarTareasEmpleado(tareasActualizadas);
        });
    }

    /**
     * Muestra un diálogo de mensaje de error.
     *
     * @param mensaje el mensaje de error a mostrar
     */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(loginView, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Maneja el registro de usuario mostrando la vista de creación de usuario.
     */
    private void manejarRegistroUsuario() {
        System.out.println("Se presionó el botón Registrarse");
        loginView.dispose();
        crearUsrView = new CrearUsrView();
        crearUsrView.addOpcionListener(this);
    }

    /**
     * Maneja la creación de usuario validando y almacenando las nuevas credenciales.
     */
    private void manejarCreacionUsuario() {
        try {
            String nuevoNombre = crearUsrView.getUsuarioField();
            String nuevaContrasenia = crearUsrView.getContraseniaField();
            String nuevoTipoUsuario = crearUsrView.gettipoEmpleadoBox();
            crearUsrView.dispose();

            if (sonCredencialesValidas(nuevoNombre, nuevaContrasenia)) {
                Archivos.agregarUsuario(nuevoNombre, nuevaContrasenia, nuevoTipoUsuario, USUARIOS_FILE);
            }

            iniciarAplicacion();
        } catch (IOException ex) {
            System.err.println("Error al crear usuario: " + ex.getMessage());
        }
    }

    /**
     * Valida las credenciales del usuario.
     *
     * @param nombre el nombre de usuario a validar
     * @param contrasenia la contraseña a validar
     * @return true si las credenciales son válidas, false en caso contrario
     */
    private boolean sonCredencialesValidas(String nombre, String contrasenia) {
        return !nombre.trim().isEmpty() && !contrasenia.trim().isEmpty();
    }

    /**
     * Maneja la creación de tareas para un Project Manager.
     */
    private void manejarCreacionTarea() {
        try {
            String nombre = tareasPMView.getNombreTareaField();
            String descripcion = tareasPMView.getDescripcionField();
            String fechaLimite = tareasPMView.getFechaField();
            String prioridad = tareasPMView.getPrioridadBox();
            String asignadoA = tareasPMView.getAsignadoAComboBox();

            int exitoTarea = Archivos.agregarTarea(nombre, fechaLimite, prioridad, asignadoA, descripcion, "Pendiente", TAREAS_FILE);

            if (exitoTarea == 0) {
                tareasPMView.agregarTareaATabla(nombre, fechaLimite, prioridad, "Pendiente", asignadoA, descripcion);
            } else {
                System.out.println("Campo/s de tarea vacios");
            }

        } catch (IOException ex) {
            System.err.println("Error al crear tarea: " + ex.getMessage());
        }
    }

    /**
     * Maneja la modificación de tareas para un Project Manager.
     */
    private void manejarModificacionTarea() {
        int filaSeleccionada = tareasPMView.getTablaTareas().getSelectedRow();
        modeloTabla = tareasPMView.getModeloTabla();

        String tituloViejo = (String) modeloTabla.getValueAt(filaSeleccionada, COL_TITULO);
        String asignadoAViejo = (String) modeloTabla.getValueAt(filaSeleccionada, COL_ASIGNADO);
        String estadoViejo = (String) modeloTabla.getValueAt(filaSeleccionada, COL_ESTADO);
        String prioridadViejo = (String) modeloTabla.getValueAt(filaSeleccionada, COL_PRIORIDAD);
        String fechaLimiteViejo = (String) modeloTabla.getValueAt(filaSeleccionada, COL_FECHA);
        String descripcionVieja = (String) modeloTabla.getValueAt(filaSeleccionada, COL_DESCRIPCION);

        System.out.println(tituloViejo);

        ModificarTareaDialog dialog = new ModificarTareaDialog(tareasPMView, tituloViejo, asignadoAViejo, estadoViejo, prioridadViejo, fechaLimiteViejo, descripcionVieja);
        dialog.actualizarEmpleadosEnComboBox(Archivos.getBoxUsuariosModificar(USUARIOS_FILE, asignadoAViejo));
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            modeloTabla.setValueAt(dialog.getTitulo(), filaSeleccionada, COL_TITULO);
            modeloTabla.setValueAt(dialog.getAsignadoA(), filaSeleccionada, COL_ASIGNADO);
            modeloTabla.setValueAt(dialog.getEstado(), filaSeleccionada, COL_ESTADO);
            modeloTabla.setValueAt(dialog.getPrioridad(), filaSeleccionada, COL_PRIORIDAD);
            modeloTabla.setValueAt(dialog.getFechaLimite(), filaSeleccionada, COL_FECHA);
            modeloTabla.setValueAt(dialog.getDescripcion(), filaSeleccionada, COL_DESCRIPCION);

            String nuevotitulo = obtenerValorCelda(filaSeleccionada, COL_TITULO);
            String nuevoAsignadoA = obtenerValorCelda(filaSeleccionada, COL_ASIGNADO);
            String nuevoEstado = obtenerValorCelda(filaSeleccionada, COL_ESTADO);
            String nuevaPrioridad = obtenerValorCelda(filaSeleccionada, TareasPMView.COL_PRIORIDAD);
            String nuevaFechaLimite = obtenerValorCelda(filaSeleccionada, TareasPMView.COL_FECHA);
            String nuevaDescripcion = obtenerValorCelda(filaSeleccionada, TareasPMView.COL_DESCRIPCION);
            System.out.println(nuevotitulo);

            Archivos.modificarTarea(TAREAS_FILE, tituloViejo, nuevotitulo,nuevoAsignadoA,nuevoEstado,nuevaPrioridad,nuevaFechaLimite,nuevaDescripcion);
        }
    }

    /**
     * Obtiene el valor de una celda en la tabla de tareas.
     *
     * @param fila el índice de la fila
     * @param columna el índice de la columna
     * @return el valor de la celda como una cadena (String)
     */
    private String obtenerValorCelda(int fila, int columna) {
        return (String) tareasPMView.getModeloTabla().getValueAt(fila, columna);
    }

    /**
     * Maneja la eliminación de tareas para un Project Manager.
     *
     * @throws IOException si ocurre un error de E/S
     */
    private void manejarEliminacionTarea() throws IOException {
        int filaSeleccionada = tareasPMView.getTablaTareas().getSelectedRow();
        if (filaSeleccionada != -1) {
            String titulo = (String) tareasPMView.getModeloTabla().getValueAt(filaSeleccionada, COL_TITULO);
            tareasPMView.getModeloTabla().removeRow(filaSeleccionada);
            Archivos.eliminarTarea(titulo, TAREAS_FILE);
        }
    }

    /**
     * Actualiza la vista de tareas para un empleado.
     *
     * @param tareas la lista de tareas asignadas al empleado
     */
    private void actualizarTareasEmpleado(List<Tareas> tareas) {
        String[][] tareasArray = convertirListaTareasAArray(tareas);
        tareasEmpleadoView.importarListaTareas(tareasArray);
    }

    /**
     * Convierte una lista de tareas a un arreglo 2D de nombres y estados de tareas.
     *
     * @param tareas la lista de tareas a convertir
     * @return un arreglo 2D de nombres y estados de tareas
     */
    public static String[][] convertirListaTareasAArray(List<Tareas> tareas) {
        String[][] tareasArray = new String[tareas.size()][2];
        for (int i = 0; i < tareas.size(); i++) {
            Tareas tarea = tareas.get(i);
            tareasArray[i][0] = tarea.getNombre();
            tareasArray[i][1] = tarea.getEstado();
        }
        return tareasArray;
    }

    /**
     * Maneja la visualización de estadísticas de tareas.
     */
    public void manejarEstadisticas() {
        String nombreEmpleado = tareasPMView.getEmpleadoSeleccionadoParaEstadisticas();

        float porcentaje;
        if (nombreEmpleado.equals("Todos los empleados")) {
            porcentaje = Archivos.obtenerEstaditicasGlobales(TAREAS_FILE);
        } else {
            porcentaje = Archivos.obtenerEstaditicasEmpleado(TAREAS_FILE, nombreEmpleado);
        }
        tareasPMView.actualizarPorcentajeTareas(porcentaje);
    }
}