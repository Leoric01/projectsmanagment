package com.leoric.services;

import com.leoric.models.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void deleteComment(Long commentId, Long userId) throws Exception;

    Comment createComment(Long issueId, Long userId, String content);

    List<Comment> findCommentByIssueId(Long issueId);
}
