package io.bootify.vms_minor_project.service;

import io.bootify.vms_minor_project.domain.Address;
import io.bootify.vms_minor_project.domain.User;
import io.bootify.vms_minor_project.domain.Visitor;
import io.bootify.vms_minor_project.model.AddressDTO;
import io.bootify.vms_minor_project.repos.AddressRepository;
import io.bootify.vms_minor_project.repos.UserRepository;
import io.bootify.vms_minor_project.repos.VisitorRepository;
import io.bootify.vms_minor_project.util.NotFoundException;
import io.bootify.vms_minor_project.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final VisitorRepository visitorRepository;

    public AddressService(final AddressRepository addressRepository,
            final UserRepository userRepository, final VisitorRepository visitorRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.visitorRepository = visitorRepository;
    }

    public List<AddressDTO> findAll() {
        final List<Address> addresses = addressRepository.findAll(Sort.by("id"));
        return addresses.stream()
                .map(address -> mapToDTO(address, new AddressDTO()))
                .toList();
    }

    public AddressDTO get(final Long id) {
        return addressRepository.findById(id)
                .map(address -> mapToDTO(address, new AddressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getId();
    }

    public void update(final Long id, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    public void delete(final Long id) {
        addressRepository.deleteById(id);
    }

    public AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setLine1(address.getLine1());
        addressDTO.setLin2(address.getLin2());
        addressDTO.setPincode(address.getPincode());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setLine1(addressDTO.getLine1());
        address.setLin2(addressDTO.getLin2());
        address.setPincode(addressDTO.getPincode());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        return address;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User addressUser = userRepository.findFirstByAddress(address);
        if (addressUser != null) {
            referencedWarning.setKey("address.user.address.referenced");
            referencedWarning.addParam(addressUser.getId());
            return referencedWarning;
        }
        final Visitor adressVisitor = visitorRepository.findFirstByAdress(address);
        if (adressVisitor != null) {
            referencedWarning.setKey("address.visitor.adress.referenced");
            referencedWarning.addParam(adressVisitor.getId());
            return referencedWarning;
        }
        return null;
    }

}
