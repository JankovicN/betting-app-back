package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Odd;

import java.util.List;

@Service
public interface OddService {

    Odd save(Odd odd);

    Odd getOddById(Integer oddId);

    List<Odd> saveOddList(List<Odd> odds);

    List<Odd> getOddsForFixtureAndOddGroup(Integer fixtureId, Integer oddGroupId);

    List<OddDTO> createOddDTOList(Integer fixtureId, Integer oddGroupId);

    boolean existsWithFixtureIdAndOddGroupId(Integer fixtureId, Integer oddGroupId);

}
