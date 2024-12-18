package victor.training.clean.domain.service;

import java.util.Optional;

public record User(
        String username,
        Optional<String> email,
        String fullName
){
//  this is not perfect.I want to have a method called return a contact
//  because look if I don't have an e-mail, I don't have a contact. As a matter
//  of fact
//    public String asContact() {
//      return fullName()
//              + " <" + email().get().toLowerCase() + ">";
//    }

    public Optional<String> asContact() {
        return email.map(e -> fullName + " <" + e.toLowerCase() + ">");
    }
}
