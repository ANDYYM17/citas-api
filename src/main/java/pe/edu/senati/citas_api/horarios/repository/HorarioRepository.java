package pe.edu.senati.citas_api.horarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.senati.citas_api.horarios.entity.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

	List<Horario> findByTerapistaIdOrderByDiaSemanaAscHoraInicioAsc(Long terapistaId);

	List<Horario> findByDiaSemanaOrderByHoraInicioAsc(Integer diaSemana);

	boolean existsByTerapistaIdAndDiaSemana(Long terapistaId, Integer diaSemana);

	boolean existsByTerapistaIdAndDiaSemanaAndIdNot(Long terapistaId, Integer diaSemana, Long id);

}
