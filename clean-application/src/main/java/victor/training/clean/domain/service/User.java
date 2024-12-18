package victor.training.clean.domain.service;

import java.util.Optional;

public record User(
        String username,
        Optional<String> email,
        String fullName
){
}
