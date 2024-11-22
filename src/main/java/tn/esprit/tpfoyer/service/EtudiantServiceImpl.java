package tn.esprit.tpfoyer.service;


import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EtudiantServiceImpl implements IEtudiantService {

    private static final Logger logger = LogManager.getLogger(EtudiantServiceImpl.class);

    EtudiantRepository etudiantRepository;

    public List<Etudiant> retrieveAllEtudiants() {
        logger.info("Retrieving all students");
        return etudiantRepository.findAll();
    }

    public Etudiant retrieveEtudiant(Long etudiantId) {
        logger.info("Retrieving student with ID: {}", etudiantId);
        return etudiantRepository.findById(etudiantId).orElse(null);
    }

    public Etudiant addEtudiant(Etudiant c) {
        logger.info("Adding new student: {}", c);
        return etudiantRepository.save(c);
    }

    public Etudiant modifyEtudiant(Etudiant c) {
        logger.info("Modifying student with ID: {}", c.getIdEtudiant());
        return etudiantRepository.save(c);
    }

    public void removeEtudiant(Long etudiantId) {
        logger.info("Deleting student with ID: {}", etudiantId);
        etudiantRepository.deleteById(etudiantId);
    }

    public Etudiant recupererEtudiantParCin(long cin) {
        logger.info("Retrieving student with CIN: {}", cin);
        return etudiantRepository.findEtudiantByCinEtudiant(cin);
    }
}

