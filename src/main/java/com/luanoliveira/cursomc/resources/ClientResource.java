package com.luanoliveira.cursomc.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luanoliveira.cursomc.domain.Category;
import com.luanoliveira.cursomc.domain.Client;
import com.luanoliveira.cursomc.dto.ClienteDTO;
import com.luanoliveira.cursomc.dto.ClienteNewDTO;
import com.luanoliveira.cursomc.services.ClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/api/clients")
@Api(value = "Clients Resource REST Endpoint.", 
	produces = MediaType.APPLICATION_JSON_VALUE,
	description = "Show the client info.")
public class ClientResource {
	
	@Autowired
	private ClienteService service;

	@RequestMapping(method=RequestMethod.GET)
	@ApiOperation("Get All Clients")
	public ResponseEntity<?> findAll() {
		
		List<Client> list = service.findAll();
		return ResponseEntity.ok().body(list) ;
	}

	@ApiOperation("Get Categories for ID ")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Category.class)})
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Client> find(@PathVariable Integer id) {
		
		Client obj = service.find(id);
		return ResponseEntity.ok().body(obj) ;
	}
	
	@RequestMapping(value="/pagination", method=RequestMethod.GET)
	public ResponseEntity<Page<Client>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="order", defaultValue="nome") String orderBy, 
			@RequestParam(value="dir", defaultValue="ASC") String direction) {
		
		Page<Client> list = service.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list) ;
	}	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO) {
		Client obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id) {
		Client obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}