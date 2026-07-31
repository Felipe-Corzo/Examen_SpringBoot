package com.logitrack.examen.specification;

import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.MovimientoDetalle;
import com.logitrack.model.TipoMovimiento;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification para filtrar movimientos de inventario.
 * <p>
 * Utiliza el patrón Criteria API en lugar de JPQL con {@code (? IS NULL OR column = ?)}
 * para evitar el error de PostgreSQL: {@code could not determine data type of parameter}.
 * </p>
 */
public class MovimientoInventarioSpecification {

    private MovimientoInventarioSpecification() {
        // Utility class
    }

    /**
     * Crea una Specification que aplica todos los filtros opcionales proporcionados.
     *
     * @param bodegaId        ID de la bodega (origen o destino) (opcional)
     * @param productoId      ID del producto en los detalles (opcional)
     * @param tipoMovimiento  tipo de movimiento (ENTRADA, SALIDA, TRANSFERENCIA) (opcional)
     * @param fechaInicio     fecha desde la cual buscar (opcional)
     * @param fechaFin        fecha hasta la cual buscar (opcional)
     * @return Specification<MovimientoInventario> lista para ser usada en {@code findAll(Specification, Sort)}
     */
    public static Specification<MovimientoInventario> conFiltros(
            @Nullable Long bodegaId,
            @Nullable Long productoId,
            @Nullable TipoMovimiento tipoMovimiento,
            @Nullable LocalDateTime fechaInicio,
            @Nullable LocalDateTime fechaFin) {

        return new Specification<MovimientoInventario>() {
            @Override
            public Predicate toPredicate(
                    @NonNull Root<MovimientoInventario> root,
                    @NonNull CriteriaQuery<?> query,
                    @NonNull CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                // Filtro por bodega (origen o destino)
                if (bodegaId != null) {
                    Predicate origenPredicate = cb.equal(
                            root.get("bodegaOrigen").get("id"), bodegaId);
                    Predicate destinoPredicate = cb.equal(
                            root.get("bodegaDestino").get("id"), bodegaId);
                    predicates.add(cb.or(origenPredicate, destinoPredicate));
                }

                // Filtro por producto en los detalles (requiere JOIN)
                if (productoId != null) {
                    Join<MovimientoInventario, MovimientoDetalle> detallesJoin = root.join("detalles");
                    predicates.add(cb.equal(detallesJoin.get("producto").get("id"), productoId));
                    // Usamos DISTINCT para evitar duplicados por el JOIN
                    query.distinct(true);
                }

                // Filtro por tipo de movimiento
                if (tipoMovimiento != null) {
                    predicates.add(cb.equal(root.get("tipoMovimiento"), tipoMovimiento));
                }

                // Filtro por rango de fechas
                if (fechaInicio != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), fechaInicio));
                }

                if (fechaFin != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), fechaFin));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}

