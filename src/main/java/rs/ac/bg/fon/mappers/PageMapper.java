package rs.ac.bg.fon.mappers;

import org.springframework.data.domain.Page;
import rs.ac.bg.fon.dtos.Page.PageDTO;

public class PageMapper {
    public static <T> PageDTO<T> pageToPageDTO(Page<T> page) throws Exception {
        if (page == null) {
            throw new Exception("Page is null!");
        }

        PageDTO<T> pageDTO = new PageDTO();
        pageDTO.setContent(page.getContent());
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setNumber(page.getNumber());

        return pageDTO;
    }
}
