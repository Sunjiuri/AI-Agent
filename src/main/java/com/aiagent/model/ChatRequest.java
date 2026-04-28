package com.aiagent.model;

/**
 * Request body for the chat endpoint.
 *
 * @param message        the user's message
 * @param conversationId optional conversation ID for continuing an existing conversation;
 *                       when null a new conversation is started
 */
public record ChatRequest(String message, String conversationId) {
}
