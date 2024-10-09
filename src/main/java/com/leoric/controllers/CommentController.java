package com.leoric.controllers;

import com.leoric.models.Comment;
import com.leoric.models.User;
import com.leoric.requests.CreateCommentRequest;
import com.leoric.response.MessageResponse;
import com.leoric.services.CommentService;
import com.leoric.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leoric.config.JwtConstant.JWT_HEADER;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CreateCommentRequest request,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Comment createdComment = commentService.createComment(request.getIssueId(), user.getId(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        commentService.deleteComment(commentId, user.getId());
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Comment deleted successfully.");
        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<List<Comment>> getCommentsByIssueId(
            @PathVariable Long issueId
    ) throws Exception {
        List<Comment> comments = commentService.findCommentByIssueId(issueId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }


}
