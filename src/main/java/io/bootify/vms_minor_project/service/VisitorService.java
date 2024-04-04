package io.bootify.vms_minor_project.service;

import io.bootify.vms_minor_project.domain.Address;
import io.bootify.vms_minor_project.domain.Visit;
import io.bootify.vms_minor_project.domain.Visitor;
import io.bootify.vms_minor_project.model.AddressDTO;
import io.bootify.vms_minor_project.model.VisitorDTO;
import io.bootify.vms_minor_project.repos.AddressRepository;
import io.bootify.vms_minor_project.repos.VisitRepository;
import io.bootify.vms_minor_project.repos.VisitorRepository;
import io.bootify.vms_minor_project.util.NotFoundException;
import io.bootify.vms_minor_project.util.ReferencedWarning;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final AddressRepository addressRepository;
    private final VisitRepository visitRepository;

    @Autowired
    private AddressService addressService;

    public VisitorService(final VisitorRepository visitorRepository,
            final AddressRepository addressRepository, final VisitRepository visitRepository) {
        this.visitorRepository = visitorRepository;
        this.addressRepository = addressRepository;
        this.visitRepository = visitRepository;
    }

    public List<VisitorDTO> findAll() {
        final List<Visitor> visitors = visitorRepository.findAll(Sort.by("id"));
        return visitors.stream()
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .toList();
    }

    public VisitorDTO get(final Long id) {
        return visitorRepository.findById(id)
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VisitorDTO visitorDTO) {
        final Visitor visitor = new Visitor();
        mapToEntity(visitorDTO, visitor);
        return visitorRepository.save(visitor).getId();
    }

    public void update(final Long id, final VisitorDTO visitorDTO) {
        final Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitorDTO, visitor);
        visitorRepository.save(visitor);
    }

    public void delete(final Long id) {
        visitorRepository.deleteById(id);
    }

    private VisitorDTO mapToDTO(final Visitor visitor, final VisitorDTO visitorDTO) {
        visitorDTO.setId(visitor.getId());
        visitorDTO.setName(visitor.getName());
        visitorDTO.setEmail(visitor.getEmail());
        visitorDTO.setPhoneNo(visitor.getPhoneNo());
        visitorDTO.setIdNumber(visitor.getIdNumber());
        final AddressDTO addressDTO = new AddressDTO();
        addressService.mapToDTO(visitor.getAdress(),addressDTO);
        visitorDTO.setAddress(addressDTO);
//        visitorDTO.setAdress(visitor.getAdress() == null ? null : visitor.getAdress().getId());
        return visitorDTO;
    }

    private Visitor mapToEntity(final VisitorDTO visitorDTO, final Visitor visitor) {
        visitor.setName(visitorDTO.getName());
        visitor.setEmail(visitorDTO.getEmail());
        visitor.setPhoneNo(visitorDTO.getPhoneNo());
        visitor.setIdNumber(visitorDTO.getIdNumber());
        final Address address = new Address();
        addressService.mapToEntity(visitorDTO.getAddress(),address);
        /*final Address adress = visitorDTO.getAdress() == null ? null : addressRepository.findById(visitorDTO.getAdress())
                .orElseThrow(() -> new NotFoundException("adress not found"));*/
        visitor.setAdress(address);
        addressRepository.save(address);
        return visitor;
    }

    public boolean emailExists(final String email) {
        return visitorRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneNoExists(final String phoneNo) {
        return visitorRepository.existsByPhoneNoIgnoreCase(phoneNo);
    }

    public boolean idNumberExists(final String idNumber) {
        return visitorRepository.existsByIdNumberIgnoreCase(idNumber);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Visit visitorVisit = visitRepository.findFirstByVisitor(visitor);
        if (visitorVisit != null) {
            referencedWarning.setKey("visitor.visit.visitor.referenced");
            referencedWarning.addParam(visitorVisit.getId());
            return referencedWarning;
        }
        return null;
    }

}
