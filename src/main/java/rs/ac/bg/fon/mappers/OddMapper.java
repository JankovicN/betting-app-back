package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Odd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OddMapper {

    public OddDTO oddToOddDTO(Odd odd) throws Exception {
        if (odd.getId() == null || odd.getId() < 0 || odd.getName() == null || odd.getName().isBlank() || odd.getOdd() == null || odd.getOdd().compareTo(BigDecimal.ONE) < 0) {
            throw new Exception("Odd object has invalid fields [id = " + odd.getId() + ", name = " + odd.getName() + ", odd = " + odd.getOdd() + "]");
        }

        OddDTO oddDTO = new OddDTO();
        oddDTO.setId(odd.getId());
        oddDTO.setOdd(odd.getOdd());
        oddDTO.setName(odd.getName());
        return oddDTO;
    }

    public List<OddDTO> oddListToOddDTOList(List<Odd> oddList) throws Exception {
        if (oddList == null || oddList.isEmpty()) {
            throw new Exception("List of Odds is Empty!");
        }
        List<OddDTO> oddDTOList = new ArrayList<>();
        for (Odd odd : oddList) {
            OddDTO oddDTO = oddToOddDTO(odd);
            oddDTOList.add(oddDTO);
        }
        return oddDTOList;
    }
}
