package io.legacyfighter.cabs.ui;

import io.legacyfighter.cabs.dto.AddressDTO;
import io.legacyfighter.cabs.dto.AnalyzedAddressesDTO;
import io.legacyfighter.cabs.repository.AddressRepository;
import io.legacyfighter.cabs.transitanalyzer.GraphTransitAnalyzer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransitAnalyzerController {

    private final GraphTransitAnalyzer graphTransitAnalyzer;
    private final AddressRepository addressRepository;

    public TransitAnalyzerController(GraphTransitAnalyzer graphTransitAnalyzer, AddressRepository addressRepository) {
        this.graphTransitAnalyzer = graphTransitAnalyzer;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/transitAnalyze/{clientId}/{addressId}")
    public AnalyzedAddressesDTO analyze(@PathVariable Long clientId, @PathVariable Long addressId) {
        List<Long> hashes = graphTransitAnalyzer.analyze(clientId, addressRepository.findHashById(addressId));
        List<AddressDTO> addressDTOs = hashes
                .stream()
                .map(this::mapToAddressDTO)
                .collect(Collectors.toList());

        return new AnalyzedAddressesDTO(addressDTOs);
    }

    private AddressDTO mapToAddressDTO(Long hash) {
        return new AddressDTO(addressRepository.getByHash(hash.intValue()));
    }
}
