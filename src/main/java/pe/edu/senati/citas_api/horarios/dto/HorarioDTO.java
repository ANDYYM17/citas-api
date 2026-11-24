package pe.edu.senati.citas_api.horarios.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record HorarioDTO(
		Long id,

		@NotNull(message = "La terapista es obligatoria")
		Long terapistaId,

		@NotNull(message = "El día es obligatorio")
		@Min(value = 1, message = "Día de 1 (lunes) a 7 (domingo)")
		@Max(value = 7, message = "Día de 1 (lunes) a 7 (domingo)")
		Integer diaSemana,

		@NotNull(message = "La hora de inicio es obligatoria")
		LocalTime horaInicio,

		@NotNull(message = "La hora de fin es obligatoria")
		LocalTime horaFin
) { }
