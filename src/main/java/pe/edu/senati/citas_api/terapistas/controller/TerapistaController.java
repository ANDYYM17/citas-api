package pe.edu.senati.citas_api.terapistas.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.senati.citas_api.terapistas.dto.TerapistaDTO;
import pe.edu.senati.citas_api.terapistas.service.TerapistaService;

@RestController
@RequestMapping("/api/terapistas")
public class TerapistaController {

	private final TerapistaService service;

	public TerapistaController(TerapistaService service) {
		this.service = service;
	}

	@GetMapping
	public List<TerapistaDTO> listar() {
		return service.listar();
	}

	@GetMapping("/activos")
	public List<TerapistaDTO> listarActivos() {
		return service.listarActivos();
	}

	@GetMapping("/buscar")
	public List<TerapistaDTO> buscar(@RequestParam String texto) {
		return service.buscar(texto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TerapistaDTO> obtener(@PathVariable Long id) {
		return service.buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<TerapistaDTO> crear(@Valid @RequestBody TerapistaDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TerapistaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TerapistaDTO dto) {
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
