package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.Odd;

import java.math.BigDecimal;

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
}
