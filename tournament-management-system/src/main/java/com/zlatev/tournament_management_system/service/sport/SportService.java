package com.zlatev.tournament_management_system.service.sport;

import com.zlatev.tournament_management_system.dto.scoring.CreateScoringTypeDTO;
import com.zlatev.tournament_management_system.dto.scoring.ScoringTypeResponseDTO;
import com.zlatev.tournament_management_system.dto.sport.CreateSportRequestDTO;
import com.zlatev.tournament_management_system.dto.sport.SportResponseDTO;
import com.zlatev.tournament_management_system.entity.model.scoring.ScoringType;
import com.zlatev.tournament_management_system.entity.model.sport.Sport;
import com.zlatev.tournament_management_system.exception.scoring.InvalidScoringData;
import com.zlatev.tournament_management_system.exception.sport.SportAlreadyExistsException;
import com.zlatev.tournament_management_system.exception.sport.SportDoesNotExistException;
import com.zlatev.tournament_management_system.mapper.scoring.ScoringMapper;
import com.zlatev.tournament_management_system.mapper.sport.SportMapper;
import com.zlatev.tournament_management_system.repository.scoring.ScoringTypeRepository;
import com.zlatev.tournament_management_system.repository.sport.SportRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SportService {
    private  final SportRepository sportRepository;
    private final ScoringTypeRepository scoringTypeRepository;

    @Transactional
    @CacheEvict(value = "sports", allEntries = true)
    public SportResponseDTO createSport(CreateSportRequestDTO createSportRequestDTO) {
        if (createSportRequestDTO.sportName() == null || createSportRequestDTO.sportName().isBlank()) {
            throw new IllegalArgumentException("Sport name is blank!");
        }

        if (sportRepository.existsBySportName(createSportRequestDTO.sportName())) {
            throw new SportAlreadyExistsException("Sport already exists!");
        }

        Sport sport = SportMapper.toSport(createSportRequestDTO);

        sportRepository.save(sport);
        return SportMapper.toSportResponseDTO(sport);
    }

    @Cacheable(value = "sports")
    public List<SportResponseDTO> getAllSports() {
        return sportRepository.findAll()
                .stream()
                .map(SportMapper::toSportResponseDTO)
                .toList();
    }

    public SportResponseDTO getSportById(UUID sportId) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new SportDoesNotExistException("Sport does not exist!"));
        return SportMapper.toSportResponseDTO(sport);
    }

    public SportResponseDTO updateSport(UUID sportId, CreateSportRequestDTO createSportRequestDTO) {
        if (createSportRequestDTO.sportName() == null || createSportRequestDTO.sportName().isBlank()) {
            throw new IllegalArgumentException("Sport name is blank!");
        }
        if (sportRepository.existsBySportName(createSportRequestDTO.sportName())) {
            throw new SportAlreadyExistsException("Sport already exists!");
        }

        String newName = createSportRequestDTO.sportName();

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new SportDoesNotExistException("Sport does not exist!"));

        if (!sport.getSportName().equals(newName) && sportRepository.existsBySportName(newName)) {
            throw new SportAlreadyExistsException("Sport already exists!");
        }

        sport.setSportName(newName);

        return SportMapper.toSportResponseDTO(sportRepository.save(sport));
    }

    public void deleteSport(UUID sportId) {
        if (!sportRepository.existsById(sportId)) {
            throw new SportDoesNotExistException("Sport does not exist!");
        }
        sportRepository.deleteById(sportId);
    }

    public ScoringTypeResponseDTO addScoringType(UUID sportId, CreateScoringTypeDTO createScoringTypeDTO) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new SportDoesNotExistException("Sport does not exist!"));

        if (createScoringTypeDTO.name() == null || createScoringTypeDTO.name().isBlank()) {
            throw new InvalidScoringData("Scoring name is blank!");
        }

        if (createScoringTypeDTO.points() ==  null || createScoringTypeDTO.points() == 0) {
            throw new InvalidScoringData("Scoring points are blank!");
        }

        ScoringType scoringType = ScoringMapper.toScoringType(createScoringTypeDTO);
        scoringType.setSport(sport);

        sport.getScoringTypes().add(scoringType);

        sportRepository.save(sport);
        scoringTypeRepository.save(scoringType);

        return ScoringMapper.toScoringResponse(scoringType);
    }

    @Transactional
    public List<ScoringTypeResponseDTO> getScoringTypes(UUID sportId) {
        if (!sportRepository.existsById(sportId)) {
            throw new SportDoesNotExistException("Sport does not exist!");
        }
        return scoringTypeRepository.findBySportId(sportId).getScoringTypes()
                .stream()
                .map(ScoringMapper::toScoringResponse)
                .toList();
    }
}
