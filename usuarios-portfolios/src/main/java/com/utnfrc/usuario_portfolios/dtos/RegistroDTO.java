package com.utnfrc.usuario_portfolios.dtos;

public class RegistroDTO {
    private String username;
    private String email;
    private String password;
    private String nombre;
    private String rol;
    private Long dni;
    private String apellido;
    private String domicilio;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Long getDni() { return dni; }
    public void setDni(Long dni) { this.dni = dni; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }
}
