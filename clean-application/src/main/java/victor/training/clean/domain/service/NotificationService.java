package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    //I don't want to call it get user, it's too vague.
//    It's too weak.very insignificant.
//    So fetch, retrieve, resolve something powerful
//    So I took the garbage out of my method.
//    get = in memory stuff,
//    fetch = from the outside API
//    find = from the database
    User user = fetchUser(usernamePart);
    // ⚠️ Data mapping mixed with core logic TODO pull it earlier

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + user.fullName())
        .build();


//    if (user.email().isPresent()) {
//      String contact = user.asContact();
//      email.getCc().add(contact);
//    }

    user.asContact().ifPresent(email.getCc()::add);

    emailSender.sendEmail(email);

    // ⚠️ Swap this line with next one to cause a bug (=TEMPORAL COUPLING) TODO make immutable💚

    // ⚠️ 'un' = bad name TODO in my ubiquitous language 'un' means 'username'
    customer.setCreatedByUsername(user.username());
  }

  private User fetchUser(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
    return new User(ldapUserDto.getUn(), Optional.ofNullable(ldapUserDto.getWorkEmail()), ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase());
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = fetchUser(usernamePart);

      String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.fullName())
        .build();
//    if(user.email().isPresent()) {
//      String contact = user.asContact();
//      email.getCc().add(contact);
//    }
    user.asContact().ifPresent(email.getCc()::add);
    emailSender.sendEmail(email);
  }

}
