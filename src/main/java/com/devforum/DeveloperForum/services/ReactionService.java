package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.*;
import com.devforum.DeveloperForum.enums.PostTag;
import com.devforum.DeveloperForum.enums.ReactionType;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.CommentNotFoundException;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.NotAllowedToReactToSelfException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.*;
import com.devforum.DeveloperForum.requests.ReactionRequests.ReactionCreateRequest;
import com.devforum.DeveloperForum.requests.ReactionRequests.ReactionUpdateRequest;
import com.devforum.DeveloperForum.responses.ReactionResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {
    private final CommentReactionRepository commentReactionRepository;
    private final PostReactionRepository postReactionRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final UserDetailsServiceImplementation userDetailsService;

    public ReactionService(CommentReactionRepository commentReactionRepository,
                           PostReactionRepository postReactionRepository, PostRepository postRepository,
                           CommentRepository commentRepository, UserRepository userRepository, UserDetailsServiceImplementation userDetailsService) {
        this.commentReactionRepository = commentReactionRepository;
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public List<ReactionResponse> getAllReactions(Optional<String> reactionTo, Optional<Long> entityId) {
        List<ReactionResponse> reactions = new ArrayList<>();
        if(reactionTo.isEmpty()){
            reactions = commentReactionRepository.findAll().stream().map(ReactionResponse::new)
                    .collect(Collectors.toList());
            reactions.addAll(postReactionRepository.findAll().stream().map(ReactionResponse::new)
                    .toList());
        }
        else if(reactionTo.get().equals("post")){
            if(entityId.isEmpty())
                reactions = postReactionRepository.findAll().stream().
                        map(ReactionResponse::new).collect(Collectors.toList());
            else{
                if(postRepository.existsById(entityId.get()))
                    reactions = postReactionRepository.findAllByPostId(entityId.get()).stream().
                            map(ReactionResponse::new).collect(Collectors.toList());
                else
                    throw new PostNotFoundException("No post found with provided entity id.");
            }

        }
        else if(reactionTo.get().equals("comment")){
            if(entityId.isEmpty())
                reactions = commentReactionRepository.findAll().stream().
                    map(ReactionResponse::new).collect(Collectors.toList());
            else{
                if(commentReactionRepository.existsById(entityId.get()))
                    reactions = commentReactionRepository.findAllByCommentId(entityId.get()).stream().
                            map(ReactionResponse::new).collect(Collectors.toList());
                else
                    throw new CommentNotFoundException("No comment found with provided entity id.");
            }
        }
        if(reactions.isEmpty())
            throw new ReactionNotFoundException("No reactions are found in the database.");
        return reactions;
    }

    public ReactionResponse findReactionById(Long reactionId, String reactionTo) {
        switch (reactionTo){
            case "post":
                PostReaction postReaction = postReactionRepository.findById(reactionId).orElse(null);
                if(postReaction == null)
                    throw new ReactionNotFoundException("No post reaction found with given id.");
                return new ReactionResponse(postReaction);
            case "comment":
                CommentReaction commentReaction = commentReactionRepository.findById(reactionId).orElse(null);
                if(commentReaction == null)
                    throw new ReactionNotFoundException("No comment reaction found with given id.");
                return new ReactionResponse(commentReaction);
            default:
                throw new IllegalArgumentException("Wrong request mapping provided.");
        }
    }

    public List<Long> getNumberOfReactionsToEntity(Long entityId, String reactionTo) {
        List<Long> reactionNumbers = new ArrayList<>(); // indexes -> 0: agree, 1: disagree, 2: helpful
        if(reactionTo.equals("post")){
            Post post = postRepository.findById(entityId).orElse(null);
            if(post == null)
                throw new PostNotFoundException("No post found with given id.");
            for (ReactionType reactionType : ReactionType.values()) {
                long count = postReactionRepository.countByPostIdAndReactionType(entityId, reactionType);
                reactionNumbers.add(count);
            }
            return reactionNumbers;
        }
        else if(reactionTo.equals("comment")){
            Comment comment = commentRepository.findById(entityId).orElse(null);
            if(comment == null)
                throw new CommentNotFoundException("No comment found with given id.");
            for (ReactionType reactionType : ReactionType.values()) {
                Long count = commentReactionRepository.countByCommentIdAndReactionType(entityId, reactionType);
                reactionNumbers.add(count);
            }
            return reactionNumbers;
        }
        throw new IllegalArgumentException();
    }

    public ReactionResponse findReactionByUserIdAndEntityId(Long userId, Long entityId, String reactionTo) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new UserNotFoundException("No user found with given id.");
        if(reactionTo.equals("post")){
            Post post = postRepository.findById(entityId).orElse(null);
            if(post == null)
                throw new PostNotFoundException("No post found with given id.");
            PostReaction reaction = postReactionRepository.findByReactorIdAndPost(userId, post).orElse(null);
            if(reaction == null)
                throw new ReactionNotFoundException("No reaction found.");
            return new ReactionResponse(reaction);
        }
        else if(reactionTo.equals("comment")){
            Comment comment = commentRepository.findById(entityId).orElse(null);
            if(comment == null)
                throw new CommentNotFoundException("No comment found with given id.");
            CommentReaction reaction = commentReactionRepository.findByReactorIdAndComment(userId, comment).
                    orElse(null);
            if(reaction == null)
                throw new ReactionNotFoundException("No reaction found.");
            return new ReactionResponse(reaction);
        }
        throw new IllegalArgumentException();
    }

    public ReactionResponse createReaction(ReactionCreateRequest reactionCreateRequest) {
        if(!userRepository.existsById(reactionCreateRequest.getReactorId()))
            throw new UserNotFoundException("There's no user with such id to react to something.");
        userDetailsService.verifyUser(userRepository.findById(reactionCreateRequest.getReactorId()).get());
        ReactionType reactionType = ReactionType.valueOf(reactionCreateRequest.getReactionType());
        if(reactionCreateRequest.getReactionTo().equals("post")){
            Optional<Post> post = postRepository.findById(reactionCreateRequest.getReactedEntityId());
            if(post.isEmpty())
                throw new PostNotFoundException("No post found to react to.");
            if(postReactionRepository.existsByReactorIdAndPost(reactionCreateRequest.getReactorId(), post.get()))
                throw new ReactionAlreadyExistsException("Reaction already exists with given reactor and post." +
                        " This request shouldn't have been received.");
            User poster = post.get().getUser();
            if(poster.getId().equals(reactionCreateRequest.getReactorId()))
                throw new NotAllowedToReactToSelfException("A user can't react to their own post. " +
                        "This request shouldn't have been received.");
            PostReaction reaction = new PostReaction();
            reaction.setReactionType(reactionType);
            reaction.setPost(post.get());
            reaction.setReactorId(reactionCreateRequest.getReactorId());
            post.get().setNumberOfReactions(post.get().getNumberOfReactions() + 1);
            if(reactionType.equals(ReactionType.DISAGREE))
                poster.setInteractionCount(poster.getInteractionCount() - 1);
            else
                poster.setInteractionCount(poster.getInteractionCount() + 1);
            poster.checkForRepRankUpgrade();
            if(post.get().getPostTag().equals(PostTag.HELPFUL_INFO)){
                if(reactionType.equals(ReactionType.HELPFUL))
                    poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                else if(reactionType.equals(ReactionType.DISAGREE))
                    poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                poster.checkForHelpfulRankUpgrade();
            }
            return new ReactionResponse(postReactionRepository.save(reaction));
        }
        else if(reactionCreateRequest.getReactionTo().equals("comment")){
            Optional<Comment> comment = commentRepository.findById(reactionCreateRequest.getReactedEntityId());
            if(comment.isEmpty())
                throw new CommentNotFoundException("No comment found to react to.");
            if(commentReactionRepository.existsByReactorIdAndComment
                    (reactionCreateRequest.getReactorId(), comment.get()))
                throw new ReactionAlreadyExistsException("Reaction already exists with given reactor and post." +
                        " This request shouldn't have been received.");
            User poster = comment.get().getUser();
            if(poster.getId().equals(reactionCreateRequest.getReactorId()))
                throw new NotAllowedToReactToSelfException("A user can't react to their own comment. " +
                        "This request shouldn't have been received.");
            CommentReaction reaction = new CommentReaction();
            reaction.setReactionType(reactionType);
            reaction.setComment(comment.get());
            reaction.setReactorId(reactionCreateRequest.getReactorId());
            comment.get().setNumberOfReactions(comment.get().getNumberOfReactions() + 1);
            if(reactionType.equals(ReactionType.DISAGREE))
                poster.setInteractionCount(poster.getInteractionCount() - 1);
            else
                poster.setInteractionCount(poster.getInteractionCount() + 1);
            if(comment.get().getPost().getPostTag().equals(PostTag.QUESTION)){
                if(reactionType.equals(ReactionType.DISAGREE))
                    poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                if(reactionType.equals(ReactionType.HELPFUL))
                    poster.setHelpfulCount(poster.getHelpfulCount() + 1);
            }
            poster.checkForRepRankUpgrade();
            poster.checkForHelpfulRankUpgrade();
            return new ReactionResponse(commentReactionRepository.save(reaction));
        }
        throw new IllegalArgumentException();
    }


    public ReactionResponse updateReactionById(Long reactionId, String reactionTo, String newReaction) {
        ReactionType newReactionType = ReactionType.valueOf(newReaction);
        if(reactionTo.equals("post")){
            PostReaction reaction = postReactionRepository.findById(reactionId).orElse(null);
            if(reaction == null)
                throw new ReactionNotFoundException("No post reaction with given id is found.");
            userDetailsService.verifyUser(userRepository.findById(reaction.getReactorId()).get());
            User poster = reaction.getPost().getUser();
            if(newReactionType.equals(reaction.getReactionType()))
                throw new ReactionAlreadyExistsException("This reaction already exists," +
                        " this request shouldn't have been received.");
            if(newReactionType.equals(ReactionType.DISAGREE)){
                poster.setInteractionCount(poster.getInteractionCount() - 2);
                poster.checkForRepRankUpgrade();
            }
            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                poster.setInteractionCount(poster.getInteractionCount() + 2);
                poster.checkForRepRankUpgrade();
            }
            if((reaction.getPost().getPostTag().equals(PostTag.HELPFUL_INFO))){
                if(reaction.getReactionType().equals(ReactionType.AGREE)){
                    if(newReactionType.equals(ReactionType.DISAGREE))
                        poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                    else
                        poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                }
                else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                    if(newReactionType.equals(ReactionType.AGREE))
                        poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                    else
                        poster.setHelpfulCount(poster.getHelpfulCount() + 2);
                }
                else{
                    if(newReactionType.equals(ReactionType.AGREE))
                        poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                    else
                        poster.setHelpfulCount(poster.getHelpfulCount() - 2);
                }
                poster.checkForHelpfulRankUpgrade();
            }
            reaction.setReactionType(newReactionType);
            return new ReactionResponse(postReactionRepository.save(reaction));
        }
        else if(reactionTo.equals("comment")){
            CommentReaction reaction = commentReactionRepository.findById(reactionId).orElse(null);
            if(reaction == null)
                throw new ReactionNotFoundException("No comment reaction with given id is found.");
            userDetailsService.verifyUser(userRepository.findById(reaction.getReactorId()).get());
            User commenter = reaction.getComment().getUser();
            PostTag postTag = reaction.getComment().getPost().getPostTag();
            if(newReactionType.equals(reaction.getReactionType()))
                throw new ReactionAlreadyExistsException("This reaction already exists," +
                        " this request shouldn't have been sent normally.");
//            if(newReactionType.equals(ReactionType.DISAGREE)){
//                if(postTag.equals(PostTag.QUESTION) && reaction.getReactionType().equals(ReactionType.HELPFUL)){
//                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 2);
//                    commenter.checkForHelpfulRankUpgrade();
//                }
//                commenter.setInteractionCount(commenter.getInteractionCount() - 2);
//                commenter.checkForRepRankUpgrade();
//            }
//            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
//                if(postTag.equals(PostTag.QUESTION) && newReactionType.equals(ReactionType.HELPFUL)){
//                    commenter.setHelpfulCount(commenter.getHelpfulCount() + 2);
//                    commenter.checkForHelpfulRankUpgrade();
//                }
//                commenter.setInteractionCount(commenter.getInteractionCount() + 2);
//                commenter.checkForRepRankUpgrade();
//            }
            if(postTag.equals(PostTag.QUESTION)){
                if(reaction.getReactionType().equals(ReactionType.HELPFUL)){
                    //this means that we are going to REMOVE helpful, which causes a drop in helpfulCount
                    if(newReactionType.equals(ReactionType.DISAGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() - 2);
                    else if(newReactionType.equals(ReactionType.AGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() - 1);
                }
                else if(newReactionType.equals(ReactionType.HELPFUL)){
                    //new reaction is helpful, so an increase in helpfulCount is the case
                    if(reaction.getReactionType().equals(ReactionType.AGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() + 1);
                    else if(reaction.getReactionType().equals(ReactionType.DISAGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() + 2);
                }
                else if(reaction.getReactionType().equals(ReactionType.AGREE)
                        && newReactionType.equals(ReactionType.DISAGREE)){
                    // 1 point drop is the case
                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 1);
                }
                else if(reaction.getReactionType().equals(ReactionType.DISAGREE)
                        && newReactionType.equals(ReactionType.AGREE)){
                    // increasing by 1 point
                    commenter.setHelpfulCount(commenter.getHelpfulCount() + 1);
                }
                else{
                    //no other such thing is possible
                    throw new RuntimeException();
                }
                commenter.checkForHelpfulRankUpgrade();
            }
            if(newReactionType.equals(ReactionType.DISAGREE)){
                // 2 drops in interaction count is certain
                commenter.setInteractionCount(commenter.getInteractionCount() - 2);
            }
            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                // increase by 2 points is certain
                commenter.setInteractionCount(commenter.getInteractionCount() + 2);
            }
            else{
                //in other cases, such as HELPFUL -> AGREE, nothing changes
            }
            commenter.checkForRepRankUpgrade();
            reaction.setReactionType(newReactionType);
            return new ReactionResponse(commentReactionRepository.save(reaction));
        }
        else
            throw new IllegalArgumentException();
    }

    public ReactionResponse updateReactionByUserIdAndEntityId(Long userId, Long entityId, String reactionTo,
                                                              ReactionUpdateRequest reactionUpdateRequest) {
        ReactionType newReactionType = ReactionType.valueOf(reactionUpdateRequest.getNewReactionType());
        User reactor = userRepository.findById(userId).orElse(null);
        if(reactor == null)
            throw new UserNotFoundException("No user found with given id.");
        if(reactionTo.equals("post")){
            Post post = postRepository.findById(entityId).orElse(null);
            if(post == null)
                throw new PostNotFoundException("No post found with given id.");
            PostReaction reaction = postReactionRepository.findByReactorIdAndPost(userId, post).orElse(null);
            if(reaction == null)
                throw new ReactionNotFoundException("Post Reaction not found.");
            userDetailsService.verifyUser(userRepository.findById(reaction.getReactorId()).get());
            if(reaction.getReactionType().equals(ReactionType.valueOf(reactionUpdateRequest.getNewReactionType())))
                throw new ReactionAlreadyExistsException("Given user already reacted this way to the given post. " +
                        "This request shouldn't have been sent.");
            User poster = post.getUser();
            if(newReactionType.equals(ReactionType.DISAGREE)){
                poster.setInteractionCount(poster.getInteractionCount() - 2);
                poster.checkForRepRankUpgrade();
            }
            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                poster.setInteractionCount(poster.getInteractionCount() + 2);
                poster.checkForRepRankUpgrade();
            }
            if((reaction.getPost().getPostTag().equals(PostTag.HELPFUL_INFO))){
                if(reaction.getReactionType().equals(ReactionType.AGREE)){
                    if(newReactionType.equals(ReactionType.DISAGREE))
                        poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                    else
                        poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                }
                else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                    if(newReactionType.equals(ReactionType.AGREE))
                        poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                    else
                        poster.setHelpfulCount(poster.getHelpfulCount() + 2);
                }
                else{
                    if(newReactionType.equals(ReactionType.AGREE))
                        poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                    else
                        poster.setHelpfulCount(poster.getHelpfulCount() - 2);
                }
                poster.checkForHelpfulRankUpgrade();
            }
            reaction.setReactionType(ReactionType.valueOf(newReactionType.toString()));
            return new ReactionResponse(postReactionRepository.save(reaction));
        }
        else if(reactionTo.equals("comment")){
            Comment comment = commentRepository.findById(entityId).orElse(null);
            if(comment == null)
                throw new CommentNotFoundException("No comment found with given id.");
            CommentReaction reaction = commentReactionRepository.findByReactorIdAndComment(userId, comment)
                    .orElse(null);
            if(reaction == null)
                throw new ReactionNotFoundException("Comment Reaction not found.");
            userDetailsService.verifyUser(userRepository.findById(reaction.getReactorId()).get());
            if(reaction.getReactionType().equals(ReactionType.valueOf(reactionUpdateRequest.getNewReactionType())))
                throw new ReactionAlreadyExistsException("Given user already reacted this way to the given post. " +
                        "This request shouldn't have been sent.");
            User commenter = reaction.getComment().getUser();
            PostTag postTag = reaction.getComment().getPost().getPostTag();
//          if(newReactionType.equals(ReactionType.DISAGREE)){
//              if(postTag.equals(PostTag.QUESTION) && reaction.getReactionType().equals(ReactionType.HELPFUL)){
//                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 2);
//                    commenter.checkForHelpfulRankUpgrade();
//               }
//               commenter.setInteractionCount(commenter.getInteractionCount() - 2);
//               commenter.checkForRepRankUpgrade();
//           }
//           else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
//               if(postTag.equals(PostTag.QUESTION) && newReactionType.equals(ReactionType.HELPFUL)){
//                   commenter.setHelpfulCount(commenter.getHelpfulCount() + 2);
//                   commenter.checkForHelpfulRankUpgrade();
//               }
//               commenter.setInteractionCount(commenter.getInteractionCount() + 2);
//               commenter.checkForRepRankUpgrade();
//           }
            if(postTag.equals(PostTag.QUESTION)){
                if(reaction.getReactionType().equals(ReactionType.HELPFUL)){
                    //this means that we are going to REMOVE helpful, which causes a drop in helpfulCount
                    if(newReactionType.equals(ReactionType.DISAGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() - 2);
                    else if(newReactionType.equals(ReactionType.AGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() - 1);
                }
                else if(newReactionType.equals(ReactionType.HELPFUL)){
                    //new reaction is helpful, so an increase in helpfulCount is the case
                    if(reaction.getReactionType().equals(ReactionType.AGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() + 1);
                    else if(reaction.getReactionType().equals(ReactionType.DISAGREE))
                        commenter.setHelpfulCount(commenter.getHelpfulCount() + 2);
                }
                else if(reaction.getReactionType().equals(ReactionType.AGREE)
                        && newReactionType.equals(ReactionType.DISAGREE)){
                    // 1 point drop is the case
                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 1);
                }
                else if(reaction.getReactionType().equals(ReactionType.DISAGREE)
                        && newReactionType.equals(ReactionType.AGREE)){
                    // increasing by 1 point
                    commenter.setHelpfulCount(commenter.getHelpfulCount() + 1);
                }
                else{
                    //no other such thing is possible
                    throw new RuntimeException();
                }
                commenter.checkForHelpfulRankUpgrade();
            }
            if(newReactionType.equals(ReactionType.DISAGREE)){
                // 2 drops in interaction count is certain
                commenter.setInteractionCount(commenter.getInteractionCount() - 2);
            }
            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                // increase by 2 points is certain
                commenter.setInteractionCount(commenter.getInteractionCount() + 2);
            }
            else{
                //in other cases, such as HELPFUL -> AGREE, nothing changes
            }
            commenter.checkForRepRankUpgrade();

            reaction.setReactionType(ReactionType.valueOf(newReactionType.toString()));
            return new ReactionResponse(commentReactionRepository.save(reaction));
        }
        throw new IllegalArgumentException();
    }

    public void deleteReactionById(Long reactionId, String reactionTo) {
        if(reactionTo.equals("post")){
            PostReaction toDelete = postReactionRepository.findById(reactionId).orElse(null);
            if(toDelete == null)
                throw new ReactionNotFoundException("Post reaction with given information not found.");
            userDetailsService.verifyUser(userRepository.findById(toDelete.getReactorId()).get());
            toDelete.getPost().setNumberOfReactions(toDelete.getPost().getNumberOfReactions() - 1);
            User poster = toDelete.getPost().getUser();
            if(toDelete.getReactionType().equals(ReactionType.HELPFUL) ||
                    toDelete.getReactionType().equals(ReactionType.AGREE))
                poster.setInteractionCount(poster.getInteractionCount() - 1);
            else
                poster.setInteractionCount(poster.getInteractionCount() + 1);
            poster.checkForRepRankUpgrade();
            if(toDelete.getPost().getPostTag().equals(PostTag.HELPFUL_INFO)){
                if(toDelete.getReactionType().equals(ReactionType.DISAGREE))
                    poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                else if(toDelete.getReactionType().equals(ReactionType.HELPFUL))
                    poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                poster.checkForHelpfulRankUpgrade();
            }
            postReactionRepository.delete(toDelete);
            return;
        }
        else if(reactionTo.equals("comment")){
            CommentReaction toDelete = commentReactionRepository.findById(reactionId).orElse(null);
            if(toDelete == null)
                throw new ReactionNotFoundException("Comment reaction with given information not found.");
            userDetailsService.verifyUser(userRepository.findById(toDelete.getReactorId()).get());
            toDelete.getComment().setNumberOfReactions(toDelete.getComment().getNumberOfReactions() - 1);
            User commenter = toDelete.getComment().getUser();
            if(toDelete.getReactionType().equals(ReactionType.HELPFUL) ||
                    toDelete.getReactionType().equals(ReactionType.AGREE))
                commenter.setInteractionCount(commenter.getInteractionCount() - 1);
            else
                commenter.setInteractionCount(commenter.getInteractionCount() + 1);
            commenter.checkForRepRankUpgrade();
            if(toDelete.getComment().getPost().getPostTag().equals(PostTag.QUESTION)){
                if(toDelete.getReactionType().equals(ReactionType.DISAGREE))
                    commenter.setHelpfulCount(commenter.getHelpfulCount() + 1);
                else if(toDelete.getReactionType().equals(ReactionType.HELPFUL))
                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 1);
                commenter.checkForHelpfulRankUpgrade();
            }
            commentReactionRepository.delete(toDelete);
            return;
        }
        throw new IllegalArgumentException();
    }


    public void deleteReactionByUserIdAndEntityId(Long userId, Long entityId, String reactionTo) {
        User reactor = userRepository.findById(userId).orElse(null);
        if(reactor == null)
            throw new UserNotFoundException("No reactor found with given user id.");
        if(reactionTo.equals("post")){
            Post post = postRepository.findById(entityId).orElse(null);
            if(post == null)
                throw new PostNotFoundException("Post not found.");
            PostReaction toDelete = postReactionRepository.findByReactorIdAndPost(userId, post).orElse(null);
            if(toDelete == null)
                throw new ReactionNotFoundException("Specified reaction to the post not found.");
            userDetailsService.verifyUser(userRepository.findById(toDelete.getReactorId()).get());
            postReactionRepository.delete(toDelete);
            post.setNumberOfReactions(post.getNumberOfReactions() - 1);
            User poster = toDelete.getPost().getUser();
            if(toDelete.getReactionType().equals(ReactionType.HELPFUL) ||
                    toDelete.getReactionType().equals(ReactionType.AGREE))
                poster.setInteractionCount(poster.getInteractionCount() - 1);
            else
                poster.setInteractionCount(poster.getInteractionCount() + 1);
            poster.checkForRepRankUpgrade();
            if(toDelete.getPost().getPostTag().equals(PostTag.HELPFUL_INFO)){
                if(toDelete.getReactionType().equals(ReactionType.DISAGREE))
                    poster.setHelpfulCount(poster.getHelpfulCount() + 1);
                else if(toDelete.getReactionType().equals(ReactionType.HELPFUL))
                    poster.setHelpfulCount(poster.getHelpfulCount() - 1);
                poster.checkForHelpfulRankUpgrade();
            }
            return;
        }
        else if(reactionTo.equals("comment")){
            Comment comment = commentRepository.findById(entityId).orElse(null);
            if(comment == null)
                throw new CommentNotFoundException("Comment not found.");
            CommentReaction toDelete = commentReactionRepository.findByReactorIdAndComment(userId, comment)
                    .orElse(null);
            if(toDelete == null)
                throw new ReactionNotFoundException("Specified reaction to the comment not found.");
            userDetailsService.verifyUser(userRepository.findById(toDelete.getReactorId()).get());
            commentReactionRepository.delete(toDelete);
            comment.setNumberOfReactions(comment.getNumberOfReactions() - 1);
            User commenter = toDelete.getComment().getUser();
            if(toDelete.getReactionType().equals(ReactionType.HELPFUL) ||
                    toDelete.getReactionType().equals(ReactionType.AGREE))
                commenter.setInteractionCount(commenter.getInteractionCount() - 1);
            else
                commenter.setInteractionCount(commenter.getInteractionCount() + 1);
            commenter.checkForRepRankUpgrade();
            if(toDelete.getComment().getPost().getPostTag().equals(PostTag.QUESTION)){
                if(toDelete.getReactionType().equals(ReactionType.DISAGREE))
                    commenter.setHelpfulCount(commenter.getHelpfulCount() + 1);
                else if(toDelete.getReactionType().equals(ReactionType.HELPFUL))
                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 1);
                commenter.checkForHelpfulRankUpgrade();
            }
            return;
        }
            throw new IllegalArgumentException();
    }
}
