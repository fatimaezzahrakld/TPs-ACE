package com.example.student_management.service;

import com.example.student_management.entity.Student;
import com.example.student_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Enregistre un étudiant dans la base de données
     * @param student l'étudiant à enregistrer
     * @return l'étudiant enregistré avec son ID généré
     */
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Supprime un étudiant par son ID
     * @param id l'identifiant de l'étudiant à supprimer
     * @return true si l'étudiant a été supprimé, false s'il n'existe pas
     */
    public boolean delete(int id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentRepository.findById(id));
        if (studentOptional.isPresent()) {
            studentRepository.delete(studentOptional.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne la liste de tous les étudiants
     * @return liste de tous les étudiants
     */
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Compte le nombre total d'étudiants
     * @return le nombre d'étudiants
     */
    public long countStudents() {
        return studentRepository.count();
    }

    /**
     * Retourne le nombre d'étudiants par année de naissance
     * @return collection d'objets contenant l'année et le nombre d'étudiants
     */
    public Collection<?> findNbrStudentByYear() {
        return studentRepository.findNbrStudentByYear();
    }
}