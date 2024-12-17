package victor.training.clean.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

//@RestController
@RequestMapping("customer-leaking")
@RequiredArgsConstructor
public class CustomerLeakingController {
   private final CustomerRepo customerRepo;
   // - Data Transfer Object = used in communication with the outside world
   // - Domain Object = used in core logic like Business Object in our projects

   @GetMapping("{id}")
   public Customer findById(@PathVariable long id) {
      // TODO return a CustomerDto instead!!!
      // WRONG because tomorrow we might need to add a field to the DTO
      // and we'll have to change all controllers
      return customerRepo.findById(id).orElseThrow();
   }

}
