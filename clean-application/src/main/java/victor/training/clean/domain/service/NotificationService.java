package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.infra.EmailSender;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapUserService ldapUserService;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    //I don't want to call it get user, it's too vague.
//    It's too weak.very insignificant.
//    So fetch, retrieve, resolve something powerful
//    So I took the garbage out of my method.
//    get = in memory stuff,
//    fetch = from the outside API
//    find = from the database
    User user = ldapUserService.fetchUser(usernamePart);
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



  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = ldapUserService.fetchUser(usernamePart);

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
