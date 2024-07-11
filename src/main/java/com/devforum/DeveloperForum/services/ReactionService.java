package com.devforum.DeveloperForum.services;

import com.devforum.DeveloperForum.entities.*;
import com.devforum.DeveloperForum.enums.PostCategory;
import com.devforum.DeveloperForum.enums.PostTag;
import com.devforum.DeveloperForum.enums.ReactionType;
import com.devforum.DeveloperForum.exceptions.CommentExceptions.CommentNotFoundException;
import com.devforum.DeveloperForum.exceptions.PostExceptions.PostNotFoundException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionAlreadyExistsException;
import com.devforum.DeveloperForum.exceptions.ReactionExceptions.ReactionNotFoundException;
import com.devforum.DeveloperForum.exceptions.UserExceptions.UserNotFoundException;
import com.devforum.DeveloperForum.repositories.*;
import com.devforum.DeveloperForum.requests.CreateReactionRequest;
import com.devforum.DeveloperForum.requests.UpdateReactionRequest;
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

    public ReactionService(CommentReactionRepository commentReactionRepository,
                           PostReactionRepository postReactionRepository, PostRepository postRepository,
                           CommentRepository commentRepository, UserRepository userRepository) {
        this.commentReactionRepository = commentReactionRepository;
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<ReactionResponse> getAllReactions(Optional<String> reactionTo) {
        List<ReactionResponse> reactions = new ArrayList<>();
        if(reactionTo.isEmpty()){
            reactions = commentReactionRepository.findAll().stream().map(ReactionResponse::new)
                    .collect(Collectors.toList());
            reactions.addAll(postReactionRepository.findAll().stream().map(ReactionResponse::new)
                    .collect(Collectors.toList()));
        }
        else if(reactionTo.get().equals("post")){
            reactions = postReactionRepository.findAll().stream().map(ReactionResponse::new).collect(Collectors.toList());
        }
        else if(reactionTo.get().equals("comment")){
            reactions = commentReactionRepository.findAll().stream().map(ReactionResponse::new).collect(Collectors.toList());
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

    public ReactionResponse createReaction(CreateReactionRequest createReactionRequest) {
        if(userRepository.findById(createReactionRequest.getReactedEntityId()).isEmpty())
            throw new UserNotFoundException("There's no user with such id to react to something.");
        ReactionType reactionType = ReactionType.valueOf(createReactionRequest.getReactionType());
        if(createReactionRequest.getReactionTo().equals("post")){
            Optional<Post> post = postRepository.findById(createReactionRequest.getReactedEntityId());
            if(post.isEmpty())
                throw new PostNotFoundException("No post found to react to.");
            if(postReactionRepository.existsByReactorIdAndAndPost(createReactionRequest.getReactorId(), post.get()))
                throw new ReactionAlreadyExistsException("Reaction already exists with given reactor and post." +
                        " This request shouldn't have been received.");
            User poster = post.get().getUser();
            PostReaction reaction = new PostReaction();
            reaction.setReactionType(reactionType);
            reaction.setPost(post.get());
            reaction.setReactorId(createReactionRequest.getReactorId());
            if(reactionType.equals(ReactionType.DISAGREE))
                poster.setInteractionCount(poster.getInteractionCount() - 1);
            else
                poster.setInteractionCount(poster.getInteractionCount() + 1);
            poster.checkForRepRankUpgrade();
            return new ReactionResponse(postReactionRepository.save(reaction));
        }
        else if(createReactionRequest.getReactionTo().equals("comment")){
            Optional<Comment> comment = commentRepository.findById(createReactionRequest.getReactedEntityId());
            if(comment.isEmpty())
                throw new CommentNotFoundException("No comment found to react to.");
            if(commentReactionRepository.existsByReactorIdAndComment
                    (createReactionRequest.getReactorId(), comment.get()))
                throw new ReactionAlreadyExistsException("Reaction already exists with given reactor and post." +
                        " This request shouldn't have been received.");
            User poster = comment.get().getUser();
            CommentReaction reaction = new CommentReaction();
            reaction.setReactionType(reactionType);
            reaction.setComment(comment.get());
            reaction.setReactorId(createReactionRequest.getReactorId());
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


    public ReactionResponse updateReactionById(Long reactionId, String newReaction) {
        ReactionType newReactionType = ReactionType.valueOf(newReaction);
        if(postReactionRepository.existsById(reactionId)){
            PostReaction reaction = postReactionRepository.findById(reactionId).get();
            User poster = reaction.getPost().getUser();
            if(newReactionType.equals(reaction.getReactionType()))
                throw new ReactionAlreadyExistsException("This reaction already exist," +
                        " this request shouldn't have been sent normally.");
            if(newReactionType.equals(ReactionType.DISAGREE)){
                poster.setInteractionCount(poster.getInteractionCount() - 2);
                poster.checkForRepRankUpgrade();
            }
            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                poster.setInteractionCount(poster.getInteractionCount() + 2);
                poster.checkForRepRankUpgrade();
            }
            reaction.setReactionType(newReactionType);
            return new ReactionResponse(postReactionRepository.save(reaction));
        }
        else if(commentReactionRepository.existsById(reactionId)){
            CommentReaction reaction = commentReactionRepository.findById(reactionId).get();
            User commenter = reaction.getComment().getUser();
            PostTag postTag = reaction.getComment().getPost().getPostTag();
            if(newReactionType.equals(reaction.getReactionType()))
                throw new ReactionAlreadyExistsException("This reaction already exist," +
                        " this request shouldn't have been sent normally.");
            if(newReactionType.equals(ReactionType.DISAGREE)){
                if(postTag.equals(PostTag.QUESTION) && reaction.getReactionType().equals(ReactionType.HELPFUL)){
                    commenter.setHelpfulCount(commenter.getHelpfulCount() - 2);
                    commenter.checkForHelpfulRankUpgrade();
                }
                commenter.setInteractionCount(commenter.getInteractionCount() - 2);
                commenter.checkForRepRankUpgrade();
            }
            else if(reaction.getReactionType().equals(ReactionType.DISAGREE)){
                if(postTag.equals(PostTag.QUESTION) && newReactionType.equals(ReactionType.HELPFUL)){
                    commenter.setHelpfulCount(commenter.getHelpfulCount() + 2);
                    commenter.checkForHelpfulRankUpgrade();
                }
                commenter.setInteractionCount(commenter.getInteractionCount() + 2);
                commenter.checkForRepRankUpgrade();
            }
            reaction.setReactionType(newReactionType);
            return new ReactionResponse(commentReactionRepository.save(reaction));
        }
        throw new ReactionNotFoundException("No such reaction found to update.");
    }

    public List<Long> getNumberOfReactionsToEntity(Long entityId, String reactionTo) {
        return null; //to be done
    }
}
