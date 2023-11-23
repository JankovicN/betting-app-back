package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.OddGroup.OddGroupDTO;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.OddGroup;

import java.util.List;

@Component
public class OddGroupMapper {
    public static OddGroupDTO oddGroupToOddGroupDTO(OddGroup oddGroup) throws Exception {
        if (oddGroup.getId() == null || oddGroup.getId() < 0 || oddGroup.getName() == null || oddGroup.getName().isBlank()) {
            throw new Exception("OddGroup object has invalid fields [id = " + oddGroup.getId() + ", name = " + oddGroup.getName() + "]");
        }
        OddGroupDTO oddGroupDTO = new OddGroupDTO();
        oddGroupDTO.setId(oddGroup.getId());
        oddGroupDTO.setName(oddGroup.getName());
        return oddGroupDTO;
    }

    public static OddGroupDTO oddGroupToOddGroupDTO(OddGroup oddGroup, List<OddDTO> oddDTOList) throws Exception {
        if (oddDTOList == null || oddDTOList.isEmpty()) {
            throw new Exception("List of odds is null!");
        }
        OddGroupDTO oddGroupDTO = oddGroupToOddGroupDTO(oddGroup);
        oddGroupDTO.setOdds(oddDTOList);
        return oddGroupDTO;
    }
}
