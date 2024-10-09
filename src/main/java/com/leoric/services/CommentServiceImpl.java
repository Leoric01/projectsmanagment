package com.leoric.services;

import com.leoric.models.Comment;
import com.leoric.models.Issue;
import com.leoric.models.User;
import com.leoric.repositories.CommentRepository;
import com.leoric.repositories.IssueRepository;
import com.leoric.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Override
    public void deleteComment(Long commentId, Long userId) throws Exception {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new Exception("comment not found with id " + commentId);
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("user not found with id " + userId);
        }
        if (comment.get().getUser().equals(user.get())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new Exception("comment not deleted with id " + commentId);
        }
    }

    @Override
    public Comment createComment(Long issueId, Long userId, String content) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<User> user = userRepository.findById(userId);
        if (issue.isEmpty()) {
            throw new RuntimeException("Issue not found");
        }
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Comment comment = new Comment();
        comment.setIssue(issue.get());
        comment.setUser(user.get());
        comment.setContent(content);
        comment.setCreatedDateTime(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        issue.get().getComments().add(savedComment);
        return savedComment;
    }

    @Override
    public List<Comment> findCommentByIssueId(Long issueId) {
        return commentRepository.findCommentByIssueId(issueId);
    }
}
