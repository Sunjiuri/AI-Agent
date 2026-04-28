package com.aiagent.controller;

import com.aiagent.model.ChatRequest;
import com.aiagent.model.ChatResponse;
import com.aiagent.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing the AI agent endpoints.
 *
 * <pre>
 * POST /api/chat                – start or continue a conversation
 * DELETE /api/chat/{id}         – clear the conversation history for {id}
 * </pre>
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AgentService agentService;

    public ChatController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * Sends a user message to the agent.
     *
     * <p>If {@code conversationId} is absent or blank a new conversation is started and
     * the generated ID is returned in the response so that the client can supply it in
     * subsequent requests.
     *
     * @param request body containing {@code message} and optional {@code conversationId}
     * @return the agent's reply together with the conversation ID
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.message() == null || request.message().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String[] result = agentService.chat(request.conversationId(), request.message());
        return ResponseEntity.ok(new ChatResponse(result[0], result[1]));
    }

    /**
     * Clears the conversation history identified by {@code id}.
     *
     * @param id the conversation ID to clear
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> clearConversation(@PathVariable String id) {
        agentService.clearConversation(id);
        return ResponseEntity.noContent().build();
    }
}
