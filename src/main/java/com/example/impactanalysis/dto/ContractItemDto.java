package com.example.impactanalysis.dto;

public record ContractItemDto(
    String id,
    String name,
    String type, // OPENAPI, ASYNCAPI
    String repository,
    String version,
    String filePath,
    int operationsCount
) {}
