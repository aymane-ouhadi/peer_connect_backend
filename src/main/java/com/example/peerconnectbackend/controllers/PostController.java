package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.*;
import com.example.peerconnectbackend.models.CreateCommentModel;
import com.example.peerconnectbackend.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final GroupUserRepository groupUserRepository;

    private final LikeRepository likeRepository;

    public PostController(PostRepository postRepository, CommentRepository commentRepository, GroupUserRepository groupUserRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.groupUserRepository = groupUserRepository;
        this.likeRepository = likeRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(
            @RequestBody Post post
    ){
        try{
            postRepository.save(post);

            return new ResponseEntity<>(
                    "Post created successfully",
                    HttpStatus.OK
            );
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/like")
    public ResponseEntity<String> like(
        @RequestParam String userId,
        @RequestParam String postId
    ){
        try{

            Like existentLike = likeRepository.findByUserIdAndPostId(userId, postId).orElse(null);

            Post post = postRepository.findById(postId).orElse(null);

            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(userId, post.getGroupId()).orElse(null);

            if(groupUser == null){
                return new ResponseEntity<>(
                        "You can't like a post in a group you're not in",
                        HttpStatus.UNAUTHORIZED
                );
            }

            if(existentLike == null){
                Like like = Like.builder()
                        .userId(userId)
                        .postId(postId)
                        .build();

                likeRepository.save(like);

                return new ResponseEntity<>(
                        "Post liked successfully",
                        HttpStatus.OK
                );
            }

            likeRepository.delete(existentLike);

            return new ResponseEntity<>(
                    "Post unliked successfully",
                    HttpStatus.OK
            );
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<String> comment(
        @RequestBody CreateCommentModel commentModel
    ){
        try{

            if(commentModel.getComment() == null){
                return new ResponseEntity<>(
                    "Comment field is required",
                    HttpStatus.BAD_REQUEST
                );
            }

            Comment comment = Comment.builder()
                    .userId(commentModel.getUserId())
                    .postId(commentModel.getPostId())
                    .comment(commentModel.getComment())
                    .build();

            commentRepository.save(comment);

            return new ResponseEntity<>(
                    "Post commented successfully",
                    HttpStatus.OK
            );

        }
        catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
