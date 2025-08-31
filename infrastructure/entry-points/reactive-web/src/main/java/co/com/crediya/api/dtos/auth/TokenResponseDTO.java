package co.com.crediya.api.dtos.auth;

public record TokenResponseDTO(
        String typeToken,
        String accessToken,
        String email
) {
}
