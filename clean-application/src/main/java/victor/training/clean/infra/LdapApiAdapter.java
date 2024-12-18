package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
// ⚠️ INFRA should not be in domain package "DOMAIN" should not depend on "INFRASTRUCTURE"
// this class generated from the external api in infra package located in target folder by using plugin with the mvn clean install command
// right now we have a dependency injection from outside the domain package, and we need to convert it inverse injection
// which is an adapter?  1. NotificationService 2. LdapApiAdapter  3. LdapApi
// LdapApiAdapter is an adapter design pattern
import victor.training.clean.domain.service.User;
import victor.training.clean.domain.service.UserFetcher;

import java.util.List;
import java.util.Optional;

//Basically hiding inside this class all the misery of the external api bad names...
//So you create the structure you map in front of you to the structure so that it's all clear,
// and then you start it as a method and then as a separate class.
// Of course the class will not stay here once it's done, and it compiles, you need to move class to a different place.
// = Adapter design pattern = protects your core from outside world
@RequiredArgsConstructor
@Slf4j
@Service
public class LdapApiAdapter implements UserFetcher {
  private final LdapApi ldapApi;


  @Override
  public User fetchUser(String usernamePart) {
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
}
