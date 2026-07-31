package com.logitrack.repository;

import com.logitrack.model.MovimientoInventario;
import com.logitrack.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long>,
        JpaSpecificationExecutor<MovimientoInventario> {

    List<MovimientoInventario> findByTipoMovimiento(TipoMovimiento tipoMovimiento);

    List<MovimientoInventario> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.bodegaOrigen.id = :bodegaId OR m.bodegaDestino.id = :bodegaId")
    List<MovimientoInventario> findByBodegaId(@Param("bodegaId") Long bodegaId);

    @Query("SELECT m FROM MovimientoInventario m ORDER BY m.fecha DESC")
    List<MovimientoInventario> findAllOrderByFechaDesc();


    @Query("""
    SELECT DISTINCT m
    FROM MovimientoInventario m
    LEFT JOIN m.detalles d
    WHERE
        (:bodegaId IS NULL OR
         m.bodegaOrigen.id = :bodegaId OR
         m.bodegaDestino.id = :bodegaId)
    AND
        (:productoId IS NULL OR
         d.producto.id = :productoId)
    AND
        (:tipoMovimiento IS NULL OR
         m.tipoMovimiento = :tipoMovimiento)
    AND
        (:fechaInicio IS NULL OR
         m.fecha >= :fechaInicio)
    AND
        (:fechaFin IS NULL OR
         m.fecha <= :fechaFin)
""")
List<MovimientoInventario> buscarMovimientosConFiltros(
        @Param("bodegaId") Long bodegaId,
        @Param("productoId") Long productoId,
        @Param("tipoMovimiento") TipoMovimiento tipoMovimiento,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
);
}


