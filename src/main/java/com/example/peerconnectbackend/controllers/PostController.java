package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.*;
import com.example.peerconnectbackend.models.CreateCommentModel;
import com.example.peerconnectbackend.models.PostDetailsModel;
import com.example.peerconnectbackend.repositories.*;
import com.example.peerconnectbackend.utils.Functions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final GroupUserRepository groupUserRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    public PostController(PostRepository postRepository, CommentRepository commentRepository, GroupUserRepository groupUserRepository, GroupRepository groupRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.groupUserRepository = groupUserRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
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

    @GetMapping("/recent")
    public ResponseEntity<List<Post>> recent(
        @RequestParam String userId
    ){
        try{

            List<Post> recentPosts = new ArrayList<>();

            //Getting the ids of the groups the user is in
            List<String> userGroupsIds = groupUserRepository
                    .findAllByUserId(userId)
                    .stream()
                    .map(GroupUser::getGroupId)
                    .toList();

            System.out.println("Groups Ids : " + userGroupsIds);

            //Getting recent posts in every group
            for(String groupId : userGroupsIds){

                LocalDateTime now = LocalDateTime.now();

                List<Post> recentPostsInGroup = postRepository.findBy_publishedAtBetweenAndGroupId(
                    now.minusDays(2),
                    now,
                    groupId
                );

                recentPosts.addAll(recentPostsInGroup);
            }

            System.out.println("Recent posts : " + recentPosts);

            return new ResponseEntity<>(
                    Functions.sortPostsByPublishedDate(recentPosts),
                    HttpStatus.OK
            );

        }
        catch(Exception e){
            return new ResponseEntity<>(
                    new ArrayList<>(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/details")
    public ResponseEntity<PostDetailsModel> details(
        @RequestParam String postId
    ){

        try{

            Post post = postRepository.findById(postId).orElse(null);

            Group group = groupRepository.findById(post.getGroupId()).orElse(null);

            User user = userRepository.findById(post.getUserId()).orElse(null);

            List<Comment> comments = commentRepository.findAllByPostId(postId);

            List<Like> likes = likeRepository.findAllByPostId(postId);

            PostDetailsModel postDetailsModel = PostDetailsModel.builder()
                    .post(post)
                    .group(group)
                    .user(user)
                    .comments(comments)
                    .likes(likes)
                    .build();

            return new ResponseEntity<>(
                    postDetailsModel,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        }
        catch (Exception e){
            return new ResponseEntity<>(
                    new PostDetailsModel(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

}
