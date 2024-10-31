package diary.api;

public record DiaryRequest(
        Long id,
        String name,
        String title,
        String content
) {
}