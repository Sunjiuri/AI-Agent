package com.aiagent.model;

/**
 * Response body returned by the chat endpoint.
 *
 * @param conversationId the conversation ID that can be passed in subsequent requests
 * @param reply          the AI-generated reply text
 */
public record ChatResponse(String conversationId, String reply) {
}
