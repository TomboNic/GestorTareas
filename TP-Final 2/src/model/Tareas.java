package model;

import java.util.ArrayList;
import java.util.List;

public class Tareas {
    private String nombre;
    private String asignadoA;
    private String estado;
    private String prioridad;
    private String fecha;
    private String descripcion;

    public Tareas(String nombre, String asignadoA, String estado, String prioridad, String fecha, String descripcion) {
        this.nombre = nombre;
        this.asignadoA = asignadoA;
        this.estado = estado;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public List<Tareas> obtenerTareasDelEmpleado(String nombreEmpleado, String[][] listaTareas) {
        List<Tareas> tareasDelEmpleado = new ArrayList<>();

        for (String[] tareaData : listaTareas) {
            if (tareaData[3].equals(nombreEmpleado)) {
                Tareas tarea = new Tareas(tareaData[0], tareaData[1], tareaData[2], tareaData[3], tareaData[4], tareaData[5]);
                tareasDelEmpleado.add(tarea);
            }
        }
        return tareasDelEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public String getEstado() {
        return estado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAsignadoA(String asignadoA) {
        this.asignadoA = asignadoA;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
