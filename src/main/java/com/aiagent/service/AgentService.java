package com.aiagent.service;

import com.aiagent.model.ChatResult;
import com.aiagent.tools.AgentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Core AI agent service.
 *
 * <p>Each conversation is identified by a {@code conversationId}. History is stored in an
 * in-memory {@link InMemoryChatMemoryRepository} wrapped by a
 * {@link MessageWindowChatMemory} and injected into every request via
 * {@link MessageChatMemoryAdvisor}.
 */
@Service
public class AgentService {

    private static final String SYSTEM_PROMPT = """
            You are a helpful AI assistant with access to the following tools:
            - currentDateTime: returns the current date and time
            - getWeather: returns the weather for a given city
            - calculate: performs basic arithmetic calculations (+, -, *, /)

            Always be concise, accurate, and friendly. When a tool can help answer the
            user's question, use it.
            """;

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AgentService(ChatClient.Builder builder, AgentTools agentTools) {
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();

        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(agentTools)
                .build();
    }

    /**
     * Sends a message to the agent and returns a {@link ChatResult} containing the
     * resolved conversation ID and the AI-generated reply.
     *
     * @param conversationId identifies the conversation; a new ID is generated when null
     * @param userMessage    the user's input
     * @return the chat result with conversation ID and reply text
     */
    public ChatResult chat(String conversationId, String userMessage) {
        String resolvedId = (conversationId != null && !conversationId.isBlank())
                ? conversationId
                : UUID.randomUUID().toString();

        String reply = chatClient.prompt()
                .user(userMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, resolvedId))
                .call()
                .content();

        return new ChatResult(resolvedId, reply);
    }

    /**
     * Removes all history for the given conversation.
     *
     * @param conversationId the conversation to clear
     */
    public void clearConversation(String conversationId) {
        chatMemory.clear(conversationId);
    }
}
