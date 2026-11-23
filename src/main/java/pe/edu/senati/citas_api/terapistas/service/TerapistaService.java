package pe.edu.senati.citas_api.terapistas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.senati.citas_api.auth.entity.Usuario;
import pe.edu.senati.citas_api.auth.repository.RolRepository;
import pe.edu.senati.citas_api.auth.repository.UsuarioRepository;
import pe.edu.senati.citas_api.terapistas.dto.TerapistaDTO;
import pe.edu.senati.citas_api.terapistas.entity.Terapista;
import pe.edu.senati.citas_api.terapistas.repository.TerapistaRepository;

@Service
public class TerapistaService {

	private final TerapistaRepository repository;
	private final UsuarioRepository usuarios;
	private final RolRepository roles;

	public TerapistaService(TerapistaRepository repository, UsuarioRepository usuarios, RolRepository roles) {
		this.repository = repository;
		this.usuarios = usuarios;
		this.roles = roles;
	}

	public List<TerapistaDTO> listar() {
		return repository.findAll().stream().map(this::aDTO).toList();
	}

	public Optional<TerapistaDTO> buscarPorId(Long id) {
		return repository.findById(id).map(this::aDTO);
	}

	public List<TerapistaDTO> listarActivos() {
		return repository.findByActivoTrue().stream().map(this::aDTO).toList();
	}

	public List<TerapistaDTO> buscar(String texto) {
		return repository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(texto, texto)
				.stream().map(this::aDTO).toList();
	}

	public TerapistaDTO crear(TerapistaDTO dto) {
		validarUsuario(dto.usuarioId());
		if (repository.existsByUsuarioId(dto.usuarioId())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Ese usuario ya tiene un perfil de terapista");
		}
		if (repository.existsByDni(dto.dni())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una terapista con ese DNI");
		}
		Terapista nueva = new Terapista();
		copiar(dto, nueva);
		return aDTO(repository.save(nueva));
	}

	public Optional<TerapistaDTO> actualizar(Long id, TerapistaDTO dto) {
		return repository.findById(id).map(existente -> {
			validarUsuario(dto.usuarioId());
			if (repository.existsByUsuarioIdAndIdNot(dto.usuarioId(), id)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Ese usuario ya tiene otro perfil de terapista");
			}
			if (repository.existsByDniAndIdNot(dto.dni(), id)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"Ya existe otra terapista con ese DNI");
			}
			copiar(dto, existente);
			return aDTO(repository.save(existente));
		});
	}

	public boolean eliminar(Long id) {
		if (!repository.existsById(id)) {
			return false;
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"No se puede eliminar: la terapista tiene horarios, servicios o citas. Desactívala con activo=false");
		}
		return true;
	}

	private void validarUsuario(Long usuarioId) {
		Usuario usuario = usuarios.findById(usuarioId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"El usuario " + usuarioId + " no existe"));
		String rol = roles.findById(usuario.getRolId()).map(r -> r.getNombre()).orElse("");
		if (!rol.equals("TERAPISTA")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El usuario " + usuarioId + " no tiene el rol TERAPISTA");
		}
	}

	private TerapistaDTO aDTO(Terapista terapista) {
		return new TerapistaDTO(terapista.getId(), terapista.getUsuarioId(), terapista.getNombres(),
				terapista.getApellidos(), terapista.getDni(), terapista.getTelefono(), terapista.getActivo());
	}

	private void copiar(TerapistaDTO dto, Terapista terapista) {
		terapista.setUsuarioId(dto.usuarioId());
		terapista.setNombres(dto.nombres());
		terapista.setApellidos(dto.apellidos());
		terapista.setDni(dto.dni());
		terapista.setTelefono(dto.telefono());
		terapista.setActivo(dto.activo() == null ? true : dto.activo());
	}

}
