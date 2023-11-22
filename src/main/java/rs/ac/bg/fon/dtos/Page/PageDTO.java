package rs.ac.bg.fon.dtos.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> implements Serializable {
    private List<T> content;
    private int number;
    private int totalPages;
}
