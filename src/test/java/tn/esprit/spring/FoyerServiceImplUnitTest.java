package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoyerServiceImplUnitTest {

    @InjectMocks
    private FoyerServiceImpl foyerService; // Class under test

    @Mock
    private FoyerRepository foyerRepository; // Mock dependency
    @BeforeEach
    void setup() {
        lenient().when(foyerRepository.findAll()).thenReturn(Arrays.asList(
                new Foyer(1L, "Foyer A", 50, null, null),
                new Foyer(2L, "Foyer B", 100, null, null)
        ));
        lenient().when(foyerRepository.findById(1L)).thenReturn(Optional.of(
                new Foyer(1L, "Foyer A", 50, null, null)
        ));
    }



    @Test
    void testRetrieveAllFoyers() {
        // Call the service method
        List<Foyer> foyers = foyerService.retrieveAllFoyers();

        // Validate results
        assertEquals(2, foyers.size());
        assertEquals("Foyer A", foyers.get(0).getName());
        assertEquals("Foyer B", foyers.get(1).getName());

        // Verify repository interaction
        verify(foyerRepository, times(1)).findAll();
    }


    @Test
    void testAddFoyer() {
        Foyer newFoyer = new Foyer();
        newFoyer.setName("New Foyer");
        newFoyer.setCapaciteFoyer(200);

        // Mock repository behavior
        when(foyerRepository.save(newFoyer)).thenReturn(new Foyer(1L, "New Foyer", 200, null, null));

        // Call the service method
        Foyer savedFoyer = foyerService.addFoyer(newFoyer);

        // Validate results
        assertEquals("New Foyer", savedFoyer.getName());
        assertEquals(200, savedFoyer.getCapaciteFoyer());

        // Verify repository interaction
        verify(foyerRepository, times(1)).save(newFoyer);
    }


    @Test
    void testRetrieveFoyer() {
        // Call the service method
        Foyer retrievedFoyer = foyerService.retrieveFoyer(1L);

        // Validate results
        assertNotNull(retrievedFoyer);
        assertEquals(1L, retrievedFoyer.getId());
        assertEquals("Foyer A", retrievedFoyer.getName());
        assertEquals(50, retrievedFoyer.getCapaciteFoyer());

        // Verify repository interaction
        verify(foyerRepository, times(1)).findById(1L);
    }


    @Test
    void testModifyFoyer() {
        Foyer existingFoyer = new Foyer(1L, "Foyer A", 50, null, null);
        Foyer modifiedFoyer = new Foyer(1L, "Updated Foyer", 100, null, null);

        // Mock repository behavior
        when(foyerRepository.save(existingFoyer)).thenReturn(modifiedFoyer);

        // Call the service method
        Foyer updatedFoyer = foyerService.modifyFoyer(existingFoyer);

        // Validate results
        assertEquals("Updated Foyer", updatedFoyer.getName());
        assertEquals(100, updatedFoyer.getCapaciteFoyer());

        // Verify repository interaction
        verify(foyerRepository, times(1)).save(existingFoyer);
    }


    @Test
    void testRemoveFoyer() {
        // Call the service method
        foyerService.removeFoyer(1L);

        // Verify repository interaction
        verify(foyerRepository, times(1)).deleteById(1L);
    }
}
