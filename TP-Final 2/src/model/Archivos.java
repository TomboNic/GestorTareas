package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Archivos {

    private String rutaTareas;
    private String rutaUsuarios;

    public Archivos(String rutaTareas, String rutaUsuarios) {
        this.rutaTareas = rutaTareas;
        this.rutaUsuarios = rutaUsuarios;
    }

    public static int agregarTarea(String nombre, String fechaLimite, String prioridad, String asignadoA, String descripcion, String estado, String rutaTareas) throws IOException {

        if (validarVariables(nombre, fechaLimite, prioridad, asignadoA, descripcion, estado, rutaTareas)) {
            String tarea = nombre + "," + asignadoA + "," + estado + "," + prioridad + "," + fechaLimite + "," + descripcion;
            try (FileWriter fw = new FileWriter(rutaTareas, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(tarea);
                return 0;
            }
        } else {return 1;}
    }


    public static int eliminarTarea(String nombre, String rutaTareas) throws IOException {
        File inputFile = new File(rutaTareas);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("El archivo no existe o no es un archivo válido: " + rutaTareas);
            return 1;
        }

        Path tempPath = Files.createTempFile("temp", ".txt");
        File tempFile = tempPath.toFile();
        System.out.println("Archivo temporal creado: " + tempFile.getAbsolutePath());

        System.out.println("Nombre de la tarea a eliminar: " + nombre);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            boolean eliminado = false;

            while ((currentLine = reader.readLine()) != null) {
                String[] datos = currentLine.split(",");
                if (!datos[0].equals(nombre)) {
                    writer.write(currentLine + System.lineSeparator());
                } else {
                    eliminado = true;
                }
            }

            writer.close();
            reader.close();

            if (eliminado) {
                if (!inputFile.delete()) {
                    System.err.println("No se pudo eliminar el archivo original: " + inputFile.getAbsolutePath());
                    return 1;
                } else if (!tempFile.renameTo(inputFile)) {
                    System.err.println("No se pudo renombrar el archivo temporal: " + tempFile.getAbsolutePath());
                    return 2;
                }
            }
        } catch (IOException ex) {
            throw new IOException("Error al procesar el archivo: " + ex.getMessage(), ex);
        }

        return 0;
    }


    public static String[][] getListaTareas(String rutaTareas) {
        List<String[]> listaTareas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaTareas))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 6) {
                    listaTareas.add(fields);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return listaTareas.toArray(new String[0][]);
    }


    public static void modificarTarea(String rutaTareas, String tituloViejo, String nuevoTitulo, String nuevoAsignado, String nuevoEstado, String nuevoPrioridad, String nuevaFecha, String nuevaDescripcion) {
        List<String> lines = new ArrayList<>();
        String newLine = String.join(",", nuevoTitulo, nuevoAsignado, nuevoEstado, nuevoPrioridad, nuevaFecha, nuevaDescripcion);
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaTareas))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(tituloViejo + ",")) {
                    lines.add(newLine);
                    encontrado = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de tareas", e);
        }

        if (!encontrado) {
            throw new RuntimeException("No se encontró la tarea con el título especificado: " + tituloViejo);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaTareas))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en el archivo de tareas", e);
        }
    }

    public static void agregarUsuario(String nombre, String contrasenia, String rol, String rutaUsuarios) throws IOException {
        try (FileWriter fw = new FileWriter(rutaUsuarios, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(nombre + "," + contrasenia + "," + rol);
        }
    }


    public static List<String[]> getListaUsuarios(String rutaUsuarios) {
        List<String[]> usuarios = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaUsuarios))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                if (valores.length == 3) {
                    usuarios.add(valores);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return usuarios;
    }

    public static String[] getNombreUsuarios(String rutaUsuarios) {
        List<String> empleadosNombres = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaUsuarios))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String role = data[2].trim();

                if (role.equalsIgnoreCase("Empleado") && !empleadosNombres.contains(name)) {
                    empleadosNombres.add(name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return empleadosNombres.toArray(new String[0]);
    }

    public static String[] getBoxUsuariosModificar(String rutaUsuarios, String asignadoViejo) {
        List<String> empleadosNombres = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaUsuarios))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String role = data[2].trim();

                if (role.equalsIgnoreCase("Empleado") && !empleadosNombres.contains(name)) {
                    empleadosNombres.add(name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (empleadosNombres.contains(asignadoViejo)) {
            empleadosNombres.remove(asignadoViejo);
            empleadosNombres.add(asignadoViejo);
        }

        return empleadosNombres.toArray(new String[0]);
    }

    public static List<Tareas> getTasksByAssignedUser(String rutaTareas, String usuarioAsignado) {
        List<Tareas> tasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaTareas))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String nombreTarea = data[0].trim();
                    String asignado = data[1].trim();
                    String estado = data[2].trim();
                    String prioridad = data[3].trim();
                    String fecha = data[4].trim();
                    String descripcion = data[5].trim();

                    if (asignado.equalsIgnoreCase(usuarioAsignado)) {
                        Tareas tarea = new Tareas(nombreTarea, asignado, estado, prioridad, fecha, descripcion);
                        tasks.add(tarea);
                    }
                } else {
                    System.err.println("Error: La línea no tiene suficientes elementos: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    public static boolean validarVariables(String nombre, String fechaLimite, String prioridad, String asignadoA, String descripcion, String estado, String rutaTareas) {
        return !nombre.isEmpty() && !fechaLimite.isEmpty() && !prioridad.isEmpty() && !asignadoA.isEmpty() && !descripcion.isEmpty() && !estado.isEmpty() && !rutaTareas.isEmpty();
    }

    public static void actualizarEstadoTarea(String rutaTareas, String titulo, String nuevoEstado) {
        List<String[]> filas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaTareas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                filas.add(linea.split(","));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean encontrado = false;

        for (int i = 0; i < filas.size(); i++) {
            String[] fila = filas.get(i);
            if (fila[0].equals(titulo)) {
                fila[2] = nuevoEstado;
                filas.set(i, fila);
                encontrado = true;
                break;
            }
        }

        try (FileWriter fw = new FileWriter(rutaTareas)) {
            for (String[] fila : filas) {
                String lineaCSV = String.join(",", fila);
                fw.write(lineaCSV + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (encontrado) {
            System.out.println("El estado de la tarea '" + titulo + "' se ha actualizado a '" + nuevoEstado + "'.");
        } else {
            System.out.println("No se encontró ninguna tarea con el título '" + titulo + "'.");
        }
    }

    public static float obtenerEstaditicasGlobales(String rutaTareas) {
        int totalLineas = 0;
        int lineasCompletadas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaTareas))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                totalLineas++;

                String[] campos = linea.split(",");
                String estado = campos[2].trim();

                if (estado.equalsIgnoreCase("Completada")) {
                    lineasCompletadas++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (totalLineas == 0) {
            return 0f;
        }

        return ((float) lineasCompletadas / (totalLineas)) * 100;
    }

    public static float obtenerEstaditicasEmpleado(String rutaTareas, String empleado) {
        float porcentajeCompletadas = 0.0f;
        int totalTareas = 0;
        int tareasCompletadas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaTareas))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");

                if (campos.length >= 3) {
                    String asignado = campos[1].trim();
                    String estado = campos[2].trim();

                    if (asignado.equals(empleado)) {
                        totalTareas++;

                        if (estado.equals("Completada")) {
                            tareasCompletadas++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (totalTareas > 0) {
            porcentajeCompletadas = (float) tareasCompletadas / totalTareas * 100;
        }

        return porcentajeCompletadas;
    }

}
