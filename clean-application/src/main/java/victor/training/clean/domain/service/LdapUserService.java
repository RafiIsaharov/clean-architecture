package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

//So you create the structure you map in front of you to the structure so that it's all clear,
// and then you start it as a method and then as a separate class.
// Of course the class will not stay here once it's done, and it compiles, you need to move class to a different place.
@RequiredArgsConstructor
@Slf4j
@Service
public class LdapUserService {
  private final LdapApi ldapApi;

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
