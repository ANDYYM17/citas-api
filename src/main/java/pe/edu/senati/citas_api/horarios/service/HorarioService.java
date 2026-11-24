package pe.edu.senati.citas_api.horarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.senati.citas_api.horarios.dto.HorarioDTO;
import pe.edu.senati.citas_api.horarios.entity.Horario;
import pe.edu.senati.citas_api.horarios.repository.HorarioRepository;
import pe.edu.senati.citas_api.terapistas.repository.TerapistaRepository;

@Service
public class HorarioService {

	private final HorarioRepository repository;
	private final TerapistaRepository terapistas;

	public HorarioService(HorarioRepository repository, TerapistaRepository terapistas) {
		this.repository = repository;
		this.terapistas = terapistas;
	}

	public List<HorarioDTO> listar() {
		return repository.findAll().stream().map(this::aDTO).toList();
	}

	public Optional<HorarioDTO> buscarPorId(Long id) {
		return repository.findById(id).map(this::aDTO);
	}

	public List<HorarioDTO> listarPorTerapista(Long terapistaId) {
		return repository.findByTerapistaIdOrderByDiaSemanaAscHoraInicioAsc(terapistaId)
				.stream().map(this::aDTO).toList();
	}

	public List<HorarioDTO> listarPorDia(Integer dia) {
		return repository.findByDiaSemanaOrderByHoraInicioAsc(dia).stream().map(this::aDTO).toList();
	}

	public HorarioDTO crear(HorarioDTO dto) {
		validar(dto);
		if (repository.existsByTerapistaIdAndDiaSemana(dto.terapistaId(), dto.diaSemana())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "La terapista ya tiene horario ese día");
		}
		Horario nuevo = new Horario();
		copiar(dto, nuevo);
		return aDTO(repository.save(nuevo));
	}

	public Optional<HorarioDTO> actualizar(Long id, HorarioDTO dto) {
		return repository.findById(id).map(existente -> {
			validar(dto);
			if (repository.existsByTerapistaIdAndDiaSemanaAndIdNot(dto.terapistaId(), dto.diaSemana(), id)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"La terapista ya tiene otro horario ese día");
			}
			copiar(dto, existente);
			return aDTO(repository.save(existente));
		});
	}

	public boolean eliminar(Long id) {
		if (!repository.existsById(id)) {
			return false;
		}
		repository.deleteById(id);
		return true;
	}

	private void validar(HorarioDTO dto) {
		if (!terapistas.existsById(dto.terapistaId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"La terapista " + dto.terapistaId() + " no existe");
		}
		if (!dto.horaInicio().isBefore(dto.horaFin())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"La hora de inicio debe ser menor que la hora de fin");
		}
	}

	private HorarioDTO aDTO(Horario horario) {
		return new HorarioDTO(horario.getId(), horario.getTerapistaId(), horario.getDiaSemana(),
				horario.getHoraInicio(), horario.getHoraFin());
	}

	private void copiar(HorarioDTO dto, Horario horario) {
		horario.setTerapistaId(dto.terapistaId());
		horario.setDiaSemana(dto.diaSemana());
		horario.setHoraInicio(dto.horaInicio());
		horario.setHoraFin(dto.horaFin());
	}

}
