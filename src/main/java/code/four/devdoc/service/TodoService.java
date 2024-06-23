package code.four.devdoc.service;

import code.four.devdoc.dto.PageRequestDTO;
import code.four.devdoc.dto.PageResponseDTO;
import code.four.devdoc.dto.TodoDTO;

public interface TodoService {

    Long register(TodoDTO todoDTO);

    TodoDTO get(Long tno);

    void modify(TodoDTO todoDTO);

    void remove(Long tno);

    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);

}