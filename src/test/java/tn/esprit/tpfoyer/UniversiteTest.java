package tn.esprit.tpfoyer;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UniversiteTest {
    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteServiceImpl universiteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUniversite() {
        Universite universiteToAdd = new Universite();
        universiteToAdd.setNomUniversite("University of Test");
        universiteToAdd.setAdresse("123 Test St");

        when(universiteRepository.save(universiteToAdd)).thenReturn(universiteToAdd);

        Universite result = universiteService.addUniversite(universiteToAdd);

        assertNotNull(result);
        assertEquals(universiteToAdd.getNomUniversite(), result.getNomUniversite());
    }



    @Test
    void testRetrieveUniversiteNotFound() {
        Long universiteId = 1L;

        when(universiteRepository.findById(universiteId)).thenReturn(Optional.empty());

        // Changer l'exception attendue en NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> universiteService.retrieveUniversite(universiteId));
    }

    @Test
    void testRetrieveUniversite() {
        Long universiteId = 1L;
        Universite universite = new Universite();
        universite.setIdUniversite(universiteId);
        universite.setNomUniversite("University of Test");

        when(universiteRepository.findById(universiteId)).thenReturn(Optional.of(universite));

        // Mise à jour pour correspondre au type de retour de la méthode
        Universite result = universiteService.retrieveUniversite(universiteId);

        assertNotNull(result);
        assertEquals(universite.getNomUniversite(), result.getNomUniversite());
    }

    @Test
    void testDeleteUniversite() {
        Long universiteId = 1L;

        universiteService.removeUniversite(universiteId);

        verify(universiteRepository, times(1)).deleteById(universiteId);
    }
}

