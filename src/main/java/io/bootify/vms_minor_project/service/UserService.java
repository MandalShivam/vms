package io.bootify.vms_minor_project.service;

import io.bootify.vms_minor_project.domain.Address;
import io.bootify.vms_minor_project.domain.Flat;
import io.bootify.vms_minor_project.domain.User;
import io.bootify.vms_minor_project.domain.Visit;
import io.bootify.vms_minor_project.model.AddressDTO;
import io.bootify.vms_minor_project.model.UserDTO;
import io.bootify.vms_minor_project.model.UserStatus;
import io.bootify.vms_minor_project.repos.AddressRepository;
import io.bootify.vms_minor_project.repos.FlatRepository;
import io.bootify.vms_minor_project.repos.UserRepository;
import io.bootify.vms_minor_project.repos.VisitRepository;
import io.bootify.vms_minor_project.util.NotFoundException;
import io.bootify.vms_minor_project.util.ReferencedWarning;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final FlatRepository flatRepository;
    private final AddressRepository addressRepository;
    private final VisitRepository visitRepository;
    @Autowired
    private AddressService addressService;

    public UserService(final UserRepository userRepository, final FlatRepository flatRepository,
            final AddressRepository addressRepository, final VisitRepository visitRepository) {
        this.userRepository = userRepository;
        this.flatRepository = flatRepository;
        this.addressRepository = addressRepository;
        this.visitRepository = visitRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void markUserInActive(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new NotFoundException("User does not exists");
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    public void markUserActive(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            throw new NotFoundException("User does not exists");
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }


    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNo(user.getPhoneNo());
        userDTO.setRole(user.getRole());
        userDTO.setStatus(user.getStatus());
        userDTO.setFlatNumber(user.getFlat().getNumber());

        AddressDTO addressDTO = new AddressDTO();
        addressService.mapToDTO(user.getAddress(),addressDTO);

        userDTO.setAddress(addressDTO);
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNo(userDTO.getPhoneNo());
        user.setRole(userDTO.getRole());
        user.setStatus(userDTO.getStatus());
       Flat flat = flatRepository.findByNumber(userDTO.getFlatNumber());
       if(flat == null) {
           flat = new Flat();
           flat.setNumber(userDTO.getFlatNumber());
           flatRepository.save(flat);
       }
       user.setFlat(flat);
       Address address = new Address();
       if(userDTO.getAddress()!=null) {
           address = addressService.mapToEntity(userDTO.getAddress(),address);
          // addressRepository.save(address);
       }
       user.setAddress(address);
       return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneNoExists(final String phoneNo) {
        return userRepository.existsByPhoneNoIgnoreCase(phoneNo);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Visit userVisit = visitRepository.findFirstByUser(user);
        if (userVisit != null) {
            referencedWarning.setKey("user.visit.user.referenced");
            referencedWarning.addParam(userVisit.getId());
            return referencedWarning;
        }
        return null;
    }

}
