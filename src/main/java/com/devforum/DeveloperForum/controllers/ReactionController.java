package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.requests.CreateReactionRequest;
import com.devforum.DeveloperForum.requests.UpdateReactionRequest;
import com.devforum.DeveloperForum.responses.ReactionResponse;
import com.devforum.DeveloperForum.services.ReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reactions")
public class ReactionController {
    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public List<ReactionResponse> getAllReactions(@RequestParam Optional<String> reactionTo){
        return reactionService.getAllReactions(reactionTo);
    }

    @GetMapping("/{reactionId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ReactionResponse findReactionById(@PathVariable Long reactionId, @RequestParam String reactionTo){
        return reactionService.findReactionById(reactionId, reactionTo);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionResponse createReaction(@RequestBody CreateReactionRequest createReactionRequest){
        return reactionService.createReaction(createReactionRequest);
    }

    @PutMapping("/{reactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ReactionResponse updateReactionById(@PathVariable Long reactionId, @RequestParam String newReaction){
        return reactionService.updateReactionById(reactionId,newReaction);
    }

    @GetMapping("/numbers/{entityId}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Long> getNumberOfReactionsToEntity(@PathVariable Long entityId, @RequestParam String reactionTo){
        return reactionService.getNumberOfReactionsToEntity(entityId, reactionTo);
    }
}
