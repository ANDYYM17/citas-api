package pe.edu.senati.citas_api.horarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.senati.citas_api.horarios.dto.HorarioDTO;
import pe.edu.senati.citas_api.horarios.service.HorarioService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

	private final HorarioService service;

	public HorarioController(HorarioService service) {
		this.service = service;
	}

	@GetMapping
	public List<HorarioDTO> listar() {
		return service.listar();
	}

	@GetMapping("/terapista/{terapistaId}")
	public List<HorarioDTO> listarPorTerapista(@PathVariable Long terapistaId) {
		return service.listarPorTerapista(terapistaId);
	}

	@GetMapping("/dia/{dia}")
	public List<HorarioDTO> listarPorDia(@PathVariable Integer dia) {
		return service.listarPorDia(dia);
	}

	@GetMapping("/{id}")
	public ResponseEntity<HorarioDTO> obtener(@PathVariable Long id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<HorarioDTO> crear(@Valid @RequestBody HorarioDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<HorarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody HorarioDTO dto) {
		return service.actualizar(id, dto)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		if (service.eliminar(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}
