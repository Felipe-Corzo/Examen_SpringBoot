package com.logitrack.examen.specification;

import com.logitrack.model.Auditoria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification para filtrar registros de Auditoría.
 * <p>
 * Utiliza el patrón Criteria API en lugar de JPQL con {@code (? IS NULL OR column = ?)}
 * para evitar el error de PostgreSQL: {@code could not determine data type of parameter}.
 * </p>
 */
public class AuditoriaSpecification {

    private AuditoriaSpecification() {
        // Utility class
    }

    /**
     * Crea una Specification que aplica todos los filtros opcionales proporcionados.
     *
     * @param entidadId        ID de la entidad afectada (opcional)
     * @param campoModificado  nombre del campo modificado (opcional)
     * @param fechaInicio      fecha desde la cual buscar (opcional)
     * @param fechaFin         fecha hasta la cual buscar (opcional)
     * @return Specification<Auditoria> lista para ser usada en {@code findAll(Specification, Sort)}
     */
    public static Specification<Auditoria> conFiltros(
            @Nullable Long entidadId,
            @Nullable String campoModificado,
            @Nullable LocalDateTime fechaInicio,
            @Nullable LocalDateTime fechaFin) {

        return new Specification<Auditoria>() {
            @Override
            public Predicate toPredicate(
                    @NonNull Root<Auditoria> root,
                    @NonNull CriteriaQuery<?> query,
                    @NonNull CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                if (entidadId != null) {
                    predicates.add(cb.equal(root.get("entidadId"), entidadId));
                }

                if (campoModificado != null && !campoModificado.trim().isEmpty()) {
                    predicates.add(cb.equal(root.get("campoModificado"), campoModificado));
                }

                if (fechaInicio != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("fechaHora"), fechaInicio));
                }

                if (fechaFin != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("fechaHora"), fechaFin));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}

