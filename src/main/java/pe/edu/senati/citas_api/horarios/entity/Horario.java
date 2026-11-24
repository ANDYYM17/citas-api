package pe.edu.senati.citas_api.horarios.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "horarios")
public class Horario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "terapista_id", nullable = false)
	private Long terapistaId;

	@Column(name = "dia_semana", nullable = false)
	private Integer diaSemana;

	@Column(name = "hora_inicio", nullable = false)
	private LocalTime horaInicio;

	@Column(name = "hora_fin", nullable = false)
	private LocalTime horaFin;

	public Long getId() { return id; }
	public Long getTerapistaId() { return terapistaId; }
	public void setTerapistaId(Long terapistaId) { this.terapistaId = terapistaId; }
	public Integer getDiaSemana() { return diaSemana; }
	public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }
	public LocalTime getHoraInicio() { return horaInicio; }
	public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
	public LocalTime getHoraFin() { return horaFin; }
	public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

}
