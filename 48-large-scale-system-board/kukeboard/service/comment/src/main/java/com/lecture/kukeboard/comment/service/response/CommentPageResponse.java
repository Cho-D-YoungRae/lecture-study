package com.lecture.kukeboard.comment.service.response;

import java.util.List;

public record CommentPageResponse(
        List<CommentResponse> comments,
        Long commentCount
) {
}
