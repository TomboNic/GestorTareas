package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private String nombreUsuario;
    private String contrasenia;
    private String rol;

    private List<Usuario> usuarios = new ArrayList<>();

    public Usuario(String nombreUsuario, String contrasenia, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void crearUsuario(String nombreUsuario, String contrasenia, String tipoUsuario){
        if (Objects.equals(tipoUsuario, "Empleado")){
            usuarios.add(new Empleado(nombreUsuario, contrasenia, "Empleado"));
        }
        else if (Objects.equals(tipoUsuario, "Project Manager")){
            usuarios.add(new ProjectManager(nombreUsuario, contrasenia, "Project Manager"));
        }
    }

    public String credencialesValidas(String nombreUsuario, String contrasenia, List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario.getRol();
            }
        }
        return null;
    }
}
