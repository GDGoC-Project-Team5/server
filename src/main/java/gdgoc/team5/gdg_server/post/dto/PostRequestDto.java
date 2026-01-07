package gdgoc.team5.gdg_server.post.dto;

import java.time.LocalDateTime;

//홈페이지에서 제목을 누르면 title, content, author, createdDate정보 필요
public record PostRequestDto(
        //Long id,
        //Long views,
        String title,
        String content,
        String author
        //LocalDateTime createdDate
) {

}
