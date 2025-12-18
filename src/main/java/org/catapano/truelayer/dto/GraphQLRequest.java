package org.catapano.truelayer.dto;

public record GraphQLRequest(String query, Object variables) {}