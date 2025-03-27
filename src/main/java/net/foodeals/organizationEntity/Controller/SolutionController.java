package net.foodeals.organizationEntity.Controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.organizationEntity.application.dtos.responses.SolutionResponse;
import net.foodeals.organizationEntity.application.services.SolutionService;
import net.foodeals.organizationEntity.domain.entities.Solution;

@RestController
@RequestMapping("v1/solutions")
@RequiredArgsConstructor
public class SolutionController {

	private final SolutionService solutionService;
	private final ModelMapper modelMapper;

	@GetMapping
	public ResponseEntity<List<SolutionResponse>> getAll() {
		List<Solution> solutions = this.solutionService.getAll();
		List<SolutionResponse> solutionsResponses = solutions.stream()
				.map(solution -> this.modelMapper.map(solution, SolutionResponse.class)).toList();
		return new ResponseEntity<List<SolutionResponse>>(solutionsResponses, HttpStatus.OK);
	}

}
