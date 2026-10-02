package GemPilot.backend.services.ai;

import java.util.List;

import GemPilot.backend.dto.CitationDto;

public record RetrievedContext(
        List<CitationDto> citations,
        String contextText) {
}