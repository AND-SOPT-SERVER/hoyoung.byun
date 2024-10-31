package diary.api;

import diary.repository.Category;

public record DiaryRequest(
        Long id,
//        String name,
        String title,
        String content,
//        Long user_id,
        Category category,
        boolean isShare
) {
}