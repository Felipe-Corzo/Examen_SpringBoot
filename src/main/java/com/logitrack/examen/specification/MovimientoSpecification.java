package com.logitrack.examen.specification;

import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.TipoMovimiento;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class MovimientoSpecification {

    public static Specification<MovimientoInventario> filtrar(
            Long bodegaId,
            Long productoId,
            TipoMovimiento tipoMovimiento,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        return (root, query, cb) -> {

            query.distinct(true);

            var predicates = cb.conjunction();

            if (bodegaId != null) {
                predicates.getExpressions().add(
                        cb.or(
                                cb.equal(root.get("bodegaOrigen").get("id"), bodegaId),
                                cb.equal(root.get("bodegaDestino").get("id"), bodegaId)
                        )
                );
            }

            if (productoId != null) {

                Join<Object, Object> detalle =
                        root.join("detalles");

                predicates.getExpressions().add(
                        cb.equal(detalle.get("producto").get("id"), productoId)
                );
            }

            if (tipoMovimiento != null) {
                predicates.getExpressions().add(
                        cb.equal(root.get("tipoMovimiento"), tipoMovimiento)
                );
            }

            if (fechaInicio != null) {
                predicates.getExpressions().add(
                        cb.greaterThanOrEqualTo(
                                root.get("fecha"),
                                fechaInicio
                        )
                );
            }

            if (fechaFin != null) {
                predicates.getExpressions().add(
                        cb.lessThanOrEqualTo(
                                root.get("fecha"),
                                fechaFin
                        )
                );
            }

            return predicates;
        };
    }
}