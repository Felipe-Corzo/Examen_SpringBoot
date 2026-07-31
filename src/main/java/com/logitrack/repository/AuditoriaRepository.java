package com.logitrack.repository;

import com.logitrack.model.Auditoria;
import com.logitrack.model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

import java.util.List;


public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByEntidadAfectadaIgnoreCase(String entidadAfectada);

    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);

    List<Auditoria> findByUsuario_Id(Long usuarioId);

    List<Auditoria> findAllByOrderByFechaHoraDesc();


    @Query("""
    SELECT a
    FROM Auditoria a
    WHERE
        (:productoId IS NULL OR
         a.entidadId = :productoId)
    AND
        (:fechaInicio IS NULL OR
         a.fechaHora >= :fechaInicio)
    AND
        (:fechaFin IS NULL OR
         a.fechaHora <= :fechaFin)
    AND
        (
            :campoModificado IS NULL OR
            LOWER(a.valoresAnteriores) LIKE LOWER(CONCAT('%', :campoModificado, '%'))
            OR
            LOWER(a.valoresNuevos) LIKE LOWER(CONCAT('%', :campoModificado, '%'))
        )
    ORDER BY a.fechaHora DESC
""")
List<Auditoria> buscarAuditoriasConFiltros(
        @Param("productoId") Long productoId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("campoModificado") String campoModificado
);
}
