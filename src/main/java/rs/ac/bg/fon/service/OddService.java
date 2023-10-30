package rs.ac.bg.fon.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Odd;

import java.util.List;

@Service
public interface OddService {

    Odd save(Odd odd);
    List<Odd> saveOddList(List<Odd> odds);


    List<Odd> getOddsForFixtureAndBetGroup(Integer fixtureId, Integer betGroupId);

    List<OddDTO> createOddDTOList(Integer fixtureId, Integer betGroupId);


    boolean existsWithFixtureIdAndBetGroupId(Integer betGroupId, Integer id);

}
