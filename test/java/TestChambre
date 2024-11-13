package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.ChambreRestController;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.service.IChambreService;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestChambre {

    @Mock
    private IChambreService chambreService;

    @InjectMocks
    private ChambreRestController chambreRestController;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chambre = new Chambre(1L, 101L, TypeChambre.SIMPLE, null, null);
    }

    @Test
    void testGetChambres() {
        // Arrange
        List<Chambre> chambres = Arrays.asList(chambre);
        when(chambreService.retrieveAllChambres()).thenReturn(chambres);

        // Act
        List<Chambre> result = chambreRestController.getChambres();

        // Assert
        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).getNumeroChambre());
        verify(chambreService, times(1)).retrieveAllChambres();
    }

    @Test
    void testRetrieveChambre() {
        // Arrange
        when(chambreService.retrieveChambre(1L)).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.retrieveChambre(1L);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreService, times(1)).retrieveChambre(1L);
    }

    @Test
    void testAddChambre() {
        // Arrange
        when(chambreService.addChambre(any(Chambre.class))).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.addChambre(chambre);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreService, times(1)).addChambre(any(Chambre.class));
    }

    @Test
    void testModifyChambre() {
        // Arrange
        when(chambreService.modifyChambre(any(Chambre.class))).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.modifyChambre(chambre);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreService, times(1)).modifyChambre(any(Chambre.class));
    }

    @Test
    void testRemoveChambre() {
        // Act
        chambreRestController.removeChambre(1L);

        // Assert
        verify(chambreService, times(1)).removeChambre(1L);
    }

    @Test
    void testTrouverChSelonTC() {
        // Arrange
        List<Chambre> chambres = Arrays.asList(chambre);
        when(chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE)).thenReturn(chambres);

        // Act
        List<Chambre> result = chambreRestController.trouverChSelonTC(TypeChambre.SIMPLE);

        // Assert
        assertEquals(1, result.size());
        verify(chambreService, times(1)).recupererChambresSelonTyp(TypeChambre.SIMPLE);
    }

    @Test
    void testTrouverChSelonEt() {
        // Arrange
        when(chambreService.trouverchambreSelonEtudiant(12345678L)).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.trouverChSelonEt(12345678L);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreService, times(1)).trouverchambreSelonEtudiant(12345678L);
    }
}
