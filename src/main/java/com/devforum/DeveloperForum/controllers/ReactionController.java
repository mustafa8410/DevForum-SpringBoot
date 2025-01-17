package com.devforum.DeveloperForum.controllers;

import com.devforum.DeveloperForum.requests.ReactionRequests.ReactionCreateRequest;
import com.devforum.DeveloperForum.requests.ReactionRequests.ReactionUpdateRequest;
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
    public List<ReactionResponse> getAllReactions(@RequestParam Optional<String> reactionTo, Optional<Long> entityId){
        return reactionService.getAllReactions(reactionTo, entityId);
    }

    @GetMapping("/{reactionId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ReactionResponse findReactionById(@PathVariable Long reactionId, @RequestParam String reactionTo){
        return reactionService.findReactionById(reactionId, reactionTo);
    }

    @GetMapping("/numbers/{entityId}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Long> getNumberOfReactionsToEntity(@PathVariable Long entityId, @RequestParam String reactionTo){
        return reactionService.getNumberOfReactionsToEntity(entityId, reactionTo);
    }

    @GetMapping("/specific")
    @ResponseStatus(HttpStatus.FOUND)
    public ReactionResponse findReactionByUserIdAndEntityId(@RequestParam Long userId, @RequestParam Long entityId,
                                                            @RequestParam String reactionTo){
        return reactionService.findReactionByUserIdAndEntityId(userId, entityId, reactionTo);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionResponse createReaction(@RequestBody ReactionCreateRequest reactionCreateRequest){
        return reactionService.createReaction(reactionCreateRequest);
    }

    @PutMapping("/{reactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ReactionResponse updateReactionById(@PathVariable Long reactionId, @RequestParam String reactionTo,
                                               @RequestParam String newReaction){
        return reactionService.updateReactionById(reactionId, reactionTo, newReaction);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ReactionResponse updateReactionByUserIdAndEntityId(@RequestParam Long userId, @RequestParam Long entityId,
                                                              @RequestParam String reactionTo,
                                                              @RequestBody ReactionUpdateRequest reactionUpdateRequest){
        return reactionService.updateReactionByUserIdAndEntityId(userId, entityId, reactionTo, reactionUpdateRequest);
    }

    @DeleteMapping("/{reactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReactionById(@PathVariable Long reactionId, @RequestParam String reactionTo){
        reactionService.deleteReactionById(reactionId, reactionTo);
    }

    @DeleteMapping("/specific")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReactionByUserIdAndEntityId(@RequestParam Long userId, @RequestParam Long entityId,
                                                  @RequestParam String reactionTo){
        reactionService.deleteReactionByUserIdAndEntityId(userId, entityId, reactionTo);
    }


}
