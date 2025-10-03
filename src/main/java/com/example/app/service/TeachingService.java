package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.app.dto.TeachingDTO;
import com.example.app.model.Teaching;
import com.example.app.repository.TeachingRepository;

@Service
public class TeachingService {

	private final TeachingRepository teachingRepository;

	public TeachingService(TeachingRepository teachingRepository) {
		this.teachingRepository = teachingRepository;
	}

	// Convert Entity -> DTO
	private TeachingDTO convertToDTO(Teaching entity) {
		return new TeachingDTO(entity.getId(), entity.getLecturerId(), entity.getCourseId(), entity.getPeriod(),
				entity.getDayOfWeek(), entity.getClassRoom());
	}

	// Convert DTO -> Entity
	private Teaching convertToEntity(TeachingDTO dto) {
		return new Teaching(dto.getId(), dto.getLecturerId(), dto.getCourseId(), dto.getPeriod(), dto.getDayOfWeek(),
				dto.getClassRoom());
	}

	public Optional<TeachingDTO> getTeachingById(Long id) {
		return teachingRepository.findById(id).map(this::convertToDTO);
	}

	public List<TeachingDTO> getAllTeachings() {
		return teachingRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public TeachingDTO saveTeaching(TeachingDTO dto) {
		Teaching saved = teachingRepository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	public Optional<TeachingDTO> updateTeaching(Long id, TeachingDTO dto) {
		return teachingRepository.findById(id).map(entity -> {
			entity.setLecturerId(dto.getLecturerId());
			entity.setCourseId(dto.getCourseId());
			entity.setPeriod(dto.getPeriod());
			entity.setDayOfWeek(dto.getDayOfWeek());
			entity.setClassRoom(dto.getClassRoom());
			return convertToDTO(teachingRepository.save(entity));
		});
	}

	public void deleteTeaching(Long id) {
		teachingRepository.deleteById(id);
	}

	public List<Teaching> getTeachingsByLecturerId(Long lecturerId) {
		return teachingRepository.findByLecturerId(lecturerId);
	}
//	@Transactional
//	public void setLecturerIdToNull(Long lecturerId) {
//		List<Teaching> teachings = teachingRepository.findByLecturerId(lecturerId);
//		for (Teaching teaching : teachings) {
//			teaching.setLecturerId(null);
//			teachingRepository.save(teaching);
//		}
//	}

}
