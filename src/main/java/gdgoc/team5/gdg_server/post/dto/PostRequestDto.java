package gdgoc.team5.gdg_server.post.dto;

//홈페이지에서 제목을 누르면 title, content, author, createdDate정보 필요
public record PostRequestDto(
	String title,
	String content,
	String author
) {
}
