package rs.ac.bg.fon.mappers;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.BetGroup.BetGroupDTO;
import rs.ac.bg.fon.dtos.Odd.OddDTO;
import rs.ac.bg.fon.entity.BetGroup;

import java.util.List;

@Component
public class BetGroupMapper {
    public static BetGroupDTO betGroupToBetGroupDTO(BetGroup betGroup) throws Exception {
        if (betGroup.getId() == null || betGroup.getId() < 0 || betGroup.getName() == null || betGroup.getName().isBlank()) {
            throw new Exception("BetGroup object has invalid fields [id = " + betGroup.getId() + ", name = " + betGroup.getName() + "]");
        }
        BetGroupDTO betGroupDTO = new BetGroupDTO();
        betGroupDTO.setId(betGroup.getId());
        betGroupDTO.setName(betGroup.getName());
        return betGroupDTO;
    }

    public static BetGroupDTO betGroupToBetGroupDTO(BetGroup betGroup, List<OddDTO> oddDTOList) throws Exception {
        if (oddDTOList == null || oddDTOList.isEmpty()) {
            throw new Exception("List of odds is null!");
        }
        BetGroupDTO betGroupDTO = betGroupToBetGroupDTO(betGroup);
        betGroupDTO.setOdds(oddDTOList);
        return betGroupDTO;
    }
}
