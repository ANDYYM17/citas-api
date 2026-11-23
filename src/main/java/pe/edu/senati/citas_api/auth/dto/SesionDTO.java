package pe.edu.senati.citas_api.auth.dto;

/** Datos de la sesión: la contraseña nunca forma parte de la respuesta. */
public record SesionDTO(Long usuarioId, String email, String rol) { }
