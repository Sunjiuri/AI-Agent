package com.aiagent.model;

/**
 * Internal result from the agent service holding the resolved conversation ID
 * and the AI-generated reply.
 *
 * @param conversationId the conversation ID (may be a newly generated one)
 * @param reply          the AI-generated reply text
 */
public record ChatResult(String conversationId, String reply) {
}
