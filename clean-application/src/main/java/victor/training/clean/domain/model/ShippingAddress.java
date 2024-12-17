package victor.training.clean.domain.model;
//Value Object design pattern - immutable, no behavior, no identity
public record ShippingAddress(
        String city,
        String street,
        String zip) {
}
